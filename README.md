# 智能养老管理系统 (Smart Elderly Care Management System)

本项目是一个基于 **Spring Boot 3** 和 **Vue 2** 构建的现代化智能养老管理系统。系统深度集成了 **智普 AI (ZhipuAI)** 大模型，实现了从传统的数字化记录到智能化服务的根本性转变，显著提升了养老机构的健康管理效率和服务质量。

## 🚀 核心突破

- **技术突破**：深度集成智普 AI 大模型，利用 Spring AI 框架实现智能化健康评估和建议生成。
- **业务突破**：健康评估响应速度从小时级缩短至秒级（提升 9,000+ 倍），实现实时健康预警。
- **模式突破**：从被动的数据记录转变为主动的健康管理，提供个性化的照护方案。

## ✨ 主要功能

### 1. 智能化服务 (AI Powered)

- **智能健康评估**：基于老人实时健康数据，AI 自动生成健康状况分析报告。
- **智能用药建议**：AI 根据老人病史和当前症状提供专业用药参考。
- **风险自动预警**：系统自动识别异常健康指标并及时发出预警。

### 2. 健康管理

- **健康档案**：记录老人的基础体征、过敏史、既往病史等详细信息。
- **实时监测**：监控老人的体温、血压、心率等关键生理指标。
- **用药管理**：详细记录每日用药计划及执行情况。

### 3. 照护服务

- **照护计划**：为每位老人定制个性化的生活照护和医疗护理方案。
- **照护记录**：实时记录日常护理工作的执行过程。
- **任务分配**：高效分配护理人员任务，确保服务质量。

### 4. 活动与社交

- **活动管理**：发布和管理养老机构内的各类文娱、康复活动。
- **留言板与聊天**：提供老人、家属及工作人员之间的沟通渠道。

### 5. 综合管理

- **费用管理**：自动计算养老费用，管理缴费记录。
- **公告通知**：及时发布机构动态和重要通知。
- **权限管理**：支持管理员、护士、普通用户等多种角色权限控制。

## 🛠️ 技术栈

### 后端 (Backend)

- **核心框架**：Spring Boot 3.2.5
- **数据库**：MySQL
- **持久层**：MyBatis
- **AI 框架**：Spring AI + 智普 AI (ZhipuAI)
- **工具类**：Hutool, Lombok
- **认证授权**：JWT (java-jwt)

### 前端 (Frontend)

- **核心框架**：Vue 2.6.11
- **UI 组件库**：Element-UI 2.15.6
- **数据可视化**：ECharts 5.6.0
- **网络请求**：Axios

## 📂 项目结构

```text
xm-association-manager
├── springboot            # 后端项目源码
│   ├── src/main/java     # Java 代码
│   ├── src/main/resources# 配置文件与 MyBatis 映射文件
│   └── pom.xml           # Maven 依赖配置
├── vue-new               # 前端项目源码
│   ├── src/views         # 页面组件
│   ├── src/router        # 路由配置
│   └── package.json      # 前端依赖配置
├── docs                  # 项目文档 (技术分析、架构说明等)
└── files                 # 上传的文件与图片资源
```

## ⚙️ 快速开始

### 环境要求

- **JDK**: 17+
- **Node.js**: 14+
- **MySQL**: 5.7+
- **Maven**: 3.6+

### 1. 数据库配置

1. 创建数据库 `zhihuiyanglao-manage`。
2. 运行项目提供的 SQL 脚本（如果存在）或根据实体类自动生成。
3. 修改 `springboot/src/main/resources/application.yml` 中的数据库连接信息：
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/zhihuiyanglao-manage?...
       username: your_username
       password: your_password
   ```

### 2. AI 配置

在 `application.yml` 中配置您的智普 AI API Key：

```yaml
spring:
  ai:
    zhipuai:
      api-key: your_api_key_here
```

### 3. 启动后端

```bash
cd springboot
mvn clean install
mvn spring-boot:run
```

### 4. 启动前端

```bash
cd vue-new
npm install
npm run serve
```

访问地址：`http://localhost:8080` (默认端口)

## 📄 许可证

本项目遵循 [MIT License](LICENSE) 开源协议。
