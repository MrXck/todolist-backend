package com.todo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.todo.dto.taskBox.AddTaskBoxDTO;
import com.todo.dto.taskBox.TaskBoxDTO;
import com.todo.dto.taskBox.UpdateTaskBoxDTO;
import com.todo.exception.APIException;
import com.todo.pojo.TaskBox;
import com.todo.mapper.TaskBoxMapper;
import com.todo.service.TaskBoxService;
import com.todo.utils.Constant;
import com.todo.utils.UserThreadLocal;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TaskBoxServiceImpl extends ServiceImpl<TaskBoxMapper, TaskBox> implements TaskBoxService {
    @Override
    public TaskBoxDTO all() {
        LambdaQueryWrapper<TaskBox> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TaskBox::getUserId, UserThreadLocal.get());

        TaskBoxDTO taskBoxDTO = new TaskBoxDTO();
        taskBoxDTO.setTaskBoxes(this.list(queryWrapper));
        return taskBoxDTO;
    }

    @Override
    public void add(AddTaskBoxDTO addTaskBoxDTO) {
        String name = addTaskBoxDTO.getName();

        LambdaQueryWrapper<TaskBox> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(TaskBox::getName, name);
        queryWrapper.eq(TaskBox::getUserId, UserThreadLocal.get());

        if (this.count(queryWrapper) != 0) {
            throw new APIException(Constant.TASK_BOX_ERROR);
        }

        TaskBox taskBox = new TaskBox();

        taskBox.setName(name);
        taskBox.setUserId(UserThreadLocal.get());
        taskBox.setCreateTime(LocalDateTime.now());
        taskBox.setUpdateTime(LocalDateTime.now());
        this.save(taskBox);
    }

    @Override
    public void updateTaskBoxById(UpdateTaskBoxDTO updateTaskBoxDTO) {
        String name = updateTaskBoxDTO.getName();
        Long id = updateTaskBoxDTO.getId();

        LambdaQueryWrapper<TaskBox> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(TaskBox::getName, name);
        queryWrapper.eq(TaskBox::getUserId, UserThreadLocal.get());

        if (this.count(queryWrapper) != 0) {
            throw new APIException(Constant.TASK_BOX_ERROR);
        }

        LambdaUpdateWrapper<TaskBox> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(TaskBox::getId, id);
        updateWrapper.eq(TaskBox::getUserId, UserThreadLocal.get());
        updateWrapper.set(TaskBox::getName, name);
        this.update(updateWrapper);
    }

    @Override
    public void deleteById(Long taskBoxId) {
        LambdaQueryWrapper<TaskBox> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TaskBox::getUserId, UserThreadLocal.get());
        queryWrapper.eq(TaskBox::getId, taskBoxId);
        this.remove(queryWrapper);
    }
}