package com.example.service;

import com.example.entity.ActivityCategory;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 活动分类服务接口
 */
public interface ActivityCategoryService {

    /**
     * 新增活动分类
     */
    void add(ActivityCategory activityCategory);

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
    PageInfo<ActivityCategory> selectPage(ActivityCategory activityCategory, Integer pageNum, Integer pageSize);
} 