package game.impostgame.repository;

import game.impostgame.domain.Assignment;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class AssignmentRepository {

    private final List<Assignment> assignments = new ArrayList<>();

    public void save(Assignment assignment) {
        assignments.add(assignment);
    }

    public List<Assignment> findByRoomId(UUID roomId) {
        return assignments.stream()
                .filter(a -> a.getRoomId().equals(roomId))
                .toList();
    }

    public Optional<Assignment> findByRoomAndPlayer(UUID roomId, UUID playerId) {
        return assignments.stream()
                .filter(a -> a.getRoomId().equals(roomId)
                        && a.getPlayerId().equals(playerId))
                .findFirst();
    }
}