package com.example.controller;

import com.example.common.Result;
import com.example.entity.HealthRecord;
import com.example.service.HealthRecordService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 健康档案前端操作接口
 **/
@RestController
@RequestMapping("/health-record")
public class HealthRecordController {

    @Resource
    private HealthRecordService healthRecordService;

    /**
     * 新增或更新健康档案
     */
    @PostMapping("/save")
    public Result save(@RequestBody HealthRecord healthRecord) {
        // 检查是否存在该用户的健康档案
        HealthRecord existing = healthRecordService.selectByUserId(healthRecord.getUserId());
        if (existing != null) {
            // 存在则更新
            healthRecord.setId(existing.getId());
            healthRecordService.updateById(healthRecord);
        } else {
            // 不存在则新增
            healthRecordService.add(healthRecord);
        }
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        healthRecordService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        healthRecordService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        HealthRecord healthRecord = healthRecordService.selectById(id);
        return Result.success(healthRecord);
    }

    /**
     * 根据用户ID查询
     */
    @GetMapping("/selectByUserId/{userId}")
    public Result selectByUserId(@PathVariable Integer userId) {
        HealthRecord healthRecord = healthRecordService.selectByUserId(userId);
        return Result.success(healthRecord);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(HealthRecord healthRecord) {
        List<HealthRecord> list = healthRecordService.selectAll(healthRecord);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(HealthRecord healthRecord,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<HealthRecord> page = healthRecordService.selectPage(healthRecord, pageNum, pageSize);
        return Result.success(page);
    }
} 