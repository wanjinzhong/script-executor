package com.synnex.shellexecutor.service;

import com.synnex.shellexecutor.bo.GroupBo;
import com.synnex.shellexecutor.bo.HistoryParamBo;
import com.synnex.shellexecutor.bo.Pageable;
import com.synnex.shellexecutor.bo.RunRequest;
import com.synnex.shellexecutor.bo.TaskHistoryBo;
import com.synnex.shellexecutor.bo.TaskParamBo;
import com.synnex.shellexecutor.constants.CommonConstants;
import com.synnex.shellexecutor.dao.TaskGroupRepository;
import com.synnex.shellexecutor.dao.TaskHistoryRepository;
import com.synnex.shellexecutor.dao.TaskRepository;
import com.synnex.shellexecutor.entity.HistoryParam;
import com.synnex.shellexecutor.entity.Task;
import com.synnex.shellexecutor.entity.TaskGroup;
import com.synnex.shellexecutor.entity.TaskHistory;
import com.synnex.shellexecutor.entity.TaskParam;
import com.synnex.shellexecutor.enums.ParamType;
import com.synnex.shellexecutor.enums.YorN;
import com.synnex.shellexecutor.event.HistoryEventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommonServiceImpl implements CommonService {

    @Autowired
    private TaskGroupRepository groupRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskHistoryRepository taskHistoryRepository;

    @Autowired
    private HistoryEventPublisher logEventPublisher;

    @Override
    public List<GroupBo> getGroups() {
        return groupRepository.findByActive(YorN.Y).stream().sorted(Comparator.comparing(TaskGroup::getSeq)).map(GroupBo::of).collect(Collectors.toList());
    }

    @Override
    public Optional<Task> getTaskById(Integer id) {
        return taskRepository.findById(id);
    }

    @Override
    public LocalDateTime updateTaskLatestRunTimeAndSaveHistory(RunRequest request, String remoteHost, String remoteAddr) {
        Task task = getTaskById(request.getTaskId()).orElse(null);
        if (task != null) {
            task.setLastRunTime(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
            taskRepository.save(task);
            buildHistory(request, remoteHost, remoteAddr, task);
            return task.getLastRunTime();
        } else {
            return null;
        }
    }
    private void buildHistory(RunRequest request, String remoteHost, String remoteAddr, Task task) {
        TaskHistory history = new TaskHistory();
        history.setTaskId(task.getId());
        history.setUser(remoteHost);
        history.setIp(remoteAddr);
        history.setDatetime(task.getLastRunTime());
        List<HistoryParam> historyParams = new ArrayList<>();
        task.getParams().stream().sorted(Comparator.comparing(TaskParam::getSeq)).forEach(param -> {
            request.getParams().stream().filter(req -> Objects.equals(req.getParamId(), param.getId())).findFirst().ifPresent(req -> {
                HistoryParam his = new HistoryParam();
                his.setParam(param);
                his.setHistory(history);
                his.setValue(req.getValue());
                historyParams.add(his);
            });
        });
        history.setParams(historyParams);
        logEventPublisher.pushLogEvent(this, history);
    }

    @Override
    public Pageable<TaskHistoryBo> getTaskHistories(Integer taskId, String remoteAddr, Integer page, Integer size) {
        page = page == null || page <= 0 ? 0 : page - 1;
        size = size == null || size <= 0 ? 10 : size;
        Page<TaskHistory> taskHistories = taskHistoryRepository.findByTaskIdOrderByDatetimeDesc(taskId, PageRequest.of(page, size));
        Pageable<TaskHistoryBo> pageable = new Pageable();
        pageable.setTotalSize(taskHistories.getTotalElements());
        pageable.setTotalPage(taskHistories.getTotalPages());
        pageable.setPage(page);
        pageable.setSize(size);
        pageable.setData(taskHistories.getContent().stream().map(t -> toTaskHistoryBo(t, remoteAddr)).collect(Collectors.toList()));
        return pageable;
    }

    private TaskHistoryBo toTaskHistoryBo(TaskHistory taskHistory, String remoteAddr) {
        if (taskHistory == null) {
            return null;
        }
        TaskHistoryBo res = new TaskHistoryBo();
        res.setIp(taskHistory.getIp());
        res.setUser(Objects.equals(remoteAddr, taskHistory.getIp()) ? "Me" : taskHistory.getUser());
        res.setDatetime(taskHistory.getDatetime().format(CommonConstants.DTF));
        if (!CollectionUtils.isEmpty(taskHistory.getParams())) {
            res.setParams(taskHistory.getParams().stream().map(this::toHistoryParamBo).collect(Collectors.toList()));
        }
        return res;
    }

    private HistoryParamBo toHistoryParamBo(HistoryParam param) {
        if (param == null || param.getParam() == null) {
            return null;
        }
        HistoryParamBo res = new HistoryParamBo();
        res.setParam(param.getParam().getName());
        res.setValue(Objects.equals(ParamType.PASSWORD, param.getParam().getType()) ? "******" : param.getValue());
        return res;
    }
}
