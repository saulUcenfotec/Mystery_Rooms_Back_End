package com.project.mysteryRomms.repository;

import com.project.mysteryRomms.model.entity.GameRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GameRoomRepository extends JpaRepository<GameRoom, Long>, JpaSpecificationExecutor<GameRoom> {
    long deleteByPlayerCountLessThanEqual(Integer playerCount);

    @Modifying
    @Query("delete from GameRoom g where g.playerCount is null or g.playerCount <= :count")
    int deleteEmptyOrBelow(@Param("count") int count);
}
