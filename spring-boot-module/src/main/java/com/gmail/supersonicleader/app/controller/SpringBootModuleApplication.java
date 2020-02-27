package com.gmail.supersonicleader.app.controller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "com.gmail.supersonicleader.app.controller.runner",
        "com.gmail.supersonicleader.app.controller.impl",
        "com.gmail.supersonicleader.app.service.impl",
        "com.gmail.supersonicleader.app.repository.impl"
})
public class SpringBootModuleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootModuleApplication.class, args);
    }

}


