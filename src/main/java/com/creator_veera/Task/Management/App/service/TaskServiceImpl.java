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
    public Page<Task> getTaskByPage(Pageable pageable) {
        return repo.findAll(pageable);
    }

    @Override
    public Page<Task> filterTask(Status status, Priority priority, LocalDate dueDate, String q, Pageable pageable) {
        Specification<Task> spec = TaskSpecification.buildSpecification(status, priority,dueDate,q);
        return repo.findAll(spec, pageable);
    }

    @Override
    public Task getTask(int id) {
        Optional<Task> opt = repo.findById(id);
        return opt.orElse(null);
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
    public Task updateTask(int id, Task task) {
        Optional<Task> opt = repo.findById(id);
        if (opt.isPresent()) {
            Task existing = opt.get();
            // copy fields (adjust based on your entity)
            existing.setTitle(task.getTitle());
            existing.setDescription(task.getDescription());
            existing.setStatus(task.getStatus());
            existing.setPriority(task.getPriority());
            existing.setDueDate(task.getDueDate());
            existing.setUpdatedAt(task.getUpdatedAt());
            return repo.save(existing);
        }
        return null;
    }

    @Override
    public void deleteTask(int id) {
        repo.deleteById(id);
    }

    @Override
    public void deleteTasks(List<Integer> ids) {
        List<Task> toDelete = repo.findAllById(ids);
        repo.deleteAll(toDelete);
    }
}

