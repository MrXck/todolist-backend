package com.todo.dto.todo;

import com.todo.valid.ValidLocalDate;
import com.todo.valid.ValidLocalTime;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AddTodoDTO {

    @NotEmpty(message = "标题不能为空")
    private String title;

    private String detail;

    private Long taskBoxId;

    @NotNull(message = "是否完成不能为空")
    private Boolean isDone;

    @NotNull(message = "重要程度不能为空")
    @Min(value = 1, message = "不能小于1")
    @Max(value = 5, message = "不能大于5")
    private Integer priority;

    @ValidLocalDate(message = "开始时间不能为空")
    private LocalDate startTime;

    @ValidLocalDate(message = "结束时间不能为空")
    private LocalDate endTime;

    @ValidLocalTime(message = "预计开始时间不能为空")
    private LocalTime predictTime;

    @NotNull(message = "是否开启邮件提醒不能为空")
    private Boolean enableEmail;

    private Integer noticeType;

    private Integer cronNum;

    private LocalTime planStartTime;

    private LocalTime planEndTime;
}
