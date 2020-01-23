package com.synnex.shellexecutor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.alibaba.fastjson.JSONArray;
import com.synnex.shellexecutor.bo.Category;
import com.synnex.shellexecutor.bo.JsonEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController {

    @Value("${app.base-dir}")
    private String baseDir;

    public String getBaseDir() {
        return baseDir.endsWith("/") || baseDir.endsWith("\\") ? baseDir.substring(0, baseDir.length() - 2) : baseDir;
    }

    @GetMapping("/public/api/categories")
    public JsonEntity<List<Category>> getCategories() {
        return JsonEntity.of(readCategories());
    }

    private List<Category> readCategories() {
        StringBuilder content = new StringBuilder();
        File file = new File(String.format("%s/config.json", getBaseDir()));
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line = reader.readLine();
            while (line != null) {
                content.append(line);
                line = reader.readLine();
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return JSONArray.parseArray(content.toString(), Category.class);
    }

    @GetMapping("/public/api/exec")
    public String exec(@RequestParam Integer id) {
        Optional<Category> categoryOptional = readCategories().stream().filter(c -> Objects.equals(c.getId(), id)).findFirst();
        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();
            String logName = getLogName(category.getScript());
            try {
                String logFileName = String.format("%s/logs/%s", getBaseDir(), logName);
                File logFile = new File(logFileName);
                if (!logFile.exists()) {
                    logFile.createNewFile();
                }
                Runtime.getRuntime().exec(
                    new String[] {"/bin/bash", "-c", String.format("echo === $(date \"+%%Y-%%m-%%d %%H:%%M:%%S\") === >> %s", logFileName)}).waitFor();
                Process process = Runtime.getRuntime().exec(
                    new String[] {"/bin/bash", "-c", String.format("%s/script/%s >>%s/logs/%s", getBaseDir(), category.getScript(), getBaseDir(), logName)});
                StreamGobbler errorGobbler = new StreamGobbler(process.getErrorStream(), "ERROR", logFileName);
                errorGobbler.start();
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            }
            return category.getSuccessMsg() == null || category.getSuccessMsg().length() == 0 ? "Success" : category.getSuccessMsg();
        } else {
            throw new RuntimeException("No such script existed.");
        }
    }

    @GetMapping("/public/api/logs")
    public List<String> getLogs(@RequestParam Integer id) {
        Optional<Category> categoryOptional = readCategories().stream().filter(c -> Objects.equals(c.getId(), id)).findFirst();
        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();
            String logName = getLogName(category.getScript());
            String logFileName = String.format("%s/logs/%s", getBaseDir(), logName);
            try {
                Process process = Runtime.getRuntime().exec(
                    new String[] {"/bin/bash", "-c", String.format("tail -n 200 %s", logFileName)});
                process.waitFor();
                InputStream is = process.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                List<String> logs = new ArrayList<>();
                String line;
                while ((line = br.readLine()) != null) {
                    logs.add(replaceColor(line));
                }
                return logs;
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
                return new ArrayList<>();
            }
        }
        return new ArrayList<>();
    }

    @GetMapping("allLogs")
    public String getAllLogs(@RequestParam Integer id) {
        Optional<Category> categoryOptional = readCategories().stream().filter(c -> Objects.equals(c.getId(), id)).findFirst();
        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();
            String logName = getLogName(category.getScript());
            String logFileName = String.format("%s/logs/%s", getBaseDir(), logName);
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

    private String replaceColor(String line) {
        line = line.replaceAll("\\u001B\\[0m", "</span>")
                   .replaceAll("\\u001B\\[30m", "<span style='color: black'>")
                   .replaceAll("\\u001B\\[31m", "<span style='color: red'>")
                   .replaceAll("\\u001B\\[32m", "<span style='color: green'>")
                   .replaceAll("\\u001B\\[33m", "<span style='color: #ffa11b'>")
                   .replaceAll("\\u001B\\[34m", "<span style='color: blue'>")
                   .replaceAll("\\u001B\\[35m", "<span style='color: purple'>")
                   .replaceAll("\\u001B\\[36m", "<span style='color: darkgreen'>")
                   .replaceAll("\\u001B\\[37m", "<span style='color: white'>");
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
        String result;

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
                        new String[] {"/bin/bash", "-c", String.format("echo %s >> %s", line, logFile)}).waitFor();
                }
            } catch (IOException | InterruptedException ioe) {
                ioe.printStackTrace();
            }
        }
    }
}
