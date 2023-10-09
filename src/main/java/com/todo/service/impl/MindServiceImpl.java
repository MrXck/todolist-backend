package com.todo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.todo.dto.mind.AddMindDTO;
import com.todo.dto.mind.GetMindByPageDTO;
import com.todo.dto.mind.MindDTO;
import com.todo.dto.mind.UpdateMindDTO;
import com.todo.mapper.MindMapper;
import com.todo.pojo.Mind;
import com.todo.service.MindService;
import com.todo.utils.UserThreadLocal;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MindServiceImpl extends ServiceImpl<MindMapper, Mind> implements MindService {
    @Override
    public MindDTO getMindById(Long mindId) {

        LambdaQueryWrapper<Mind> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Mind::getId, mindId);
        queryWrapper.eq(Mind::getUserId, UserThreadLocal.get());
        Mind mind = this.getOne(queryWrapper);

        MindDTO mindDTO = new MindDTO();
        mindDTO.setMind(mind);

        return mindDTO;
    }

    @Override
    public void add(AddMindDTO addMindDTO) {
        Mind mind = new Mind();
        mind.setName(addMindDTO.getName());
        mind.setContent(addMindDTO.getContent());
        mind.setUserId(UserThreadLocal.get());
        mind.setCreateTime(LocalDateTime.now());
        mind.setUpdateTime(LocalDateTime.now());
        this.save(mind);
    }

    @Override
    public MindDTO getByPage(GetMindByPageDTO getMindByPageDTO) {
        IPage<Mind> page = new Page<>(getMindByPageDTO.getPageNum(), getMindByPageDTO.getPageSize());
        String keyword = getMindByPageDTO.getKeyword();

        LambdaQueryWrapper<Mind> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Mind::getUserId, UserThreadLocal.get());
        queryWrapper.like(keyword != null && !keyword.isEmpty(), Mind::getName, keyword);
        queryWrapper.select(Mind.class, i -> !"content".equals(i.getColumn()));
        queryWrapper.orderByDesc(Mind::getCreateTime);

        MindDTO mindDTO = new MindDTO();
        mindDTO.setPage(this.page(page, queryWrapper));
        return mindDTO;
    }

    @Override
    public void deleteById(Long mindId) {
        LambdaQueryWrapper<Mind> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Mind::getUserId, UserThreadLocal.get());
        queryWrapper.eq(Mind::getId, mindId);

        this.remove(queryWrapper);
    }

    @Override
    public void updateMind(UpdateMindDTO updateMindDTO) {
        LambdaUpdateWrapper<Mind> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Mind::getUserId, UserThreadLocal.get());
        updateWrapper.eq(Mind::getId, updateMindDTO.getId());
        updateWrapper.set(Mind::getName, updateMindDTO.getName());
        updateWrapper.set(Mind::getContent, updateMindDTO.getContent());
        updateWrapper.set(Mind::getUpdateTime, LocalDateTime.now());

        this.update(updateWrapper);
    }
}