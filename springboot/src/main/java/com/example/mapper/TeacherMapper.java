package com.example.mapper;

import com.example.entity.Teacher;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 老师Mapper
 */
public interface TeacherMapper {
    /**
     * 新增
     */
    int insert(Teacher teacher);

    /**
     * 删除
     */
    int deleteById(Integer id);

    /**
     * 修改
     */
    int updateById(Teacher teacher);

    /**
     * 根据ID查询
     */
    Teacher selectById(Integer id);

    /**
     * 查询所有
     */
    List<Teacher> selectAll(Teacher teacher);

    /**
     * 根据用户名查询
     */
    @Select("select * from teacher where username = #{username}")
    Teacher selectByUsername(@Param("username") String username);
} 