package com.synnex.shellexecutor.event;

import com.synnex.shellexecutor.entity.TaskHistory;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class HistoryEvent extends ApplicationEvent {
    private TaskHistory log;
    public HistoryEvent(Object source, TaskHistory log) {
        super(source);
        this.log = log;
    }
}
