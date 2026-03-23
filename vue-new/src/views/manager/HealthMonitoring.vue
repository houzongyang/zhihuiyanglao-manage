<template>
  <div class="health-monitoring-manage-container">
    <!-- 页面标题 -->
    <div class="page-header bg-white rounded-lg shadow-sm p-6 mb-6">
      <div class="flex items-center justify-between">
        <div class="flex items-center">
          <div class="bg-gradient-to-r from-blue-600 to-indigo-600 rounded-lg p-3 shadow-lg mr-4">
            <i class="el-icon-data-analysis text-2xl text-white"></i>
          </div>
          <div>
            <h1 class="text-2xl font-bold text-gray-800 mb-1">健康监控管理</h1>
            <p class="text-sm text-gray-500">管理所有用户的健康监控数据</p>
          </div>
        </div>
        <div class="flex space-x-3">
          <el-button type="primary" icon="el-icon-plus" @click="openAddDialog">
            添加监控记录
          </el-button>
          <el-button icon="el-icon-refresh" @click="load">
            刷新数据
          </el-button>
        </div>
      </div>
    </div>

    <!-- 搜索筛选区域 -->
    <div class="search-container bg-white rounded-lg shadow-sm p-6 mb-6">
      <el-form :model="searchForm" :inline="true" class="search-form">
        <el-form-item label="用户姓名">
          <el-input
            v-model="searchForm.userName"
            placeholder="请输入用户姓名"
            clearable
            class="w-48"
          />
        </el-form-item>
