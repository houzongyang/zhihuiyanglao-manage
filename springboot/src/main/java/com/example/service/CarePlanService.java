package com.example.service;

import com.example.entity.CarePlan;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 护理计划服务接口
 */
public interface CarePlanService {

    /**
     * 新增护理计划
     * 
     * @param carePlan 护理计划信息
     */
    void add(CarePlan carePlan);

    /**
     * 根据ID删除护理计划
     * 
     * @param id 护理计划ID
     */
    void deleteById(Integer id);

    /**
     * 批量删除护理计划
     * 
     * @param ids 护理计划ID列表
     */
    void deleteBatch(List<Integer> ids);

    /**
     * 修改护理计划
     * 
     * @param carePlan 护理计划信息
     */
    void updateById(CarePlan carePlan);

    /**
     * 根据ID查询护理计划
     * 
     * @param id 护理计划ID
     * @return 护理计划信息
     */
    CarePlan selectById(Integer id);

    /**
     * 查询所有护理计划
     * 
     * @param carePlan 查询条件
     * @return 护理计划列表
     */
    List<CarePlan> selectAll(CarePlan carePlan);

    /**
     * 分页查询护理计划
     * 
     * @param carePlan 查询条件
     * @param pageNum 页码
     * @param pageSize 页大小
     * @return 分页结果
     */
    PageInfo<CarePlan> selectPage(CarePlan carePlan, Integer pageNum, Integer pageSize);
}