package com.example.service;

import com.example.entity.HealthMonitoring;
import com.example.mapper.HealthMonitoringMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 健康监控业务处理
 **/
@Service
public class HealthMonitoringService {

    @Resource
    private HealthMonitoringMapper healthMonitoringMapper;

    /**
     * 新增健康监控记录
     */
    public void add(HealthMonitoring healthMonitoring) {
        healthMonitoringMapper.insert(healthMonitoring);
    }

    /**
     * 保存或更新健康监控记录
     */
    public void save(HealthMonitoring healthMonitoring) {
        if (healthMonitoring.getId() == null) {
            healthMonitoringMapper.insert(healthMonitoring);
        } else {
            healthMonitoringMapper.updateById(healthMonitoring);
        }
    }

    /**
     * 删除健康监控记录
     */
    public void deleteById(Integer id) {
        healthMonitoringMapper.deleteById(id);
    }

    /**
     * 批量删除健康监控记录
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            healthMonitoringMapper.deleteById(id);
        }
    }

    /**
     * 修改健康监控记录
     */
    public void updateById(HealthMonitoring healthMonitoring) {
        healthMonitoringMapper.updateById(healthMonitoring);
    }

    /**
     * 根据ID查询健康监控记录
     */
    public HealthMonitoring selectById(Integer id) {
        return healthMonitoringMapper.selectById(id);
    }

    /**
     * 查询所有健康监控记录
     */
    public List<HealthMonitoring> selectAll(HealthMonitoring healthMonitoring) {
        return healthMonitoringMapper.selectAll(healthMonitoring);
    }

    /**
     * 分页查询健康监控记录
     */
    public PageInfo<HealthMonitoring> selectPage(HealthMonitoring healthMonitoring, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<HealthMonitoring> list = healthMonitoringMapper.selectPageWithUserName(healthMonitoring);
        return PageInfo.of(list);
    }

    /**
     * 根据用户ID和日期查询当日记录
     */
    public List<HealthMonitoring> getDailyRecords(Integer userId, LocalDate date) {
        return healthMonitoringMapper.selectByUserIdAndDate(userId, date);
    }

    /**
     * 获取用户某月的日期列表（包含记录数量）
     */
    public List<Map<String, Object>> getDateList(Integer userId, String month) {
        return healthMonitoringMapper.getDateListByMonth(userId, month);
    }

    /**
     * 获取统计数据
     */
    public Map<String, Object> getStats() {
        return healthMonitoringMapper.getStats();
    }

    /**
     * 根据用户ID获取最新记录
     */
    public HealthMonitoring getLatestByUserId(Integer userId) {
        return healthMonitoringMapper.selectLatestByUserId(userId);
    }

    /**
     * 根据条件分页查询（管理员端用）
     */
    public PageInfo<HealthMonitoring> selectPageWithConditions(HealthMonitoring healthMonitoring, 
                                                              String userName, 
                                                              String startDate, 
                                                              String endDate,
                                                              Integer pageNum, 
                                                              Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        
        // 设置用户名查询条件
        if (userName != null && !userName.trim().isEmpty()) {
            healthMonitoring.setUserName(userName.trim());
        }
        
        // 这里可以添加日期范围查询的逻辑
        // 由于XML中已经处理了基本条件，这里主要是设置分页
        
        List<HealthMonitoring> list = healthMonitoringMapper.selectPageWithUserName(healthMonitoring);
        return PageInfo.of(list);
    }
} 