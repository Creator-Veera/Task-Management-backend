package com.creator_veera.Task.Management.App.repo;

import com.creator_veera.Task.Management.App.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface TaskRepo extends JpaRepository<Task, Integer>, JpaSpecificationExecutor<Task> {
}
