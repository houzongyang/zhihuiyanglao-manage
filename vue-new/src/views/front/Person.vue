<template>
  <div class="p-0">
    <!-- 页面标题 -->
    <div class="flex justify-between items-center mb-6">
      <div class="flex items-center text-2xl font-bold text-gray-800">
        <i class="el-icon-user text-blue-500 mr-3"></i>
        <span>个人中心</span>
      </div>

    </div>
    
    <el-row :gutter="24">
      <!-- 左侧：个人信息卡片 -->
      <el-col :lg="8" :md="24" :sm="24">
        <div class="bg-white rounded-xl shadow-sm overflow-hidden mb-6">
          <div class="py-8 text-center bg-gradient-to-r from-blue-50 to-indigo-50 relative">
            <div class="flex justify-center mb-4">
              <div class="relative w-30 h-30">
                <img :src="form.avatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" 
                     class="w-28 h-28 rounded-full object-cover shadow-md border-2 border-white">
                <div class="absolute bottom-0 left-0 right-0 bg-black bg-opacity-50 h-10 flex items-center justify-center cursor-pointer opacity-0 hover:opacity-100 transition-opacity text-white"
                     @click="triggerUpload">
                  <i class="el-icon-camera-solid text-lg"></i>
                </div>
                <input type="file" ref="avatarInput" @change="uploadAvatar" class="hidden">
              </div>
            </div>
            <h2 class="text-xl font-bold text-gray-800 mb-2">{{ form.name || '同学' }}</h2>
            <div class="inline-block px-3 py-1 bg-blue-100 text-blue-600 rounded-full text-sm mb-2">{{ roleText }}</div>
            <div class="text-sm text-gray-500">学号：{{ form.username }}</div>
          </div>
          

          <div class="p-5 space-y-4">
            <div class="flex items-center">
              <div class="w-10 h-10 rounded-full bg-blue-100 text-blue-500 flex items-center justify-center mr-4">
                <i class="el-icon-user"></i>
              </div>
              <div>
                <div class="text-sm text-gray-500 mb-1">性别</div>
                <div class="text-gray-800">{{ form.gender || '暂无' }}</div>
              </div>
            </div>
            
            <div class="flex items-center">
              <div class="w-10 h-10 rounded-full bg-blue-100 text-blue-500 flex items-center justify-center mr-4">
                <i class="el-icon-date"></i>
              </div>
              <div>
                <div class="text-sm text-gray-500 mb-1">年龄</div>
                <div class="text-gray-800">{{ form.age || '暂无' }}</div>
              </div>
            </div>
            
            <div class="flex items-center">
              <div class="w-10 h-10 rounded-full bg-blue-100 text-blue-500 flex items-center justify-center mr-4">
                <i class="el-icon-mobile-phone"></i>
              </div>
              <div>
                <div class="text-sm text-gray-500 mb-1">联系电话</div>
                <div class="text-gray-800">{{ form.phone || '暂无' }}</div>
              </div>
            </div>
            
            <div class="flex items-center">
              <div class="w-10 h-10 rounded-full bg-blue-100 text-blue-500 flex items-center justify-center mr-4">
                <i class="el-icon-message"></i>
              </div>
              <div>
                <div class="text-sm text-gray-500 mb-1">电子邮箱</div>
                <div class="text-gray-800">{{ form.email || '暂无' }}</div>
              </div>
            </div>
            
            <div class="flex items-center">
              <div class="w-10 h-10 rounded-full bg-blue-100 text-blue-500 flex items-center justify-center mr-4">
                <i class="el-icon-location"></i>
              </div>
              <div>
                <div class="text-sm text-gray-500 mb-1">住址</div>
                <div class="text-gray-800">{{ form.address || '暂无' }}</div>
              </div>
            </div>
          </div>
        </div>
      </el-col>
      
      <!-- 右侧：详细信息和最近申报 -->
      <el-col :lg="16" :md="24" :sm="24">
        <!-- 编辑信息卡片 -->
        <div class="bg-white rounded-xl shadow-sm overflow-hidden mb-6">
          <el-tabs v-model="activeTab" type="border-card" class="custom-tabs">
            <el-tab-pane label="基本信息" name="basic">
              <div class="px-5 py-6 border-b border-gray-100">
                <div class="text-lg font-semibold text-gray-800 mb-2">个人资料</div>
                <p class="text-sm text-gray-500">您可以在此修改个人基本信息</p>
              </div>
              
              <el-form :model="form" :rules="rules" ref="personForm" label-width="100px" class="p-5">
                <el-row :gutter="20">
                  <el-col :md="12" :sm="24">
                    <el-form-item label="姓名" prop="name">
                      <el-input v-model="form.name" placeholder="请输入姓名" prefix-icon="el-icon-user"></el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :md="12" :sm="24">
                    <el-form-item label="性别" prop="gender">
                      <el-radio-group v-model="form.gender">
                        <el-radio label="男">男</el-radio>
                        <el-radio label="女">女</el-radio>
                      </el-radio-group>
                    </el-form-item>
                  </el-col>
                </el-row>
                
                <el-row :gutter="20">
                  <el-col :md="12" :sm="24">
                    <el-form-item label="年龄" prop="age">
                      <el-input-number v-model="form.age" :min="0" :max="120" placeholder="请输入年龄" style="width: 100%"></el-input-number>
                    </el-form-item>
                  </el-col>
                  <el-col :md="12" :sm="24">
                    <el-form-item label="手机号" prop="phone">
                      <el-input v-model="form.phone" placeholder="请输入手机号" prefix-icon="el-icon-mobile-phone"></el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
                
                <el-row :gutter="20">
                  <el-col :md="12" :sm="24">
                    <el-form-item label="邮箱" prop="email">
                      <el-input v-model="form.email" placeholder="请输入邮箱" prefix-icon="el-icon-message"></el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :md="12" :sm="24">
                    <el-form-item label="住址" prop="address">
                      <el-input v-model="form.address" placeholder="请输入住址" prefix-icon="el-icon-location"></el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
                
                <el-form-item>
                  <el-button type="primary" @click="saveInfo" :loading="loading" 
                             round icon="el-icon-check" 
                             class="shadow-sm hover:shadow-md transition-shadow">保存更改</el-button>
                  <el-button @click="resetForm" round icon="el-icon-refresh-left">重置信息</el-button>
                </el-form-item>
              </el-form>
            </el-tab-pane>
            
            <el-tab-pane label="修改密码" name="password">
              <div class="px-5 py-6 border-b border-gray-100">
                <div class="text-lg font-semibold text-gray-800 mb-2">账号安全</div>
                <p class="text-sm text-gray-500">定期修改密码可以保护您的账号安全</p>
              </div>
              
              <el-form :model="passwordForm" :rules="passwordRules" ref="passwordForm" label-width="100px" class="p-5">
                <el-form-item label="原密码" prop="oldPassword">
                  <el-input v-model="passwordForm.oldPassword" type="password" show-password prefix-icon="el-icon-lock"></el-input>
                </el-form-item>
                
                <el-form-item label="新密码" prop="newPassword">
                  <el-input v-model="passwordForm.newPassword" type="password" show-password prefix-icon="el-icon-key"></el-input>
                  <div class="text-xs text-gray-500 mt-1">密码长度应为6-20个字符</div>
                </el-form-item>
                
                <el-form-item label="确认密码" prop="confirmPassword">
                  <el-input v-model="passwordForm.confirmPassword" type="password" show-password prefix-icon="el-icon-key"></el-input>
                </el-form-item>
                
                <el-form-item>
                  <el-button type="primary" @click="updatePassword" :loading="passwordLoading" 
                             round icon="el-icon-check"
                             class="shadow-sm hover:shadow-md transition-shadow">更新密码</el-button>
                  <el-button @click="resetPasswordForm" round icon="el-icon-refresh-left">重置</el-button>
                </el-form-item>
              </el-form>
            </el-tab-pane>
          </el-tabs>
        </div>
        
        <!-- 最近申报卡片 -->

      </el-col>
    </el-row>
    
    <!-- 申报详情弹窗 -->
    <el-dialog
      title="申报详情"
      :visible.sync="detailDialogVisible"
      width="650px"
      custom-class="rounded-lg overflow-hidden"
      center
    >
      <div v-if="currentApplication" class="application-detail">
        <div class="flex items-center mb-6">
          <div class="w-14 h-14 rounded-lg flex items-center justify-center text-white text-2xl mr-4"
               :class="{
                 'bg-green-500': currentApplication.status === 1,
                 'bg-red-500': currentApplication.status === 2,
                 'bg-gray-500': currentApplication.status === 0
               }">
            <i class="el-icon-notebook-2"></i>
          </div>
          <div>
            <h3 class="text-lg font-bold text-gray-800 mb-1">{{ currentApplication.textbookName }}</h3>
            <div>
              <el-tag :type="getStatusType(currentApplication.status)" effect="light">
                {{ getStatusText(currentApplication.status) }}
              </el-tag>
            </div>
          </div>
        </div>
        
        <!-- 教材信息卡片 -->



        


      </div>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="detailDialogVisible = false" round>关闭</el-button>
        <el-button 
          type="primary" 
          @click="handleApplicationAction()" 
          v-if="currentApplication && currentApplication.status === 2" 
          round
        >
          重新申报
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "FrontPerson",
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
      detailDialogVisible: false,
      currentApplication: null,
      applicationCount: 0,
      approvedCount: 0,
      rejectedCount: 0,
      form: {
        id: null,
        username: '',
        role: '',
        name: '',
        gender: '',
        age: null,
        phone: '',
        email: '',
        avatar: '',
        address: ''
      },
      rules: {
        name: [
          { required: true, message: '请输入姓名', trigger: 'blur' }
        ],
        gender: [
          { required: true, message: '请选择性别', trigger: 'change' }
        ],
        age: [
          { required: true, message: '请输入年龄', trigger: 'blur' },
          { type: 'number', message: '年龄必须为数字', trigger: 'blur' },
          { validator: (rule, value, callback) => {
              if (value && (value < 0 || value > 120)) {
                callback(new Error('请输入0-120之间的年龄'));
              } else {
                callback();
              }
            }, trigger: 'blur' }
        ],
        phone: [
          { required: true, message: '请输入手机号', trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
        ],
        email: [
          { required: true, message: '请输入邮箱', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
        ],
        address: [
          { required: true, message: '请输入住址', trigger: 'blur' }
        ]
      },
      passwordForm: {
        oldPassword: '',
        newPassword: '',
        confirmPassword: '',
        userId: null
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
      recentApplications: []
    };
  },
  created() {
    this.getUserInfo();
  },
  computed: {
    roleText() {
      const roleMap = {
        'ADMIN': '管理员',
        'USER': '老人',
        'TEACHER': '教师'
      };
      return roleMap[this.form.role] || this.form.role || '';
    }
  },
  methods: {
    // 触发头像上传
    triggerUpload() {
      this.$refs.avatarInput.click();
    },
    
    // 上传头像
    uploadAvatar(e) {
      const file = e.target.files[0];
      if (!file) return;
      
      const isJPG = file.type === 'image/jpeg' || file.type === 'image/png';
      const isLt2M = file.size / 1024 / 1024 < 2;
      
      if (!isJPG) {
        this.$message.error('上传头像图片只能是 JPG 或 PNG 格式!');
        return;
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!');
        return;
      }
      
      const formData = new FormData();
      formData.append('file', file);
      
      this.$request.post('/file/upload', formData).then(res => {
        if (res.code === '200' && res.data) {
          this.form.avatar = res.data;
          this.$message.success('头像上传成功');
        } else {
          this.$message.error(res.msg || '头像上传失败');
        }
      }).catch(() => {
        this.$message.error('头像上传请求异常');
      });
    },
    
    // 获取用户信息
    getUserInfo() {
      const userInfo = JSON.parse(localStorage.getItem('xm-user') || '{}');
      if (!userInfo.token || !userInfo.id) {
        this.$message.error('用户未登录');
        this.$router.push('/login');
        return;
      }

      // 设置用户ID
      this.form.id = userInfo.id;
      this.passwordForm.userId = userInfo.id;
      
      // 从后端获取用户详情
      this.$request.get('/user/selectById/' + userInfo.id).then(res => {
        if (res.code === '200' && res.data) {
          // 更新用户信息
          this.form = {
            ...this.form,
            ...res.data,
            role: res.data.role || userInfo.role
          };
          
          // 获取用户申报记录
        } else {
          // 如果没有从后端获取到数据，则使用本地存储的信息
          this.form = {
            ...this.form,
            username: userInfo.username || '',
            role: userInfo.role || '',
            name: userInfo.name || '',
            gender: userInfo.gender || '',
            age: userInfo.age || null,
            phone: userInfo.phone || '',
            email: userInfo.email || '',
            avatar: userInfo.avatar || '',
            address: userInfo.address || ''
          };
          
          // 获取用户申报记录
          this.getUserApplications(userInfo.id);
        }
      }).catch(error => {
        console.error('获取用户信息失败:', error);
        this.$message.error('获取用户信息失败');
        
        // 使用本地信息
        this.form = {
          ...this.form,
          username: userInfo.username || '',
            role: userInfo.role || '',
            name: userInfo.name || '',
            gender: userInfo.gender || '',
            age: userInfo.age || null,
            phone: userInfo.phone || '',
            email: userInfo.email || '',
            avatar: userInfo.avatar || '',
            address: userInfo.address || ''
        };
        

      });
    },
    
    // 获取用户申报记录

    

    // 保存基本信息
    saveInfo() {
      this.$refs.personForm.validate(valid => {
        if (valid) {
          this.loading = true;
          
          this.$request.put('/user/update', this.form).then(res => {
            if (res.code === '200') {
              this.$message.success('保存成功');
              
              // 更新本地存储的用户信息
              const userInfo = JSON.parse(localStorage.getItem('xm-user') || '{}');
              const newUserInfo = {...userInfo, ...this.form};
              localStorage.setItem('xm-user', JSON.stringify(newUserInfo));
            } else {
              this.$message.error(res.msg || '保存失败');
            }
          }).catch(error => {
            console.error('保存用户信息失败:', error);
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
          
          const params = {
            username: this.form.username,
            password: this.passwordForm.oldPassword,
            newPassword: this.passwordForm.newPassword,
            role: this.form.role
          };
          
          this.$request.put('/updatePassword', params).then(res => {
            if (res.code === '200') {
              this.$message.success('密码修改成功，请重新登录');
              localStorage.removeItem('xm-user');
              this.$router.push('/login');
            } else {
              this.$message.error(res.msg || '密码修改失败');
            }
          }).catch(error => {
            console.error('修改密码失败:', error);
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
    



    
    // 格式化日期
    formatDate(dateStr) {
      if (!dateStr) return '未知时间';
      const date = new Date(dateStr);
      return date.getFullYear() + '-' + 
             String(date.getMonth() + 1).padStart(2, '0') + '-' + 
             String(date.getDate()).padStart(2, '0');
    }
  }
};
</script>

<style scoped>
/* 使用Tailwind CSS，保留Element UI必要的样式覆盖 */
.custom-tabs :deep(.el-tabs__nav-wrap) {
  padding: 0 20px;
}

/* Element UI元素的自定义样式 */
:deep(.el-form-item__label) {
  @apply text-gray-600;
}

:deep(.el-input__inner),
:deep(.el-select .el-input__inner) {
  @apply border border-gray-300 rounded-md shadow-sm;
}

:deep(.el-input__inner:focus),
:deep(.el-select .el-input__inner:focus) {
  @apply border-blue-400 ring-1 ring-blue-300;
}

:deep(.el-button--primary) {
  @apply bg-blue-500 border-blue-500;
}

:deep(.el-button--primary:hover) {
  @apply bg-blue-600 border-blue-600;
}

/* 隐藏上传输入框 */
.hidden {
  display: none !important;
}
</style>