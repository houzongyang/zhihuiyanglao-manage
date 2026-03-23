<template>
  <div class="health-monitoring-container bg-gradient-to-br from-blue-50 via-purple-50 to-indigo-50 min-h-screen">
    <div class="flex h-screen">
      <!-- 左侧日期选择区域 -->
      <div class="w-80 bg-white/80 backdrop-blur-sm border-r border-gray-200/50 shadow-xl">
        <!-- 顶部标题区域 -->
        <div class="p-6 bg-gradient-to-r from-blue-600 to-indigo-700 text-white">
          <div class="flex items-center mb-4">
            <div class="bg-white/20 rounded-full p-2 mr-3">
              <svg class="w-6 h-6 text-white" fill="currentColor" viewBox="0 0 24 24">
                <path d="M3 13h8V3H3v10zm0 8h8v-6H3v6zm10 0h8V11h-8v10zm5-18v4h3V3h-3z"/>
              </svg>
            </div>
            <div>
              <h1 class="text-xl font-bold">健康监控</h1>
              <p class="text-blue-100 text-sm">日常健康数据管理</p>
            </div>
          </div>
          

        </div>

        <!-- 日期选择区域 -->
        <div class="p-4">
          <!-- 月份选择器 -->
          <div class="mb-4">
            <el-date-picker
              v-model="selectedMonth"
              type="month"
              placeholder="选择月份"
              format="yyyy年MM月"
              value-format="yyyy-MM"
              @change="onMonthChange"
              class="w-full"
            />
          </div>

          <!-- 日期列表 -->
          <div class="space-y-2 max-h-96 overflow-y-auto">
            <div
              v-for="date in dateList"
              :key="date.date"
              @click="selectDate(date.date)"
              :class="[
                'p-3 rounded-lg cursor-pointer transition-all duration-200 border',
                selectedDate === date.date
                  ? 'bg-gradient-to-r from-blue-500 to-indigo-600 text-white border-blue-500 shadow-lg'
                  : 'bg-white/80 hover:bg-blue-50 border-gray-200/50 hover:border-blue-300'
              ]"
            >
              <div class="flex items-center justify-between">
                <div>
                  <div class="font-medium">{{ formatDateDisplay(date.date) }}</div>
                  <div 
                    :class="[
                      'text-xs',
                      selectedDate === date.date ? 'text-blue-100' : 'text-gray-500'
                    ]"
                  >
                    {{ date.recordCount }}条记录
                  </div>
                </div>
                <div
                  v-if="date.recordCount > 0"
                  :class="[
                    'w-2 h-2 rounded-full',
                    selectedDate === date.date ? 'bg-white' : 'bg-blue-500'
                  ]"
                ></div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 右侧内容展示区域 -->
      <div class="flex-1 overflow-y-auto">
        <div class="p-6">
          <!-- 顶部日期和统计信息 -->
          <div class="bg-white/80 backdrop-blur-sm rounded-2xl shadow-xl p-6 mb-6 border border-gray-200/50">
            <div class="flex items-center justify-between mb-4">
              <div>
                <h2 class="text-2xl font-bold text-gray-800">{{ formatSelectedDate() }}</h2>
                <p class="text-gray-500">今日健康监控数据</p>
              </div>
              <div class="flex space-x-2">
                <el-button
                  size="small"
                  @click="refreshData"
                  class="border-gray-300"
                >
                  <svg class="w-4 h-4 mr-1" fill="currentColor" viewBox="0 0 24 24">
                    <path d="M17.65 6.35C16.2 4.9 14.21 4 12 4c-4.42 0-7.99 3.58-7.99 8s3.57 8 7.99 8c3.73 0 6.84-2.55 7.73-6h-2.08c-.82 2.33-3.04 4-5.65 4-3.31 0-6-2.69-6-6s2.69-6 6-6c1.66 0 3.14.69 4.22 1.78L13 11h7V4l-2.35 2.35z"/>
                  </svg>
                  刷新
                </el-button>
              </div>
            </div>

            <!-- 快速统计卡片 -->
            <div class="grid grid-cols-2 md:grid-cols-4 gap-4">
              <div class="bg-gradient-to-r from-red-50 to-red-100 p-4 rounded-xl border border-red-200/50">
                <div class="flex items-center">
                  <div class="w-10 h-10 bg-red-100 rounded-lg flex items-center justify-center mr-2">
                    <svg class="w-5 h-5 text-red-600" fill="currentColor" viewBox="0 0 24 24">
                      <path d="M11 2c2.21 0 4 1.79 4 4v8.5c0 1.38 1.12 2.5 2.5 2.5s2.5-1.12 2.5-2.5c0-.28.22-.5.5-.5s.5.22.5.5c0 1.93-1.57 3.5-3.5 3.5s-3.5-1.57-3.5-3.5V6c0-1.1-.9-2-2-2s-2 .9-2 2v8.5c0 .28-.22.5-.5.5s-.5-.22-.5-.5V6c0-2.21 1.79-4 4-4z"/>
                    </svg>
                  </div>
                  <div>
                    <div class="text-xs text-red-600 font-medium">体温</div>
                    <div class="font-bold text-red-700">{{ dataUpdateTrigger >= 0 ? getLatestValue('bodyTemperature') : '-' }}°C</div>
                  </div>
                </div>
              </div>
              
              <div class="bg-gradient-to-r from-blue-50 to-blue-100 p-4 rounded-xl border border-blue-200/50">
                <div class="flex items-center">
                  <div class="w-10 h-10 bg-blue-100 rounded-lg flex items-center justify-center mr-2">
                    <svg class="w-5 h-5 text-blue-600" fill="currentColor" viewBox="0 0 24 24">
                      <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"/>
                    </svg>
                  </div>
                  <div>
                    <div class="text-xs text-blue-600 font-medium">血压</div>
                    <div class="font-bold text-blue-700">{{ dataUpdateTrigger >= 0 ? getLatestBloodPressure() : '-' }}</div>
                  </div>
                </div>
              </div>
              
              <div class="bg-gradient-to-r from-green-50 to-green-100 p-4 rounded-xl border border-green-200/50">
                <div class="flex items-center">
                  <div class="w-10 h-10 bg-green-100 rounded-lg flex items-center justify-center mr-2">
                    <svg class="w-5 h-5 text-green-600" fill="currentColor" viewBox="0 0 24 24">
                      <path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"/>
                    </svg>
                  </div>
                  <div>
                    <div class="text-xs text-green-600 font-medium">心率</div>
                    <div class="font-bold text-green-700">{{ dataUpdateTrigger >= 0 ? getLatestValue('heartRate') : '-' }}次/分</div>
                  </div>
                </div>
              </div>
              
              <div class="bg-gradient-to-r from-purple-50 to-purple-100 p-4 rounded-xl border border-purple-200/50">
                <div class="flex items-center">
                  <div class="w-10 h-10 bg-purple-100 rounded-lg flex items-center justify-center mr-2">
                    <svg class="w-5 h-5 text-purple-600" fill="currentColor" viewBox="0 0 24 24">
                      <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-2 15l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z"/>
                    </svg>
                  </div>
                  <div>
                    <div class="text-xs text-purple-600 font-medium">血糖</div>
                    <div class="font-bold text-purple-700">{{ dataUpdateTrigger >= 0 ? getLatestValue('bloodSugar') : '-' }}mmol/L</div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- 详细记录列表 -->
          <div class="space-y-4" v-if="dailyRecords.length > 0">
            <div
              v-for="record in dailyRecords"
              :key="record.id"
              class="bg-white/80 backdrop-blur-sm rounded-2xl shadow-lg p-6 border border-gray-200/50 hover:shadow-xl transition-all duration-300"
            >
              <!-- 记录头部 -->
              <div class="flex items-center justify-between mb-4">
                <div class="flex items-center">
                  <div class="bg-gradient-to-r from-blue-500 to-indigo-600 rounded-full p-2 mr-3">
                    <svg class="w-5 h-5 text-white" fill="currentColor" viewBox="0 0 24 24">
                      <path d="M11.99 2C6.47 2 2 6.48 2 12s4.47 10 9.99 10C17.52 22 22 17.52 22 12S17.52 2 11.99 2zM12 20c-4.42 0-8-3.58-8-8s3.58-8 8-8 8 3.58 8 8-3.58 8-8 8zm.5-13H11v6l5.25 3.15.75-1.23-4.5-2.67z"/>
                    </svg>
                  </div>
                  <div>
                    <h3 class="font-semibold text-gray-800">{{ record.recordTime }}</h3>
                    <p class="text-sm text-gray-500">测量人员：{{ record.measuredBy || '自助测量' }}</p>
                  </div>
                </div>
              </div>

              <!-- 健康指标网格 -->
              <div class="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
                <!-- 体温 -->
                <div v-if="record.bodyTemperature" class="health-metric">
                  <div class="metric-icon">
                    <div class="w-8 h-8 bg-red-50 border border-red-200 rounded-lg flex items-center justify-center">
                      <svg class="w-4 h-4 text-red-600" fill="currentColor" viewBox="0 0 24 24">
                        <path d="M11 2c2.21 0 4 1.79 4 4v8.5c0 1.38 1.12 2.5 2.5 2.5s2.5-1.12 2.5-2.5c0-.28.22-.5.5-.5s.5.22.5.5c0 1.93-1.57 3.5-3.5 3.5s-3.5-1.57-3.5-3.5V6c0-1.1-.9-2-2-2s-2 .9-2 2v8.5c0-.28-.22-.5-.5-.5s-.5.22-.5.5V6c0-2.21 1.79-4 4-4z"/>
                      </svg>
                    </div>
                  </div>
                  <div class="metric-content">
                    <div class="metric-label">体温</div>
                    <div class="metric-value">{{ record.bodyTemperature }}°C</div>
                  </div>
                </div>

                <!-- 血压 -->
                <div v-if="record.systolicPressure && record.diastolicPressure" class="health-metric">
                  <div class="metric-icon">
                    <div class="w-8 h-8 bg-blue-50 border border-blue-200 rounded-lg flex items-center justify-center">
                      <svg class="w-4 h-4 text-blue-600" fill="currentColor" viewBox="0 0 24 24">
                        <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"/>
                      </svg>
                    </div>
                  </div>
                  <div class="metric-content">
                    <div class="metric-label">血压</div>
                    <div class="metric-value">{{ record.systolicPressure }}/{{ record.diastolicPressure }}</div>
                  </div>
                </div>

                <!-- 心率 -->
                <div v-if="record.heartRate" class="health-metric">
                  <div class="metric-icon">
                    <div class="w-8 h-8 bg-green-50 border border-green-200 rounded-lg flex items-center justify-center">
                      <svg class="w-4 h-4 text-green-600" fill="currentColor" viewBox="0 0 24 24">
                        <path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"/>
                      </svg>
                    </div>
                  </div>
                  <div class="metric-content">
                    <div class="metric-label">心率</div>
                    <div class="metric-value">{{ record.heartRate }}次/分</div>
                  </div>
                </div>

                <!-- 血糖 -->
                <div v-if="record.bloodSugar" class="health-metric">
                  <div class="metric-icon">
                    <div class="w-8 h-8 bg-purple-50 border border-purple-200 rounded-lg flex items-center justify-center">
                      <svg class="w-4 h-4 text-purple-600" fill="currentColor" viewBox="0 0 24 24">
                        <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-2 15l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z"/>
                      </svg>
                    </div>
                  </div>
                  <div class="metric-content">
                    <div class="metric-label">血糖({{ getBloodSugarTypeLabel(record.bloodSugarType) }})</div>
                    <div class="metric-value">{{ record.bloodSugar }}mmol/L</div>
                  </div>
                </div>

                <!-- 血氧 -->
                <div v-if="record.bloodOxygen" class="health-metric">
                  <div class="metric-icon">
                    <div class="w-8 h-8 bg-cyan-50 border border-cyan-200 rounded-lg flex items-center justify-center">
                      <svg class="w-4 h-4 text-cyan-600" fill="currentColor" viewBox="0 0 24 24">
                        <path d="M7.5 4C5.5 4 4 5.5 4 7.5S5.5 11 7.5 11 11 9.5 11 7.5 9.5 4 7.5 4zm0 5C6.7 9 6 8.3 6 7.5S6.7 6 7.5 6 9 6.7 9 7.5 8.3 9 7.5 9zm8.5 2c-2 0-3.5 1.5-3.5 3.5s1.5 3.5 3.5 3.5 3.5-1.5 3.5-3.5-1.5-3.5-3.5-3.5zm0 5c-.8 0-1.5-.7-1.5-1.5s.7-1.5 1.5-1.5 1.5.7 1.5 1.5-.7 1.5-1.5 1.5z"/>
                      </svg>
                    </div>
                  </div>
                  <div class="metric-content">
                    <div class="metric-label">血氧</div>
                    <div class="metric-value">{{ record.bloodOxygen }}%</div>
                  </div>
                </div>

                <!-- 体重 -->
                <div v-if="record.weight" class="health-metric">
                  <div class="metric-icon">
                    <div class="w-8 h-8 bg-orange-50 border border-orange-200 rounded-lg flex items-center justify-center">
                      <svg class="w-4 h-4 text-orange-600" fill="currentColor" viewBox="0 0 24 24">
                        <path d="M13.5.67s.74 2.65.74 4.8c0 2.06-1.35 3.73-3.41 3.73-2.07 0-3.63-1.67-3.63-3.73l.03-.36C5.21 7.51 4 10.62 4 14c0 4.42 3.58 8 8 8s8-3.58 8-8C20 8.61 17.41 3.8 13.5.67zM11.71 19c-1.78 0-3.22-1.4-3.22-3.14 0-1.62 1.05-2.76 2.81-3.12 1.77-.36 3.6-1.21 4.62-2.58.39 1.29.59 2.65.59 4.04 0 2.65-2.15 4.8-4.8 4.8z"/>
                      </svg>
                    </div>
                  </div>
                  <div class="metric-content">
                    <div class="metric-label">体重</div>
                    <div class="metric-value">{{ record.weight }}kg</div>
                  </div>
                </div>

                <!-- 睡眠 -->
                <div v-if="record.sleepHours" class="health-metric">
                  <div class="metric-icon">
                    <div class="w-8 h-8 bg-indigo-50 border border-indigo-200 rounded-lg flex items-center justify-center">
                      <svg class="w-4 h-4 text-indigo-600" fill="currentColor" viewBox="0 0 24 24">
                        <path d="M6 6.08V4h-.5c-.83 0-1.5.67-1.5 1.5v9c0 .83.67 1.5 1.5 1.5H18c.83 0 1.5-.67 1.5-1.5v-9c0-.83-.67-1.5-1.5-1.5H6.92L6 6.08z"/>
                      </svg>
                    </div>
                  </div>
                  <div class="metric-content">
                    <div class="metric-label">睡眠</div>
                    <div class="metric-value">{{ record.sleepHours }}小时</div>
                    <div class="metric-sub">{{ getSleepQualityLabel(record.sleepQuality) }}</div>
                  </div>
                </div>

                <!-- 情绪 -->
                <div v-if="record.moodStatus" class="health-metric">
                  <div class="metric-icon">
                    <div class="w-8 h-8 bg-yellow-50 border border-yellow-200 rounded-lg flex items-center justify-center">
                      <svg class="w-4 h-4 text-yellow-600" fill="currentColor" viewBox="0 0 24 24">
                        <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-2 15l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z"/>
                      </svg>
                    </div>
                  </div>
                  <div class="metric-content">
                    <div class="metric-label">情绪</div>
                    <div class="metric-value">{{ getMoodStatusLabel(record.moodStatus) }}</div>
                  </div>
                </div>
              </div>

              <!-- 附加信息 -->
              <div v-if="record.physicalActivity || record.abnormalSymptoms || record.remarks" class="mt-4 pt-4 border-t border-gray-200">
                <div v-if="record.physicalActivity" class="mb-2">
                  <span class="text-sm font-medium text-gray-600">体力活动：</span>
                  <span class="text-sm text-gray-800">{{ record.physicalActivity }}</span>
                </div>
                <div v-if="record.abnormalSymptoms" class="mb-2">
                  <span class="text-sm font-medium text-red-600">异常症状：</span>
                  <span class="text-sm text-red-700">{{ record.abnormalSymptoms }}</span>
                </div>
                <div v-if="record.remarks" class="mb-2">
                  <span class="text-sm font-medium text-gray-600">备注：</span>
                  <span class="text-sm text-gray-800">{{ record.remarks }}</span>
                </div>
              </div>
            </div>
          </div>

          <!-- 空状态 -->
          <div v-else class="bg-white/80 backdrop-blur-sm rounded-2xl shadow-lg p-12 text-center border border-gray-200/50">
            <div class="bg-gray-100 rounded-full w-20 h-20 flex items-center justify-center mx-auto mb-4">
              <svg class="w-8 h-8 text-gray-400" fill="currentColor" viewBox="0 0 24 24">
                <path d="M3 13h8V3H3v10zm0 8h8v-6H3v6zm10 0h8V11h-8v10zm5-18v4h3V3h-3z"/>
              </svg>
            </div>
            <h3 class="text-lg font-medium text-gray-800 mb-2">暂无监控数据</h3>
            <p class="text-gray-500 mb-4">{{ formatSelectedDate() }}还没有健康监控记录</p>
          </div>
        </div>
      </div>
    </div>


  </div>
