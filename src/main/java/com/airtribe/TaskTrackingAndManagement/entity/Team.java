package com.airtribe.TaskTrackingAndManagement.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name="owner_id", nullable = false)
    private User owner; //The user who created the team

    @OneToMany(mappedBy= "team", cascade = CascadeType.ALL, fetch= FetchType.LAZY)
    private Set<TeamMember> members;
}
