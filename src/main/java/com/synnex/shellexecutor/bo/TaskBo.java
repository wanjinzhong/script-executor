package com.synnex.shellexecutor.bo;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.synnex.shellexecutor.constants.CommonConstants;
import com.synnex.shellexecutor.entity.Task;
import com.synnex.shellexecutor.entity.TaskParam;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class TaskBo {
    private Integer id;
    private String name;
    private String desc;
    @JsonIgnore
    private String seq;
    private String lastRunTime;
    private List<TaskParamBo> params;

    public static TaskBo of(Task task) {
        if (task == null) {
            return null;
        }
        TaskBo res = new TaskBo();
        res.setId(task.getId());
        res.setName(task.getName());
        res.setSeq(task.getSeq());
        res.setDesc(task.getDescription());
        res.setLastRunTime(task.getLastRunTime() == null ? null : task.getLastRunTime().format(CommonConstants.DTF));
        res.setParams(task.getParams() == null ? new ArrayList<>() : task.getParams().stream().sorted(Comparator.comparing(TaskParam::getSeq))
                .map(TaskParamBo::of).collect(Collectors.toList()));
        return res;
    }
}
