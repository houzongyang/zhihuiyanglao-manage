package com.example.mapper;

import com.example.entity.Nurse;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 护士Mapper
 */
public interface NurseMapper {
    /**
     * 新增
     */
    int insert(Nurse nurse);

    /**
     * 删除
     */
    int deleteById(Integer id);

    /**
     * 修改
     */
    int updateById(Nurse nurse);

    /**
     * 根据ID查询
     */
    Nurse selectById(Integer id);

    /**
     * 查询所有
     */
    List<Nurse> selectAll(Nurse nurse);

    /**
     * 根据用户名查询
     */
    @Select("select * from nurse where username = #{username}")
    Nurse selectByUsername(@Param("username") String username);
}
