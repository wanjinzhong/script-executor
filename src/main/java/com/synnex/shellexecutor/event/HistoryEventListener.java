package com.synnex.shellexecutor.event;

import com.synnex.shellexecutor.entity.TaskHistory;
import com.synnex.shellexecutor.dao.TaskHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class HistoryEventListener {

    @Autowired
    private TaskHistoryRepository taskLogRepository;

    @Async
    @EventListener
    public void handleLogEvent(HistoryEvent logEvent) {
        TaskHistory log = logEvent.getLog();
        if (log != null) {
            taskLogRepository.save(log);
        }
    }

}
