package com.synnex.shellexecutor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.alibaba.fastjson.JSONArray;
import com.synnex.shellexecutor.bo.Category;
import com.synnex.shellexecutor.bo.JsonEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/api")
public class WebController {

    @Value("${app.base-dir}")
    private String baseDir;

    public String getBaseDir() {
        return baseDir.endsWith("/") || baseDir.endsWith("\\") ? baseDir.substring(0, baseDir.length() - 2) : baseDir;
    }

    @GetMapping("categories")
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

    @GetMapping("exec")
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
                String line = null;
                while ((line = br.readLine()) != null)
                    Runtime.getRuntime().exec(
                        new String[] {"/bin/bash", "-c", String.format("echo %s >> %s", line, logFile)}).waitFor();
            } catch (IOException | InterruptedException ioe) {
                ioe.printStackTrace();
            }
        }
    }
}
