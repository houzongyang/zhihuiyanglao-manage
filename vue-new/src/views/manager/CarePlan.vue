<template>
  <div>
    <!-- 功能说明 -->
    <div class="bg-white rounded-lg shadow-sm p-6 mb-5">
      <div class="flex items-center">
        <i class="el-icon-document text-3xl text-blue-500 mr-3"></i>
        <div>
          <h2 class="text-xl font-bold text-gray-800 mb-1">护理计划管理</h2>
          <p class="text-gray-600">本模块用于管理老年人的个性化护理计划，包括护理等级设定、饮食运动规划，提供计划的增加、修改、删除和查询功能</p>
        </div>
      </div>
    </div>

    <!-- 搜索和操作区域 -->
    <div class="bg-white rounded-lg shadow-sm p-6 mb-5">
      <div class="flex flex-wrap items-center justify-between">
        <div class="w-full lg:w-auto mb-4 lg:mb-0">
          <el-form :inline="true" :model="searchForm" ref="searchForm">
            <el-form-item>
              <el-select 
                v-model="searchForm.userId" 
                placeholder="请选择老年人" 
                clearable
                filterable
                size="small"
              >
                <el-option
                  v-for="user in userOptions"
                  :key="user.id"
                  :label="user.name"
                  :value="user.id">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-input
                v-model="searchForm.planTitle"
                placeholder="请输入计划标题"
                clearable
                size="small">
              </el-input>
            </el-form-item>
            <el-form-item>
              <el-select 
                v-model="searchForm.careLevel" 
                placeholder="请选择护理等级" 
                clearable
                size="small"
              >
                <el-option label="一级护理" value="LEVEL1"></el-option>
                <el-option label="二级护理" value="LEVEL2"></el-option>
                <el-option label="三级护理" value="LEVEL3"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-select 
                v-model="searchForm.status" 
                placeholder="请选择状态" 
                clearable
                size="small"
              >
                <el-option label="执行中" value="ACTIVE"></el-option>
                <el-option label="已完成" value="COMPLETED"></el-option>
                <el-option label="已取消" value="CANCELLED"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSearch" icon="el-icon-search" size="small" class="mr-2">查询</el-button>
              <el-button @click="resetSearch" icon="el-icon-refresh" size="small" class="mr-2">重置</el-button>
              <el-button type="success" @click="queryAllPlans" icon="el-icon-menu" size="small">查询全部</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div class="w-full lg:w-auto flex justify-end space-x-3">
          <el-button type="primary" @click="handleAdd" icon="el-icon-plus" size="small">添加护理计划</el-button>
          <el-button type="danger" @click="handleBatchDelete" :disabled="selection.length === 0" icon="el-icon-delete" size="small">
            批量删除
          </el-button>
        </div>
      </div>
    </div>

    <!-- 护理计划表格 -->
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
        <el-table-column label="老年人" min-width="120">
          <template slot-scope="scope">
            <div class="flex items-center">
              <i class="el-icon-user text-gray-400 mr-1"></i>
              <span>{{ getUserName(scope.row.userId) }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="planTitle" label="计划标题" min-width="200" show-overflow-tooltip></el-table-column>
        <el-table-column label="护理等级" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getCareLevelTagType(scope.row.careLevel)" size="small">
              {{ getCareLevelName(scope.row.careLevel) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="执行期间" width="200">
          <template slot-scope="scope">
            <div class="flex items-center">
              <i class="el-icon-date text-gray-400 mr-1"></i>
              <span>{{ scope.row.startDate }} ~ {{ scope.row.endDate || '长期' }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusTagType(scope.row.status)" size="small">
              {{ getStatusName(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdBy" label="制定人" width="120"></el-table-column>
        <el-table-column label="创建时间" width="180">
          <template slot-scope="scope">
            <div class="flex items-center">
              <i class="el-icon-time text-gray-400 mr-1"></i>
              <span>{{ scope.row.createTime }}</span>
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

    <!-- 添加/编辑护理计划对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="900px" :close-on-click-modal="false" custom-class="rounded-lg">
      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="老年人" prop="userId">
              <el-select v-model="form.userId" placeholder="请选择老年人" style="width: 100%" filterable>
                <el-option
                  v-for="user in userOptions"
                  :key="user.id"
                  :label="user.name"
                  :value="user.id">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="护理等级" prop="careLevel">
              <el-select v-model="form.careLevel" placeholder="请选择护理等级" style="width: 100%">
                <el-option label="一级护理" value="LEVEL1"></el-option>
                <el-option label="二级护理" value="LEVEL2"></el-option>
                <el-option label="三级护理" value="LEVEL3"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="开始日期" prop="startDate">
              <el-date-picker
                v-model="form.startDate"
                type="date"
                placeholder="选择开始日期"
                style="width: 100%"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束日期">
              <el-date-picker
                v-model="form.endDate"
                type="date"
                placeholder="选择结束日期（可选）"
                style="width: 100%"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-select v-model="form.status" placeholder="请选择状态" style="width: 100%">
                <el-option label="执行中" value="ACTIVE"></el-option>
                <el-option label="已完成" value="COMPLETED"></el-option>
                <el-option label="已取消" value="CANCELLED"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="制定人" prop="createdBy">
              <el-input v-model="form.createdBy" placeholder="请输入制定人"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="计划标题" prop="planTitle">
          <el-input v-model="form.planTitle" placeholder="请输入护理计划标题"></el-input>
        </el-form-item>
        
        <el-form-item label="计划内容" prop="planContent">
          <el-input v-model="form.planContent" type="textarea" :rows="4" placeholder="请输入护理计划的详细内容"></el-input>
        </el-form-item>
        
        <el-form-item label="饮食计划">
          <el-input v-model="form.dietPlan" type="textarea" :rows="3" placeholder="请输入饮食计划（可选）"></el-input>
        </el-form-item>
        
        <el-form-item label="运动计划">
          <el-input v-model="form.exercisePlan" type="textarea" :rows="3" placeholder="请输入运动计划（可选）"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="flex justify-end space-x-3">
        <el-button @click="dialogVisible = false" icon="el-icon-close">取 消</el-button>
        <el-button type="primary" @click="savePlan" :loading="submitLoading" icon="el-icon-check">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 护理计划详情对话框 -->
    <el-dialog title="护理计划详情" :visible.sync="detailDialogVisible" width="800px" custom-class="rounded-lg">
      <div v-if="currentPlan" class="px-5">
        <div class="flex items-center mb-6">
          <div class="w-16 h-16 bg-blue-50 rounded-lg flex items-center justify-center mr-4">
            <i class="el-icon-document text-2xl text-blue-500"></i>
          </div>
          <div>
            <h3 class="text-xl font-bold text-gray-800 mb-1">{{ currentPlan.planTitle }}</h3>
            <p class="text-gray-500">{{ getCareLevelName(currentPlan.careLevel) }} · {{ getStatusName(currentPlan.status) }}</p>
          </div>
        </div>
        
        <el-divider></el-divider>
        
        <div class="space-y-6">
          <div class="bg-gray-50 rounded-lg p-4">
            <h4 class="flex items-center text-gray-700 font-medium mb-3">
              <i class="el-icon-user text-blue-500 mr-2"></i> 基础信息
            </h4>
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div class="flex">
                <span class="text-gray-500 w-24">老年人:</span>
                <span class="text-gray-800">{{ getUserName(currentPlan.userId) }}</span>
              </div>
              <div class="flex">
                <span class="text-gray-500 w-24">护理等级:</span>
                <span class="text-gray-800">{{ getCareLevelName(currentPlan.careLevel) }}</span>
              </div>
              <div class="flex">
                <span class="text-gray-500 w-24">制定人:</span>
                <span class="text-gray-800">{{ currentPlan.createdBy }}</span>
              </div>
              <div class="flex">
                <span class="text-gray-500 w-24">当前状态:</span>
                <span class="text-gray-800">{{ getStatusName(currentPlan.status) }}</span>
              </div>
            </div>
          </div>
          
          <div class="bg-gray-50 rounded-lg p-4">
            <h4 class="flex items-center text-gray-700 font-medium mb-3">
              <i class="el-icon-time text-green-500 mr-2"></i> 执行时间
            </h4>
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div class="flex">
                <span class="text-gray-500 w-24">开始日期:</span>
                <span class="text-gray-800">{{ currentPlan.startDate }}</span>
              </div>
              <div class="flex">
                <span class="text-gray-500 w-24">结束日期:</span>
                <span class="text-gray-800">{{ currentPlan.endDate || '长期执行' }}</span>
              </div>
            </div>
          </div>
          
          <div v-if="currentPlan.planContent" class="bg-gray-50 rounded-lg p-4">
            <h4 class="flex items-center text-gray-700 font-medium mb-3">
              <i class="el-icon-document text-purple-500 mr-2"></i> 计划内容
            </h4>
            <p class="text-gray-800 leading-relaxed">{{ currentPlan.planContent }}</p>
          </div>
          
          <div v-if="currentPlan.dietPlan" class="bg-gray-50 rounded-lg p-4">
            <h4 class="flex items-center text-gray-700 font-medium mb-3">
              <i class="el-icon-food text-orange-500 mr-2"></i> 饮食计划
            </h4>
            <p class="text-gray-800 leading-relaxed">{{ currentPlan.dietPlan }}</p>
          </div>
          
          <div v-if="currentPlan.exercisePlan" class="bg-gray-50 rounded-lg p-4">
            <h4 class="flex items-center text-gray-700 font-medium mb-3">
              <i class="el-icon-bicycle text-red-500 mr-2"></i> 运动计划
            </h4>
            <p class="text-gray-800 leading-relaxed">{{ currentPlan.exercisePlan }}</p>
          </div>
        </div>
      </div>
      <div slot="footer" class="flex justify-end space-x-3">
        <el-button @click="detailDialogVisible = false" icon="el-icon-close">关 闭</el-button>
        <el-button type="primary" @click="handleEdit(currentPlan)" icon="el-icon-edit">编辑计划</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "CarePlanManage",
  data() {
    return {
      // 搜索表单
      searchForm: {
        userId: "",
        planTitle: "",
        careLevel: "",
        status: "",
        createdBy: ""
      },
      // 表格数据
      tableData: [],
      loading: false,
      total: 0,
      pageNum: 1,
      pageSize: 10,
      
      // 选中项
      selection: [],
      
      // 选项数据
      userOptions: [],      // 老年人选项
      
      // 对话框
      dialogVisible: false,
      dialogTitle: "添加护理计划",
      isEdit: false,
      submitLoading: false,
      
      // 详情对话框
      detailDialogVisible: false,
      currentPlan: null,
      
      // 表单
      form: {
        id: null,
        userId: "",
        planTitle: "",
        planContent: "",
        careLevel: "LEVEL2",
        dietPlan: "",
        exercisePlan: "",
        startDate: "",
        endDate: "",
        status: "ACTIVE",
        createdBy: ""
      },
      
      // 表单验证规则
      rules: {
        userId: [
          { required: true, message: "请选择老年人", trigger: "change" }
        ],
        planTitle: [
          { required: true, message: "请输入计划标题", trigger: "blur" }
        ],
        planContent: [
          { required: true, message: "请输入计划内容", trigger: "blur" }
        ],
        careLevel: [
          { required: true, message: "请选择护理等级", trigger: "change" }
        ],
        startDate: [
          { required: true, message: "请选择开始日期", trigger: "change" }
        ],
        status: [
          { required: true, message: "请选择状态", trigger: "change" }
        ],
        createdBy: [
          { required: true, message: "请输入制定人", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.fetchData();
    this.fetchOptions();
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
      
      // 添加搜索条件
      if (this.searchForm.userId) params.userId = this.searchForm.userId;
      if (this.searchForm.planTitle) params.planTitle = this.searchForm.planTitle;
      if (this.searchForm.careLevel) params.careLevel = this.searchForm.careLevel;
      if (this.searchForm.status) params.status = this.searchForm.status;
      if (this.searchForm.createdBy) params.createdBy = this.searchForm.createdBy;
      
      this.$request.get("/care-plan/selectPage", { params })
        .then(res => {
          if (res.code === '200') {
            this.tableData = res.data.list || [];
            this.total = res.data.total || 0;
          } else {
            this.$message.error(res.msg || "获取护理计划数据失败");
          }
        })
        .catch(() => {
          this.$message.error("获取护理计划数据异常");
        })
        .finally(() => {
          this.loading = false;
        });
    },
    
    // 获取选项数据
    fetchOptions() {
      // 获取老年人选项
      this.$request.get("/user/selectAll")
        .then(res => {
          if (res.code === '200') {
            this.userOptions = res.data || [];
          }
        })
        .catch(() => {
          console.error("获取老年人选项失败");
        });
    },
    
    // 查询
    handleSearch() {
      this.pageNum = 1;
      this.fetchData();
    },
    
    // 重置查询
    resetSearch() {
      this.$refs.searchForm.resetFields();
      this.pageNum = 1;
      this.fetchData();
      this.$message.success("已重置搜索条件，显示所有护理计划数据");
    },
    
    // 查询全部护理计划
    queryAllPlans() {
      this.$refs.searchForm.resetFields();
      this.pageNum = 1;
      this.fetchData();
      this.$message.success("已显示所有护理计划数据");
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
    
    // 添加护理计划
    handleAdd() {
      this.dialogTitle = "添加护理计划";
      this.isEdit = false;
      this.form = {
        id: null,
        userId: "",
        planTitle: "",
        planContent: "",
        careLevel: "LEVEL2",
        dietPlan: "",
        exercisePlan: "",
        startDate: "",
        endDate: "",
        status: "ACTIVE",
        createdBy: ""
      };
      this.dialogVisible = true;
      
      // 清除验证
      this.$nextTick(() => {
        if (this.$refs.form) {
          this.$refs.form.clearValidate();
        }
      });
    },
    
    // 编辑护理计划
    handleEdit(row) {
      this.dialogTitle = "编辑护理计划";
      this.isEdit = true;
      this.form = JSON.parse(JSON.stringify(row));
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
      this.currentPlan = JSON.parse(JSON.stringify(row));
      this.detailDialogVisible = true;
    },
    
    // 保存护理计划（添加或更新）
    savePlan() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.submitLoading = true;
          
          const request = this.isEdit 
            ? this.$request.put("/care-plan/update", this.form)
            : this.$request.post("/care-plan/add", this.form);
            
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
              this.$message.error(this.isEdit ? "编辑护理计划请求异常" : "添加护理计划请求异常");
            })
            .finally(() => {
              this.submitLoading = false;
            });
        }
      });
    },
    
    // 删除护理计划
    handleDelete(row) {
      this.$confirm("确定删除该护理计划吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        this.$request.delete(`/care-plan/delete/${row.id}`)
          .then(res => {
            if (res.code === '200') {
              this.$message.success("删除成功");
              this.fetchData();
            } else {
              this.$message.error(res.msg || "删除失败");
            }
          })
          .catch(() => {
            this.$message.error("删除护理计划请求异常");
          });
      }).catch(() => {});
    },
    
    // 批量删除
    handleBatchDelete() {
      if (this.selection.length === 0) {
        this.$message.warning("请选择要删除的护理计划");
        return;
      }
      
      this.$confirm(`确定删除选中的 ${this.selection.length} 个护理计划吗?`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        const ids = this.selection.map(item => item.id);
        
        this.$request.delete("/care-plan/delete/batch", { data: ids })
          .then(res => {
            if (res.code === '200') {
              this.$message.success("批量删除成功");
              this.fetchData();
            } else {
              this.$message.error(res.msg || "批量删除失败");
            }
          })
          .catch(() => {
            this.$message.error("批量删除护理计划请求异常");
          });
      }).catch(() => {});
    },

    // 获取用户名称
    getUserName(userId) {
      const user = this.userOptions.find(item => item.id === userId);
      return user ? user.name : '未知用户';
    },

    // 获取护理等级名称
    getCareLevelName(careLevel) {
      switch (careLevel) {
        case 'LEVEL1':
          return '一级护理';
        case 'LEVEL2':
          return '二级护理';
        case 'LEVEL3':
          return '三级护理';
        default:
          return '未知';
      }
    },
    
    // 获取护理等级标签类型
    getCareLevelTagType(careLevel) {
      switch (careLevel) {
        case 'LEVEL1':
          return 'danger';
        case 'LEVEL2':
          return 'warning';
        case 'LEVEL3':
          return 'success';
        default:
          return '';
      }
    },

    // 获取状态名称
    getStatusName(status) {
      switch (status) {
        case 'ACTIVE':
          return '执行中';
        case 'COMPLETED':
          return '已完成';
        case 'CANCELLED':
          return '已取消';
        default:
          return '未知';
      }
    },
    
    // 获取状态标签类型
    getStatusTagType(status) {
      switch (status) {
        case 'ACTIVE':
          return 'info';
        case 'COMPLETED':
          return 'success';
        case 'CANCELLED':
          return 'danger';
        default:
          return '';
      }
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
</style>