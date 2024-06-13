package com.todo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.todo.dto.operationLog.GetDateAndCountDTO;
import com.todo.dto.operationLog.OperationLogDTO;
import com.todo.dto.operationLog.PageDTO;
import com.todo.pojo.OperationLog;

public interface OperationLogService extends IService<OperationLog> {
    OperationLogDTO getByPage(PageDTO dto);

    void delete(Long id);

    OperationLogDTO getDateAndCountByUserId(GetDateAndCountDTO dto);
}
