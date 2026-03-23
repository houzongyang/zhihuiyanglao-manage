package com.example.mapper;

import com.example.entity.ActivityCategory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 活动分类Mapper接口
 */
@Mapper
public interface ActivityCategoryMapper {
    
    /**
     * 新增活动分类
     */
    void insert(ActivityCategory activityCategory);
    
    /**
     * 删除活动分类
     */
    void deleteById(Integer id);
    
    /**
     * 批量删除活动分类
     */
    void deleteBatch(List<Integer> ids);
    
    /**
     * 修改活动分类
     */
    void updateById(ActivityCategory activityCategory);
    
    /**
     * 根据ID查询
     */
    ActivityCategory selectById(Integer id);
    
    /**
     * 查询所有
     */
    List<ActivityCategory> selectAll(ActivityCategory activityCategory);
    
    /**
     * 分页查询
     */
    List<ActivityCategory> selectPage(ActivityCategory activityCategory);
} 