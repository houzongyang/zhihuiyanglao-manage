<template>
  <div class="p-6">
    <!-- 顶部操作栏：添加刷新按钮 -->
    <div class="flex justify-end mb-4">
      <el-button type="primary" icon="el-icon-refresh" size="small" @click="loadData" :loading="isLoading">
        刷新数据
      </el-button>
    </div>
    
    <!-- 欢迎区域 -->
    <div class="bg-gradient-to-r from-blue-500 to-indigo-600 rounded-xl p-6 mb-6 shadow-lg flex flex-wrap text-white relative overflow-hidden">
      <div class="relative z-10 w-full md:w-2/3">
        <h1 class="text-2xl font-bold mb-2">欢迎使用智能养老系统</h1>
        <p class="text-white text-opacity-90 mb-6">今天是 {{ currentDate }}，祝您工作愉快！</p>

      </div>
      <div class="hidden md:flex md:w-1/3 items-center justify-center">
        <img src="/img/admin-welcome.svg" alt="Welcome" class="max-h-40 filter drop-shadow-lg">
      </div>
      
      <!-- 装饰圆圈 -->
      <div class="absolute top-0 right-0 rounded-full bg-white bg-opacity-10 w-60 h-60 -mt-20 -mr-20"></div>
      <div class="absolute bottom-0 left-0 rounded-full bg-white bg-opacity-10 w-80 h-80 -mb-40 -ml-40"></div>
    </div>
    
    <!-- 数据统计卡片区域 -->
    <el-row :gutter="20" class="mb-6">
      <el-col :xs="12" :sm="12" :md="6" :lg="6" :xl="6" class="mb-4 md:mb-0">
        <div class="bg-gradient-to-r from-blue-400 to-blue-500 rounded-xl p-5 shadow-md h-full text-white relative overflow-hidden">
          <div class="flex items-center mb-3">
            <div class="p-2 bg-white bg-opacity-20 rounded-lg">
              <svg viewBox="0 0 24 24" width="22" height="22" stroke="currentColor" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round">
                <path d="M16 4h2a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2H6a2 2 0 0 1-2-2V6a2 2 0 0 1 2-2h2"></path>
                <rect x="8" y="2" width="8" height="4" rx="1" ry="1"></rect>
                <path d="M9 14h6"></path>
                <path d="M9 18h6"></path>
                <path d="M9 10h6"></path>
              </svg>
            </div>
            <div class="ml-3">
              <div class="text-2xl font-bold">{{ statistics.elderCount }}</div>
              <div class="text-sm opacity-80">老人总数</div>
            </div>
          </div>
          <div class="absolute bottom-0 left-0 w-full opacity-30">
            <svg viewBox="0 0 100 30" preserveAspectRatio="none" class="w-full h-8">
              <path d="M0,15 Q20,5 40,15 T80,15 T100,15" stroke="rgba(255,255,255,0.5)" stroke-width="2" fill="none" />
            </svg>
          </div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="12" :md="6" :lg="6" :xl="6" class="mb-4 md:mb-0">
        <div class="bg-gradient-to-r from-green-400 to-green-500 rounded-xl p-5 shadow-md h-full text-white relative overflow-hidden">
          <div class="flex items-center mb-3">
            <div class="p-2 bg-white bg-opacity-20 rounded-lg">
              <svg viewBox="0 0 24 24" width="22" height="22" stroke="currentColor" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round">
                <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"></path>
                <circle cx="9" cy="7" r="4"></circle>
                <path d="M23 21v-2a4 4 0 0 0-3-3.87"></path>
                <path d="M16 3.13a4 4 0 0 1 0 7.75"></path>
              </svg>
            </div>
            <div class="ml-3">
              <div class="text-2xl font-bold">{{ statistics.checkinCount }}</div>
              <div class="text-sm opacity-80">入住数量</div>
            </div>
          </div>
          <div class="absolute bottom-0 left-0 w-full opacity-30">
            <svg viewBox="0 0 100 30" preserveAspectRatio="none" class="w-full h-8">
              <path d="M0,20 Q30,5 50,15 T100,10" stroke="rgba(255,255,255,0.5)" stroke-width="2" fill="none" />
            </svg>
          </div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="12" :md="6" :lg="6" :xl="6" class="mb-4 md:mb-0">
        <div class="bg-gradient-to-r from-yellow-400 to-orange-500 rounded-xl p-5 shadow-md h-full text-white relative overflow-hidden">
          <div class="flex items-center mb-3">
            <div class="p-2 bg-white bg-opacity-20 rounded-lg">
              <svg viewBox="0 0 24 24" width="22" height="22" stroke="currentColor" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round">
                <rect x="2" y="3" width="20" height="14" rx="2" ry="2"></rect>
                <line x1="8" y1="21" x2="16" y2="21"></line>
                <line x1="12" y1="17" x2="12" y2="21"></line>
              </svg>
            </div>
            <div class="ml-3">
              <div class="text-2xl font-bold">{{ statistics.activityCount }}</div>
              <div class="text-sm opacity-80">活动数量</div>
            </div>
          </div>
          <div class="absolute bottom-0 left-0 w-full opacity-30">
            <svg viewBox="0 0 100 30" preserveAspectRatio="none" class="w-full h-8">
              <path d="M0,10 Q25,20 50,10 T100,15" stroke="rgba(255,255,255,0.5)" stroke-width="2" fill="none" />
            </svg>
          </div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="12" :md="6" :lg="6" :xl="6" class="mb-4 md:mb-0">
        <div class="bg-gradient-to-r from-red-400 to-red-500 rounded-xl p-5 shadow-md h-full text-white relative overflow-hidden">
          <div class="flex items-center mb-3">
            <div class="p-2 bg-white bg-opacity-20 rounded-lg">
              <svg viewBox="0 0 24 24" width="22" height="22" stroke="currentColor" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round">
                <path d="M22 12h-4l-3 9L9 3l-3 9H2"></path>
              </svg>
            </div>
            <div class="ml-3">
              <div class="text-2xl font-bold">{{ statistics.categoryCount }}</div>
              <div class="text-sm opacity-80">活动分类</div>
            </div>
          </div>
          <div class="absolute bottom-0 left-0 w-full opacity-30">
            <svg viewBox="0 0 100 30" preserveAspectRatio="none" class="w-full h-8">
              <path d="M0,15 Q35,5 50,15 T100,5" stroke="rgba(255,255,255,0.5)" stroke-width="2" fill="none" />
            </svg>
          </div>
        </div>
      </el-col>
    </el-row>
    
    <!-- 图表和通知区域 -->
    <el-row :gutter="20" class="mb-6">
      <el-col :span="12" class="mb-4 md:mb-0">
        <!-- 饼图区域 -->
        <div class="bg-white rounded-xl shadow-md overflow-hidden h-full">
          <div class="flex justify-between items-center px-5 py-4 border-b border-gray-100">
            <div class="flex items-center">
              <i class="el-icon-pie-chart text-purple-500 text-xl mr-2"></i>
              <span class="font-semibold text-gray-800">老人年龄分布</span>
            </div>
          </div>
          <div class="p-5">
            <div ref="pieChart" class="w-full h-80"></div>
          </div>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="bg-white rounded-xl shadow-md overflow-hidden h-full">
          <div class="flex justify-between items-center px-5 py-4 border-b border-gray-100">
            <div class="flex items-center">
              <i class="el-icon-bell text-orange-500 text-xl mr-2"></i>
              <span class="font-semibold text-gray-800">最新通知</span>
            </div>
            <button @click="$router.push('/manager/notice')" class="text-blue-500 hover:text-blue-600 text-sm font-medium">
              查看更多
            </button>
          </div>
          <div class="px-5 py-3 max-h-80 overflow-y-auto">
            <div v-for="(item, index) in notices" :key="index" class="py-3 border-b border-gray-100 last:border-0">
              <div class="flex">
                <span class="flex-shrink-0 w-2 h-2 rounded-full mt-2" :class="index < 2 ? 'bg-red-500' : 'bg-gray-300'"></span>
                <div class="ml-3">
                  <p class="text-gray-800 mb-1 text-sm">{{ item.title }}</p>
                  <p class="text-gray-400 text-xs">{{ item.time }}</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
    
    <!-- 活动分类占比图表和快捷入口 -->
    <el-row :gutter="20">
      <el-col :span="16" class="mb-4 md:mb-0">
        <div class="bg-white rounded-xl shadow-md overflow-hidden">
          <div class="flex justify-between items-center px-5 py-4 border-b border-gray-100">
            <div class="flex items-center">
              <i class="el-icon-s-data text-blue-500 text-xl mr-2"></i>
              <span class="font-semibold text-gray-800">活动分类占比</span>
            </div>
            <button @click="$router.push('/manager/activity')" class="text-blue-500 hover:text-blue-600 text-sm font-medium">
              查看活动
            </button>
          </div>
          <div class="p-5">
            <div ref="categoryPieChart" class="w-full h-80"></div>
          </div>
        </div>
      </el-col>

    </el-row>
  </div>
