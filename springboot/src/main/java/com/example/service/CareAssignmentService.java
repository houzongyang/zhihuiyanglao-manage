package com.example.service;

import com.example.entity.CareAssignment;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 护理安排服务接口
 */
public interface CareAssignmentService {

    /**
     * 新增护理安排
     * 
     * @param careAssignment 护理安排信息
     */
    void add(CareAssignment careAssignment);

    /**
     * 根据ID删除护理安排
     * 
     * @param id 护理安排ID
     */
    void deleteById(Integer id);

    /**
     * 批量删除护理安排
     * 
     * @param ids 护理安排ID列表
     */
    void deleteBatch(List<Integer> ids);

    /**
     * 修改护理安排
     * 
     * @param careAssignment 护理安排信息
     */
    void updateById(CareAssignment careAssignment);

    /**
     * 根据ID查询护理安排
     * 
     * @param id 护理安排ID
     * @return 护理安排信息
     */
    CareAssignment selectById(Integer id);

    /**
     * 查询所有护理安排
     * 
     * @param careAssignment 查询条件
     * @return 护理安排列表
     */
    List<CareAssignment> selectAll(CareAssignment careAssignment);

    /**
     * 分页查询护理安排
     * 
     * @param careAssignment 查询条件
     * @param pageNum 页码
     * @param pageSize 页大小
     * @return 分页结果
     */
    PageInfo<CareAssignment> selectPage(CareAssignment careAssignment, Integer pageNum, Integer pageSize);
}