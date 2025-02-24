package com.airtribe.TaskTrackingAndManagement.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User  implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String email;

    @ManyToMany(mappedBy = "members")
    @JsonIgnore
    private Set<Project> projects = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles !=null? roles.stream().map(role -> (GrantedAuthority) () -> "ROLE_"+ role).toList(): List.of();
    }
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="user_roles", joinColumns = @JoinColumn(name="user_id"))
    @Column(name="role")
    private Set<String> roles =new HashSet<>();;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.roles = new HashSet<>();
    }
    @OneToMany(mappedBy = "assignedTo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference // marks this as a managed side of the relationship
    private Set<Task> tasks = new HashSet<>();

}
