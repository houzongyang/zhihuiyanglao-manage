package com.example.service.impl;

import com.example.entity.Activity;
import com.example.entity.ActivityParticipant;
import com.example.mapper.ActivityParticipantMapper;
import com.example.service.ActivityParticipantService;
import com.example.service.ActivityService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 活动参与服务实现类
 */
@Service
public class ActivityParticipantServiceImpl implements ActivityParticipantService {

    @Resource
    private ActivityParticipantMapper activityParticipantMapper;

    @Resource
    private ActivityService activityService;

    @Override
    @Transactional
    public String registerActivity(ActivityParticipant activityParticipant) {
        try {
            // 1. 检查活动是否存在
            Activity activity = activityService.selectById(activityParticipant.getActivityId());
            if (activity == null) {
                return "活动不存在";
            }

            // 2. 检查活动状态
            if (!"PLANNED".equals(activity.getStatus())) {
                return "活动状态不允许报名";
            }

            // 3. 检查报名截止时间
            if (activity.getRegistrationDeadline() != null && 
                activity.getRegistrationDeadline().isBefore(LocalDateTime.now())) {
                return "报名时间已截止";
            }

            // 4. 检查用户是否已报名
            if (isUserRegistered(activityParticipant.getActivityId(), activityParticipant.getUserId())) {
                return "您已报名该活动";
            }

            // 5. 检查活动是否已满员
            if (activity.getMaxParticipants() != null && activity.getMaxParticipants() > 0) {
                int currentCount = countByActivityId(activityParticipant.getActivityId());
                if (currentCount >= activity.getMaxParticipants()) {
                    return "活动已满员";
                }
            }

            // 6. 设置默认值
            activityParticipant.setRegistrationTime(LocalDateTime.now());
            activityParticipant.setAttendanceStatus("REGISTERED");
            activityParticipant.setCreateTime(LocalDateTime.now());
            activityParticipant.setUpdateTime(LocalDateTime.now());

            // 7. 保存报名记录
            activityParticipantMapper.insert(activityParticipant);

            // 8. 更新活动参与人数
            int newCount = countByActivityId(activityParticipant.getActivityId());
            activityService.updateParticipantCount(activityParticipant.getActivityId(), newCount);

            return "报名成功";
        } catch (Exception e) {
            throw new RuntimeException("报名失败: " + e.getMessage());
        }
    }

    @Override
    public boolean isUserRegistered(Integer activityId, Integer userId) {
        ActivityParticipant participant = activityParticipantMapper.selectByActivityAndUser(activityId, userId);
        return participant != null && !"CANCELLED".equals(participant.getAttendanceStatus());
    }

    @Override
    @Transactional
    public String cancelRegistration(Integer activityId, Integer userId) {
        try {
            // 直接取消报名（软删除）
            activityParticipantMapper.cancelRegistration(activityId, userId);

            // 更新活动参与人数
            int newCount = countByActivityId(activityId);
            activityService.updateParticipantCount(activityId, newCount);

            return "取消报名成功";
        } catch (Exception e) {
            throw new RuntimeException("取消报名失败: " + e.getMessage());
        }
    }

    @Override
    public List<ActivityParticipant> getUserRegistrations(Integer userId) {
        return activityParticipantMapper.selectByUserId(userId);
    }

    @Override
    public List<ActivityParticipant> getActivityParticipants(Integer activityId) {
        return activityParticipantMapper.selectByActivityId(activityId);
    }

    @Override
    public PageInfo<ActivityParticipant> selectPage(ActivityParticipant activityParticipant, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<ActivityParticipant> list = activityParticipantMapper.selectPage(activityParticipant);
        return new PageInfo<>(list);
    }

    @Override
    public ActivityParticipant selectById(Integer id) {
        return activityParticipantMapper.selectById(id);
    }

    @Override
    public List<ActivityParticipant> selectAll(ActivityParticipant activityParticipant) {
        return activityParticipantMapper.selectAll(activityParticipant);
    }

    @Override
    @Transactional
    public void updateById(ActivityParticipant activityParticipant) {
        activityParticipant.setUpdateTime(LocalDateTime.now());
        activityParticipantMapper.updateById(activityParticipant);
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        activityParticipantMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteBatch(List<Integer> ids) {
        activityParticipantMapper.deleteBatch(ids);
    }

    @Override
    public int countByActivityId(Integer activityId) {
        return activityParticipantMapper.countByActivityId(activityId);
    }

    @Override
    @Transactional
    public void deleteByUserId(Integer userId) {
        activityParticipantMapper.deleteByUserId(userId);
    }
}