package com.example.controller;

import com.example.common.Result;
import com.example.common.enums.ResultCodeEnum;
import com.example.entity.ActivityParticipant;
import com.example.service.ActivityParticipantService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 活动参与前端操作接口
 */
@RestController
@RequestMapping("/activityParticipant")
public class ActivityParticipantController {

    @Resource
    private ActivityParticipantService activityParticipantService;

    /**
     * 活动报名
     */
    @PostMapping("/register")
    public Result registerActivity(@RequestBody ActivityParticipant activityParticipant) {
        String result = activityParticipantService.registerActivity(activityParticipant);
        if ("报名成功".equals(result)) {
            return Result.success(result);
        } else {
            return Result.error(ResultCodeEnum.valueOf(result));
        }
    }

    /**
     * 检查用户是否已报名某活动
     */
    @GetMapping("/checkRegistration")
    public Result checkRegistration(@RequestParam Integer activityId, @RequestParam Integer userId) {
        boolean isRegistered = activityParticipantService.isUserRegistered(activityId, userId);
        return Result.success(isRegistered);
    }

    /**
     * 取消报名
     */
    @PostMapping("/cancel")
    public Result cancelRegistration(@RequestParam Integer activityId, @RequestParam Integer userId) {
        String result = activityParticipantService.cancelRegistration(activityId, userId);
        if ("取消报名成功".equals(result)) {
            return Result.success(result);
        } else {
            return Result.error(ResultCodeEnum.valueOf(result));
        }
    }

    /**
     * 查询用户的报名记录
     */
    @GetMapping("/userRegistrations/{userId}")
    public Result getUserRegistrations(@PathVariable Integer userId) {
        List<ActivityParticipant> list = activityParticipantService.getUserRegistrations(userId);
        return Result.success(list);
    }

    /**
     * 查询活动的参与者列表
     */
    @GetMapping("/activityParticipants/{activityId}")
    public Result getActivityParticipants(@PathVariable Integer activityId) {
        List<ActivityParticipant> list = activityParticipantService.getActivityParticipants(activityId);
        return Result.success(list);
    }

    /**
     * 新增活动参与记录（管理员用）
     */
    @PostMapping("/add")
    public Result add(@RequestBody ActivityParticipant activityParticipant) {
        String result = activityParticipantService.registerActivity(activityParticipant);
        if ("报名成功".equals(result)) {
            return Result.success();
        } else {
            return Result.error(ResultCodeEnum.valueOf(result));
        }
    }

    /**
     * 删除活动参与记录
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        activityParticipantService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除活动参与记录
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        activityParticipantService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改活动参与记录
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody ActivityParticipant activityParticipant) {
        activityParticipantService.updateById(activityParticipant);
        return Result.success();
    }

    /**
     * 根据ID查询活动参与记录
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        ActivityParticipant activityParticipant = activityParticipantService.selectById(id);
        return Result.success(activityParticipant);
    }

    /**
     * 查询所有活动参与记录
     */
    @GetMapping("/selectAll")
    public Result selectAll(ActivityParticipant activityParticipant) {
        List<ActivityParticipant> list = activityParticipantService.selectAll(activityParticipant);
        return Result.success(list);
    }

    /**
     * 分页查询活动参与记录
     */
    @GetMapping("/selectPage")
    public Result selectPage(ActivityParticipant activityParticipant,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<ActivityParticipant> page = activityParticipantService.selectPage(activityParticipant, pageNum, pageSize);
        return Result.success(page);
    }

    /**
     * 统计活动参与人数
     */
    @GetMapping("/count/{activityId}")
    public Result countByActivityId(@PathVariable Integer activityId) {
        int count = activityParticipantService.countByActivityId(activityId);
        return Result.success(count);
    }

    /**
     * 删除用户的所有活动报名记录
     */
    @DeleteMapping("/deleteByUserId/{userId}")
    public Result deleteByUserId(@PathVariable Integer userId) {
        activityParticipantService.deleteByUserId(userId);
        return Result.success();
    }
}