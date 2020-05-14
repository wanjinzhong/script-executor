package com.synnex.shellexecutor.bo;

import com.synnex.shellexecutor.entity.TaskGroup;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class GroupBo {
    private String group;
    private List<TaskBo> tasks = new ArrayList<>();
    public static GroupBo of(TaskGroup group) {
        if (group == null) {
            return null;
        }
        GroupBo res = new GroupBo();
        res.setGroup(group.getName());
        if (group.getTasks() != null && group.getTasks().size() > 0) {
            res.setTasks(group.getTasks().stream().map(TaskBo::of).collect(Collectors.toList()));
        }
        return res;
    }
}
