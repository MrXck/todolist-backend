package com.todo.utils;

import java.text.MessageFormat;

public class CronUtils {

    public static String generateDailyCron(String second, String minute, String hour) {
        return generateCron(second, minute, hour, "*", "*", "?");
    }

    public static String generateWeeklyCron(String second, String minute, String hour, String dayOfWeek) {
        return generateCron(second, minute, hour, "?", "*", dayOfWeek);
    }

    public static String generateMonthlyCron(String second, String minute, String hour, String dayOfMonth) {
        return generateCron(second, minute, hour, dayOfMonth, "*", "?");
    }

    public static String generateCron(String seconds, String minutes, String hours, String dayOfMonth, String month, String dayOfWeek) {
        return MessageFormat.format("{0} {1} {2} {3} {4} {5}", seconds, minutes, hours, dayOfMonth, month, dayOfWeek);
    }

    public static String getWeekDay (int dayOfWeek) {
        switch (dayOfWeek) {
            case 1:
                return "MON";
            case 2:
                return "TUE";
            case 3:
                return "WED";
            case 4:
                return "THU";
            case 5:
                return "FRI";
            case 6:
                return "SAT";
            case 7:
                return "SUN";
            default:
                return "";
        }
    }
}
