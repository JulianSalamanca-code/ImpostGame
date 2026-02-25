package game.impostgame.Domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vote {

    private UUID roomId;
    private int roundNumber;
    private UUID voterId;
    private UUID votedId;
}
