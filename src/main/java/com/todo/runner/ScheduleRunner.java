package com.todo.runner;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.todo.exception.APIException;
import com.todo.pojo.Todo;
import com.todo.pojo.User;
import com.todo.service.TodoService;
import com.todo.service.UserService;
import com.todo.task.Schedule;
import com.todo.utils.*;
import org.quartz.Scheduler;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Component
public class ScheduleRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        TodoService todoService = SpringUtils.getBean(TodoService.class);
        UserService userService = SpringUtils.getBean(UserService.class);
        Scheduler scheduler = SpringUtils.getBean(Scheduler.class);
        LambdaQueryWrapper<Todo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Todo::getIsDone, false);
        queryWrapper.ge(Todo::getStartTime, LocalDate.now());
        queryWrapper.gt(Todo::getPredictTime, LocalTime.now());
        List<Todo> todoList = todoService.list(queryWrapper);

        Map<Long, List<Todo>> map = new HashMap<>();
        Set<Long> userIds = new HashSet<>();
        for (Todo todo : todoList) {
            Long userId = todo.getUserId();
            if (map.containsKey(userId)) {
                map.get(userId).add(todo);
            } else {
                List<Todo> todos = new ArrayList<>();
                todos.add(todo);
                map.put(userId, todos);
            }
            userIds.add(userId);
        }

        if (userIds.isEmpty()) {
            return;
        }

        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.in(User::getId, userIds);
        List<User> users = userService.list(userLambdaQueryWrapper);

        for (User user : users) {
            String email = user.getEmail();
            if (!EmailValidatorUtils.isValid(email)) {
                continue;
            }

            List<Todo> todos = map.get(user.getId());

            for (Todo todo : todos) {
                LocalTime predictTime = todo.getPredictTime();
                LocalDate startTime = todo.getStartTime();
                LocalDate endTime = todo.getEndTime();
                Integer noticeType = todo.getNoticeType();
                Long id = todo.getId();
                String title = todo.getTitle();

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
                                CronUtils.generateWeeklyCron(second, minute, hour, String.valueOf(cronNum)),
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
    }
}
