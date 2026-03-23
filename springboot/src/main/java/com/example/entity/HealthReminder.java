package com.example.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 健康提醒信息
 */
@Data
public class HealthReminder implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 提醒记录ID */
    private Integer id;
    /** 提醒内容 */
    private String content;
    /** 发送人ID */
    private Integer senderId;
    /** 发送人昵称 */
    private String senderName;
    /** 发送人类型：ADMIN-管理员，TEACHER-护理人员，USER-用户 */
    private String senderType;
    /** 接收人ID（可为空，表示所有人） */
    private Integer receiverId;
    /** 接收人类型：ALL-所有人，MEDICAL_STAFF-医护人员，FAMILY-家属，USER-用户 */
    private String receiverType;
    /** 关联的健康档案ID */
    private Integer healthRecordId;
    /** 相关用户ID（老人） */
    private Integer relatedUserId;
    /** 提醒类型：HEALTH_ALERT-健康警报，MEDICATION-用药提醒，CHECK_UP-体检提醒，EMERGENCY-紧急情况 */
    private String reminderType;
    /** 优先级：HIGH-高，NORMAL-普通，LOW-低 */
    private String priority;
    /** 是否已读：0-未读，1-已读 */
    private Integer isRead;
    /** 阅读时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime readTime;
    /** 状态：ACTIVE-有效，RESOLVED-已处理，EXPIRED-已过期 */
    private String status;
    /** 过期时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime expireTime;
    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;
}
