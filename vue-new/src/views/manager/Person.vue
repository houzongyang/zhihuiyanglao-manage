<template>
  <div class="person-container">
    <div class="page-title">个人信息 - {{ roleText }}</div>
    
    <el-card class="info-card">
      <el-tabs v-model="activeTab">
        <el-tab-pane label="基本信息" name="basic">
          <el-form :model="form" :rules="rules" ref="personForm" label-width="100px" class="person-form">
            <el-row>
              <el-col :span="12">
                <el-form-item label="用户名" prop="username">
                  <el-input v-model="form.username" disabled></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="角色" prop="role">
                  <el-input v-model="roleText" disabled></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            
            <el-row>
              <el-col :span="12">
                <el-form-item label="姓名" prop="name">
                  <el-input v-model="form.name"></el-input>
                </el-form-item>
              </el-col>
              
            </el-row>
            
            <el-row>
              <el-col :span="12">
                <el-form-item label="手机号" prop="phone">
                  <el-input v-model="form.phone"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="邮箱" prop="email">
                  <el-input v-model="form.email"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            
            <el-row>
              <el-col :span="24">
                <el-form-item label="头像" prop="avatar">
                  <el-upload
                    class="avatar-uploader"
                    :action="$baseUrl + '/files/upload'"
                    :headers="{ token: getToken() }"
                    :show-file-list="false"
                    :on-success="handleAvatarSuccess"
                    :before-upload="beforeAvatarUpload">
                    <div v-if="form.avatar" class="avatar-preview">
                      <img :src="form.avatar" class="avatar">
                      <div class="avatar-hover-mask">
                        <i class="el-icon-edit text-xl mb-1"></i>
                        <span class="text-sm">更换头像</span>
                      </div>
                    </div>
                    <div v-else class="avatar-upload-btn">
                      <i class="el-icon-plus"></i>
                      <span>上传头像</span>
                    </div>
                  </el-upload>
                </el-form-item>
              </el-col>
            </el-row>
            
            <el-form-item>
              <el-button type="primary" @click="saveInfo" :loading="loading">保存信息</el-button>
              <el-button @click="resetForm">重置</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        
        <el-tab-pane label="修改密码" name="password">
          <el-form :model="passwordForm" :rules="passwordRules" ref="passwordForm" label-width="100px" class="password-form">
            <el-form-item label="原密码" prop="oldPassword">
              <el-input v-model="passwordForm.oldPassword" type="password" show-password></el-input>
            </el-form-item>
            
            <el-form-item label="新密码" prop="newPassword">
              <el-input v-model="passwordForm.newPassword" type="password" show-password></el-input>
            </el-form-item>
            
            <el-form-item label="确认密码" prop="confirmPassword">
              <el-input v-model="passwordForm.confirmPassword" type="password" show-password></el-input>
            </el-form-item>
            
            <el-form-item>
              <el-button type="primary" @click="updatePassword" :loading="passwordLoading">更新密码</el-button>
              <el-button @click="resetPasswordForm">重置</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script>
