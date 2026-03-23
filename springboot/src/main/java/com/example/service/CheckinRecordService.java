package com.example.service;

import com.example.entity.CheckinRecord;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 入住信息记录服务接口
 */
public interface CheckinRecordService {
    /**
     * 新增入住记录
     */
    void add(CheckinRecord checkinRecord);

    /**
     * 根据ID删除入住记录
     */
    void deleteById(Integer id);

    /**
     * 批量删除入住记录
     */
    void deleteBatch(List<Integer> ids);

    /**
     * 更新入住记录
     */
    void updateById(CheckinRecord checkinRecord);

    /**
     * 根据ID查询入住记录
     */
    CheckinRecord selectById(Integer id);

    /**
     * 查询所有入住记录
     */
    List<CheckinRecord> selectAll(CheckinRecord checkinRecord);

    /**
     * 分页查询入住记录
     */
    PageInfo<CheckinRecord> selectPage(CheckinRecord checkinRecord, Integer pageNum, Integer pageSize);
} 