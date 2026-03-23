<template>
  <div class="min-h-screen bg-gradient-to-br from-green-50 to-teal-100 flex items-center justify-center py-12 px-4 sm:px-6 lg:px-8">
    <div class="max-w-6xl w-full flex flex-col md:flex-row rounded-2xl overflow-hidden shadow-2xl">
      <!-- 左侧信息区域 -->
      <div class="w-full md:w-1/2 bg-gradient-to-br from-green-500 to-teal-600 p-8 text-white relative overflow-hidden">
        <div class="relative z-10">
          <div class="flex items-center mb-8">
            <img src="/img/logo.svg" alt="Logo" class="h-10 w-10 mr-3" />
            <h1 class="text-2xl font-bold">智慧养老服务平台</h1>
          </div>
          <h2 class="text-xl font-medium mb-6">关爱长者 · 智慧服务</h2>
          
          <div class="space-y-6">
            <div class="flex items-start">
              <div class="rounded-full bg-white bg-opacity-20 p-3 mr-4">
                <i class="el-icon-s-help text-xl"></i>
              </div>
              <div>
                <h3 class="text-lg font-medium mb-1">智能健康监测</h3>
                <p class="text-sm text-white text-opacity-80">实时监测健康数据，提供及时关爱服务</p>
              </div>
            </div>
            
            <div class="flex items-start">
              <div class="rounded-full bg-white bg-opacity-20 p-3 mr-4">
                <i class="el-icon-s-cooperation text-xl"></i>
              </div>
              <div>
                <h3 class="text-lg font-medium mb-1">社区活动管理</h3>
                <p class="text-sm text-white text-opacity-80">丰富多彩的社区活动，增进邻里互动</p>
              </div>
            </div>
            
            <div class="flex items-start">
              <div class="rounded-full bg-white bg-opacity-20 p-3 mr-4">
                <i class="el-icon-chat-line-round text-xl"></i>
              </div>
              <div>
                <h3 class="text-lg font-medium mb-1">智能助手服务</h3>
                <p class="text-sm text-white text-opacity-80">AI智能问答，提供便捷生活咨询</p>
              </div>
            </div>
          </div>
          
          <div class="mt-12 grid grid-cols-3 gap-4 text-center">
            <div>
              <div class="text-3xl font-bold">5000+</div>
              <div class="text-sm text-white text-opacity-80">服务长者</div>
            </div>
            <div>
              <div class="text-3xl font-bold">120</div>
              <div class="text-sm text-white text-opacity-80">专业人员</div>
            </div>
            <div>
              <div class="text-3xl font-bold">98%</div>
              <div class="text-sm text-white text-opacity-80">满意度</div>
            </div>
          </div>
          
          <div class="mt-12 text-sm text-center text-white text-opacity-70">
            ©2025 智慧养老服务平台 版权所有
          </div>
        </div>
        
        <!-- 装饰圆圈 -->
        <div class="absolute top-0 right-0 w-64 h-64 rounded-full bg-white bg-opacity-10 -mt-20 -mr-20"></div>
        <div class="absolute bottom-0 left-0 w-80 h-80 rounded-full bg-white bg-opacity-10 -mb-40 -ml-40"></div>
      </div>
      
      <!-- 右侧登录表单 -->
      <div class="w-full md:w-1/2 bg-white p-8">
        <div class="max-w-md mx-auto">
          <div class="text-center mb-10">
            <h2 class="text-3xl font-bold text-gray-800 mb-2">欢迎登录</h2>
            <p class="text-gray-500">请使用您的账号登录智慧养老服务平台</p>
          </div>
          
          <el-form ref="loginForm" :model="form" :rules="rules" class="space-y-6">
            <el-form-item prop="username">
              <div class="flex items-center border border-gray-300 rounded-lg overflow-hidden focus-within:ring-2 focus-within:ring-blue-500 focus-within:border-blue-500 transition-all">
                <div class="w-12 flex items-center justify-center bg-gray-50 h-full border-r border-gray-300">
                  <i class="el-icon-user text-gray-400"></i>
                </div>
                <el-input 
                  v-model="form.username" 
                  placeholder="请输入账号" 
                  class="flex-1 border-none shadow-none"
                  :class="{'tw-input': true}">
                </el-input>
              </div>
            </el-form-item>
            
            <el-form-item prop="password">
              <div class="flex items-center border border-gray-300 rounded-lg overflow-hidden focus-within:ring-2 focus-within:ring-blue-500 focus-within:border-blue-500 transition-all">
                <div class="w-12 flex items-center justify-center bg-gray-50 h-full border-r border-gray-300">
                  <i class="el-icon-lock text-gray-400"></i>
                </div>
                <el-input 
                  v-model="form.password" 
                  type="password" 
                  placeholder="请输入密码" 
                  class="flex-1 border-none shadow-none"
                  :class="{'tw-input': true}">
                </el-input>
              </div>
            </el-form-item>
            
            <el-form-item prop="role">
              <div class="flex items-center border border-gray-300 rounded-lg overflow-hidden focus-within:ring-2 focus-within:ring-blue-500 focus-within:border-blue-500 transition-all">
                <div class="w-12 flex items-center justify-center bg-gray-50 h-full border-r border-gray-300">
                  <i class="el-icon-s-custom text-gray-400"></i>
                </div>
                <el-select 
                  v-model="form.role" 
                  placeholder="普通用户" 
                  class="flex-1 border-none"
                  :class="{'tw-select': true}">
                  <el-option label="普通用户" value="USER"></el-option>
                  <el-option label="护士" value="NURSE"></el-option>
                  <el-option label="管理员" value="ADMIN"></el-option>
                </el-select>
              </div>
            </el-form-item>
            
            <div class="flex items-center justify-between">
              <el-checkbox v-model="form.remember" class="text-gray-600">记住我</el-checkbox>
              <router-link to="/forgot-password" class="text-sm text-green-600 hover:text-green-800 transition-colors">忘记密码?</router-link>
            </div>
            
            <div>
              <button 
                type="button" 
                class="w-full py-3 px-4 bg-gradient-to-r from-green-500 to-teal-600 text-white font-medium rounded-lg shadow-md hover:from-green-600 hover:to-teal-700 focus:outline-none focus:ring-2 focus:ring-green-500 focus:ring-offset-2 transition-all duration-150 flex items-center justify-center"
                @click="login"
                :disabled="loading">
                <i v-if="loading" class="el-icon-loading mr-2"></i>
                {{ loading ? '登录中...' : '登 录' }}
              </button>
            </div>
            
            <div class="text-center text-sm text-gray-600">
              还没有账号? <router-link to="/register" class="text-green-600 hover:text-green-800 transition-colors">立即注册</router-link>
            </div>
          </el-form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Login",
  data() {
    return {
      form: {
        username: "",
        password: "",
        role: "USER",
        remember: false
      },
      rules: {
        username: [
          { required: true, message: "请输入账号", trigger: "blur" },
          { min: 3, max: 20, message: "长度在 3 到 20 个字符", trigger: "blur" }
        ],
       
        role: [
          { required: true, message: "请选择角色", trigger: "change" }
        ]
      },
      loading: false
    };
  },
  methods: {
    login() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true;
          this.$request.post("/login", this.form).then(res => {
            if (res.code === '200') {
              // 保存用户信息和token
              localStorage.setItem("xm-user", JSON.stringify(res.data));
              localStorage.setItem("token", res.data.token);
              
              // 使用后端返回的角色，而不是表单选择的角色进行跳转
              this.redirectByRole(res.data.role);
              
              this.$message.success("登录成功");
            } else {
              this.$message.error(res.msg || "登录失败");
            }
          }).catch(err => {
            console.error(err);
            this.$message.error("登录失败，请稍后重试");
          }).finally(() => {
            this.loading = false;
          });
        } else {
          return false;
        }
      });
    },
    redirectByRole(role) {
      switch(role) {
        case 'ADMIN':
          this.$router.push("/manager/home");
          break;
        case 'NURSE':
          this.$router.push("/nurse/home");
          break;
        case "USER":
          this.$router.push("/front/home");
          break;
        default:
          this.$router.push("/front/home");
          break;
      }
    }
  }
};
</script>

