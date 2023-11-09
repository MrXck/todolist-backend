package com.todo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.todo.dto.taskBox.TaskBoxDTO;
import com.todo.pojo.TaskBox;
import com.todo.mapper.TaskBoxMapper;
import com.todo.service.TaskBoxService;
import org.springframework.stereotype.Service;

@Service
public class TaskBoxServiceImpl extends ServiceImpl<TaskBoxMapper, TaskBox> implements TaskBoxService {
    @Override
    public TaskBoxDTO all() {
        TaskBoxDTO taskBoxDTO = new TaskBoxDTO();
        taskBoxDTO.setTaskBoxes(this.list());
        return taskBoxDTO;
    }
}