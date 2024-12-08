package com.example.devansh.Taskmanager.repositories;

import com.example.devansh.Taskmanager.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User,Long> {
}
