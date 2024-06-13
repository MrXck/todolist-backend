package com.todo.controller;

import com.todo.dto.mind.AddMindDTO;
import com.todo.dto.mind.GetMindByPageDTO;
import com.todo.dto.mind.MindDTO;
import com.todo.dto.mind.UpdateMindDTO;
import com.todo.service.MindService;
import com.todo.utils.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/mind")
public class MindController {

    @Autowired
    private MindService mindService;

    @GetMapping("/{mindId}")
    public MindDTO getById(@PathVariable("mindId") Long mindId) {
        return mindService.getMindById(mindId);
    }

    @Log
    @PostMapping("/add")
    public void add(@RequestBody @Valid AddMindDTO addMindDTO) {
        mindService.add(addMindDTO);
    }

    @PostMapping("/getByPage")
    public MindDTO getByPage(@RequestBody @Valid GetMindByPageDTO getMindByPageDTO) {
        return mindService.getByPage(getMindByPageDTO);
    }

    @Log
    @PostMapping("/remove/{mindId}")
    public void removeById(@PathVariable("mindId") Long mindId) {
        mindService.deleteById(mindId);
    }

    @Log
    @PostMapping("/update")
    public void update(@RequestBody @Valid UpdateMindDTO updateMindDTO) {
        mindService.updateMind(updateMindDTO);
    }

}
