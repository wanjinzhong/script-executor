package com.synnex.shellexecutor.event;

import com.synnex.shellexecutor.entity.TaskHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class HistoryEventPublisher {
    @Autowired
    private ApplicationContext applicationContext;

    public void pushLogEvent(Object source, TaskHistory log) {
        if (log == null) {
            return;
        }
        applicationContext.publishEvent(new HistoryEvent(source, log));
    }
}
