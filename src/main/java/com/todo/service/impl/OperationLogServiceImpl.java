package com.todo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.todo.dto.operationLog.GetDateAndCountDTO;
import com.todo.dto.operationLog.OperationLogDTO;
import com.todo.dto.operationLog.PageDTO;
import com.todo.mapper.OperationLogMapper;
import com.todo.pojo.OperationLog;
import com.todo.service.OperationLogService;
import com.todo.utils.UserThreadLocal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog> implements OperationLogService {
    @Override
    public OperationLogDTO getByPage(PageDTO dto) {
        Page<OperationLog> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        LambdaQueryWrapper<OperationLog> queryWrapper = new LambdaQueryWrapper<>();
        String keyword = dto.getKeyword();
        queryWrapper.orderByDesc(OperationLog::getUpdateTime);
        queryWrapper.orderByDesc(OperationLog::getCreateTime);
        queryWrapper.like(OperationLog::getPath, keyword);

        OperationLogDTO operationLogDTO = new OperationLogDTO();
        operationLogDTO.setPage(this.page(page, queryWrapper));
        return operationLogDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        this.removeById(id);
    }

    @Override
    public OperationLogDTO getDateAndCountByUserId(GetDateAndCountDTO dto) {
        LambdaQueryWrapper<OperationLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ge(OperationLog::getCreateTime, dto.getStartDate());
        queryWrapper.le(OperationLog::getCreateTime, dto.getEndDate());
        queryWrapper.eq(OperationLog::getUserId, UserThreadLocal.get());
        queryWrapper.select(OperationLog.class, i -> i.getColumn().equals("create_time"));
        List<OperationLog> list = this.list(queryWrapper);
        OperationLogDTO operationLogDTO = new OperationLogDTO();
        operationLogDTO.setList(list);
        return operationLogDTO;
    }
}
