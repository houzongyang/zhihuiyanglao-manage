<template>
  <div>
    <!-- 功能说明 -->
    <div class="bg-white rounded-lg shadow-sm p-6 mb-5">
      <div class="flex items-center">
        <i class="el-icon-s-custom text-3xl text-green-500 mr-3"></i>
        <div>
          <h2 class="text-xl font-bold text-gray-800 mb-1">护理安排管理</h2>
          <p class="text-gray-600">本模块用于管理护理人员与老年人的排班安排，提供护理安排的增加、修改、删除和查询功能，包括班次安排、工作内容分配等</p>
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
              <el-select 
                v-model="searchForm.nurseId" 
                placeholder="请选择护理人员" 
                clearable
                filterable
                size="small"
              >
                <el-option
                  v-for="nurse in nurseOptions"
                  :key="nurse.id"
                  :label="nurse.name"
                  :value="nurse.id">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-select 
                v-model="searchForm.shiftType" 
                placeholder="请选择班次类型" 
                clearable
                size="small"
              >
                <el-option label="早班" value="MORNING"></el-option>
                <el-option label="中班" value="AFTERNOON"></el-option>
                <el-option label="夜班" value="NIGHT"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-select 
                v-model="searchForm.status" 
                placeholder="请选择状态" 
                clearable
                size="small"
              >
                <el-option label="已安排" value="SCHEDULED"></el-option>
                <el-option label="已完成" value="COMPLETED"></el-option>
                <el-option label="已取消" value="CANCELLED"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-date-picker
                v-model="searchForm.shiftDate"
                type="date"
                placeholder="选择安排日期"
                size="small"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd">
              </el-date-picker>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSearch" icon="el-icon-search" size="small" class="mr-2">查询</el-button>
              <el-button @click="resetSearch" icon="el-icon-refresh" size="small" class="mr-2">重置</el-button>
              <el-button type="success" @click="queryAllAssignments" icon="el-icon-menu" size="small">查询全部</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div class="w-full lg:w-auto flex justify-end space-x-3">
          <el-button type="primary" @click="handleAdd" icon="el-icon-plus" size="small">添加护理安排</el-button>
          <el-button type="danger" @click="handleBatchDelete" :disabled="selection.length === 0" icon="el-icon-delete" size="small">
            批量删除
          </el-button>
        </div>
      </div>
    </div>

    <!-- 护理安排表格 -->
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
        <el-table-column label="护理人员" min-width="120">
          <template slot-scope="scope">
            <div class="flex items-center">
              <i class="el-icon-s-custom text-gray-400 mr-1"></i>
              <span>{{ getNurseName(scope.row.nurseId) }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="护理计划" min-width="150">
          <template slot-scope="scope">
            <div class="flex items-center">
              <i class="el-icon-document text-gray-400 mr-1"></i>
              <span>{{ getCarePlanTitle(scope.row.carePlanId) }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="安排日期" width="120">
          <template slot-scope="scope">
            <div class="flex items-center">
              <i class="el-icon-date text-gray-400 mr-1"></i>
              <span>{{ scope.row.shiftDate }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="工作时间" width="150">
          <template slot-scope="scope">
            <div class="flex items-center">
              <i class="el-icon-time text-gray-400 mr-1"></i>
              <span>{{ scope.row.startTime }} - {{ scope.row.endTime }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="班次类型" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getShiftTagType(scope.row.shiftType)" size="small">
              {{ getShiftTypeName(scope.row.shiftType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="workContent" label="工作内容" min-width="200" show-overflow-tooltip></el-table-column>
        <el-table-column label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusTagType(scope.row.status)" size="small">
              {{ getStatusName(scope.row.status) }}
            </el-tag>
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

    <!-- 添加/编辑护理安排对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="800px" :close-on-click-modal="false" custom-class="rounded-lg">
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
            <el-form-item label="护理人员" prop="nurseId">
              <el-select v-model="form.nurseId" placeholder="请选择护理人员" style="width: 100%" filterable>
                <el-option
                  v-for="nurse in nurseOptions"
                  :key="nurse.id"
                  :label="nurse.name"
                  :value="nurse.id">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="护理计划" prop="carePlanId">
              <el-select v-model="form.carePlanId" placeholder="请选择护理计划" style="width: 100%" filterable clearable>
                <el-option
                  v-for="plan in carePlanOptions"
                  :key="plan.id"
                  :label="plan.planTitle"
                  :value="plan.id">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="安排日期" prop="shiftDate">
              <el-date-picker
                v-model="form.shiftDate"
                type="date"
                placeholder="选择安排日期"
                style="width: 100%"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="开始时间" prop="startTime">
              <el-time-picker
                v-model="form.startTime"
                placeholder="选择开始时间"
                style="width: 100%"
                format="HH:mm"
                value-format="HH:mm:ss">
              </el-time-picker>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="结束时间" prop="endTime">
              <el-time-picker
                v-model="form.endTime"
                placeholder="选择结束时间"
                style="width: 100%"
                format="HH:mm"
                value-format="HH:mm:ss">
              </el-time-picker>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="班次类型" prop="shiftType">
              <el-select v-model="form.shiftType" placeholder="请选择班次类型" style="width: 100%">
                <el-option label="早班" value="MORNING"></el-option>
                <el-option label="中班" value="AFTERNOON"></el-option>
                <el-option label="夜班" value="NIGHT"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择状态" style="width: 100%">
            <el-option label="已安排" value="SCHEDULED"></el-option>
            <el-option label="已完成" value="COMPLETED"></el-option>
            <el-option label="已取消" value="CANCELLED"></el-option>
          </el-select>
        </el-form-item>
        
        <el-form-item label="工作内容" prop="workContent">
          <el-input v-model="form.workContent" type="textarea" :rows="3" placeholder="请输入工作内容"></el-input>
        </el-form-item>
        
        <el-form-item label="完成说明" prop="completionNotes">
          <el-input v-model="form.completionNotes" type="textarea" :rows="2" placeholder="请输入完成说明（可选）"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="flex justify-end space-x-3">
        <el-button @click="dialogVisible = false" icon="el-icon-close">取 消</el-button>
        <el-button type="primary" @click="saveAssignment" :loading="submitLoading" icon="el-icon-check">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 护理安排详情对话框 -->
    <el-dialog title="护理安排详情" :visible.sync="detailDialogVisible" width="700px" custom-class="rounded-lg">
      <div v-if="currentAssignment" class="px-5">
        <div class="flex items-center mb-6">
          <div class="w-16 h-16 bg-green-50 rounded-lg flex items-center justify-center mr-4">
            <i class="el-icon-s-custom text-2xl text-green-500"></i>
          </div>
          <div>
            <h3 class="text-xl font-bold text-gray-800 mb-1">护理安排详情</h3>
            <p class="text-gray-500">{{ getShiftTypeName(currentAssignment.shiftType) }} · {{ getStatusName(currentAssignment.status) }}</p>
          </div>
        </div>
        
        <el-divider></el-divider>
        
        <div class="space-y-6">
          <div class="bg-gray-50 rounded-lg p-4">
            <h4 class="flex items-center text-gray-700 font-medium mb-3">
              <i class="el-icon-user text-blue-500 mr-2"></i> 人员信息
            </h4>
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div class="flex">
                <span class="text-gray-500 w-24">老年人:</span>
                <span class="text-gray-800">{{ getUserName(currentAssignment.userId) }}</span>
              </div>
              <div class="flex">
                <span class="text-gray-500 w-24">护理人员:</span>
                <span class="text-gray-800">{{ getNurseName(currentAssignment.nurseId) }}</span>
              </div>
              <div class="flex">
                <span class="text-gray-500 w-24">护理计划:</span>
                <span class="text-gray-800">{{ getCarePlanTitle(currentAssignment.carePlanId) }}</span>
              </div>
            </div>
          </div>
          
          <div class="bg-gray-50 rounded-lg p-4">
            <h4 class="flex items-center text-gray-700 font-medium mb-3">
              <i class="el-icon-time text-green-500 mr-2"></i> 时间安排
            </h4>
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div class="flex">
                <span class="text-gray-500 w-24">安排日期:</span>
                <span class="text-gray-800">{{ currentAssignment.shiftDate }}</span>
              </div>
              <div class="flex">
                <span class="text-gray-500 w-24">工作时间:</span>
                <span class="text-gray-800">{{ currentAssignment.startTime }} - {{ currentAssignment.endTime }}</span>
              </div>
              <div class="flex">
                <span class="text-gray-500 w-24">班次类型:</span>
                <span class="text-gray-800">{{ getShiftTypeName(currentAssignment.shiftType) }}</span>
              </div>
              <div class="flex">
                <span class="text-gray-500 w-24">当前状态:</span>
                <span class="text-gray-800">{{ getStatusName(currentAssignment.status) }}</span>
              </div>
            </div>
          </div>
          
          <div v-if="currentAssignment.workContent" class="bg-gray-50 rounded-lg p-4">
            <h4 class="flex items-center text-gray-700 font-medium mb-3">
              <i class="el-icon-document text-purple-500 mr-2"></i> 工作内容
            </h4>
            <p class="text-gray-800 leading-relaxed">{{ currentAssignment.workContent }}</p>
          </div>
          
          <div v-if="currentAssignment.completionNotes" class="bg-gray-50 rounded-lg p-4">
            <h4 class="flex items-center text-gray-700 font-medium mb-3">
              <i class="el-icon-chat-line-square text-orange-500 mr-2"></i> 完成说明
            </h4>
            <p class="text-gray-800">{{ currentAssignment.completionNotes }}</p>
          </div>
        </div>
      </div>
      <div slot="footer" class="flex justify-end space-x-3">
        <el-button @click="detailDialogVisible = false" icon="el-icon-close">关 闭</el-button>
        <el-button type="primary" @click="handleEdit(currentAssignment)" icon="el-icon-edit">编辑安排</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "CareAssignmentManage",
  data() {
    return {
      // 搜索表单
      searchForm: {
        userId: "",
        nurseId: "",
        shiftType: "",
        status: "",
        shiftDate: "",
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
      nurseOptions: [],     // 护理人员选项
      carePlanOptions: [],  // 护理计划选项
      
      // 对话框
      dialogVisible: false,
      dialogTitle: "添加护理安排",
      isEdit: false,
      submitLoading: false,
      
      // 详情对话框
      detailDialogVisible: false,
      currentAssignment: null,
      
      // 表单
      form: {
        id: null,
        userId: "",
        nurseId: "",
        carePlanId: "",
        shiftDate: "",
        startTime: "",
        endTime: "",
        shiftType: "MORNING",
        workContent: "",
        status: "SCHEDULED",
        completionNotes: "",
      },
      
      // 表单验证规则
      rules: {
        userId: [
          { required: true, message: "请选择老年人", trigger: "change" }
        ],
        nurseId: [
          { required: true, message: "请选择护理人员", trigger: "change" }
        ],
        shiftDate: [
          { required: true, message: "请选择安排日期", trigger: "change" }
        ],
        startTime: [
          { required: true, message: "请选择开始时间", trigger: "change" }
        ],
        endTime: [
          { required: true, message: "请选择结束时间", trigger: "change" }
        ],
        shiftType: [
          { required: true, message: "请选择班次类型", trigger: "change" }
        ],
        status: [
          { required: true, message: "请选择状态", trigger: "change" }
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
      if (this.searchForm.nurseId) params.nurseId = this.searchForm.nurseId;
      if (this.searchForm.shiftType) params.shiftType = this.searchForm.shiftType;
      if (this.searchForm.status) params.status = this.searchForm.status;
      if (this.searchForm.shiftDate) params.shiftDate = this.searchForm.shiftDate;
      
      this.$request.get("/care-assignment/selectPage", { params })
        .then(res => {
          if (res.code === '200') {
            this.tableData = res.data.list || [];
            this.total = res.data.total || 0;
          } else {
            this.$message.error(res.msg || "获取护理安排数据失败");
          }
        })
        .catch(() => {
          this.$message.error("获取护理安排数据异常");
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
      
      // 获取护理人员选项 (nurse表)
      this.$request.get("/nurse/selectAll")
        .then(res => {
          if (res.code === '200') {
            this.nurseOptions = res.data || [];
          }
        })
        .catch(() => {
          console.error("获取护理人员选项失败");
        });
      
      // 获取护理计划选项
      this.$request.get("/care-plan/selectAll")
        .then(res => {
          if (res.code === '200') {
            this.carePlanOptions = res.data || [];
          }
        })
        .catch(() => {
          console.error("获取护理计划选项失败");
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
      this.$message.success("已重置搜索条件，显示所有护理安排数据");
    },
    
    // 查询全部护理安排
    queryAllAssignments() {
      this.$refs.searchForm.resetFields();
      this.pageNum = 1;
      this.fetchData();
      this.$message.success("已显示所有护理安排数据");
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
    
    // 添加护理安排
    handleAdd() {
      this.dialogTitle = "添加护理安排";
      this.isEdit = false;
      this.form = {
        id: null,
        userId: "",
        nurseId: "",
        carePlanId: "",
        shiftDate: "",
        startTime: "",
        endTime: "",
        shiftType: "MORNING",
        workContent: "",
        status: "SCHEDULED",
        completionNotes: "",
      };
      this.dialogVisible = true;
      
      // 清除验证
      this.$nextTick(() => {
        if (this.$refs.form) {
          this.$refs.form.clearValidate();
        }
      });
    },
    
    // 编辑护理安排
    handleEdit(row) {
      this.dialogTitle = "编辑护理安排";
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
      this.currentAssignment = JSON.parse(JSON.stringify(row));
      this.detailDialogVisible = true;
    },
    
    // 保存护理安排（添加或更新）
    saveAssignment() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.submitLoading = true;
          
          const request = this.isEdit 
            ? this.$request.put("/care-assignment/update", this.form)
            : this.$request.post("/care-assignment/add", this.form);
            
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
              this.$message.error(this.isEdit ? "编辑护理安排请求异常" : "添加护理安排请求异常");
            })
            .finally(() => {
              this.submitLoading = false;
            });
        }
      });
    },
    
    // 删除护理安排
    handleDelete(row) {
      this.$confirm("确定删除该护理安排吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        this.$request.delete(`/care-assignment/delete/${row.id}`)
          .then(res => {
            if (res.code === '200') {
              this.$message.success("删除成功");
              this.fetchData();
            } else {
              this.$message.error(res.msg || "删除失败");
            }
          })
          .catch(() => {
            this.$message.error("删除护理安排请求异常");
          });
      }).catch(() => {});
    },
    
    // 批量删除
    handleBatchDelete() {
      if (this.selection.length === 0) {
        this.$message.warning("请选择要删除的护理安排");
        return;
      }
      
      this.$confirm(`确定删除选中的 ${this.selection.length} 个护理安排吗?`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        const ids = this.selection.map(item => item.id);
        
        this.$request.delete("/care-assignment/delete/batch", { data: ids })
          .then(res => {
            if (res.code === '200') {
              this.$message.success("批量删除成功");
              this.fetchData();
            } else {
              this.$message.error(res.msg || "批量删除失败");
            }
          })
          .catch(() => {
            this.$message.error("批量删除护理安排请求异常");
          });
      }).catch(() => {});
    },

    // 获取用户名称
    getUserName(userId) {
      const user = this.userOptions.find(item => item.id === userId);
      return user ? user.name : '未知用户';
    },
    
    // 获取护理人员名称
    getNurseName(nurseId) {
      const nurse = this.nurseOptions.find(item => item.id === nurseId);
      return nurse ? nurse.name : '未知护理人员';
    },
    
    // 获取护理计划标题
    getCarePlanTitle(carePlanId) {
      if (!carePlanId) return '无';
      const plan = this.carePlanOptions.find(item => item.id === carePlanId);
      return plan ? plan.planTitle : '未知计划';
    },

    // 获取班次类型名称
    getShiftTypeName(shiftType) {
      switch (shiftType) {
        case 'MORNING':
          return '早班';
        case 'AFTERNOON':
          return '中班';
        case 'NIGHT':
          return '夜班';
        default:
          return '未知';
      }
    },
    
    // 获取班次标签类型
    getShiftTagType(shiftType) {
      switch (shiftType) {
        case 'MORNING':
          return 'success';
        case 'AFTERNOON':
          return 'warning';
        case 'NIGHT':
          return 'info';
        default:
          return '';
      }
    },

    // 获取状态名称
    getStatusName(status) {
      switch (status) {
        case 'SCHEDULED':
          return '已安排';
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
        case 'SCHEDULED':
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