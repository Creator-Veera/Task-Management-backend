package com.creator_veera.Task.Management.App.service;

import com.creator_veera.Task.Management.App.ENUM.Priority;
import com.creator_veera.Task.Management.App.ENUM.Status;
import com.creator_veera.Task.Management.App.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;

public interface TaskService {
    Page<Task> getTaskByPage(Pageable pageable);
    Page<Task> filterTask(Status status, Priority priority, LocalDate dueDate, String q, Pageable pageable);
    Task getTask(int id);
    Task addTask(Task task);
    List<Task> addListOfTasks(List<Task> tasks);
    Task updateTask(int id, Task task);
    void deleteTask(int id);
    void deleteTasks(List<Integer> ids);



}
