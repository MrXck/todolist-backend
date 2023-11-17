package com.todo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.todo.dto.todo.*;
import com.todo.pojo.Todo;

public interface TodoService extends IService<Todo> {
    TodoDTO getByMonth(GetTodoDTO getTodoDTO);

    void updateTodoById(UpdateTodoDTO updateTodoDTO);

    Long add(AddTodoDTO addTodoDTO);

    void deleteById(Long todoId);

    TodoDTO getThisMonthTodo();

    TodoDTO getAllTodo();

    TodoDTO getTodayTodo();

    TodoDTO getDelayTodo();

    TodoDTO getDoneTodo();

    void batchGenerate(BatchGenerateTodoDTO batchGenerateTodoDTO);
}