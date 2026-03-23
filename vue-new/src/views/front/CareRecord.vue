<template>
  <div class="min-h-screen bg-gray-50">
    <!-- 页面头部 -->
    <div class="relative overflow-hidden bg-slate-700 rounded-xl p-8 mb-8 shadow-xl">
      <div class="absolute top-0 right-0 w-32 h-32 opacity-10">
        <svg viewBox="0 0 100 100" class="w-full h-full">
          <path d="M20,20 L50,10 L80,20 L80,50 L50,60 L20,50 Z" fill="white" />
        </svg>
      </div>
      <div class="relative z-10 flex items-center justify-between">
        <div>
          <h1 class="text-3xl font-bold text-white mb-2 flex items-center">
            <i class="el-icon-notebook-2 mr-3 text-4xl"></i>
            我的护理记录
          </h1>
          <p class="text-slate-200 text-lg">专业护理团队为您提供贴心服务，记录您的健康成长历程</p>
        </div>
        <div class="hidden md:flex items-center space-x-4">
          <div class="text-center">
            <div class="text-2xl font-bold text-white">{{ totalRecords }}</div>
            <div class="text-slate-200 text-sm">总记录数</div>
          </div>
          <div class="w-px h-12 bg-slate-300"></div>
          <div class="text-center">
            <div class="text-2xl font-bold text-white">{{ recentDays }}</div>
            <div class="text-slate-200 text-sm">最近天数</div>
          </div>
        </div>
      </div>
    </div>

    <!-- 主要内容区域 - 左右分栏布局 -->
    <div class="grid grid-cols-12 gap-8 min-h-[calc(100vh-300px)]">
      <!-- 左侧日期选择区域 -->
      <div class="col-span-12 lg:col-span-4 xl:col-span-3">
        <!-- 筛选工具栏 -->
        <div class="bg-white rounded-xl shadow-lg p-6 mb-6 border border-gray-100">
          <h3 class="text-lg font-semibold text-gray-800 mb-4 flex items-center">
            <i class="el-icon-filter text-slate-600 mr-2"></i>
            筛选条件
          </h3>
          <div class="space-y-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">护理类型</label>
              <el-select 
                v-model="searchForm.careType" 
                placeholder="选择护理类型" 
                clearable
                size="medium"
                class="w-full"
              >
                <el-option label="日常照顾" value="DAILY_CARE">
                  <span class="flex items-center">
                    <i class="el-icon-house text-blue-500 mr-2"></i>
                    日常照顾
                  </span>
                </el-option>
                <el-option label="饮食护理" value="DIET">
                  <span class="flex items-center">
                    <i class="el-icon-food text-orange-500 mr-2"></i>
                    饮食护理
                  </span>
                </el-option>
                <el-option label="运动护理" value="EXERCISE">
                  <span class="flex items-center">
                    <i class="el-icon-bicycle text-green-500 mr-2"></i>
                    运动护理
                  </span>
                </el-option>
                <el-option label="医疗护理" value="MEDICAL">
                  <span class="flex items-center">
                    <i class="el-icon-first-aid-kit text-red-500 mr-2"></i>
                    医疗护理
                  </span>
                </el-option>
              </el-select>
            </div>
            
