package com.example.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 费用管理
 */
@Data
public class BillManagement implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Integer id;
    /** 用户ID */
    private Integer userId;
    /** 用户姓名 */
    private String userName;
    /** 账单类型：ACCOMMODATION入住费用,ACTIVITY活动费用 */
    private String billType;
    /** 关联ID：入住记录ID或活动参与ID */
    private Integer relatedId;
    /** 关联名称：房间号或活动名称 */
    private String relatedName;
    /** 费用金额 */
    private BigDecimal amount;
    /** 费用描述 */
    private String description;
    /** 支付状态：UNPAID未支付,PAID已支付 */
    private String paymentStatus;
    /** 生成日期 */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate generatedDate;
    /** 支付日期 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime paymentDate;
    /** 生成人（管理员） */
    private String generatedBy;
    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createdAt;
    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updatedAt;

    // 查询条件字段（不存储到数据库）
    /** 开始日期 - 用于查询条件 */
    private String startDate;
    /** 结束日期 - 用于查询条件 */
    private String endDate;
}
