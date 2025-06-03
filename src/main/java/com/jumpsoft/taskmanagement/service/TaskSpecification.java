package  com.jumpsoft.taskmanagement.service;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.jumpsoft.taskmanagement.enums.TaskStatus;
import com.jumpsoft.taskmanagement.entity.Task;

/**
 * Specification class for filtering tasks based on various criteria.
 * Provides methods to create specifications for task status and user ID.
 */
@Component
public class TaskSpecification {

    public static Specification<Task> withStatus(TaskStatus status) {
        return (root, query, cb) -> {
            if (status == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("status"), status);
        };
    }

    public static Specification<Task> withUserId(Long userId) {
        return (root, query, cb) -> {
            if (userId == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("user").get("id"), userId);
        };
    }
}
