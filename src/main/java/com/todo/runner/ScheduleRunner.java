package com.todo.runner;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.todo.pojo.Todo;
import com.todo.pojo.User;
import com.todo.service.TodoService;
import com.todo.service.UserService;
import com.todo.utils.EmailValidatorUtils;
import com.todo.utils.SpringUtils;
import org.quartz.Scheduler;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
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
        queryWrapper.ge(Todo::getEndTime, LocalDate.now());

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
                todoService.addQuartz(scheduler, todo, user.getId());
            }
        }
    }
}
