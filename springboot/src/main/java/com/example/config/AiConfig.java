//package com.example.config;
//
//import org.springframework.ai.model.chat.ChatClient;
//import org.springframework.ai.model.zhipuai.ZhiPuAiChatClient;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.ai.model.zhipuai.ZhiPuAiChatOptions;
//
//@Configuration
//public class AiConfig {
//
//    /**
//     * 明确指定优先使用智普AI聊天模型
//     */
//    @Bean
//    @Primary
//    public ChatClient zhiPuAiChatClient(ZhiPuAiChatClient zhiPuAiChatClient) {
//        return zhiPuAiChatClient;
//    }
//}