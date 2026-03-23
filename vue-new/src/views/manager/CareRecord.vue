<template>
  <div>
    <!-- 功能说明 -->
    <div class="bg-white rounded-lg shadow-sm p-6 mb-5">
      <div class="flex items-center">
        <i class="el-icon-notebook-2 text-3xl text-slate-600 mr-3"></i>
        <div>
          <h2 class="text-xl font-bold text-gray-800 mb-1">护理记录管理</h2>
          <p class="text-gray-600">本模块用于记录护理人员的工作日志，包括生命体征监测、护理内容、饮食运动记录等，提供记录的增加、修改、删除和查询功能</p>
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
                v-model="searchForm.careType" 
                placeholder="请选择护理类型" 
                clearable
                size="small"
              >
                <el-option label="日常照顾" value="DAILY_CARE"></el-option>
                <el-option label="饮食护理" value="DIET"></el-option>
                <el-option label="运动护理" value="EXERCISE"></el-option>
                <el-option label="医疗护理" value="MEDICAL"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-select 
                v-model="searchForm.moodStatus" 
                placeholder="请选择情绪状态" 
                clearable
                size="small"
              >
                <el-option label="良好" value="GOOD"></el-option>
                <el-option label="一般" value="NORMAL"></el-option>
                <el-option label="较差" value="POOR"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-date-picker
                v-model="searchForm.recordDate"
                type="date"
                placeholder="选择记录日期"
                size="small"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd">
              </el-date-picker>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSearch" icon="el-icon-search" size="small" class="mr-2">查询</el-button>
              <el-button @click="resetSearch" icon="el-icon-refresh" size="small" class="mr-2">重置</el-button>
              <el-button type="success" @click="queryAllRecords" icon="el-icon-menu" size="small">查询全部</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div class="w-full lg:w-auto flex justify-end space-x-3">
          <el-button type="primary" @click="handleAdd" icon="el-icon-plus" size="small">添加护理记录</el-button>
          <el-button type="danger" @click="handleBatchDelete" :disabled="selection.length === 0" icon="el-icon-delete" size="small">
            批量删除
          </el-button>
        </div>
      </div>
    </div>

    <!-- 护理记录表格 -->
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
        <el-table-column label="记录日期" width="120">
          <template slot-scope="scope">
            <div class="flex items-center">
              <i class="el-icon-date text-gray-400 mr-1"></i>
              <span>{{ scope.row.recordDate }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="护理类型" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getCareTypeTagType(scope.row.careType)" size="small">
              {{ getCareTypeName(scope.row.careType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="careContent" label="护理内容" min-width="200" show-overflow-tooltip></el-table-column>
        <el-table-column label="生命体征" width="150">
          <template slot-scope="scope">
            <div class="text-sm">
              <div v-if="scope.row.bloodPressure">血压: {{ scope.row.bloodPressure }}</div>
              <div v-if="scope.row.heartRate">心率: {{ scope.row.heartRate }}</div>
              <div v-if="scope.row.temperature">体温: {{ scope.row.temperature }}°C</div>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="情绪状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getMoodStatusTagType(scope.row.moodStatus)" size="small">
              {{ getMoodStatusName(scope.row.moodStatus) }}
            </el-tag>
          </template>
        </el-table-column>
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

    <!-- 添加/编辑护理记录对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="1000px" :close-on-click-modal="false" custom-class="rounded-lg">
      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="8">
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
          <el-col :span="8">
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
          <el-col :span="8">
            <el-form-item label="记录日期" prop="recordDate">
              <el-date-picker
                v-model="form.recordDate"
                type="date"
                placeholder="选择记录日期"
                style="width: 100%"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="护理类型" prop="careType">
              <el-select v-model="form.careType" placeholder="请选择护理类型" style="width: 100%">
                <el-option label="日常照顾" value="DAILY_CARE"></el-option>
                <el-option label="饮食护理" value="DIET"></el-option>
                <el-option label="运动护理" value="EXERCISE"></el-option>
                <el-option label="医疗护理" value="MEDICAL"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="情绪状态" prop="moodStatus">
              <el-select v-model="form.moodStatus" placeholder="请选择情绪状态" style="width: 100%">
                <el-option label="良好" value="GOOD"></el-option>
                <el-option label="一般" value="NORMAL"></el-option>
                <el-option label="较差" value="POOR"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="护理安排">
              <el-select v-model="form.careAssignmentId" placeholder="选择护理安排（可选）" style="width: 100%" clearable>
                <el-option
                  v-for="assignment in careAssignmentOptions"
                  :key="assignment.id"
                  :label="assignment.workContent"
                  :value="assignment.id">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <div class="bg-gray-50 rounded-lg p-4 mb-4">
          <h4 class="text-gray-700 font-medium mb-3">
            <i class="el-icon-first-aid-kit text-slate-600 mr-2"></i>生命体征
          </h4>
          <el-row :gutter="20">
            <el-col :span="8">
              <el-form-item label="血压">
                <el-input v-model="form.bloodPressure" placeholder="如：120/80"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="心率">
                <el-input v-model="form.heartRate" placeholder="如：72"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="体温">
                <el-input v-model="form.temperature" placeholder="如：36.5"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </div>
        
        <el-form-item label="护理内容" prop="careContent">
          <el-input v-model="form.careContent" type="textarea" :rows="3" placeholder="请输入护理内容详情"></el-input>
        </el-form-item>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="饮食记录">
              <el-input v-model="form.dietRecord" type="textarea" :rows="3" placeholder="请输入饮食记录（可选）"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="运动记录">
              <el-input v-model="form.exerciseRecord" type="textarea" :rows="3" placeholder="请输入运动记录（可选）"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="异常情况">
          <el-input v-model="form.abnormalSituation" type="textarea" :rows="2" placeholder="请输入异常情况（可选）"></el-input>
        </el-form-item>
        
        <el-form-item label="备注">
          <el-input v-model="form.remarks" type="textarea" :rows="2" placeholder="请输入备注（可选）"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="flex justify-end space-x-3">
        <el-button @click="dialogVisible = false" icon="el-icon-close">取 消</el-button>
        <el-button type="primary" @click="saveRecord" :loading="submitLoading" icon="el-icon-check">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 护理记录详情对话框 -->
    <el-dialog title="护理记录详情" :visible.sync="detailDialogVisible" width="900px" custom-class="rounded-lg">
      <div v-if="currentRecord" class="px-5">
        <div class="flex items-center mb-6">
          <div class="w-16 h-16 bg-slate-50 rounded-lg flex items-center justify-center mr-4">
            <i class="el-icon-notebook-2 text-2xl text-slate-600"></i>
          </div>
          <div>
            <h3 class="text-xl font-bold text-gray-800 mb-1">护理记录详情</h3>
            <p class="text-gray-500">{{ getCareTypeName(currentRecord.careType) }} · {{ getMoodStatusName(currentRecord.moodStatus) }}</p>
          </div>
        </div>
        
        <el-divider></el-divider>
        
        <div class="space-y-6">
          <div class="bg-gray-50 rounded-lg p-4">
            <h4 class="flex items-center text-gray-700 font-medium mb-3">
              <i class="el-icon-user text-slate-600 mr-2"></i> 基础信息
            </h4>
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div class="flex">
                <span class="text-gray-500 w-24">老年人:</span>
                <span class="text-gray-800">{{ getUserName(currentRecord.userId) }}</span>
              </div>
              <div class="flex">
                <span class="text-gray-500 w-24">护理人员:</span>
                <span class="text-gray-800">{{ getNurseName(currentRecord.nurseId) }}</span>
              </div>
              <div class="flex">
                <span class="text-gray-500 w-24">记录日期:</span>
                <span class="text-gray-800">{{ currentRecord.recordDate }}</span>
              </div>
              <div class="flex">
                <span class="text-gray-500 w-24">护理类型:</span>
                <span class="text-gray-800">{{ getCareTypeName(currentRecord.careType) }}</span>
              </div>
            </div>
          </div>
          
          <div class="bg-gray-50 rounded-lg p-4">
            <h4 class="flex items-center text-gray-700 font-medium mb-3">
              <i class="el-icon-first-aid-kit text-slate-600 mr-2"></i> 生命体征
            </h4>
            <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
              <div class="flex">
                <span class="text-gray-500 w-16">血压:</span>
                <span class="text-gray-800">{{ currentRecord.bloodPressure || '未记录' }}</span>
              </div>
              <div class="flex">
                <span class="text-gray-500 w-16">心率:</span>
                <span class="text-gray-800">{{ currentRecord.heartRate || '未记录' }}</span>
              </div>
              <div class="flex">
                <span class="text-gray-500 w-16">体温:</span>
                <span class="text-gray-800">{{ currentRecord.temperature ? currentRecord.temperature + '°C' : '未记录' }}</span>
              </div>
            </div>
          </div>
          
          <div v-if="currentRecord.careContent" class="bg-gray-50 rounded-lg p-4">
            <h4 class="flex items-center text-gray-700 font-medium mb-3">
              <i class="el-icon-document text-slate-600 mr-2"></i> 护理内容
            </h4>
            <p class="text-gray-800 leading-relaxed">{{ currentRecord.careContent }}</p>
          </div>
          
          <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div v-if="currentRecord.dietRecord" class="bg-gray-50 rounded-lg p-4">
              <h4 class="flex items-center text-gray-700 font-medium mb-3">
                <i class="el-icon-food text-slate-600 mr-2"></i> 饮食记录
              </h4>
              <p class="text-gray-800 leading-relaxed">{{ currentRecord.dietRecord }}</p>
            </div>
            
            <div v-if="currentRecord.exerciseRecord" class="bg-gray-50 rounded-lg p-4">
              <h4 class="flex items-center text-gray-700 font-medium mb-3">
                <i class="el-icon-bicycle text-slate-600 mr-2"></i> 运动记录
              </h4>
              <p class="text-gray-800 leading-relaxed">{{ currentRecord.exerciseRecord }}</p>
            </div>
          </div>
          
          <div class="bg-gray-50 rounded-lg p-4">
            <h4 class="flex items-center text-gray-700 font-medium mb-3">
              <i class="el-icon-star-on text-slate-600 mr-2"></i> 状态评估
            </h4>
            <div class="grid grid-cols-1 gap-4">
              <div class="flex">
                <span class="text-gray-500 w-24">情绪状态:</span>
                <span class="text-gray-800">{{ getMoodStatusName(currentRecord.moodStatus) }}</span>
              </div>
              <div v-if="currentRecord.abnormalSituation" class="flex">
                <span class="text-gray-500 w-24">异常情况:</span>
                <span class="text-gray-800">{{ currentRecord.abnormalSituation }}</span>
              </div>
              <div v-if="currentRecord.remarks" class="flex">
                <span class="text-gray-500 w-24">备注:</span>
                <span class="text-gray-800">{{ currentRecord.remarks }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div slot="footer" class="flex justify-end space-x-3">
        <el-button @click="detailDialogVisible = false" icon="el-icon-close">关 闭</el-button>
        <el-button type="primary" @click="handleEdit(currentRecord)" icon="el-icon-edit">编辑记录</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "CareRecordManage",
  data() {
    return {
      // 搜索表单
      searchForm: {
        userId: "",
        nurseId: "",
        careType: "",
        moodStatus: "",
        recordDate: ""
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
      userOptions: [],           // 老年人选项
      nurseOptions: [],          // 护理人员选项
      careAssignmentOptions: [], // 护理安排选项
      
      // 对话框
      dialogVisible: false,
      dialogTitle: "添加护理记录",
      isEdit: false,
      submitLoading: false,
      
      // 详情对话框
      detailDialogVisible: false,
      currentRecord: null,
      
      // 表单
      form: {
        id: null,
        userId: "",
        nurseId: "",
        careAssignmentId: "",
        recordDate: "",
        careType: "DAILY_CARE",
        careContent: "",
        bloodPressure: "",
        heartRate: "",
        temperature: "",
        dietRecord: "",
        exerciseRecord: "",
        moodStatus: "GOOD",
        abnormalSituation: "",
        remarks: ""
      },
      
      // 表单验证规则
      rules: {
        userId: [
          { required: true, message: "请选择老年人", trigger: "change" }
        ],
        nurseId: [
          { required: true, message: "请选择护理人员", trigger: "change" }
        ],
        recordDate: [
          { required: true, message: "请选择记录日期", trigger: "change" }
        ],
        careType: [
          { required: true, message: "请选择护理类型", trigger: "change" }
        ],
        careContent: [
          { required: true, message: "请输入护理内容", trigger: "blur" }
        ],
        moodStatus: [
          { required: true, message: "请选择情绪状态", trigger: "change" }
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
      if (this.searchForm.careType) params.careType = this.searchForm.careType;
      if (this.searchForm.moodStatus) params.moodStatus = this.searchForm.moodStatus;
      if (this.searchForm.recordDate) params.recordDate = this.searchForm.recordDate;
      
      this.$request.get("/care-record/selectPage", { params })
        .then(res => {
          if (res.code === '200') {
            this.tableData = res.data.list || [];
            this.total = res.data.total || 0;
          } else {
            this.$message.error(res.msg || "获取护理记录数据失败");
          }
        })
        .catch(() => {
          this.$message.error("获取护理记录数据异常");
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
      
      // 获取护理安排选项
      this.$request.get("/care-assignment/selectAll")
        .then(res => {
          if (res.code === '200') {
            this.careAssignmentOptions = res.data || [];
          }
        })
        .catch(() => {
          console.error("获取护理安排选项失败");
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
      this.$message.success("已重置搜索条件，显示所有护理记录数据");
    },
    
    // 查询全部护理记录
    queryAllRecords() {
      this.$refs.searchForm.resetFields();
      this.pageNum = 1;
      this.fetchData();
      this.$message.success("已显示所有护理记录数据");
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
    
    // 添加护理记录
    handleAdd() {
      this.dialogTitle = "添加护理记录";
      this.isEdit = false;
      this.form = {
        id: null,
        userId: "",
        nurseId: "",
        careAssignmentId: "",
        recordDate: "",
        careType: "DAILY_CARE",
        careContent: "",
        bloodPressure: "",
        heartRate: "",
        temperature: "",
        dietRecord: "",
        exerciseRecord: "",
        moodStatus: "GOOD",
        abnormalSituation: "",
        remarks: ""
      };
      this.dialogVisible = true;
      
      // 清除验证
      this.$nextTick(() => {
        if (this.$refs.form) {
          this.$refs.form.clearValidate();
        }
      });
    },
    
    // 编辑护理记录
    handleEdit(row) {
      this.dialogTitle = "编辑护理记录";
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
      this.currentRecord = JSON.parse(JSON.stringify(row));
      this.detailDialogVisible = true;
    },
    
    // 保存护理记录（添加或更新）
    saveRecord() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.submitLoading = true;
          
          const request = this.isEdit 
            ? this.$request.put("/care-record/update", this.form)
            : this.$request.post("/care-record/add", this.form);
            
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
              this.$message.error(this.isEdit ? "编辑护理记录请求异常" : "添加护理记录请求异常");
            })
            .finally(() => {
              this.submitLoading = false;
            });
        }
      });
    },
    
    // 删除护理记录
    handleDelete(row) {
      this.$confirm("确定删除该护理记录吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        this.$request.delete(`/care-record/delete/${row.id}`)
          .then(res => {
            if (res.code === '200') {
              this.$message.success("删除成功");
              this.fetchData();
            } else {
              this.$message.error(res.msg || "删除失败");
            }
          })
          .catch(() => {
            this.$message.error("删除护理记录请求异常");
          });
      }).catch(() => {});
    },
    
    // 批量删除
    handleBatchDelete() {
      if (this.selection.length === 0) {
        this.$message.warning("请选择要删除的护理记录");
        return;
      }
      
      this.$confirm(`确定删除选中的 ${this.selection.length} 个护理记录吗?`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        const ids = this.selection.map(item => item.id);
        
        this.$request.delete("/care-record/delete/batch", { data: ids })
          .then(res => {
            if (res.code === '200') {
              this.$message.success("批量删除成功");
              this.fetchData();
            } else {
              this.$message.error(res.msg || "批量删除失败");
            }
          })
          .catch(() => {
            this.$message.error("批量删除护理记录请求异常");
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

    // 获取护理类型名称
    getCareTypeName(careType) {
      switch (careType) {
        case 'DAILY_CARE':
          return '日常照顾';
        case 'DIET':
          return '饮食护理';
        case 'EXERCISE':
          return '运动护理';
        case 'MEDICAL':
          return '医疗护理';
        default:
          return '未知';
      }
    },
    
    // 获取护理类型标签类型
    getCareTypeTagType(careType) {
      switch (careType) {
        case 'DAILY_CARE':
          return 'info';
        case 'DIET':
          return 'warning';
        case 'EXERCISE':
          return 'success';
        case 'MEDICAL':
          return 'danger';
        default:
          return '';
      }
    },

    // 获取情绪状态名称
    getMoodStatusName(moodStatus) {
      switch (moodStatus) {
        case 'GOOD':
          return '良好';
        case 'NORMAL':
          return '一般';
        case 'POOR':
          return '较差';
        default:
          return '未知';
      }
    },
    
    // 获取情绪状态标签类型
    getMoodStatusTagType(moodStatus) {
      switch (moodStatus) {
        case 'GOOD':
          return 'success';
        case 'NORMAL':
          return 'warning';
        case 'POOR':
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