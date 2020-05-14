package com.synnex.shellexecutor.bo;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.synnex.shellexecutor.constants.CommonConstants;
import com.synnex.shellexecutor.entity.Task;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class TaskBo {
    private Integer id;
    private String name;
    private String desc;
    @JsonIgnore
    private String seq;
    private String lastRunTime;

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
        return res;
    }
}
