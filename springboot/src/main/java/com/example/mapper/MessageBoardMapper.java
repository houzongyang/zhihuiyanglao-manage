package com.example.mapper;

import com.example.entity.MessageBoard;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;

import java.util.List;

/**
 * 操作message_board相关数据接口
 */
public interface MessageBoardMapper {

    /**
     * 新增
     */
    int insert(MessageBoard messageBoard);

    /**
     * 删除
     */
    int deleteById(Integer id);

    /**
     * 修改
     */
    int updateById(MessageBoard messageBoard);

    /**
     * 根据ID查询
     */
    MessageBoard selectById(Integer id);

    /**
     * 查询所有
     */
    List<MessageBoard> selectAll(MessageBoard messageBoard);

    /**
     * 根据父留言ID删除所有回复
     */
    @Delete("delete from message_board where parent_id = #{parentId}")
    int deleteByParentId(Integer parentId);

    /**
     * 增加回复数量
     */
    @Update("update message_board set reply_count = reply_count + 1, update_time = now() where id = #{id}")
    int incrementReplyCount(Integer id);

    /**
     * 减少回复数量
     */
    @Update("update message_board set reply_count = reply_count - 1, update_time = now() where id = #{id} and reply_count > 0")
    int decrementReplyCount(Integer id);

    /**
     * 增加查看次数
     */
    @Update("update message_board set view_count = view_count + 1 where id = #{id}")
    int incrementViewCount(Integer id);

    /**
     * 根据状态查询留言列表
     */
    List<MessageBoard> selectByStatus(@Param("status") String status);

    /**
     * 根据用户类型查询留言列表
     */
    List<MessageBoard> selectByUserType(@Param("userType") String userType);

    /**
     * 根据父留言ID查询回复列表
     */
    List<MessageBoard> selectByParentId(@Param("parentId") Integer parentId);

    /**
     * 查询置顶留言列表
     */
    List<MessageBoard> selectTopMessages();

    /**
     * 查询主留言列表（parent_id = 0）
     */
    List<MessageBoard> selectMainMessages();
} 