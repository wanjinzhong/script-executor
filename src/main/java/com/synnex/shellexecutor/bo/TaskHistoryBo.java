package com.synnex.shellexecutor.bo;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TaskHistoryBo {
    private String user;
    private String ip;
    private String datetime;
    private List<HistoryParamBo> params = new ArrayList<>();
}