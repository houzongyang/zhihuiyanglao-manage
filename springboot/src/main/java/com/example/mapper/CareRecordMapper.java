package com.example.mapper;

import com.example.entity.CareRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 护理记录 Mapper接口
 */
@Mapper
public interface CareRecordMapper {

    /**
     * 新增护理记录
     * 
     * @param careRecord 护理记录信息
     * @return 影响行数
     */
    int insert(CareRecord careRecord);

    /**
     * 根据ID删除护理记录
     * 
     * @param id 护理记录ID
     * @return 影响行数
     */
    int deleteById(Integer id);

    /**
     * 批量删除护理记录
     * 
     * @param ids 护理记录ID列表
     * @return 影响行数
     */
    int deleteBatch(@Param("ids") List<Integer> ids);

    /**
     * 修改护理记录
     * 
     * @param careRecord 护理记录信息
     * @return 影响行数
     */
    int updateById(CareRecord careRecord);

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
     * @return 护理记录列表
     */
    List<CareRecord> selectPage(CareRecord careRecord);
}