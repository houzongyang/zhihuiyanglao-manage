package com.example.mapper;

import com.example.entity.CheckinRecord;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 入住信息记录 Mapper 接口
 */
public interface CheckinRecordMapper {
    /**
     * 新增入住记录
     */
    void insert(CheckinRecord checkinRecord);

    /**
     * 根据ID删除入住记录
     */
    void deleteById(Integer id);

    /**
     * 批量删除入住记录
     */
    void deleteBatch(@Param("ids") List<Integer> ids);

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
    List<CheckinRecord> selectPage(CheckinRecord checkinRecord);
} 