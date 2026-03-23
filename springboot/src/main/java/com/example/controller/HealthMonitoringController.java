package com.example.controller;

import com.example.common.Result;
import com.example.entity.HealthMonitoring;
import com.example.service.HealthMonitoringService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 健康监控操作接口
 **/
@RestController
@RequestMapping("/health-monitoring")
public class HealthMonitoringController {

    @Resource
    private HealthMonitoringService healthMonitoringService;

    /**
     * 新增健康监控记录
     */
    @PostMapping("/add")
    public Result add(@RequestBody HealthMonitoring healthMonitoring) {
        healthMonitoringService.add(healthMonitoring);
        return Result.success();
    }

    /**
     * 保存健康监控记录（新增或更新）
     */
    @PostMapping("/save")
    public Result save(@RequestBody HealthMonitoring healthMonitoring) {
        healthMonitoringService.save(healthMonitoring);
        return Result.success();
    }

    /**
     * 删除健康监控记录
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        healthMonitoringService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除健康监控记录
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        healthMonitoringService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改健康监控记录（支持PUT和POST两种方式）
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody HealthMonitoring healthMonitoring) {
        healthMonitoringService.updateById(healthMonitoring);
        return Result.success();
    }

    /**
     * 修改健康监控记录（POST方式，兼容前端）
     */
    @PostMapping("/update")
    public Result updateByPost(@RequestBody HealthMonitoring healthMonitoring) {
        healthMonitoringService.updateById(healthMonitoring);
        return Result.success();
    }

    /**
     * 根据ID查询健康监控记录
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        HealthMonitoring healthMonitoring = healthMonitoringService.selectById(id);
        return Result.success(healthMonitoring);
    }

    /**
     * 查询所有健康监控记录
     */
    @GetMapping("/selectAll")
    public Result selectAll(HealthMonitoring healthMonitoring) {
        List<HealthMonitoring> list = healthMonitoringService.selectAll(healthMonitoring);
        return Result.success(list);
    }

    /**
     * 分页查询健康监控记录
     */
    @GetMapping("/selectPage")
    public Result selectPage(HealthMonitoring healthMonitoring,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<HealthMonitoring> page = healthMonitoringService.selectPage(healthMonitoring, pageNum, pageSize);
        return Result.success(page);
    }

    /**
     * 根据用户ID和日期查询当日记录
     */
    @GetMapping("/getDailyRecords")
    public Result getDailyRecords(@RequestParam Integer userId, 
                                  @RequestParam String date) {
        try {
            // 首先检查日期参数格式
            if (date == null || date.trim().isEmpty()) {
                return Result.error("400","日期参数不能为空");
            }
            
            LocalDate recordDate;
            
            // 检查是否是时间戳格式
            if (date.matches("^\\d{10,13}$")) {
                // 时间戳格式 - 转换为LocalDate
                long timestamp = Long.parseLong(date);
                // 如果是13位时间戳（毫秒），转换为秒
                if (date.length() == 13) {
                    timestamp = timestamp / 1000;
                }
                recordDate = LocalDate.ofInstant(
                    java.time.Instant.ofEpochSecond(timestamp), 
                    java.time.ZoneId.systemDefault()
                );
            } else {
                // 尝试解析为日期字符串 (YYYY-MM-DD 格式)
                recordDate = LocalDate.parse(date);
            }
            
            List<HealthMonitoring> list = healthMonitoringService.getDailyRecords(userId, recordDate);
            return Result.success(list);
        } catch (Exception e) {
            // 记录详细错误信息
            System.err.println("日期解析错误 - 输入参数: userId=" + userId + ", date=" + date);
            System.err.println("错误详情: " + e.getMessage());
            return Result.error("400","日期格式错误，请使用 YYYY-MM-DD 格式");
        }
    }

    /**
     * 获取用户某月的日期列表（包含记录数量）
     */
    @GetMapping("/getDateList")
    public Result getDateList(@RequestParam Integer userId, 
                              @RequestParam String month) {
        List<Map<String, Object>> list = healthMonitoringService.getDateList(userId, month);
        return Result.success(list);
    }

    /**
     * 获取统计数据
     */
    @GetMapping("/getStats")
    public Result getStats() {
        Map<String, Object> stats = healthMonitoringService.getStats();
        return Result.success(stats);
    }

    /**
     * 根据用户ID获取最新记录
     */
    @GetMapping("/getLatest/{userId}")
    public Result getLatestByUserId(@PathVariable Integer userId) {
        HealthMonitoring healthMonitoring = healthMonitoringService.getLatestByUserId(userId);
        return Result.success(healthMonitoring);
    }

    /**
     * 管理员端分页查询（支持复杂条件）
     */
    @GetMapping("/selectPageWithConditions")
    public Result selectPageWithConditions(HealthMonitoring healthMonitoring,
                                          @RequestParam(required = false) String userName,
                                          @RequestParam(required = false) String startDate,
                                          @RequestParam(required = false) String endDate,
                                          @RequestParam(defaultValue = "1") Integer pageNum,
                                          @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<HealthMonitoring> page = healthMonitoringService.selectPageWithConditions(
                healthMonitoring, userName, startDate, endDate, pageNum, pageSize);
        return Result.success(page);
    }
} 