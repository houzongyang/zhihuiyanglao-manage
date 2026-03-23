package com.example.common.enums;

public enum ResultCodeEnum {
    SUCCESS("200", "成功"),

    PARAM_ERROR("400", "参数异常"),
    TOKEN_INVALID_ERROR("401", "无效的token"),
    TOKEN_CHECK_ERROR("401", "token验证失败，请重新登录"),
    PARAM_LOST_ERROR("4001", "参数缺失"),

    SYSTEM_ERROR("500", "系统异常"),
    USER_EXIST_ERROR("5001", "用户名已存在"),
    USER_NOT_LOGIN("5002", "用户未登录"),
    USER_ACCOUNT_ERROR("5003", "账号或密码错误"),
    USER_NOT_EXIST_ERROR("5004", "用户不存在"),
    PARAM_PASSWORD_ERROR("5005", "原密码输入错误"),
    HEADER_ALREADY_ERROR("5006", "该用户已经担任其他社团社长了"),
    APPLY_ALREADY_ERROR("5007", "您已经申请过该社团"),
    
    // 活动申请相关错误码
    ACTIVITY_NOT_EXIST("6001", "活动不存在"),
    ACTIVITY_FULL("6002", "活动人数已满，无法申请"),
    ACTIVITY_ALREADY_APPLIED("6003", "您已经申请过该活动，请勿重复申请"),
    ACTIVITY_APPLY_FAIL("6004", "申请失败"),
    ACTIVITY_UPDATE_FAIL("6005", "更新失败"),
    ACTIVITY_DELETE_FAIL("6006", "删除失败"),
    ACTIVITY_APPLY_NOT_EXIST("6007", "申请记录不存在"),
    ACTIVITY_AUDIT_FAIL("6008", "审核失败"),
    
    // 项目相关错误码
    PROJECT_NOT_EXIST("7001", "项目不存在"),
    PROJECT_STATUS_ERROR("7002", "只有草稿状态的项目才能提交"),
    PROJECT_PROGRESS_INVALID("7003", "进度值必须在0-100之间"),
    ;

    public String code;
    public String msg;

    ResultCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
