package com.example.devansh.Taskmanager.controllers;


import com.example.devansh.Taskmanager.payloads.ApiResponse;
import com.example.devansh.Taskmanager.payloads.TaskDto;
import com.example.devansh.Taskmanager.payloads.TaskResponse;
import com.example.devansh.Taskmanager.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;


    @PostMapping("/")
    public ResponseEntity<TaskDto> createTask(@RequestBody TaskDto taskDto) {
        TaskDto createdTask = this.taskService.createTask(taskDto);
        return new ResponseEntity<TaskDto>(createdTask, HttpStatus.CREATED);
    }
    @GetMapping("/")
    public  ResponseEntity<TaskResponse> getAllTasks(@RequestParam(value = "pageNumber", defaultValue = "0",required = false) Integer pageNumber,
                                                     @RequestParam(value = "pageSize", defaultValue = "5",required = false) Integer pageSize,
                                                     @RequestParam(value = "sortBy", defaultValue = "title",required = false) String sortBy,
                                                     @RequestParam(value = "sortDir" , defaultValue = "asc",required = false) String sortDir) {
        TaskResponse taskResponse = this.taskService.getAllTasks(pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<TaskResponse>(taskResponse,HttpStatus.OK);
    }
    @GetMapping("/filter")
    public ResponseEntity<List<TaskDto>> getTasksByFilter(
            @RequestParam(value = "status", required = false) String status) {

        List<TaskDto> tasks = taskService.getTasksByFilter(status);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable Long id)
    {
        TaskDto taskDto = this.taskService.getTaskById(id);
        return new ResponseEntity<TaskDto>(taskDto,HttpStatus.OK);

    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDto> updateTask(@PathVariable Long id, @RequestBody TaskDto taskDto) {
        TaskDto updatedTask = this.taskService.updateTask(id, taskDto);
        return new  ResponseEntity<TaskDto>(updatedTask,HttpStatus.OK);
    }
    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<ApiResponse> deleteTask(@PathVariable Long id) {
        this.taskService.deleteTask(id);
        return ResponseEntity.ok(new ApiResponse("deleted", true));

    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TaskDto>> getPostsByUser(@PathVariable Long userId){
        List<TaskDto> tasks = this.taskService.getTasksByUser(userId);
        return new ResponseEntity<List<TaskDto>>(tasks,HttpStatus.OK);
    }
    @GetMapping("/search/{keywords}")
    public ResponseEntity<List<TaskDto>> searchPostByTitle(@PathVariable("keywords") String keywords){
        List<TaskDto> result = this.taskService.searchTask(keywords);
        return new ResponseEntity<List<TaskDto>>(result,HttpStatus.OK);
    }

}