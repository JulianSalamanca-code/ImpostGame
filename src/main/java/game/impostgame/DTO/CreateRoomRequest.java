package game.impostgame.DTO;

import lombok.Data;

@Data
public class CreateRoomRequest {
    private String hostNickname;
    private String category;
    private int impostorCount = 1;
}