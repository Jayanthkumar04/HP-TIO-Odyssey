package com.ascendion.agentflowmind.repository;

import com.ascendion.agentflowmind.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleMasterRepo extends JpaRepository<Role, Long> {
}
