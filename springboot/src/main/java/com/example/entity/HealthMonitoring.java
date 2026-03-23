package com.example.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

/**
 * 健康监控记录
 */
public class HealthMonitoring implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 监控记录ID */
    private Integer id;
    /** 用户ID（老人） */
    private Integer userId;
    /** 记录日期 */
    private LocalDate recordDate;
    /** 记录时间 */
    private LocalTime recordTime;
    /** 体温（℃） */
    private BigDecimal bodyTemperature;
    /** 收缩压（mmHg） */
    private Integer systolicPressure;
    /** 舒张压（mmHg） */
    private Integer diastolicPressure;
    /** 心率（次/分） */
    private Integer heartRate;
    /** 血糖（mmol/L） */
    private BigDecimal bloodSugar;
    /** 血糖类型：FASTING-空腹，POSTPRANDIAL-餐后，RANDOM-随机 */
    private String bloodSugarType;
    /** 血氧饱和度（%） */
    private Integer bloodOxygen;
    /** 体重（kg） */
    private BigDecimal weight;
    /** 睡眠时长（小时） */
    private BigDecimal sleepHours;
    /** 睡眠质量：EXCELLENT-优秀，GOOD-良好，FAIR-一般，POOR-较差 */
    private String sleepQuality;
    /** 情绪状态：HAPPY-愉快，NORMAL-正常，ANXIOUS-焦虑，DEPRESSED-抑郁 */
    private String moodStatus;
    /** 体力活动记录 */
    private String physicalActivity;
    /** 异常症状描述 */
    private String abnormalSymptoms;
    /** 测量人员 */
    private String measuredBy;
    /** 备注信息 */
    private String remarks;
    /** 创建时间 */
    private LocalDateTime createTime;
    /** 更新时间 */
    private LocalDateTime updateTime;

    // 扩展字段，用于查询显示
    /** 用户姓名 */
    private String userName;

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

    public LocalDate getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(LocalDate recordDate) {
        this.recordDate = recordDate;
    }

    public LocalTime getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(LocalTime recordTime) {
        this.recordTime = recordTime;
    }

    public BigDecimal getBodyTemperature() {
        return bodyTemperature;
    }

    public void setBodyTemperature(BigDecimal bodyTemperature) {
        this.bodyTemperature = bodyTemperature;
    }

    public Integer getSystolicPressure() {
        return systolicPressure;
    }

    public void setSystolicPressure(Integer systolicPressure) {
        this.systolicPressure = systolicPressure;
    }

    public Integer getDiastolicPressure() {
        return diastolicPressure;
    }

    public void setDiastolicPressure(Integer diastolicPressure) {
        this.diastolicPressure = diastolicPressure;
    }

    public Integer getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(Integer heartRate) {
        this.heartRate = heartRate;
    }

    public BigDecimal getBloodSugar() {
        return bloodSugar;
    }

    public void setBloodSugar(BigDecimal bloodSugar) {
        this.bloodSugar = bloodSugar;
    }

    public String getBloodSugarType() {
        return bloodSugarType;
    }

    public void setBloodSugarType(String bloodSugarType) {
        this.bloodSugarType = bloodSugarType;
    }

    public Integer getBloodOxygen() {
        return bloodOxygen;
    }

    public void setBloodOxygen(Integer bloodOxygen) {
        this.bloodOxygen = bloodOxygen;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public BigDecimal getSleepHours() {
        return sleepHours;
    }

    public void setSleepHours(BigDecimal sleepHours) {
        this.sleepHours = sleepHours;
    }

    public String getSleepQuality() {
        return sleepQuality;
    }

    public void setSleepQuality(String sleepQuality) {
        this.sleepQuality = sleepQuality;
    }

    public String getMoodStatus() {
        return moodStatus;
    }

    public void setMoodStatus(String moodStatus) {
        this.moodStatus = moodStatus;
    }

    public String getPhysicalActivity() {
        return physicalActivity;
    }

    public void setPhysicalActivity(String physicalActivity) {
        this.physicalActivity = physicalActivity;
    }

    public String getAbnormalSymptoms() {
        return abnormalSymptoms;
    }

    public void setAbnormalSymptoms(String abnormalSymptoms) {
        this.abnormalSymptoms = abnormalSymptoms;
    }

    public String getMeasuredBy() {
        return measuredBy;
    }

    public void setMeasuredBy(String measuredBy) {
        this.measuredBy = measuredBy;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
} 