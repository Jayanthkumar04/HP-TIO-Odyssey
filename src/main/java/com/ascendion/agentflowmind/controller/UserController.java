package com.ascendion.agentflowmind.controller;

import com.ascendion.agentflowmind.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/login")
    public String login() {
        return "Welcome to the Agent Flow Mind";
    }

    @GetMapping("/getUserAttribs")
    public ResponseEntity<?> getUserDetails(@RequestHeader("Authorization") String authHeader) throws Exception {
        ResponseEntity<?> userDetails = userService.getUserDetailsFromToken(authHeader);
        return ResponseEntity.ok(userDetails);
    }

}
