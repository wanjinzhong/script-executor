package com.synnex.shellexecutor.controller;

import com.synnex.shellexecutor.bo.GroupBo;
import com.synnex.shellexecutor.bo.Pageable;
import com.synnex.shellexecutor.bo.RunParam;
import com.synnex.shellexecutor.bo.RunRequest;
import com.synnex.shellexecutor.bo.RunResult;
import com.synnex.shellexecutor.bo.TaskBo;
import com.synnex.shellexecutor.bo.TaskHistoryBo;
import com.synnex.shellexecutor.constants.CommonConstants;
import com.synnex.shellexecutor.entity.Task;
import com.synnex.shellexecutor.entity.TaskParam;
import com.synnex.shellexecutor.service.CommonService;
import com.synnex.shellexecutor.service.ConfigService;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
public class WebController {

    @Autowired
    private ConfigService configService;

    @Autowired
    private CommonService commonService;

    @GetMapping("/public/api/tasks")
    public List<GroupBo> getTasks() {
        return readTasks();
    }

    private List<GroupBo> readTasks() {
        List<GroupBo> groups = commonService.getGroups();
        groups.forEach(g -> g.getTasks().sort(Comparator.comparing(TaskBo::getSeq)));
        return groups;
    }

    @PostMapping(value = "/public/api/exec")
    public RunResult exec(@RequestBody RunRequest request, HttpServletRequest httpRequest) {
        Optional<Task> taskOpt = commonService.getTaskById(request.getTaskId());
        if (taskOpt.isPresent()) {
            String baseDir = configService.getBaseDir();
            Task task = taskOpt.get();
            String logName = getLogName(task.getScript());
            String runTime;
            try {
                String logFileName = String.format("%s/logs/%s", baseDir, logName);
                File logFile = new File(logFileName);
                if (logFile.exists()) {
                    logFile.delete();
                }
                logFile.createNewFile();
                LocalDateTime time = commonService.updateTaskLatestRunTimeAndSaveHistory(request, httpRequest.getRemoteAddr());
                runTime = time.format(CommonConstants.DTF);
                List<String> params = new ArrayList<>();
                if (request.getParams() != null && request.getParams().size() != 0) {
                    task.getParams().stream().sorted(Comparator.comparing(TaskParam::getSeq)).forEach(p -> {
                        for (RunParam rp : request.getParams()) {
                            if (Objects.equals(p.getId(), rp.getParamId())) {
                                params.add(rp.getValue() == null ? "":rp.getValue());
                                return;
                            }
                        }
                        params.add("");
                    });
                }
                Runtime.getRuntime().exec(
                        new String[]{"/bin/bash", "-c", String.format("echo %s >> %s", task.getName(), logFileName)}).waitFor();
                StringBuilder script = new StringBuilder();
                script.append(baseDir).append("/script/").append(task.getScript());
                params.forEach(p -> script.append(" \"")
                        .append(StringEscapeUtils.unescapeJava(StringEscapeUtils.escapeJava(p)))
                        .append("\" "));
                script.append(">>").append(baseDir).append("/logs/").append(logName);
                Process process = Runtime.getRuntime().exec(
                        new String[]{"/bin/bash", "-c", script.toString()});
                StreamGobbler errorGobbler = new StreamGobbler(process.getErrorStream(), "ERROR", logFileName);
                errorGobbler.start();
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            }
            return new RunResult(task.getSuccessMsg() == null || task.getSuccessMsg().length() == 0 ? "Success" : task.getSuccessMsg(), runTime);
        } else {
            throw new RuntimeException("No such script existed.");
        }
    }

    @GetMapping("/public/api/logs")
    public List<String> getLogs(@RequestParam Integer id) {
        Optional<Task> taskOpt = commonService.getTaskById(id);
        if (taskOpt.isPresent()) {
            Task task = taskOpt.get();
            String logName = getLogName(task.getScript());
            String logFileName = String.format("%s/logs/%s", configService.getBaseDir(), logName);
            File logFile = new File(logFileName);
            return readLastNLine(logFile, 500);
        }
        return new ArrayList<>();
    }

