package com.ascendion.agentflowmind.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ascendion.agentflowmind.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	Optional<User> findByUserEmail(String userEmail);

	Optional<User> findByUserEmailAndUserName(String email, String name);
}
