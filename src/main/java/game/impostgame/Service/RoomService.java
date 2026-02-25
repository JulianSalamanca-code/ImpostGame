package game.impostgame.Service;

import game.impostgame.Domain.Player;
import game.impostgame.Domain.Room;
import game.impostgame.Domain.RoomStatus;
import game.impostgame.Repository.RoomRepository;
import org.springframework.stereotype.Service;
import game.impostgame.Repository.RoomRepository;
import game.impostgame.Exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Room createRoom(String hostNickname, String category, int impostorCount) {

        if (hostNickname == null || hostNickname.isBlank())
            throw new BusinessException("Nickname inválido", HttpStatus.BAD_REQUEST);

        Room room = new Room();
        room.setId(UUID.randomUUID());
        room.setCode(UUID.randomUUID().toString().substring(0,6).toUpperCase());
        room.setStatus(RoomStatus.Lobby);
        room.setCategory(category);
        room.setImpostorCount(impostorCount);
        room.setCurrentRound(0);

        Player host = new Player();
        host.setId(UUID.randomUUID());
        host.setRoomId(room.getId());
        host.setNickname(hostNickname);

        room.setHostPlayerId(host.getId());
        room.getPlayers().add(host);

        roomRepository.save(room);

        return room;
    }

    public Player joinRoom(String code, String nickname) {

        Room room = getRoom(code);

        if (room.getStatus() != RoomStatus.Lobby)
            throw new BusinessException("La sala ya inició", HttpStatus.CONFLICT);

        Player player = new Player();
        player.setId(UUID.randomUUID());
        player.setRoomId(room.getId());
        player.setNickname(nickname);

        room.getPlayers().add(player);

        return player;
    }

    public Room getRoom(String code) {
        return roomRepository.findByCode(code)
                .orElseThrow(() ->
                        new BusinessException("Sala no encontrada", HttpStatus.NOT_FOUND));
    }
}