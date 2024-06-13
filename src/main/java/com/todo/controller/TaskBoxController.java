package com.todo.controller;

import com.todo.dto.taskBox.AddTaskBoxDTO;
import com.todo.dto.taskBox.TaskBoxDTO;
import com.todo.dto.taskBox.UpdateTaskBoxDTO;
import com.todo.service.TaskBoxService;
import com.todo.utils.Log;
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

    @Log
    @PostMapping("/add")
    public void add(@RequestBody @Valid AddTaskBoxDTO addTaskBoxDTO) {
        taskBoxService.add(addTaskBoxDTO);
    }

    @Log
    @PostMapping("/update")
    public void update(@RequestBody @Valid UpdateTaskBoxDTO updateTaskBoxDTO) {
        taskBoxService.updateTaskBoxById(updateTaskBoxDTO);
    }

    @Log
    @GetMapping("/remove/{taskBoxId}")
    public void remove(@PathVariable("taskBoxId") Long taskBoxId) {
        taskBoxService.deleteById(taskBoxId);
    }

    @GetMapping("/getTodo/{taskBoxId}")
    public TaskBoxDTO getTodo(@PathVariable("taskBoxId") Long taskBoxId) {
        return taskBoxService.getTodo(taskBoxId);
    }
}
