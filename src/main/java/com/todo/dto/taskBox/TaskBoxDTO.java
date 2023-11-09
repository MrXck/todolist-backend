package com.todo.dto.taskBox;

import com.todo.pojo.TaskBox;
import com.todo.pojo.Todo;
import lombok.Data;

import java.util.List;

@Data
public class TaskBoxDTO {

    private List<TaskBox> taskBoxes;

    private List<Todo> todos;
}
