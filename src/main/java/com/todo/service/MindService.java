package com.todo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.todo.dto.mind.AddMindDTO;
import com.todo.dto.mind.GetMindByPageDTO;
import com.todo.dto.mind.MindDTO;
import com.todo.dto.mind.UpdateMindDTO;
import com.todo.pojo.Mind;

public interface MindService extends IService<Mind> {
    MindDTO getMindById(Long mindId);

    void add(AddMindDTO addMindDTO);

    MindDTO getByPage(GetMindByPageDTO getMindByPageDTO);

    void deleteById(Long mindId);

    void updateMind(UpdateMindDTO updateMindDTO);
}