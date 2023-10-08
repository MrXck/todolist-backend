package com.todo.dto.todo;

import com.todo.pojo.Todo;
import lombok.Data;

import java.util.List;

@Data
public class TodoDTO {

    private List<Todo> list;

    private Integer count;
}