export default {
  name: "ManagerPerson",
  data() {
    // 确认密码的验证规则
    const validateConfirmPassword = (rule, value, callback) => {
      if (value !== this.passwordForm.newPassword) {
        callback(new Error('两次输入的密码不一致!'));
      } else {
        callback();
      }
    };
    
    return {
      activeTab: 'basic',
      loading: false,
      passwordLoading: false,
      userId: null, // 添加用户ID字段
      form: {
        username: '',
        role: '',
        name: '',
        gender: '',
        phone: '',
        email: '',
        avatar: ''
      },
      rules: {
        name: [
          { required: true, message: '请输入姓名', trigger: 'blur' }
        ],
        phone: [
          { required: true, message: '请输入手机号', trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
        ],
        email: [
          { required: true, message: '请输入邮箱', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
        ]
      },
      passwordForm: {
        oldPassword: '',
        newPassword: '',
        confirmPassword: ''
      },
      passwordRules: {
        oldPassword: [
          { required: true, message: '请输入原密码', trigger: 'blur' }
        ],
        newPassword: [
          { required: true, message: '请输入新密码', trigger: 'blur' },
          { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: '请确认密码', trigger: 'blur' },
          { validator: validateConfirmPassword, trigger: 'blur' }
        ]
      }
    };
  },
  created() {
    this.getUserInfo();
  },
  computed: {
    roleText() {
      const roleMap = {
        'ADMIN': '管理员',
        'USER': '用户',
        'NURSE': '护士'
      };
      return roleMap[this.form.role] || this.form.role;
    }
  },
  methods: {
    // 获取用户信息
    getUserInfo() {
      const userInfo = JSON.parse(localStorage.getItem('xm-user') || '{}');
      if (!userInfo.token) {
        this.$message.error('用户未登录');
        this.$router.push('/login');
        return;
      }
      
      // 从本地存储获取用户ID和角色
      this.userId = userInfo.id;
      const userRole = userInfo.role;
      
      if (!this.userId) {
        this.$message.error('无法获取用户ID');
        return;
      }
      
      // 根据角色选择不同的API接口
      let apiUrl = '';
      if (userRole === 'ADMIN') {
        apiUrl = `/admin/selectById/${this.userId}`;
      } else if (userRole === 'NURSE') {
        apiUrl = `/nurse/selectById/${this.userId}`;
      } else {
        this.$message.error('不支持的用户角色');
        return;
      }
      
      // 调用对应角色的接口获取用户详细信息
      this.$request.get(apiUrl).then(res => {
        // 处理不同的成功状态码格式，兼容code为'0'或'200'的情况
        if ((res.code === '0' || res.code === '200') && res.data) {
          this.form = {...res.data};
        } else {
          // 如果没有从后端获取到数据，则使用本地存储的信息
          this.form = {
            username: userInfo.username || '',
            role: userInfo.role || '',
            name: userInfo.name || '',
            gender: userInfo.gender || '',
            phone: userInfo.phone || '',
            email: userInfo.email || '',
            avatar: userInfo.avatar || ''
          };
          this.$message.warning('无法获取最新用户信息，显示本地缓存数据');
        }
      }).catch(err => {
        console.error('获取用户信息失败:', err);
        this.$message.error('获取用户信息失败');
        
        // 使用本地存储的信息
        this.form = {
          username: userInfo.username || '',
          role: userInfo.role || '',
          name: userInfo.name || '',
          gender: userInfo.gender || '',
          phone: userInfo.phone || '',
          email: userInfo.email || '',
          avatar: userInfo.avatar || ''
        };
      });
    },
    
    // 保存基本信息
    saveInfo() {
      this.$refs.personForm.validate(valid => {
        if (valid) {
          this.loading = true;
          
          // 根据角色选择不同的更新接口
          let apiUrl = '';
          if (this.form.role === 'ADMIN') {
            apiUrl = '/admin/update';
          } else if (this.form.role === 'NURSE') {
            apiUrl = '/nurse/update';
          } else {
            this.$message.error('不支持的用户角色');
            this.loading = false;
            return;
          }
          
          this.$request.put(apiUrl, this.form).then(res => {
            if (res.code === '0' || res.code === '200') {
              this.$message.success('保存成功');
              
              // 更新本地存储的用户信息
              const userInfo = JSON.parse(localStorage.getItem('xm-user') || '{}');
              const newUserInfo = {...userInfo, ...this.form};
              localStorage.setItem('xm-user', JSON.stringify(newUserInfo));
            } else {
              this.$message.error(res.msg || '保存失败');
            }
          }).catch(() => {
            this.$message.error('保存请求异常');
          }).finally(() => {
            this.loading = false;
          });
        }
      });
    },
    
    // 修改密码
    updatePassword() {
      this.$refs.passwordForm.validate(valid => {
        if (valid) {
          this.passwordLoading = true;
          
          // 构造请求参数，使用统一的密码更新接口
          const passwordData = {
            username: this.form.username,
            password: this.passwordForm.oldPassword,  // 原密码
            newPassword: this.passwordForm.newPassword,  // 新密码
            role: this.form.role,  // 用户角色
            id: this.userId
          };
          
          this.$request.put('/updatePassword', passwordData).then(res => {
            if (res.code === '0' || res.code === '200') {
              this.$message.success('密码修改成功，请重新登录');
              localStorage.removeItem('xm-user');
              this.$router.push('/login');
            } else {
              this.$message.error(res.msg || '密码修改失败');
            }
          }).catch((err) => {
            console.error('密码修改请求异常:', err);
            this.$message.error('密码修改请求异常');
          }).finally(() => {
            this.passwordLoading = false;
          });
        }
      });
    },
    
    // 重置表单
    resetForm() {
      this.$refs.personForm.resetFields();
      this.getUserInfo();
    },
    
    // 重置密码表单
    resetPasswordForm() {
      this.$refs.passwordForm.resetFields();
    },
    
    // 头像上传前检查
    beforeAvatarUpload(file) {
      const isJPG = file.type === 'image/jpeg' || file.type === 'image/png';
      const isLt2M = file.size / 1024 / 1024 < 2;
      
      if (!isJPG) {
        this.$message.error('上传头像图片只能是 JPG 或 PNG 格式!');
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!');
      }
      
      return isJPG && isLt2M;
    },
    
    // 头像上传成功处理
    handleAvatarSuccess(response) {
      if ((response.code === '0' || response.code === '200') && response.data) {
        this.form.avatar = response.data;
        this.$message.success('头像上传成功');
      } else {
        this.$message.error(response.msg || '头像上传失败');
      }
    },
    
    // 获取用户token
    getToken() {
      const userInfo = JSON.parse(localStorage.getItem('xm-user') || '{}');
      return userInfo.token || '';
    }
  }
};
</script>

<style scoped>
.person-container {
  padding: 10px;
}

.page-title {
  font-size: 22px;
  font-weight: bold;
  margin-bottom: 20px;
  color: #303133;
}

.info-card {
  margin-bottom: 20px;
}

.person-form, .password-form {
  max-width: 800px;
}

.avatar-uploader {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 120px;
  height: 120px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.avatar-uploader:hover {
  border-color: #409EFF;
}

.avatar-upload-btn {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  color: #8c939d;
}

.avatar-preview {
  width: 100%;
  height: 100%;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
}

.avatar {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.avatar-hover-mask {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  color: #fff;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s;
}

.avatar-preview:hover .avatar-hover-mask {
  opacity: 1;
}
</style> 