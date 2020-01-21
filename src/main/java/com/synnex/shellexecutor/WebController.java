package com.synnex.shellexecutor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.synnex.shellexecutor.bo.Category;
import com.synnex.shellexecutor.bo.JsonEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/api")
public class WebController {

    @GetMapping("categories")
    public JsonEntity<List<Category>> getCategories() throws IOException {
        return JsonEntity.of(readCategories());
    }

    private List<Category> readCategories() throws IOException {
        StringBuilder content = new StringBuilder();
        File file = new File("/home/neilw/shell-executor/config.json");
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String line = reader.readLine();
        while(line != null) {
            content.append(line);
            line = reader.readLine();
        }
        return JSONArray.parseArray(content.toString(), Category.class);
    }

    @PostMapping("exec")
    public String exec(@RequestBody Category category) throws IOException {
       Runtime.getRuntime().exec("/home/neilw/shell-executor/shell/" + category.getScript());
        return "OK";
    }

    @GetMapping("exec")
    public String exec(@RequestParam String script) throws IOException {
        Runtime.getRuntime().exec("/home/neilw/shell-executor/shell/" + script);
        return "OK";
    }
}
