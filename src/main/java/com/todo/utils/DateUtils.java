package com.todo.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class DateUtils {

    public static List<Date> generateDateWithWeek(String startTime, String endTime, List<Integer> daysOfWeekInput) throws Exception {
        List<Date> result = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date startDate = sdf.parse(startTime);//开始时间（包含该天）
        Date endDate = sdf.parse(endTime);//结束时间(不包含该天)
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(endDate);
        calendar.add(Calendar.DATE, 1);
        endDate = calendar.getTime();

        while (startDate.before(endDate)) {
            calendar = Calendar.getInstance();
            calendar.setTime(startDate);

            int day = calendar.get(Calendar.DAY_OF_WEEK) - 1;

            if (day == 0) {
                day = 7;
            }

            if (daysOfWeekInput.contains(day)) {
                result.add(calendar.getTime());
            }

            calendar.add(Calendar.DATE, 1);
            startDate = calendar.getTime();
        }

        return result;
    }

    public static List<Date> generateDateWithMonth(String startTime, String endTime, List<Integer> daysOfMonthInput) throws Exception {
        List<Date> result = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date startDate = sdf.parse(startTime);//开始时间（包含该天）
        Date endDate = sdf.parse(endTime);//结束时间(不包含该天)
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(endDate);
        calendar.add(Calendar.DATE, 1);
        endDate = calendar.getTime();

        while (startDate.before(endDate)) {
            calendar = Calendar.getInstance();
            calendar.setTime(startDate);

            int day = calendar.get(Calendar.DAY_OF_MONTH);

            if (daysOfMonthInput.contains(day)) {
                result.add(calendar.getTime());
            }

            calendar.add(Calendar.DATE, 1);
            startDate = calendar.getTime();
        }

        return result;
    }

    public static List<Date> generateDateWithDay(String startTime, String endTime) throws Exception {
        List<Date> result = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date startDate = sdf.parse(startTime);//开始时间（包含该天）
        Date endDate = sdf.parse(endTime);//结束时间(不包含该天)
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(endDate);
        calendar.add(Calendar.DATE, 1);
        endDate = calendar.getTime();

        while (startDate.before(endDate)) {
            calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            result.add(calendar.getTime());
            calendar.add(Calendar.DATE, 1);
            startDate = calendar.getTime();
        }

        return result;
    }

    public static Date generateDateWithLocalDateAndLocalTime(LocalDate localDate, LocalTime localTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(localDate.getYear(), localDate.getMonth().getValue() - 1, localDate.getDayOfMonth(), localTime.getHour(), localTime.getMinute(), 0);
        return calendar.getTime();
    }

}
