package com.creator_veera.Task.Management.App.controller;

import com.creator_veera.Task.Management.App.ENUM.Priority;
import com.creator_veera.Task.Management.App.ENUM.Status;
import com.creator_veera.Task.Management.App.model.Task;
import com.creator_veera.Task.Management.App.service.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/tasks")

public class Taskcontroller{

    private final TaskService taskService;

    public Taskcontroller(TaskService taskService) {
        this.taskService = taskService;
    }

    // General paginated listing (no filters)
    @GetMapping("/")
    public ResponseEntity<Page<Task>> getTaskByPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestHeader("X-User-Id") String userId  // NEW: get userId from frontend
    ) {
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Task> tasks = taskService.getTaskByPageForUser(userId, pageable); // NEW: filter by user
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<Task>> getTasks(
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) Priority priority,
            @RequestParam(required = false) LocalDate dueDate,
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestHeader("X-User-Id") String userId // NEW
    ) {
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Task> tasks = taskService.filterTaskForUser(userId, status, priority, dueDate, q, pageable); // NEW
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(@PathVariable int id, @RequestHeader("X-User-Id") String userId) {
        Task task = taskService.getTaskForUser(id, userId); // NEW: check task belongs to this user
        if (task == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(task);
    }

    @PostMapping("/")
    public ResponseEntity<Task> addTask(@RequestBody Task task, @RequestHeader("X-User-Id") String userId) {
        task.setUserId(userId); // NEW: assign userId to task
        Task newTask = taskService.addTask(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(newTask);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<Task>> addListOfTasks(@RequestBody List<Task> tasks, @RequestHeader("X-User-Id") String userId) {
        tasks.forEach(task -> task.setUserId(userId)); // NEW: assign userId to all tasks
        return ResponseEntity.ok(taskService.addListOfTasks(tasks));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable int id, @RequestBody Task task, @RequestHeader("X-User-Id") String userId) {
        task.setUserId(userId); // ensure task belongs to user
        Task updated = taskService.updateTaskForUser(id, task, userId); // NEW: update only if user matches
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable int id, @RequestHeader("X-User-Id") String userId) {
        taskService.deleteTaskForUser(id, userId); // NEW
        return ResponseEntity.ok("Task deleted successfully");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteTasks(@RequestBody List<Integer> ids, @RequestHeader("X-User-Id") String userId) {
        taskService.deleteTasksForUser(ids, userId); // NEW
        return ResponseEntity.ok("Selected tasks deleted successfully");
    }
}
