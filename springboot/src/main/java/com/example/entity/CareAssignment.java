package com.example.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;

/**
 * 护理安排实体类
 */
public class CareAssignment implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 护理安排ID
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
     * 护理计划ID
     */
    private Integer carePlanId;
    
    /**
     * 安排日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date shiftDate;
    
    /**
     * 开始时间
     */
    @JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+8")
    private Date startTime;
    
    /**
     * 结束时间
     */
    @JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+8")
    private Date endTime;
    
    /**
     * 班次类型：MORNING-早班，AFTERNOON-中班，NIGHT-夜班
     */
    private String shiftType;
    
    /**
     * 工作内容
     */
    private String workContent;
    
    /**
     * 状态：SCHEDULED-已安排，COMPLETED-已完成，CANCELLED-已取消
     */
    private String status;
    
    /**
     * 完成说明
     */
    private String completionNotes;
    
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
    
    /**
     * 护理计划标题
     */
    private String carePlanTitle;

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

    public Integer getCarePlanId() {
        return carePlanId;
    }

    public void setCarePlanId(Integer carePlanId) {
        this.carePlanId = carePlanId;
    }

    public Date getShiftDate() {
        return shiftDate;
    }

    public void setShiftDate(Date shiftDate) {
        this.shiftDate = shiftDate;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getShiftType() {
        return shiftType;
    }

    public void setShiftType(String shiftType) {
        this.shiftType = shiftType;
    }

    public String getWorkContent() {
        return workContent;
    }

    public void setWorkContent(String workContent) {
        this.workContent = workContent;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCompletionNotes() {
        return completionNotes;
    }

    public void setCompletionNotes(String completionNotes) {
        this.completionNotes = completionNotes;
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

    public String getCarePlanTitle() {
        return carePlanTitle;
    }

    public void setCarePlanTitle(String carePlanTitle) {
        this.carePlanTitle = carePlanTitle;
    }
}