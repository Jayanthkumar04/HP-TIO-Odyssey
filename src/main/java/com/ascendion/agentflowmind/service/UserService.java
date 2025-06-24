package com.ascendion.agentflowmind.service;

import java.util.Map;
import java.util.Optional;

import com.ascendion.agentflowmind.repository.UserTeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import com.ascendion.agentflowmind.entity.Role;
import com.ascendion.agentflowmind.entity.User;
import com.ascendion.agentflowmind.entity.UserRole;
import com.ascendion.agentflowmind.repository.RoleMasterRepo;
import com.ascendion.agentflowmind.repository.UserRepository;
import com.ascendion.agentflowmind.repository.UserRoleRepository;
import com.ascendion.agentflowmind.util.JwtDecoder;

@Service
public class UserService {

//    @Value("${azure.client-id}")
//    private String clientId;
//
//    @Value("${azure.client-secret}")
//    private String clientSecret;
//
//    @Value("${azure.tenant-id}")
//    private String tenantId;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepo;

    @Autowired
    private RoleMasterRepo roleRepo;

    @Autowired
    private UserTeamRepository userTeamRepository;

    private Map<String, Object> extractEmailFromJwt(String jwtToken) throws Exception {
        String token = jwtToken.replace("Bearer ", "");
        return JwtDecoder.decodePayload(token);
    }

    public ResponseEntity<?> getUserDetailsFromToken(String authHeader) {
        try {
            Map<String, Object> claims = extractEmailFromJwt(authHeader);
            String email = (String) claims.get("unique_name");
            String name = (String) claims.get("name");
            String userDesignation = "";
            Optional<User> userOpt = userRepository.findByUserEmailAndUserName(email, name);
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(404).body("User Not Found");
            }
            User user = userOpt.get();
            Optional<UserRole> userRole = userRoleRepo.findByUserId(user.getUserId());
            if (!userRole.isEmpty()) {
                long roleId = userRole.get().getRoleId();
                Optional<Role> roleDetails = roleRepo.findById(roleId);
                if (roleDetails.isPresent()) {
                    userDesignation = roleDetails.get().getRoleName();
                }
            } else {
                return ResponseEntity.status(404).body("User Role Not Found");
            }
            int teamId = userTeamRepository.findTeamIdByUserId(user.getUserId());
            return ResponseEntity.ok(Map.of("userId", user.getUserId(), "userName", user.getUserName(), "userEmail",
                    user.getUserEmail(), "designation", userDesignation, "teamId",teamId));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid token : " + e.getMessage());
        }
    }
}