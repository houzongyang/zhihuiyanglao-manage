<template>
  <div class="not-found">
    <div class="container">
      <div class="error-code">404</div>
      <div class="error-desc">抱歉，您访问的页面不存在</div>
      <el-button type="primary" @click="$router.push('/')" class="back-btn">返回首页</el-button>
    </div>
  </div>
</template>

<script>
export default {
  name: "NotFound"
}
</script>

<style scoped>
.not-found {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100vh;
  background-color: #f0f2f5;
}

.container {
  text-align: center;
}

.error-code {
  font-size: 120px;
  font-weight: bold;
  color: #409EFF;
  margin-bottom: 20px;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.1);
}

.error-desc {
  font-size: 22px;
  color: #606266;
  margin-bottom: 30px;
}

.back-btn {
  padding: 12px 20px;
  font-size: 16px;
}
</style> 