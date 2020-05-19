package com.synnex.shellexecutor.bo;

import com.synnex.shellexecutor.entity.Task;
import com.synnex.shellexecutor.entity.TaskGroup;
import com.synnex.shellexecutor.enums.YorN;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@Setter
public class GroupBo {
    private Integer id;
    private String group;
    private YorN defaultGroup;
    private List<TaskBo> tasks = new ArrayList<>();
    public static GroupBo of(TaskGroup group) {
        if (group == null) {
            return null;
        }
        GroupBo res = new GroupBo();
        res.setId(group.getId());
        res.setDefaultGroup(group.getDefaultGroup());
        res.setGroup(group.getName());
        if (group.getTasks() != null && group.getTasks().size() > 0) {
            res.setTasks(group.getTasks().stream().filter(task -> Objects.equals(task.getActive(), YorN.Y))
                    .sorted(Comparator.comparing(Task::getSeq)).map(TaskBo::of).collect(Collectors.toList()));
        }
        return res;
    }
}
