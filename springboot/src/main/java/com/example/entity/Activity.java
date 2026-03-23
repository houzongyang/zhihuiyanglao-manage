package com.example.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 活动信息
 */
@Data
public class Activity implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 活动ID */
    private Integer id;
    /** 活动分类ID */
    private Integer categoryId;
    /** 活动标题 */
    private String title;
    /** 活动描述 */
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")

    private Date activityDate;

    /** 开始时间 */
    @JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+8")
    private LocalTime startTime;
    /** 结束时间 */
    @JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+8")
    private LocalTime endTime;
    /** 活动地点 */
    private String location;
    /** 最大参与人数，0表示无限制 */
    private Integer maxParticipants;
    /** 当前参与人数 */
    private Integer currentParticipants;
    /** 组织者 */
    private String organizer;
    /** 组织者电话 */
    private String organizerPhone;
    /** 参与要求 */
    private String requirements;
    /** 所需物品 */
    private String materialsNeeded;
    /** 活动费用 */
    private BigDecimal cost;
    /** 活动状态：PLANNED-计划中，ONGOING-进行中，COMPLETED-已完成，CANCELLED-已取消 */
    private String status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")

    /** 报名截止时间 */
    private LocalDateTime registrationDeadline;
    /** 备注信息 */
    private String notes;
    /** 活动图片 */
    private String imageUrl;
    /** 活动封面图片 */
    private String coverImage;
    /** 创建时间 */
    private LocalDateTime createTime;
    /** 更新时间 */
    private LocalDateTime updateTime;
    /** 创建人 */
    private String createdBy;


}