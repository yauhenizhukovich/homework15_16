package com.gmail.supersonicleader.app.controller.runner;

import com.gmail.supersonicleader.app.controller.TaskController;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class AppStartupRunner implements ApplicationRunner {

    private final TaskController taskController;

    public AppStartupRunner(TaskController taskController) {
        this.taskController = taskController;
    }

    @Override
    public void run(ApplicationArguments args) {
        taskController.runTask();
    }

}
