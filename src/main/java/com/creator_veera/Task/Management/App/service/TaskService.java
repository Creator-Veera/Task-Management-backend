package com.creator_veera.Task.Management.App.service;

import com.creator_veera.Task.Management.App.ENUM.Priority;
import com.creator_veera.Task.Management.App.ENUM.Status;
import com.creator_veera.Task.Management.App.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
// import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;

public interface TaskService {
    Page<Task> getTaskByPageForUser(String userId,Pageable pageable);
    Page<Task> filterTaskForUser(String userId,Status status, Priority priority, LocalDate dueDate, String q, Pageable pageable);
    Task getTaskForUser(int id,String userId);
    Task addTask(Task task);
    List<Task> addListOfTasks(List<Task> tasks);
    Task updateTaskForUser(int id, Task task,String userId);
    void deleteTaskForUser(int id,String userId);
    void deleteTasksForUser(List<Integer> ids,String userId);



}
