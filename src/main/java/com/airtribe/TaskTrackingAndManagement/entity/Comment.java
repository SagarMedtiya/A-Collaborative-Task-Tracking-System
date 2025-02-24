package com.airtribe.TaskTrackingAndManagement.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name="comments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name="task_id", nullable=false)
    @JsonBackReference
    private Task task; // the task to which the comment belongs

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }

    @ManyToOne
    @JoinColumn(name ="user_id")
    private User user;
}
