package com.todo.controller;

import com.todo.dto.taskBox.AddTaskBoxDTO;
import com.todo.dto.taskBox.TaskBoxDTO;
import com.todo.service.TaskBoxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/taskBox")
public class TaskBoxController {

    @Autowired
    private TaskBoxService taskBoxService;

    @GetMapping("/all")
    public TaskBoxDTO all() {
        return taskBoxService.all();
    }

    @PostMapping("/add")
    public String add(@RequestBody @Valid AddTaskBoxDTO addTaskBoxDTO) {
        taskBoxService.add(addTaskBoxDTO);
        return "";
    }
}
