<template>
  <div class="p-0">
    <!-- 欢迎区域 -->
    <div class="relative bg-gradient-to-r from-blue-50 to-indigo-50 rounded-xl shadow-sm overflow-hidden mb-6">
      <!-- 背景装饰 -->
      <div class="absolute top-0 right-0 w-1/3 h-full opacity-10">
        <div class="absolute top-10 right-10 w-32 h-32 rounded-full bg-blue-400"></div>
        <div class="absolute top-20 right-40 w-16 h-16 rounded-full bg-indigo-400"></div>
        <div class="absolute top-40 right-20 w-24 h-24 rounded-full bg-purple-400"></div>
      </div>
      
      <div class="relative z-10 p-8 md:p-10 flex flex-col md:flex-row items-center">
        <div class="flex-1">
          <h1 class="text-3xl md:text-4xl font-bold text-gray-800 mb-3">
            <span class="text-transparent bg-clip-text bg-gradient-to-r from-blue-600 to-indigo-600">
              智慧养老管理系统
            </span>
          </h1>
          <p class="text-base md:text-lg text-gray-600 mb-6 max-w-2xl">
            为老年人提供全方位的智能化服务，打造温馨、安全、高效的养老服务生态系统
          </p>
          <div class="flex flex-wrap gap-4">
            <el-button type="primary" size="medium" class="flex items-center" round >
              <i class="el-icon-s-cooperation mr-1"></i> 养老服务
            </el-button>
            <el-button type="success" size="medium" class="flex items-center" round >
              <i class="el-icon-s-help mr-1"></i> 健康监测
            </el-button>
          </div>
        </div>
        <div class="mt-6 md:mt-0 md:ml-8">
          <img src="" alt="智慧养老" class="w-64 h-auto" />
        </div>
      </div>
    </div>

    <!-- 统计概览 -->
