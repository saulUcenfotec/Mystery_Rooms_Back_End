package com.project.mysteryRomms.controller;

import com.project.mysteryRomms.model.entity.GameRoom;
import com.project.mysteryRomms.repository.GameRoomRepository;
import com.project.mysteryRomms.service.GameRoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping({"/rooms", "/games"})
public class GameRoomController {
    private final GameRoomRepository gameRoomRepository;
    private final GameRoomService gameRoomService;

    public GameRoomController(GameRoomRepository gameRoomRepository, GameRoomService gameRoomService) {
        this.gameRoomRepository = gameRoomRepository;
        this.gameRoomService = gameRoomService;
    }

    @PostMapping
    public ResponseEntity<GameRoom> createRoom(@RequestBody GameRoom room) {
        if (room.getPlayerCount() == null) {
            room.setPlayerCount(0);
        }
        if (room.getMaxPlayers() == null) {
            room.setMaxPlayers(1);
        }
        if (room.getIsPrivate() == null) {
            room.setIsPrivate(false);
        }
        GameRoom saved = gameRoomRepository.save(room);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping
    public List<GameRoom> listRooms(
            @RequestParam(required = false) String roomName,
            @RequestParam(required = false) String difficulty,
            @RequestParam(required = false) Boolean isPrivate,
            @RequestParam(required = false) Integer minPlayers,
            @RequestParam(required = false) Integer maxPlayers,
            @RequestParam(required = false) Boolean hasSpace,
            @RequestParam(required = false) Boolean activeOnly) {
        return gameRoomService.findRooms(roomName, difficulty, isPrivate, minPlayers, maxPlayers, hasSpace, activeOnly);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameRoom> getRoom(@PathVariable Long id) {
        Optional<GameRoom> found = gameRoomRepository.findById(id);
        return found.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<GameRoom> patchRoom(@PathVariable Long id, @RequestBody GameRoom updates) {
        return updateRoomInternal(id, updates);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GameRoom> putRoom(@PathVariable Long id, @RequestBody GameRoom updates) {
        return updateRoomInternal(id, updates);
    }

    @PostMapping("/{id}")
    public ResponseEntity<GameRoom> postRoom(@PathVariable Long id, @RequestBody GameRoom updates) {
        return updateRoomInternal(id, updates);
    }

    private ResponseEntity<GameRoom> updateRoomInternal(Long id, GameRoom updates) {
        Optional<GameRoom> found = gameRoomRepository.findById(id);
        if (found.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        GameRoom existing = found.get();
        existing.setRoomName(updates.getRoomName());
        existing.setPlayerCount(updates.getPlayerCount());
        existing.setMaxPlayers(updates.getMaxPlayers());
        existing.setDifficulty(updates.getDifficulty());
        existing.setMaxTime(updates.getMaxTime());
        existing.setIsPrivate(updates.getIsPrivate());
        existing.setAccessCode(updates.getAccessCode());
        existing.setActivePlayers(updates.getActivePlayers());
        existing.setDescription(updates.getDescription());
        existing.setStatus(updates.getStatus());
        existing.setImgURL(updates.getImgURL());
        existing.setLastActivity(updates.getLastActivity());

        GameRoom saved = gameRoomRepository.save(existing);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        if (!gameRoomRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        gameRoomRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/empty")
    public ResponseEntity<Long> deleteEmptyRooms() {
        long deleted = gameRoomService.deleteEmptyRooms();
        return ResponseEntity.ok(deleted);
    }
}