<style scoped>
/* 页面容器 */
.page-container {
  min-height: 100vh;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #e8f0fe 0%, #f0f7ff 25%, #edf7ff 50%, #e6f2ff 75%, #e0edfe 100%);
  background-size: 400% 400%;
  animation: gradientBackground 15s ease infinite;
  padding: 20px;
  position: relative;
  overflow: hidden;
}

@keyframes gradientBackground {
  0% {
    background-position: 0% 50%;
  }
  50% {
    background-position: 100% 50%;
  }
  100% {
    background-position: 0% 50%;
  }
}

/* 背景装饰 */
.page-container:before {
  content: "";
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: radial-gradient(circle, rgba(63, 81, 181, 0.03) 0%, rgba(33, 150, 243, 0.01) 50%, rgba(240, 247, 255, 0) 70%);
  animation: rotate 30s linear infinite;
  z-index: 0;
}

.page-container:after {
  content: "";
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: radial-gradient(circle, rgba(33, 150, 243, 0.03) 0%, rgba(63, 81, 181, 0.01) 50%, rgba(240, 247, 255, 0) 70%);
  animation: rotate 30s linear infinite reverse;
  z-index: 0;
}

@keyframes rotate {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

/* 主容器样式 */
.login-container {
  width: 1000px;
  max-width: 100%;
  height: 600px;
  display: flex;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 10px 30px rgba(33, 150, 243, 0.15), 0 5px 15px rgba(63, 81, 181, 0.1);
  margin: auto;
  position: relative;
  z-index: 1;
  backdrop-filter: blur(5px);
  background-color: rgba(255, 255, 255, 0.8);
}

/* 左侧展示区域 */
.left-section {
  flex: 1;
  background: linear-gradient(135deg, #3f51b5, #2196f3);
  color: white;
  position: relative;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.content-wrapper {
  position: relative;
  z-index: 2;
  padding: 40px 30px;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.brand {
  margin-bottom: 40px;
}

.logo-container {
  display: flex;
  align-items: center;
}

.logo {
  width: 50px;
  height: 50px;
  margin-right: 15px;
}

.title-group h1 {
  font-size: 24px;
  font-weight: bold;
  margin: 0 0 5px;
}

.title-group p {
  font-size: 14px;
  opacity: 0.9;
  margin: 0;
}

/* 特性列表 */
.features {
  margin-bottom: 30px;
}

.feature-item {
  display: flex;
  align-items: flex-start;
  margin-bottom: 20px;
}

.feature-icon {
  width: 40px;
  height: 40px;
  border-radius: 12px;
  background: rgba(255, 255, 255, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
  flex-shrink: 0;
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.1);
}

.feature-icon i {
  font-size: 20px;
}

.feature-content h3 {
  font-size: 16px;
  margin: 0 0 5px;
}

.feature-content p {
  font-size: 13px;
  opacity: 0.8;
  margin: 0;
}

/* 统计数据 */
.stats {
  display: flex;
  margin-bottom: 20px;
  margin-top: auto;
}

.stat-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.stat-number {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 13px;
  opacity: 0.8;
}

.footer {
  text-align: center;
  font-size: 12px;
  opacity: 0.7;
}

/* 装饰元素 */
.decoration {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 1;
}

.circle {
  position: absolute;
  border-radius: 50%;
  background: radial-gradient(circle, rgba(255,255,255,0.1) 0%, rgba(255,255,255,0) 70%);
}

.circle-1 {
  width: 250px;
  height: 250px;
  top: -80px;
  left: -80px;
}

.circle-2 {
  width: 300px;
  height: 300px;
  bottom: -100px;
  right: -100px;
}

.circle-3 {
  width: 150px;
  height: 150px;
  top: 50%;
  left: 20%;
}

.floating-icon {
  position: absolute;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  opacity: 0.7;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.icon-1 {
  top: 20%;
  right: 15%;
  animation: float 6s infinite;
}

.icon-2 {
  bottom: 25%;
  left: 10%;
  animation: float 8s infinite;
}

.icon-3 {
  top: 60%;
  right: 30%;
  animation: float 7s infinite;
}

@keyframes float {
  0% {
    transform: translateY(0px);
  }
  50% {
    transform: translateY(-15px);
  }
  100% {
    transform: translateY(0px);
  }
}

/* 右侧登录表单区域 */
.right-section {
  width: 400px;
  background-color: #ffffff;
  display: flex;
  align-items: center;
  justify-content: center;
}

.form-container {
  width: 320px;
  padding: 20px 0;
}

.form-header {
  text-align: center;
  margin-bottom: 30px;
}

.form-header h2 {
  color: #333;
  font-size: 24px;
  font-weight: 600;
  margin: 0 0 10px;
}

.form-header p {
  color: #888;
  font-size: 14px;
  margin: 0;
}

.login-form {
  margin-bottom: 25px;
}

/* 修复图标对齐问题 */
.input-with-icon {
  display: flex;
  align-items: center;
  width: 100%;
}

.icon-column {
  width: 40px; /* 图标列的宽度 */
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.icon-column i {
  font-size: 16px;
  color: #bbb;
  z-index: 2;
  height: 16px;
  width: 16px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  pointer-events: none; /* 防止图标干扰输入 */
}

.input-with-icon .el-input__inner,
.input-with-icon .el-input input {
  padding-left: 12px !important;
}

.custom-input input {
  padding-left: 40px;
  height: 40px;
  font-size: 14px;
  border-radius: 8px;
  line-height: 40px;
}

.custom-select {
  width: 100%;
}

.custom-select .el-input input {
  padding-left: 40px;
  height: 40px;
  line-height: 40px;
  font-size: 14px;
  border-radius: 8px;
}

.option-content {
  display: flex;
  align-items: center;
}

.option-content i {
  margin-right: 8px;
  font-size: 16px;
}

.form-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.remember-text {
  font-size: 14px;
  color: #666;
}

.forgot-link {
  font-size: 14px;
  color: #2196f3;
  text-decoration: none;
}

.submit-button {
  width: 100%;
  height: 40px;
  border-radius: 8px;
  font-size: 16px;
  letter-spacing: 2px;
  background: linear-gradient(135deg, #3f51b5, #2196f3);
  border: none;
  box-shadow: 0 4px 12px rgba(33, 150, 243, 0.3);
  transition: all 0.3s;
}

.submit-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(33, 150, 243, 0.4);
}

.form-footer {
  text-align: center;
  color: #888;
  font-size: 14px;
}

.register-link {
  color: #2196f3;
  font-weight: 600;
  text-decoration: none;
}

/* 响应式调整 */
@media screen and (max-width: 768px) {
  .login-container {
    flex-direction: column;
    height: auto;
  }
  
  .left-section {
    display: none;
  }
  
  .right-section {
    width: 100%;
    padding: 40px 0;
  }
}

/* 覆盖Element UI的样式 */
:deep(.el-input__inner) {
  border: none;
  box-shadow: none !important;
  padding-left: 0 !important;
  height: 46px;
}

:deep(.el-select) {
  width: 100%;
}

:deep(.el-select .el-input__inner) {
  border: none;
  box-shadow: none !important;
  padding-left: 0 !important;
  height: 46px;
}

:deep(.el-checkbox__label) {
  font-weight: normal;
}
</style>