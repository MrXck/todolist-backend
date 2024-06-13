package com.todo.controller;

import com.todo.dto.operationLog.GetDateAndCountDTO;
import com.todo.dto.operationLog.OperationLogDTO;
import com.todo.service.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/operationLog")
public class OperationLogController {

    @Autowired
    private OperationLogService operationLogService;

//    @PostMapping("/page")
//    public OperationLogDTO page(@RequestBody @Valid PageDTO dto) {
//        return operationLogService.getByPage(dto);
//    }

//    @Log
//    @DeleteMapping("/{id}")
//    public void delete(@PathVariable("id") Long id) {
//        operationLogService.delete(id);
//    }

    @PostMapping("/getDateAndCountByUserId")
    public OperationLogDTO getDateAndCountByUserId(@RequestBody GetDateAndCountDTO dto) {
        return operationLogService.getDateAndCountByUserId(dto);
    }

}
