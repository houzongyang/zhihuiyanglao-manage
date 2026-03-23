package com.example.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

@Configuration
public class JacksonConfig {

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        
        // 创建支持多种格式的 LocalDateTime 反序列化器
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("yyyy-MM-dd HH:mm:ss")
                .optionalStart()
                .appendPattern(".SSS")
                .optionalEnd()
                .optionalStart()
                .appendPattern("X")
                .optionalEnd()
                .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                .toFormatter();
        
        // 自定义 LocalDateTime 反序列化器
        LocalDateTimeDeserializer localDateTimeDeserializer = new LocalDateTimeDeserializer(formatter) {
            @Override
            public LocalDateTime deserialize(com.fasterxml.jackson.core.JsonParser parser, 
                    com.fasterxml.jackson.databind.DeserializationContext context) throws java.io.IOException {
                String dateString = parser.getText();
                
                // 处理 ISO 8601 格式（包含 Z 或时区偏移）
                if (dateString.contains("T") && (dateString.endsWith("Z") || dateString.contains("+") || dateString.matches(".*[+-]\\d{2}:\\d{2}$"))) {
                    // 移除时区信息，只保留本地时间部分
                    if (dateString.endsWith("Z")) {
                        dateString = dateString.substring(0, dateString.length() - 1);
                    } else if (dateString.contains("+")) {
                        dateString = dateString.substring(0, dateString.indexOf("+"));
                    } else if (dateString.lastIndexOf("-") > 10) { // 确保不是日期中的 -
                        dateString = dateString.substring(0, dateString.lastIndexOf("-"));
                    }
                    
                    // 解析为 LocalDateTime
                    try {
                        return LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"));
                    } catch (Exception e) {
                        try {
                            return LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
                        } catch (Exception e2) {
                            return LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        }
                    }
                }
                
                // 使用默认处理
                return super.deserialize(parser, context);
            }
        };
        
        javaTimeModule.addDeserializer(LocalDateTime.class, localDateTimeDeserializer);
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        
        return Jackson2ObjectMapperBuilder.json()
                .modules(javaTimeModule)
                .build();
    }
}