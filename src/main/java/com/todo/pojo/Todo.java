package com.todo.pojo;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class Todo {

    private Long id;
    private Long userId;
    private Long taskBoxId;
    private String title;
    private String detail;
    private Boolean isDone;
    private Integer priority;
    private LocalDate startTime;
    private LocalDate endTime;
    private LocalTime predictTime;
    private Boolean enableEmail;
    private Integer noticeType;
    private Integer cronNum;
    private Boolean isDelete;
    private LocalTime planStartTime;
    private LocalTime planEndTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private LocalDateTime startDoTime;
    private LocalDateTime endDoTime;
}
