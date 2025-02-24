package com.airtribe.TaskTrackingAndManagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Column(nullable = false)
    private String description;
    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(
            name="project_members",
            joinColumns = @JoinColumn(name="project_id"),
            inverseJoinColumns = @JoinColumn(name ="user_id")
    )
    @JsonManagedReference
    private Set<User> members = new HashSet<>();;

    @OneToMany(mappedBy = "project")
    @JsonIgnore
    private Set<Task> tasks;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;// the user who created the project


}
