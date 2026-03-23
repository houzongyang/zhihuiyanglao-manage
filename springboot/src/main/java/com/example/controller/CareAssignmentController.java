package com.example.controller;

import com.example.common.Result;
import com.example.entity.CareAssignment;
import com.example.service.CareAssignmentService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 护理安排前端操作接口
 */
@RestController
@RequestMapping("/care-assignment")
public class CareAssignmentController {

    @Resource
    private CareAssignmentService careAssignmentService;

    /**
     * 新增护理安排
     */
    @PostMapping("/add")
    public Result add(@RequestBody CareAssignment careAssignment) {
        careAssignmentService.add(careAssignment);
        return Result.success();
    }

    /**
     * 删除护理安排
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        careAssignmentService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除护理安排
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        careAssignmentService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改护理安排
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody CareAssignment careAssignment) {
        careAssignmentService.updateById(careAssignment);
        return Result.success();
    }

    /**
     * 根据ID查询护理安排
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        CareAssignment careAssignment = careAssignmentService.selectById(id);
        return Result.success(careAssignment);
    }

    /**
     * 查询所有护理安排
     */
    @GetMapping("/selectAll")
    public Result selectAll(CareAssignment careAssignment) {
        List<CareAssignment> list = careAssignmentService.selectAll(careAssignment);
        return Result.success(list);
    }

    /**
     * 分页查询护理安排
     */
    @GetMapping("/selectPage")
    public Result selectPage(CareAssignment careAssignment,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<CareAssignment> page = careAssignmentService.selectPage(careAssignment, pageNum, pageSize);
        return Result.success(page);
    }
}