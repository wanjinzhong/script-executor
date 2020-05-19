package com.synnex.shellexecutor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ShellExecutorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShellExecutorApplication.class, args);
    }

}
