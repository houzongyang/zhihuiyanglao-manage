package com.example.entity.vo;

/**
 * 老师确认项目进度的VO类
 */
public class TeacherConfirmVO {
    /** 老师ID */
    private Integer teacherId;
    
    /** 老师姓名 */
    private String teacherName;
    
    /** 反馈意见 */
    private String feedback;

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
} 