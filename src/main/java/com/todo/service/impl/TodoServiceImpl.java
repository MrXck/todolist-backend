package com.todo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.todo.dto.todo.*;
import com.todo.exception.APIException;
import com.todo.mapper.TodoMapper;
import com.todo.pojo.Todo;
import com.todo.service.TodoService;
import com.todo.utils.Constant;
import com.todo.utils.DateUtils;
import com.todo.utils.UserThreadLocal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class TodoServiceImpl extends ServiceImpl<TodoMapper, Todo> implements TodoService {

    @Override
    public TodoDTO getByMonth(GetTodoDTO getTodoDTO) {
        Long userId = UserThreadLocal.get();
        LocalDate startTime = getTodoDTO.getStartTime();
        LocalDate endTime = getTodoDTO.getEndTime();

        int i = startTime.compareTo(endTime);
        if (i > 0) {
            throw new APIException(Constant.DATE_ERROR);
        }

        LambdaQueryWrapper<Todo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Todo::getUserId, userId);
        queryWrapper.ge(Todo::getEndTime, startTime);
        queryWrapper.le(Todo::getStartTime, endTime);

        List<Todo> list = this.list(queryWrapper);

        TodoDTO todoDTO = new TodoDTO();
        todoDTO.setList(list);

        return todoDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTodoById(UpdateTodoDTO updateTodoDTO) {
        LocalDate startTime = updateTodoDTO.getStartTime();
        LocalDate endTime = updateTodoDTO.getEndTime();
        Long taskBoxId = updateTodoDTO.getTaskBoxId();
        int i = startTime.compareTo(endTime);
        if (i > 0) {
            throw new APIException(Constant.DATE_ERROR);
        }

        LambdaUpdateWrapper<Todo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Todo::getUserId, UserThreadLocal.get());
        updateWrapper.eq(Todo::getId, updateTodoDTO.getId());
        updateWrapper.set(Todo::getTitle, updateTodoDTO.getTitle());
        updateWrapper.set(Todo::getDetail, updateTodoDTO.getDetail());
        updateWrapper.set(Todo::getIsDone, updateTodoDTO.getIsDone());
        updateWrapper.set(Todo::getPriority, updateTodoDTO.getPriority());
        updateWrapper.set(Todo::getStartTime, startTime);
        updateWrapper.set(Todo::getEndTime, endTime);
        updateWrapper.set(Todo::getUpdateTime, LocalDateTime.now());
        updateWrapper.set(taskBoxId != null, Todo::getTaskBoxId, taskBoxId);
        this.update(updateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(AddTodoDTO addTodoDTO) {
        LocalDate startTime = addTodoDTO.getStartTime();
        LocalDate endTime = addTodoDTO.getEndTime();
        int i = startTime.compareTo(endTime);
        if (i > 0) {
            throw new APIException(Constant.DATE_ERROR);
        }

        Todo todo = new Todo();
        BeanUtil.copyProperties(addTodoDTO, todo);
        todo.setUserId(UserThreadLocal.get());
        todo.setCreateTime(LocalDateTime.now());
        todo.setUpdateTime(LocalDateTime.now());
        this.save(todo);
        return todo.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long todoId) {
        LambdaQueryWrapper<Todo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Todo::getId, todoId);
        queryWrapper.eq(Todo::getUserId, UserThreadLocal.get());
        this.remove(queryWrapper);
    }

    @Override
    public TodoDTO getThisMonthTodo() {
        TodoDTO todoDTO = new TodoDTO();

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constant.DATE_FORMAT);
        String currentYearMonth = currentDate.format(formatter);

        LambdaQueryWrapper<Todo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Todo::getUserId, UserThreadLocal.get());
        queryWrapper.ge(Todo::getStartTime, currentYearMonth + "-01");
        queryWrapper.le(Todo::getStartTime, currentYearMonth + "-31");
        int count = this.count(queryWrapper);
        todoDTO.setCount(count);
        return todoDTO;
    }

    @Override
    public TodoDTO getAllTodo() {
        TodoDTO todoDTO = new TodoDTO();
        LambdaQueryWrapper<Todo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Todo::getUserId, UserThreadLocal.get());
        int count = this.count(queryWrapper);
        todoDTO.setCount(count);
        return todoDTO;
    }

    @Override
    public TodoDTO getTodayTodo() {
        TodoDTO todoDTO = new TodoDTO();
        LocalDate currentDate = LocalDate.now();
        LambdaQueryWrapper<Todo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Todo::getUserId, UserThreadLocal.get());
        queryWrapper.le(Todo::getStartTime, currentDate);
        queryWrapper.ge(Todo::getEndTime, currentDate);
        queryWrapper.eq(Todo::getIsDone, false);
        int count = this.count(queryWrapper);
        todoDTO.setCount(count);
        return todoDTO;
    }

    @Override
    public TodoDTO getDelayTodo() {
        TodoDTO todoDTO = new TodoDTO();
        LocalDate currentDate = LocalDate.now();
        LambdaQueryWrapper<Todo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Todo::getUserId, UserThreadLocal.get());
        queryWrapper.lt(Todo::getEndTime, currentDate);
        queryWrapper.eq(Todo::getIsDone, false);
        int count = this.count(queryWrapper);
        todoDTO.setCount(count);
        return todoDTO;
    }

    @Override
    public TodoDTO getDoneTodo() {
        TodoDTO todoDTO = new TodoDTO();
        LambdaQueryWrapper<Todo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Todo::getUserId, UserThreadLocal.get());
        queryWrapper.eq(Todo::getIsDone, true);
        int count = this.count(queryWrapper);
        todoDTO.setCount(count);
        return todoDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchGenerate(BatchGenerateTodoDTO batchGenerateTodoDTO) {
        Integer generateType = batchGenerateTodoDTO.getGenerateType();
        LocalDate startTime = batchGenerateTodoDTO.getStartTime();
        LocalDate endTime = batchGenerateTodoDTO.getEndTime();

        if (startTime.isAfter(endTime)) {
            throw new APIException(Constant.DATE_ERROR);
        }

        String title = batchGenerateTodoDTO.getTitle();
        String detail = batchGenerateTodoDTO.getDetail();
        Integer priority = batchGenerateTodoDTO.getPriority();
        Long taskBoxId = batchGenerateTodoDTO.getTaskBoxId();
        Integer duration = batchGenerateTodoDTO.getDuration();
        List<Integer> generateDateList = batchGenerateTodoDTO.getGenerateDateList();

        List<Date> dates = null;
        try {
            if (generateType.equals(Constant.GENERATE_TYPE_DAY)) {
                dates = DateUtils.generateDateWithDay(startTime.toString(), endTime.toString());
            } else if (generateType.equals(Constant.GENERATE_TYPE_WEEK)) {
                dates = DateUtils.generateDateWithWeek(startTime.toString(), endTime.toString(), generateDateList);
            } else if (generateType.equals(Constant.GENERATE_TYPE_MONTH)) {
                dates = DateUtils.generateDateWithMonth(startTime.toString(), endTime.toString(), generateDateList);
            }
        } catch (Exception e) {
            throw new APIException(Constant.DATE_GENERATE_ERROR);
        }

        List<Todo> todos = new ArrayList<>();

        Long userId = UserThreadLocal.get();

        for (Date date : dates) {
            SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_TIME_FORMAT);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DATE, duration - 1);

            Todo todo = new Todo();
            todo.setUserId(userId);
            todo.setTaskBoxId(taskBoxId);
            todo.setTitle(title);
            todo.setDetail(detail);
            todo.setIsDone(false);
            todo.setPriority(priority);

            todo.setStartTime(LocalDate.parse(sdf.format(date)));
            todo.setEndTime(LocalDate.parse(sdf.format(calendar.getTime())));

            todo.setCreateTime(LocalDateTime.now());
            todo.setUpdateTime(LocalDateTime.now());
            todos.add(todo);

        }

        this.saveBatch(todos);
    }

}