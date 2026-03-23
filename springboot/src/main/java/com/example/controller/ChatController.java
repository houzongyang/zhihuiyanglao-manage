package com.example.controller;

import com.example.common.Result;
import com.example.entity.HealthRecord;
import com.example.entity.HealthMonitoring;
import com.example.service.HealthRecordService;
import com.example.service.HealthMonitoringService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.zhipuai.ZhiPuAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import jakarta.annotation.Resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ChatController {
    
    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);
    
    private final ZhiPuAiChatModel chatModel;
    
    @Value("${spring.ai.zhipuai.api-key}")
    private String apiKey;
    
    @Resource
    private HealthRecordService healthRecordService;
    
    @Resource
    private HealthMonitoringService healthMonitoringService;
    
//    @Value("${spring.ai.zhipuai.base-url}")
//    private String baseUrl;

    @Autowired
    public ChatController(ZhiPuAiChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @GetMapping("/ai/generate")
    public Result generate(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        logger.info("接收到AI请求，消息：{}", message);
        logger.info("当前API密钥：{}", apiKey);
//        logger.info("当前Base URL：{}", baseUrl);
        
        try {
            String response = this.chatModel.call(message);
            logger.info("AI响应成功: {}", response);
            Map<String, Object> data = new HashMap<>();
            data.put("generation", response);
            return Result.success(data);
        } catch (Exception e) {
            logger.error("调用智谱AI时发生错误：", e);
            return Result.error("500", "AI服务暂时不可用: " + e.getMessage());
        }
    }

    @GetMapping("/ai/generateStream")
    public Flux<ChatResponse> generateStream(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        logger.info("接收到AI流式请求，消息：{}", message);
        try {
            var prompt = new Prompt(new UserMessage(message));
            return this.chatModel.stream(prompt);
        } catch (Exception e) {
            logger.error("调用智谱AI流式接口时发生错误：", e);
            throw e;
        }
    }

    /**
     * 生成健康评估报告
     */
    @GetMapping("/ai/healthAssessment")
    public Result generateHealthAssessment(@RequestParam Integer userId) {
        logger.info("接收到健康评估请求，用户ID：{}", userId);
        
        try {
            // 获取用户健康档案
            HealthRecord healthRecord = healthRecordService.selectByUserId(userId);
            if (healthRecord == null) {
                return Result.error("404", "未找到该用户的健康档案，请先完善健康信息");
            }
            
            // 获取用户最近的健康监控数据
            HealthMonitoring latestMonitoring = healthMonitoringService.getLatestByUserId(userId);
            
            // 构建AI提示词
            String prompt = buildHealthAssessmentPrompt(healthRecord, latestMonitoring);
            
            // 调用AI生成评估报告
            String aiResponse = this.chatModel.call(prompt);
            logger.info("AI健康评估响应成功");
            
            // 构建返回数据
            Map<String, Object> data = new HashMap<>();
            data.put("userId", userId);
            data.put("assessmentTime", System.currentTimeMillis());
            data.put("healthRecord", healthRecord);
            data.put("latestMonitoring", latestMonitoring);
            data.put("aiAssessment", aiResponse);
            
            return Result.success(data);
        } catch (Exception e) {
            logger.error("生成健康评估报告时发生错误：", e);
            return Result.error("500", "健康评估服务暂时不可用: " + e.getMessage());
        }
    }
    
    /**
     * 构建健康评估的AI提示词
     */
    private String buildHealthAssessmentPrompt(HealthRecord healthRecord, HealthMonitoring latestMonitoring) {
        StringBuilder prompt = new StringBuilder();
        
        prompt.append("请作为一名专业的老年健康评估专家，基于以下老年人的健康档案和最新监控数据，生成一份详细的健康状况评估报告。\n\n");
        
        // 基本健康档案信息
        prompt.append("【健康档案信息】\n");
        prompt.append("血型：").append(healthRecord.getBloodType() != null ? healthRecord.getBloodType() : "未知").append("\n");
        prompt.append("身高：").append(healthRecord.getHeight() != null ? healthRecord.getHeight() + "cm" : "未记录").append("\n");
        prompt.append("体重：").append(healthRecord.getWeight() != null ? healthRecord.getWeight() + "kg" : "未记录").append("\n");
        
        // BMI计算
        if (healthRecord.getHeight() != null && healthRecord.getWeight() != null && healthRecord.getHeight() > 0) {
            double bmi = healthRecord.getWeight() / Math.pow(healthRecord.getHeight() / 100, 2);
            prompt.append("BMI指数：").append(String.format("%.1f", bmi)).append("\n");
        }
        
        prompt.append("慢性疾病：").append(healthRecord.getChronicDisease() != null ? healthRecord.getChronicDisease() : "无").append("\n");
        prompt.append("病史记录：").append(healthRecord.getMedicalHistory() != null ? healthRecord.getMedicalHistory() : "无特殊病史").append("\n");
        prompt.append("药物过敏史：").append(healthRecord.getDrugAllergies() != null ? healthRecord.getDrugAllergies() : "无药物过敏史").append("\n");
        prompt.append("体检记录：").append(healthRecord.getPhysicalExamination() != null ? healthRecord.getPhysicalExamination() : "暂无体检记录").append("\n");
        
        // 最新监控数据
        prompt.append("\n【最新健康监控数据】\n");
        if (latestMonitoring != null) {
            prompt.append("监测日期：").append(latestMonitoring.getRecordDate()).append("\n");
            prompt.append("体温：").append(latestMonitoring.getBodyTemperature() != null ? latestMonitoring.getBodyTemperature() + "°C" : "未测量").append("\n");
            prompt.append("血压：");
            if (latestMonitoring.getSystolicPressure() != null && latestMonitoring.getDiastolicPressure() != null) {
                prompt.append(latestMonitoring.getSystolicPressure()).append("/").append(latestMonitoring.getDiastolicPressure()).append("mmHg");
            } else {
                prompt.append("未测量");
            }
            prompt.append("\n");
            prompt.append("心率：").append(latestMonitoring.getHeartRate() != null ? latestMonitoring.getHeartRate() + "次/分" : "未测量").append("\n");
            prompt.append("血糖：").append(latestMonitoring.getBloodSugar() != null ? latestMonitoring.getBloodSugar() + "mmol/L" : "未测量").append("\n");
            prompt.append("血氧饱和度：").append(latestMonitoring.getBloodOxygen() != null ? latestMonitoring.getBloodOxygen() + "%" : "未测量").append("\n");
            prompt.append("睡眠时长：").append(latestMonitoring.getSleepHours() != null ? latestMonitoring.getSleepHours() + "小时" : "未记录").append("\n");
            prompt.append("睡眠质量：").append(latestMonitoring.getSleepQuality() != null ? latestMonitoring.getSleepQuality() : "未评估").append("\n");
            prompt.append("情绪状态：").append(latestMonitoring.getMoodStatus() != null ? latestMonitoring.getMoodStatus() : "未记录").append("\n");
            prompt.append("异常症状：").append(latestMonitoring.getAbnormalSymptoms() != null ? latestMonitoring.getAbnormalSymptoms() : "无").append("\n");
        } else {
            prompt.append("暂无最新监控数据\n");
        }
        
        prompt.append("\n请按照以下格式生成评估报告（请严格按照此格式，每个部分用【】标记）：\n\n");
        prompt.append("【健康状况总评】\n");
        prompt.append("（请给出整体健康状况的评价，包括良好、一般、需要关注等级别）\n\n");
        
        prompt.append("【主要健康指标分析】\n");
        prompt.append("（分析各项生理指标是否正常，如血压、心率、血糖、BMI等）\n\n");
        
        prompt.append("【风险因素识别】\n");
        prompt.append("（基于慢性疾病、异常指标等识别潜在健康风险）\n\n");
        
        prompt.append("【健康建议】\n");
        prompt.append("（提供具体的饮食、运动、生活方式建议）\n\n");
        
        prompt.append("【医疗建议】\n");
        prompt.append("（是否需要就医检查、复查频率等专业医疗建议）\n\n");
        
        prompt.append("【注意事项】\n");
        prompt.append("（重点关注的健康问题和日常注意事项）\n\n");
        
        prompt.append("请确保评估报告专业、准确、易懂，适合老年人及其家属阅读。");
        
        return prompt.toString();
    }
}