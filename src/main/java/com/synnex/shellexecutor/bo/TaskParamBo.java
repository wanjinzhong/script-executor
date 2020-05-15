package com.synnex.shellexecutor.bo;

import com.synnex.shellexecutor.entity.TaskParam;
import com.synnex.shellexecutor.enums.ParamType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TaskParamBo {
    private Integer id;
    private String name;
    private ParamType type;
    private List<String> availableValue;
    private Object value;
    public static TaskParamBo of(TaskParam param) {
        if (param == null) {
            return null;
        }
        TaskParamBo res = new TaskParamBo();
        res.setId(param.getId());
        res.setName(param.getName());
        res.setType(param.getType());
        if (param.getType() == ParamType.BOOLEAN) {
            res.setValue(Boolean.valueOf(param.getDefaultValue()));
        } else {
            res.setValue(param.getDefaultValue());
        }
        res.setAvailableValue(param.getAvailableValue());
        return res;
    }
}
