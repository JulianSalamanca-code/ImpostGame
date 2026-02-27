package game.impostgame.domain;

import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class Assignment {
    private UUID roomId;
    private UUID playerId;
    private Role role;
    private String word;

}
