package com.example.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 留言板信息
 */
@Data
public class MessageBoard implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 留言ID */
    private Integer id;
    /** 留言标题 */
    private String title;
    /** 留言内容 */
    private String content;
    /** 用户ID */
    private Integer userId;
    /** 用户类型：ADMIN-管理员，TEACHER-教师，USER-普通用户 */
    private String userType;
    /** 父留言ID，0表示主留言，其他表示回复 */
    private Integer parentId;
    /** 留言状态：PENDING-待审核，APPROVED-已通过，REJECTED-已拒绝 */
    private String status;
    /** 是否公开显示：1-公开，0-不公开 */
    private Integer isPublic;
    /** 是否置顶：1-置顶，0-不置顶 */
    private Integer isTop;
    /** 回复数量 */
    private Integer replyCount;
    /** 查看次数 */
    private Integer viewCount;
    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;
    /** 创建人姓名 */
    private String createdBy;
    /** 审核人 */
    private String reviewedBy;
    /** 审核时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime reviewedTime;
} 