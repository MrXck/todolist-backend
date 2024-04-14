package com.todo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.todo.dto.todo.*;
import com.todo.exception.APIException;
import com.todo.mapper.TodoMapper;
import com.todo.pojo.Todo;
import com.todo.pojo.User;
import com.todo.service.TodoService;
import com.todo.service.UserService;
import com.todo.task.Schedule;
import com.todo.utils.*;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class TodoServiceImpl extends ServiceImpl<TodoMapper, Todo> implements TodoService {

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private UserService userService;

    @Override
    public TodoDTO getByMonth(GetTodoDTO getTodoDTO) {
        Long userId = UserThreadLocal.get();
        LocalDate startTime = getTodoDTO.getStartTime();
        LocalDate endTime = getTodoDTO.getEndTime();

        // 判断开始日期是否在结束日期之后
        int i = startTime.compareTo(endTime);
        if (i > 0) {
            throw new APIException(Constant.DATE_ERROR);
        }

        // 查询待办事项
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

        // 判断开始日期是否在结束日期之后
        int i = startTime.compareTo(endTime);
        if (i > 0) {
            throw new APIException(Constant.DATE_ERROR);
        }

        // 修改待办事项设置
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
        updateWrapper.set(Todo::getPredictTime, updateTodoDTO.getPredictTime());
        updateWrapper.set(Todo::getEnableEmail, updateTodoDTO.getEnableEmail());
        updateWrapper.set(taskBoxId != null, Todo::getTaskBoxId, taskBoxId);
        boolean b = this.update(updateWrapper);

        if (!b) {
            return;
        }

        try {
            // 删除定时任务中该的待办事项的提醒
            QuartzUtils.deleteScheduleJob(scheduler, updateTodoDTO.getId().toString());
        } catch (Exception ignored) {

        }
        Todo todo = new Todo();
        todo.setId(updateTodoDTO.getId());
        todo.setTitle(updateTodoDTO.getTitle());
        todo.setStartTime(updateTodoDTO.getStartTime());
        todo.setEndTime(updateTodoDTO.getEndTime());
        todo.setPredictTime(updateTodoDTO.getPredictTime());
        todo.setEnableEmail(updateTodoDTO.getEnableEmail());
        todo.setNoticeType(updateTodoDTO.getNoticeType());
        todo.setCronNum(updateTodoDTO.getCronNum());

        // 添加到定时任务中
        addQuartz(scheduler, todo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(AddTodoDTO addTodoDTO) {
        LocalDate startTime = addTodoDTO.getStartTime();
        LocalDate endTime = addTodoDTO.getEndTime();

        // 判断开始日期是否在结束日期之后
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

        // 添加到定时任务中
        addQuartz(scheduler, todo);
        return todo.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long todoId) {
        LambdaQueryWrapper<Todo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Todo::getId, todoId);
        queryWrapper.eq(Todo::getUserId, UserThreadLocal.get());
        boolean b = this.remove(queryWrapper);
        if (b) {
            QuartzUtils.deleteScheduleJob(scheduler, todoId.toString());
        }
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
        LocalTime predictTime = batchGenerateTodoDTO.getPredictTime();
        Boolean enableEmail = batchGenerateTodoDTO.getEnableEmail();
        Integer noticeType = batchGenerateTodoDTO.getNoticeType();
        Integer cronNum = batchGenerateTodoDTO.getCronNum();

        // 根据生成参数生成日期列表
        List<Date> dates = new ArrayList<>();
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
            todo.setPredictTime(predictTime);
            todo.setEnableEmail(enableEmail);
            todo.setNoticeType(noticeType);
            todo.setCronNum(cronNum);

            todo.setStartTime(LocalDate.parse(sdf.format(date)));
            todo.setEndTime(LocalDate.parse(sdf.format(calendar.getTime())));

            todo.setCreateTime(LocalDateTime.now());
            todo.setUpdateTime(LocalDateTime.now());
            todos.add(todo);

        }

        this.saveBatch(todos);
        for (Todo todo : todos) {
            addQuartz(scheduler, todo);
        }
    }

    @Override
    public void startTodo(Long todoId) {
        Todo todo = this.getById(todoId);

        if (todo.getStartDoTime() != null) {
            throw new APIException(Constant.TODO_START_TODO_ERROR);
        }

        LambdaUpdateWrapper<Todo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Todo::getUserId, UserThreadLocal.get());
        updateWrapper.eq(Todo::getId, todoId);
        updateWrapper.set(Todo::getStartDoTime, LocalDateTime.now());
        this.update(updateWrapper);
    }

    @Override
    public void endTodo(Long todoId) {
        Todo todo = this.getById(todoId);

        if (todo.getEndDoTime() != null) {
            throw new APIException(Constant.TODO_END_TODO_ERROR);
        }

        LambdaUpdateWrapper<Todo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Todo::getUserId, UserThreadLocal.get());
        updateWrapper.eq(Todo::getId, todoId);
        updateWrapper.set(Todo::getEndDoTime, LocalDateTime.now());
        this.update(updateWrapper);
    }

    public boolean canAddQuartz(LocalDate endTime, LocalTime predictTime, Todo todo) {
        // 判断该待办事项是否开启邮件提醒
        Boolean enableEmail = todo.getEnableEmail();
        if (Constant.DISABLE_EMAIL.equals(enableEmail)) {
            return false;
        }

        // 获取用户然后判断该用户是否开启邮件提醒
        User user = userService.getById(UserThreadLocal.get());
        if (Constant.DISABLE_EMAIL.equals(user.getEnableEmail())) {
            return false;
        }

        // 判断该待办事项是否已经完成
        Boolean isDone = todo.getIsDone();
        if (isDone != null && isDone) {
            return false;
        }

        LocalDate now = LocalDate.now();

        if (endTime.isBefore(now)) {
            return false;
        }

        return (endTime.isEqual(now) && predictTime.isAfter(LocalTime.now())) || endTime.isAfter(now);
    }

    public void addQuartz(Scheduler scheduler, Todo todo) {
        LocalTime predictTime = todo.getPredictTime();
        LocalDate endTime = todo.getEndTime();

        if (!canAddQuartz(endTime, predictTime, todo)) {
            return;
        }

        LocalDate startTime = todo.getStartTime();
        Integer noticeType = todo.getNoticeType();
        Long id = todo.getId();
        String title = todo.getTitle();

        if (noticeType == null) {
            throw new APIException(Constant.NOTICE_TYPE_ERROR);
        }

        if (Constant.QUARTZ_EXECUTE_ONCE.equals(noticeType)) {
            QuartzUtils.createScheduleJobWithDateTime(
                    scheduler,
                    new Schedule(id, title, DateUtils.generateDateWithLocalDateAndLocalTime(startTime, predictTime), DateUtils.generateDateWithLocalDateAndLocalTime(endTime, predictTime)),
                    Constant.QUARTZ_TASK_PATH
            );
        } else {
            String second = String.valueOf(predictTime.getSecond());
            String minute = String.valueOf(predictTime.getMinute());
            String hour = String.valueOf(predictTime.getHour());
            Integer cronNum = todo.getCronNum();
            if (Constant.QUARTZ_EXECUTE_EVERY_DAY.equals(noticeType)) {
                QuartzUtils.createScheduleJobWithCron(
                        scheduler,
                        new Schedule(id, title, DateUtils.generateDateWithLocalDateAndLocalTime(startTime, predictTime), DateUtils.generateDateWithLocalDateAndLocalTime(endTime, predictTime)),
                        CronUtils.generateDailyCron(second, minute, hour),
                        Constant.QUARTZ_TASK_PATH
                );
            } else if (Constant.QUARTZ_EXECUTE_EVERY_WEEK.equals(noticeType)) {
                if (cronNum == null) {
                    throw new APIException("cronNum不能为空");
                }
                QuartzUtils.createScheduleJobWithCron(
                        scheduler,
                        new Schedule(id, title, DateUtils.generateDateWithLocalDateAndLocalTime(startTime, predictTime), DateUtils.generateDateWithLocalDateAndLocalTime(endTime, predictTime)),
                        CronUtils.generateWeeklyCron(second, minute, hour, CronUtils.getWeekDay(cronNum)),
                        Constant.QUARTZ_TASK_PATH
                );
            } else if (Constant.QUARTZ_EXECUTE_EVERY_MONTH.equals(noticeType)) {
                if (cronNum == null) {
                    throw new APIException("cronNum不能为空");
                }
                QuartzUtils.createScheduleJobWithCron(
                        scheduler,
                        new Schedule(id, title, DateUtils.generateDateWithLocalDateAndLocalTime(startTime, predictTime), DateUtils.generateDateWithLocalDateAndLocalTime(endTime, predictTime)),
                        CronUtils.generateMonthlyCron(second, minute, hour, String.valueOf(cronNum)),
                        Constant.QUARTZ_TASK_PATH
                );
            }
        }


    }

}