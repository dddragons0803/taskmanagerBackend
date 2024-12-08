package com.example.devansh.Taskmanager.services;

import com.example.devansh.Taskmanager.payloads.TaskDto;
import com.example.devansh.Taskmanager.payloads.TaskResponse;

import java.util.List;

public interface TaskService {
    TaskDto createTask(TaskDto taskDto);
    TaskResponse getAllTasks(Integer pageNumber , Integer pageSize, String sortBy, String sortDir);
    TaskDto getTaskById(Long id);
    TaskDto updateTask(Long id, TaskDto taskDto);
    void deleteTask(Long id);
    List<TaskDto> getTasksByUser(Long id);
    List<TaskDto> searchTask(String keyword);
    public List<TaskDto> getTasksByFilter( String status);
}
