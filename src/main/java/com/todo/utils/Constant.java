package com.todo.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author xck
 */
public class Constant {

    public static final String PATH = System.getProperty("user.dir") + "/files/";
    public static final List<String> SUFFIX_WHITE_LIST = new ArrayList<>(Arrays.asList("jpg", "jpeg", "png", "gif"));

    public static final String LOGIN_ERROR = "用户名或密码错误";
    public static final String USERNAME_ALREADY_ERROR = "用户名已存在";
    public static final Boolean ENABLE_EMAIL = true;
    public static final Boolean DISABLE_EMAIL = false;



    public static final String FILE_NOT_FOUND = "文件不存在";

    public static final String DATE_ERROR = "开始时间不能在结束时间之后";
    public static final String DATE_GENERATE_ERROR = "日期生成错误";
    public static final String DATE_FORMAT = "yyyy-MM";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd";
    public static final String NOT_FOUND_NOTE_ERROR = "没有这篇文章";

    public static final String SEND_TODO_EMAIL_SUBJECT = "今日待办事项";

    public static final String TODO_START_TODO_ERROR = "待办事项开始后就不能修改开始时间了";

    public static final String TODO_END_TODO_ERROR = "待办事项结束后就不能修改开始时间了";

    public static final String CHECK_EMAIL_ERROR = "请输入正确的邮箱";

    public static final String TASK_BOX_ERROR = "已有相同名称的清单";


    public static final Integer GENERATE_TYPE_DAY = 1;
    public static final Integer GENERATE_TYPE_WEEK = 2;
    public static final Integer GENERATE_TYPE_MONTH = 3;

    public static final String QUARTZ_TASK_PATH = "com.todo.task.AutomaticTask";

}