</template>
<script>
// 引入echarts
import * as echarts from 'echarts'

export default {
  name: "ManagerHome",
  data() {
    return {
      currentDate: this.formatDate(new Date()),
      notices: [],
      colors: ['#409EFF', '#67C23A', '#E6A23C', '#F56C6C', '#909399'],
      isLoading: false,
      // 统计数据
      statistics: {
        elderCount: 0,
        checkinCount: 0,
        activityCount: 0,
        categoryCount: 0
      },
      // 老人年龄分布数据（用于饼图）
      ageDistribution: [],
      // 活动分类分布数据（用于饼图）
      activityCategoryDistribution: [],
      pieChartInstance: null,
      categoryPieChartInstance: null
    };
  },
  mounted() {
    this.loadData();
    
    // 添加窗口大小变化时重新渲染图表
    window.addEventListener('resize', this.resizeCharts);
  },
  beforeDestroy() {
    // 销毁图表实例
    if (this.pieChartInstance) {
      this.pieChartInstance.dispose();
      this.pieChartInstance = null;
    }
    if (this.categoryPieChartInstance) {
      this.categoryPieChartInstance.dispose();
      this.categoryPieChartInstance = null;
    }
    
    // 移除事件监听
    window.removeEventListener('resize', this.resizeCharts);
  },

  methods: {
    
    // 图表自适应大小
    resizeCharts() {
      if (this.pieChartInstance) {
        this.pieChartInstance.resize();
      }
      if (this.categoryPieChartInstance) {
        this.categoryPieChartInstance.resize();
      }
    },
    
    // 加载数据
    async loadData() {
      this.isLoading = true;
      try {
        // 并行加载所有数据
        await Promise.all([
          this.loadUserStatistics(),
          this.loadCheckinStatistics(),
          this.loadActivityStatistics(),
          this.loadCategoryStatistics(),
          this.loadNotices(),
          this.loadAgeDistribution()
        ]);
        
        // 初始化图表
        this.$nextTick(() => {
          this.initPieChart();
          this.initCategoryPieChart();
        });
        
        // 更新当前日期
        this.currentDate = this.formatDate(new Date());
      } catch (error) {
        console.error('加载数据失败:', error);
        this.$message.error('加载数据失败');
      } finally {
        this.isLoading = false;
      }
    },

    // 加载用户统计数据
    async loadUserStatistics() {
      try {
        const res = await this.$request.get('/user/selectPage', {
          params: { pageNum: 1, pageSize: 1000 }
        });
        if (res.code === '200') {
          this.statistics.elderCount = res.data.total || res.data.list?.length || 0;
        }
      } catch (error) {
        console.error('加载用户统计失败:', error);
      }
    },

    // 加载入住统计数据
    async loadCheckinStatistics() {
      try {
        const res = await this.$request.get('/checkinRecord/selectPage', {
          params: { pageNum: 1, pageSize: 1000 }
        });
        
        console.log('📊 入住记录API响应:', res);
        
        if (res.code === '200') {
          // 根据CheckinRecord.vue的逻辑处理数据结构
          const data = res.data.list || res.data || [];
          const total = res.data.total || data.length || 0;
          
          this.statistics.checkinCount = total;
          
          console.log('✅ 入住统计结果:', {
            响应数据: res.data,
            数据列表: data,
            统计总数: total
          });
        } else {
          console.warn('⚠️ 入住记录API返回错误:', res);
        }
      } catch (error) {
        console.error('❌ 加载入住统计失败:', error);
      }
    },

    // 加载活动统计数据
    async loadActivityStatistics() {
      try {
        // 先获取分类列表
        const categoryRes = await this.$request.get('/activity/category/selectAll');
        const categories = categoryRes.code === '200' ? categoryRes.data || [] : [];
        
        // 创建分类映射
        const categoryMap = {};
        categories.forEach(cat => {
          categoryMap[cat.id] = cat.name;
        });
        
        // 获取活动列表
        const res = await this.$request.get('/activity/selectPage', {
          params: { pageNum: 1, pageSize: 1000 }
        });
        
        if (res.code === '200') {
          this.statistics.activityCount = res.data.total || res.data.list?.length || 0;
          
          // 统计活动分类分布
          const categoryStats = {};
          res.data.list?.forEach(activity => {
            const categoryName = categoryMap[activity.categoryId] || '未分类';
            categoryStats[categoryName] = (categoryStats[categoryName] || 0) + 1;
          });
          
          this.activityCategoryDistribution = Object.entries(categoryStats).map(([name, count]) => ({
            name,
            value: count
          }));
        }
      } catch (error) {
        console.error('加载活动统计失败:', error);
      }
    },

    // 加载活动分类统计数据
    async loadCategoryStatistics() {
      try {
        const res = await this.$request.get('/activity/category/selectAll');
        if (res.code === '200') {
          this.statistics.categoryCount = res.data?.length || 0;
        }
      } catch (error) {
        console.error('加载分类统计失败:', error);
      }
    },

    // 加载公告数据
    async loadNotices() {
      try {
        const res = await this.$request.get('/notice/selectPage', {
          params: { pageNum: 1, pageSize: 5 }
        });
        if (res.code === '200') {
          this.notices = res.data.list?.map(notice => ({
            title: notice.title,
            time: notice.createTime ? this.formatDate(new Date(notice.createTime), true) : ''
          })) || [];
        }
      } catch (error) {
        console.error('加载公告失败:', error);
      }
    },

    // 加载老人年龄分布数据
    async loadAgeDistribution() {
      try {
        const res = await this.$request.get('/user/selectPage', {
          params: { pageNum: 1, pageSize: 1000 }
        });
        if (res.code === '200') {
          const ageStats = {
            '60-65岁': 0,
            '66-70岁': 0,
            '71-75岁': 0,
            '76-80岁': 0,
            '80岁以上': 0
          };
          
          console.log('👴 用户数据:', res.data.list);
          
          res.data.list?.forEach(user => {
            // 直接使用用户的 age 字段
            if (user.age && typeof user.age === 'number') {
              const age = user.age;
              
              console.log(`📊 用户 ${user.name} 年龄: ${age}`);
              
              if (age >= 60 && age <= 65) {
                ageStats['60-65岁']++;
              } else if (age >= 66 && age <= 70) {
                ageStats['66-70岁']++;
              } else if (age >= 71 && age <= 75) {
                ageStats['71-75岁']++;
              } else if (age >= 76 && age <= 80) {
                ageStats['76-80岁']++;
              } else if (age > 80) {
                ageStats['80岁以上']++;
              }
            }
          });
          
          console.log('📈 年龄统计结果:', ageStats);
          
          this.ageDistribution = Object.entries(ageStats).map(([name, value]) => ({
            name,
            value
          }));
          
          console.log('🎯 最终图表数据:', this.ageDistribution);
        }
      } catch (error) {
        console.error('加载年龄分布失败:', error);
        // 如果加载失败，使用默认数据
        this.ageDistribution = [
          { name: "60-65岁", value: 0 },
          { name: "66-70岁", value: 0 },
          { name: "71-75岁", value: 0 },
          { name: "76-80岁", value: 0 },
          { name: "80岁以上", value: 0 }
        ];
      }
    },
    

    
    formatDate(date, short = false) {
      if (!date) return '';
      
      const d = new Date(date);
      if (isNaN(d.getTime())) return '';
      
      const year = d.getFullYear();
      const month = String(d.getMonth() + 1).padStart(2, '0');
      const day = String(d.getDate()).padStart(2, '0');
      
      if (short) {
        return `${year}-${month}-${day}`;
      }
      
      const weekdays = ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'];
      const weekday = weekdays[d.getDay()];
      
      return `${year}年${month}月${day}日 ${weekday}`;
    },
    
    getRandomColor(index) {
      return this.colors[index % this.colors.length];
    },

    
    // 初始化饼图
    initPieChart() {
      if (!this.$refs.pieChart) {
        console.error('找不到饼图DOM元素');
        return;
      }
      
      // 如果饼图实例已存在，则销毁
      if (this.pieChartInstance) {
        this.pieChartInstance.dispose();
      }
      
      try {
        // 初始化饼图
        this.pieChartInstance = echarts.init(this.$refs.pieChart);
        
        // 使用静态数据
        const chartData = this.ageDistribution.map((item, index) => ({
          name: item.name,
          value: item.value,
          itemStyle: {
            color: this.colors[index % this.colors.length]
          }
        }));
        
        // 配置饼图选项
        const option = {
          tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b}: {c} ({d}%)'
          },
          legend: {
            orient: 'vertical',
            left: 'left',
            data: chartData.map(item => item.name)
          },
          series: [
            {
              name: '年龄分布',
              type: 'pie',
              radius: '55%',
              center: ['50%', '60%'],
              data: chartData,
              emphasis: {
                itemStyle: {
                  shadowBlur: 10,
                  shadowOffsetX: 0,
                  shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
              },
              // 添加标签显示
              label: {
                show: true,
                formatter: '{b}: {c}人 ({d}%)'
              }
            }
          ]
        };
        
        // 应用配置
        this.pieChartInstance.setOption(option);
        console.log('饼图初始化完成');
      } catch (error) {
        console.error('饼图初始化失败:', error);
      }
    },

    // 初始化活动分类饼图
    initCategoryPieChart() {
      if (!this.$refs.categoryPieChart) {
        console.error('找不到活动分类饼图DOM元素');
        return;
      }
      
      // 如果饼图实例已存在，则销毁
      if (this.categoryPieChartInstance) {
        this.categoryPieChartInstance.dispose();
      }
      
      try {
        // 初始化饼图
        this.categoryPieChartInstance = echarts.init(this.$refs.categoryPieChart);
        
        // 使用活动分类分布数据
        const chartData = this.activityCategoryDistribution.map((item, index) => ({
          name: item.name,
          value: item.value,
          itemStyle: {
            color: this.colors[index % this.colors.length]
          }
        }));
        
        // 配置饼图选项
        const option = {
          tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b}: {c} ({d}%)'
          },
          legend: {
            orient: 'vertical',
            left: 'left',
            data: chartData.map(item => item.name)
          },
          series: [
            {
              name: '活动分类',
              type: 'pie',
              radius: '55%',
              center: ['50%', '60%'],
              data: chartData,
              emphasis: {
                itemStyle: {
                  shadowBlur: 10,
                  shadowOffsetX: 0,
                  shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
              },
              // 添加标签显示
              label: {
                show: true,
                formatter: '{b}: {c}个 ({d}%)'
              }
            }
          ]
        };
        
        // 应用配置
        this.categoryPieChartInstance.setOption(option);
        console.log('活动分类饼图初始化完成');
      } catch (error) {
        console.error('活动分类饼图初始化失败:', error);
      }
    }
  }
};
</script>
<style scoped>
/* 自定义el-table样式覆盖 */
:deep(.el-table) {
  border-radius: 0.75rem !important;
  overflow: hidden;
  border: none !important;
}

:deep(.el-table th) {
  background-color: #f8fafc !important;
  color: #475569 !important;
  font-weight: 600 !important;
  border-bottom: 1px solid #e2e8f0 !important;
}

:deep(.el-table td) {
  border-bottom: 1px solid #f1f5f9 !important;
  color: #334155;
}

:deep(.el-table--striped .el-table__body tr.el-table__row--striped td) {
  background-color: #f9fafb !important;
}

:deep(.el-table::before) {
  display: none;
}
</style>