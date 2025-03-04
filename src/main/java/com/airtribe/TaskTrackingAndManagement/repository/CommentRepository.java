package com.airtribe.TaskTrackingAndManagement.repository;

import com.airtribe.TaskTrackingAndManagement.entity.Comment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Long> {
    List<Comment> findByTaskId(Long taskId);
}
