package com.example.service;

import com.example.entity.HealthRecord;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 健康档案服务接口
 */
public interface HealthRecordService {
    
    /**
     * 新增健康档案
     */
    void add(HealthRecord healthRecord);
    
    /**
     * 根据ID删除健康档案
     */
    void deleteById(Integer id);
    
    /**
     * 批量删除健康档案
     */
    void deleteBatch(List<Integer> ids);
    
    /**
     * 更新健康档案
     */
    void updateById(HealthRecord healthRecord);
    
    /**
     * 根据ID查询健康档案
     */
    HealthRecord selectById(Integer id);
    
    /**
     * 根据用户ID查询健康档案
     */
    HealthRecord selectByUserId(Integer userId);
    
    /**
     * 查询所有健康档案（可带条件）
     */
    List<HealthRecord> selectAll(HealthRecord healthRecord);
    
    /**
     * 分页查询健康档案
     */
    PageInfo<HealthRecord> selectPage(HealthRecord healthRecord, Integer pageNum, Integer pageSize);
} 