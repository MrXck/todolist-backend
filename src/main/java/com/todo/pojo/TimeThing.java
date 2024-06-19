package com.todo.pojo;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class TimeThing {

    private Long id;

    private Long userId;

    private String title;

    private String content;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private LocalDate thingTime;

    private LocalTime startTime;

    private LocalTime endTime;
}
