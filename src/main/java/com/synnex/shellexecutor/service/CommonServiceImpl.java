package com.synnex.shellexecutor.service;

import com.synnex.shellexecutor.bo.GroupBo;
import com.synnex.shellexecutor.constants.CommonConstants;
import com.synnex.shellexecutor.dao.TaskGroupRepository;
import com.synnex.shellexecutor.dao.TaskRepository;
import com.synnex.shellexecutor.entity.Task;
import com.synnex.shellexecutor.enums.YorN;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommonServiceImpl implements CommonService{

    @Autowired
    private TaskGroupRepository groupRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Override
    public List<GroupBo> getGroups() {
        return groupRepository.findByActive(YorN.Y).stream().map(GroupBo::of).collect(Collectors.toList());
    }

    @Override
    public Optional<Task> getTaskById(Integer id) {
        return taskRepository.findById(id);
    }

    @Override
    public String updateTaskLatestRunTime(Integer id) {
        Task task = getTaskById(id).orElse(null);
        if (task != null) {
            task.setLastRunTime(LocalDateTime.now());
            taskRepository.save(task);
            return task.getLastRunTime().format(CommonConstants.DTF);
        } else {
            return null;
        }
    }
}
