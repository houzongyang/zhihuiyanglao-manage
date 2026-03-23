package com.example.service;

import com.example.entity.ActivityParticipant;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 活动参与服务接口
 */
public interface ActivityParticipantService {

    /**
     * 活动报名
     * @param activityParticipant 活动参与信息
     * @return 报名结果
     */
    String registerActivity(ActivityParticipant activityParticipant);

    /**
     * 检查用户是否已报名某活动
     * @param activityId 活动ID
     * @param userId 用户ID
     * @return 是否已报名
     */
    boolean isUserRegistered(Integer activityId, Integer userId);

    /**
     * 取消报名
     * @param activityId 活动ID
     * @param userId 用户ID
     * @return 取消结果
     */
    String cancelRegistration(Integer activityId, Integer userId);

    /**
     * 查询用户的报名记录
     * @param userId 用户ID
     * @return 报名记录列表
     */
    List<ActivityParticipant> getUserRegistrations(Integer userId);

    /**
     * 查询活动的参与者列表
     * @param activityId 活动ID
     * @return 参与者列表
     */
    List<ActivityParticipant> getActivityParticipants(Integer activityId);

    /**
     * 分页查询活动参与记录
     * @param activityParticipant 查询条件
     * @param pageNum 页码
     * @param pageSize 页大小
     * @return 分页结果
     */
    PageInfo<ActivityParticipant> selectPage(ActivityParticipant activityParticipant, Integer pageNum, Integer pageSize);

    /**
     * 根据ID查询活动参与记录
     * @param id 记录ID
     * @return 活动参与记录
     */
    ActivityParticipant selectById(Integer id);

    /**
     * 查询所有活动参与记录
     * @param activityParticipant 查询条件
     * @return 活动参与记录列表
     */
    List<ActivityParticipant> selectAll(ActivityParticipant activityParticipant);

    /**
     * 更新活动参与记录
     * @param activityParticipant 活动参与信息
     */
    void updateById(ActivityParticipant activityParticipant);

    /**
     * 删除活动参与记录
     * @param id 记录ID
     */
    void deleteById(Integer id);

    /**
     * 批量删除活动参与记录
     * @param ids 记录ID列表
     */
    void deleteBatch(List<Integer> ids);

    /**
     * 统计活动参与人数
     * @param activityId 活动ID
     * @return 参与人数
     */
    int countByActivityId(Integer activityId);

    /**
     * 删除用户的所有活动报名记录
     * @param userId 用户ID
     */
    void deleteByUserId(Integer userId);
}