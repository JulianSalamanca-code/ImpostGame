package game.impostgame.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity

public class Player {

    @Id
    private UUID id;
    private UUID roomId;
    private String nickname;
    private boolean alive = true;

}
