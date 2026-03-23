<template>
  <div class="medication-management">
    <!-- 页面头部 -->
    <div class="page-header mb-6">
      <div class="flex items-center justify-between">
        <div class="flex items-center">
          <div class="bg-gradient-to-r from-blue-500 to-blue-600 p-3 rounded-xl shadow-lg mr-4">
            <i class="el-icon-medicine text-white text-xl"></i>
          </div>
          <div>
            <h1 class="text-2xl font-bold text-gray-800 mb-1">药物管理</h1>
            <p class="text-gray-500 text-sm">科学用药，健康生活</p>
          </div>
        </div>
        <div class="flex space-x-3">
          <el-button type="primary" icon="el-icon-plus" @click="showAddDialog = true">
            添加药物
          </el-button>
          <el-button icon="el-icon-refresh" @click="loadMedicationList">
            刷新
          </el-button>
        </div>
      </div>
    </div>

    <!-- 统计卡片 -->
    <div class="grid grid-cols-1 md:grid-cols-4 gap-4 mb-6">
      <div class="bg-gradient-to-r from-green-400 to-green-500 rounded-xl p-6 text-white shadow-lg hover:shadow-xl transition-shadow">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-green-100 text-sm">进行中</p>
            <p class="text-2xl font-bold">{{ stats.active || 0 }}</p>
          </div>
          <i class="el-icon-check text-3xl text-green-200"></i>
        </div>
      </div>
      
      <div class="bg-gradient-to-r from-orange-400 to-orange-500 rounded-xl p-6 text-white shadow-lg hover:shadow-xl transition-shadow">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-orange-100 text-sm">今日用药</p>
            <p class="text-2xl font-bold">{{ stats.today || 0 }}</p>
          </div>
          <i class="el-icon-time text-3xl text-orange-200"></i>
        </div>
      </div>
      
      <div class="bg-gradient-to-r from-purple-400 to-purple-500 rounded-xl p-6 text-white shadow-lg hover:shadow-xl transition-shadow">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-purple-100 text-sm">药物种类</p>
            <p class="text-2xl font-bold">{{ stats.types || 0 }}</p>
          </div>
          <i class="el-icon-collection text-3xl text-purple-200"></i>
        </div>
      </div>
      
      <div class="bg-gradient-to-r from-red-400 to-red-500 rounded-xl p-6 text-white shadow-lg hover:shadow-xl transition-shadow">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-red-100 text-sm">需关注</p>
            <p class="text-2xl font-bold">{{ stats.attention || 0 }}</p>
          </div>
          <i class="el-icon-warning text-3xl text-red-200"></i>
        </div>
      </div>
    </div>

    <!-- 筛选和搜索 -->
    <div class="bg-white rounded-xl shadow-sm p-6 mb-6">
      <div class="flex flex-wrap items-center gap-4">
        <div class="flex items-center">
          <span class="text-gray-600 mr-2 font-medium">状态：</span>
          <el-select v-model="filters.status" placeholder="选择状态" clearable size="medium" style="width: 140px;">
            <el-option label="全部" value=""></el-option>
            <el-option label="进行中" value="ACTIVE"></el-option>
            <el-option label="已完成" value="COMPLETED"></el-option>
            <el-option label="已停药" value="STOPPED"></el-option>
          </el-select>
        </div>
        
        <div class="flex items-center">
          <span class="text-gray-600 mr-2 font-medium">类型：</span>
          <el-select v-model="filters.type" placeholder="选择类型" clearable size="medium" style="width: 160px;">
            <el-option label="全部" value=""></el-option>
            <el-option label="心血管" value="CARDIOVASCULAR"></el-option>
            <el-option label="糖尿病" value="DIABETES"></el-option>
            <el-option label="止痛" value="ANALGESIC"></el-option>
            <el-option label="抗生素" value="ANTIBIOTIC"></el-option>
            <el-option label="补充剂" value="SUPPLEMENT"></el-option>
          </el-select>
        </div>
        
        <div class="flex items-center flex-1 max-w-md">
          <span class="text-gray-600 mr-2 font-medium">搜索：</span>
          <el-input
            v-model="filters.keyword"
            placeholder="搜索药物名称或医生"
            prefix-icon="el-icon-search"
            size="medium"
            clearable
          ></el-input>
        </div>
        
        <el-button type="primary" icon="el-icon-search" @click="searchMedications">
          搜索
        </el-button>
        <el-button icon="el-icon-refresh-left" @click="resetFilters">
          重置
        </el-button>
      </div>
    </div>

    <!-- 药物列表 -->
    <div class="space-y-4" v-loading="loading">
      <div
        v-for="medication in medicationList"
        :key="medication.id"
        class="bg-white rounded-xl shadow-sm hover:shadow-md transition-shadow p-6 border border-gray-100"
      >
        <div class="flex items-start justify-between">
          <div class="flex-1">
            <div class="flex items-center mb-3">
              <div class="flex items-center">
                <div :class="[
                  'w-3 h-3 rounded-full mr-3',
                  medication.status === 'ACTIVE' ? 'bg-green-500' : 
                  medication.status === 'COMPLETED' ? 'bg-blue-500' : 'bg-gray-400'
                ]"></div>
                <h3 class="text-lg font-semibold text-gray-800 mr-3">{{ medication.medicationName }}</h3>
                <el-tag 
                  :type="getMedicationTypeColor(medication.medicationType)"
                  size="small"
                  class="mr-2"
                >
                  {{ getMedicationTypeText(medication.medicationType) }}
                </el-tag>
                <el-tag 
                  :type="getStatusColor(medication.status)"
                  size="small"
                >
                  {{ getStatusText(medication.status) }}
                </el-tag>
              </div>
            </div>
            
            <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4 mb-4">
              <div class="flex items-center text-sm text-gray-600">
                <i class="el-icon-medicine mr-2 text-blue-500"></i>
                <span class="font-medium mr-1">剂量：</span>
                <span>{{ medication.dosage }}</span>
              </div>
              
              <div class="flex items-center text-sm text-gray-600">
                <i class="el-icon-time mr-2 text-green-500"></i>
                <span class="font-medium mr-1">频率：</span>
                <span>{{ medication.frequency }}</span>
              </div>
              
              <div class="flex items-center text-sm text-gray-600">
                <i class="el-icon-tableware mr-2 text-orange-500"></i>
                <span class="font-medium mr-1">用餐：</span>
                <span>{{ getMealRelationText(medication.mealRelation) }}</span>
              </div>
              
              <div class="flex items-center text-sm text-gray-600">
                <i class="el-icon-user mr-2 text-purple-500"></i>
                <span class="font-medium mr-1">医生：</span>
                <span>{{ medication.prescribedBy || '未记录' }}</span>
              </div>
            </div>
            
            <!-- 用药时间 -->
            <div v-if="medication.administrationTimes" class="mb-3">
              <div class="flex items-center mb-2">
                <i class="el-icon-alarm-clock mr-2 text-blue-500"></i>
                <span class="text-sm font-medium text-gray-700">用药时间：</span>
              </div>
              <div class="flex flex-wrap gap-2">
                <el-tag
                  v-for="time in medication.administrationTimes.split(',')"
                  :key="time"
                  size="small"
                  type="info"
                  class="bg-blue-50 text-blue-600 border-blue-200"
                >
                  {{ time.trim() }}
                </el-tag>
              </div>
            </div>
            
            <!-- 用药周期 -->
            <div class="flex items-center text-sm text-gray-600 mb-2">
              <i class="el-icon-date mr-2 text-indigo-500"></i>
              <span class="font-medium mr-1">周期：</span>
              <span>{{ formatDate(medication.startDate) }}</span>
              <span class="mx-2">-</span>
              <span>{{ medication.endDate ? formatDate(medication.endDate) : '长期' }}</span>
            </div>
            
            <!-- 用药目的 -->
            <div v-if="medication.purpose" class="text-sm text-gray-600 mb-2">
              <i class="el-icon-info mr-2 text-teal-500"></i>
              <span class="font-medium mr-1">目的：</span>
              <span>{{ medication.purpose }}</span>
            </div>
          </div>
          
          <div class="flex flex-col space-y-2 ml-4">
            <el-button 
              size="small" 
              type="primary" 
              plain 
              icon="el-icon-view"
              @click="viewMedication(medication)"
            >
              详情
            </el-button>
            <el-button 
              size="small" 
              type="warning" 
              plain 
              icon="el-icon-edit"
              @click="editMedication(medication)"
            >
              编辑
            </el-button>
            <el-button 
              v-if="medication.status === 'ACTIVE'"
              size="small" 
              type="danger" 
              plain 
              icon="el-icon-remove"
              @click="stopMedication(medication)"
            >
              停药
            </el-button>
          </div>
        </div>
      </div>
    </div>

    <!-- 空状态 -->
    <div v-if="medicationList.length === 0" class="text-center py-16">
      <div class="mb-4">
        <i class="el-icon-medicine text-6xl text-gray-300"></i>
      </div>
      <h3 class="text-lg font-medium text-gray-500 mb-2">暂无药物记录</h3>
      <p class="text-gray-400 mb-4">点击上方"添加药物"按钮开始管理您的用药信息</p>
      <el-button type="primary" icon="el-icon-plus" @click="showAddDialog = true">
        添加药物
      </el-button>
    </div>

    <!-- 分页 -->
    <div v-if="total > 0" class="flex justify-center mt-8">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        background
      >
      </el-pagination>
    </div>

    <!-- 添加/编辑药物对话框 -->
    <el-dialog
      :title="editingMedication ? '编辑药物' : '添加药物'"
      :visible.sync="showAddDialog"
      width="50%"
      :close-on-click-modal="false"
    >
      <el-form ref="medicationForm" :model="medicationForm" :rules="medicationRules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="药物名称" prop="medicationName">
              <el-input v-model="medicationForm.medicationName" placeholder="请输入药物名称"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="药物类型" prop="medicationType">
              <el-select v-model="medicationForm.medicationType" placeholder="选择药物类型" style="width: 100%;">
                <el-option label="心血管" value="CARDIOVASCULAR"></el-option>
                <el-option label="糖尿病" value="DIABETES"></el-option>
                <el-option label="止痛" value="ANALGESIC"></el-option>
                <el-option label="抗生素" value="ANTIBIOTIC"></el-option>
                <el-option label="补充剂" value="SUPPLEMENT"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="单次剂量" prop="dosage">
              <el-input v-model="medicationForm.dosage" placeholder="如：1片、5ml"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="用药频率" prop="frequency">
              <el-input v-model="medicationForm.frequency" placeholder="如：每日3次"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="用药时间" prop="administrationTimes">
              <el-input v-model="medicationForm.administrationTimes" placeholder="如：08:00,12:00,18:00"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="与餐关系" prop="mealRelation">
              <el-select v-model="medicationForm.mealRelation" placeholder="选择与餐关系" style="width: 100%;">
                <el-option label="餐前" value="BEFORE_MEAL"></el-option>
                <el-option label="餐中" value="WITH_MEAL"></el-option>
                <el-option label="餐后" value="AFTER_MEAL"></el-option>
                <el-option label="空腹" value="EMPTY_STOMACH"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="开始日期" prop="startDate">
              <el-date-picker
                v-model="medicationForm.startDate"
                type="date"
                placeholder="选择开始日期"
                style="width: 100%;"
              ></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束日期">
              <el-date-picker
                v-model="medicationForm.endDate"
                type="date"
                placeholder="选择结束日期（可选）"
                style="width: 100%;"
              ></el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="开药医生">
              <el-input v-model="medicationForm.prescribedBy" placeholder="请输入医生姓名"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="开药日期">
              <el-date-picker
                v-model="medicationForm.prescriptionDate"
                type="date"
                placeholder="选择开药日期"
                style="width: 100%;"
              ></el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="用药目的">
          <el-input
            v-model="medicationForm.purpose"
            type="textarea"
            :rows="2"
            placeholder="请输入用药目的"
          ></el-input>
        </el-form-item>
        
        <el-form-item label="特殊说明">
          <el-input
            v-model="medicationForm.specialInstructions"
            type="textarea"
            :rows="2"
            placeholder="请输入特殊用药说明"
          ></el-input>
        </el-form-item>
      </el-form>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="showAddDialog = false">取消</el-button>
        <el-button type="primary" @click="saveMedication">确定</el-button>
      </div>
    </el-dialog>

    <!-- 药物详情对话框 -->
    <el-dialog
      title="药物详情"
      :visible.sync="showDetailDialog"
      width="60%"
    >
      <div v-if="selectedMedication" class="medication-detail">
        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div class="space-y-4">
            <div class="flex items-center">
              <span class="font-medium text-gray-700 w-20">药物名称：</span>
              <span>{{ selectedMedication.medicationName }}</span>
            </div>
            <div class="flex items-center">
              <span class="font-medium text-gray-700 w-20">药物类型：</span>
              <el-tag :type="getMedicationTypeColor(selectedMedication.medicationType)" size="small">
                {{ getMedicationTypeText(selectedMedication.medicationType) }}
              </el-tag>
            </div>
            <div class="flex items-center">
              <span class="font-medium text-gray-700 w-20">单次剂量：</span>
              <span>{{ selectedMedication.dosage }}</span>
            </div>
            <div class="flex items-center">
              <span class="font-medium text-gray-700 w-20">用药频率：</span>
              <span>{{ selectedMedication.frequency }}</span>
            </div>
            <div class="flex items-center">
              <span class="font-medium text-gray-700 w-20">与餐关系：</span>
              <span>{{ getMealRelationText(selectedMedication.mealRelation) }}</span>
            </div>
          </div>
          
          <div class="space-y-4">
            <div class="flex items-center">
              <span class="font-medium text-gray-700 w-20">用药状态：</span>
              <el-tag :type="getStatusColor(selectedMedication.status)" size="small">
                {{ getStatusText(selectedMedication.status) }}
              </el-tag>
            </div>
            <div class="flex items-center">
              <span class="font-medium text-gray-700 w-20">开药医生：</span>
              <span>{{ selectedMedication.prescribedBy || '未记录' }}</span>
            </div>
            <div class="flex items-center">
              <span class="font-medium text-gray-700 w-20">开始日期：</span>
              <span>{{ formatDate(selectedMedication.startDate) }}</span>
            </div>
            <div class="flex items-center">
              <span class="font-medium text-gray-700 w-20">结束日期：</span>
              <span>{{ selectedMedication.endDate ? formatDate(selectedMedication.endDate) : '长期用药' }}</span>
            </div>
            <div class="flex items-center">
              <span class="font-medium text-gray-700 w-20">开药日期：</span>
              <span>{{ selectedMedication.prescriptionDate ? formatDate(selectedMedication.prescriptionDate) : '未记录' }}</span>
            </div>
          </div>
        </div>
        
        <div v-if="selectedMedication.administrationTimes" class="mt-6">
          <h4 class="font-medium text-gray-700 mb-2">用药时间</h4>
          <div class="flex flex-wrap gap-2">
            <el-tag
              v-for="time in selectedMedication.administrationTimes.split(',')"
              :key="time"
              size="medium"
              type="info"
            >
              {{ time.trim() }}
            </el-tag>
          </div>
        </div>
        
        <div v-if="selectedMedication.purpose" class="mt-6">
          <h4 class="font-medium text-gray-700 mb-2">用药目的</h4>
          <p class="text-gray-600">{{ selectedMedication.purpose }}</p>
        </div>
        
        <div v-if="selectedMedication.specialInstructions" class="mt-6">
          <h4 class="font-medium text-gray-700 mb-2">特殊说明</h4>
          <p class="text-gray-600">{{ selectedMedication.specialInstructions }}</p>
        </div>
        
        <div v-if="selectedMedication.sideEffects" class="mt-6">
          <h4 class="font-medium text-gray-700 mb-2">副作用记录</h4>
          <p class="text-gray-600">{{ selectedMedication.sideEffects }}</p>
        </div>
        
        <div v-if="selectedMedication.remarks" class="mt-6">
          <h4 class="font-medium text-gray-700 mb-2">备注信息</h4>
          <p class="text-gray-600">{{ selectedMedication.remarks }}</p>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import request from '@/utils/request'

