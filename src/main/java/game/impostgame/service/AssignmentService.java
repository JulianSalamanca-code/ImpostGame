package game.impostgame.service;

import game.impostgame.domain.Assignment;
import game.impostgame.repository.AssignmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AssignmentService {

    private final AssignmentRepository repository;

    public AssignmentService(AssignmentRepository repository) {
        this.repository = repository;
    }

    public void save(Assignment assignment) {
        repository.save(assignment);
    }

    public Assignment getByRoomAndPlayer(UUID roomId, UUID playerId) {
        return repository.findByRoomAndPlayer(roomId, playerId)
                .orElseThrow(() -> new RuntimeException("Asignación no encontrada"));
    }

    public List<Assignment> getByRoom(UUID roomId) {
        return repository.findByRoomId(roomId);
    }
}