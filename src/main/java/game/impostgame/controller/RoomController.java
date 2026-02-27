package game.impostgame.controller;

import game.impostgame.domain.*;
import game.impostgame.service.*;
import game.impostgame.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;
    private final GameService gameService;
    private final VoteService voteService;
    private final AssignmentService assignmentService;

    public RoomController(RoomService roomService,
                          GameService gameService,
                          VoteService voteService,
                          AssignmentService assignmentService) {
        this.roomService = roomService;
        this.gameService = gameService;
        this.voteService = voteService;
        this.assignmentService = assignmentService;
    }

    @PostMapping
    public ResponseEntity<?> createRoom(@RequestBody CreateRoomRequest request) {

        Room room = roomService.createRoom(
                request.getHostNickname(),
                request.getCategory(),
                request.getImpostorCount()
        );

        return ResponseEntity.ok(Map.of(
                "roomCode", room.getCode(),
                "hostPlayerId", room.getHostPlayerId()
        ));
    }

    @PostMapping("/{code}/players")
    public ResponseEntity<?> joinRoom(
            @PathVariable String code,
            @RequestBody JoinRoomRequest request) {

        Player player = roomService.joinRoom(code, request.getNickname());

        return ResponseEntity.ok(Map.of(
                "playerId", player.getId(),
                "nickname", player.getNickname()
        ));


    }

    @GetMapping("/{code}")
    public ResponseEntity<?> getRoom(@PathVariable String code) {
        return ResponseEntity.ok(roomService.getRoom(code));
    }

    @PostMapping("/{code}/start")
    public ResponseEntity<?> startGame(
            @PathVariable String code,
            @RequestParam UUID hostPlayerId) {

        gameService.startGame(code, hostPlayerId);

        return ResponseEntity.ok(Map.of(
                "status", "IN_GAME"
        ));
    }

    @GetMapping("/{code}/me")
    public ResponseEntity<?> getMyRole(
            @PathVariable String code,
            @RequestParam UUID playerId) {

        Room room = roomService.getRoom(code);

        Assignment assignment = assignmentService
                .getByRoomAndPlayer(room.getId(), playerId);

        return ResponseEntity.ok(Map.of(
                "role", assignment.getRole(),
                "word", assignment.getWord()
        ));
    }

    @PostMapping("/{code}/votes")
    public ResponseEntity<?> vote(
            @PathVariable String code,
            @RequestParam UUID voterId,
            @RequestBody Map<String, UUID> body) {

        voteService.registerVote(code, voterId, body.get("votedId"));

        return ResponseEntity.ok(Map.of(
                "message", "Voto registrado"
        ));
    }

    @PostMapping("/{code}/round/close")
    public ResponseEntity<?> closeRound(
            @PathVariable String code,
            @RequestParam UUID hostPlayerId) {

        return ResponseEntity.ok(
                gameService.closeRound(code, hostPlayerId)
        );
    }
}