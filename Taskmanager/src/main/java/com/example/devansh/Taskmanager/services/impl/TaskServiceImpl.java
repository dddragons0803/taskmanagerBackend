package com.example.devansh.Taskmanager.services.impl;


import com.example.devansh.Taskmanager.entities.Task;
import com.example.devansh.Taskmanager.entities.User;
import com.example.devansh.Taskmanager.exceptions.ResourceNotFoundException;
import com.example.devansh.Taskmanager.payloads.TaskDto;

import com.example.devansh.Taskmanager.payloads.TaskResponse;
import com.example.devansh.Taskmanager.repositories.TaskRepo;
import com.example.devansh.Taskmanager.repositories.UserRepo;
import com.example.devansh.Taskmanager.services.TaskService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    private TaskRepo taskRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ModelMapper modelMapper;
    private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);

    @Override
    public TaskDto createTask(TaskDto taskDto) {

        User assignedUser = userRepo.findById(taskDto.getAssignedToId())
                .orElseThrow(() -> new ResourceNotFoundException("User: " ,"userId",taskDto.getAssignedToId()));
        if (!assignedUser.getIsActive()) {
            throw new IllegalArgumentException("Cannot assign task to an inactive user.");
        }

        Task task = this.dtoToTask(taskDto, assignedUser);
        task.setAssignedTo(assignedUser);
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        Task savedTask = this.taskRepo.save(task);
        TaskDto responseDto = modelMapper.map(savedTask, TaskDto.class);
        responseDto.setAssignedToId(assignedUser.getId());

        return responseDto;
    }

    @Override
    public TaskResponse getAllTasks(Integer pageNumber , Integer pageSize, String sortBy, String sortDir) {
        Sort sort = null;
        if(sortDir.equalsIgnoreCase("asc"))
        {
            sort = Sort.by(sortBy).ascending();
        }
        else{
            sort = Sort.by(sortBy).descending();
        }

        Pageable p = PageRequest.of(pageNumber,pageSize,sort);
        Page<Task> pageTask = this.taskRepo.findAll(p);
        List<Task> allTasks = pageTask.getContent();
//        List<Task> alltaskforcontent = this.taskRepo.findAll();
        List<TaskDto> taskDtos= allTasks.stream()
                .map(task -> this.modelMapper.map(task, TaskDto.class))
                .collect(Collectors.toList());
        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setContent(taskDtos);
        taskResponse.setPageNumber(pageTask.getNumber());
        taskResponse.setPageSize(pageTask.getSize());
        taskResponse.setTotalElements(pageTask.getTotalElements());
        taskResponse.setTotalPages(pageTask.getTotalPages());
        taskResponse.setLastPage(pageTask.isLast());
        logger.info("This is an info log");
        return taskResponse;

    }

    @Override
    public TaskDto getTaskById(Long id) {
        Task task = this.taskRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("task","taskId", id));
        return modelMapper.map(task, TaskDto.class);
    }

    @Override
    public TaskDto updateTask(Long id, TaskDto taskDto) {
        // Fetch the existing task
        Task existingTask = taskRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("task: " ,"taskId",id));
        User assignedUser = userRepo.findById(taskDto.getAssignedToId())
                .orElseThrow(() -> new ResourceNotFoundException("User  " ,"userid", taskDto.getAssignedToId()));
        if (!assignedUser.getIsActive()) {
            throw new IllegalArgumentException("Cannot assign task to an inactive user.");
        }
        Task task = taskRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task " ,"taskId", id));

        // Update fields
        existingTask.setTitle(taskDto.getTitle());
        existingTask.setDescription(taskDto.getDescription());
        existingTask.setStatus(taskDto.getStatus());
        existingTask.setAssignedTo(assignedUser);
        existingTask.setUpdatedAt(LocalDateTime.now());

        Task updatedTask = taskRepo.save(existingTask);
        TaskDto responseDto = modelMapper.map(updatedTask, TaskDto.class);
        responseDto.setAssignedToId(assignedUser.getId());

        return responseDto;
    }

    @Override
    public void deleteTask(Long id) {
        Task task = taskRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "taskId", id));
        this.taskRepo.delete(task);
    }
    @Override
    public List<TaskDto> getTasksByUser(Long id) {
        User user = this.userRepo.findById(id).
                orElseThrow(()->new ResourceNotFoundException("user","user id",id));
        List<Task> tasks = this.taskRepo.findByAssignedTo(user);
        List<TaskDto> taskDtos = tasks.stream().map(task-> this.modelMapper.map(task,TaskDto.class)).collect(Collectors.toList());
        return taskDtos;
    }

    @Override
    public List<TaskDto> searchTask(String keyword) {
        logger.info("This1");
        List<Task> tasks =  this.taskRepo.searchByTitle("%" + keyword + "%");
        logger.info("This2");
        List<TaskDto> taskDtos = tasks.stream().map(task -> this.modelMapper.map(task,TaskDto.class)).collect(Collectors.toList());
        logger.info("This3");
        return taskDtos;
    }

    @Override
    public List<TaskDto> getTasksByFilter( String status) {

        List<Task> tasks;
        if (status != null) {
            tasks = taskRepo.findByStatus(status);
        } else {
            tasks = taskRepo.findAll();  // Get all tasks if no filters are provided
        }

        return tasks.stream().map(task -> this.modelMapper.map(task,TaskDto.class)).collect(Collectors.toList());
    }

    public Task dtoToTask(TaskDto taskDto, User assignedUser) {
        // Map the basic fields
        Task task = this.modelMapper.map(taskDto, Task.class);

        // Set the assigned user
        if (assignedUser != null) {
            task.setAssignedTo(assignedUser);
        } else {
            throw new IllegalArgumentException("Assigned user cannot be null");
        }
        return task;
    }
    public TaskDto taskToDto(Task task) {

        TaskDto taskDto = new TaskDto();

        taskDto.setId(task.getId());
        taskDto.setTitle(task.getTitle());
        taskDto.setDescription(task.getDescription());
        taskDto.setStatus(task.getStatus());
        taskDto.setAssignedToId(task.getAssignedTo().getId());
        taskDto.setCreatedAt(task.getCreatedAt());
        taskDto.setUpdatedAt(task.getUpdatedAt());
        return taskDto;
    }
}
