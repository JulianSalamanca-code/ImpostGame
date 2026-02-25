package game.impostgame.Domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class Room {

    private UUID id;
    private String name;
    private String code;
    private RoomStatus status;
    private UUID hostPlayerId;
    private String category;
    private int impostorCount;
    private int currentRound;
    private String secretWord;
    private String winner;

    private List<Player> players = new ArrayList<>();

}
