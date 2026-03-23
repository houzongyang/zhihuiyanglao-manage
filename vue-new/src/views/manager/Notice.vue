<template>
  <div>
    <!-- 功能说明 -->
    <div class="bg-white rounded-lg shadow-sm p-6 mb-5">
      <div class="flex items-center">
        <i class="el-icon-bell text-3xl text-blue-500 mr-3"></i>
        <div>
          <h2 class="text-xl font-bold text-gray-800 mb-1">公告管理</h2>
          <p class="text-gray-600">本模块用于管理系统公告信息，提供公告的增加、修改、删除和查询功能</p>
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
                v-model="searchForm.title" 
                placeholder="请输入公告标题" 
                prefix-icon="el-icon-document" 
                clearable
                size="small"
              ></el-input>
            </el-form-item>
            <el-form-item>
              <el-input 
                v-model="searchForm.user" 
                placeholder="请输入创建人" 
                prefix-icon="el-icon-user" 
                clearable
                size="small"
              ></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSearch" icon="el-icon-search" size="small" class="mr-2">查询</el-button>
              <el-button @click="resetSearch" icon="el-icon-refresh" size="small" class="mr-2">重置</el-button>
              <el-button type="success" @click="queryAllNotices" icon="el-icon-menu" size="small">查询全部</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div class="w-full lg:w-auto flex justify-end space-x-3">
          <el-button type="primary" @click="handleAdd" icon="el-icon-plus" size="small">添加公告</el-button>
          <el-button type="danger" @click="handleBatchDelete" :disabled="selection.length === 0" icon="el-icon-delete" size="small">
            批量删除
          </el-button>
        </div>
      </div>
    </div>

    <!-- 公告表格 -->
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
        <el-table-column prop="title" label="公告标题" min-width="200">
          <template slot-scope="scope">
            <div class="flex items-center">
              <i class="el-icon-document text-blue-400 mr-2"></i>
              <span class="font-medium text-gray-800">{{ scope.row.title }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="content" label="公告内容" min-width="300">
          <template slot-scope="scope">
            <div class="text-gray-600 truncate" :title="scope.row.content">
              {{ scope.row.content || '暂无内容' }}
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="user" label="创建人" min-width="120">
          <template slot-scope="scope">
            <div class="flex items-center">
              <i class="el-icon-user text-green-400 mr-1"></i>
              <span>{{ scope.row.user || '未知' }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="time" label="创建时间" min-width="130">
          <template slot-scope="scope">
            <div class="flex items-center">
              <i class="el-icon-time text-gray-400 mr-1"></i>
              <span>{{ scope.row.time || '未设置' }}</span>
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

    <!-- 添加/编辑公告对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="800px" :close-on-click-modal="false" custom-class="rounded-lg">
      <el-form :model="form" :rules="rules" ref="form" label-width="80px">
        <el-form-item label="公告标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入公告标题" prefix-icon="el-icon-document"></el-input>
        </el-form-item>
        <el-form-item label="公告内容" prop="content">
          <el-input 
            v-model="form.content" 
            type="textarea" 
            :rows="8"
            placeholder="请输入公告内容"
            maxlength="1000"
            show-word-limit
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="flex justify-end space-x-3">
        <el-button @click="dialogVisible = false" icon="el-icon-close">取 消</el-button>
        <el-button type="primary" @click="saveNotice" :loading="submitLoading" icon="el-icon-check">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 公告详情对话框 -->
    <el-dialog title="公告详情" :visible.sync="detailDialogVisible" width="700px" custom-class="rounded-lg">
      <div v-if="currentNotice" class="px-5">
        <div class="mb-6">
          <div class="flex items-center mb-4">
            <i class="el-icon-document text-blue-500 text-2xl mr-3"></i>
            <h3 class="text-xl font-bold text-gray-800">{{ currentNotice.title }}</h3>
          </div>
        </div>
        
        <el-divider></el-divider>
        
        <div class="space-y-6">
          <div class="bg-gray-50 rounded-lg p-4">
            <h4 class="flex items-center text-gray-700 font-medium mb-3">
              <i class="el-icon-info text-blue-500 mr-2"></i> 基本信息
            </h4>
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div class="flex">
                <span class="text-gray-500 w-20">公告ID:</span>
                <span class="text-gray-800">{{ currentNotice.id }}</span>
              </div>
              <div class="flex">
                <span class="text-gray-500 w-20">创建人:</span>
                <span class="text-gray-800">{{ currentNotice.user || '未知' }}</span>
              </div>
              <div class="flex">
                <span class="text-gray-500 w-20">创建时间:</span>
                <span class="text-gray-800">{{ currentNotice.time || '未设置' }}</span>
              </div>
            </div>
          </div>
          
          <div class="bg-gray-50 rounded-lg p-4">
            <h4 class="flex items-center text-gray-700 font-medium mb-3">
              <i class="el-icon-document text-green-500 mr-2"></i> 公告内容
            </h4>
            <div class="bg-white rounded-lg p-4 border border-gray-200">
              <p class="text-gray-800 leading-relaxed whitespace-pre-wrap">{{ currentNotice.content || '暂无内容' }}</p>
            </div>
          </div>
        </div>
      </div>
      <div slot="footer" class="flex justify-end space-x-3">
        <el-button @click="detailDialogVisible = false" icon="el-icon-close">关 闭</el-button>
        <el-button type="primary" @click="handleEdit(currentNotice)" icon="el-icon-edit">编辑公告</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "NoticeManage",
  data() {
    return {
      // 搜索表单
      searchForm: {
        title: "",
        user: "",
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
      dialogTitle: "添加公告",
      isEdit: false,
      submitLoading: false,
      
      // 详情对话框
      detailDialogVisible: false,
      currentNotice: null,
      
      // 表单
      form: {
        id: null,
        title: "",
        content: "",
        time: "",
        user: ""
      },
      
      // 表单验证规则
      rules: {
        title: [
          { required: true, message: "请输入公告标题", trigger: "blur" },
          { min: 2, max: 100, message: "长度在 2 到 100 个字符", trigger: "blur" }
        ],
        content: [
          { required: true, message: "请输入公告内容", trigger: "blur" },
          { min: 5, max: 1000, message: "长度在 5 到 1000 个字符", trigger: "blur" }
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
      if (this.searchForm.title) {
        params.title = this.searchForm.title;
      }
      
      if (this.searchForm.user) {
        params.user = this.searchForm.user;
      }
      
      this.$request.get("/notice/selectPage", { params })
        .then(res => {
          if (res.code === '200') {
            this.tableData = res.data.list || [];
            this.total = res.data.total || 0;
          } else {
            this.$message.error(res.msg || "获取公告数据失败");
          }
        })
        .catch(() => {
          this.$message.error("获取公告数据异常");
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
      this.$message.success("已重置搜索条件，显示所有公告数据");
    },
    
    // 查询全部公告
    queryAllNotices() {
      // 清除搜索表单
      this.$refs.searchForm.resetFields();
      this.pageNum = 1;
      this.fetchData();
      this.$message.success("已显示所有公告数据");
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
    
    // 添加公告
    handleAdd() {
      this.dialogTitle = "添加公告";
      this.isEdit = false;
      this.form = {
        id: null,
        title: "",
        content: "",
        time: "",
        user: ""
      };
      this.dialogVisible = true;
      
      // 清除验证
      this.$nextTick(() => {
        if (this.$refs.form) {
          this.$refs.form.clearValidate();
        }
      });
    },
    
    // 编辑公告
    handleEdit(row) {
      this.dialogTitle = "编辑公告";
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
      this.currentNotice = row;
      this.detailDialogVisible = true;
    },
    
    // 保存公告（添加或更新）
    saveNotice() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.submitLoading = true;
          
          const request = this.isEdit 
            ? this.$request.put("/notice/update", this.form)
            : this.$request.post("/notice/add", this.form);
            
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
              this.$message.error(this.isEdit ? "编辑公告请求异常" : "添加公告请求异常");
            })
            .finally(() => {
              this.submitLoading = false;
            });
        }
      });
    },
    
    // 删除公告
    handleDelete(row) {
      this.$confirm("确定删除该公告吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        this.$request.delete(`/notice/delete/${row.id}`)
          .then(res => {
            if (res.code === '200') {
              this.$message.success("删除成功");
              this.fetchData();
            } else {
              this.$message.error(res.msg || "删除失败");
            }
          })
          .catch(() => {
            this.$message.error("删除公告请求异常");
          });
      }).catch(() => {});
    },
    
    // 批量删除
    handleBatchDelete() {
      if (this.selection.length === 0) {
        this.$message.warning("请选择要删除的公告");
        return;
      }
      
      this.$confirm(`确定删除选中的 ${this.selection.length} 个公告吗?`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        const ids = this.selection.map(item => item.id);
        
        this.$request.delete("/notice/delete/batch", { data: ids })
          .then(res => {
            if (res.code === '200') {
              this.$message.success("批量删除成功");
              this.fetchData();
            } else {
              this.$message.error(res.msg || "批量删除失败");
            }
          })
          .catch(() => {
            this.$message.error("批量删除公告请求异常");
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

/* 内容截断样式 */
.truncate {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>