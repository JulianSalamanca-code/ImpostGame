package game.impostgame.service;

import game.impostgame.domain.*;
import game.impostgame.repository.*;
import game.impostgame.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class VoteService {

    private final RoomRepository roomRepository;
    private final VoteRepository voteRepository;

    public VoteService(RoomRepository roomRepository,
                       VoteRepository voteRepository) {
        this.roomRepository = roomRepository;
        this.voteRepository = voteRepository;
    }

    public void registerVote(String code, UUID voterId, UUID votedId) {

        Room room = roomRepository.findByCode(code)
                .orElseThrow(() ->
                        new BusinessException("Sala no encontrada", HttpStatus.NOT_FOUND));

        if (room.getStatus() != RoomStatus.In_Game)
            throw new BusinessException("La sala no está en juego", HttpStatus.CONFLICT);

        Player voter = room.getPlayers().stream()
                .filter(p -> p.getId().equals(voterId))
                .findFirst()
                .orElseThrow(() ->
                        new BusinessException("Jugador no pertenece a sala", HttpStatus.NOT_FOUND));

        if (!voter.isAlive())
            throw new BusinessException("Jugador muerto no puede votar", HttpStatus.CONFLICT);

        Player voted = room.getPlayers().stream()
                .filter(p -> p.getId().equals(votedId))
                .findFirst()
                .orElseThrow(() ->
                        new BusinessException("Jugador votado no existe", HttpStatus.NOT_FOUND));

        if (!voted.isAlive())
            throw new BusinessException("No se puede votar por muerto", HttpStatus.CONFLICT);

        if (voteRepository.existsVote(room.getId(), room.getCurrentRound(), voterId))
            throw new BusinessException("Ya votó en esta ronda", HttpStatus.CONFLICT);

        voteRepository.save(new Vote(
                room.getId(),
                room.getCurrentRound(),
                voterId,
                votedId
        ));
    }
}