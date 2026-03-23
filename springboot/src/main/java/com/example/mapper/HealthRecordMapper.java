package com.example.mapper;

import com.example.entity.HealthRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 健康档案Mapper接口
 */
@Mapper
public interface HealthRecordMapper {
    
    /**
     * 新增健康档案
     */
    void insert(HealthRecord healthRecord);
    
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
    void update(HealthRecord healthRecord);
    
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
    List<HealthRecord> selectPage(@Param("healthRecord") HealthRecord healthRecord);
} 