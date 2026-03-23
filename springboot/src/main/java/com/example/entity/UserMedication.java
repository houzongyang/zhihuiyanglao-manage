package com.example.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户药物管理
 */
@Data
public class UserMedication implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 用药记录ID */
    private Integer id;
    
    /** 用户ID（老人） */
    private Integer userId;
    
    /** 药物名称 */
    private String medicationName;
    
    /** 药物类型：CARDIOVASCULAR-心血管，DIABETES-糖尿病，ANALGESIC-止痛，ANTIBIOTIC-抗生素等 */
    private String medicationType;
    
    /** 单次剂量（如：1片，5ml） */
    private String dosage;
    
    /** 用药频率（如：每日3次，每12小时1次） */
    private String frequency;
    
    /** 具体用药时间（如：08:00,12:00,18:00） */
    private String administrationTimes;
    
    /** 与餐食关系：BEFORE_MEAL-餐前，WITH_MEAL-餐中，AFTER_MEAL-餐后，EMPTY_STOMACH-空腹 */
    private String mealRelation;
    
    /** 开始用药日期 */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate startDate;
    
    /** 结束用药日期 */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate endDate;
    
    /** 开药医生 */
    private String prescribedBy;
    
    /** 开药日期 */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate prescriptionDate;
    
    /** 用药目的 */
    private String purpose;
    
    /** 特殊用药说明 */
    private String specialInstructions;
    
    /** 副作用记录 */
    private String sideEffects;
    
    /** 状态：ACTIVE-进行中，COMPLETED-已完成，STOPPED-已停药 */
    private String status;
    
    /** 停药原因 */
    private String stopReason;
    
    /** 备注信息 */
    private String remarks;
    
    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
    
    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;
    
    /** 创建人 */
    private String createdBy;

}