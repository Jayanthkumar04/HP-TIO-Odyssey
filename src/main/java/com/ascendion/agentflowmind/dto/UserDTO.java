package com.ascendion.agentflowmind.dto;

import lombok.Data;

@Data
public class UserDTO {
    private int userId;
    private String userName;
    private String email;
    private byte[] imageData;
    public UserDTO(int userId, String userName, String email, byte[] imageData) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.imageData = imageData;
    }
}