<!--            <div>-->
<!--              <label class="block text-sm font-medium text-gray-700 mb-2">情绪状态</label>-->
<!--              <el-select -->
<!--                v-model="searchForm.moodStatus" -->
<!--                placeholder="选择情绪状态" -->
<!--                clearable-->
<!--                size="medium"-->
<!--                class="w-full"-->
<!--              >-->
<!--                <el-option label="良好" value="GOOD">-->
<!--                  <span class="flex items-center">-->
<!--                    <i class="el-icon-sunny text-green-500 mr-2"></i>-->
<!--                    良好-->
<!--                  </span>-->
<!--                </el-option>-->
<!--                <el-option label="一般" value="NORMAL">-->
<!--                  <span class="flex items-center">-->
<!--                    <i class="el-icon-cloudy text-yellow-500 mr-2"></i>-->
<!--                    一般-->
<!--                  </span>-->
<!--                </el-option>-->
<!--                <el-option label="较差" value="POOR">-->
<!--                  <span class="flex items-center">-->
<!--                    <i class="el-icon-heavy-rain text-red-500 mr-2"></i>-->
<!--                    较差-->
<!--                  </span>-->
<!--                </el-option>-->
<!--              </el-select>-->
<!--            </div>-->
            
            <div class="flex space-x-2">
              <el-button type="primary" @click="handleSearch" icon="el-icon-search" size="medium" class="flex-1">
                筛选
              </el-button>
              <el-button @click="resetSearch" icon="el-icon-refresh" size="medium" class="flex-1">
                重置
              </el-button>
            </div>
          </div>
        </div>

        <!-- 日期选择器 -->
        <div class="bg-white rounded-xl shadow-lg border border-gray-100 overflow-hidden">
          <div class="p-4 bg-slate-600">
            <h3 class="text-lg font-semibold text-white flex items-center">
              <i class="el-icon-date mr-2"></i>
              选择日期
            </h3>
            <p class="text-slate-200 text-sm mt-1">点击日期查看当天的护理记录</p>
          </div>
          
          <div class="p-4">
            <el-calendar v-model="selectedDate" @input="handleDateSelect">
              <template slot="dateCell" slot-scope="{date, data}">
                <div class="custom-calendar-cell" 
                     :class="{ 
                       'has-records': hasRecordsOnDate(data.day),
                       'selected': isSelectedDate(data.day)
                     }"
                     @click="selectDate(data.day)">
                  <div class="date-number">{{ data.day.split('-').slice(-1)[0] }}</div>
                  <div v-if="hasRecordsOnDate(data.day)" class="record-indicator">
                    <div class="record-dot"></div>
                  </div>
                </div>
              </template>
            </el-calendar>
          </div>
        </div>

        <!-- 日期统计 -->
        <div class="bg-white rounded-xl shadow-lg p-6 mt-6 border border-gray-100">
          <h3 class="text-lg font-semibold text-gray-800 mb-4 flex items-center">
            <i class="el-icon-pie-chart text-slate-600 mr-2"></i>
            记录统计
          </h3>
          <div class="space-y-3">
            <div class="flex items-center justify-between">
              <span class="text-gray-600">总记录数</span>
              <span class="font-semibold text-slate-700">{{ totalRecords }}</span>
            </div>
            <div class="flex items-center justify-between">
              <span class="text-gray-600">当前月份</span>
              <span class="font-semibold text-slate-700">{{ getCurrentMonthRecords() }}</span>
            </div>
            <div class="flex items-center justify-between">
              <span class="text-gray-600">本周记录</span>
              <span class="font-semibold text-slate-700">{{ getCurrentWeekRecords() }}</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 右侧护理记录展示区域 -->
      <div class="col-span-12 lg:col-span-8 xl:col-span-9">
        <!-- 选中日期的标题 -->
        <div class="bg-white rounded-xl shadow-lg p-6 mb-6 border border-gray-100">
          <div class="flex items-center justify-between">
            <div>
              <h2 class="text-2xl font-bold text-gray-800 flex items-center">
                <i class="el-icon-calendar text-slate-600 mr-3"></i>
                {{ formatSelectedDate() }} 的护理记录
              </h2>
              <p class="text-gray-600 mt-1">
                共 {{ currentDateRecords.length }} 条记录
                <span v-if="currentDateRecords.length > 0">
                  · 最新更新于 {{ getLatestUpdateTime() }}
                </span>
              </p>
            </div>
            <div class="flex items-center space-x-3">
              <el-button-group>
                <el-button 
                  :type="viewMode === 'timeline' ? 'primary' : ''"
                  @click="viewMode = 'timeline'"
                  icon="el-icon-time"
                  size="medium">
                  时间轴
                </el-button>
                <el-button 
                  :type="viewMode === 'cards' ? 'primary' : ''"
                  @click="viewMode = 'cards'"
                  icon="el-icon-menu"
                  size="medium">
                  卡片
                </el-button>
              </el-button-group>
            </div>
          </div>
        </div>

        <!-- 无数据状态 -->
        <div v-if="!loading && currentDateRecords.length === 0" class="text-center py-20">
          <div class="inline-block p-8 bg-white rounded-full shadow-lg mb-6">
            <i class="el-icon-calendar text-6xl text-gray-300"></i>
          </div>
          <h3 class="text-xl font-semibold text-gray-600 mb-2">{{ formatSelectedDate() }} 暂无护理记录</h3>
          <p class="text-gray-500">这一天还没有护理记录，请选择其他日期查看</p>
        </div>

        <!-- 时间轴视图 -->
        <div v-else-if="viewMode === 'timeline'" class="relative">
          <!-- 时间轴线 -->
          <div class="absolute left-8 top-0 bottom-0 w-0.5 bg-slate-300"></div>
          
          <div v-loading="loading" class="space-y-8">
            <div 
              v-for="record in currentDateRecords" 
              :key="record.id"
              class="relative flex items-start"
            >
              <!-- 时间轴节点 -->
              <div class="absolute left-6 w-4 h-4 rounded-full border-4 border-white shadow-lg transform -translate-x-1/2"
                   :class="getCareTypeColor(record.careType)">
              </div>
              
              <!-- 记录卡片 -->
              <div class="ml-16 flex-1">
                <div class="bg-white rounded-xl shadow-lg hover:shadow-xl transition-all duration-300 overflow-hidden border border-gray-100 hover:border-blue-200">
                  <!-- 卡片头部 -->
                  <div class="p-6 border-b border-gray-100"
                       :class="getCareTypeGradient(record.careType)">
                    <div class="flex items-center justify-between">
                      <div class="flex items-center space-x-3">
                        <div class="w-12 h-12 rounded-lg bg-white/20 flex items-center justify-center">
                          <i :class="[getCareTypeIcon(record.careType), 'text-2xl text-white']"></i>
                        </div>
                        <div>
                          <h3 class="text-lg font-semibold text-white">{{ getCareTypeName(record.careType) }}</h3>
                          <p class="text-white/80 text-sm">{{ record.recordDate }} · {{ record.nurseName || '护理人员' }}</p>
                        </div>
                      </div>
                      <div class="flex items-center space-x-2">
                        <el-tag :type="getMoodStatusTagType(record.moodStatus)" size="medium">
                          <i :class="[getMoodStatusIcon(record.moodStatus), 'mr-1']"></i>
                          {{ getMoodStatusName(record.moodStatus) }}
                        </el-tag>
                      </div>
                    </div>
                  </div>
                  
                  <!-- 卡片内容 -->
                  <div class="p-6">
                    <!-- 护理内容 -->
                    <div class="mb-6">
                      <h4 class="text-sm font-medium text-gray-500 mb-2 flex items-center">
                        <i class="el-icon-document text-purple-500 mr-2"></i>
                        护理内容
                      </h4>
                      <p class="text-gray-800 leading-relaxed bg-gray-50 rounded-lg p-4">{{ record.careContent }}</p>
                    </div>
                    
                    <!-- 生命体征 -->
                    <div v-if="hasVitalSigns(record)" class="mb-6">
                      <h4 class="text-sm font-medium text-gray-500 mb-3 flex items-center">
                        <i class="el-icon-first-aid-kit text-red-500 mr-2"></i>
                        生命体征
                      </h4>
                      <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
                        <div v-if="record.bloodPressure" class="bg-gradient-to-r from-red-50 to-pink-50 rounded-lg p-4 border border-red-100">
                          <div class="flex items-center justify-between">
                            <div>
                              <div class="text-red-600 text-sm font-medium">血压</div>
                              <div class="text-red-800 text-lg font-bold">{{ record.bloodPressure }}</div>
                            </div>
                            <i class="el-icon-heart text-2xl text-red-400"></i>
                          </div>
                        </div>
                        <div v-if="record.heartRate" class="bg-gradient-to-r from-blue-50 to-cyan-50 rounded-lg p-4 border border-blue-100">
                          <div class="flex items-center justify-between">
                            <div>
                              <div class="text-blue-600 text-sm font-medium">心率</div>
                              <div class="text-blue-800 text-lg font-bold">{{ record.heartRate }} 次/分</div>
                            </div>
                            <i class="el-icon-heartbeat text-2xl text-blue-400"></i>
                          </div>
                        </div>
                        <div v-if="record.temperature" class="bg-gradient-to-r from-orange-50 to-yellow-50 rounded-lg p-4 border border-orange-100">
                          <div class="flex items-center justify-between">
                            <div>
                              <div class="text-orange-600 text-sm font-medium">体温</div>
                              <div class="text-orange-800 text-lg font-bold">{{ record.temperature }}°C</div>
                            </div>
                            <i class="el-icon-thermometer text-2xl text-orange-400"></i>
                          </div>
                        </div>
                      </div>
                    </div>
                    
                    <!-- 饮食运动记录 -->
                    <div v-if="record.dietRecord || record.exerciseRecord" class="mb-6">
                      <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                        <div v-if="record.dietRecord" class="bg-gradient-to-r from-green-50 to-emerald-50 rounded-lg p-4 border border-green-100">
                          <h5 class="text-green-700 font-medium mb-2 flex items-center">
                            <i class="el-icon-food text-green-500 mr-2"></i>
                            饮食记录
                          </h5>
                          <p class="text-green-800 text-sm leading-relaxed">{{ record.dietRecord }}</p>
                        </div>
                        <div v-if="record.exerciseRecord" class="bg-gradient-to-r from-blue-50 to-indigo-50 rounded-lg p-4 border border-blue-100">
                          <h5 class="text-blue-700 font-medium mb-2 flex items-center">
                            <i class="el-icon-bicycle text-blue-500 mr-2"></i>
                            运动记录
                          </h5>
                          <p class="text-blue-800 text-sm leading-relaxed">{{ record.exerciseRecord }}</p>
                        </div>
                      </div>
                    </div>
                    
                    <!-- 异常情况和备注 -->
                    <div v-if="record.abnormalSituation || record.remarks" class="space-y-4">
                      <div v-if="record.abnormalSituation" class="bg-gradient-to-r from-yellow-50 to-amber-50 rounded-lg p-4 border border-yellow-200">
                        <h5 class="text-yellow-700 font-medium mb-2 flex items-center">
                          <i class="el-icon-warning text-yellow-500 mr-2"></i>
                          异常情况
                        </h5>
                        <p class="text-yellow-800 text-sm leading-relaxed">{{ record.abnormalSituation }}</p>
                      </div>
                      <div v-if="record.remarks" class="bg-gradient-to-r from-gray-50 to-slate-50 rounded-lg p-4 border border-gray-200">
                        <h5 class="text-gray-700 font-medium mb-2 flex items-center">
                          <i class="el-icon-chat-line-square text-gray-500 mr-2"></i>
                          备注信息
                        </h5>
                        <p class="text-gray-800 text-sm leading-relaxed">{{ record.remarks }}</p>
                      </div>
                    </div>
                  </div>
                  
                  <!-- 卡片底部 -->
                  <div class="px-6 py-3 bg-gray-50 border-t border-gray-100">
                    <div class="flex items-center justify-between text-xs text-gray-500">
                      <span>记录时间：{{ record.createTime }}</span>
                      <span class="flex items-center">
                        <i class="el-icon-time mr-1"></i>
                        {{ getTimeAgo(record.createTime) }}
                      </span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 卡片视图 -->
        <div v-else class="grid grid-cols-1 xl:grid-cols-2 gap-6" v-loading="loading">
          <div 
            v-for="record in currentDateRecords" 
            :key="record.id"
            class="bg-white rounded-xl shadow-lg hover:shadow-xl transition-all duration-300 overflow-hidden border border-gray-100 hover:border-blue-200 transform hover:-translate-y-1"
          >
            <!-- 卡片头部 -->
            <div class="h-2" :class="getCareTypeGradient(record.careType)"></div>
            <div class="p-6">
              <div class="flex items-center justify-between mb-4">
                <div class="flex items-center space-x-3">
                  <div class="w-10 h-10 rounded-lg flex items-center justify-center"
                       :class="getCareTypeBackground(record.careType)">
                    <i :class="[getCareTypeIcon(record.careType), 'text-xl']" 
                       :style="{color: getCareTypeIconColor(record.careType)}"></i>
                  </div>
                  <div>
                    <h3 class="font-semibold text-gray-800">{{ getCareTypeName(record.careType) }}</h3>
                    <p class="text-sm text-gray-500">{{ record.recordDate }}</p>
                  </div>
                </div>
                <el-tag :type="getMoodStatusTagType(record.moodStatus)" size="small">
                  {{ getMoodStatusName(record.moodStatus) }}
                </el-tag>
              </div>
              
              <p class="text-gray-700 text-sm leading-relaxed mb-4 line-clamp-3">{{ record.careContent }}</p>
              
              <!-- 生命体征简要显示 -->
              <div v-if="hasVitalSigns(record)" class="flex items-center space-x-4 text-xs text-gray-500 mb-4">
                <span v-if="record.bloodPressure" class="flex items-center">
                  <i class="el-icon-heart text-red-400 mr-1"></i>
                  {{ record.bloodPressure }}
                </span>
                <span v-if="record.heartRate" class="flex items-center">
                  <i class="el-icon-heartbeat text-blue-400 mr-1"></i>
                  {{ record.heartRate }}
                </span>
                <span v-if="record.temperature" class="flex items-center">
                  <i class="el-icon-thermometer text-orange-400 mr-1"></i>
                  {{ record.temperature }}°C
                </span>
              </div>
              
              <div class="flex items-center justify-between text-xs text-gray-400">
                <span>{{ record.nurseName || '护理人员' }}</span>
                <span>{{ getTimeAgo(record.createTime) }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "FrontCareRecord",
  data() {
    return {
      user: JSON.parse(localStorage.getItem("xm-user") || "{}"),
      
      // 搜索条件
      searchForm: {
        careType: "",
        moodStatus: ""
      },
      
      // 视图模式
      viewMode: 'timeline', // timeline | cards
      
      // 日期相关
      selectedDate: new Date(),
      
      // 数据
      allRecords: [], // 所有护理记录
      currentDateRecords: [], // 当前选中日期的护理记录
      loading: false,
      
      // 统计
      totalRecords: 0,
      recentDays: 0
    };
  },
  created() {
    this.fetchData();
  },
  methods: {
    // 获取护理记录数据
    fetchData() {
      this.loading = true;
      
      const params = {
        userId: this.user.id
      };
      
      this.$request.get("/care-record/selectAll", { params })
        .then(res => {
          if (res.code === '200') {
            this.allRecords = res.data || [];
            this.totalRecords = this.allRecords.length;
            
            // 计算最近天数
            if (this.allRecords.length > 0) {
              const dates = this.allRecords.map(record => new Date(record.recordDate)).sort((a, b) => a - b);
              const earliest = dates[0];
              const latest = dates[dates.length - 1];
              this.recentDays = Math.ceil((latest - earliest) / (1000 * 60 * 60 * 24)) + 1;
            }
            
            // 过滤当前选中日期的记录
            this.filterRecordsByDate();
          } else {
            this.$message.error(res.msg || "获取护理记录失败");
          }
        })
        .catch(() => {
          this.$message.error("获取护理记录异常");
        })
        .finally(() => {
          this.loading = false;
        });
    },
    
    // 根据选中日期和筛选条件过滤记录
    filterRecordsByDate() {
      const selectedDateStr = this.formatDate(this.selectedDate);
      
      let filteredRecords = this.allRecords.filter(record => {
        // 按日期筛选
        if (record.recordDate !== selectedDateStr) {
          return false;
        }
        
        // 按护理类型筛选
        if (this.searchForm.careType && record.careType !== this.searchForm.careType) {
          return false;
        }
        
        // 按情绪状态筛选
        if (this.searchForm.moodStatus && record.moodStatus !== this.searchForm.moodStatus) {
          return false;
        }
        
        return true;
      });
      
      // 按创建时间排序（最新的在前）
      this.currentDateRecords = filteredRecords.sort((a, b) => new Date(b.createTime) - new Date(a.createTime));
    },
    
    // 处理日期选择
    handleDateSelect(date) {
      this.selectedDate = date;
      this.filterRecordsByDate();
    },
    
    // 选择特定日期
    selectDate(dateStr) {
      this.selectedDate = new Date(dateStr);
      this.filterRecordsByDate();
    },
    
    // 检查某日期是否有记录
    hasRecordsOnDate(dateStr) {
      return this.allRecords.some(record => record.recordDate === dateStr);
    },
    
    // 检查是否是选中的日期
    isSelectedDate(dateStr) {
      return this.formatDate(this.selectedDate) === dateStr;
    },
    
    // 格式化日期
    formatDate(date) {
      const d = new Date(date);
      const year = d.getFullYear();
      const month = String(d.getMonth() + 1).padStart(2, '0');
      const day = String(d.getDate()).padStart(2, '0');
      return `${year}-${month}-${day}`;
    },
    
    // 格式化选中日期显示
    formatSelectedDate() {
      const date = new Date(this.selectedDate);
      const year = date.getFullYear();
      const month = date.getMonth() + 1;
      const day = date.getDate();
      const weekDays = ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'];
      const weekDay = weekDays[date.getDay()];
      
      return `${year}年${month}月${day}日 ${weekDay}`;
    },
    
    // 获取最新更新时间
    getLatestUpdateTime() {
      if (this.currentDateRecords.length === 0) return '';
      
      const latest = this.currentDateRecords.reduce((prev, current) => {
        return new Date(current.createTime) > new Date(prev.createTime) ? current : prev;
      });
      
      return latest.createTime;
    },
    
    // 获取当前月份记录数
    getCurrentMonthRecords() {
      const currentMonth = new Date().getMonth();
      const currentYear = new Date().getFullYear();
      
      return this.allRecords.filter(record => {
        const recordDate = new Date(record.recordDate);
        return recordDate.getMonth() === currentMonth && recordDate.getFullYear() === currentYear;
      }).length;
    },
    
    // 获取当前周记录数
    getCurrentWeekRecords() {
      const now = new Date();
      const startOfWeek = new Date(now.setDate(now.getDate() - now.getDay()));
      const endOfWeek = new Date(now.setDate(now.getDate() - now.getDay() + 6));
      
      return this.allRecords.filter(record => {
        const recordDate = new Date(record.recordDate);
        return recordDate >= startOfWeek && recordDate <= endOfWeek;
      }).length;
    },
    
    // 搜索
    handleSearch() {
      this.filterRecordsByDate();
    },
    
    // 重置搜索
    resetSearch() {
      this.searchForm = {
        careType: "",
        moodStatus: ""
      };
      this.filterRecordsByDate();
    },
    
    // 判断是否有生命体征数据
    hasVitalSigns(record) {
      return record.bloodPressure || record.heartRate || record.temperature;
    },
    
    // 获取护理类型名称
    getCareTypeName(careType) {
      switch (careType) {
        case 'DAILY_CARE': return '日常照顾';
        case 'DIET': return '饮食护理';
        case 'EXERCISE': return '运动护理';
        case 'MEDICAL': return '医疗护理';
        default: return '其他护理';
      }
    },
    
    // 获取护理类型图标
    getCareTypeIcon(careType) {
      switch (careType) {
        case 'DAILY_CARE': return 'el-icon-house';
        case 'DIET': return 'el-icon-food';
        case 'EXERCISE': return 'el-icon-bicycle';
        case 'MEDICAL': return 'el-icon-first-aid-kit';
        default: return 'el-icon-service';
      }
    },
    
    // 获取护理类型颜色
    getCareTypeColor(careType) {
      switch (careType) {
        case 'DAILY_CARE': return 'bg-blue-500';
        case 'DIET': return 'bg-orange-500';
        case 'EXERCISE': return 'bg-green-500';
        case 'MEDICAL': return 'bg-red-500';
        default: return 'bg-gray-500';
      }
    },
    
    // 获取护理类型渐变背景
    getCareTypeGradient(careType) {
      switch (careType) {
        case 'DAILY_CARE': return 'bg-gradient-to-r from-blue-500 to-blue-600';
        case 'DIET': return 'bg-gradient-to-r from-orange-500 to-orange-600';
        case 'EXERCISE': return 'bg-gradient-to-r from-green-500 to-green-600';
        case 'MEDICAL': return 'bg-gradient-to-r from-red-500 to-red-600';
        default: return 'bg-gradient-to-r from-gray-500 to-gray-600';
      }
    },
    
    // 获取护理类型背景色
    getCareTypeBackground(careType) {
      switch (careType) {
        case 'DAILY_CARE': return 'bg-blue-50';
        case 'DIET': return 'bg-orange-50';
        case 'EXERCISE': return 'bg-green-50';
        case 'MEDICAL': return 'bg-red-50';
        default: return 'bg-gray-50';
      }
    },
    
    // 获取护理类型图标颜色
    getCareTypeIconColor(careType) {
      switch (careType) {
        case 'DAILY_CARE': return '#3B82F6';
        case 'DIET': return '#F97316';
        case 'EXERCISE': return '#10B981';
        case 'MEDICAL': return '#EF4444';
        default: return '#6B7280';
      }
    },
    
    // 获取情绪状态名称
    getMoodStatusName(moodStatus) {
      switch (moodStatus) {
        case 'GOOD': return '良好';
        case 'NORMAL': return '一般';
        case 'POOR': return '较差';
        default: return '未知';
      }
    },
    
    // 获取情绪状态标签类型
    getMoodStatusTagType(moodStatus) {
      switch (moodStatus) {
        case 'GOOD': return 'success';
        case 'NORMAL': return 'warning';
        case 'POOR': return 'danger';
        default: return '';
      }
    },
    
    // 获取情绪状态图标
    getMoodStatusIcon(moodStatus) {
      switch (moodStatus) {
        case 'GOOD': return 'el-icon-sunny';
        case 'NORMAL': return 'el-icon-cloudy';
        case 'POOR': return 'el-icon-heavy-rain';
        default: return 'el-icon-cloudy';
      }
    },
    
    // 获取时间差描述
    getTimeAgo(createTime) {
      if (!createTime) return '';
      
      const now = new Date();
      const time = new Date(createTime);
      const diff = now - time;
      
      const minutes = Math.floor(diff / (1000 * 60));
      const hours = Math.floor(diff / (1000 * 60 * 60));
      const days = Math.floor(diff / (1000 * 60 * 60 * 24));
      
      if (days > 0) return `${days}天前`;
      if (hours > 0) return `${hours}小时前`;
      if (minutes > 0) return `${minutes}分钟前`;
      return '刚刚';
    }
  }
};
</script>

