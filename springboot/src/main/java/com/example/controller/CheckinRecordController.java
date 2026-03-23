package com.example.controller;

import com.example.common.Result;
import com.example.entity.CheckinRecord;
import com.example.service.CheckinRecordService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 入住信息记录前端操作接口
 */
@RestController
@RequestMapping("/checkinRecord")
public class CheckinRecordController {

    @Resource
    private CheckinRecordService checkinRecordService;

    /**
     * 新增入住记录
     */
    @PostMapping("/add")
    public Result add(@RequestBody CheckinRecord checkinRecord) {
        checkinRecordService.add(checkinRecord);
        return Result.success();
    }

    /**
     * 删除入住记录
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        checkinRecordService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除入住记录
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        checkinRecordService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改入住记录
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody CheckinRecord checkinRecord) {
        checkinRecordService.updateById(checkinRecord);
        return Result.success();
    }

    /**
     * 根据ID查询入住记录
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        CheckinRecord checkinRecord = checkinRecordService.selectById(id);
        return Result.success(checkinRecord);
    }

    /**
     * 查询所有入住记录
     */
    @GetMapping("/selectAll")
    public Result selectAll(CheckinRecord checkinRecord) {
        List<CheckinRecord> list = checkinRecordService.selectAll(checkinRecord);
        return Result.success(list);
    }

    /**
     * 分页查询入住记录
     */
    @GetMapping("/selectPage")
    public Result selectPage(CheckinRecord checkinRecord,
                            @RequestParam(defaultValue = "1") Integer pageNum,
                            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<CheckinRecord> pageInfo = checkinRecordService.selectPage(checkinRecord, pageNum, pageSize);
        return Result.success(pageInfo.getList(), (int)pageInfo.getTotal());
    }
} 