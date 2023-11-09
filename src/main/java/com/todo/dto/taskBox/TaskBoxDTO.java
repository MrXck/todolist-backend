package com.todo.dto.taskBox;

import com.todo.pojo.TaskBox;
import lombok.Data;

import java.util.List;

@Data
public class TaskBoxDTO {

    private List<TaskBox> taskBoxes;
}
