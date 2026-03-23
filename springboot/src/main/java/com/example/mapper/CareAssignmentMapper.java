package com.example.mapper;

import com.example.entity.CareAssignment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 护理安排 Mapper接口
 */
@Mapper
public interface CareAssignmentMapper {

    /**
     * 新增护理安排
     * 
     * @param careAssignment 护理安排信息
     * @return 影响行数
     */
    int insert(CareAssignment careAssignment);

    /**
     * 根据ID删除护理安排
     * 
     * @param id 护理安排ID
     * @return 影响行数
     */
    int deleteById(Integer id);

    /**
     * 批量删除护理安排
     * 
     * @param ids 护理安排ID列表
     * @return 影响行数
     */
    int deleteBatch(@Param("ids") List<Integer> ids);

    /**
     * 修改护理安排
     * 
     * @param careAssignment 护理安排信息
     * @return 影响行数
     */
    int updateById(CareAssignment careAssignment);

    /**
     * 根据ID查询护理安排
     * 
     * @param id 护理安排ID
     * @return 护理安排信息
     */
    CareAssignment selectById(Integer id);

    /**
     * 查询所有护理安排
     * 
     * @param careAssignment 查询条件
     * @return 护理安排列表
     */
    List<CareAssignment> selectAll(CareAssignment careAssignment);

    /**
     * 分页查询护理安排
     * 
     * @param careAssignment 查询条件
     * @return 护理安排列表
     */
    List<CareAssignment> selectPage(CareAssignment careAssignment);
}