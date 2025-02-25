package com.airtribe.TaskTrackingAndManagement.repository;

import com.airtribe.TaskTrackingAndManagement.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByAssignedToId(Long userId);
    List<Task> findByTitleContainingOrDescriptionContaining(String title, String description);

    List<Task> findByAssignedToUsername(String username);
    List<Task> findByAssignedToUsernameAndCompleted(String userName, boolean completed);

    //search by title or description
    @Query("SELECT t FROM Task t WHERE LOWER(t.title) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(t.description) LIKE LOWER(CONCAT('%', :query,'%'))")
    List<Task> searchTasks(@Param("query") String query);
}
