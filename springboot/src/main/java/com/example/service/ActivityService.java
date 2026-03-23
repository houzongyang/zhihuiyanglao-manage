package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.entity.Activity;
import com.example.mapper.ActivityMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 活动信息业务处理
 **/
@Service
public class ActivityService {

    @Resource
    private ActivityMapper activityMapper;

    /**
     * 新增
     */
    public void add(Activity activity) {
        if (ObjectUtil.isEmpty(activity.getStatus())) {
            activity.setStatus("PLANNED");
        }
        if (ObjectUtil.isEmpty(activity.getCurrentParticipants())) {
            activity.setCurrentParticipants(0);
        }
        if (ObjectUtil.isEmpty(activity.getMaxParticipants())) {
            activity.setMaxParticipants(0);
        }
        activity.setCreateTime(LocalDateTime.now());
        activity.setUpdateTime(LocalDateTime.now());
        activityMapper.insert(activity);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        activityMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            activityMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Activity activity) {
        activity.setUpdateTime(LocalDateTime.now());
        activityMapper.updateById(activity);
    }

    /**
     * 根据ID查询
     */
    public Activity selectById(Integer id) {
        return activityMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Activity> selectAll(Activity activity) {
        return activityMapper.selectAll(activity);
    }

    /**
     * 分页查询
     */
    public PageInfo<Activity> selectPage(Activity activity, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Activity> list = activityMapper.selectAll(activity);
        return PageInfo.of(list);
    }

    /**
     * 根据分类ID查询活动列表
     */
    public List<Activity> selectByCategoryId(Integer categoryId) {
        return activityMapper.selectByCategoryId(categoryId);
    }

    /**
     * 根据状态查询活动列表
     */
    public List<Activity> selectByStatus(String status) {
        return activityMapper.selectByStatus(status);
    }

    /**
     * 更新活动参与人数
     */
    public void updateParticipantCount(Integer activityId, Integer count) {
        activityMapper.updateParticipantCount(activityId, count);
    }

}