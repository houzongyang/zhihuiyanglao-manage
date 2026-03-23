<template>
  <div class="page-container">
    <div class="register-container">
      <!-- 左侧展示区域 -->
      <div class="left-section">
        <div class="content-wrapper">
          <div class="brand">
            <div class="logo-container">
              <img src="/img/logo.svg" alt="Logo" class="logo">
              <div class="title-group">
                <h1>智慧养老服务平台</h1>
                <p>快速注册 · 立即体验</p>
              </div>
            </div>
          </div>
          
          <div class="register-process">
            <div class="process-title">注册流程</div>
            
            <div class="process-item">
              <div class="process-step">
                <span class="step-number">01</span>
                <div class="step-line"></div>
              </div>
              <div class="process-content">
                <h3>填写信息</h3>
                <p>完善您的基本信息与联系方式</p>
              </div>
            </div>
            
            <div class="process-item">
              <div class="process-step">
                <span class="step-number">02</span>
                <div class="step-line"></div>
              </div>
              <div class="process-content">
                <h3>账号注册</h3>
                <p>系统审核通过后创建您的账号</p>
              </div>
            </div>
            
            <div class="process-item">
              <div class="process-step">
                <span class="step-number">03</span>
              </div>
              <div class="process-content">
                <h3>立即使用</h3>
                <p>登录平台，享受智慧养老服务</p>
              </div>
            </div>
          </div>
          
          <div class="footer">
            <p>©2023 智慧养老服务平台 版权所有</p>
          </div>
        </div>
        
        <!-- 背景装饰 -->
        <div class="decoration">
          <div class="circle circle-1"></div>
          <div class="circle circle-2"></div>
          <div class="circle circle-3"></div>
          <div class="floating-icon icon-1">
            <i class="el-icon-edit-outline"></i>
          </div>
          <div class="floating-icon icon-2">
            <i class="el-icon-user"></i>
          </div>
          <div class="floating-icon icon-3">
            <i class="el-icon-s-cooperation"></i>
          </div>
        </div>
      </div>
      
      <!-- 右侧注册表单区域 -->
      <div class="right-section">
        <div class="form-container">
          <div class="form-header">
            <h2>用户注册</h2>
            <p>请完成以下信息填写，加入智慧养老服务平台</p>
          </div>
          
          <el-form :model="form" :rules="rules" ref="registerForm" class="register-form">
            <!-- 修改注册表单，将图标放在输入框外部 -->
            <el-form-item prop="username">
              <div class="input-with-icon">
                <div class="icon-column">
                  <i class="el-icon-user"></i>
                </div>
                <el-input v-model="form.username" placeholder="请输入账号"></el-input>
              </div>
            </el-form-item>
            <el-form-item prop="password">
              <div class="input-with-icon">
                <div class="icon-column">
                  <i class="el-icon-lock"></i>
                </div>
                <el-input v-model="form.password" type="password" placeholder="请输入密码"></el-input>
              </div>
            </el-form-item>
            <!-- 修复确认密码输入框 -->
            <el-form-item prop="confirmPassword">
              <div class="input-with-icon">
                <div class="icon-column">
                  <i class="el-icon-key"></i>
                </div>
                <el-input v-model="form.confirmPassword" type="password" placeholder="请确认密码"></el-input>
              </div>
            </el-form-item>
            
            <el-form-item prop="name">
              <div class="input-with-icon">
                <div class="icon-column">
                  <i class="el-icon-s-custom"></i>
                </div>
                <el-input v-model="form.name" placeholder="请输入姓名"></el-input>
              </div>
            </el-form-item>
            
            <el-form-item prop="phone">
              <div class="input-with-icon">
                <div class="icon-column">
                  <i class="el-icon-phone"></i>
                </div>
                <el-input v-model="form.phone" placeholder="请输入手机号"></el-input>
              </div>
            </el-form-item>
            
            <el-form-item prop="email">
              <div class="input-with-icon">
                <div class="icon-column">
                  <i class="el-icon-message"></i>
                </div>
                <el-input v-model="form.email" placeholder="请输入邮箱"></el-input>
              </div>
            </el-form-item>
            
            <el-form-item prop="role">
              <div class="input-with-icon">
                <div class="icon-column">
                  <i class="el-icon-s-check"></i>
                </div>
                <el-select v-model="form.role" placeholder="普通用户" style="width: 100%">
                  <el-option label="普通用户" value="USER"></el-option>
                  <el-option label="护士" value="NURSE"></el-option>
                </el-select>
              </div>
            </el-form-item>
            
            <div v-if="form.role === 'NURSE'" class="role-extra-fields">
              <el-form-item prop="department">
                <div class="input-icon-wrapper">
                  <i class="el-icon-office-building input-icon"></i>
                  <el-input 
                    v-model="form.department" 
                    placeholder="请输入所在科室/部门" 
                    class="custom-input">
                  </el-input>
                </div>
              </el-form-item>
            </div>
            
            <div v-if="form.role === 'USER'" class="role-extra-fields">
              <el-form-item prop="college">
                <div class="input-icon-wrapper">
                  <i class="el-icon-school input-icon"></i>
                  <el-input 
                    v-model="form.college" 
                    placeholder="请输入所在社区" 
                    class="custom-input">
                  </el-input>
                </div>
              </el-form-item>
              
              <el-form-item prop="major">
                <div class="input-icon-wrapper">
                  <i class="el-icon-reading input-icon"></i>
                  <el-input 
                    v-model="form.major" 
                    placeholder="请输入健康状况" 
                    class="custom-input">
                  </el-input>
                </div>
              </el-form-item>
            </div>
            
            <el-form-item>
              <el-button 
                type="primary" 
                class="submit-button" 
                @click="register"
                :loading="loading">
                立即注册
              </el-button>
            </el-form-item>
          </el-form>
          
          <div class="form-footer">
            <p>已有账号? <router-link to="/login" class="login-link">立即登录</router-link></p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Register",
  data() {
    // 确认密码验证规则
    const validateConfirmPassword = (rule, value, callback) => {
      if (value !== this.form.password) {
        callback(new Error('两次输入的密码不一致'));
      } else {
        callback();
      }
    };
    
    return {
      form: {
        username: "",
        password: "",
        confirmPassword: "",
        name: "",
        phone: "",
        email: "",
        role: "USER",
        department: "",
        college: "",
        major: ""
      },
      rules: {
        username: [
          { required: true, message: "请输入账号", trigger: "blur" },
          { min: 3, max: 20, message: "长度在 3 到 20 个字符", trigger: "blur" }
        ],
        password: [
          { required: true, message: "请输入密码", trigger: "blur" },
          { min: 6, max: 20, message: "长度在 6 到 20 个字符", trigger: "blur" }
        ],
        confirmPassword: [
          { required: true, message: "请确认密码", trigger: "blur" },
          { validator: validateConfirmPassword, trigger: "blur" }
        ],
        name: [
          { required: true, message: "请输入姓名", trigger: "blur" }
        ],
        phone: [
          { required: true, message: "请输入手机号", trigger: "blur" },
          { pattern: /^1[3-9]\d{9}$/, message: "请输入正确的手机号", trigger: "blur" }
        ],
        email: [
          { required: true, message: "请输入邮箱", trigger: "blur" },
          { type: "email", message: "请输入正确的邮箱地址", trigger: "blur" }
        ],
        role: [
          { required: true, message: "请选择角色", trigger: "change" }
        ],
        department: [
          { required: true, message: "请输入所在科室/部门", trigger: "blur" }
        ],
        college: [
          { required: true, message: "请输入所在社区", trigger: "blur" }
        ],
        major: [
          { required: true, message: "请输入健康状况", trigger: "blur" }
        ]
      },
      loading: false
    };
  },
  created() {
    // 清除可能存在的用户信息
    localStorage.removeItem("xm-user");
  },
  methods: {
    register() {
      this.$refs.registerForm.validate(valid => {
        if (valid) {
          this.loading = true;
          
          // 构建注册数据
          const registerData = {
            username: this.form.username,
            password: this.form.password,
            name: this.form.name,
            phone: this.form.phone,
            email: this.form.email,
            role: this.form.role
          };
          
          // 根据角色添加不同的字段
          if (this.form.role === 'NURSE') {
            registerData.department = this.form.department;
          } else if (this.form.role === 'USER') {
            registerData.college = this.form.college;
            registerData.major = this.form.major;
          }
          
          // 调用注册API
          this.$request.post("/register", registerData).then(res => {
            if (res.code === '0') {
              this.$message.success("注册成功，请登录");
              this.$router.push("/login");
            } else {
              this.$message.error(res.msg || "注册失败");
            }
          }).catch(() => {
            this.$message.error("注册请求异常");
          }).finally(() => {
            this.loading = false;
          });
        }
      });
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
  background: linear-gradient(135deg, #e8f5e9 0%, #f1f8e9 25%, #e8f6e3 50%, #e0f2f1 75%, #e0f7eb 100%);
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
.register-container {
  width: 1000px;
  max-width: 100%;
  height: 680px;
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
  background: linear-gradient(135deg, #2e7d32, #4caf50);
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

/* 注册流程 */
.register-process {
  flex: 1;
  margin: 20px 0;
}

.process-title {
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 30px;
  position: relative;
  display: inline-block;
}

.process-title:after {
  content: '';
  position: absolute;
  bottom: -8px;
  left: 0;
  width: 50px;
  height: 3px;
  background-color: rgba(255, 255, 255, 0.6);
  border-radius: 2px;
}

.process-item {
  display: flex;
  margin-bottom: 25px;
}

.process-step {
  margin-right: 15px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.step-number {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.3);
  border: 2px solid rgba(255, 255, 255, 0.6);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.step-line {
  width: 2px;
  height: 40px;
  background: rgba(255, 255, 255, 0.4);
}

.process-content h3 {
  font-size: 16px;
  margin: 0 0 6px;
  font-weight: 500;
}

.process-content p {
  font-size: 13px;
  opacity: 0.8;
  margin: 0;
}

.footer {
  text-align: center;
  font-size: 12px;
  opacity: 0.7;
  margin-top: auto;
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
  background: radial-gradient(circle, rgba(255,255,255,0.15) 0%, rgba(255,255,255,0) 70%);
  box-shadow: inset 0 0 20px rgba(255,255,255,0.05);
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

/* 右侧注册表单区域 */
.right-section {
  width: 400px;
  background-color: #ffffff;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow-y: auto;
}

.form-container {
  width: 320px;
  padding: 30px 0;
  margin: 20px 0;
}

.form-header {
  text-align: center;
  margin-bottom: 25px;
}

.form-header h2 {
  color: #333;
  font-size: 24px;
  font-weight: 600;
  margin: 0 0 8px;
}

.form-header p {
  color: #888;
  font-size: 14px;
  margin: 0;
}

.register-form {
  margin-bottom: 20px;
}

/* 修复图标对齐问题 */
.input-icon-wrapper {
  position: relative;
}

.input-icon-wrapper i.input-icon {
  position: absolute;
  left: 15px;
  top: 50%;
  transform: translateY(-50%);
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

.input-icon-wrapper .el-input__inner,
.input-icon-wrapper .el-input input {
  padding-left: 40px !important;
}

.custom-input input {
  height: 40px;
  font-size: 14px;
  border-radius: 8px;
}

.custom-input input:focus {
  border-color: #4caf50;
  box-shadow: 0 0 0 2px rgba(76, 175, 80, 0.2);
}

.custom-select {
  width: 100%;
}

.custom-select .el-input input {
  height: 40px;
  font-size: 14px;
  border-radius: 8px;
}

.custom-select .el-input input:focus {
  border-color: #4caf50;
  box-shadow: 0 0 0 2px rgba(76, 175, 80, 0.2);
}

.option-content {
  display: flex;
  align-items: center;
}

.option-content i {
  margin-right: 8px;
  font-size: 16px;
}

.role-extra-fields {
  padding: 10px 15px;
  background-color: #f9f9f9;
  border-radius: 8px;
  margin-bottom: 20px;
  box-shadow: inset 0 2px 6px rgba(0,0,0,0.05);
  animation: fadeIn 0.3s;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(-10px); }
  to { opacity: 1; transform: translateY(0); }
}

.submit-button {
  width: 100%;
  height: 40px;
  border-radius: 8px;
  font-size: 16px;
  letter-spacing: 2px;
  background: linear-gradient(135deg, #2e7d32, #4caf50);
  border: none;
  box-shadow: 0 4px 12px rgba(76, 175, 80, 0.3);
  transition: all 0.3s;
}

.el-step__head.is-process {
  color: #4caf50;
  border-color: #4caf50;
}

.el-step__head.is-finish {
  color: #4caf50;
  border-color: #4caf50;
}

.el-step__title.is-process {
  font-weight: bold;
  color: #4caf50;
}

.el-step__title.is-finish {
  color: #4caf50;
}

.submit-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(76, 175, 80, 0.4);
}

.form-footer {
  text-align: center;
  color: #888;
  font-size: 14px;
}

.login-link {
  color: #4caf50;
  font-weight: 600;
  text-decoration: none;
}

/* 响应式调整 */
@media screen and (max-width: 768px) {
  .register-container {
    flex-direction: column;
    height: auto;
  }
  
  .left-section {
    display: none;
  }
  
  .right-section {
    width: 100%;
    padding: 30px 0;
  }
}

/* 修改为新的图标列样式 */
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
  height: 16px;
  width: 16px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
}

.input-with-icon .el-input__inner,
.input-with-icon .el-input input {
  padding-left: 12px !important;
}
</style>