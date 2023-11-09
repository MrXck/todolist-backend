package com.todo.controller;

import com.todo.dto.taskBox.TaskBoxDTO;
import com.todo.service.TaskBoxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/taskBox")
public class TaskBoxController {

    @Autowired
    private TaskBoxService taskBoxService;

    @GetMapping("/all")
    public TaskBoxDTO all() {
        return taskBoxService.all();
    }
}
