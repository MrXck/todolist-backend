package com.todo.controller;

import com.todo.dto.todo.*;
import com.todo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/todo")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @PostMapping("/getByMonth")
    public TodoDTO getByMonth(@RequestBody @Valid GetTodoDTO getTodoDTO) {
        return todoService.getByMonth(getTodoDTO);
    }

    @PostMapping("/updateById")
    public String updateById(@RequestBody @Valid UpdateTodoDTO updateTodoDTO) {
        todoService.updateTodoById(updateTodoDTO);
        return "";
    }

    @PostMapping("/addTodo")
    public Long addTodo(@RequestBody @Valid AddTodoDTO addTodoDTO) {
        return todoService.add(addTodoDTO);
    }

    @PostMapping("/removeTodoById/{todoId}")
    public String removeTodoById(@PathVariable Long todoId) {
        todoService.deleteById(todoId);
        return "";
    }

    @GetMapping("/getThisMonthTodo")
    public TodoDTO getThisMonthTodo() {
        return todoService.getThisMonthTodo();
    }

    @GetMapping("/getAllTodo")
    public TodoDTO getAllTodo() {
        return todoService.getAllTodo();
    }

    @GetMapping("/getDoneTodo")
    public TodoDTO getDoneTodo() {
        return todoService.getDoneTodo();
    }

    @GetMapping("/getTodayTodo")
    public TodoDTO getTodayTodo() {
        return todoService.getTodayTodo();
    }

    @GetMapping("/getDelayTodo")
    public TodoDTO getDelayTodo() {
        return todoService.getDelayTodo();
    }

}
