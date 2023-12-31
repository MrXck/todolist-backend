package com.todo.controller;

import com.todo.dto.mind.AddMindDTO;
import com.todo.dto.mind.GetMindByPageDTO;
import com.todo.dto.mind.MindDTO;
import com.todo.dto.mind.UpdateMindDTO;
import com.todo.service.MindService;
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

    @PostMapping("/add")
    public String add(@RequestBody @Valid AddMindDTO addMindDTO) {
        mindService.add(addMindDTO);
        return "";
    }

    @PostMapping("/getByPage")
    public MindDTO getByPage(@RequestBody @Valid GetMindByPageDTO getMindByPageDTO) {
        return mindService.getByPage(getMindByPageDTO);
    }

    @PostMapping("/remove/{mindId}")
    public String removeById(@PathVariable("mindId") Long mindId) {
        mindService.deleteById(mindId);
        return "";
    }

    @PostMapping("/update")
    public String update(@RequestBody @Valid UpdateMindDTO updateMindDTO) {
        mindService.updateMind(updateMindDTO);
        return "";
    }

}