<!--    <el-row :gutter="20" class="mb-6">-->
<!--      <el-col :span="6">-->
<!--        <div class="h-28 rounded-xl bg-gradient-to-br from-blue-500 to-blue-600 shadow-lg flex items-center px-6 text-white transition-all duration-300 hover:shadow-xl relative overflow-hidden">-->
<!--          <div class="absolute top-0 right-0 opacity-10">-->
<!--            <svg class="w-20 h-20" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg">-->
<!--              <path d="M13 6a3 3 0 11-6 0 3 3 0 016 0zM18 8a2 2 0 11-4 0 2 2 0 014 0zM14 15a4 4 0 00-8 0v3h8v-3zM6 8a2 2 0 11-4 0 2 2 0 014 0zM16 18v-3a5.972 5.972 0 00-.75-2.906A3.005 3.005 0 0119 15v3h-3zM4.75 12.094A5.973 5.973 0 004 15v3H1v-3a3 3 0 013.75-2.906z"></path>-->
<!--            </svg>-->
<!--          </div>-->
<!--          <div class="z-10">-->
<!--            <div class="text-3xl mr-5">-->
<!--              <i class="el-icon-user"></i>-->
<!--            </div>-->
<!--          </div>-->
<!--          <div class="flex-1 z-10">-->
<!--            <div class="text-2xl font-bold">{{ stats.elderlyCount }}</div>-->
<!--            <div class="text-sm opacity-90">服务老人</div>-->
<!--          </div>-->
<!--        </div>-->
<!--      </el-col>-->
<!--      <el-col :span="6">-->
<!--        <div class="h-28 rounded-xl bg-gradient-to-br from-green-500 to-green-600 shadow-lg flex items-center px-6 text-white transition-all duration-300 hover:shadow-xl relative overflow-hidden">-->
<!--          <div class="absolute top-0 right-0 opacity-10">-->
<!--            <svg class="w-20 h-20" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg">-->
<!--              <path fill-rule="evenodd" d="M10 2a4 4 0 00-4 4v1H5a1 1 0 00-.994.89l-1 9A1 1 0 004 18h12a1 1 0 00.994-1.11l-1-9A1 1 0 0015 7h-1V6a4 4 0 00-4-4zm2 5V6a2 2 0 10-4 0v1h4zm-6 3a1 1 0 112 0 1 1 0 01-2 0zm7-1a1 1 0 100 2 1 1 0 000-2z" clip-rule="evenodd"></path>-->
<!--            </svg>-->
<!--          </div>-->
<!--          <div class="z-10">-->
<!--            <div class="text-3xl mr-5">-->
<!--              <i class="el-icon-s-cooperation"></i>-->
<!--            </div>-->
<!--          </div>-->
<!--          <div class="flex-1 z-10">-->
<!--            <div class="text-2xl font-bold">{{ stats.servicesCount }}</div>-->
<!--            <div class="text-sm opacity-90">服务项目</div>-->
<!--          </div>-->
<!--        </div>-->
<!--      </el-col>-->
<!--      <el-col :span="6">-->
<!--        <div class="h-28 rounded-xl bg-gradient-to-br from-amber-500 to-amber-600 shadow-lg flex items-center px-6 text-white transition-all duration-300 hover:shadow-xl relative overflow-hidden">-->
<!--          <div class="absolute top-0 right-0 opacity-10">-->
<!--            <svg class="w-20 h-20" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg">-->
<!--              <path fill-rule="evenodd" d="M6 2a1 1 0 00-1 1v1H4a2 2 0 00-2 2v10a2 2 0 002 2h12a2 2 0 002-2V6a2 2 0 00-2-2h-1V3a1 1 0 10-2 0v1H7V3a1 1 0 00-1-1zm0 5a1 1 0 000 2h8a1 1 0 100-2H6z" clip-rule="evenodd"></path>-->
<!--            </svg>-->
<!--          </div>-->
<!--          <div class="z-10">-->
<!--            <div class="text-3xl mr-5">-->
<!--              <i class="el-icon-s-order"></i>-->
<!--            </div>-->
<!--          </div>-->
<!--          <div class="flex-1 z-10">-->
<!--            <div class="text-2xl font-bold">{{ stats.appointmentCount }}</div>-->
<!--            <div class="text-sm opacity-90">今日预约</div>-->
<!--          </div>-->
<!--        </div>-->
<!--      </el-col>-->
<!--      <el-col :span="6">-->
<!--        <div class="h-28 rounded-xl bg-gradient-to-br from-purple-500 to-purple-600 shadow-lg flex items-center px-6 text-white transition-all duration-300 hover:shadow-xl relative overflow-hidden">-->
<!--          <div class="absolute top-0 right-0 opacity-10">-->
<!--            <svg class="w-20 h-20" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg">-->
<!--              <path fill-rule="evenodd" d="M3.172 5.172a4 4 0 015.656 0L10 6.343l1.172-1.171a4 4 0 115.656 5.656L10 17.657l-6.828-6.829a4 4 0 010-5.656z" clip-rule="evenodd"></path>-->
<!--            </svg>-->
<!--          </div>-->
<!--          <div class="z-10">-->
<!--            <div class="text-3xl mr-5">-->
<!--              <i class="el-icon-s-custom"></i>-->
<!--            </div>-->
<!--          </div>-->
<!--          <div class="flex-1 z-10">-->
<!--            <div class="text-2xl font-bold">{{ stats.staffCount }}</div>-->
<!--            <div class="text-sm opacity-90">服务人员</div>-->
<!--          </div>-->
<!--        </div>-->
<!--      </el-col>-->
<!--    </el-row>-->
    
    <!-- 通知轮播 -->
    <div class="bg-white rounded-xl px-5 h-10 flex items-center mb-6 shadow-sm">
      <i class="el-icon-bell text-amber-500 text-lg mr-4"></i>
      <el-carousel height="40px" direction="vertical" :autoplay="true" :interval="4000" indicator-position="none" class="flex-1">
        <el-carousel-item v-for="(notice, index) in notices" :key="index">
          <div class="flex items-center justify-between h-10 cursor-pointer" @click="viewNotice(notice)">
            <span class="text-sm text-gray-700 truncate">{{ notice.title }}</span>
            <span class="text-xs text-gray-500 ml-4">{{ notice.time }}</span>
          </div>
        </el-carousel-item>
      </el-carousel>
    </div>
    
    <!-- 内容区域 -->
    <el-row :gutter="24" class="mb-6">
      <!-- 左侧：主要内容 -->
      <el-col :span="16">
        <!-- 健康监测概览 -->
        <div class="bg-white rounded-xl shadow-sm overflow-hidden mb-6">
          <div class="flex justify-between items-center p-5 border-b border-gray-100">
            <div class="flex items-center">
              <i class="el-icon-s-data text-blue-500 text-xl mr-2"></i>
              <span class="text-lg font-medium text-gray-800">健康监测概览</span>
            </div>
            <el-button type="text" @click="$router.push('/front/health-monitor')">
              查看详情 <i class="el-icon-arrow-right"></i>
            </el-button>
          </div>
          
          <div class="p-5">
            <el-row :gutter="20">
              <!-- 健康指标卡片 -->
              <el-col :span="8">
                <div class="bg-blue-50 rounded-lg p-4 mb-4">
                  <div class="flex items-center justify-between mb-2">
                    <span class="text-sm font-medium text-blue-700">平均血压指数</span>
                    <i class="el-icon-s-data text-blue-500"></i>
                  </div>
                  <div class="text-xl font-bold text-blue-800">120/80</div>
                  <div class="text-xs text-blue-600 mt-1">较上周 <i class="el-icon-caret-bottom"></i> 2%</div>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="bg-green-50 rounded-lg p-4 mb-4">
                  <div class="flex items-center justify-between mb-2">
                    <span class="text-sm font-medium text-green-700">平均心率</span>
                    <i class="el-icon-s-data text-green-500"></i>
                  </div>
                  <div class="text-xl font-bold text-green-800">76 BPM</div>
                  <div class="text-xs text-green-600 mt-1">较上周 <i class="el-icon-caret-top"></i> 1%</div>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="bg-purple-50 rounded-lg p-4 mb-4">
                  <div class="flex items-center justify-between mb-2">
                    <span class="text-sm font-medium text-purple-700">平均睡眠时长</span>
                    <i class="el-icon-s-data text-purple-500"></i>
                  </div>
                  <div class="text-xl font-bold text-purple-800">7.2 小时</div>
                  <div class="text-xs text-purple-600 mt-1">较上周 <i class="el-icon-caret-top"></i> 5%</div>
                </div>
              </el-col>
            </el-row>

            <!-- 健康功能入口 -->
            <div class="bg-gray-50 rounded-lg p-4">
              <h4 class="text-sm font-medium text-gray-700 mb-3">健康管理功能</h4>
              <div class="grid grid-cols-2 gap-3">
                <div class="bg-white rounded-lg p-3 cursor-pointer hover:shadow-sm transition-all" 
                     @click="$router.push('/front/health-monitoring')">
                  <div class="flex items-center">
                    <div class="w-8 h-8 bg-blue-100 rounded-lg flex items-center justify-center mr-3">
                      <i class="el-icon-s-data text-blue-500 text-sm"></i>
                    </div>
                    <div>
                      <div class="text-sm font-medium text-gray-800">健康记录</div>
                      <div class="text-xs text-gray-500">记录生理指标</div>
                    </div>
                  </div>
                </div>
                <div class="bg-white rounded-lg p-3 cursor-pointer hover:shadow-sm transition-all" 
                     @click="$router.push('/front/medication')">
                  <div class="flex items-center">
                    <div class="w-8 h-8 bg-green-100 rounded-lg flex items-center justify-center mr-3">
                      <i class="el-icon-s-help text-green-500 text-sm"></i>
                    </div>
                    <div>
                      <div class="text-sm font-medium text-gray-800">用药管理</div>
                      <div class="text-xs text-gray-500">药物提醒</div>
                    </div>
                  </div>
                </div>
                <div class="bg-white rounded-lg p-3 cursor-pointer hover:shadow-sm transition-all" 
                     @click="$router.push('/front/health-record')">
                  <div class="flex items-center">
                    <div class="w-8 h-8 bg-amber-100 rounded-lg flex items-center justify-center mr-3">
                      <i class="el-icon-document text-amber-500 text-sm"></i>
                    </div>
                    <div>
                      <div class="text-sm font-medium text-gray-800">健康档案</div>
                      <div class="text-xs text-gray-500">查看历史</div>
                    </div>
                  </div>
                </div>
                <div class="bg-white rounded-lg p-3 cursor-pointer hover:shadow-sm transition-all" 
                     @click="$router.push('/front/care-record')">
                  <div class="flex items-center">
                    <div class="w-8 h-8 bg-purple-100 rounded-lg flex items-center justify-center mr-3">
                      <i class="el-icon-s-custom text-purple-500 text-sm"></i>
                    </div>
                    <div>
                      <div class="text-sm font-medium text-gray-800">护理记录</div>
                      <div class="text-xs text-gray-500">护理服务</div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        
        <!-- 活动中心 -->
        <div class="bg-white rounded-xl shadow-sm overflow-hidden mb-6">
          <div class="flex justify-between items-center p-5 border-b border-gray-100">
            <div class="flex items-center">
              <i class="el-icon-date text-blue-500 text-xl mr-2"></i>
              <span class="text-lg font-medium text-gray-800">活动中心</span>
            </div>
            <el-button type="text" @click="$router.push('/front/activity')">
              查看全部 <i class="el-icon-arrow-right"></i>
            </el-button>
          </div>
          
          <div class="p-5">
            <el-row :gutter="20" v-loading="activitiesLoading">
              <el-col :md="8" v-for="(activity, index) in recommendActivities" :key="index" class="mb-4">
                <div class="bg-white rounded-lg shadow-sm hover:shadow-md transition-all duration-300 cursor-pointer h-full border border-gray-100" @click="viewActivityDetail(activity)">
                  <div class="h-40 relative overflow-hidden rounded-t-lg">
                    <div v-if="activity.coverImage" class="absolute inset-0">
                      <img :src="activity.coverImage" class="w-full h-full object-cover" />
                    </div>
                    <div v-else class="absolute inset-0 bg-gradient-to-br" :style="{ backgroundColor: getActivityCoverColor(activity.categoryId) }">
                      <div class="flex items-center justify-center h-full">
                        <i class="el-icon-date text-white text-4xl"></i>
                      </div>
                    </div>
                    <!-- 状态标签 -->
                    <div class="absolute top-2 right-2">
                      <el-tag size="mini" :type="getActivityStatusTagType(activity.status)" class="shadow-sm">
                        {{ getActivityStatusName(activity.status) }}
                      </el-tag>
                    </div>
                  </div>
                  <div class="p-4">
                    <h3 class="text-base font-medium text-gray-800 mb-2">{{ activity.title }}</h3>
                    <p class="text-sm text-gray-500 mb-3 line-clamp-2">{{ activity.description || '暂无活动简介' }}</p>
                    <div class="flex justify-between items-center mb-2">
                      <div class="text-xs text-gray-500">
                        <i class="el-icon-time mr-1"></i>{{ formatActivityDate(activity.activityDate) }}
                      </div>
                      <el-tag size="mini" :style="{ backgroundColor: getActivityCategoryColor(activity.categoryId), border: 'none', color: '#fff' }">
                        {{ getActivityCategoryName(activity.categoryId) }}
                      </el-tag>
                    </div>
                    <div class="flex justify-between items-center">
                      <span class="text-sm text-blue-600">
                        {{ activity.currentParticipants || 0 }}/{{ activity.maxParticipants || '不限' }} 人
                      </span>
                      <span class="text-sm font-medium" :class="activity.cost > 0 ? 'text-orange-500' : 'text-green-500'">
                        {{ activity.cost > 0 ? '¥' + activity.cost : '免费' }}
                      </span>
                    </div>
                  </div>
                </div>
              </el-col>
            </el-row>
            
            <!-- 无活动时的提示 -->
            <div v-if="recommendActivities.length === 0 && !activitiesLoading" class="text-center py-8 text-gray-400">
              <i class="el-icon-date text-3xl mb-2"></i>
              <p class="text-sm">暂无推荐活动</p>
            </div>
          </div>
        </div>
      </el-col>
      
      <!-- 右侧：侧边栏 -->
      <el-col :span="8">
        <!-- 通知公告 -->
        <div class="bg-white rounded-xl shadow-sm overflow-hidden mb-6">
          <div class="flex justify-between items-center p-5 border-b border-gray-100">
            <div class="flex items-center">
              <i class="el-icon-bell text-blue-500 text-xl mr-2"></i>
              <span class="text-lg font-medium text-gray-800">通知公告</span>
            </div>
            <el-button type="text" @click="$router.push('/front/notice')">
              更多 <i class="el-icon-arrow-right"></i>
            </el-button>
          </div>
          
          <div v-if="notices.length > 0" class="divide-y divide-gray-50" v-loading="noticesLoading">
            <div v-for="(notice) in notices" :key="notice.id"
              class="px-5 py-3 hover:bg-gray-50 transition-colors cursor-pointer" 
              @click="viewNotice(notice)">
              <div class="flex items-center mb-1">
                <i class="el-icon-info text-blue-500 mr-2"></i>
                <div class="text-sm font-medium text-gray-700 truncate flex-1">{{ notice.title }}</div>
              </div>
              <div class="text-xs text-gray-400 flex items-center ml-5">
                <i class="el-icon-time mr-1"></i> {{ formatDate(notice.time) }}
              </div>
            </div>
          </div>
          <div v-else-if="!noticesLoading" class="py-10 text-center text-gray-400">
            <i class="el-icon-info text-3xl mb-2"></i>
            <p class="text-sm">暂无通知</p>
          </div>
          <div v-else class="py-10 text-center">
            <i class="el-icon-loading text-gray-400 text-2xl"></i>
          </div>
        </div>
        
        <!-- 快捷入口 -->
