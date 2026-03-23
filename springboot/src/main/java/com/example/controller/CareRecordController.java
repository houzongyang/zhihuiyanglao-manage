package com.example.controller;

import com.example.common.Result;
import com.example.entity.CareRecord;
import com.example.service.CareRecordService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 护理记录前端操作接口
 */
@RestController
@RequestMapping("/care-record")
public class CareRecordController {

    @Resource
    private CareRecordService careRecordService;

    /**
     * 新增护理记录
     */
    @PostMapping("/add")
    public Result add(@RequestBody CareRecord careRecord) {
        careRecordService.add(careRecord);
        return Result.success();
    }

    /**
     * 删除护理记录
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        careRecordService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除护理记录
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        careRecordService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改护理记录
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody CareRecord careRecord) {
        careRecordService.updateById(careRecord);
        return Result.success();
    }

    /**
     * 根据ID查询护理记录
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        CareRecord careRecord = careRecordService.selectById(id);
        return Result.success(careRecord);
    }

    /**
     * 查询所有护理记录
     */
    @GetMapping("/selectAll")
    public Result selectAll(CareRecord careRecord) {
        List<CareRecord> list = careRecordService.selectAll(careRecord);
        return Result.success(list);
    }

    /**
     * 分页查询护理记录
     */
    @GetMapping("/selectPage")
    public Result selectPage(CareRecord careRecord,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<CareRecord> page = careRecordService.selectPage(careRecord, pageNum, pageSize);
        return Result.success(page);
    }
}