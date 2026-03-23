package com.example.service.impl;

import com.example.entity.ActivityCategory;
import com.example.mapper.ActivityCategoryMapper;
import com.example.service.ActivityCategoryService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 活动分类服务实现类
 */
@Service
public class ActivityCategoryServiceImpl implements ActivityCategoryService {

    @Resource
    private ActivityCategoryMapper activityCategoryMapper;

    /**
     * 新增活动分类
     */
    @Override
    public void add(ActivityCategory activityCategory) {
        // 设置默认值
        if (activityCategory.getSortOrder() == null) {
            activityCategory.setSortOrder(0);
        }
        if (activityCategory.getStatus() == null || activityCategory.getStatus().isEmpty()) {
            activityCategory.setStatus("ACTIVE");
        }

        activityCategoryMapper.insert(activityCategory);
    }

    /**
     * 删除活动分类
     */
    @Override
    public void deleteById(Integer id) {
        activityCategoryMapper.deleteById(id);
    }

    /**
     * 批量删除活动分类
     */
    @Override
    public void deleteBatch(List<Integer> ids) {
        activityCategoryMapper.deleteBatch(ids);
    }

    /**
     * 修改活动分类
     */
    @Override
    public void updateById(ActivityCategory activityCategory) {
        activityCategoryMapper.updateById(activityCategory);
    }

    /**
     * 根据ID查询
     */
    @Override
    public ActivityCategory selectById(Integer id) {
        return activityCategoryMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    @Override
    public List<ActivityCategory> selectAll(ActivityCategory activityCategory) {
        return activityCategoryMapper.selectAll(activityCategory);
    }

    /**
     * 分页查询
     */
    @Override
    public PageInfo<ActivityCategory> selectPage(ActivityCategory activityCategory, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<ActivityCategory> list = activityCategoryMapper.selectPage(activityCategory);
        return new PageInfo<>(list);
    }
}