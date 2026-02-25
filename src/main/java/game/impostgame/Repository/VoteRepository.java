package game.impostgame.Repository;

import game.impostgame.Domain.Vote;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class VoteRepository {

    private final List<Vote> votes = new ArrayList<>();

    public void save(Vote vote) {
        votes.add(vote);
    }

    public List<Vote> findByRoomAndRound(UUID roomId, int round) {
        return votes.stream()
                .filter(v -> v.getRoomId().equals(roomId)
                        && v.getRoundNumber() == round)
                .collect(Collectors.toList());
    }

    public boolean existsVote(UUID roomId, int round, UUID voterId) {
        return votes.stream()
                .anyMatch(v -> v.getRoomId().equals(roomId)
                        && v.getRoundNumber() == round
                        && v.getVoterId().equals(voterId));
    }
}