<!--        <div class="bg-white rounded-xl shadow-sm overflow-hidden">-->
<!--          <div class="flex items-center p-5 border-b border-gray-100">-->
<!--            <i class="el-icon-menu text-blue-500 text-xl mr-2"></i>-->
<!--            <span class="text-lg font-medium text-gray-800">快捷入口</span>-->
<!--          </div>-->
          
<!--          <div class="grid grid-cols-2 gap-4 p-4">-->
<!--            <div class="bg-blue-50 rounded-lg p-4 flex flex-col items-center justify-center cursor-pointer hover:bg-blue-100 transition-all duration-300" -->
<!--                @click="$router.push('/front/care-record')">-->
<!--              <i class="el-icon-s-cooperation text-blue-500 text-2xl mb-2"></i>-->
<!--              <span class="text-sm text-blue-700">护理服务</span>-->
<!--            </div>-->
<!--            <div class="bg-green-50 rounded-lg p-4 flex flex-col items-center justify-center cursor-pointer hover:bg-green-100 transition-all duration-300" -->
<!--                @click="$router.push('/front/health-monitoring')">-->
<!--              <i class="el-icon-s-help text-green-500 text-2xl mb-2"></i>-->
<!--              <span class="text-sm text-green-700">健康监测</span>-->
<!--            </div>-->
<!--            <div class="bg-amber-50 rounded-lg p-4 flex flex-col items-center justify-center cursor-pointer hover:bg-amber-100 transition-all duration-300" -->
<!--                @click="$router.push('/front/activity')">-->
<!--              <i class="el-icon-date text-amber-500 text-2xl mb-2"></i>-->
<!--              <span class="text-sm text-amber-700">活动中心</span>-->
<!--            </div>-->
<!--            <div class="bg-purple-50 rounded-lg p-4 flex flex-col items-center justify-center cursor-pointer hover:bg-purple-100 transition-all duration-300" -->
<!--                @click="$router.push('/front/person')">-->
<!--              <i class="el-icon-user text-purple-500 text-2xl mb-2"></i>-->
<!--              <span class="text-sm text-purple-700">个人中心</span>-->
<!--            </div>-->
<!--          </div>-->
<!--        </div>-->
      </el-col>
    </el-row>


  </div>
