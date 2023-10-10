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


    public static final String FILE_NOT_FOUND = "文件不存在";

    public static final String DATE_ERROR = "开始时间不能在结束时间之后";
    public static final String NOT_FOUND_NOTE_ERROR = "没有这篇文章";

    public static final String SEND_TODO_EMAIL_SUBJECT = "今日待办事项";

    public static final String CHECK_EMAIL_ERROR = "请输入正确的邮箱";
}
