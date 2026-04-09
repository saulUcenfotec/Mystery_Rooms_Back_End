package com.project.mysteryRomms.service;

import com.project.mysteryRomms.model.entity.GameRoom;
import com.project.mysteryRomms.repository.GameRoomRepository;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class GameRoomService {
    private final GameRoomRepository gameRoomRepository;

    public GameRoomService(GameRoomRepository gameRoomRepository) {
        this.gameRoomRepository = gameRoomRepository;
    }

    public List<GameRoom> findRooms(String roomName,
                                    String difficulty,
                                    Boolean isPrivate,
                                    Integer minPlayers,
                                    Integer maxPlayers,
                                    Boolean hasSpace,
                                    Boolean activeOnly) {
        Specification<GameRoom> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (roomName != null && !roomName.isBlank()) {
                Expression<String> field = cb.lower(root.get("roomName"));
                predicates.add(cb.like(field, "%" + roomName.toLowerCase() + "%"));
            }

            if (difficulty != null && !difficulty.isBlank()) {
                predicates.add(cb.equal(root.get("difficulty"), difficulty));
            }

            if (isPrivate != null) {
                predicates.add(cb.equal(root.get("isPrivate"), isPrivate));
            }

            if (minPlayers != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("playerCount"), minPlayers));
            }

            if (maxPlayers != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("playerCount"), maxPlayers));
            }

            if (hasSpace != null && hasSpace) {
                predicates.add(cb.lessThan(root.get("playerCount"), root.get("maxPlayers")));
            }

            if (activeOnly != null && activeOnly) {
                predicates.add(cb.greaterThan(root.get("playerCount"), 0));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Sort sort = Sort.by(Sort.Direction.DESC, "lastActivity", "createdAt", "id");
        return gameRoomRepository.findAll(spec, sort);
    }

    @Transactional
    public long deleteEmptyRooms() {
        return gameRoomRepository.deleteEmptyOrBelow(0);
    }
}
