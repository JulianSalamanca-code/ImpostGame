package game.impostgame.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Room {

    @Id
    private UUID id;

    private String name;
    private String code;

    @Enumerated(EnumType.STRING)
    private RoomStatus status;

    private UUID hostPlayerId;
    private String category;
    private int impostorCount;
    private int currentRound;
    private String secretWord;
    private String winner;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Player> players = new ArrayList<>();
}