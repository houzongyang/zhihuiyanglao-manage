# Beta版发布指南（Spring Boot + Vue.js）

## 目标
- 将当前项目以“可试用的Beta版”发布给受控用户，便于收集反馈。
- 保留必要的日志与监控，明确“Beta”标识与回滚策略。

## 版本命名（SemVer + 预发布标签）
- 后端版本示例：`1.0.0-beta.1`
- 前端版本示例：`1.0.0-beta.1`
- 统一在变更日志中记录：`docs/CHANGELOG.md`（建议新增）

## 后端（Spring Boot）准备
- 修改 `springboot/pom.xml` 中的项目版本为预发布：
  - `<version>1.0.0-beta.1</version>`
- 新增 Beta 配置档（建议）：
  - 在 `springboot/src/main/resources/` 下新增 `application-beta.yml`，用于指向“预发布/测试环境”资源。
  - 示例内容（按实际环境修改）：
    - ```yaml
      server:
        port: 8080
      spring:
        profiles: beta
        datasource:
          url: jdbc:mysql://<staging-host>:3306/<db>?useSSL=false&serverTimezone=UTC
          username: <staging-user>
          password: <staging-pass>
        jackson:
          date-format: yyyy-MM-dd HH:mm:ss
          time-zone: GMT+8
      logging:
        level:
          root: INFO
          com.xm.association: DEBUG
      app:
        featureFlags:
          enableSmartAdvice: true
          enablePsychEval: false
      ```
- 启动参数（本地/服务器）：
  - `java -jar xm-association-manager-1.0.0-beta.1.jar --spring.profiles.active=beta`
- 构建命令（Windows PowerShell）：
  - `mvn -f springboot/pom.xml clean package -DskipTests`
  - 产物位于 `springboot/target/*.jar`

## 前端（Vue.js，Vue CLI）准备
- 修改 `vue-new/package.json` 的 `version` 为 `1.0.0-beta.1`。
- 新增环境文件 `.env.beta`（位于 `vue-new/` 根目录）：
  - ```env
    VUE_APP_ENV=beta
    VUE_APP_API_BASE_URL=https://staging.api.example.com
    VUE_APP_FEATURE_ENABLE_SMART_ADVICE=true
    VUE_APP_FEATURE_ENABLE_PSYCH_EVAL=false
    ```
- Beta 构建（Windows PowerShell）：
  - 在 `vue-new/` 目录运行：
    - `npm ci`
    - `npm run build -- --mode beta`  （Vue CLI 将读取 `.env.beta`）
  - 构建产物位于 `vue-new/dist/`

## Beta标识与反馈渠道
- 在前端页眉/页脚显示“Beta”标识（基于 `VUE_APP_ENV` 条件渲染）。
- 增加“问题反馈”入口，收集用户问题（Issue表单或第三方如 Sentry）。
- 后端打开关键模块的 INFO/DEBUG 日志以便排查（生产勿过度）。

## 监控与埋点（建议）
- 接入错误监控：前端 Sentry，后端日志聚合（ELK/Cloud Logging）。
- 关键路径埋点：
  - `健康评估`接口响应时间、成功率、吞吐量（req/s）。
  - 用户满意度反馈入口点击率与提交率。

## 发布与部署
- 推荐“预发布环境（staging）”部署，限制访问（IP白名单或账号邀请）。
- 后端：
  - 将 `*.jar` 部署到应用服务器，`--spring.profiles.active=beta`。
- 前端：
  - 部署 `dist/` 到静态服务器或 CDN，后端 `API_BASE_URL` 指向 Beta 环境。
- 可选 Docker 化：
  - 后端镜像：`xm-association-manager-backend:1.0.0-beta.1`
  - 前端镜像：`xm-association-manager-frontend:1.0.0-beta.1`

## 测试与质量门禁（Beta前）
- 冒烟测试：核心路由与健康评估流程可用。
- 指标基线：
  - `F1`（健康建议准确性）、`吞吐量`（req/s）、`满意度`（问卷平均分）。
- 回归检查：关键页面加载无报错，主要接口2xx成功率≥99%。

## 风险与回滚
- 快速回滚策略：保留上一稳定版产物与环境配置；一键切换负载到稳定版。
- 日志与告警：异常波动自动报警（响应时间、错误率）。

## 最小改动清单（便于落地）
- 后端：
  - `pom.xml` 版本改为 `1.0.0-beta.1`。
  - 新增 `application-beta.yml` 并指向预发布资源。
  - 启动时使用 `--spring.profiles.active=beta`。
- 前端：
  - `package.json` 版本改为 `1.0.0-beta.1`。
  - 新增 `.env.beta` 并设置 `VUE_APP_API_BASE_URL`。
  - 构建命令：`npm run build -- --mode beta`。
- 标识与反馈：
  - UI 显示“Beta”标签；提供反馈入口。

## 实用命令汇总（Windows）
- 后端：
  - `mvn -f springboot/pom.xml clean package -DskipTests`
  - `java -jar springboot/target/xxx-1.0.0-beta.1.jar --spring.profiles.active=beta`
- 前端：
  - `cd vue-new && npm ci && npm run build -- --mode beta`

## 附：伦理与合规（Beta阶段应同步落实）
- 数据隐私：最小化采集、加密存储、访问审计、明确知情同意。
- 责任边界：AI仅为辅助，关键决策由专业人员审批；建立纠错与干预机制。
- 公平可及：适老化设计与替代渠道保留，避免数字鸿沟。

如需，我可以继续为您添加示例 `application-beta.yml` 与 `.env.beta` 模板文件，并在前端页面加上“Beta”角标与反馈入口的示例代码。