<style scoped>
.line-clamp-3 {
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

/* 自定义日历样式 */
.custom-calendar-cell {
  position: relative;
  width: 100%;
  height: 100%;
  padding: 8px;
  cursor: pointer;
  border-radius: 8px;
  transition: all 0.3s ease;
  min-height: 50px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.custom-calendar-cell:hover {
  background-color: #f3f4f6;
  transform: scale(1.05);
}

.custom-calendar-cell.selected {
  background: #475569;
  color: white;
  box-shadow: 0 4px 12px rgba(71, 85, 105, 0.3);
}

.custom-calendar-cell.has-records {
  background-color: #e2e8f0;
  border: 2px solid #475569;
}

.custom-calendar-cell.has-records.selected {
  background: #334155;
}

.date-number {
  font-weight: 600;
  font-size: 14px;
}

.record-indicator {
  margin-top: 4px;
}

.record-dot {
  width: 6px;
  height: 6px;
  background-color: #475569;
  border-radius: 50%;
  animation: pulse 2s infinite;
}

.custom-calendar-cell.selected .record-dot {
  background-color: white;
}

@keyframes pulse {
  0% {
    box-shadow: 0 0 0 0 rgba(71, 85, 105, 0.7);
  }
  70% {
    box-shadow: 0 0 0 10px rgba(71, 85, 105, 0);
  }
  100% {
    box-shadow: 0 0 0 0 rgba(71, 85, 105, 0);
  }
}

/* 自定义滚动条 */
::-webkit-scrollbar {
  width: 8px;
}

::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 4px;
}

::-webkit-scrollbar-thumb {
  background: #64748b;
  border-radius: 4px;
}

::-webkit-scrollbar-thumb:hover {
  background: #475569;
}

/* 渐变过渡动画 */
.bg-gradient-to-r {
  transition: all 0.3s ease;
}

/* 卡片hover效果 */
.transform:hover {
  transform: translateY(-4px);
}

/* Element UI样式覆盖 */
:deep(.el-select .el-input__inner) {
  border-radius: 8px;
}

:deep(.el-date-editor .el-input__inner) {
  border-radius: 8px;
}

:deep(.el-button) {
  border-radius: 8px;
}

:deep(.el-tag) {
  border-radius: 6px;
}

/* 日历容器样式 */
:deep(.el-calendar) {
  background: transparent;
  border: none;
}

:deep(.el-calendar__header) {
  padding: 12px 20px;
  border-bottom: 1px solid #e5e7eb;
}

:deep(.el-calendar__title) {
  color: #374151;
  font-weight: 600;
  font-size: 18px;
}

:deep(.el-calendar__button-group) {
  gap: 8px;
}

:deep(.el-calendar__button-group .el-button--text) {
  color: #6b7280;
  font-weight: 500;
}

:deep(.el-calendar__body) {
  padding: 0;
}

:deep(.el-calendar-table) {
  border: none;
}

:deep(.el-calendar-table thead th) {
  background-color: #f9fafb;
  border: none;
  color: #6b7280;
  font-weight: 600;
  padding: 12px 8px;
  text-align: center;
}

:deep(.el-calendar-table .el-calendar-day) {
  border: 1px solid #f3f4f6;
  padding: 0;
  height: 60px;
}

:deep(.el-calendar-table .el-calendar-day.is-selected) {
  background-color: transparent;
}

:deep(.el-calendar-table .el-calendar-day.is-today .el-calendar-day__text) {
  background: transparent;
  color: #3B82F6;
  font-weight: bold;
}
</style>