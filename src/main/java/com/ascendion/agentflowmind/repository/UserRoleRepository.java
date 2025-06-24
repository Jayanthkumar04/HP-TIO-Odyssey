package com.ascendion.agentflowmind.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ascendion.agentflowmind.entity.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, Integer>{

	Optional<UserRole> findByUserId(Integer userId);

}