    @GetMapping("allLogs")
    public String getAllLogs(@RequestParam Integer id) {
        Optional<Task> taskOpt = commonService.getTaskById(id);
        if (taskOpt.isPresent()) {
            Task task = taskOpt.get();
            String logName = getLogName(task.getScript());
            String logFileName = String.format("%s/logs/%s", configService.getBaseDir(), logName);
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(logFileName))));
                StringBuilder builder = new StringBuilder("<pre>");
                String line;
                while ((line = reader.readLine()) != null) {
                    line = replaceColor(line);
                    builder.append(line).append("</br>");
                }
                builder.append("</pre>");
                return builder.toString();
            } catch (IOException e) {
                e.printStackTrace();
                return "";
            }
        }
        return "";
    }

    @GetMapping("/public/api/history/{taskId}")
    public Pageable<TaskHistoryBo> getTaskHistories(@PathVariable("taskId") Integer taskId, HttpServletRequest request, Integer page, Integer size) {
        return commonService.getTaskHistories(taskId, request.getRemoteAddr(), page, size);
    }

    @GetMapping("/public/api/refreshIpTable")
    public String refreshIpTable() {
        commonService.refreshIpTable();
        return "OK";
    }

    private String replaceColor(String line) {
        line = line.replaceAll("\\u001B\\[0m", "</span>")
                .replaceAll("\\u001B\\[1m", " ")
                .replaceAll("\\u001B\\[0;1m", "</span>")
                .replaceAll("\\u001B\\[m", "</span>")
                .replaceAll("\\u001B\\[30m", "<span style='color: black'>")
                .replaceAll("\\u001B\\[0;30m", "<span style='color: black'>")
                .replaceAll("\\u001B\\[1;30m", "<span style='color: black'>")
                .replaceAll("\\u001B\\[31m", "<span style='color: red'>")
                .replaceAll("\\u001B\\[0;31m", "<span style='color: red'>")
                .replaceAll("\\u001B\\[1;31m", "<span style='color: red'>")
                .replaceAll("\\u001B\\[32m", "<span style='color: green'>")
                .replaceAll("\\u001B\\[1;32m", "<span style='color: green'>")
                .replaceAll("\\u001B\\[0;32m", "<span style='color: green'>")
                .replaceAll("\\u001B\\[33m", "<span style='color: #ffa11b'>")
                .replaceAll("\\u001B\\[0;33m", "<span style='color: #ffa11b'>")
                .replaceAll("\\u001B\\[1;33m", "<span style='color: #ffa11b'>")
                .replaceAll("\\u001B\\[1;34m", "<span style='color: blue'>")
                .replaceAll("\\u001B\\[35m", "<span style='color: purple'>")
                .replaceAll("\\u001B\\[36m", "<span style='color: darkgreen'>")
                .replaceAll("\\u001B\\[0;36m", "<span style='color: darkgreen'>")
                .replaceAll("\\u001B\\[37m", "<span style='color: white'>")
                .replaceAll("\\u001B\\[[\\s\\S]{2}", "");
        return line;
    }

    private String getLogName(String shellName) {
        int last = shellName.lastIndexOf(".");
        if (last > 0) {
            shellName = shellName.substring(0, last);
        }
        return shellName + ".log";
    }

    static class StreamGobbler extends Thread {
        InputStream is;
        String type;
        String logFile;

        StreamGobbler(InputStream is, String type, String logFile) {
            this.is = is;
            this.type = type;
            this.logFile = logFile;
        }

        public void run() {
            try {
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String line;
                while ((line = br.readLine()) != null) {
                    Runtime.getRuntime().exec(
                            new String[]{"/bin/bash", "-c", String.format("echo %s >> %s", line, logFile)}).waitFor();
                }
            } catch (IOException | InterruptedException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    public List<String> readLastNLine(File file, long numRead) {
        List<String> result = new ArrayList<>();
        long count = 0;
        if (!file.exists() || file.isDirectory() || !file.canRead()) {
            return null;
        }
        RandomAccessFile fileRead = null;
        try {
            fileRead = new RandomAccessFile(file, "r");
            long length = fileRead.length();
            if (length == 0L) {
                return result;
            } else {
                long pos = length - 1;
                while (pos > 0) {
                    pos--;
                    fileRead.seek(pos);
                    if (fileRead.readByte() == '\n') {
                        String line = replaceColor(new String(fileRead.readLine().getBytes("ISO-8859-1"),"utf-8"));
                        result.add(line);
                        count++;
                        if (count == numRead) {
                            break;
                        }
                    }
                }
                if (pos == 0) {
                    fileRead.seek(0);
                    result.add(replaceColor(fileRead.readLine()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileRead != null) {
                try {
                    fileRead.close();
                } catch (Exception e) {
                }
            }
        }
        Collections.reverse(result);
        return result;
    }
}
