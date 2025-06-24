package com.ascendion.agentflowmind.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/agent/monitoring")
public class AgentController {

    @GetMapping("/status/{agentId}")
    public String getAgentStatus(@PathVariable Long agentId) {
        return null;
    }
}
