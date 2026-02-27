package game.impostgame.service;

import game.impostgame.domain.Player;
import game.impostgame.domain.Room;
import game.impostgame.domain.RoomStatus;
import game.impostgame.repository.RoomRepository;
import org.springframework.stereotype.Service;
import game.impostgame.exception.BusinessException;
import org.springframework.http.HttpStatus;
import game.impostgame.util.CodeGenerator;

import java.util.ArrayList;
import java.util.UUID;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Room createRoom(String hostNickname, String category, int impostorCount) {

        if (category == null || category.isBlank())
            throw new BusinessException("Categoría inválida", HttpStatus.BAD_REQUEST);

        Room room = new Room();
        room.setId(UUID.randomUUID());
        room.setCode(CodeGenerator.generateCode());
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

        System.out.println("Code recibido: " + code);
        System.out.println("Nickname recibido: " + nickname);

        Room room = getRoom(code);

        if (room.getStatus() != RoomStatus.Lobby)
            throw new BusinessException("La sala ya inició", HttpStatus.CONFLICT);

        Player player = new Player();
        player.setId(UUID.randomUUID());
        player.setRoomId(room.getId());
        player.setNickname(nickname);

        room.getPlayers().add(player);

        roomRepository.save(room);

        return player;
    }

    public Room getRoom(String code) {
        return roomRepository.findByCode(code)
                .orElseThrow(() ->
                        new BusinessException("Sala no encontrada", HttpStatus.NOT_FOUND));
    }
}