<!--        <el-form-item label="记录日期">-->
<!--          <el-date-picker-->
<!--            v-model="searchForm.dateRange"-->
<!--            type="daterange"-->
<!--            range-separator="至"-->
<!--            start-placeholder="开始日期"-->
<!--            end-placeholder="结束日期"-->
<!--            format="yyyy-MM-dd"-->
<!--            value-format="yyyy-MM-dd"-->
<!--            class="w-64"-->
<!--          />-->
<!--        </el-form-item>-->
        <el-form-item label="测量人员">
          <el-input
            v-model="searchForm.measuredBy"
            placeholder="请输入测量人员"
            clearable
            class="w-48"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="load" icon="el-icon-search">
            搜索
          </el-button>
          <el-button @click="resetSearch" icon="el-icon-refresh-left">
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 数据统计卡片 -->
    <div class="stats-container grid grid-cols-1 md:grid-cols-4 gap-6 mb-6">
      <div class="stat-card bg-gradient-to-r from-blue-500 to-blue-600 text-white rounded-lg p-6 shadow-lg">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-blue-100 text-sm">今日记录</p>
            <p class="text-2xl font-bold">{{ stats.todayCount || 0 }}</p>
          </div>
          <i class="el-icon-calendar text-3xl text-blue-200"></i>
        </div>
      </div>
      <div class="stat-card bg-gradient-to-r from-green-500 to-green-600 text-white rounded-lg p-6 shadow-lg">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-green-100 text-sm">本周记录</p>
            <p class="text-2xl font-bold">{{ stats.weekCount || 0 }}</p>
          </div>
          <i class="el-icon-date text-3xl text-green-200"></i>
        </div>
      </div>
      <div class="stat-card bg-gradient-to-r from-purple-500 to-purple-600 text-white rounded-lg p-6 shadow-lg">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-purple-100 text-sm">异常记录</p>
            <p class="text-2xl font-bold">{{ stats.abnormalCount || 0 }}</p>
          </div>
          <i class="el-icon-warning text-3xl text-purple-200"></i>
        </div>
      </div>
      <div class="stat-card bg-gradient-to-r from-orange-500 to-orange-600 text-white rounded-lg p-6 shadow-lg">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-orange-100 text-sm">总记录数</p>
            <p class="text-2xl font-bold">{{ stats.totalCount || 0 }}</p>
          </div>
          <i class="el-icon-data-line text-3xl text-orange-200"></i>
        </div>
      </div>
    </div>

    <!-- 数据表格 -->
    <div class="table-container bg-white rounded-lg shadow-sm">
      <el-table
        :data="tableData"
        style="width: 100%"
        stripe
        border
        :header-cell-style="{ backgroundColor: '#f8fafc', color: '#374151', fontWeight: '600' }"
        v-loading="loading"
      >
        <el-table-column prop="userName" label="用户姓名" width="120" fixed="left">
          <template slot-scope="scope">
            <div class="flex items-center">
              <div class="w-8 h-8 bg-blue-100 rounded-full flex items-center justify-center mr-2">
                <i class="el-icon-user text-blue-600"></i>
              </div>
              <span class="font-medium">{{ scope.row.userName }}</span>
            </div>
          </template>
        </el-table-column>
        
        <el-table-column prop="recordDate" label="记录日期" width="120">
          <template slot-scope="scope">
            <div class="text-gray-800">{{ scope.row.recordDate }}</div>
          </template>
        </el-table-column>
        
        <el-table-column prop="recordTime" label="记录时间" width="100">
          <template slot-scope="scope">
            <div class="text-gray-600">{{ scope.row.recordTime }}</div>
          </template>
        </el-table-column>

        <el-table-column label="生命体征" width="240">
          <template slot-scope="scope">
            <div class="space-y-1">
              <div v-if="scope.row.bodyTemperature" class="flex items-center text-xs">
                <i class="el-icon-thermometer text-red-500 mr-1"></i>
                <span>体温: {{ scope.row.bodyTemperature }}°C</span>
              </div>
              <div v-if="scope.row.systolicPressure && scope.row.diastolicPressure" class="flex items-center text-xs">
                <i class="el-icon-monitor text-blue-500 mr-1"></i>
                <span>血压: {{ scope.row.systolicPressure }}/{{ scope.row.diastolicPressure }}</span>
              </div>
              <div v-if="scope.row.heartRate" class="flex items-center text-xs">
                <i class="el-icon-heart text-green-500 mr-1"></i>
                <span>心率: {{ scope.row.heartRate }}次/分</span>
              </div>
              <div v-if="scope.row.bloodSugar" class="flex items-center text-xs">
                <i class="el-icon-grape text-purple-500 mr-1"></i>
                <span>血糖: {{ scope.row.bloodSugar }}mmol/L</span>
              </div>
            </div>
          </template>
        </el-table-column>

        <el-table-column label="其他指标" width="200">
          <template slot-scope="scope">
            <div class="space-y-1">
              <div v-if="scope.row.bloodOxygen" class="flex items-center text-xs">
                <i class="el-icon-wind-power text-cyan-500 mr-1"></i>
                <span>血氧: {{ scope.row.bloodOxygen }}%</span>
              </div>
              <div v-if="scope.row.weight" class="flex items-center text-xs">
                <i class="el-icon-data-line text-orange-500 mr-1"></i>
                <span>体重: {{ scope.row.weight }}kg</span>
              </div>
              <div v-if="scope.row.sleepHours" class="flex items-center text-xs">
                <i class="el-icon-moon text-indigo-500 mr-1"></i>
                <span>睡眠: {{ scope.row.sleepHours }}小时</span>
              </div>
              <div v-if="scope.row.moodStatus" class="flex items-center text-xs">
                <i class="el-icon-sunny text-yellow-500 mr-1"></i>
                <span>情绪: {{ getMoodStatusLabel(scope.row.moodStatus) }}</span>
              </div>
            </div>
          </template>
        </el-table-column>

        <el-table-column prop="measuredBy" label="测量人员" width="120">
          <template slot-scope="scope">
            <el-tag size="small" type="info" v-if="scope.row.measuredBy">
              {{ scope.row.measuredBy }}
            </el-tag>
            <span class="text-gray-400 text-xs" v-else>自助测量</span>
          </template>
        </el-table-column>

        <el-table-column prop="abnormalSymptoms" label="异常症状" width="200" show-overflow-tooltip>
          <template slot-scope="scope">
            <div v-if="scope.row.abnormalSymptoms" class="text-red-600 text-xs">
              <i class="el-icon-warning mr-1"></i>
              {{ scope.row.abnormalSymptoms }}
            </div>
            <span v-else class="text-gray-400 text-xs">无</span>
          </template>
        </el-table-column>

        <el-table-column prop="createTime" label="创建时间" width="180">
          <template slot-scope="scope">
            <div class="text-gray-600 text-xs">{{ formatDateTime(scope.row.createTime) }}</div>
          </template>
        </el-table-column>

        <el-table-column label="操作" width="250" fixed="right">
          <template slot-scope="scope">
            <div class="flex space-x-1">
              <el-button size="mini" type="primary" @click="viewRecord(scope.row)" plain>
                <i class="el-icon-view"></i>
                查看
              </el-button>
              <el-button size="mini" type="success" @click="editRecord(scope.row)" plain>
                <i class="el-icon-edit"></i>
                编辑
              </el-button>
              <el-button size="mini" type="danger" @click="deleteRecord(scope.row.id)" plain>
                <i class="el-icon-delete"></i>
                删除
              </el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container p-4 flex justify-center">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
        />
      </div>
    </div>

    <!-- 添加/编辑对话框 -->
    <el-dialog
      :visible.sync="dialogVisible"
      width="900px"
      :close-on-click-modal="false"
      custom-class="health-monitoring-dialog"
    >
      <div class="p-4 bg-gradient-to-r from-blue-500 to-indigo-600 rounded-t-lg text-white mb-6 -mt-6 -mx-4 flex items-center">
        <i class="el-icon-edit-outline text-2xl mr-3"></i>
        <span class="text-xl font-medium">{{ dialogTitle }}</span>
      </div>

      <el-form :model="form" :rules="rules" ref="form" label-width="120px">
        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
          <!-- 基础信息 -->
          <div class="col-span-full">
            <h3 class="text-lg font-medium text-gray-800 mb-4 flex items-center">
              <i class="el-icon-user text-blue-600 mr-2"></i>
              基础信息
            </h3>
            <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
              <el-form-item label="用户" prop="userId" required>
                <el-select
                  v-model="form.userId"
                  placeholder="请选择用户"
                  filterable
                  class="w-full"
                  :disabled="isEdit"
                >
                  <el-option
                    v-for="user in userList"
                    :key="user.id"
                    :label="user.name"
                    :value="user.id"
                  />
                </el-select>
              </el-form-item>
              <el-form-item label="记录日期" prop="recordDate" required>
                <el-input
                  v-model="form.recordDate"
                  type="date"
                  placeholder="选择日期"
                  class="w-full"
                />
              </el-form-item>
              <el-form-item label="记录时间" prop="recordTime" required>
                <el-input
                  v-model="form.recordTime"
                  type="time"
                  placeholder="选择时间"
                  class="w-full"
                />
              </el-form-item>
            </div>
          </div>

          <!-- 生命体征 -->
          <div class="col-span-full">
            <h3 class="text-lg font-medium text-gray-800 mb-4 flex items-center">
              <i class="el-icon-heart text-red-600 mr-2"></i>
              生命体征
            </h3>
            <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
              <el-form-item label="体温(°C)">
                <el-input-number
                  v-model="form.bodyTemperature"
                  :precision="1"
                  :step="0.1"
                  :min="35"
                  :max="42"
                  controls-position="right"
                  class="w-full"
                />
              </el-form-item>
              <el-form-item label="收缩压(mmHg)">
                <el-input-number
                  v-model="form.systolicPressure"
                  :min="80"
                  :max="200"
                  controls-position="right"
                  class="w-full"
                />
              </el-form-item>
              <el-form-item label="舒张压(mmHg)">
                <el-input-number
                  v-model="form.diastolicPressure"
                  :min="40"
                  :max="120"
                  controls-position="right"
                  class="w-full"
                />
              </el-form-item>
              <el-form-item label="心率(次/分)">
                <el-input-number
                  v-model="form.heartRate"
                  :min="40"
                  :max="150"
                  controls-position="right"
                  class="w-full"
                />
              </el-form-item>
              <el-form-item label="血糖(mmol/L)">
                <el-input-number
                  v-model="form.bloodSugar"
                  :precision="2"
                  :step="0.1"
                  :min="2"
                  :max="20"
                  controls-position="right"
                  class="w-full"
                />
              </el-form-item>
              <el-form-item label="血糖类型">
                <el-select v-model="form.bloodSugarType" placeholder="请选择" class="w-full">
                  <el-option label="空腹" value="FASTING"/>
                  <el-option label="餐后" value="POSTPRANDIAL"/>
                  <el-option label="随机" value="RANDOM"/>
                </el-select>
              </el-form-item>
              <el-form-item label="血氧(%)">
                <el-input-number
                  v-model="form.bloodOxygen"
                  :min="70"
                  :max="100"
                  controls-position="right"
                  class="w-full"
                />
              </el-form-item>
              <el-form-item label="体重(kg)">
                <el-input-number
                  v-model="form.weight"
                  :precision="2"
                  :step="0.1"
                  :min="30"
                  :max="200"
                  controls-position="right"
                  class="w-full"
                />
              </el-form-item>
            </div>
          </div>

          <!-- 生活状态 -->
          <div class="col-span-full">
            <h3 class="text-lg font-medium text-gray-800 mb-4 flex items-center">
              <i class="el-icon-sunny text-yellow-600 mr-2"></i>
              生活状态
            </h3>
            <div class="grid grid-cols-1 md:grid-cols-4 gap-4">
              <el-form-item label="睡眠时长(小时)">
                <el-input-number
                  v-model="form.sleepHours"
                  :precision="1"
                  :step="0.5"
                  :min="0"
                  :max="24"
                  controls-position="right"
                  class="w-full"
                />
              </el-form-item>
              <el-form-item label="睡眠质量">
                <el-select v-model="form.sleepQuality" placeholder="请选择" class="w-full">
                  <el-option label="优秀" value="EXCELLENT"/>
                  <el-option label="良好" value="GOOD"/>
                  <el-option label="一般" value="FAIR"/>
                  <el-option label="较差" value="POOR"/>
                </el-select>
              </el-form-item>
              <el-form-item label="情绪状态">
                <el-select v-model="form.moodStatus" placeholder="请选择" class="w-full">
                  <el-option label="愉快" value="HAPPY"/>
                  <el-option label="正常" value="NORMAL"/>
                  <el-option label="焦虑" value="ANXIOUS"/>
                  <el-option label="抑郁" value="DEPRESSED"/>
                </el-select>
              </el-form-item>
              <el-form-item label="测量人员">
                <el-input v-model="form.measuredBy" placeholder="请输入" class="w-full"/>
              </el-form-item>
            </div>
          </div>

          <!-- 附加信息 -->
          <div class="col-span-full">
            <h3 class="text-lg font-medium text-gray-800 mb-4 flex items-center">
              <i class="el-icon-document text-blue-600 mr-2"></i>
              附加信息
            </h3>
            <div class="space-y-4">
              <el-form-item label="体力活动">
                <el-input
                  v-model="form.physicalActivity"
                  placeholder="请描述体力活动情况"
                  class="w-full"
                />
              </el-form-item>
              <el-form-item label="异常症状">
                <el-input
                  type="textarea"
                  v-model="form.abnormalSymptoms"
                  :rows="3"
                  placeholder="如有异常症状请详细描述"
                  class="w-full"
                />
              </el-form-item>
              <el-form-item label="备注">
                <el-input
                  type="textarea"
                  v-model="form.remarks"
                  :rows="3"
                  placeholder="其他需要记录的信息"
                  class="w-full"
                />
              </el-form-item>
            </div>
          </div>
        </div>
      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">保 存</el-button>
      </span>
    </el-dialog>

    <!-- 查看详情对话框 -->
    <el-dialog
      title="健康监控详情"
      :visible.sync="viewDialogVisible"
      width="700px"
      custom-class="view-dialog"
    >
      <div v-if="currentRecord" class="p-4">
        <!-- 基础信息 -->
        <div class="mb-6">
          <h3 class="text-lg font-medium text-gray-800 mb-4 flex items-center">
            <i class="el-icon-user text-blue-600 mr-2"></i>
            基础信息
          </h3>
          <el-descriptions border :column="3">
            <el-descriptions-item label="用户姓名">{{ currentRecord.userName }}</el-descriptions-item>
            <el-descriptions-item label="记录日期">{{ currentRecord.recordDate }}</el-descriptions-item>
            <el-descriptions-item label="记录时间">{{ currentRecord.recordTime }}</el-descriptions-item>
            <el-descriptions-item label="测量人员" :span="3">{{ currentRecord.measuredBy || '自助测量' }}</el-descriptions-item>
          </el-descriptions>
        </div>

        <!-- 生命体征 -->
        <div class="mb-6">
          <h3 class="text-lg font-medium text-gray-800 mb-4 flex items-center">
            <i class="el-icon-heart text-red-600 mr-2"></i>
            生命体征
          </h3>
          <div class="grid grid-cols-2 md:grid-cols-4 gap-4">
            <div v-if="currentRecord.bodyTemperature" class="health-metric-view">
              <div class="metric-icon bg-red-100 text-red-600">
                <i class="el-icon-thermometer"></i>
              </div>
              <div class="metric-content">
                <div class="metric-label">体温</div>
                <div class="metric-value">{{ currentRecord.bodyTemperature }}°C</div>
              </div>
            </div>
            <div v-if="currentRecord.systolicPressure && currentRecord.diastolicPressure" class="health-metric-view">
              <div class="metric-icon bg-blue-100 text-blue-600">
                <i class="el-icon-monitor"></i>
              </div>
              <div class="metric-content">
                <div class="metric-label">血压</div>
                <div class="metric-value">{{ currentRecord.systolicPressure }}/{{ currentRecord.diastolicPressure }}</div>
              </div>
            </div>
            <div v-if="currentRecord.heartRate" class="health-metric-view">
              <div class="metric-icon bg-green-100 text-green-600">
                <i class="el-icon-heart"></i>
              </div>
              <div class="metric-content">
                <div class="metric-label">心率</div>
                <div class="metric-value">{{ currentRecord.heartRate }}次/分</div>
              </div>
            </div>
            <div v-if="currentRecord.bloodSugar" class="health-metric-view">
              <div class="metric-icon bg-purple-100 text-purple-600">
                <i class="el-icon-grape"></i>
              </div>
              <div class="metric-content">
                <div class="metric-label">血糖</div>
                <div class="metric-value">{{ currentRecord.bloodSugar }}mmol/L</div>
              </div>
            </div>
          </div>
        </div>

        <!-- 其他信息 -->
        <div v-if="currentRecord.physicalActivity || currentRecord.abnormalSymptoms || currentRecord.remarks">
          <h3 class="text-lg font-medium text-gray-800 mb-4 flex items-center">
            <i class="el-icon-document text-blue-600 mr-2"></i>
            附加信息
          </h3>
          <div class="space-y-3">
            <div v-if="currentRecord.physicalActivity" class="info-item">
              <span class="info-label">体力活动：</span>
              <span class="info-value">{{ currentRecord.physicalActivity }}</span>
            </div>
            <div v-if="currentRecord.abnormalSymptoms" class="info-item">
              <span class="info-label text-red-600">异常症状：</span>
              <span class="info-value text-red-700">{{ currentRecord.abnormalSymptoms }}</span>
            </div>
            <div v-if="currentRecord.remarks" class="info-item">
              <span class="info-label">备注：</span>
              <span class="info-value">{{ currentRecord.remarks }}</span>
            </div>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import request from '@/utils/request'

