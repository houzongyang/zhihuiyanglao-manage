package com.example.controller;

import com.example.common.Result;
import com.example.entity.CarePlan;
import com.example.service.CarePlanService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 护理计划前端操作接口
 */
@RestController
@RequestMapping("/care-plan")
public class CarePlanController {

    @Resource
    private CarePlanService carePlanService;

    /**
     * 新增护理计划
     */
    @PostMapping("/add")
    public Result add(@RequestBody CarePlan carePlan) {
        carePlanService.add(carePlan);
        return Result.success();
    }

    /**
     * 删除护理计划
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        carePlanService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除护理计划
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        carePlanService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改护理计划
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody CarePlan carePlan) {
        carePlanService.updateById(carePlan);
        return Result.success();
    }

    /**
     * 根据ID查询护理计划
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        CarePlan carePlan = carePlanService.selectById(id);
        return Result.success(carePlan);
    }

    /**
     * 查询所有护理计划
     */
    @GetMapping("/selectAll")
    public Result selectAll(CarePlan carePlan) {
        List<CarePlan> list = carePlanService.selectAll(carePlan);
        return Result.success(list);
    }

    /**
     * 分页查询护理计划
     */
    @GetMapping("/selectPage")
    public Result selectPage(CarePlan carePlan,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<CarePlan> page = carePlanService.selectPage(carePlan, pageNum, pageSize);
        return Result.success(page);
    }
}