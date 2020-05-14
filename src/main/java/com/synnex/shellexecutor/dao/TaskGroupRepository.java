package com.synnex.shellexecutor.dao;

import com.synnex.shellexecutor.entity.TaskGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskGroupRepository extends JpaRepository<TaskGroup, Integer> {
}
