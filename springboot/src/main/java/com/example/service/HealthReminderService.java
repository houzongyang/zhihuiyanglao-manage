package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.entity.HealthReminder;
import com.example.mapper.HealthReminderMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 健康提醒信息业务处理
 **/
@Service
public class HealthReminderService {

    @Resource
    private HealthReminderMapper healthReminderMapper;

    /**
     * 新增
     */
    public void add(HealthReminder healthReminder) {
        if (ObjectUtil.isEmpty(healthReminder.getStatus())) {
            healthReminder.setStatus("ACTIVE");
        }
        if (ObjectUtil.isEmpty(healthReminder.getIsRead())) {
            healthReminder.setIsRead(0);
        }
        if (ObjectUtil.isEmpty(healthReminder.getPriority())) {
            healthReminder.setPriority("NORMAL");
        }
        if (ObjectUtil.isEmpty(healthReminder.getReminderType())) {
            healthReminder.setReminderType("HEALTH_ALERT");
        }
        if (ObjectUtil.isEmpty(healthReminder.getReceiverType())) {
            healthReminder.setReceiverType("ALL");
        }
        healthReminder.setCreateTime(LocalDateTime.now());
        healthReminder.setUpdateTime(LocalDateTime.now());
        healthReminderMapper.insert(healthReminder);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        healthReminderMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            healthReminderMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(HealthReminder healthReminder) {
        healthReminder.setUpdateTime(LocalDateTime.now());
        healthReminderMapper.updateById(healthReminder);
    }

    /**
     * 根据ID查询
     */
    public HealthReminder selectById(Integer id) {
        return healthReminderMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<HealthReminder> selectAll(HealthReminder healthReminder) {
        return healthReminderMapper.selectAll(healthReminder);
    }

    /**
     * 分页查询
     */
    public PageInfo<HealthReminder> selectPage(HealthReminder healthReminder, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<HealthReminder> list = healthReminderMapper.selectAll(healthReminder);
        return PageInfo.of(list);
    }

    /**
     * 根据接收人类型查询提醒列表
     */
    public List<HealthReminder> selectByReceiverType(String receiverType) {
        return healthReminderMapper.selectByReceiverType(receiverType);
    }

    /**
     * 根据相关用户ID查询提醒列表
     */
    public List<HealthReminder> selectByRelatedUserId(Integer relatedUserId) {
        return healthReminderMapper.selectByRelatedUserId(relatedUserId);
    }

    /**
     * 标记为已读
     */
    public void markAsRead(Integer id) {
        healthReminderMapper.markAsRead(id);
    }

    /**
     * 根据状态查询提醒列表
     */
    public List<HealthReminder> selectByStatus(String status) {
        return healthReminderMapper.selectByStatus(status);
    }

    /**
     * 获取用户未读消息数量
     */
    public int getUnreadCount(String userType) {
        return healthReminderMapper.getUnreadCount(userType);
    }

    /**
     * 根据接收人类型和用户ID查询消息（包括全员消息）
     */
    public List<HealthReminder> selectByReceiverInfo(String receiverType, Integer receiverId) {
        return healthReminderMapper.selectByReceiverInfo(receiverType, receiverId);
    }
}
