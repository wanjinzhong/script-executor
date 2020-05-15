package com.synnex.shellexecutor.bo;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RunRequest {
    private Integer taskId;
    private List<RunParam> params = new ArrayList<>();
}
