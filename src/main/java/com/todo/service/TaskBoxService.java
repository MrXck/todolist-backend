package com.todo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.todo.dto.taskBox.AddTaskBoxDTO;
import com.todo.dto.taskBox.TaskBoxDTO;
import com.todo.dto.taskBox.UpdateTaskBoxDTO;
import com.todo.pojo.TaskBox;

public interface TaskBoxService extends IService<TaskBox> {
    TaskBoxDTO all();

    void add(AddTaskBoxDTO addTaskBoxDTO);

    void updateTaskBoxById(UpdateTaskBoxDTO updateTaskBoxDTO);
}