package com.synnex.shellexecutor.dao;

import com.synnex.shellexecutor.entity.TaskHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskHistoryRepository extends JpaRepository<TaskHistory, Integer> {
    Page<TaskHistory> findByTaskIdOrderByDatetimeDesc(Integer taskId, Pageable page);
}
