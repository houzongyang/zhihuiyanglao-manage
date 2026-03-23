<template>
  <div>
    <!-- 功能说明 -->
    <div class="bg-white rounded-lg shadow-sm p-6 mb-5">
      <div class="flex items-center">
        <i class="el-icon-user text-3xl text-blue-500 mr-3"></i>
        <div>
          <h2 class="text-xl font-bold text-gray-800 mb-1">用户管理</h2>
          <p class="text-gray-600">本模块用于管理系统学生用户，提供用户信息的增加、修改、删除和查询功能</p>
        </div>
      </div>
    </div>

    <!-- 搜索和操作区域 -->
    <div class="bg-white rounded-lg shadow-sm p-6 mb-5">
      <div class="flex flex-wrap items-center justify-between">
        <div class="w-full lg:w-auto mb-4 lg:mb-0">
          <el-form :inline="true" :model="searchForm" ref="searchForm">
            <el-form-item>
              <el-input 
                v-model="searchForm.username" 
                placeholder="请输入用户名" 
                prefix-icon="el-icon-user" 
                clearable
                size="small"
              ></el-input>
            </el-form-item>
            <el-form-item>
              <el-input 
                v-model="searchForm.name" 
                placeholder="请输入姓名" 
                prefix-icon="el-icon-s-custom" 
                clearable
                size="small"
              ></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSearch" icon="el-icon-search" size="small" class="mr-2">查询</el-button>
              <el-button @click="resetSearch" icon="el-icon-refresh" size="small" class="mr-2">重置</el-button>
              <el-button type="success" @click="queryAllUsers" icon="el-icon-menu" size="small">查询全部</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div class="w-full lg:w-auto flex justify-end space-x-3">
          <el-button type="primary" @click="handleAdd" icon="el-icon-plus" size="small">添加用户</el-button>
          <el-button type="danger" @click="handleBatchDelete" :disabled="selection.length === 0" icon="el-icon-delete" size="small">
            批量删除
          </el-button>
        </div>
      </div>
    </div>

    <!-- 用户表格 -->
    <div class="bg-white rounded-lg shadow-sm overflow-hidden mb-5">
      <el-table
        :data="tableData"
        style="width: 100%"
        v-loading="loading"
        @selection-change="handleSelectionChange"
        :border="false"
        stripe
        :cell-style="{padding: '16px 0'}"
        :header-cell-style="{background:'#f8fafc', color:'#475569', fontWeight: '600'}"
      >
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="username" label="用户名" min-width="120"></el-table-column>
        <el-table-column prop="name" label="姓名" min-width="120"></el-table-column>
        <el-table-column prop="age" label="年龄" width="80" align="center">
          <template slot-scope="scope">
            <el-tag size="mini" :type="scope.row.age ? (scope.row.age >= 60 ? 'warning' : 'success') : 'info'">
              {{ scope.row.age || '未设置' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="头像" width="80" align="center">
          <template slot-scope="scope">
            <el-avatar size="small" :src="scope.row.avatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'"></el-avatar>
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="手机号" min-width="130">
          <template slot-scope="scope">
            <div class="flex items-center">
              <i class="el-icon-mobile-phone text-gray-400 mr-1"></i>
              <span>{{ scope.row.phone || '未设置' }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="email" label="邮箱" min-width="180">
          <template slot-scope="scope">
            <div class="flex items-center">
              <i class="el-icon-message text-gray-400 mr-1"></i>
              <span>{{ scope.row.email || '未设置' }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="操作" fixed="right" width="180" align="center">
          <template slot-scope="scope">
            <div class="flex justify-center space-x-2">
              <el-button type="primary" size="mini" icon="el-icon-edit" circle @click="handleEdit(scope.row)" class="transform hover:-translate-y-1 transition-transform"></el-button>
              <el-button type="info" size="mini" icon="el-icon-view" circle @click="handleDetail(scope.row)" class="transform hover:-translate-y-1 transition-transform"></el-button>
              <el-button type="danger" size="mini" icon="el-icon-delete" circle @click="handleDelete(scope.row)" class="transform hover:-translate-y-1 transition-transform"></el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 分页组件 -->
    <div class="bg-white rounded-lg shadow-sm p-4 flex justify-end">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        background
      ></el-pagination>
    </div>

    <!-- 添加/编辑用户对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="750px" :close-on-click-modal="false" custom-class="rounded-lg">
      <el-form :model="form" :rules="rules" ref="form" label-width="80px">
        <el-row :gutter="30">
          <el-col :span="15">
            <!-- 左侧表单 -->
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="用户名" prop="username">
                  <el-input v-model="form.username" :disabled="isEdit" placeholder="请输入用户名" prefix-icon="el-icon-user"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item v-if="!isEdit" label="密码" prop="password">
                  <el-input v-model="form.password" type="password" placeholder="请输入密码" show-password prefix-icon="el-icon-lock"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="姓名" prop="name">
                  <el-input v-model="form.name" placeholder="请输入姓名" prefix-icon="el-icon-s-custom"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="年龄" prop="age">
                  <el-input-number v-model="form.age" :min="0" :max="120" placeholder="请输入年龄" style="width: 100%" controls-position="right"></el-input-number>
                </el-form-item>
              </el-col>
            </el-row>
            
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="手机号" prop="phone">
                  <el-input v-model="form.phone" placeholder="请输入手机号" prefix-icon="el-icon-mobile-phone"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="邮箱" prop="email">
                  <el-input v-model="form.email" placeholder="请输入邮箱" prefix-icon="el-icon-message"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            

          </el-col>
          
          <el-col :span="9">
            <!-- 右侧头像 -->
            <div class="flex flex-col items-center">
              <p class="mb-3 text-gray-700 font-medium">用户头像</p>
              <el-form-item prop="avatar" class="w-full">
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
                    <i class="el-icon-plus text-2xl mb-1"></i>
                    <span class="text-sm">上传头像</span>
                  </div>
                </el-upload>
              </el-form-item>
              <p class="text-xs text-gray-500">支持JPG、PNG、GIF格式，大小不超过2MB</p>
            </div>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="flex justify-end space-x-3">
        <el-button @click="dialogVisible = false" icon="el-icon-close">取 消</el-button>
        <el-button type="primary" @click="saveUser" :loading="submitLoading" icon="el-icon-check">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 用户详情对话框 -->
    <el-dialog title="用户详情" :visible.sync="detailDialogVisible" width="600px" custom-class="rounded-lg">
      <div v-if="currentUser" class="px-5">
        <div class="flex items-center mb-6">
          <el-avatar :size="64" :src="currentUser.avatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" class="mr-4"></el-avatar>
          <div>
            <h3 class="text-xl font-bold text-gray-800 mb-1">{{ currentUser.name || '未设置' }}</h3>
            <p class="text-gray-500">{{ currentUser.username }}</p>
          </div>
        </div>
        
        <el-divider></el-divider>
        
        <div class="space-y-6">
          <div class="bg-gray-50 rounded-lg p-4">
            <h4 class="flex items-center text-gray-700 font-medium mb-3">
              <i class="el-icon-user text-blue-500 mr-2"></i> 基本信息
            </h4>
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div class="flex">
                <span class="text-gray-500 w-20">用户ID:</span>
                <span class="text-gray-800">{{ currentUser.id }}</span>
              </div>
              <div class="flex">
                <span class="text-gray-500 w-20">用户名:</span>
                <span class="text-gray-800">{{ currentUser.username }}</span>
              </div>
              <div class="flex">
                <span class="text-gray-500 w-20">姓名:</span>
                <span class="text-gray-800">{{ currentUser.name || '未设置' }}</span>
              </div>
              <div class="flex">
                <span class="text-gray-500 w-20">年龄:</span>
                <span class="text-gray-800">{{ currentUser.age || '未设置' }}</span>
              </div>
            </div>
          </div>
          
          <div class="bg-gray-50 rounded-lg p-4">
            <h4 class="flex items-center text-gray-700 font-medium mb-3">
              <i class="el-icon-mobile-phone text-green-500 mr-2"></i> 联系方式
            </h4>
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div class="flex">
                <span class="text-gray-500 w-20">手机号:</span>
                <span class="text-gray-800">{{ currentUser.phone || '未设置' }}</span>
              </div>
              <div class="flex">
                <span class="text-gray-500 w-20">邮箱:</span>
                <span class="text-gray-800">{{ currentUser.email || '未设置' }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div slot="footer" class="flex justify-end space-x-3">
        <el-button @click="detailDialogVisible = false" icon="el-icon-close">关 闭</el-button>
        <el-button type="primary" @click="handleEdit(currentUser)" icon="el-icon-edit">编辑信息</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "UserManage",
  data() {
    // 手机号验证规则
    const validatePhone = (rule, value, callback) => {
      if (value && !/^1[3-9]\d{9}$/.test(value)) {
        callback(new Error("请输入正确的手机号格式"));
      } else {
        callback();
      }
    };

    return {
      // 搜索表单
      searchForm: {
        username: "",
        name: "",
      },
      // 表格数据
      tableData: [],
      loading: false,
      total: 0,
      pageNum: 1,
      pageSize: 10,
      
      // 选中项
      selection: [],
      
      // 对话框
      dialogVisible: false,
      dialogTitle: "添加用户",
      isEdit: false,
      submitLoading: false,
      activeTab: "info",  // 表单选项卡默认选中基本信息
      
      // 详情对话框
      detailDialogVisible: false,
      currentUser: null,
      
      // 表单
      form: {
        id: null,
        username: "",
        password: "",
        name: "",
        age: null,
        phone: "",
        email: "",
        avatar: "",
        role: "USER" // 默认为学生用户，保留用于后端
      },
      
      // 表单验证规则
      rules: {
        username: [
          { required: true, message: "请输入用户名", trigger: "blur" },
          { min: 3, max: 20, message: "长度在 3 到 20 个字符", trigger: "blur" }
        ],
        password: [
          { required: true, message: "请输入密码", trigger: "blur" },
          { min: 6, max: 20, message: "长度在 6 到 20 个字符", trigger: "blur" }
        ],
        name: [
          { required: true, message: "请输入姓名", trigger: "blur" }
        ],
        age: [
          { type: 'number', message: '年龄必须为数字', trigger: 'blur' },
          { validator: (rule, value, callback) => {
              if (value !== null && value !== undefined && (value < 0 || value > 120)) {
                callback(new Error('请输入0-120之间的年龄'));
              } else {
                callback();
              }
            }, trigger: 'blur' }
        ],
        phone: [
          { validator: validatePhone, trigger: "blur" }
        ],
       
        avatar: [
          { required: true, message: "请上传头像", trigger: "change" }
        ]
      }
    };
  },
  created() {
    this.fetchData();
  },
  methods: {
    // 获取数据
    fetchData() {
      this.loading = true;
      
      // 构建请求参数
      const params = {
        pageNum: this.pageNum,
        pageSize: this.pageSize
      };
      
      // 只有当搜索表单中有值时才添加到请求参数中
      if (this.searchForm.username) {
        params.username = this.searchForm.username;
      }
      
      if (this.searchForm.name) {
        params.name = this.searchForm.name;
      }
      
      this.$request.get("/user/selectPage", { params })
        .then(res => {
          if (res.code === '200') {
            this.tableData = res.data.list || [];
            this.total = res.data.total || 0;
          } else {
            this.$message.error(res.msg || "获取用户数据失败");
          }
        })
        .catch(() => {
          this.$message.error("获取用户数据异常");
        })
        .finally(() => {
          this.loading = false;
        });
    },
    
    // 查询
    handleSearch() {
      this.pageNum = 1;
      this.fetchData();
    },
    
    // 重置查询
    resetSearch() {
      // 清除搜索表单
      this.$refs.searchForm.resetFields();
      this.pageNum = 1;
      // 重新加载所有数据
      this.fetchData();
      this.$message.success("已重置搜索条件，显示所有用户数据");
    },
    
    // 查询全部用户 - 使用selectPage接口但不传查询条件，保留分页功能
    queryAllUsers() {
      // 清除搜索表单
      this.$refs.searchForm.resetFields();
      this.pageNum = 1;
      this.fetchData();
      this.$message.success("已显示所有用户数据");
    },
    
    // 分页相关
    handleSizeChange(val) {
      this.pageSize = val;
      this.fetchData();
    },
    
    handleCurrentChange(val) {
      this.pageNum = val;
      this.fetchData();
    },
    
    // 表格选中项变化
    handleSelectionChange(selection) {
      this.selection = selection;
    },
    
    // 添加用户
    handleAdd() {
      this.dialogTitle = "添加用户";
      this.isEdit = false;
      this.form = {
        id: null,
        username: "",
        password: "",
        name: "",
        age: null,
        phone: "",
        email: "",
        avatar: "",
        role: "USER" // 默认为学生用户，保留用于后端
      };
      this.dialogVisible = true;
      
      // 清除验证
      this.$nextTick(() => {
        if (this.$refs.form) {
          this.$refs.form.clearValidate();
        }
      });
    },
    
    // 编辑用户
    handleEdit(row) {
      this.dialogTitle = "编辑用户";
      this.isEdit = true;
      this.form = JSON.parse(JSON.stringify(row)); // 深拷贝，防止直接修改表格数据
      this.dialogVisible = true;
      
      // 清除验证
      this.$nextTick(() => {
        if (this.$refs.form) {
          this.$refs.form.clearValidate();
        }
      });
    },
    
    // 查看详情
    handleDetail(row) {
      this.currentUser = row;
      this.detailDialogVisible = true;
    },
    
    // 保存用户（添加或更新）
    saveUser() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.submitLoading = true;
          
          const request = this.isEdit 
            ? this.$request.put("/user/update", this.form)
            : this.$request.post("/user/add", this.form);
            
          request.then(res => {
              if (res.code === '200') {
                this.$message.success(this.isEdit ? "编辑成功" : "添加成功");
                this.dialogVisible = false;
                this.fetchData();
              } else {
                this.$message.error(res.msg || (this.isEdit ? "编辑失败" : "添加失败"));
              }
            })
            .catch(() => {
              this.$message.error(this.isEdit ? "编辑用户请求异常" : "添加用户请求异常");
            })
            .finally(() => {
              this.submitLoading = false;
            });
        }
      });
    },
    
    // 删除用户
    handleDelete(row) {
      this.$confirm("确定删除该用户吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        this.$request.delete(`/user/delete/${row.id}`)
          .then(res => {
            if (res.code === '200') {
              this.$message.success("删除成功");
              this.fetchData();
            } else {
              this.$message.error(res.msg || "删除失败");
            }
          })
          .catch(() => {
            this.$message.error("删除用户请求异常");
          });
      }).catch(() => {});
    },
    
    // 批量删除
    handleBatchDelete() {
      if (this.selection.length === 0) {
        this.$message.warning("请选择要删除的用户");
        return;
      }
      
      this.$confirm(`确定删除选中的 ${this.selection.length} 个用户吗?`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        const ids = this.selection.map(item => item.id);
        
        this.$request.delete("/user/delete/batch", { data: ids })
          .then(res => {
            if (res.code === '200') {
              this.$message.success("批量删除成功");
              this.fetchData();
            } else {
              this.$message.error(res.msg || "批量删除失败");
            }
          })
          .catch(() => {
            this.$message.error("批量删除用户请求异常");
          });
      }).catch(() => {});
    },

    // 获取角色名称
    getRoleName(role) {
      switch (role) {
        case 'ADMIN':
          return '管理员';
        case 'NURSE':
          return '护士';
        case 'USER':
          return '用户';
        default:
          return '未知';
      }
    },

    // 头像上传成功
    handleAvatarSuccess(response) {
      // 根据后台API响应格式处理
      if (response.code === '200') {
        // 根据实际后台返回格式调整
        this.form.avatar = response.data;
        // 如果后台返回的是对象且包含url字段
        if (typeof response.data === 'object' && response.data.url) {
          this.form.avatar = response.data.url;
        }
        this.$message.success('头像上传成功');
      } else {
        this.$message.error(response.msg || '头像上传失败');
      }
    },

    // 获取用户token
    getToken() {
      // 从localStorage中获取token
      const userInfo = JSON.parse(localStorage.getItem('xm-user') || '{}');
      return userInfo.token || '';
    },

    // 头像上传前校验
    beforeAvatarUpload(file) {
      const isJPG = file.type === 'image/jpeg' || file.type === 'image/png' || file.type === 'image/gif';
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isJPG) {
        this.$message.error('上传头像图片只能是 JPG/PNG/GIF 格式!');
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!');
      }
      return isJPG && isLt2M;
    }
  }
};
</script>

<style scoped>
/* ElementUI覆盖样式 */
:deep(.el-table) {
  @apply rounded-lg overflow-hidden border-none;
}

:deep(.el-table td) {
  @apply border-b border-gray-100 text-gray-700;
}

:deep(.el-table--striped .el-table__body tr.el-table__row--striped td) {
  @apply bg-gray-50;
}

:deep(.el-table::before) {
  @apply hidden;
}

:deep(.el-dialog) {
  @apply rounded-lg overflow-hidden;
}

:deep(.el-dialog__header) {
  @apply p-5 bg-gray-50 mb-2 border-b border-gray-100;
}

:deep(.el-dialog__title) {
  @apply font-bold text-gray-800;
}

/* 按钮过渡动画 */
:deep(.el-button) {
  @apply transition-all duration-300;
}

/* 头像上传样式 */
.avatar-uploader :deep(.el-upload) {
  border: 1px dashed #d9d9d9;
  border-radius: 8px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 128px;
  height: 128px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s;
  margin: 0 auto;
}

.avatar-uploader :deep(.el-upload:hover) {
  border-color: #409EFF;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.avatar-upload-btn {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  border: none;
  background: transparent;
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
  display: block;
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