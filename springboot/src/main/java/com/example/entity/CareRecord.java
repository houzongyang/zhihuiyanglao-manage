package com.example.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;

/**
 * 护理记录实体类
 */
public class CareRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 护理记录ID
     */
    private Integer id;
    
    /**
     * 老人ID
     */
    private Integer userId;
    
    /**
     * 护理人员ID
     */
    private Integer nurseId;
    
    /**
     * 护理安排ID
     */
    private Integer careAssignmentId;
    
    /**
     * 记录日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date recordDate;
    
    /**
     * 护理类型：DAILY_CARE-日常照顾，DIET-饮食护理，EXERCISE-运动护理，MEDICAL-医疗护理
     */
    private String careType;
    
    /**
     * 护理内容
     */
    private String careContent;
    
    /**
     * 血压
     */
    private String bloodPressure;
    
    /**
     * 心率
     */
    private String heartRate;
    
    /**
     * 体温
     */
    private String temperature;
    
    /**
     * 饮食记录
     */
    private String dietRecord;
    
    /**
     * 运动记录
     */
    private String exerciseRecord;
    
    /**
     * 情绪状态：GOOD-良好，NORMAL-一般，POOR-较差
     */
    private String moodStatus;
    
    /**
     * 异常情况
     */
    private String abnormalSituation;
    
    /**
     * 备注
     */
    private String remarks;
    
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    // 关联查询字段
    /**
     * 用户名称
     */
    private String userName;
    
    /**
     * 护理人员名称
     */
    private String nurseName;
    
    // 查询条件字段
    /**
     * 开始日期（查询条件）
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startDate;
    
    /**
     * 结束日期（查询条件）
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date endDate;

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getNurseId() {
        return nurseId;
    }

    public void setNurseId(Integer nurseId) {
        this.nurseId = nurseId;
    }

    public Integer getCareAssignmentId() {
        return careAssignmentId;
    }

    public void setCareAssignmentId(Integer careAssignmentId) {
        this.careAssignmentId = careAssignmentId;
    }

    public Date getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
    }

    public String getCareType() {
        return careType;
    }

    public void setCareType(String careType) {
        this.careType = careType;
    }

    public String getCareContent() {
        return careContent;
    }

    public void setCareContent(String careContent) {
        this.careContent = careContent;
    }

    public String getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(String bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public String getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(String heartRate) {
        this.heartRate = heartRate;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getDietRecord() {
        return dietRecord;
    }

    public void setDietRecord(String dietRecord) {
        this.dietRecord = dietRecord;
    }

    public String getExerciseRecord() {
        return exerciseRecord;
    }

    public void setExerciseRecord(String exerciseRecord) {
        this.exerciseRecord = exerciseRecord;
    }

    public String getMoodStatus() {
        return moodStatus;
    }

    public void setMoodStatus(String moodStatus) {
        this.moodStatus = moodStatus;
    }

    public String getAbnormalSituation() {
        return abnormalSituation;
    }

    public void setAbnormalSituation(String abnormalSituation) {
        this.abnormalSituation = abnormalSituation;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNurseName() {
        return nurseName;
    }

    public void setNurseName(String nurseName) {
        this.nurseName = nurseName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}