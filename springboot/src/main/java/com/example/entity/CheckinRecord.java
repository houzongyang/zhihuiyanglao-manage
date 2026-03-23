package com.example.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 入住信息记录实体类
 */
@Data
public class CheckinRecord {
    /**
     * 入住记录ID
     */
    private Integer id;
    
    /**
     * 关联用户ID
     */
    private Integer userId;
    
    /**
     * 用户姓名（非数据库字段，用于展示）
     */
    private String userName;
    
    /**
     * 房间号
     */
    private String roomNumber;
    
    /**
     * 入住时间
     */
    private String checkinDate;
    
    /**
     * 退房时间
     */
    private String checkoutDate;
    
    /**
     * 入住状态：CHECKED_IN-已入住，CHECKED_OUT-已退房，RESERVED-已预订
     */
    private String checkinStatus;
    
    /**
     * 月费用
     */
    private BigDecimal monthlyFee;
    
    /**
     * 押金
     */
    private BigDecimal deposit;
    
    /**
     * 已缴费用
     */
    private BigDecimal totalPaid;
    
    /**
     * 缴费状态：PAID-已缴费，UNPAID-未缴费，PARTIAL-部分缴费
     */
    private String paymentStatus;
    
    /**
     * 房间类型
     */
    private String roomType;
    
    /**
     * 床位号
     */
    private String bedNumber;
    
    /**
     * 特殊需求
     */
    private String specialNeeds;
    
    /**
     * 紧急联系人
     */
    private String emergencyContact;
    
    /**
     * 紧急联系电话
     */
    private String emergencyPhone;
    
    /**
     * 备注信息
     */
    private String remarks;
    
    /**
     * 创建时间
     */
    private Date createTime;
    
    /**
     * 更新时间
     */
    private Date updateTime;
    
    /**
     * 创建人
     */
    private String createdBy;
} 