# Spring AI × 智普AI 论文引用代码片段

以下片段可直接用于论文，短小且关键，注释清晰：依赖、配置、交互与提示词模板。

## 依赖（pom.xml）

```xml
<!-- 智普AI Spring AI Starter：自动配置 ChatClient -->
<dependency>
  <groupId>org.springframework.ai</groupId>
  <artifactId>spring-ai-zhipuai-spring-boot-starter</artifactId>
  <version>0.8.1</version>
  <!-- 论文中只展示关键依赖，其他依赖省略 -->
</dependency>
```

## 配置（application.yml）

```yaml
spring:
  ai:
    zhipuai:
      api-key: ${ZHIPU_API_KEY}   # 环境变量读取，避免明文
      chat:
        options:
          model: glm-4-flash      # 轻量快速，适合中文交互
```

## 交互与提示词模板（Java）

```java
// 与智普AI交互：生成面向老年护理的专业建议
@Service
public class MedicalAdviceService {

    @Autowired
    private ChatClient chatClient; // 由starter自动注入

    // 专业边界与输出结构（System角色）
    private static final String SYSTEM_PROMPT =
            "你是一名资深老年护理助手，提供循证的健康建议。\n" +
            "边界：不替代医生诊断；出现红旗症状需建议立即就医。\n" +
            "输出结构：1) 风险评估 2) 监测要点 3) 当天可执行建议 4) 何时就医 5) 用药提醒（如有）。";

    // 用户模板：注入关键变量，保持模板可复用
    private static final String USER_TEMPLATE =
            "患者信息：\n" +
            "- 年龄：{age}\n- 主要症状：{symptoms}\n- 生命体征：{vitals}\n" +
            "- 既往病史：{history}\n- 当前用药：{medications}\n" +
            "请基于以上信息，生成安全、可执行的护理建议。";

    public String generateAdvice(int age, String symptoms, String vitals,
                                 String history, String medications) {
        var userPrompt = new PromptTemplate(USER_TEMPLATE)
                .create(Map.of(
                        "age", age,
                        "symptoms", symptoms,
                        "vitals", vitals,
                        "history", history,
                        "medications", medications
                ));

        return chatClient
                .prompt()
                .system(SYSTEM_PROMPT)   // 设定专业边界与输出结构
                .user(userPrompt)        // 注入患者信息
                .call()
                .content();              // 返回建议文本
    }
}
```

---

提示词设计要点（论文可摘录）：
- 明确角色边界，降低风险（非诊断、红旗症状→就医）。
- 固定输出结构，便于评估可执行性与一致性。
- 关键变量注入，信息最小化：年龄、症状、生命体征、病史、用药。
- 语言约束：中文、简洁、条目化，避免模糊与过度推断。