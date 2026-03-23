package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.entity.UserMedication;
import com.example.mapper.UserMedicationMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户药物管理业务处理
 **/
@Service
public class UserMedicationService {

    @Resource
    private UserMedicationMapper userMedicationMapper;

    /**
     * 新增
     */
    public void add(UserMedication userMedication) {
        if (ObjectUtil.isEmpty(userMedication.getStatus())) {
            userMedication.setStatus("ACTIVE");
        }
        if (ObjectUtil.isEmpty(userMedication.getMealRelation())) {
            userMedication.setMealRelation("WITH_MEAL");
        }
        userMedication.setCreateTime(LocalDateTime.now());
        userMedication.setUpdateTime(LocalDateTime.now());
        userMedicationMapper.insert(userMedication);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        userMedicationMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            userMedicationMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(UserMedication userMedication) {
        userMedication.setUpdateTime(LocalDateTime.now());
        userMedicationMapper.updateById(userMedication);
    }

    /**
     * 根据ID查询
     */
    public UserMedication selectById(Integer id) {
        return userMedicationMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<UserMedication> selectAll(UserMedication userMedication) {
        return userMedicationMapper.selectAll(userMedication);
    }

    /**
     * 分页查询
     */
    public PageInfo<UserMedication> selectPage(UserMedication userMedication, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<UserMedication> list = userMedicationMapper.selectAll(userMedication);
        return PageInfo.of(list);
    }

    /**
     * 根据用户ID查询药物列表
     */
    public List<UserMedication> selectByUserId(Integer userId) {
        return userMedicationMapper.selectByUserId(userId);
    }

    /**
     * 根据状态查询药物列表
     */
    public List<UserMedication> selectByStatus(String status) {
        return userMedicationMapper.selectByStatus(status);
    }

    /**
     * 根据药物类型查询药物列表
     */
    public List<UserMedication> selectByMedicationType(String medicationType) {
        return userMedicationMapper.selectByMedicationType(medicationType);
    }

    /**
     * 根据用户ID和状态查询药物列表
     */
    public List<UserMedication> selectByUserIdAndStatus(Integer userId, String status) {
        return userMedicationMapper.selectByUserIdAndStatus(userId, status);
    }

    /**
     * 停用药物
     */
    public void stopMedication(Integer id, String stopReason) {
        userMedicationMapper.stopMedication(id, stopReason);
    }

    /**
     * 获取用户药物统计信息
     */
    public Map<String, Object> getMedicationStats(Integer userId) {
        Map<String, Object> stats = new HashMap<>();
        
        // 进行中的药物数量
        int activeCount = userMedicationMapper.countByUserIdAndStatus(userId, "ACTIVE");
        stats.put("active", activeCount);
        
        // 已完成的药物数量
        int completedCount = userMedicationMapper.countByUserIdAndStatus(userId, "COMPLETED");
        stats.put("completed", completedCount);
        
        // 已停用的药物数量
        int stoppedCount = userMedicationMapper.countByUserIdAndStatus(userId, "STOPPED");
        stats.put("stopped", stoppedCount);
        
        // 今日需要用药的数量
        int todayCount = userMedicationMapper.countTodayMedications(userId);
        stats.put("today", todayCount);
        
        // 需要关注的药物（即将结束的药物）
        List<UserMedication> attentionMedications = userMedicationMapper.selectAttentionMedications(userId);
        stats.put("attention", attentionMedications.size());
        
        // 药物类型统计
        List<UserMedication> allActiveMedications = userMedicationMapper.selectByUserIdAndStatus(userId, "ACTIVE");
        long typeCount = allActiveMedications.stream()
                .map(UserMedication::getMedicationType)
                .distinct()
                .count();
        stats.put("types", typeCount);
        
        return stats;
    }

    /**
     * 查询用户需要关注的药物
     */
    public List<UserMedication> selectAttentionMedications(Integer userId) {
        return userMedicationMapper.selectAttentionMedications(userId);
    }

    /**
     * 根据用户ID分页查询药物列表
     */
    public PageInfo<UserMedication> selectPageByUserId(Integer userId, UserMedication userMedication, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        if (userMedication == null) {
            userMedication = new UserMedication();
        }
        userMedication.setUserId(userId);
        List<UserMedication> list = userMedicationMapper.selectAll(userMedication);
        return PageInfo.of(list);
    }

}