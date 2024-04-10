package com.todo.task;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class Schedule {
    private Long id;
    private String name;

    private Date startDate;

    private Date endDate;
}
