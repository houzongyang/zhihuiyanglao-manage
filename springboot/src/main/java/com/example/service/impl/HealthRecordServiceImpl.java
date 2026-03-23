package com.example.service.impl;

import com.example.entity.HealthRecord;
import com.example.mapper.HealthRecordMapper;
import com.example.service.HealthRecordService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 健康档案服务实现类
 */
@Service
public class HealthRecordServiceImpl implements HealthRecordService {
    
    @Resource
    private HealthRecordMapper healthRecordMapper;
    
    /**
     * 新增健康档案
     */
    @Override
    public void add(HealthRecord healthRecord) {
        healthRecord.setCreateTime(new Date());
        healthRecord.setUpdateTime(new Date());
        healthRecordMapper.insert(healthRecord);
    }
    
    /**
     * 根据ID删除健康档案
     */
    @Override
    public void deleteById(Integer id) {
        healthRecordMapper.deleteById(id);
    }
    
    /**
     * 批量删除健康档案
     */
    @Override
    public void deleteBatch(List<Integer> ids) {
        healthRecordMapper.deleteBatch(ids);
    }
    
    /**
     * 更新健康档案
     */
    @Override
    public void updateById(HealthRecord healthRecord) {
        healthRecord.setUpdateTime(new Date());
        healthRecordMapper.update(healthRecord);
    }
    
    /**
     * 根据ID查询健康档案
     */
    @Override
    public HealthRecord selectById(Integer id) {
        return healthRecordMapper.selectById(id);
    }
    
    /**
     * 根据用户ID查询健康档案
     */
    @Override
    public HealthRecord selectByUserId(Integer userId) {
        return healthRecordMapper.selectByUserId(userId);
    }
    
    /**
     * 查询所有健康档案（可带条件）
     */
    @Override
    public List<HealthRecord> selectAll(HealthRecord healthRecord) {
        return healthRecordMapper.selectAll(healthRecord);
    }
    
    /**
     * 分页查询健康档案
     */
    @Override
    public PageInfo<HealthRecord> selectPage(HealthRecord healthRecord, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<HealthRecord> list = healthRecordMapper.selectPage(healthRecord);
        return new PageInfo<>(list);
    }
} 