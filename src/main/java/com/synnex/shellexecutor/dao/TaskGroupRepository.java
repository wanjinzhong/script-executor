package com.synnex.shellexecutor.dao;

import com.synnex.shellexecutor.entity.TaskGroup;
import com.synnex.shellexecutor.enums.YorN;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskGroupRepository extends JpaRepository<TaskGroup, Integer> {
    List<TaskGroup> findByActive(YorN active);
}
