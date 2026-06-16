package com.todo.task;

import com.todo.exception.APIException;
import com.todo.pojo.Todo;
import com.todo.pojo.User;
import com.todo.service.TodoService;
import com.todo.service.UserService;
import com.todo.utils.*;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.UnsupportedEncodingException;

@DisallowConcurrentExecution
public class AutomaticTask implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        TodoService todoService = SpringUtils.getBean(TodoService.class);
        Todo todo = todoService.getById(jobExecutionContext.getJobDetail().getKey().getName());

        if (Constant.DISABLE_EMAIL.equals(todo.getEnableEmail()) && Constant.DISABLE_ANDROID.equals(todo.getEnableAndroid()) && Constant.DISABLE_IOS.equals(todo.getEnableIos())) {
            return;
        }

        if (todo.getIsDone()) {
            return;
        }

        UserService userService = SpringUtils.getBean(UserService.class);
        User user = userService.getById(todo.getUserId());
        String title = todo.getTitle();
        String detail = todo.getDetail();
        if (detail == null || detail.isEmpty()) {
            detail = "(空)";
        }

        if (Constant.ENABLE_EMAIL.equals(todo.getEnableEmail()) && Constant.ENABLE_EMAIL.equals(user.getEnableEmail()) && EmailValidatorUtils.isValid(user.getEmail())) {
            MailUtils mailUtils = SpringUtils.getBean(MailUtils.class);
            mailUtils.sendMail(user.getEmail(), title, detail);
        }

        if (Constant.ENABLE_IOS.equals(todo.getEnableIos()) && Constant.ENABLE_IOS.equals(user.getEnableIos()) && URLValidatorUtils.isValid(user.getIosPath())) {
            try {
                BarkUtils.sendTitleAndContent(user.getIosPath(), title, detail);
            } catch (UnsupportedEncodingException e) {
                throw new APIException(e.getMessage());
            }
        }

        if (Constant.ENABLE_ANDROID.equals(todo.getEnableAndroid()) && Constant.ENABLE_ANDROID.equals(user.getEnableAndroid()) && URLValidatorUtils.isValid(user.getAndroidPath())) {
            GotifyUtils.sendTitleAndContent(user.getAndroidPath(), title, detail);
        }
    }
}