</template>

<script>
export default {
  name: "FrontHome",
  data() {
    return {
      stats: {
        elderlyCount: "--",
        servicesCount: "--",
        appointmentCount: "--",
        staffCount: "--"
      },
      notices: [],
      recommendActivities: [],
      activityCategories: [],
      activitiesLoading: false,
      noticesLoading: false,
      healthStats: {
        bloodPressure: '120/80',
        heartRate: '76',
        sleepHours: '7.2'
      }
    };
  },
  created() {
    this.fetchStatistics();
    this.fetchRecommendActivities();
    this.fetchNotices();
    this.fetchActivityCategories();
  },
  methods: {
    // 查看通知详情
    viewNotice(notice) {
      this.$message.info(`查看通知: ${notice.title}`);
      this.$router.push(`/front/notice/${notice.id}`);
    },
    
    // 查看活动详情
    viewActivityDetail(activity) {
      this.$router.push(`/front/activity/${activity.id}`);
    },

    // 获取推荐活动
    fetchRecommendActivities() {
      this.activitiesLoading = true;
      this.$request.get("/activity/selectPage", { 
        params: { 
          pageNum: 1, 
          pageSize: 6,
          status: 'PLANNED'
        } 
      })
        .then(res => {
          if (res.code === '200') {
            this.recommendActivities = res.data.list || [];
          }
        })
        .catch(error => {
          console.error('获取推荐活动失败:', error);
        })
        .finally(() => {
          this.activitiesLoading = false;
        });
    },

    // 获取通知公告
    fetchNotices() {
      this.noticesLoading = true;
      this.$request.get("/notice/selectPage", { 
        params: { 
          pageNum: 1, 
          pageSize: 5
        } 
      })
        .then(res => {
          if (res.code === '200') {
            this.notices = res.data.list || [];
          }
        })
        .catch(error => {
          console.error('获取通知公告失败:', error);
        })
        .finally(() => {
          this.noticesLoading = false;
        });
    },

    // 获取活动分类
    fetchActivityCategories() {
      this.$request.get("/activity/category/selectAll")
        .then(res => {
          if (res.code === '200') {
            this.activityCategories = res.data || [];
          }
        })
        .catch(error => {
          console.error('获取活动分类失败:', error);
        });
    },

    // 获取活动分类名称
    getActivityCategoryName(categoryId) {
      const category = this.activityCategories.find(item => item.id === categoryId);
      return category ? category.name : '未分类';
    },

    // 获取活动分类颜色
    getActivityCategoryColor(categoryId) {
      const category = this.activityCategories.find(item => item.id === categoryId);
      return category?.color || '#909399';
    },

    // 获取活动封面颜色
    getActivityCoverColor(categoryId) {
      const colors = ['#4CAF50', '#2196F3', '#FF9800', '#9C27B0', '#F44336', '#607D8B'];
      return colors[categoryId % colors.length] || '#909399';
    },

    // 获取活动状态名称
    getActivityStatusName(status) {
      switch (status) {
        case 'PLANNED':
          return '计划中';
        case 'ONGOING':
          return '进行中';
        case 'COMPLETED':
          return '已完成';
        case 'CANCELLED':
          return '已取消';
        default:
          return '未知';
      }
    },

    // 获取活动状态标签类型
    getActivityStatusTagType(status) {
      switch (status) {
        case 'PLANNED':
          return 'info';
        case 'ONGOING':
          return 'success';
        case 'COMPLETED':
          return '';
        case 'CANCELLED':
          return 'danger';
        default:
          return 'info';
      }
    },

    // 格式化活动日期
    formatActivityDate(dateStr) {
      if (!dateStr) return '';
      const date = new Date(dateStr);
      return date.getMonth() + 1 + '月' + date.getDate() + '日';
    },

    // 获取统计数据
    fetchStatistics() {
      // 这里使用模拟数据，实际项目中可以通过API获取
      this.$request.get("/statistics/dashboard")
        .then(res => {
          if (res.code === '200') {
            // 处理返回的数据
          }
        })
        .catch(error => {
          console.error('获取统计数据异常:', error);
          // 使用模拟数据（已经在data中定义）
        });
    },

    // 格式化日期
    formatDate(dateStr) {
      if (!dateStr) return '';
      const date = new Date(dateStr);
      return date.getFullYear() + '-' + 
             String(date.getMonth() + 1).padStart(2, '0') + '-' + 
             String(date.getDate()).padStart(2, '0');
    }
  }
};
</script>

<style scoped>
/* Tailwind 已经覆盖了大部分样式 */

/* 为卡片添加悬浮效果 */
.el-card:hover {
  transform: translateY(-3px);
  transition: all 0.3s ease;
}

/* 对话框样式 */
:deep(.service-detail-dialog) {
  border-radius: 0.5rem;
  overflow: hidden;
}

:deep(.el-dialog__header) {
  padding: 1rem;
  border-bottom: 1px solid #f0f0f0;
  background-color: #f9fafb;
}

:deep(.el-dialog__body) {
  padding: 1.5rem;
}

/* 文字截断样式 */
.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style> 