export default {
  name: 'HealthMonitoringManage',
  data() {
    return {
      tableData: [],
      userList: [],
      stats: {},
      loading: false,
      total: 0,
      pageNum: 1,
      pageSize: 20,
      searchForm: {
        userName: '',
        dateRange: [],
        measuredBy: ''
      },
      dialogVisible: false,
      viewDialogVisible: false,
      isEdit: false,
      currentRecord: null,
      form: this.getInitialForm(),
      rules: {
        userId: [{ required: true, message: '请选择用户', trigger: 'change' }],
        recordDate: [{ required: true, message: '请选择记录日期', trigger: 'change' }],
        recordTime: [{ required: true, message: '请选择记录时间', trigger: 'change' }]
      }
    }
  },
  computed: {
    dialogTitle() {
      return this.isEdit ? '编辑健康监控记录' : '添加健康监控记录'
    }
  },
  created() {
    this.load()
    this.loadUsers()
    this.loadStats()
  },
  methods: {
    getInitialForm() {
      return {
        id: null,
        userId: null,
        recordDate: '',
        recordTime: '',
        bodyTemperature: null,
        systolicPressure: null,
        diastolicPressure: null,
        heartRate: null,
        bloodSugar: null,
        bloodSugarType: '',
        bloodOxygen: null,
        weight: null,
        sleepHours: null,
        sleepQuality: '',
        moodStatus: '',
        physicalActivity: '',
        abnormalSymptoms: '',
        measuredBy: '',
        remarks: ''
      }
    },

    // 数据加载
    async load() {
      this.loading = true
      try {
        const params = {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          ...this.searchForm
        }
        if (this.searchForm.dateRange && this.searchForm.dateRange.length === 2) {
          params.startDate = this.searchForm.dateRange[0]
          params.endDate = this.searchForm.dateRange[1]
        }
        
        const res = await request.get('/health-monitoring/selectPage', { params })
        if (res.code === '200') {
          this.tableData = res.data.list || []
          this.total = res.data.total || 0
        }
      } catch (error) {
        this.$message.error('加载数据失败')
        console.error('加载数据失败:', error)
      } finally {
        this.loading = false
      }
    },

    async loadUsers() {
      try {
        const res = await request.get('/user/selectAll')
        if (res.code === '200') {
          this.userList = res.data || []
        }
      } catch (error) {
        console.error('加载用户列表失败:', error)
      }
    },

    async loadStats() {
      try {
        const res = await request.get('/health-monitoring/getStats')
        if (res.code === '200') {
          this.stats = res.data || {}
        }
      } catch (error) {
        console.error('加载统计数据失败:', error)
      }
    },

    // 搜索重置
    resetSearch() {
      this.searchForm = {
        userName: '',
        dateRange: [],
        measuredBy: ''
      }
      this.load()
    },

    // 分页
    handleSizeChange(val) {
      this.pageSize = val
      this.load()
    },
    handleCurrentChange(val) {
      this.pageNum = val
      this.load()
    },

    // 对话框操作
    openAddDialog() {
      this.isEdit = false
      this.form = this.getInitialForm()
      this.form.recordDate = new Date().toISOString().split('T')[0]
      this.form.recordTime = new Date().toTimeString().substring(0, 5)
      this.dialogVisible = true
    },

    editRecord(record) {
      this.isEdit = true
      console.log('编辑记录原始数据:', record)
      this.form = { ...record }
      
      console.log('原始日期:', this.form.recordDate, '类型:', typeof this.form.recordDate)
      console.log('原始时间:', this.form.recordTime, '类型:', typeof this.form.recordTime)
      
      // 确保日期时间格式正确显示
      this.form.recordDate = this.formatDate(this.form.recordDate)
      this.form.recordTime = this.formatTime(this.form.recordTime)
      
      console.log('格式化后日期:', this.form.recordDate)
      console.log('格式化后时间:', this.form.recordTime)
      
      this.dialogVisible = true
    },

    viewRecord(record) {
      this.currentRecord = record
      this.viewDialogVisible = true
    },

    // 数据操作
    async save() {
      try {
        await this.$refs.form.validate()
        const url = this.isEdit ? '/health-monitoring/update' : '/health-monitoring/save'
        const res = await request.post(url, this.form)
        
        if (res.code === '200') {
          this.$message.success(this.isEdit ? '更新成功' : '保存成功')
          this.dialogVisible = false
          this.load()
          this.loadStats()
        } else {
          this.$message.error(res.msg || '操作失败')
        }
      } catch (error) {
        console.error('保存失败:', error)
      }
    },

    async deleteRecord(id) {
      try {
        await this.$confirm('确认删除这条健康监控记录吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const res = await request.delete(`/health-monitoring/delete/${id}`)
        if (res.code === '200') {
          this.$message.success('删除成功')
          this.load()
          this.loadStats()
        } else {
          this.$message.error(res.msg || '删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败')
        }
      }
    },

    // 工具方法
    formatDateTime(dateTime) {
      if (!dateTime) return ''
      const date = new Date(dateTime)
      return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`
    },

    // 格式化日期为YYYY-MM-DD格式
    formatDate(date) {
      if (!date) return ''
      
      // 如果是字符串且已经是YYYY-MM-DD格式
      if (typeof date === 'string') {
        if (date.includes('-') && date.length === 10) {
          return date // 已经是正确格式
        }
        // 如果是YYYY-MM-DD HH:mm:ss格式，取日期部分
        if (date.includes(' ')) {
          return date.split(' ')[0]
        }
      }
      
      // 尝试解析为Date对象
      try {
        const d = new Date(date)
        if (!isNaN(d.getTime())) {
          return d.toISOString().split('T')[0]
        }
      } catch (e) {
        console.warn('日期格式解析失败:', date)
      }
      
      return date
    },

    // 格式化时间为HH:mm格式
    formatTime(time) {
      if (!time) return ''
      
      // 如果是字符串格式的时间
      if (typeof time === 'string') {
        // 如果包含冒号，说明是时间格式
        if (time.includes(':')) {
          // 支持 HH:mm:ss 格式，取前5位变成 HH:mm
          const parts = time.split(':')
          if (parts.length >= 2) {
            return `${parts[0].padStart(2, '0')}:${parts[1].padStart(2, '0')}`
          }
          return time.length > 5 ? time.substring(0, 5) : time
        }
      }
      
      // 尝试解析为Date对象
      try {
        const t = new Date(`1970-01-01T${time}`)
        if (!isNaN(t.getTime())) {
          return `${String(t.getHours()).padStart(2, '0')}:${String(t.getMinutes()).padStart(2, '0')}`
        }
      } catch (e) {
        console.warn('时间格式解析失败:', time)
      }
      
      return time
    },

    getMoodStatusLabel(mood) {
      const moods = {
        'HAPPY': '愉快',
        'NORMAL': '正常',
        'ANXIOUS': '焦虑',
        'DEPRESSED': '抑郁'
      }
      return moods[mood] || ''
    }
  }
}
</script>

<style scoped>
.health-monitoring-manage-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: 100vh;
}

.health-metric-view {
  @apply flex items-center p-3 bg-gray-50 rounded-lg border border-gray-200;
}

.metric-icon {
  @apply w-10 h-10 rounded-lg flex items-center justify-center mr-3 text-lg;
}

.metric-content {
  @apply flex-1;
}

.metric-label {
  @apply text-xs text-gray-600 font-medium;
}

.metric-value {
  @apply text-sm font-bold text-gray-800;
}

.info-item {
  @apply flex items-start;
}

.info-label {
  @apply text-sm font-medium text-gray-600 mr-2 flex-shrink-0;
}

.info-value {
  @apply text-sm text-gray-800;
}

/* 对话框样式 */
:deep(.health-monitoring-dialog) {
  border-radius: 0.75rem;
  overflow: hidden;
}

:deep(.health-monitoring-dialog .el-dialog__header) {
  padding: 0;
}

:deep(.health-monitoring-dialog .el-dialog__headerbtn) {
  top: 16px;
  right: 16px;
  z-index: 10;
}

:deep(.health-monitoring-dialog .el-dialog__headerbtn .el-dialog__close) {
  color: white;
  font-weight: bold;
}

:deep(.view-dialog) {
  border-radius: 0.75rem;
}

/* 表格样式优化 */
:deep(.el-table) {
  border-radius: 0.5rem;
  overflow: hidden;
}

:deep(.el-table th) {
  background-color: #f8fafc !important;
}

/* 表单样式 */
:deep(.el-form-item__label) {
  color: #374151;
  font-weight: 500;
}

:deep(.el-input__inner),
:deep(.el-textarea__inner) {
  border-radius: 6px;
  transition: all 0.2s ease;
}

:deep(.el-input__inner:focus),
:deep(.el-textarea__inner:focus) {
  border-color: #3b82f6;
  box-shadow: 0 0 0 2px rgba(59, 130, 246, 0.1);
}

/* 统计卡片样式 */
.stat-card {
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.stat-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04);
}
</style> 