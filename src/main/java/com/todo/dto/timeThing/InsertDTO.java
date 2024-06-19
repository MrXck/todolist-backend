package com.todo.dto.timeThing;

import com.todo.valid.ValidLocalDate;
import com.todo.valid.ValidLocalTime;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class InsertDTO {

    @NotEmpty(message = "标题不能为空")
    private String title;

    @NotEmpty(message = "内容不能为空")
    private String content;

    @ValidLocalDate(message = "时间不能为空")
    private LocalDate thingTime;

    @ValidLocalTime(message = "开始时间不能为空")
    private LocalTime startTime;

    @ValidLocalTime(message = "结束时间不能为空")
    private LocalTime endTime;

}