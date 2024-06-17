package com.todo.dto.todo;

import lombok.Data;

import java.util.List;

@Data
public class DeleteBatchDTO {

    private List<Long> todoIds;
}
