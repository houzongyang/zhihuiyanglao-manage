<template>
  <div>
    <!-- 功能说明 -->
    <div class="bg-white rounded-lg shadow-sm p-6 mb-5">
      <div class="flex items-center">
        <i class="el-icon-collection-tag text-3xl text-blue-500 mr-3"></i>
        <div>
          <h2 class="text-xl font-bold text-gray-800 mb-1">活动分类管理</h2>
          <p class="text-gray-600">本模块用于管理系统活动分类，提供分类信息的增加、修改、删除和查询功能</p>
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
                v-model="searchForm.name" 
                placeholder="请输入分类名称" 
                prefix-icon="el-icon-collection-tag" 
                clearable
                size="small"
              ></el-input>
            </el-form-item>
            <el-form-item>
              <el-select 
                v-model="searchForm.status" 
                placeholder="状态" 
                clearable
                size="small"
              >
                <el-option label="启用" value="ACTIVE"></el-option>
                <el-option label="禁用" value="INACTIVE"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSearch" icon="el-icon-search" size="small" class="mr-2">查询</el-button>
              <el-button @click="resetSearch" icon="el-icon-refresh" size="small" class="mr-2">重置</el-button>
              <el-button type="success" @click="queryAllCategories" icon="el-icon-menu" size="small">查询全部</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div class="w-full lg:w-auto flex justify-end space-x-3">
          <el-button type="primary" @click="handleAdd" icon="el-icon-plus" size="small">添加分类</el-button>
          <el-button type="danger" @click="handleBatchDelete" :disabled="selection.length === 0" icon="el-icon-delete" size="small">
            批量删除
          </el-button>
        </div>
      </div>
    </div>

    <!-- 分类表格 -->
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
        <el-table-column prop="name" label="分类名称" min-width="120"></el-table-column>
        <el-table-column prop="description" label="分类描述" min-width="180">
          <template slot-scope="scope">
            <el-tooltip :content="scope.row.description || '无'" placement="top" :disabled="!scope.row.description">
              <div class="truncate max-w-xs">{{ scope.row.description || '无' }}</div>
            </el-tooltip>
          </template>
        </el-table-column>
        <!-- <el-table-column label="图标" width="80" align="center">
          <template slot-scope="scope">
            <i :class="scope.row.icon || 'el-icon-folder'" :style="{color: scope.row.color || '#909399'}"></i>
          </template>
        </el-table-column> -->
        <el-table-column prop="color" label="颜色" width="100" align="center">
          <template slot-scope="scope">
            <div class="color-block" :style="{backgroundColor: scope.row.color || '#909399'}"></div>
          </template>
        </el-table-column>
        <!-- <el-table-column prop="sort_order" label="排序" width="80" align="center"></el-table-column> -->
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 'ACTIVE' ? 'success' : 'info'" size="small">
              {{ scope.row.status === 'ACTIVE' ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" min-width="160">
          <template slot-scope="scope">
            {{ scope.row.createTime || '-' }}
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

    <!-- 添加/编辑分类对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="650px" :close-on-click-modal="false" custom-class="rounded-lg">
      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="分类名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入分类名称"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="分类描述" prop="description">
              <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入分类描述"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <!-- <el-form-item label="图标" prop="icon">
              <el-input v-model="form.icon" placeholder="请输入图标类名">
                <template slot="append">
                  <i :class="form.icon || 'el-icon-folder'" :style="{color: form.color || '#909399'}"></i>
                </template>
              </el-input>
            </el-form-item> -->
          </el-col>
          <el-col :span="12">
            <el-form-item label="颜色" prop="color">
              <el-color-picker v-model="form.color" show-alpha></el-color-picker>
              <span class="ml-2 text-gray-500 text-sm">{{ form.color }}</span>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <!-- <el-col :span="12"> -->
            <!-- <el-form-item label="排序顺序" prop="sort_order">
              <el-input-number v-model="form.sort_order" :min="0" :max="999" controls-position="right"></el-input-number>
            </el-form-item> -->
          <!-- </el-col> -->
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-select v-model="form.status" placeholder="请选择状态">
                <el-option label="启用" value="ACTIVE"></el-option>
                <el-option label="禁用" value="INACTIVE"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="flex justify-end space-x-3">
        <el-button @click="dialogVisible = false" icon="el-icon-close">取 消</el-button>
        <el-button type="primary" @click="saveCategory" :loading="submitLoading" icon="el-icon-check">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 分类详情对话框 -->
    <el-dialog title="分类详情" :visible.sync="detailDialogVisible" width="600px" custom-class="rounded-lg">
      <div v-if="currentCategory" class="px-5">
        <div class="flex items-center mb-6">
          <div class="w-12 h-12 rounded-lg flex items-center justify-center mr-4" :style="{backgroundColor: currentCategory.color || '#909399', color: '#fff'}">
            <i :class="currentCategory.icon || 'el-icon-folder'" class="text-2xl"></i>
          </div>
          <div>
            <h3 class="text-xl font-bold text-gray-800 mb-1">{{ currentCategory.name }}</h3>
            <p class="text-gray-500">ID: {{ currentCategory.id }}</p>
          </div>
        </div>
        
        <el-divider></el-divider>
        
        <div class="space-y-6">
          <div class="bg-gray-50 rounded-lg p-4">
            <h4 class="flex items-center text-gray-700 font-medium mb-3">
              <i class="el-icon-info text-blue-500 mr-2"></i> 基本信息
            </h4>
            <div class="space-y-3">
              <div class="flex">
                <span class="text-gray-500 w-24">分类名称:</span>
                <span class="text-gray-800">{{ currentCategory.name }}</span>
              </div>
              <div class="flex items-start">
                <span class="text-gray-500 w-24">分类描述:</span>
                <span class="text-gray-800">{{ currentCategory.description || '无' }}</span>
              </div>
              <!-- <div class="flex">
                <span class="text-gray-500 w-24">排序顺序:</span>
                <span class="text-gray-800">{{ currentCategory.sort_order }}</span>
              </div> -->
              <div class="flex">
                <span class="text-gray-500 w-24">状态:</span>
                <el-tag :type="currentCategory.status === 'ACTIVE' ? 'success' : 'info'" size="small">
                  {{ currentCategory.status === 'ACTIVE' ? '启用' : '禁用' }}
                </el-tag>
              </div>
            </div>
          </div>
          
          <div class="bg-gray-50 rounded-lg p-4">
            <h4 class="flex items-center text-gray-700 font-medium mb-3">
              <i class="el-icon-brush text-green-500 mr-2"></i> 样式信息
            </h4>
            <div class="grid grid-cols-2 gap-4">
              <!-- <div class="flex">
                <span class="text-gray-500 w-24">图标:</span>
                <div class="flex items-center">
                  <i :class="currentCategory.icon || 'el-icon-folder'" :style="{color: currentCategory.color || '#909399'}" class="mr-2"></i>
                  <span class="text-gray-800">{{ currentCategory.icon || '-' }}</span>
                </div>
              </div> -->
              <div class="flex">
                <span class="text-gray-500 w-24">颜色:</span>
                <div class="flex items-center">
                  <div class="color-block mr-2" :style="{backgroundColor: currentCategory.color || '#909399'}"></div>
                  <span class="text-gray-800">{{ currentCategory.color || '-' }}</span>
                </div>
              </div>
            </div>
          </div>
          
          <!-- <div class="bg-gray-50 rounded-lg p-4">
            <h4 class="flex items-center text-gray-700 font-medium mb-3">
              <i class="el-icon-time text-orange-500 mr-2"></i> 其他信息
            </h4>
            <div class="grid grid-cols-1 gap-3">
              <div class="flex">
                <span class="text-gray-500 w-24">创建时间:</span>
                <span class="text-gray-800">{{ currentCategory.create_time || '-' }}</span>
              </div>
              <div class="flex">
                <span class="text-gray-500 w-24">创建人:</span>
                <span class="text-gray-800">{{ currentCategory.created_by || '-' }}</span>
              </div>
              <div class="flex">
                <span class="text-gray-500 w-24">更新时间:</span>
                <span class="text-gray-800">{{ currentCategory.update_time || '-' }}</span>
              </div>
            </div>
          </div> -->
        </div>
      </div>
      <div slot="footer" class="flex justify-end space-x-3">
        <el-button @click="detailDialogVisible = false" icon="el-icon-close">关 闭</el-button>
        <el-button type="primary" @click="handleEdit(currentCategory)" icon="el-icon-edit">编辑信息</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "ActivityCategoryManage",
  data() {
    return {
      // 搜索表单
      searchForm: {
        name: "",
        status: ""
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
      dialogTitle: "添加活动分类",
      isEdit: false,
      submitLoading: false,
      
      // 详情对话框
      detailDialogVisible: false,
      currentCategory: null,
      
      // 表单
      form: {
        id: null,
        name: "",
        description: "",
        icon: "",
        color: "",
        sort_order: 0,
        status: "ACTIVE"
      },
      
      // 表单验证规则
      rules: {
        name: [
          { required: true, message: "请输入分类名称", trigger: "blur" },
          { max: 100, message: "长度不能超过 100 个字符", trigger: "blur" }
        ],
        description: [
          { max: 500, message: "长度不能超过 500 个字符", trigger: "blur" }
        ],
        icon: [
          { max: 255, message: "长度不能超过 255 个字符", trigger: "blur" }
        ],
        
     
        status: [
          { required: true, message: "请选择状态", trigger: "change" }
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
      
      const params = {
        pageNum: this.pageNum,
        pageSize: this.pageSize
      };
      
      // 只有当搜索表单中有值时才添加到请求参数中
      if (this.searchForm.name) {
        params.name = this.searchForm.name;
      }
      
      if (this.searchForm.status) {
        params.status = this.searchForm.status;
      }
      
      this.$request.get("/activity/category/selectPage", { params })
        .then(res => {
          if (res.code === '200') {
            this.tableData = res.data.list || [];
            this.total = res.data.total || 0;
          } else {
            this.$message.error(res.msg || "获取分类数据失败");
          }
        })
        .catch(() => {
          this.$message.error("获取分类数据异常");
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
      this.$message.success("已重置搜索条件，显示所有分类数据");
    },
    
    // 查询全部分类
    queryAllCategories() {
      // 清除搜索表单
      this.$refs.searchForm.resetFields();
      this.pageNum = 1;
      this.fetchData();
      this.$message.success("已显示所有分类数据");
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
    
    // 添加分类
    handleAdd() {
      this.dialogTitle = "添加活动分类";
      this.isEdit = false;
      this.form = {
        id: null,
        name: "",
        description: "",
        icon: "",
        color: "",
        sort_order: 0,
        status: "ACTIVE"
      };
      this.dialogVisible = true;
      
      // 清除验证
      this.$nextTick(() => {
        if (this.$refs.form) {
          this.$refs.form.clearValidate();
        }
      });
    },
    
    // 编辑分类
    handleEdit(row) {
      this.dialogTitle = "编辑活动分类";
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
      this.currentCategory = row;
      this.detailDialogVisible = true;
    },
    
    // 保存分类（添加或更新）
    saveCategory() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.submitLoading = true;
          
          // 获取当前登录用户
          const userInfo = JSON.parse(localStorage.getItem('xm-user') || '{}');
          
          // 如果是新增，设置创建人
          if (!this.isEdit) {
            this.form.createdBy = userInfo.name || 'admin';
          }
          
          const request = this.isEdit 
            ? this.$request.put("/activity/category/update", this.form)
            : this.$request.post("/activity/category/add", this.form);
            
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
              this.$message.error(this.isEdit ? "编辑分类请求异常" : "添加分类请求异常");
            })
            .finally(() => {
              this.submitLoading = false;
            });
        }
      });
    },
    
    // 删除分类
    handleDelete(row) {
      this.$confirm("确定删除该分类吗? 删除后将无法恢复。", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        this.$request.delete(`/activity/category/delete/${row.id}`)
          .then(res => {
            if (res.code === '200') {
              this.$message.success("删除成功");
              this.fetchData();
            } else {
              this.$message.error(res.msg || "删除失败");
            }
          })
          .catch(() => {
            this.$message.error("删除分类请求异常");
          });
      }).catch(() => {});
    },
    
    // 批量删除
    handleBatchDelete() {
      if (this.selection.length === 0) {
        this.$message.warning("请选择要删除的分类");
        return;
      }
      
      this.$confirm(`确定删除选中的 ${this.selection.length} 个分类吗? 删除后将无法恢复。`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        const ids = this.selection.map(item => item.id);
        
        this.$request.delete("/activity/category/delete/batch", { data: ids })
          .then(res => {
            if (res.code === '200') {
              this.$message.success("批量删除成功");
              this.fetchData();
            } else {
              this.$message.error(res.msg || "批量删除失败");
            }
          })
          .catch(() => {
            this.$message.error("批量删除分类请求异常");
          });
      }).catch(() => {});
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

/* 颜色块样式 */
.color-block {
  width: 20px;
  height: 20px;
  border-radius: 4px;
  display: inline-block;
  border: 1px solid rgba(0, 0, 0, 0.1);
}

/* 截断长文本 */
.truncate {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style> 