package com.airtribe.TaskTrackingAndManagement.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class TeamMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="team_id", nullable = false)
    private Team team; // the team to which the user belongs

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user; // the user who is a member of the team

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TeamRole role; // the role of the user in the team
}

enum TeamRole{
    OWNER, MEMBER
}
