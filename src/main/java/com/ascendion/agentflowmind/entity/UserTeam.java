package com.ascendion.agentflowmind.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "UserTeam")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userTeam_Id")
    private Integer userTeamId;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "team_id")
    private int teamId;
}
