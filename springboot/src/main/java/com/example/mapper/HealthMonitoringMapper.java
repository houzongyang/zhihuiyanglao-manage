package com.example.mapper;

import com.example.entity.HealthMonitoring;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 操作health_monitoring相关数据接口
 */
public interface HealthMonitoringMapper {

    /**
     * 新增
     */
    int insert(HealthMonitoring healthMonitoring);

    /**
     * 删除
     */
    int deleteById(Integer id);

    /**
     * 修改
     */
    int updateById(HealthMonitoring healthMonitoring);

    /**
     * 根据ID查询
     */
    HealthMonitoring selectById(Integer id);

    /**
     * 查询所有
     */
    List<HealthMonitoring> selectAll(HealthMonitoring healthMonitoring);

    /**
     * 根据用户ID和日期查询当日记录
     */
    List<HealthMonitoring> selectByUserIdAndDate(@Param("userId") Integer userId, @Param("date") LocalDate date);

    /**
     * 获取用户某月的日期列表（包含记录数量）
     */
    List<Map<String, Object>> getDateListByMonth(@Param("userId") Integer userId, @Param("month") String month);

    /**
     * 获取统计数据
     */
    Map<String, Object> getStats();

    /**
     * 根据条件分页查询（包含用户姓名）
     */
    List<HealthMonitoring> selectPageWithUserName(HealthMonitoring healthMonitoring);

    /**
     * 根据用户ID获取最新记录
     */
    HealthMonitoring selectLatestByUserId(Integer userId);
} 