package com.creator_veera.Task.Management.App.repo;


import com.creator_veera.Task.Management.App.ENUM.Priority;
import com.creator_veera.Task.Management.App.ENUM.Status;
import com.creator_veera.Task.Management.App.model.Task;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class TaskSpecification {

    public static Specification<Task> buildSpecification(
            Status status,
            Priority priority,
            LocalDate dueDate,
            String q
    ) {
        return (root, query, cb) -> {
            Predicate finalPredicate = cb.conjunction();

            if (status != null) {
                finalPredicate = cb.and(finalPredicate, cb.equal(root.get("status"), status));
            }

            if (priority != null) {
                finalPredicate = cb.and(finalPredicate, cb.equal(root.get("priority"), priority));
            }

            if (dueDate != null) {
                finalPredicate = cb.and(finalPredicate, cb.equal(root.get("dueDate"), dueDate));
            }

            if (q != null && !q.isBlank()) {
                String likePattern = "%" + q.toLowerCase() + "%";
                Expression<String> titleExp = cb.lower(root.get("title"));
                Expression<String> descExp = cb.lower(root.get("description"));
                Predicate titleLike = cb.like(titleExp, likePattern);
                Predicate descLike = cb.like(descExp, likePattern);
                finalPredicate = cb.and(finalPredicate, cb.or(titleLike, descLike));
            }

            return finalPredicate;
        };
    }
}
