package com.example.mapper;

import com.example.entity.ActivityParticipant;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Delete;

import java.util.List;

/**
 * 操作activity_participant相关数据接口
 */
public interface ActivityParticipantMapper {

    /**
     * 新增参与记录
     */
    int insert(ActivityParticipant activityParticipant);

    /**
     * 删除参与记录
     */
    int deleteById(Integer id);

    /**
     * 批量删除参与记录
     */
    int deleteBatch(List<Integer> ids);

    /**
     * 修改参与记录
     */
    int updateById(ActivityParticipant activityParticipant);

    /**
     * 根据ID查询
     */
    ActivityParticipant selectById(Integer id);

    /**
     * 查询所有参与记录
     */
    List<ActivityParticipant> selectAll(ActivityParticipant activityParticipant);

    /**
     * 分页查询参与记录
     */
    List<ActivityParticipant> selectPage(ActivityParticipant activityParticipant);

    /**
     * 根据活动ID和用户ID查询参与记录
     */
    ActivityParticipant selectByActivityAndUser(@Param("activityId") Integer activityId, @Param("userId") Integer userId);

    /**
     * 根据用户ID查询参与记录
     */
    List<ActivityParticipant> selectByUserId(Integer userId);

    /**
     * 根据活动ID查询参与记录
     */
    List<ActivityParticipant> selectByActivityId(Integer activityId);

    /**
     * 统计活动参与人数
     */
    Integer countByActivityId(Integer activityId);

    /**
     * 取消报名（软删除，更新状态为CANCELLED）
     */
    int cancelRegistration(@Param("activityId") Integer activityId, @Param("userId") Integer userId);

    /**
     * 删除用户的活动报名记录
     */
    @Delete("delete from activity_participant where activity_id = #{activityId} and user_id = #{userId}")
    int deleteByActivityAndUser(@Param("activityId") Integer activityId, @Param("userId") Integer userId);

    /**
     * 根据用户ID删除所有活动参与记录
     */
    int deleteByUserId(Integer userId);
}