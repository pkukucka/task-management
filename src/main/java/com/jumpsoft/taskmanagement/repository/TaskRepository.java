package com.jumpsoft.taskmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jumpsoft.taskmanagement.entity.Task;
import com.jumpsoft.taskmanagement.entity.User;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task>
{
    // This interface will automatically provide CRUD operations and support for JPA specifications.
    // Additional custom query methods can be defined here if needed.

    /**
     *
     * @param oldUserId
     * @param newUserId
     * @return
     */
    @Modifying
    @Query("UPDATE Task t SET t.user.id = :newUserId WHERE t.user.id = :oldUserId")
    int updateUserIdInTasks(@Param("oldUserId") Long oldUserId, @Param("newUserId") Long newUserId);


}
