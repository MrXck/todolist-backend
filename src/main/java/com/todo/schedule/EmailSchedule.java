package com.todo.schedule;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.todo.pojo.Todo;
import com.todo.pojo.User;
import com.todo.service.TodoService;
import com.todo.service.UserService;
import com.todo.utils.Constant;
import com.todo.utils.MailUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class EmailSchedule {

    @Autowired
    private TodoService todoService;

    @Autowired
    private UserService userService;

    @Autowired
    private MailUtils mailUtils;

    @Scheduled(cron = "0 0 8 * * ?")
    public void sendEmail() {
        LambdaQueryWrapper<Todo> queryWrapper = new LambdaQueryWrapper<>();
        LocalDate now = LocalDate.now();
        queryWrapper.ge(Todo::getStartTime, now);
        queryWrapper.le(Todo::getEndTime, now);
        queryWrapper.eq(Todo::getIsDone, false);

        List<Todo> list = todoService.list(queryWrapper);

        HashMap<Long, List<String>> map = new HashMap<>();
        List<Long> userIds = new ArrayList<>();

        for (Todo todo : list) {
            Long userId = todo.getUserId();
            if (!userIds.contains(userId)) {
                userIds.add(userId);
            }

            if (!map.containsKey(userId)) {
                ArrayList<String> detail = new ArrayList<>();
                detail.add("1. " + todo.getTitle() + "\n    " + todo.getDetail());
                map.put(userId, detail);
            } else {
                List<String> strings = map.get(userId);
                strings.add(strings.size() + 1 + ". " + todo.getTitle() + "\n    " + todo.getDetail());
            }
        }

        LambdaQueryWrapper<User> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.in(User::getId, userIds);
        List<User> users = userService.list(queryWrapper1);

        for (User user : users) {
            String email = user.getEmail();
            if (email != null && !email.isEmpty()) {
                try {
                    mailUtils.sendMail(email, Constant.SEND_TODO_EMAIL_SUBJECT, String.join("\n\n", map.get(user.getId())));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
