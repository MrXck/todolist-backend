package com.todo.dto.operationLog;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class GetDateAndCountDTO {

    @NotEmpty(message = "开始时间不能为空")
    private String startDate;

    @NotEmpty(message = "结束时间不能为空")
    private String endDate;
}
