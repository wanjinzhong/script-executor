package com.synnex.shellexecutor.service;

import com.synnex.shellexecutor.bo.GroupBo;
import com.synnex.shellexecutor.entity.Task;

import java.util.List;
import java.util.Optional;

public interface CommonService {
    List<GroupBo> getGroups();

    Optional<Task> getTaskById(Integer id);

    String updateTaskLatestRunTime(Integer id);
}
