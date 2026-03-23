package com.example.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 活动参与记录
 */
@Data
public class ActivityParticipant implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 参与记录ID */
    private Integer id;
    /** 活动ID */
    private Integer activityId;
    /** 用户ID */
    private Integer userId;
    /** 报名时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime registrationTime;
    /** 出席状态：REGISTERED-已报名，ATTENDED-已参加，ABSENT-缺席，CANCELLED-已取消 */
    private String attendanceStatus;
    /** 签到时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime checkinTime;
    /** 签退时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime checkoutTime;
    /** 活动评分(1-5分) */
    private Integer rating;
    /** 活动反馈 */
    private String feedback;
    /** 特殊需求 */
    private String specialNeeds;
    /** 紧急联系人 */
    private String emergencyContact;
    /** 紧急联系电话 */
    private String emergencyPhone;
    /** 备注信息 */
    private String notes;
    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    // 关联查询字段
    /** 活动标题 */
    private String activityTitle;
    /** 用户姓名 */
    private String userName;
    /** 活动日期 */
    private String activityDate;
    /** 活动地点 */
    private String activityLocation;
    /** 活动分类ID */
    private Integer categoryId;
    /** 活动分类名称 */
    private String categoryName;
    /** 活动分类颜色 */
    private String categoryColor;
    /** 活动状态 */
    private String activityStatus;
    /** 活动开始时间 */
    private String startTime;
    /** 活动结束时间 */
    private String endTime;
    /** 活动费用 */
    private java.math.BigDecimal cost;
}