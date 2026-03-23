package com.example.controller;

import com.example.common.Result;
import com.example.entity.UserMedication;
import com.example.service.UserMedicationService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 用户药物管理前端操作接口
 **/
@RestController
@RequestMapping("/user-medication")
public class UserMedicationController {

    @Resource
    private UserMedicationService userMedicationService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody UserMedication userMedication) {
        userMedicationService.add(userMedication);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        userMedicationService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        userMedicationService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody UserMedication userMedication) {
        userMedicationService.updateById(userMedication);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        UserMedication userMedication = userMedicationService.selectById(id);
        return Result.success(userMedication);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(UserMedication userMedication) {
        List<UserMedication> list = userMedicationService.selectAll(userMedication);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(UserMedication userMedication,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<UserMedication> page = userMedicationService.selectPage(userMedication, pageNum, pageSize);
        return Result.success(page);
    }

    /**
     * 根据用户ID查询药物列表
     */
    @GetMapping("/selectByUserId/{userId}")
    public Result selectByUserId(@PathVariable Integer userId) {
        List<UserMedication> list = userMedicationService.selectByUserId(userId);
        return Result.success(list);
    }

    /**
     * 根据用户ID分页查询药物列表
     */
    @GetMapping("/selectPageByUserId/{userId}")
    public Result selectPageByUserId(@PathVariable Integer userId,
                                     UserMedication userMedication,
                                     @RequestParam(defaultValue = "1") Integer pageNum,
                                     @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<UserMedication> page = userMedicationService.selectPageByUserId(userId, userMedication, pageNum, pageSize);
        return Result.success(page);
    }

    /**
     * 根据状态查询药物列表
     */
    @GetMapping("/selectByStatus/{status}")
    public Result selectByStatus(@PathVariable String status) {
        List<UserMedication> list = userMedicationService.selectByStatus(status);
        return Result.success(list);
    }

    /**
     * 根据药物类型查询药物列表
     */
    @GetMapping("/selectByMedicationType/{medicationType}")
    public Result selectByMedicationType(@PathVariable String medicationType) {
        List<UserMedication> list = userMedicationService.selectByMedicationType(medicationType);
        return Result.success(list);
    }

    /**
     * 根据用户ID和状态查询药物列表
     */
    @GetMapping("/selectByUserIdAndStatus/{userId}/{status}")
    public Result selectByUserIdAndStatus(@PathVariable Integer userId, @PathVariable String status) {
        List<UserMedication> list = userMedicationService.selectByUserIdAndStatus(userId, status);
        return Result.success(list);
    }

    /**
     * 停用药物
     */
    @PutMapping("/stop/{id}")
    public Result stopMedication(@PathVariable Integer id, @RequestParam(required = false) String stopReason) {
        if (stopReason == null || stopReason.trim().isEmpty()) {
            stopReason = "用户主动停药";
        }
        userMedicationService.stopMedication(id, stopReason);
        return Result.success();
    }

    /**
     * 获取用户药物统计信息
     */
    @GetMapping("/stats/{userId}")
    public Result getMedicationStats(@PathVariable Integer userId) {
        Map<String, Object> stats = userMedicationService.getMedicationStats(userId);
        return Result.success(stats);
    }

    /**
     * 查询用户需要关注的药物
     */
    @GetMapping("/attention/{userId}")
    public Result selectAttentionMedications(@PathVariable Integer userId) {
        List<UserMedication> list = userMedicationService.selectAttentionMedications(userId);
        return Result.success(list);
    }

}