<template>
  <div class="person-container">
    <div class="page-title">个人信息 - 教师</div>
    
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card class="user-card">
          <div class="avatar-container">
            <img :src="form.avatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" class="user-avatar">
            <h3 class="user-name">{{ form.name || '教师' }}</h3>
            <p class="user-role">{{ roleText }}</p>
            <el-divider></el-divider>
            <div class="user-stats">
              <div class="stat-item">
                <div class="stat-value">{{ reviewCount }}</div>
                <div class="stat-label">已审核</div>
              </div>
              <div class="stat-item">
                <div class="stat-value">{{ pendingCount }}</div>
                <div class="stat-label">待审核</div>
              </div>
              <div class="stat-item">
                <div class="stat-value">{{ textbookCount }}</div>
                <div class="stat-label">教材数</div>
              </div>
            </div>
            <el-divider></el-divider>
            <div class="action-buttons">
              <el-button type="primary" @click="$router.push('/teacher/textbook-review')">审核申报</el-button>
              <el-button @click="$router.push('/teacher/textbook')">管理教材</el-button>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="16">
        <el-card class="info-card">
          <el-tabs v-model="activeTab">
            <el-tab-pane label="基本信息" name="basic">
              <el-form :model="form" :rules="rules" ref="personForm" label-width="100px" class="person-form">
                <el-row>
                  <el-col :span="12">
                    <el-form-item label="工号" prop="username">
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
                  <el-col :span="12">
                    <el-form-item label="性别" prop="gender">
                      <el-select v-model="form.gender" placeholder="请选择性别" style="width: 100%">
                        <el-option label="男" value="男"></el-option>
                        <el-option label="女" value="女"></el-option>
                      </el-select>
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
                  <el-col :span="12">
                    <el-form-item label="院系" prop="department">
                      <el-input v-model="form.department"></el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="职称" prop="title">
                      <el-select v-model="form.title" placeholder="请选择职称" style="width: 100%">
                        <el-option label="教授" value="教授"></el-option>
                        <el-option label="副教授" value="副教授"></el-option>
                        <el-option label="讲师" value="讲师"></el-option>
                        <el-option label="助教" value="助教"></el-option>
                      </el-select>
                    </el-form-item>
                  </el-col>
                </el-row>

                <el-row>
                  <el-col :span="24">
                    <el-form-item label="专业领域" prop="expertise">
                      <el-input v-model="form.expertise"></el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
                
                <el-row>
                  <el-col :span="24">
                    <el-form-item label="头像" prop="avatar">
                      <el-upload
                        class="avatar-uploader"
                        action="#"
                        :show-file-list="false"
                        :before-upload="beforeAvatarUpload"
                        :http-request="handleAvatarUpload">
                        <img v-if="form.avatar" :src="form.avatar" class="avatar">
                        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
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
        
        <el-card class="recent-reviews">
          <div slot="header" class="review-header">
            <span>最近审核</span>
            <el-button type="text" @click="$router.push('/teacher/review-history')">查看全部</el-button>
          </div>
          <el-table :data="recentReviews" style="width: 100%">
            <el-table-column prop="name" label="教材名称" width="180"></el-table-column>
            <el-table-column prop="applicant" label="申请人" width="120"></el-table-column>
            <el-table-column prop="reviewTime" label="审核时间" width="180"></el-table-column>
            <el-table-column prop="status" label="结果">
              <template slot-scope="scope">
                <el-tag :type="scope.row.status ? 'success' : 'danger'">{{ scope.row.status ? '通过' : '未通过' }}</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: "TeacherPerson",
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
      reviewCount: 86,
      pendingCount: 12,
      textbookCount: 48,
      form: {
        username: '',
        role: '',
        name: '',
        gender: '',
        phone: '',
        email: '',
        department: '',
        title: '',
        expertise: '',
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
        ],
        department: [
          { required: true, message: '请输入所在院系', trigger: 'blur' }
        ],
        title: [
          { required: true, message: '请选择职称', trigger: 'change' }
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
      },
      recentReviews: [
        { id: 1, name: '高等数学（第7版）', applicant: '张三', reviewTime: '2023-07-20', status: 1 },
        { id: 2, name: '大学物理学', applicant: '李四', reviewTime: '2023-07-18', status: 0 },
        { id: 3, name: '计算机网络', applicant: '王五', reviewTime: '2023-07-15', status: 1 },
        { id: 4, name: '数据结构（C语言版）', applicant: '赵六', reviewTime: '2023-07-10', status: 1 }
      ]
    };
  },
  created() {
    this.getUserInfo();
  },
  computed: {
    roleText() {
      const roleMap = {
        'ADMIN': '管理员',
        'USER': '学生',
        'TEACHER': '教师'
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
      
      this.$request.get('/teacher/info').then(res => {
        if (res.code === '0' && res.data) {
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
            department: userInfo.department || '',
            title: userInfo.title || '',
            expertise: userInfo.expertise || '',
            avatar: userInfo.avatar || ''
          };
        }
      }).catch(() => {
        this.$message.error('获取用户信息失败');
      });
    },
    
    // 保存基本信息
    saveInfo() {
      this.$refs.personForm.validate(valid => {
        if (valid) {
          this.loading = true;
          
          this.$request.put('/teacher/update', this.form).then(res => {
            if (res.code === '0') {
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
          
          this.$request.put('/teacher/updatePassword', this.passwordForm).then(res => {
            if (res.code === '0') {
              this.$message.success('密码修改成功，请重新登录');
              localStorage.removeItem('xm-user');
              this.$router.push('/login');
            } else {
              this.$message.error(res.msg || '密码修改失败');
            }
          }).catch(() => {
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
    
    // 自定义头像上传
    handleAvatarUpload(options) {
      const formData = new FormData();
      formData.append('file', options.file);
      
      this.$request.post('/file/upload', formData).then(res => {
        if (res.code === '0' && res.data) {
          this.form.avatar = res.data;
          this.$message.success('头像上传成功');
        } else {
          this.$message.error(res.msg || '头像上传失败');
        }
      }).catch(() => {
        this.$message.error('头像上传请求异常');
      });
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

.user-card {
  margin-bottom: 20px;
}

.avatar-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px 0;
}

.user-avatar {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  object-fit: cover;
  margin-bottom: 15px;
}

.user-name {
  font-size: 18px;
  font-weight: bold;
  margin: 0 0 5px;
}

.user-role {
  color: #909399;
  margin: 0;
}

.user-stats {
  display: flex;
  width: 100%;
  justify-content: space-around;
  padding: 10px 0;
}

.stat-item {
  text-align: center;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #409EFF;
}

.stat-label {
  font-size: 14px;
  color: #909399;
}

.action-buttons {
  display: flex;
  flex-direction: column;
  width: 100%;
  gap: 10px;
}

.info-card, .recent-reviews {
  margin-bottom: 20px;
}

.review-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
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

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 100%;
  height: 100%;
  line-height: 120px;
  text-align: center;
}

.avatar {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
</style> 