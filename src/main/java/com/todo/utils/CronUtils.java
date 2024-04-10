package com.todo.utils;

import java.text.MessageFormat;

public class CronUtils {

    public static String generateDailyCron(String hour, String minute, String second) {
        return generateCron(second, minute, hour, "*", "*", "?");
    }

    public static String generateWeeklyCron(String hour, String minute, String second, String dayOfWeek) {
        return generateCron(second, minute, hour, "*", "*", dayOfWeek);
    }

    public static String generateMonthlyCron(String hour, String minute, String second, String dayOfMonth) {
        return generateCron(second, minute, hour, dayOfMonth, "*", "?");
    }

    public static String generateCron(String seconds, String minutes, String hours, String dayOfMonth, String month, String dayOfWeek) {
        return MessageFormat.format("{0} {1} {2} {3} {4} {5}", seconds, minutes, hours, dayOfMonth, month, dayOfWeek);
    }
}
