package com.todo.controller;

import com.todo.dto.timeThing.*;
import com.todo.service.TimeThingService;
import com.todo.utils.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/timeThing")
public class TimeThingController {

    @Autowired
    private TimeThingService timeThingService;

    @PostMapping("/page")
    public TimeThingDTO page(@RequestBody @Valid GetTimeThingByPageDTO dto) {
        return timeThingService.getByPage(dto);
    }

    @PostMapping("/date")
    public TimeThingDTO date(@RequestBody @Valid GetTimeThingByDateDTO dto) {
        return timeThingService.getByDate(dto);
    }

    @Log
    @PostMapping("/insert")
    public void insert(@RequestBody @Valid InsertDTO dto) {
        timeThingService.insert(dto);
    }

    @Log
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        timeThingService.deleteById(id);
    }

    @Log
    @PutMapping("/update")
    public void update(@RequestBody @Valid UpdateDTO dto) {
        timeThingService.edit(dto);
    }

}