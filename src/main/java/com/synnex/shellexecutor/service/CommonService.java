package com.synnex.shellexecutor.service;

import com.synnex.shellexecutor.bo.GroupBo;
import com.synnex.shellexecutor.bo.Pageable;
import com.synnex.shellexecutor.bo.RunRequest;
import com.synnex.shellexecutor.bo.TaskHistoryBo;
import com.synnex.shellexecutor.entity.Task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CommonService {
    List<GroupBo> getGroups();

    Optional<Task> getTaskById(Integer id);

    LocalDateTime updateTaskLatestRunTimeAndSaveHistory(RunRequest request, String remoteAddr);

    Pageable<TaskHistoryBo> getTaskHistories(Integer taskId, String remoteAddr, Integer page, Integer size);

    void refreshIpTable();
}
