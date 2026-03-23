package com.example.controller;

import com.example.common.Result;
import com.example.entity.HealthReminder;
import com.example.service.HealthReminderService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 健康提醒信息前端操作接口
 **/
@RestController
@RequestMapping("/health-reminder")
public class HealthReminderController {

    @Resource
    private HealthReminderService healthReminderService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody HealthReminder healthReminder) {
        healthReminderService.add(healthReminder);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        healthReminderService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        healthReminderService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody HealthReminder healthReminder) {
        healthReminderService.updateById(healthReminder);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        HealthReminder healthReminder = healthReminderService.selectById(id);
        return Result.success(healthReminder);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(HealthReminder healthReminder) {
        List<HealthReminder> list = healthReminderService.selectAll(healthReminder);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(HealthReminder healthReminder,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<HealthReminder> page = healthReminderService.selectPage(healthReminder, pageNum, pageSize);
        return Result.success(page);
    }

    /**
     * 根据接收人类型查询提醒列表
     */
    @GetMapping("/selectByReceiverType/{receiverType}")
    public Result selectByReceiverType(@PathVariable String receiverType) {
        List<HealthReminder> list = healthReminderService.selectByReceiverType(receiverType);
        return Result.success(list);
    }

    /**
     * 根据相关用户ID查询提醒列表
     */
    @GetMapping("/selectByRelatedUserId/{relatedUserId}")
    public Result selectByRelatedUserId(@PathVariable Integer relatedUserId) {
        List<HealthReminder> list = healthReminderService.selectByRelatedUserId(relatedUserId);
        return Result.success(list);
    }

    /**
     * 标记为已读
     */
    @PutMapping("/markAsRead/{id}")
    public Result markAsRead(@PathVariable Integer id) {
        healthReminderService.markAsRead(id);
        return Result.success();
    }

    /**
     * 根据状态查询提醒列表
     */
    @GetMapping("/selectByStatus/{status}")
    public Result selectByStatus(@PathVariable String status) {
        List<HealthReminder> list = healthReminderService.selectByStatus(status);
        return Result.success(list);
    }

    /**
     * 获取用户未读消息数量
     */
    @GetMapping("/getUnreadCount")
    public Result getUnreadCount(@RequestParam String userType) {
        int count = healthReminderService.getUnreadCount(userType);
        return Result.success(count);
    }

    /**
     * 根据接收人类型和用户ID查询消息（包括全员消息）
     */
    @GetMapping("/selectByReceiverInfo")
    public Result selectByReceiverInfo(@RequestParam String receiverType, 
                                       @RequestParam(required = false) Integer receiverId) {
        List<HealthReminder> list = healthReminderService.selectByReceiverInfo(receiverType, receiverId);
        return Result.success(list);
    }
}
