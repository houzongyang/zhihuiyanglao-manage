package com.example.common.enums;

/**
 * 项目进度状态枚举
 */
public enum ProjectProgressStatusEnum {
    // 未开始
    NOT_STARTED("未开始", 0),
    // 已启动
    STARTED("已启动", 1),
    // 进行中
    IN_PROGRESS("进行中", 2),
    // 接近完成
    NEARLY_COMPLETED("接近完成", 3),
    // 已完成
    COMPLETED("已完成", 4),
    // 已暂停
    SUSPENDED("已暂停", 5),
    ;

    public String description;
    public int code;

    ProjectProgressStatusEnum(String description, int code) {
        this.description = description;
        this.code = code;
    }
    
    /**
     * 根据进度百分比获取对应的状态
     * @param progressPercent 进度百分比(0-100)
     * @return 进度状态枚举
     */
    public static ProjectProgressStatusEnum getStatusByProgress(Integer progressPercent) {
        if (progressPercent == null) {
            return NOT_STARTED;
        }
        
        if (progressPercent == 0) {
            return NOT_STARTED;
        } else if (progressPercent < 30) {
            return STARTED;
        } else if (progressPercent < 70) {
            return IN_PROGRESS;
        } else if (progressPercent < 100) {
            return NEARLY_COMPLETED;
        } else {
            return COMPLETED;
        }
    }
    
    /**
     * 根据编码获取枚举
     * @param code 状态编码
     * @return 对应的枚举，如不存在返回null
     */
    public static ProjectProgressStatusEnum getByCode(int code) {
        for (ProjectProgressStatusEnum status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        return null;
    }
} 