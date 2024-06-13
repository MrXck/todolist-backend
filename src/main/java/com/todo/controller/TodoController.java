package com.todo.controller;

import com.todo.dto.todo.*;
import com.todo.service.TodoService;
import com.todo.utils.Log;
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

    @Log
    @PostMapping("/updateById")
    public void updateById(@RequestBody @Valid UpdateTodoDTO updateTodoDTO) {
        todoService.updateTodoById(updateTodoDTO);
    }

    @Log
    @PostMapping("/addTodo")
    public Long addTodo(@RequestBody @Valid AddTodoDTO addTodoDTO) {
        return todoService.add(addTodoDTO);
    }

    @Log
    @PostMapping("/removeTodoById/{todoId}")
    public void removeTodoById(@PathVariable Long todoId) {
        todoService.deleteById(todoId);
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

    @Log
    @PostMapping("/batchGenerateTodo")
    public void batchGenerateTodo(@RequestBody @Valid BatchGenerateTodoDTO batchGenerateTodoDTO) {
        todoService.batchGenerate(batchGenerateTodoDTO);
    }

    @Log
    @PostMapping("/startTodo/{todoId}")
    public void startTodo(@PathVariable Long todoId) {
        todoService.startTodo(todoId);
    }

    @Log
    @PostMapping("/endTodo/{todoId}")
    public void endTodo(@PathVariable Long todoId) {
        todoService.endTodo(todoId);
    }

}
