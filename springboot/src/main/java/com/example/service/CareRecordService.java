package com.example.service;

import com.example.entity.CareRecord;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 护理记录服务接口
 */
public interface CareRecordService {

    /**
     * 新增护理记录
     * 
     * @param careRecord 护理记录信息
     */
    void add(CareRecord careRecord);

    /**
     * 根据ID删除护理记录
     * 
     * @param id 护理记录ID
     */
    void deleteById(Integer id);

    /**
     * 批量删除护理记录
     * 
     * @param ids 护理记录ID列表
     */
    void deleteBatch(List<Integer> ids);

    /**
     * 修改护理记录
     * 
     * @param careRecord 护理记录信息
     */
    void updateById(CareRecord careRecord);

    /**
     * 根据ID查询护理记录
     * 
     * @param id 护理记录ID
     * @return 护理记录信息
     */
    CareRecord selectById(Integer id);

    /**
     * 查询所有护理记录
     * 
     * @param careRecord 查询条件
     * @return 护理记录列表
     */
    List<CareRecord> selectAll(CareRecord careRecord);

    /**
     * 分页查询护理记录
     * 
     * @param careRecord 查询条件
     * @param pageNum 页码
     * @param pageSize 页大小
     * @return 分页结果
     */
    PageInfo<CareRecord> selectPage(CareRecord careRecord, Integer pageNum, Integer pageSize);
}