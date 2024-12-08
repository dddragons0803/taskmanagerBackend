package com.example.devansh.Taskmanager.repositories;

import com.example.devansh.Taskmanager.entities.Task;
import com.example.devansh.Taskmanager.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepo extends JpaRepository<Task,Long> {
List<Task> findByAssignedTo(User assignedTo);

List<Task> findByStatus(String status);


@Query("select t from Task t where t.title LIKE :key")
List<Task> searchByTitle(@Param("key") String title);
}
