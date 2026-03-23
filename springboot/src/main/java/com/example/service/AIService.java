package com.example.service;

/**
 * AI服务接口
 */
public interface AIService {
    /**
     * 发送消息给AI并获取回复
     * @param message 用户消息
     * @return AI回复
     */
    String chat(String message);
} 