</template>

<script>
import request from '@/utils/request'

export default {
  name: 'HealthMonitoring',
  data() {
    return {
      user: JSON.parse(localStorage.getItem("xm-user") || "{}"),
      selectedMonth: this.getCurrentMonth(),
      selectedDate: this.getCurrentDate(),
      dateList: [],
      dailyRecords: []
    }
  },
  computed: {
    // 添加一个响应式计算属性来强制更新
    dataUpdateTrigger() {
      // 这个计算属性会在dailyRecords变化时自动重新计算
      return this.dailyRecords?.length || 0
    }
  },
  created() {
    this.loadDateList()
    this.loadDailyRecords()
  },
  watch: {
    // 监听选中日期变化
    selectedDate: {
      handler(newDate, oldDate) {
        console.log('📅 日期切换:', oldDate, '->', newDate)
        if (newDate !== oldDate) {
          this.loadDailyRecords()
        }
      },
      immediate: false
    },
    // 监听数据变化
    dailyRecords: {
      handler(newRecords, oldRecords) {
        console.log('📊 数据监听器触发 - 旧记录数:', oldRecords?.length || 0, '新记录数:', newRecords?.length || 0)
        console.log('🔄 强制更新视图')
        this.$nextTick(() => {
          this.$forceUpdate()
        })
      },
      immediate: true,
      deep: true
    }
  },
  methods: {
    // 初始化方法
    getCurrentMonth() {
      const now = new Date()
      return `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}`
    },
    getCurrentDate() {
      const now = new Date()
      return `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}-${String(now.getDate()).padStart(2, '0')}`
    },


    // 数据加载方法
    async loadDateList() {
      try {
        const res = await request.get(`/health-monitoring/getDateList`, {
          params: {
            userId: this.user.id,
            month: this.selectedMonth
          }
        })
        if (res.code === '200') {
          this.dateList = res.data || []
        }
      } catch (error) {
        console.error('加载日期列表失败:', error)
      }
    },
    async loadDailyRecords() {
      try {
        // 确保传递给后端的日期格式正确
        const dateParam = this.formatDateToString(this.selectedDate)
        if (!dateParam) {
          console.error('无法格式化日期:', this.selectedDate)
          return
        }
        
        console.log('📅 开始加载数据 - 用户ID:', this.user.id, '原始日期:', this.selectedDate, '传递日期:', dateParam)
        
        const res = await request.get(`/health-monitoring/getDailyRecords`, {
          params: {
            userId: this.user.id,
            date: dateParam
          }
        })
        
        console.log('📊 API响应:', res)
        
        if (res.code === '200') {
          const newRecords = res.data || []
          console.log('🔄 更新前记录数:', this.dailyRecords.length, '更新后记录数:', newRecords.length)
          console.log('📋 新记录详情:', newRecords)
          
          // 使用Vue.set确保响应式更新
          this.$set(this, 'dailyRecords', newRecords)
          
          // 强制重新渲染
          this.$nextTick(() => {
            console.log('✅ 视图已更新, 当前记录数:', this.dailyRecords.length)
            this.$forceUpdate()
          })
        } else {
          console.warn('⚠️ API返回错误:', res)
          this.$set(this, 'dailyRecords', [])
        }
      } catch (error) {
        console.error('❌ 加载每日记录失败:', error)
        this.$set(this, 'dailyRecords', [])
      }
    },

    // 事件处理方法
    onMonthChange() {
      this.loadDateList()
    },
    selectDate(date) {
      console.log('🎯 用户点击日期:', date, '类型:', typeof date)
      
      const formattedDate = this.formatDateToString(date)
      if (!formattedDate) {
        console.error('日期格式转换失败:', date)
        return
      }
      
      console.log('📅 格式化后的日期:', formattedDate)
      this.selectedDate = formattedDate
    },
    refreshData() {
      this.loadDateList()
      this.loadDailyRecords()
    },





    // 工具方法
    /**
     * 格式化日期为 YYYY-MM-DD 格式
     * 支持 Date 对象、时间戳、字符串等多种输入
     */
    formatDateToString(date) {
      if (!date) return null
      
      let dateObj
      if (date instanceof Date) {
        dateObj = date
      } else if (typeof date === 'number') {
        dateObj = new Date(date)
      } else if (typeof date === 'string') {
        // 如果已经是 YYYY-MM-DD 格式，直接返回
        if (date.match(/^\d{4}-\d{2}-\d{2}$/)) {
          return date
        }
        dateObj = new Date(date)
      } else {
        return null
      }
      
      // 验证日期有效性
      if (isNaN(dateObj.getTime())) {
        console.error('无效日期:', date)
        return null
      }
      
      return dateObj.toISOString().split('T')[0]
    },
    
    formatDateDisplay(date) {
      const d = new Date(date)

      const yesterday = new Date()
      yesterday.setDate(yesterday.getDate() - 1)
      
      if (date === this.getCurrentDate()) {
        return '今天'
      } else if (date === yesterday.toISOString().split('T')[0]) {
        return '昨天'
      } else {
        return `${d.getMonth() + 1}月${d.getDate()}日`
      }
    },
    formatSelectedDate() {
      const d = new Date(this.selectedDate)
      // const today = new Date()
      
      if (this.selectedDate === this.getCurrentDate()) {
        return '今天'
      } else {
        return `${d.getFullYear()}年${d.getMonth() + 1}月${d.getDate()}日`
      }
    },
             getLatestValue(field) {
      console.log('🔍 getLatestValue调用:', field, '记录数量:', this.dailyRecords?.length || 0)
      if (!this.dailyRecords || this.dailyRecords.length === 0) {
        console.log('📭 无记录数据')
        return '-'
      }
      const latest = this.dailyRecords[0]
      let value = latest[field]
      
      // 处理可能的数组格式数据
      if (Array.isArray(value)) {
        value = value.length > 0 ? value[value.length - 1] : '-'
      } else if (typeof value === 'string' && value.startsWith('[') && value.endsWith(']')) {
        try {
          const parsed = JSON.parse(value)
          if (Array.isArray(parsed)) {
            value = parsed.length > 0 ? parsed[parsed.length - 1] : '-'
          }
        } catch (e) {
          console.warn('解析数组字符串失败:', value)
        }
      }
      
      const result = value || '-'
      console.log('📊 字段', field, '的值:', result, '来自记录:', latest.id)
      return result
    },
    getLatestBloodPressure() {
      console.log('🩺 getLatestBloodPressure调用, 记录数量:', this.dailyRecords?.length || 0)
      if (!this.dailyRecords || this.dailyRecords.length === 0) {
        console.log('📭 无血压记录数据')
        return '-'
      }
      const latest = this.dailyRecords[0]
      
      let systolic = latest.systolicPressure
      let diastolic = latest.diastolicPressure
      
      // 处理收缩压可能的数组格式
      if (Array.isArray(systolic)) {
        systolic = systolic.length > 0 ? systolic[systolic.length - 1] : null
      } else if (typeof systolic === 'string' && systolic.startsWith('[') && systolic.endsWith(']')) {
        try {
          const parsed = JSON.parse(systolic)
          if (Array.isArray(parsed)) {
            systolic = parsed.length > 0 ? parsed[parsed.length - 1] : null
          }
        } catch (e) {
          console.warn('解析收缩压数组字符串失败:', systolic)
        }
      }
      
      // 处理舒张压可能的数组格式
      if (Array.isArray(diastolic)) {
        diastolic = diastolic.length > 0 ? diastolic[diastolic.length - 1] : null
      } else if (typeof diastolic === 'string' && diastolic.startsWith('[') && diastolic.endsWith(']')) {
        try {
          const parsed = JSON.parse(diastolic)
          if (Array.isArray(parsed)) {
            diastolic = parsed.length > 0 ? parsed[parsed.length - 1] : null
          }
        } catch (e) {
          console.warn('解析舒张压数组字符串失败:', diastolic)
        }
      }
      
      if (systolic && diastolic) {
        const bp = `${systolic}/${diastolic}`
        console.log('💓 血压值:', bp, '来自记录:', latest.id)
        return bp
      }
      console.log('⚠️ 血压数据不完整:', systolic, '/', diastolic)
      return '-'
    },
    getBloodSugarTypeLabel(type) {
      const types = {
        'FASTING': '空腹',
        'POSTPRANDIAL': '餐后',
        'RANDOM': '随机'
      }
      return types[type] || ''
    },
    getSleepQualityLabel(quality) {
      const qualities = {
        'EXCELLENT': '优秀',
        'GOOD': '良好',
        'FAIR': '一般',
        'POOR': '较差'
      }
      return qualities[quality] || ''
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
/* 健康指标卡片样式 */
.health-metric {
  @apply flex items-center p-3 bg-white rounded-lg border border-gray-200/60 hover:border-gray-300 hover:shadow-md transition-all duration-200 shadow-sm;
}

.metric-icon {
  @apply mr-3;
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

.metric-sub {
  @apply text-xs text-gray-500;
}



/* 滚动条样式 */
::-webkit-scrollbar {
  width: 6px;
}

::-webkit-scrollbar-track {
  background: rgba(0, 0, 0, 0.05);
  border-radius: 3px;
}

::-webkit-scrollbar-thumb {
  background: rgba(0, 0, 0, 0.2);
  border-radius: 3px;
}

::-webkit-scrollbar-thumb:hover {
  background: rgba(0, 0, 0, 0.3);
}
</style> 