package game.impostgame.service;

import game.impostgame.domain.*;
import game.impostgame.repository.*;
import game.impostgame.util.WordBank;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GameService {

    private final RoomRepository roomRepository;
    private final AssignmentRepository assignmentRepository;
    private final VoteRepository voteRepository;
    private final WordBank wordBank;

    public GameService(RoomRepository roomRepository,
                       AssignmentRepository assignmentRepository,
                       VoteRepository voteRepository,
                       WordBank wordBank) {
        this.roomRepository = roomRepository;
        this.assignmentRepository = assignmentRepository;
        this.voteRepository = voteRepository;
        this.wordBank = wordBank;
    }

    public void startGame(String code, UUID hostId) {

        Room room = roomRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Sala no encontrada"));

        if (!room.getHostPlayerId().equals(hostId))
            throw new RuntimeException("Solo el host puede iniciar");

        if (room.getStatus() != RoomStatus.Lobby)
            throw new RuntimeException("La sala ya inició");

        long alivePlayers = room.getPlayers().stream().filter(Player::isAlive).count();

        if (alivePlayers < 3)
            throw new RuntimeException("Mínimo 3 jugadores");

        String secretWord = wordBank.getRandomWord(room.getCategory());
        room.setSecretWord(secretWord);
        room.setStatus(RoomStatus.In_Game);
        room.setCurrentRound(1);

        List<Player> players = new ArrayList<>(room.getPlayers());
        Collections.shuffle(players);

        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);

            if (i < room.getImpostorCount()) {
                assignmentRepository.save(
                        new Assignment(room.getId(), p.getId(), Role.Impostor, null));
            } else {
                assignmentRepository.save(
                        new Assignment(room.getId(), p.getId(), Role.Civil, secretWord));
            }
        }
    }

    public Map<String, Object> closeRound(String code, UUID hostId) {

        Room room = roomRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Sala no encontrada"));

        if (!room.getHostPlayerId().equals(hostId))
            throw new RuntimeException("Solo el host puede cerrar ronda");

        List<Vote> votes = voteRepository
                .findByRoomAndRound(room.getId(), room.getCurrentRound());

        if (votes.isEmpty())
            throw new RuntimeException("No hay votos");

        Map<UUID, Long> count = votes.stream()
                .collect(Collectors.groupingBy(Vote::getVotedId, Collectors.counting()));

        long max = Collections.max(count.values());

        List<UUID> tied = count.entrySet().stream()
                .filter(e -> e.getValue() == max)
                .map(Map.Entry::getKey)
                .toList();

        if (tied.size() > 1) {
            room.setCurrentRound(room.getCurrentRound() + 1);
            return Map.of("status", "Empate - nadie expulsado");
        }

        UUID expelledId = tied.get(0);

        Player expelled = room.getPlayers().stream()
                .filter(p -> p.getId().equals(expelledId))
                .findFirst()
                .orElseThrow();

        expelled.setAlive(false);

        Assignment assignment = assignmentRepository
                .findByRoomAndPlayer(room.getId(), expelledId)
                .orElseThrow();

        if (assignment.getRole() == Role.Impostor) {
            room.setStatus(RoomStatus.Finished);
            room.setWinner("CIVILES");
            return Map.of("winner", "CIVILES");
        }

        long alive = room.getPlayers().stream().filter(Player::isAlive).count();

        boolean impostorAlive = assignmentRepository.findByRoomId(room.getId())
                .stream()
                .anyMatch(a -> a.getRole() == Role.Impostor &&
                        room.getPlayers().stream()
                                .anyMatch(p -> p.getId().equals(a.getPlayerId()) && p.isAlive()));

        if (alive == 2 && impostorAlive) {
            room.setStatus(RoomStatus.Finished);
            room.setWinner("IMPOSTORES");
            return Map.of("winner", "IMPOSTORES");
        }

        room.setCurrentRound(room.getCurrentRound() + 1);

        return Map.of("status", "Siguiente ronda",
                "nextRound", room.getCurrentRound());
    }
}