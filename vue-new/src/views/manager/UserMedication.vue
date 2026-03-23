<template>
  <div>
    <!-- 功能说明 -->
    <div class="bg-white rounded-lg shadow-sm p-6 mb-5">
      <div class="flex items-center">
        <i class="el-icon-medicine text-3xl text-blue-500 mr-3"></i>
        <div>
          <h2 class="text-xl font-bold text-gray-800 mb-1">药物管理</h2>
          <p class="text-gray-600">本模块用于管理用户的药物信息，提供药物信息的增加、修改、删除和查询功能，支持药物状态管理</p>
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
                v-model="searchForm.medicationName" 
                placeholder="请输入药物名称" 
                prefix-icon="el-icon-medicine" 
                clearable
                size="small"
              ></el-input>
            </el-form-item>
            <el-form-item>
              <el-select 
                v-model="searchForm.status" 
                placeholder="选择状态" 
                clearable
                size="small"
                style="width: 120px;"
              >
                <el-option label="全部" value=""></el-option>
                <el-option label="进行中" value="ACTIVE"></el-option>
                <el-option label="已完成" value="COMPLETED"></el-option>
                <el-option label="已停药" value="STOPPED"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-select 
                v-model="searchForm.medicationType" 
                placeholder="选择类型" 
                clearable
                size="small"
                style="width: 140px;"
              >
                <el-option label="全部" value=""></el-option>
                <el-option label="心血管" value="CARDIOVASCULAR"></el-option>
                <el-option label="糖尿病" value="DIABETES"></el-option>
                <el-option label="止痛" value="ANALGESIC"></el-option>
                <el-option label="抗生素" value="ANTIBIOTIC"></el-option>
                <el-option label="补充剂" value="SUPPLEMENT"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSearch" icon="el-icon-search" size="small" class="mr-2">查询</el-button>
              <el-button @click="resetSearch" icon="el-icon-refresh" size="small" class="mr-2">重置</el-button>
              <el-button type="success" @click="queryAllMedications" icon="el-icon-menu" size="small">查询全部</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div class="w-full lg:w-auto flex justify-end space-x-3">
          <el-button type="primary" @click="handleAdd" icon="el-icon-plus" size="small">添加药物</el-button>
          <el-button type="danger" @click="handleBatchDelete" :disabled="selection.length === 0" icon="el-icon-delete" size="small">
            批量删除
          </el-button>
        </div>
      </div>
    </div>

    <!-- 药物表格 -->
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
        <el-table-column label="用户信息" min-width="130">
          <template slot-scope="scope">
            <div class="flex items-center">
              <i class="el-icon-user text-gray-400 mr-1"></i>
              <span>{{ getUserDisplayName(scope.row) }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="medicationName" label="药物名称" min-width="150">
          <template slot-scope="scope">
            <div class="font-medium text-gray-800">{{ scope.row.medicationName }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="medicationType" label="药物类型" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getMedicationTypeColor(scope.row.medicationType)" size="small">
              {{ getMedicationTypeText(scope.row.medicationType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="用药信息" min-width="180">
          <template slot-scope="scope">
            <div class="text-sm">
              <div class="mb-1">
                <span class="text-gray-500">剂量：</span>
                <span>{{ scope.row.dosage }}</span>
              </div>
              <div>
                <span class="text-gray-500">频率：</span>
                <span>{{ scope.row.frequency }}</span>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusColor(scope.row.status)" size="small">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="startDate" label="开始日期" width="120">
          <template slot-scope="scope">
            <div class="flex items-center text-sm">
              <i class="el-icon-date text-gray-400 mr-1"></i>
              <span>{{ formatDate(scope.row.startDate) }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="prescribedBy" label="开药医生" width="120">
          <template slot-scope="scope">
            <span>{{ scope.row.prescribedBy || '未记录' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" fixed="right" width="200" align="center">
          <template slot-scope="scope">
            <div class="flex justify-center space-x-1">
              <el-button type="primary" size="mini" icon="el-icon-view" circle @click="handleDetail(scope.row)" class="transform hover:-translate-y-1 transition-transform"></el-button>
              <el-button type="warning" size="mini" icon="el-icon-edit" circle @click="handleEdit(scope.row)" class="transform hover:-translate-y-1 transition-transform"></el-button>
              <el-button v-if="scope.row.status === 'ACTIVE'" type="info" size="mini" icon="el-icon-remove" circle @click="handleStop(scope.row)" class="transform hover:-translate-y-1 transition-transform"></el-button>
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

    <!-- 添加/编辑药物对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="900px" :close-on-click-modal="false" custom-class="rounded-lg">
      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="选择用户" prop="userId">
              <el-select v-model="form.userId" placeholder="请选择用户" filterable style="width: 100%;" @change="handleUserChange">
                <el-option
                  v-for="user in userList"
                  :key="user.id"
                  :label="`${user.name || user.username} (${user.username})`"
                  :value="user.id"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="药物名称" prop="medicationName">
              <el-input v-model="form.medicationName" placeholder="请输入药物名称"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="药物类型" prop="medicationType">
              <el-select v-model="form.medicationType" placeholder="选择药物类型" style="width: 100%;">
                <el-option label="心血管" value="CARDIOVASCULAR"></el-option>
                <el-option label="糖尿病" value="DIABETES"></el-option>
                <el-option label="止痛" value="ANALGESIC"></el-option>
                <el-option label="抗生素" value="ANTIBIOTIC"></el-option>
                <el-option label="补充剂" value="SUPPLEMENT"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="单次剂量" prop="dosage">
              <el-input v-model="form.dosage" placeholder="如：1片、5ml"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="用药频率" prop="frequency">
              <el-input v-model="form.frequency" placeholder="如：每日3次"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="用药时间" prop="administrationTimes">
              <el-input v-model="form.administrationTimes" placeholder="如：08:00,12:00,18:00"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="与餐关系" prop="mealRelation">
              <el-select v-model="form.mealRelation" placeholder="选择与餐关系" style="width: 100%;">
                <el-option label="餐前" value="BEFORE_MEAL"></el-option>
                <el-option label="餐中" value="WITH_MEAL"></el-option>
                <el-option label="餐后" value="AFTER_MEAL"></el-option>
                <el-option label="空腹" value="EMPTY_STOMACH"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="开始日期" prop="startDate">
              <el-date-picker
                v-model="form.startDate"
                type="date"
                placeholder="选择开始日期"
                style="width: 100%;"
              ></el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="结束日期">
              <el-date-picker
                v-model="form.endDate"
                type="date"
                placeholder="选择结束日期（可选）"
                style="width: 100%;"
              ></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="开药医生">
              <el-input v-model="form.prescribedBy" placeholder="请输入医生姓名"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="开药日期">
              <el-date-picker
                v-model="form.prescriptionDate"
                type="date"
                placeholder="选择开药日期"
                style="width: 100%;"
              ></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="用药状态">
              <el-select v-model="form.status" placeholder="选择状态" style="width: 100%;">
                <el-option label="进行中" value="ACTIVE"></el-option>
                <el-option label="已完成" value="COMPLETED"></el-option>
                <el-option label="已停药" value="STOPPED"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="用药目的">
          <el-input
            v-model="form.purpose"
            type="textarea"
            :rows="2"
            placeholder="请输入用药目的"
          ></el-input>
        </el-form-item>
        
        <el-form-item label="特殊说明">
          <el-input
            v-model="form.specialInstructions"
            type="textarea"
            :rows="2"
            placeholder="请输入特殊用药说明"
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="flex justify-end space-x-3">
        <el-button @click="dialogVisible = false" icon="el-icon-close">取 消</el-button>
        <el-button type="primary" @click="saveMedication" :loading="submitLoading" icon="el-icon-check">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 药物详情对话框 -->
    <el-dialog title="药物详情" :visible.sync="detailDialogVisible" width="800px" custom-class="rounded-lg">
      <div v-if="currentMedication" class="px-5">
        <div class="grid grid-cols-1 md:grid-cols-2 gap-6 mb-6">
          <div class="space-y-4">
            <div class="bg-gray-50 rounded-lg p-4">
              <h4 class="flex items-center text-gray-700 font-medium mb-3">
                <i class="el-icon-user text-blue-500 mr-2"></i> 用户信息
              </h4>
              <div class="space-y-2">
                <div class="flex">
                  <span class="text-gray-500 w-20">用户：</span>
                  <span class="text-gray-800">{{ getUserDisplayName(currentMedication) }}</span>
                </div>
              </div>
            </div>
            
            <div class="bg-gray-50 rounded-lg p-4">
              <h4 class="flex items-center text-gray-700 font-medium mb-3">
                <i class="el-icon-medicine text-green-500 mr-2"></i> 药物信息
              </h4>
              <div class="space-y-2">
                <div class="flex">
                  <span class="text-gray-500 w-20">名称：</span>
                  <span class="text-gray-800">{{ currentMedication.medicationName }}</span>
                </div>
                <div class="flex">
                  <span class="text-gray-500 w-20">类型：</span>
                  <el-tag :type="getMedicationTypeColor(currentMedication.medicationType)" size="small">
                    {{ getMedicationTypeText(currentMedication.medicationType) }}
                  </el-tag>
                </div>
                <div class="flex">
                  <span class="text-gray-500 w-20">状态：</span>
                  <el-tag :type="getStatusColor(currentMedication.status)" size="small">
                    {{ getStatusText(currentMedication.status) }}
                  </el-tag>
                </div>
              </div>
            </div>
          </div>
          
          <div class="space-y-4">
            <div class="bg-gray-50 rounded-lg p-4">
              <h4 class="flex items-center text-gray-700 font-medium mb-3">
                <i class="el-icon-time text-orange-500 mr-2"></i> 用药计划
              </h4>
              <div class="space-y-2">
                <div class="flex">
                  <span class="text-gray-500 w-20">剂量：</span>
                  <span class="text-gray-800">{{ currentMedication.dosage }}</span>
                </div>
                <div class="flex">
                  <span class="text-gray-500 w-20">频率：</span>
                  <span class="text-gray-800">{{ currentMedication.frequency }}</span>
                </div>
                <div class="flex">
                  <span class="text-gray-500 w-20">与餐：</span>
                  <span class="text-gray-800">{{ getMealRelationText(currentMedication.mealRelation) }}</span>
                </div>
              </div>
            </div>
            
            <div class="bg-gray-50 rounded-lg p-4">
              <h4 class="flex items-center text-gray-700 font-medium mb-3">
                <i class="el-icon-date text-purple-500 mr-2"></i> 时间信息
              </h4>
              <div class="space-y-2">
                <div class="flex">
                  <span class="text-gray-500 w-20">开始：</span>
                  <span class="text-gray-800">{{ formatDate(currentMedication.startDate) }}</span>
                </div>
                <div class="flex">
                  <span class="text-gray-500 w-20">结束：</span>
                  <span class="text-gray-800">{{ currentMedication.endDate ? formatDate(currentMedication.endDate) : '长期用药' }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
        
        <div v-if="currentMedication.administrationTimes" class="mb-6">
          <h4 class="font-medium text-gray-700 mb-2">用药时间</h4>
          <div class="flex flex-wrap gap-2">
            <el-tag
              v-for="time in currentMedication.administrationTimes.split(',')"
              :key="time"
              size="medium"
              type="info"
            >
              {{ time.trim() }}
            </el-tag>
          </div>
        </div>
        
        <div v-if="currentMedication.purpose" class="mb-6">
          <h4 class="font-medium text-gray-700 mb-2">用药目的</h4>
          <p class="text-gray-600 bg-gray-50 p-3 rounded">{{ currentMedication.purpose }}</p>
        </div>
        
        <div v-if="currentMedication.specialInstructions" class="mb-6">
          <h4 class="font-medium text-gray-700 mb-2">特殊说明</h4>
          <p class="text-gray-600 bg-gray-50 p-3 rounded">{{ currentMedication.specialInstructions }}</p>
        </div>
      </div>
      <div slot="footer" class="flex justify-end space-x-3">
        <el-button @click="detailDialogVisible = false" icon="el-icon-close">关 闭</el-button>
        <el-button type="primary" @click="handleEdit(currentMedication)" icon="el-icon-edit">编辑信息</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "UserMedicationManage",
  data() {
    return {
      // 搜索表单
      searchForm: {
        medicationName: "",
        status: "",
        medicationType: "",
      },
      // 表格数据
      tableData: [],
      loading: false,
      total: 0,
      pageNum: 1,
      pageSize: 10,
      
      // 用户列表
      userList: [],
      
      // 选中项
      selection: [],
      
      // 对话框
      dialogVisible: false,
      dialogTitle: "添加药物",
      isEdit: false,
      submitLoading: false,
      
      // 详情对话框
      detailDialogVisible: false,
      currentMedication: null,
      
      // 表单
      form: {
        id: null,
        userId: null,
        medicationName: "",
        medicationType: "",
        dosage: "",
        frequency: "",
        administrationTimes: "",
        mealRelation: "WITH_MEAL",
        startDate: "",
        endDate: "",
        prescribedBy: "",
        prescriptionDate: "",
        purpose: "",
        specialInstructions: "",
        status: "ACTIVE"
      },
      
      // 表单验证规则
      rules: {
        userId: [
          { required: true, message: "请选择用户", trigger: "change" }
        ],
        medicationName: [
          { required: true, message: "请输入药物名称", trigger: "blur" }
        ],
        medicationType: [
          { required: true, message: "请选择药物类型", trigger: "change" }
        ],
        dosage: [
          { required: true, message: "请输入单次剂量", trigger: "blur" }
        ],
        frequency: [
          { required: true, message: "请输入用药频率", trigger: "blur" }
        ],
        startDate: [
          { required: true, message: "请选择开始日期", trigger: "change" }
        ]
      }
    };
  },
  created() {
    this.fetchData();
    this.fetchUserList();
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
      if (this.searchForm.medicationName) {
        params.medicationName = this.searchForm.medicationName;
      }
      if (this.searchForm.status) {
        params.status = this.searchForm.status;
      }
      if (this.searchForm.medicationType) {
        params.medicationType = this.searchForm.medicationType;
      }
      
      this.$request.get("/user-medication/selectPage", { params })
        .then(res => {
          if (res.code === '200') {
            this.tableData = res.data.list || [];
            this.total = res.data.total || 0;
          } else {
            this.$message.error(res.msg || "获取药物数据失败");
          }
        })
        .catch(() => {
          this.$message.error("获取药物数据异常");
        })
        .finally(() => {
          this.loading = false;
        });
    },
    
    // 获取用户列表
    fetchUserList() {
      this.$request.get("/user/selectAll")
        .then(res => {
          if (res.code === '200') {
            this.userList = res.data || [];
          }
        })
        .catch(() => {
          console.error("获取用户列表失败");
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
      this.$message.success("已重置搜索条件，显示所有药物数据");
    },
    
    // 查询全部药物
    queryAllMedications() {
      this.$refs.searchForm.resetFields();
      this.pageNum = 1;
      this.fetchData();
      this.$message.success("已显示所有药物数据");
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
    
    // 添加药物
    handleAdd() {
      this.dialogTitle = "添加药物";
      this.isEdit = false;
      this.form = {
        id: null,
        userId: null,
        medicationName: "",
        medicationType: "",
        dosage: "",
        frequency: "",
        administrationTimes: "",
        mealRelation: "WITH_MEAL",
        startDate: "",
        endDate: "",
        prescribedBy: "",
        prescriptionDate: "",
        purpose: "",
        specialInstructions: "",
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
    
    // 编辑药物
    handleEdit(row) {
      this.dialogTitle = "编辑药物";
      this.isEdit = true;
      this.form = JSON.parse(JSON.stringify(row));
      
      // 转换日期格式
      if (this.form.startDate) {
        this.form.startDate = new Date(this.form.startDate);
      }
      if (this.form.endDate) {
        this.form.endDate = new Date(this.form.endDate);
      }
      if (this.form.prescriptionDate) {
        this.form.prescriptionDate = new Date(this.form.prescriptionDate);
      }
      
      this.dialogVisible = true;
      this.detailDialogVisible = false;
      
      // 清除验证
      this.$nextTick(() => {
        if (this.$refs.form) {
          this.$refs.form.clearValidate();
        }
      });
    },
    
    // 查看详情
    handleDetail(row) {
      this.currentMedication = row;
      this.detailDialogVisible = true;
    },
    
    // 停用药物
    handleStop(row) {
      this.$confirm(`确定要停用"${row.medicationName}"吗？`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        this.$request.put(`/user-medication/stop/${row.id}`, {}, {
          params: { stopReason: '管理员停药' }
        })
          .then(res => {
            if (res.code === '200') {
              this.$message.success("药物已停用");
              this.fetchData();
            } else {
              this.$message.error(res.msg || "停用失败");
            }
          })
          .catch(() => {
            this.$message.error("停用药物请求异常");
          });
      }).catch(() => {});
    },
    
    // 保存药物
    saveMedication() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.submitLoading = true;
          
          // 准备数据
          const medicationData = { ...this.form };
          
          // 格式化日期
          if (medicationData.startDate) {
            medicationData.startDate = this.formatDateForAPI(medicationData.startDate);
          }
          if (medicationData.endDate) {
            medicationData.endDate = this.formatDateForAPI(medicationData.endDate);
          }
          if (medicationData.prescriptionDate) {
            medicationData.prescriptionDate = this.formatDateForAPI(medicationData.prescriptionDate);
          }
          
          const request = this.isEdit 
            ? this.$request.put("/user-medication/update", medicationData)
            : this.$request.post("/user-medication/add", medicationData);
            
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
              this.$message.error(this.isEdit ? "编辑药物请求异常" : "添加药物请求异常");
            })
            .finally(() => {
              this.submitLoading = false;
            });
        }
      });
    },
    
    // 删除药物
    handleDelete(row) {
      this.$confirm("确定删除该药物记录吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        this.$request.delete(`/user-medication/delete/${row.id}`)
          .then(res => {
            if (res.code === '200') {
              this.$message.success("删除成功");
              this.fetchData();
            } else {
              this.$message.error(res.msg || "删除失败");
            }
          })
          .catch(() => {
            this.$message.error("删除药物请求异常");
          });
      }).catch(() => {});
    },
    
    // 批量删除
    handleBatchDelete() {
      if (this.selection.length === 0) {
        this.$message.warning("请选择要删除的药物记录");
        return;
      }
      
      this.$confirm(`确定删除选中的 ${this.selection.length} 个药物记录吗?`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        const ids = this.selection.map(item => item.id);
        
        this.$request.delete("/user-medication/delete/batch", { data: ids })
          .then(res => {
            if (res.code === '200') {
              this.$message.success("批量删除成功");
              this.fetchData();
            } else {
              this.$message.error(res.msg || "批量删除失败");
            }
          })
          .catch(() => {
            this.$message.error("批量删除药物请求异常");
          });
      }).catch(() => {});
    },
    
    // 用户选择变化
    // handleUserChange(userId) {
    //   // 可以在这里添加额外的逻辑
    // },
    
    // 获取用户显示名称
    getUserDisplayName(row) {
      if (row.userName && row.userRealName) {
        return `${row.userRealName} (${row.userName})`;
      }
      return row.userName || row.userRealName || `用户ID: ${row.userId}`;
    },
    
    // 格式化日期
    formatDate(date) {
      if (!date) return '';
      return new Date(date).toLocaleDateString('zh-CN');
    },
    
    // 格式化日期为API需要的格式
    formatDateForAPI(date) {
      if (!date) return null;
      if (typeof date === 'string') return date;
      const d = new Date(date);
      return d.getFullYear() + '-' + 
             String(d.getMonth() + 1).padStart(2, '0') + '-' + 
             String(d.getDate()).padStart(2, '0');
    },
    
    // 获取药物类型文本
    getMedicationTypeText(type) {
      const typeMap = {
        'CARDIOVASCULAR': '心血管',
        'DIABETES': '糖尿病',
        'ANALGESIC': '止痛',
        'ANTIBIOTIC': '抗生素',
        'SUPPLEMENT': '补充剂'
      };
      return typeMap[type] || type;
    },
    
    // 获取药物类型颜色
    getMedicationTypeColor(type) {
      const colorMap = {
        'CARDIOVASCULAR': 'danger',
        'DIABETES': 'warning',
        'ANALGESIC': 'success',
        'ANTIBIOTIC': 'info',
        'SUPPLEMENT': 'primary'
      };
      return colorMap[type] || 'info';
    },
    
    // 获取状态文本
    getStatusText(status) {
      const statusMap = {
        'ACTIVE': '进行中',
        'COMPLETED': '已完成',
        'STOPPED': '已停药'
      };
      return statusMap[status] || status;
    },
    
    // 获取状态颜色
    getStatusColor(status) {
      const colorMap = {
        'ACTIVE': 'success',
        'COMPLETED': 'info',
        'STOPPED': 'warning'
      };
      return colorMap[status] || 'info';
    },
    
    // 获取与餐关系文本
    getMealRelationText(relation) {
      const relationMap = {
        'BEFORE_MEAL': '餐前',
        'WITH_MEAL': '餐中',
        'AFTER_MEAL': '餐后',
        'EMPTY_STOMACH': '空腹'
      };
      return relationMap[relation] || relation;
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