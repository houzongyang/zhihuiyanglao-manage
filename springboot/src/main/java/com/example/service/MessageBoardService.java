package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.entity.MessageBoard;
import com.example.mapper.MessageBoardMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 留言板业务处理
 **/
@Service
public class MessageBoardService {

    @Resource
    private MessageBoardMapper messageBoardMapper;

    /**
     * 新增
     */
    public void add(MessageBoard messageBoard) {
        // 设置默认值
        if (ObjectUtil.isEmpty(messageBoard.getStatus())) {
            messageBoard.setStatus("PENDING");
        }
        if (ObjectUtil.isEmpty(messageBoard.getIsPublic())) {
            messageBoard.setIsPublic(1);
        }
        if (ObjectUtil.isEmpty(messageBoard.getIsTop())) {
            messageBoard.setIsTop(0);
        }
        if (ObjectUtil.isEmpty(messageBoard.getReplyCount())) {
            messageBoard.setReplyCount(0);
        }
        if (ObjectUtil.isEmpty(messageBoard.getViewCount())) {
            messageBoard.setViewCount(0);
        }
        if (ObjectUtil.isEmpty(messageBoard.getParentId())) {
            messageBoard.setParentId(0);
        }
        
        messageBoard.setCreateTime(LocalDateTime.now());
        messageBoard.setUpdateTime(LocalDateTime.now());
        
        messageBoardMapper.insert(messageBoard);
        
        // 如果是回复，更新父留言的回复数量
        if (messageBoard.getParentId() != null && messageBoard.getParentId() > 0) {
            messageBoardMapper.incrementReplyCount(messageBoard.getParentId());
        }
    }

    /**
     * 回复留言
     */
    public void reply(MessageBoard messageBoard) {
        // 设置回复的默认值
        messageBoard.setStatus("APPROVED"); // 管理员回复直接通过
        messageBoard.setIsPublic(1);
        messageBoard.setIsTop(0);
        messageBoard.setReplyCount(0);
        messageBoard.setViewCount(0);
        messageBoard.setCreateTime(LocalDateTime.now());
        messageBoard.setUpdateTime(LocalDateTime.now());
        
        // 插入回复记录
        messageBoardMapper.insert(messageBoard);
        
        // 更新父留言的回复数量
        if (messageBoard.getParentId() != null && messageBoard.getParentId() > 0) {
            messageBoardMapper.incrementReplyCount(messageBoard.getParentId());
        }
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        MessageBoard messageBoard = messageBoardMapper.selectById(id);
        if (messageBoard != null) {
            // 如果是回复，需要减少父留言的回复数量
            if (messageBoard.getParentId() != null && messageBoard.getParentId() > 0) {
                messageBoardMapper.decrementReplyCount(messageBoard.getParentId());
            }
            
            // 删除该留言的所有回复
            messageBoardMapper.deleteByParentId(id);
            
            // 删除留言本身
            messageBoardMapper.deleteById(id);
        }
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(MessageBoard messageBoard) {
        messageBoard.setUpdateTime(LocalDateTime.now());
        messageBoardMapper.updateById(messageBoard);
    }

    /**
     * 根据ID查询
     */
    public MessageBoard selectById(Integer id) {
        return messageBoardMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<MessageBoard> selectAll(MessageBoard messageBoard) {
        return messageBoardMapper.selectAll(messageBoard);
    }

    /**
     * 分页查询
     */
    public PageInfo<MessageBoard> selectPage(MessageBoard messageBoard, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<MessageBoard> list = messageBoardMapper.selectAll(messageBoard);
        return PageInfo.of(list);
    }

    /**
     * 审核通过
     */
    public void approve(Integer id) {
        MessageBoard messageBoard = new MessageBoard();
        messageBoard.setId(id);
        messageBoard.setStatus("APPROVED");
        messageBoard.setReviewedTime(LocalDateTime.now());
        messageBoard.setUpdateTime(LocalDateTime.now());
        // TODO: 获取当前登录用户信息设置审核人
        // messageBoard.setReviewedBy(getCurrentUser());
        messageBoardMapper.updateById(messageBoard);
    }

    /**
     * 审核拒绝
     */
    public void reject(Integer id) {
        MessageBoard messageBoard = new MessageBoard();
        messageBoard.setId(id);
        messageBoard.setStatus("REJECTED");
        messageBoard.setReviewedTime(LocalDateTime.now());
        messageBoard.setUpdateTime(LocalDateTime.now());
        // TODO: 获取当前登录用户信息设置审核人
        // messageBoard.setReviewedBy(getCurrentUser());
        messageBoardMapper.updateById(messageBoard);
    }

    /**
     * 批量审核通过
     */
    public void batchApprove(List<Integer> ids) {
        for (Integer id : ids) {
            approve(id);
        }
    }

    /**
     * 批量审核拒绝
     */
    public void batchReject(List<Integer> ids) {
        for (Integer id : ids) {
            reject(id);
        }
    }

    /**
     * 置顶/取消置顶
     */
    public void toggleTop(Integer id) {
        MessageBoard current = messageBoardMapper.selectById(id);
        if (current != null) {
            MessageBoard messageBoard = new MessageBoard();
            messageBoard.setId(id);
            messageBoard.setIsTop(current.getIsTop() == 1 ? 0 : 1);
            messageBoard.setUpdateTime(LocalDateTime.now());
            messageBoardMapper.updateById(messageBoard);
        }
    }

    /**
     * 增加查看次数
     */
    public void incrementViewCount(Integer id) {
        messageBoardMapper.incrementViewCount(id);
    }
} 