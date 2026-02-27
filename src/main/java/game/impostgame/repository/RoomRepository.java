package game.impostgame.repository;

import game.impostgame.domain.Room;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class RoomRepository {
    private final Map<String, Room> rooms = new HashMap<>();

    public void save(Room room) {
        rooms.put(room.getCode(), room);
    }

    public Optional<Room> findByCode(String code){
        return Optional.ofNullable(rooms.get(code));
    }
}
