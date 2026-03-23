package com.example.controller;

import com.example.common.Result;
import com.example.entity.ActivityCategory;
import com.example.service.ActivityCategoryService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 活动分类前端操作接口
 */
@RestController
@RequestMapping("/activity/category")
public class ActivityCategoryController {

    @Resource
    private ActivityCategoryService activityCategoryService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody ActivityCategory activityCategory) {
        activityCategoryService.add(activityCategory);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        activityCategoryService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        activityCategoryService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody ActivityCategory activityCategory) {
        activityCategoryService.updateById(activityCategory);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        ActivityCategory activityCategory = activityCategoryService.selectById(id);
        return Result.success(activityCategory);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(ActivityCategory activityCategory) {
        List<ActivityCategory> list = activityCategoryService.selectAll(activityCategory);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(ActivityCategory activityCategory,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<ActivityCategory> page = activityCategoryService.selectPage(activityCategory, pageNum, pageSize);
        return Result.success(page);
    }
} 