package com.todo.task;

import com.todo.pojo.Todo;
import com.todo.pojo.User;
import com.todo.service.TodoService;
import com.todo.service.UserService;
import com.todo.utils.EmailValidatorUtils;
import com.todo.utils.MailUtils;
import com.todo.utils.SpringUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@DisallowConcurrentExecution
public class AutomaticTask implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        TodoService todoService = SpringUtils.getBean(TodoService.class);
        Todo todo = todoService.getById(jobExecutionContext.getJobDetail().getKey().getName());

        UserService userService = SpringUtils.getBean(UserService.class);
        User user = userService.getById(todo.getUserId());

        if (!EmailValidatorUtils.isValid(user.getEmail())) {
            return;
        }

        MailUtils mailUtils = SpringUtils.getBean(MailUtils.class);
        mailUtils.sendMail(user.getEmail(), todo.getTitle(), todo.getDetail());
    }
}
