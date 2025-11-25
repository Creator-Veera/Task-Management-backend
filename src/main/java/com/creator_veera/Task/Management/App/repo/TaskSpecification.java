package com.creator_veera.Task.Management.App.repo;


import com.creator_veera.Task.Management.App.ENUM.Priority;
import com.creator_veera.Task.Management.App.ENUM.Status;
import com.creator_veera.Task.Management.App.model.Task;
// import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TaskSpecification {

    public static Specification<Task> buildSpecification(
            String userId,
            Status status,
            Priority priority,
            LocalDate dueDate,
            String q
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (userId != null && !userId.isEmpty()) {
                predicates.add(cb.equal(root.get("userId"), userId));
            }
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            if (priority != null) {
                predicates.add(cb.equal(root.get("priority"), priority));
            }
            if (dueDate != null) {
                predicates.add(cb.equal(root.get("dueDate"), dueDate));
            }
            if (q != null && !q.isEmpty()) {
                predicates.add(cb.or(
                cb.like(cb.lower(root.get("title")), "%" + q.toLowerCase() + "%"),
                cb.like(cb.lower(root.get("description")), "%" + q.toLowerCase() + "%")
                ));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
