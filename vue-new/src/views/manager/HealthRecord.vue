<template>
  <div>
    <!-- 功能说明 -->
    <div class="bg-white rounded-lg shadow-sm p-6 mb-5">
      <div class="flex items-center">
        <i class="el-icon-document-checked text-3xl text-blue-500 mr-3"></i>
        <div>
          <h2 class="text-xl font-bold text-gray-800 mb-1">健康档案管理</h2>
          <p class="text-gray-600">本模块用于管理系统用户健康档案，提供健康信息的查询、修改、删除和详情查看功能</p>
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
                placeholder="请输入用户姓名" 
                prefix-icon="el-icon-user" 
                clearable
                size="small"
              ></el-input>
            </el-form-item>
            <el-form-item>
              <el-select 
                v-model="searchForm.bloodType" 
                placeholder="请选择血型" 
                clearable
                size="small"
              >
                <el-option label="A" value="A"></el-option>
                <el-option label="B" value="B"></el-option>
                <el-option label="O" value="O"></el-option>
                <el-option label="AB" value="AB"></el-option>
                <el-option label="未知" value="未知"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-input 
                v-model="searchForm.chronicDisease" 
                placeholder="慢性疾病关键词" 
                prefix-icon="el-icon-warning-outline" 
                clearable
                size="small"
              ></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSearch" icon="el-icon-search" size="small" class="mr-2">查询</el-button>
              <el-button @click="resetSearch" icon="el-icon-refresh" size="small" class="mr-2">重置</el-button>
              <el-button type="success" @click="fetchData" icon="el-icon-menu" size="small">查询全部</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div class="w-full lg:w-auto flex justify-end space-x-3">
          <el-button type="primary" @click="handleAdd" icon="el-icon-plus" size="small">添加健康档案</el-button>
          <el-button type="danger" @click="handleBatchDelete" :disabled="selection.length === 0" icon="el-icon-delete" size="small">
            批量删除
          </el-button>
        </div>
      </div>
    </div>

    <!-- 健康档案表格 -->
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
        <el-table-column prop="userId" label="用户ID" width="100"></el-table-column>
        <el-table-column label="用户信息" min-width="180">
          <template slot-scope="scope">
            <div v-if="scope.row.user">
              <div class="flex items-center">
                <el-avatar size="small" :src="scope.row.user.avatar" class="mr-2"></el-avatar>
                <span class="font-medium">{{ scope.row.user.name }}</span>
              </div>
            </div>
            <span v-else class="text-gray-500">未找到用户信息</span>
          </template>
        </el-table-column>
        <el-table-column label="血型" width="80" align="center">
          <template slot-scope="scope">
            <el-tag type="danger" size="small" effect="dark" v-if="scope.row.bloodType">{{ scope.row.bloodType }}</el-tag>
            <span v-else class="text-gray-500">未知</span>
          </template>
        </el-table-column>
        <el-table-column prop="height" label="身高(cm)" width="100">
          <template slot-scope="scope">
            <span>{{ scope.row.height || '未记录' }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="weight" label="体重(kg)" width="100">
          <template slot-scope="scope">
            <span>{{ scope.row.weight || '未记录' }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="chronicDisease" label="慢性疾病" min-width="180">
          <template slot-scope="scope">
            <div v-if="scope.row.chronicDisease">
              <el-tag 
                v-for="(disease, index) in scope.row.chronicDisease.split(',')" 
                :key="index"
                size="small"
                type="warning"
                class="mr-1 mb-1"
              >
                {{ disease }}
              </el-tag>
            </div>
            <span v-else class="text-gray-500">无</span>
          </template>
        </el-table-column>
        <el-table-column prop="emergencyContact" label="紧急联系人" min-width="120">
          <template slot-scope="scope">
            <div class="flex items-center" v-if="scope.row.emergencyContact">
              <i class="el-icon-user text-gray-400 mr-1"></i>
              <span>{{ scope.row.emergencyContact }}</span>
            </div>
            <span v-else class="text-gray-500">未设置</span>
          </template>
        </el-table-column>
        <el-table-column prop="updateTime" label="更新时间" min-width="160">
          <template slot-scope="scope">
            <div class="flex items-center">
              <i class="el-icon-time text-gray-400 mr-1"></i>
              <span>{{ formatDate(scope.row.updateTime) }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="操作" fixed="right" width="240" align="center">
          <template slot-scope="scope">
            <div class="flex justify-center space-x-2">
              <el-button type="primary" size="mini" icon="el-icon-edit" circle @click="handleEdit(scope.row)" class="transform hover:-translate-y-1 transition-transform"></el-button>
              <el-button type="info" size="mini" icon="el-icon-view" circle @click="handleDetail(scope.row)" class="transform hover:-translate-y-1 transition-transform"></el-button>
              <el-button type="warning" size="mini" icon="el-icon-bell" circle @click="handleSendReminder(scope.row)" class="transform hover:-translate-y-1 transition-transform" title="发送健康提醒"></el-button>
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

    <!-- 添加/编辑健康档案对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="750px" :close-on-click-modal="false" custom-class="rounded-lg">
      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
        <!-- 基础信息 -->
        <div class="bg-gray-50 p-4 rounded-lg mb-4">
          <h3 class="text-lg font-medium text-gray-800 mb-4 flex items-center">
            <i class="el-icon-user text-blue-500 mr-2"></i>基础信息
          </h3>
          
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="用户" prop="userId">
                <el-select v-model="form.userId" placeholder="请选择用户" filterable>
                  <el-option
                    v-for="item in userOptions"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id">
                    <div class="flex items-center">
                      <el-avatar :size="20" :src="item.avatar" class="mr-2"></el-avatar>
                      <span>{{ item.name }}</span>
                      <span class="text-gray-400 text-xs ml-2">(ID: {{ item.id }})</span>
                    </div>
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="血型">
                <el-select v-model="form.bloodType" placeholder="请选择血型">
                  <el-option label="A" value="A"></el-option>
                  <el-option label="B" value="B"></el-option>
                  <el-option label="O" value="O"></el-option>
                  <el-option label="AB" value="AB"></el-option>
                  <el-option label="未知" value="未知"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="身高(cm)">
                <el-input-number v-model="form.height" :precision="2" :step="0.01" :min="0" class="w-full"></el-input-number>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="体重(kg)">
                <el-input-number v-model="form.weight" :precision="2" :step="0.01" :min="0" class="w-full"></el-input-number>
              </el-form-item>
            </el-col>
          </el-row>
          
          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item label="慢性疾病">
                <el-input v-model="form.chronicDisease" placeholder="多个疾病请用逗号分隔"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="紧急联系人">
                <el-input v-model="form.emergencyContact" placeholder="请输入紧急联系人姓名"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="联系电话">
                <el-input v-model="form.emergencyPhone" placeholder="请输入紧急联系人电话"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </div>
        
        <!-- 健康记录 -->
        <div class="bg-gray-50 p-4 rounded-lg mb-4">
          <h3 class="text-lg font-medium text-gray-800 mb-4 flex items-center">
            <i class="el-icon-notebook-2 text-green-500 mr-2"></i>健康记录
          </h3>
          
          <el-form-item label="病史记录">
            <el-input type="textarea" v-model="form.medicalHistory" :rows="4" placeholder="请详细描述病史"></el-input>
          </el-form-item>
          
          <el-form-item label="药物过敏史">
            <el-input type="textarea" v-model="form.drugAllergies" :rows="4" placeholder="请详细描述药物过敏情况"></el-input>
          </el-form-item>
          
          <el-form-item label="体检记录">
            <el-input type="textarea" v-model="form.physicalExamination" :rows="4" placeholder="请填写体检记录"></el-input>
          </el-form-item>
          
          <el-form-item label="病历记录">
            <el-input type="textarea" v-model="form.medicalRecords" :rows="4" placeholder="请填写病历记录"></el-input>
          </el-form-item>
        </div>
      </el-form>
      <div slot="footer" class="flex justify-end space-x-3">
        <el-button @click="dialogVisible = false" icon="el-icon-close">取 消</el-button>
        <el-button type="primary" @click="saveHealthRecord" :loading="submitLoading" icon="el-icon-check">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 健康档案详情对话框 -->
    <el-dialog title="健康档案详情" :visible.sync="detailDialogVisible" width="800px" custom-class="rounded-lg">
      <div v-if="currentRecord" class="px-5">
        <!-- 用户信息和基础健康数据 -->
        <div class="bg-blue-50 rounded-xl p-5 mb-6 relative overflow-hidden">
          <div class="absolute top-0 right-0 h-full w-40 opacity-10 flex justify-center items-center">
            <i class="el-icon-user-solid text-8xl text-blue-500"></i>
          </div>
          
          <div class="flex items-center mb-4 relative z-10">
            <el-avatar :size="64" :src="currentRecord.user?.avatar" class="mr-4 border-2 border-white shadow-md"></el-avatar>
            <div>
              <h3 class="text-xl font-bold text-gray-800 mb-1">{{ currentRecord.user?.name || '未知用户' }}</h3>
              <p class="text-gray-500 flex items-center">
                <i class="el-icon-user mr-1"></i>
                <span>用户ID: {{ currentRecord.userId }}</span>
              </p>
            </div>
          </div>
          
          <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div class="bg-white rounded-lg p-3 shadow-sm flex items-center">
              <div class="bg-red-100 rounded-full p-2 mr-3">
                <i class="el-icon-cherry text-red-500"></i>
              </div>
              <div>
                <div class="text-xs text-gray-500">血型</div>
                <div class="text-gray-800 font-medium">{{ currentRecord.bloodType || '未知' }}</div>
              </div>
            </div>
            
            <div class="bg-white rounded-lg p-3 shadow-sm flex items-center">
              <div class="bg-blue-100 rounded-full p-2 mr-3">
                <i class="el-icon-top text-blue-500"></i>
              </div>
              <div>
                <div class="text-xs text-gray-500">身高</div>
                <div class="text-gray-800 font-medium">{{ currentRecord.height ? `${currentRecord.height} cm` : '未记录' }}</div>
              </div>
            </div>
            
            <div class="bg-white rounded-lg p-3 shadow-sm flex items-center">
              <div class="bg-green-100 rounded-full p-2 mr-3">
                <i class="el-icon-data-line text-green-500"></i>
              </div>
              <div>
                <div class="text-xs text-gray-500">体重</div>
                <div class="text-gray-800 font-medium">{{ currentRecord.weight ? `${currentRecord.weight} kg` : '未记录' }}</div>
              </div>
            </div>
          </div>
        </div>
        
        <!-- 紧急联系信息 -->
        <div class="bg-white rounded-lg shadow-sm p-4 mb-6">
          <h4 class="flex items-center text-gray-700 font-medium mb-3">
            <i class="el-icon-phone text-red-500 mr-2"></i> 紧急联系信息
          </h4>
          <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div class="flex">
              <span class="text-gray-500 w-24">联系人:</span>
              <span class="text-gray-800">{{ currentRecord.emergencyContact || '未设置' }}</span>
            </div>
            <div class="flex">
              <span class="text-gray-500 w-24">联系电话:</span>
              <span class="text-gray-800">{{ currentRecord.emergencyPhone || '未设置' }}</span>
            </div>
          </div>
        </div>
        
        <!-- 慢性疾病 -->
        <div class="bg-white rounded-lg shadow-sm p-4 mb-6">
          <h4 class="flex items-center text-gray-700 font-medium mb-3">
            <i class="el-icon-warning-outline text-amber-500 mr-2"></i> 慢性疾病
          </h4>
          <div>
            <template v-if="currentRecord.chronicDisease">
              <el-tag 
                v-for="(disease, index) in currentRecord.chronicDisease.split(',')" 
                :key="index"
                type="warning"
                class="mr-2 mb-2"
              >
                {{ disease }}
              </el-tag>
            </template>
            <span v-else class="text-gray-500">无慢性疾病</span>
          </div>
        </div>
        
        <!-- 健康记录详情 -->
        <div class="grid grid-cols-1 md:grid-cols-2 gap-6 mb-4">
          <!-- 病史记录 -->
          <div class="bg-white rounded-lg shadow-sm overflow-hidden">
            <div class="bg-blue-500 p-3 text-white">
              <i class="el-icon-notebook-2 mr-1"></i> 病史记录
            </div>
            <div class="p-4">
              <div v-if="currentRecord.medicalHistory" class="text-gray-700 whitespace-pre-line">
                {{ currentRecord.medicalHistory }}
              </div>
              <div v-else class="text-gray-500">暂无病史记录</div>
            </div>
          </div>
          
          <!-- 药物过敏史 -->
          <div class="bg-white rounded-lg shadow-sm overflow-hidden">
            <div class="bg-red-500 p-3 text-white">
              <i class="el-icon-warning mr-1"></i> 药物过敏史
            </div>
            <div class="p-4">
              <div v-if="currentRecord.drugAllergies" class="text-gray-700 whitespace-pre-line">
                {{ currentRecord.drugAllergies }}
              </div>
              <div v-else class="text-gray-500">暂无药物过敏记录</div>
            </div>
          </div>
          
          <!-- 体检记录 -->
          <div class="bg-white rounded-lg shadow-sm overflow-hidden">
            <div class="bg-green-500 p-3 text-white">
              <i class="el-icon-data-analysis mr-1"></i> 体检记录
            </div>
            <div class="p-4">
              <div v-if="currentRecord.physicalExamination" class="text-gray-700 whitespace-pre-line">
                {{ currentRecord.physicalExamination }}
              </div>
              <div v-else class="text-gray-500">暂无体检记录</div>
            </div>
          </div>
          
          <!-- 病历记录 -->
          <div class="bg-white rounded-lg shadow-sm overflow-hidden">
            <div class="bg-purple-500 p-3 text-white">
              <i class="el-icon-document mr-1"></i> 病历记录
            </div>
            <div class="p-4">
              <div v-if="currentRecord.medicalRecords" class="text-gray-700 whitespace-pre-line">
                {{ currentRecord.medicalRecords }}
              </div>
              <div v-else class="text-gray-500">暂无病历记录</div>
            </div>
          </div>
        </div>
        
        <!-- 时间信息 -->
        <div class="bg-gray-50 rounded-lg p-3 text-gray-500 flex justify-between">
          <div>创建时间: {{ formatDate(currentRecord.createTime) }}</div>
          <div>更新时间: {{ formatDate(currentRecord.updateTime) }}</div>
        </div>
      </div>
      <div slot="footer" class="flex justify-end space-x-3">
        <el-button @click="detailDialogVisible = false" icon="el-icon-close">关 闭</el-button>
        <el-button type="primary" @click="handleEdit(currentRecord)" icon="el-icon-edit">编辑</el-button>
      </div>
    </el-dialog>

    <!-- 发送健康提醒对话框 -->
    <el-dialog title="发送健康提醒" :visible.sync="reminderDialogVisible" width="600px" :close-on-click-modal="false" custom-class="rounded-lg">
      <el-form :model="reminderForm" :rules="reminderRules" ref="reminderForm" label-width="100px">
        <div class="bg-blue-50 rounded-lg p-4 mb-4">
          <h4 class="flex items-center text-blue-800 font-medium mb-2">
            <i class="el-icon-user text-blue-600 mr-2"></i> 相关用户信息
          </h4>
          <div v-if="currentHealthRecord && currentHealthRecord.user" class="flex items-center">
            <el-avatar :size="40" :src="currentHealthRecord.user.avatar" class="mr-3"></el-avatar>
            <div>
              <div class="font-medium text-gray-800">{{ currentHealthRecord.user.name }}</div>
              <div class="text-sm text-gray-500">用户ID: {{ currentHealthRecord.user.id }}</div>
            </div>
          </div>
        </div>
        
        <el-form-item label="提醒类型" prop="reminderType">
          <el-select v-model="reminderForm.reminderType" placeholder="请选择提醒类型">
            <el-option label="健康警报" value="HEALTH_ALERT"></el-option>
            <el-option label="用药提醒" value="MEDICATION"></el-option>
            <el-option label="体检提醒" value="CHECK_UP"></el-option>
            <el-option label="紧急情况" value="EMERGENCY"></el-option>
          </el-select>
        </el-form-item>
        
        <el-form-item label="优先级" prop="priority">
          <el-select v-model="reminderForm.priority" placeholder="请选择优先级">
            <el-option label="高" value="HIGH">
              <span style="color: #F56C6C;">🔴 高优先级</span>
            </el-option>
            <el-option label="普通" value="NORMAL">
              <span style="color: #E6A23C;">🟡 普通优先级</span>
            </el-option>
            <el-option label="低" value="LOW">
              <span style="color: #67C23A;">🟢 低优先级</span>
            </el-option>
          </el-select>
        </el-form-item>
        
        <el-form-item label="接收人类型" prop="receiverType">
          <el-select v-model="reminderForm.receiverType" placeholder="请选择接收人类型">
            <el-option label="所有人" value="ALL"></el-option>
            <el-option label="医护人员" value="MEDICAL_STAFF"></el-option>
            <el-option label="家属" value="FAMILY"></el-option>
            <el-option label="用户本人" value="USER"></el-option>
          </el-select>
        </el-form-item>
        
        <el-form-item label="提醒内容" prop="content">
          <el-input 
            type="textarea" 
            v-model="reminderForm.content" 
            :rows="4" 
            placeholder="请输入详细的健康提醒内容，例如：该用户的血压异常，建议立即关注并采取相应措施..."
            maxlength="500"
            show-word-limit
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="flex justify-end space-x-3">
        <el-button @click="reminderDialogVisible = false" icon="el-icon-close">取 消</el-button>
        <el-button type="primary" @click="sendHealthReminder" :loading="reminderSubmitLoading" icon="el-icon-s-promotion">发 送</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import request from '@/utils/request'

export default {
  name: "HealthRecordManage",
  data() {
    return {
      // 搜索表单
      searchForm: {
        name: "",
        bloodType: "",
        chronicDisease: ""
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
      dialogTitle: "添加健康档案",
      isEdit: false,
      submitLoading: false,
      
      // 详情对话框
      detailDialogVisible: false,
      currentRecord: null,
      
      // 提醒对话框
      reminderDialogVisible: false,
      currentHealthRecord: null,
      reminderSubmitLoading: false,
      
      // 用户选项
      userOptions: [],
      
      // 表单
      form: {
        id: null,
        userId: null,
        bloodType: '',
        height: null,
        weight: null,
        chronicDisease: '',
        emergencyContact: '',
        emergencyPhone: '',
        medicalHistory: '',
        drugAllergies: '',
        physicalExamination: '',
        medicalRecords: ''
      },
      
      // 提醒表单
      reminderForm: {
        content: '',
        reminderType: 'HEALTH_ALERT',
        priority: 'NORMAL',
        receiverType: 'ALL',
        healthRecordId: null,
        relatedUserId: null
      },
      
      // 表单验证规则
      rules: {
        userId: [
          { required: true, message: "请选择用户", trigger: "change" }
        ]
      },
      
      // 提醒表单验证规则
      reminderRules: {
        reminderType: [
          { required: true, message: "请选择提醒类型", trigger: "change" }
        ],
        priority: [
          { required: true, message: "请选择优先级", trigger: "change" }
        ],
        receiverType: [
          { required: true, message: "请选择接收人类型", trigger: "change" }
        ],
        content: [
          { required: true, message: "请输入提醒内容", trigger: "blur" },
          { min: 10, max: 500, message: "提醒内容长度在 10 到 500 个字符", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    // 先加载用户数据，然后加载健康档案数据，确保数据关联正确
    this.fetchUsers()
      .then(() => {
        this.fetchData();
      })
      .catch(() => {
        // 即使用户数据加载失败，也要加载健康档案数据
        this.fetchData();
      });
  },
  methods: {
    // 获取健康档案数据
    fetchData() {
      this.loading = true;
      
      // 构建请求参数
      const params = {
        pageNum: this.pageNum,
        pageSize: this.pageSize
      };
      
      // 只有当搜索表单中有值时才添加到请求参数中
      if (this.searchForm.name) {
        params.name = this.searchForm.name;
      }
      
      if (this.searchForm.bloodType) {
        params.bloodType = this.searchForm.bloodType;
      }
      
      if (this.searchForm.chronicDisease) {
        params.chronicDisease = this.searchForm.chronicDisease;
      }
      
      request.get("/health-record/selectPage", { params })
        .then(res => {
          if (res.code === '200') {
            this.tableData = res.data.list || [];
            this.total = res.data.total || 0;
            
            // 查询关联用户信息
            this.fetchRelatedUsers();
          } else {
            this.$message.error(res.msg || "获取健康档案数据失败");
          }
        })
        .catch(() => {
          this.$message.error("获取健康档案数据异常");
        })
        .finally(() => {
          this.loading = false;
        });
    },
    
    // 获取用户选项数据
    fetchUsers() {
      return request.get("/user/selectAll")
        .then(res => {
          if (res.code === '200') {
            this.userOptions = res.data || [];
          }
          return res;
        })
        .catch(error => {
          console.error('获取用户数据失败:', error);
          this.$message.error('获取用户数据失败');
          throw error;
        });
    },
    
    // 查询关联用户的详细信息
    fetchRelatedUsers() {
      // 从健康档案中提取用户ID
      const userIds = [...new Set(this.tableData.map(record => record.userId))];
      
      // 如果没有用户ID，则不需要查询
      if (userIds.length === 0) return;
      
      // 使用已获取的userOptions数据，避免重复请求
      if (this.userOptions.length > 0) {
        // 将用户信息添加到健康档案数据中
        this.tableData.forEach((record, index) => {
          const user = this.userOptions.find(u => u.id === record.userId);
          if (user) {
            // 使用$set确保Vue的响应式更新
            this.$set(this.tableData[index], 'user', user);
          }
        });
      } else {
        // 如果userOptions还没有数据，查询用户信息
        request.get("/user/selectAll")
          .then(res => {
            if (res.code === '200') {
              const users = res.data || [];
              
              // 将用户信息添加到健康档案数据中
              this.tableData.forEach((record, index) => {
                const user = users.find(u => u.id === record.userId);
                if (user) {
                  // 使用$set确保Vue的响应式更新
                  this.$set(this.tableData[index], 'user', user);
                }
              });
            }
          });
      }
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
    
    // 添加健康档案
    handleAdd() {
      this.dialogTitle = "添加健康档案";
      this.isEdit = false;
      this.form = {
        id: null,
        userId: null,
        bloodType: '',
        height: null,
        weight: null,
        chronicDisease: '',
        emergencyContact: '',
        emergencyPhone: '',
        medicalHistory: '',
        drugAllergies: '',
        physicalExamination: '',
        medicalRecords: ''
      };
      this.dialogVisible = true;
      
      // 清除验证
      this.$nextTick(() => {
        if (this.$refs.form) {
          this.$refs.form.clearValidate();
        }
      });
    },
    
    // 编辑健康档案
    handleEdit(row) {
      this.dialogTitle = "编辑健康档案";
      this.isEdit = true;
      this.form = JSON.parse(JSON.stringify(row)); // 深拷贝，防止直接修改表格数据
      this.dialogVisible = true;
      
      if (this.detailDialogVisible) {
        this.detailDialogVisible = false; // 如果从详情页进入，先关闭详情对话框
      }
      
      // 清除验证
      this.$nextTick(() => {
        if (this.$refs.form) {
          this.$refs.form.clearValidate();
        }
      });
    },
    
    // 查看详情
    handleDetail(row) {
      this.currentRecord = row;
      
      // 如果没有用户信息，尝试获取
      if (!row.user && row.userId) {
        request.get(`/user/selectById/${row.userId}`)
          .then(res => {
            if (res.code === '200' && res.data) {
              this.$set(this.currentRecord, 'user', res.data);
            }
          });
      }
      
      this.detailDialogVisible = true;
    },
    
    // 保存健康档案
    saveHealthRecord() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.submitLoading = true;
          
          // 使用统一的保存接口
          request.post("/health-record/save", this.form)
            .then(res => {
              if (res.code === '200') {
                this.$message.success(this.isEdit ? "编辑成功" : "添加成功");
                this.dialogVisible = false;
                this.fetchData();
              } else {
                this.$message.error(res.msg || (this.isEdit ? "编辑失败" : "添加失败"));
              }
            })
            .catch(() => {
              this.$message.error(this.isEdit ? "编辑健康档案请求异常" : "添加健康档案请求异常");
            })
            .finally(() => {
              this.submitLoading = false;
            });
        }
      });
    },
    
    // 删除健康档案
    handleDelete(row) {
      this.$confirm("确定删除该健康档案吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        request.delete(`/health-record/delete/${row.id}`)
          .then(res => {
            if (res.code === '200') {
              this.$message.success("删除成功");
              this.fetchData();
            } else {
              this.$message.error(res.msg || "删除失败");
            }
          })
          .catch(() => {
            this.$message.error("删除健康档案请求异常");
          });
      }).catch(() => {});
    },
    
    // 批量删除
    handleBatchDelete() {
      if (this.selection.length === 0) {
        this.$message.warning("请选择要删除的健康档案");
        return;
      }
      
      this.$confirm(`确定删除选中的 ${this.selection.length} 个健康档案吗?`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        const ids = this.selection.map(item => item.id);
        
        request.delete("/health-record/delete/batch", { data: ids })
          .then(res => {
            if (res.code === '200') {
              this.$message.success("批量删除成功");
              this.fetchData();
            } else {
              this.$message.error(res.msg || "批量删除失败");
            }
          })
          .catch(() => {
            this.$message.error("批量删除健康档案请求异常");
          });
      }).catch(() => {});
    },
    
    // 发送健康提醒
    handleSendReminder(row) {
      this.currentHealthRecord = row;
      
      // 重置提醒表单
      this.reminderForm = {
        content: '',
        reminderType: 'HEALTH_ALERT',
        priority: 'NORMAL',
        receiverType: 'ALL',
        healthRecordId: row.id,
        relatedUserId: row.userId
      };
      
      this.reminderDialogVisible = true;
      
      // 清除验证
      this.$nextTick(() => {
        if (this.$refs.reminderForm) {
          this.$refs.reminderForm.clearValidate();
        }
      });
    },
    
    // 发送健康提醒
    sendHealthReminder() {
      this.$refs.reminderForm.validate(valid => {
        if (valid) {
          this.reminderSubmitLoading = true;
          
          // 获取当前登录用户信息
          const currentUser = JSON.parse(localStorage.getItem("xm-user") || "{}");
          
          // 构建提醒数据
          const reminderData = {
            ...this.reminderForm,
            senderId: currentUser.id,
            senderName: currentUser.name,
            senderType: currentUser.role
          };
          
          request.post("/health-reminder/add", reminderData)
            .then(res => {
              if (res.code === '200') {
                this.$message.success("健康提醒发送成功");
                this.reminderDialogVisible = false;
              } else {
                this.$message.error(res.msg || "发送健康提醒失败");
              }
            })
            .catch(() => {
              this.$message.error("发送健康提醒请求异常");
            })
            .finally(() => {
              this.reminderSubmitLoading = false;
            });
        }
      });
    },
    
    // 格式化日期
    formatDate(dateStr) {
      if (!dateStr) return '未知';
      const date = new Date(dateStr);
      return date.toLocaleDateString() + ' ' + date.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' });
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

/* 自定义表格内容样式 */
.cell-tag-container {
  display: flex;
  flex-wrap: wrap;
}

/* 自定义元素样式 */
:deep(.el-tag) {
  border-radius: 4px;
  font-weight: 500;
}

:deep(.el-input__inner), 
:deep(.el-textarea__inner) {
  border-radius: 6px;
  transition: all 0.2s ease;
}

:deep(.el-input__inner:focus), 
:deep(.el-textarea__inner:focus) {
  box-shadow: 0 0 0 2px rgba(59, 130, 246, 0.3);
}

:deep(.el-textarea__inner) {
  padding: 10px;
  line-height: 1.5;
}

/* 媒体查询 - 移动端适配 */
@media (max-width: 640px) {
  .cell-tag-container {
    flex-direction: column;
  }
}
</style> 