package com.example.controller;

import com.example.common.Result;
import com.example.entity.MessageBoard;
import com.example.service.MessageBoardService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 留言板前端操作接口
 **/
@RestController
@RequestMapping("/messageBoard")
public class MessageBoardController {

    @Resource
    private MessageBoardService messageBoardService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody MessageBoard messageBoard) {
        messageBoardService.add(messageBoard);
        return Result.success();
    }

    /**
     * 回复留言
     */
    @PostMapping("/reply")
    public Result reply(@RequestBody MessageBoard messageBoard) {
        messageBoardService.reply(messageBoard);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        messageBoardService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        messageBoardService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody MessageBoard messageBoard) {
        messageBoardService.updateById(messageBoard);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        MessageBoard messageBoard = messageBoardService.selectById(id);
        return Result.success(messageBoard);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(MessageBoard messageBoard) {
        List<MessageBoard> list = messageBoardService.selectAll(messageBoard);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(MessageBoard messageBoard,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<MessageBoard> page = messageBoardService.selectPage(messageBoard, pageNum, pageSize);
        return Result.success(page);
    }

    /**
     * 审核通过
     */
    @PutMapping("/approve/{id}")
    public Result approve(@PathVariable Integer id) {
        messageBoardService.approve(id);
        return Result.success();
    }

    /**
     * 审核拒绝
     */
    @PutMapping("/reject/{id}")
    public Result reject(@PathVariable Integer id) {
        messageBoardService.reject(id);
        return Result.success();
    }

    /**
     * 批量审核通过
     */
    @PutMapping("/approve/batch")
    public Result batchApprove(@RequestBody List<Integer> ids) {
        messageBoardService.batchApprove(ids);
        return Result.success();
    }

    /**
     * 批量审核拒绝
     */
    @PutMapping("/reject/batch")
    public Result batchReject(@RequestBody List<Integer> ids) {
        messageBoardService.batchReject(ids);
        return Result.success();
    }

    /**
     * 置顶/取消置顶
     */
    @PutMapping("/toggleTop/{id}")
    public Result toggleTop(@PathVariable Integer id) {
        messageBoardService.toggleTop(id);
        return Result.success();
    }

    /**
     * 增加查看次数
     */
    @PutMapping("/view/{id}")
    public Result incrementViewCount(@PathVariable Integer id) {
        messageBoardService.incrementViewCount(id);
        return Result.success();
    }
} 