package com.example.mapper;

import com.example.entity.Activity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 操作activity相关数据接口
 */
public interface ActivityMapper {

    /**
     * 新增
     */
    int insert(Activity activity);

    /**
     * 删除
     */
    int deleteById(Integer id);

    /**
     * 修改
     */
    int updateById(Activity activity);

    /**
     * 根据ID查询
     */
    Activity selectById(Integer id);

    /**
     * 查询所有
     */
    List<Activity> selectAll(Activity activity);

    /**
     * 根据分类ID查询活动列表
     */
    @Select("select * from activity where category_id = #{categoryId} order by activity_date desc")
    List<Activity> selectByCategoryId(Integer categoryId);

    /**
     * 根据状态查询活动列表
     */
    @Select("select * from activity where status = #{status} order by activity_date desc")
    List<Activity> selectByStatus(String status);

    /**
     * 更新活动参与人数
     */
    @Update("update activity set current_participants = #{count} where id = #{activityId}")
    int updateParticipantCount(@Param("activityId") Integer activityId, @Param("count") Integer count);

}