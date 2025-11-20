package com.creator_veera.Task.Management.App.service;

import com.creator_veera.Task.Management.App.model.Task;
import com.creator_veera.Task.Management.App.repo.TaskRepo;
import com.creator_veera.Task.Management.App.ENUM.Priority;
import com.creator_veera.Task.Management.App.ENUM.Status;
import com.creator_veera.Task.Management.App.repo.TaskSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service

public class TaskServiceImpl implements TaskService{

    public final TaskRepo repo;
    @Autowired
    public TaskServiceImpl(TaskRepo repo) {
        this.repo = repo;
    }
    @Override
     public Page<Task> getTaskByPageForUser(String userId, Pageable pageable) {
        return repo.findAll(TaskSpecification.buildSpecification(userId, null, null, null, null), pageable);
    }

    @Override
    public Page<Task> filterTaskForUser(String userId, Status status, Priority priority, LocalDate dueDate, String q, Pageable pageable) {
        return repo.findAll(TaskSpecification.buildSpecification(userId, status, priority, dueDate, q), pageable);
    }


    @Override
    public Task addTask(Task task) {
        return repo.save(task);
    }

    @Override
    public List<Task> addListOfTasks(List<Task> tasks) {
        return repo.saveAll(tasks);
    }


    @Override
    public void deleteTasksForUser(List<Integer> ids, String userId) {
        ids.forEach(id -> deleteTaskForUser(id, userId));
    }
    @Override
    public Task getTaskForUser(int id, String userId) {
    return repo.findOne(TaskSpecification.buildSpecification(userId, null, null, null, null)
                .and((root, query, cb) -> cb.equal(root.get("id"), id)))
                .orElse(null);    
    }
    @Override
    public Task updateTaskForUser(int id, Task task, String userId) {
        Task existing = getTaskForUser(id, userId);
        if (existing == null) return null;
        existing.setTitle(task.getTitle());
        existing.setDescription(task.getDescription());
        existing.setStatus(task.getStatus());
        existing.setPriority(task.getPriority());
        existing.setDueDate(task.getDueDate());
        return repo.save(existing);
    }
    @Override
    public void deleteTaskForUser(int id, String userId) {
        Task existing = getTaskForUser(id, userId);
        if (existing != null) repo.delete(existing);
    }

    
}

