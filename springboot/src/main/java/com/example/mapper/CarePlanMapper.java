package com.example.mapper;

import com.example.entity.CarePlan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 护理计划 Mapper接口
 */
@Mapper
public interface CarePlanMapper {

    /**
     * 新增护理计划
     * 
     * @param carePlan 护理计划信息
     * @return 影响行数
     */
    int insert(CarePlan carePlan);

    /**
     * 根据ID删除护理计划
     * 
     * @param id 护理计划ID
     * @return 影响行数
     */
    int deleteById(Integer id);

    /**
     * 批量删除护理计划
     * 
     * @param ids 护理计划ID列表
     * @return 影响行数
     */
    int deleteBatch(@Param("ids") List<Integer> ids);

    /**
     * 修改护理计划
     * 
     * @param carePlan 护理计划信息
     * @return 影响行数
     */
    int updateById(CarePlan carePlan);

    /**
     * 根据ID查询护理计划
     * 
     * @param id 护理计划ID
     * @return 护理计划信息
     */
    CarePlan selectById(Integer id);

    /**
     * 查询所有护理计划
     * 
     * @param carePlan 查询条件
     * @return 护理计划列表
     */
    List<CarePlan> selectAll(CarePlan carePlan);

    /**
     * 分页查询护理计划
     * 
     * @param carePlan 查询条件
     * @return 护理计划列表
     */
    List<CarePlan> selectPage(CarePlan carePlan);
}