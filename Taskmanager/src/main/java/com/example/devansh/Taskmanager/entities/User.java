package com.example.devansh.Taskmanager.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String timezone;

    @Column(nullable = false)
    private Boolean isActive;

    // One-to-Many relationship with Task
    @OneToMany(mappedBy = "assignedTo", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Task> tasks  = new ArrayList<>();

    public void assignTask(Task task) {
        task.setAssignedTo(this);
        this.tasks.add(task);
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getTimezone() {
        return timezone;
    }

    public Boolean getIsActive() {
        return isActive;
    }

//    public void setIsActive(Boolean isActive) {
//        this.isActive = isActive;
//    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