export default {
  name: 'FrontMedication',
  data() {
    return {
      // 药物列表数据
      medicationList: [],
      
      // 统计数据
      stats: {
        active: 0,
        today: 0,
        types: 0,
        attention: 0
      },
      
      // 当前用户信息
      user: JSON.parse(localStorage.getItem("xm-user") || "{}"),
      
      // 筛选条件
      filters: {
        status: '',
        type: '',
        keyword: ''
      },
      
      // 分页
      currentPage: 1,
      pageSize: 10,
      total: 0,
      loading: false,
      
      // 对话框状态
      showAddDialog: false,
      showDetailDialog: false,
      editingMedication: null,
      selectedMedication: null,
      
      // 表单数据
      medicationForm: {
        medicationName: '',
        medicationType: '',
        dosage: '',
        frequency: '',
        administrationTimes: '',
        mealRelation: 'WITH_MEAL',
        startDate: '',
        endDate: '',
        prescribedBy: '',
        prescriptionDate: '',
        purpose: '',
        specialInstructions: ''
      },
      
      // 表单验证规则
      medicationRules: {
        medicationName: [
          { required: true, message: '请输入药物名称', trigger: 'blur' }
        ],
        medicationType: [
          { required: true, message: '请选择药物类型', trigger: 'change' }
        ],
        dosage: [
          { required: true, message: '请输入单次剂量', trigger: 'blur' }
        ],
        frequency: [
          { required: true, message: '请输入用药频率', trigger: 'blur' }
        ],
        startDate: [
          { required: true, message: '请选择开始日期', trigger: 'change' }
        ]
      }
    };
  },
  
  created() {
    this.loadMedicationList();
    this.loadMedicationStats();
  },
  
  methods: {
    // 加载药物列表
    async loadMedicationList() {
      try {
        this.loading = true;
        const params = {
          pageNum: this.currentPage,
          pageSize: this.pageSize
        };
        
        // 添加筛选条件
        if (this.filters.status) {
          params.status = this.filters.status;
        }
        if (this.filters.type) {
          params.medicationType = this.filters.type;
        }
        if (this.filters.keyword) {
          params.medicationName = this.filters.keyword;
        }
        
        const response = await request.get(`/user-medication/selectPageByUserId/${this.user.id}`, { params });
        if (response.code === "200") {
          this.medicationList = response.data.list || [];
          this.total = response.data.total || 0;
        } else {
          this.$message.error(response.msg || '加载药物列表失败');
        }
      } catch (error) {
        console.error('加载药物列表失败:', error);
        this.$message.error('加载药物列表失败');
      } finally {
        this.loading = false;
      }
    },
    
    // 加载统计信息
    async loadMedicationStats() {
      try {
        const response = await request.get(`/user-medication/stats/${this.user.id}`);
        if (response.code === "200") {
          this.stats = response.data;
        } else {
          console.error('加载统计信息失败:', response.msg);
        }
      } catch (error) {
        console.error('加载统计信息失败:', error);
      }
    },
    
    // 搜索药物
    searchMedications() {
      this.currentPage = 1; // 重置到第一页
      this.loadMedicationList();
    },
    
    // 重置筛选条件
    resetFilters() {
      this.filters = {
        status: '',
        type: '',
        keyword: ''
      };
      this.searchMedications();
    },
    
    // 查看药物详情
    viewMedication(medication) {
      this.selectedMedication = medication;
      this.showDetailDialog = true;
    },
    
    // 编辑药物
    editMedication(medication) {
      this.editingMedication = medication;
      this.medicationForm = { ...medication };
      
      // 转换日期格式为Date对象，以便日期选择器正确显示
      if (this.medicationForm.startDate) {
        this.medicationForm.startDate = new Date(this.medicationForm.startDate);
      }
      if (this.medicationForm.endDate) {
        this.medicationForm.endDate = new Date(this.medicationForm.endDate);
      }
      if (this.medicationForm.prescriptionDate) {
        this.medicationForm.prescriptionDate = new Date(this.medicationForm.prescriptionDate);
      }
      
      this.showAddDialog = true;
    },
    
    // 停药
    stopMedication(medication) {
      this.$confirm(`确定要停用"${medication.medicationName}"吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await request.put(`/user-medication/stop/${medication.id}`, {}, {
            params: { stopReason: '用户主动停药' }
          });
          
          if (response.code === "200") {
            this.$message.success(`"${medication.medicationName}"已停用`);
            this.loadMedicationList();
            this.loadMedicationStats(); // 更新统计信息
          } else {
            this.$message.error(response.msg || '停药操作失败');
          }
        } catch (error) {
          console.error('停药失败:', error);
          this.$message.error('停药操作失败');
        }
      }).catch(() => {});
    },
    
    // 保存药物
    saveMedication() {
      this.$refs.medicationForm.validate(async (valid) => {
        if (valid) {
          try {
            // 准备数据
            const medicationData = { ...this.medicationForm };
            
            // 添加用户ID
            medicationData.userId = this.user.id;
            
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
            
            let response;
            if (this.editingMedication) {
              // 编辑
              medicationData.id = this.editingMedication.id;
              response = await request.put('/user-medication/update', medicationData);
            } else {
              // 新增
              response = await request.post('/user-medication/add', medicationData);
            }
            
            if (response.code === "200") {
              this.$message.success(this.editingMedication ? '药物信息已更新' : '药物添加成功');
              this.showAddDialog = false;
              this.resetForm();
              this.loadMedicationList();
              this.loadMedicationStats(); // 更新统计信息
            } else {
              this.$message.error(response.msg || '保存失败');
            }
          } catch (error) {
            console.error('保存药物失败:', error);
            this.$message.error('保存失败');
          }
        }
      });
    },
    
    // 重置表单
    resetForm() {
      this.editingMedication = null;
      this.medicationForm = {
        medicationName: '',
        medicationType: '',
        dosage: '',
        frequency: '',
        administrationTimes: '',
        mealRelation: 'WITH_MEAL',
        startDate: '',
        endDate: '',
        prescribedBy: '',
        prescriptionDate: '',
        purpose: '',
        specialInstructions: ''
      };
      this.$refs.medicationForm && this.$refs.medicationForm.resetFields();
    },
    
    // 分页事件
    handleSizeChange(val) {
      this.pageSize = val;
      this.loadMedicationList();
    },
    
    handleCurrentChange(val) {
      this.currentPage = val;
      this.loadMedicationList();
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
.medication-management {
  min-height: 100vh;
  background: #f8fafc;
}

.page-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  margin: -24px -24px 24px -24px;
  padding: 24px;
  border-radius: 12px;
  color: white;
}

.page-header h1 {
  color: white;
}

.page-header p {
  color: rgba(255, 255, 255, 0.8);
}

/* 卡片悬停效果 */
.bg-white:hover {
  transform: translateY(-2px);
  transition: all 0.3s ease;
}

/* 对话框样式优化 */
:deep(.el-dialog) {
  border-radius: 16px;
}

:deep(.el-dialog__header) {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 16px 16px 0 0;
  padding: 20px;
}

:deep(.el-dialog__title) {
  color: white;
  font-weight: 600;
}

:deep(.el-dialog__headerbtn .el-dialog__close) {
  color: white;
}

/* 表单样式优化 */
:deep(.el-form-item__label) {
  font-weight: 500;
  color: #374151;
}

/* 标签样式优化 */
:deep(.el-tag) {
  border-radius: 8px;
  font-weight: 500;
}

/* 分页样式优化 */
:deep(.el-pagination) {
  padding: 20px 0;
}

:deep(.el-pager li.active) {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 8px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .page-header {
    margin: -16px -16px 16px -16px;
    padding: 16px;
  }
  
  .grid {
    grid-template-columns: 1fr;
  }
}
</style>