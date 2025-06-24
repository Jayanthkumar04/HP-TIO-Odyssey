package com.ascendion.agentflowmind.repository;

import com.ascendion.agentflowmind.entity.UserTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTeamRepository extends JpaRepository<UserTeam, Long> {

    @Query("SELECT ut.teamId FROM UserTeam ut WHERE ut.userId = :userId")
    Integer findTeamIdByUserId(@Param("userId") Integer userId);
}
