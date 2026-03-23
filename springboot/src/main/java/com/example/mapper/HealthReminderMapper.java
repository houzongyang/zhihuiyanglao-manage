package com.example.mapper;

import com.example.entity.HealthReminder;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 操作health_reminder相关数据接口
 */
public interface HealthReminderMapper {

    /**
     * 新增
     */
    int insert(HealthReminder healthReminder);

    /**
     * 删除
     */
    int deleteById(Integer id);

    /**
     * 修改
     */
    int updateById(HealthReminder healthReminder);

    /**
     * 根据ID查询
     */
    HealthReminder selectById(Integer id);

    /**
     * 查询所有
     */
    List<HealthReminder> selectAll(HealthReminder healthReminder);

    /**
     * 根据接收人类型查询提醒列表
     */
    @Select("select * from health_reminder where receiver_type = #{receiverType} and status = 'ACTIVE' order by create_time desc")
    List<HealthReminder> selectByReceiverType(String receiverType);

    /**
     * 根据相关用户ID查询提醒列表
     */
    @Select("select * from health_reminder where related_user_id = #{relatedUserId} and status = 'ACTIVE' order by create_time desc")
    List<HealthReminder> selectByRelatedUserId(Integer relatedUserId);

    /**
     * 标记为已读
     */
    @Update("update health_reminder set is_read = 1, read_time = now() where id = #{id}")
    int markAsRead(Integer id);

    /**
     * 根据状态查询提醒列表
     */
    @Select("select * from health_reminder where status = #{status} order by create_time desc")
    List<HealthReminder> selectByStatus(String status);

    /**
     * 获取用户未读消息数量
     */
    @Select("select count(*) from health_reminder where (receiver_type = 'ALL' or receiver_type = #{userType}) and is_read = 0 and status = 'ACTIVE'")
    int getUnreadCount(@Param("userType") String userType);

    /**
     * 根据接收人类型和用户ID查询消息（包括全员消息）
     */
    @Select("select * from health_reminder where ((receiver_type = 'ALL') or (receiver_type = #{receiverType}) or (receiver_id = #{receiverId})) and status = 'ACTIVE' order by create_time desc")
    List<HealthReminder> selectByReceiverInfo(@Param("receiverType") String receiverType, @Param("receiverId") Integer receiverId);
}
