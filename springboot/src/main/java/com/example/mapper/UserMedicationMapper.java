package com.example.mapper;

import com.example.entity.UserMedication;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 操作user_medication相关数据接口
 */
public interface UserMedicationMapper {

    /**
     * 新增
     */
    int insert(UserMedication userMedication);

    /**
     * 删除
     */
    int deleteById(Integer id);

    /**
     * 修改
     */
    int updateById(UserMedication userMedication);

    /**
     * 根据ID查询
     */
    UserMedication selectById(Integer id);

    /**
     * 查询所有
     */
    List<UserMedication> selectAll(UserMedication userMedication);

    /**
     * 根据用户ID查询药物列表
     */
    @Select("select * from user_medication where user_id = #{userId} order by create_time desc")
    List<UserMedication> selectByUserId(Integer userId);

    /**
     * 根据状态查询药物列表
     */
    @Select("select * from user_medication where status = #{status} order by create_time desc")
    List<UserMedication> selectByStatus(String status);

    /**
     * 根据药物类型查询药物列表
     */
    @Select("select * from user_medication where medication_type = #{medicationType} order by create_time desc")
    List<UserMedication> selectByMedicationType(String medicationType);

    /**
     * 根据用户ID和状态查询药物列表
     */
    @Select("select * from user_medication where user_id = #{userId} and status = #{status} order by create_time desc")
    List<UserMedication> selectByUserIdAndStatus(@Param("userId") Integer userId, @Param("status") String status);

    /**
     * 停用药物
     */
    @Update("update user_medication set status = 'STOPPED', stop_reason = #{stopReason}, update_time = now() where id = #{id}")
    int stopMedication(@Param("id") Integer id, @Param("stopReason") String stopReason);

    /**
     * 统计用户的药物数量（按状态）
     */
    @Select("select count(*) from user_medication where user_id = #{userId} and status = #{status}")
    int countByUserIdAndStatus(@Param("userId") Integer userId, @Param("status") String status);

    /**
     * 统计用户今日需要用药的数量
     */
    @Select("select count(*) from user_medication where user_id = #{userId} and status = 'ACTIVE' and (end_date is null or end_date >= curdate())")
    int countTodayMedications(@Param("userId") Integer userId);

    /**
     * 查询用户需要关注的药物（即将结束的药物）
     */
    @Select("select * from user_medication where user_id = #{userId} and status = 'ACTIVE' and end_date is not null and end_date <= date_add(curdate(), interval 7 day) order by end_date asc")
    List<UserMedication> selectAttentionMedications(@Param("userId") Integer userId);

}