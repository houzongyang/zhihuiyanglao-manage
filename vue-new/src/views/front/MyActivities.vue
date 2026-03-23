<template>
  <div class="max-w-6xl mx-auto px-4 py-6">
    <!-- 页面标题 -->
    <div class="mb-6">
      <div class="flex items-center justify-between">
        <div>
          <div class="text-2xl font-bold text-gray-800 mb-2 flex items-center">
            <i class="el-icon-user text-blue-500 mr-2"></i>
            我的活动
          </div>
          <p class="text-gray-600">查看您参与的所有活动记录，管理您的活动安排</p>
        </div>
        <el-button type="primary" plain @click="exportData" v-if="activities.length > 0">
          <i class="el-icon-download mr-1"></i>
          导出数据
        </el-button>
      </div>
    </div>

    <!-- 统计卡片 -->
    <div class="grid grid-cols-1 md:grid-cols-4 gap-4 mb-6">
      <div class="bg-white border border-gray-200 rounded-lg p-4 shadow-sm">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-gray-500 text-sm">总报名数</p>
            <p class="text-2xl font-bold text-gray-800">{{ statistics.total }}</p>
          </div>
          <i class="el-icon-document text-3xl text-blue-500"></i>
        </div>
      </div>
      <div class="bg-white border border-gray-200 rounded-lg p-4 shadow-sm">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-gray-500 text-sm">已参加</p>
            <p class="text-2xl font-bold text-gray-800">{{ statistics.attended }}</p>
          </div>
          <i class="el-icon-check text-3xl text-green-500"></i>
        </div>
      </div>
      <div class="bg-white border border-gray-200 rounded-lg p-4 shadow-sm">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-gray-500 text-sm">待参加</p>
            <p class="text-2xl font-bold text-gray-800">{{ statistics.registered }}</p>
          </div>
          <i class="el-icon-time text-3xl text-orange-500"></i>
        </div>
      </div>
      <div class="bg-white border border-gray-200 rounded-lg p-4 shadow-sm">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-gray-500 text-sm">平均评分</p>
            <p class="text-2xl font-bold text-gray-800">{{ statistics.averageRating }}</p>
          </div>
          <i class="el-icon-star-on text-3xl text-yellow-500"></i>
        </div>
      </div>
    </div>

    <!-- 筛选和搜索区 -->
    <div class="bg-white rounded-lg border border-gray-200 p-6 mb-6">
      <div class="grid grid-cols-1 md:grid-cols-4 gap-4">
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">参与状态</label>
          <el-select v-model="searchForm.attendanceStatus" placeholder="选择状态" clearable class="w-full" @change="handleSearch">
            <el-option label="全部" value=""></el-option>
            <el-option label="已报名" value="REGISTERED"></el-option>
            <el-option label="已参加" value="ATTENDED"></el-option>
            <el-option label="缺席" value="ABSENT"></el-option>
            <el-option label="已取消" value="CANCELLED"></el-option>
          </el-select>
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">活动分类</label>
          <el-select v-model="searchForm.categoryId" placeholder="选择分类" clearable class="w-full" @change="handleSearch">
            <el-option label="全部" value=""></el-option>
            <el-option 
              v-for="category in categories" 
              :key="category.id" 
              :label="category.name" 
              :value="category.id">
            </el-option>
          </el-select>
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">活动名称</label>
          <el-input 
            v-model="searchForm.activityTitle" 
            placeholder="搜索活动名称" 
            clearable
            class="w-full"
            @input="handleSearch">
          </el-input>
        </div>
        <div class="flex items-end">
          <div class="flex space-x-2 w-full">
            <el-button type="primary" @click="handleSearch" class="flex-1">
              <i class="el-icon-search mr-1"></i>
              搜索
            </el-button>
            <el-button @click="resetSearch" class="flex-1">
              <i class="el-icon-refresh mr-1"></i>
              重置
            </el-button>
          </div>
        </div>
      </div>
    </div>

    <!-- 活动列表 -->
    <div v-loading="loading" class="space-y-4">
      <div 
        v-for="activity in activities" 
        :key="activity.id" 
        class="bg-white rounded-lg border border-gray-200 hover:border-gray-300 transition-all duration-300"
      >
        <!-- 活动卡片头部 -->
        <div class="px-6 pt-4 pb-2 border-b border-gray-100">
          <div class="flex items-start justify-between">
            <div class="flex-1">
              <div class="flex items-center mb-2">
                <h3 class="text-gray-800 text-lg font-semibold mr-3 truncate hover:text-blue-600 cursor-pointer" 
                    @click="viewActivityDetail(activity.activityId)">{{ activity.activityTitle }}</h3>
                <span class="inline-flex items-center px-2.5 py-0.5 rounded-md text-xs font-medium bg-gray-100 text-gray-700">
                  {{ activity.categoryName }}
                </span>
              </div>
              <div class="flex items-center text-gray-600 text-sm space-x-4">
                <span><i class="el-icon-date mr-1"></i>{{ activity.activityDate }}</span>
                <span><i class="el-icon-time mr-1"></i>{{ activity.startTime }} - {{ activity.endTime }}</span>
                <span><i class="el-icon-location mr-1"></i>{{ activity.activityLocation || '地点待定' }}</span>
              </div>
            </div>
            
            <div class="flex flex-col items-end space-y-2">
              <span class="inline-flex items-center px-3 py-1 rounded-md text-xs font-medium" 
                    :class="getStatusClass(activity.attendanceStatus)">
                {{ getAttendanceStatusName(activity.attendanceStatus) }}
              </span>
              <span class="inline-flex items-center px-3 py-1 rounded-md text-xs font-medium" 
                    :class="getActivityStatusClass(activity.activityStatus)">
                {{ getActivityStatusName(activity.activityStatus) }}
              </span>
            </div>
          </div>
        </div>
        
        <!-- 活动卡片内容 -->
        <div class="px-6 py-4">
          <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 text-sm">
            <!-- 基本信息 -->
            <div class="space-y-2">
              <div class="flex items-center text-gray-600">
                <i class="el-icon-location-outline mr-2 text-gray-500"></i>
                <span class="font-medium mr-2">地点:</span>
                <span>{{ activity.activityLocation || '地点待定' }}</span>
              </div>
              <div class="flex items-center text-gray-600">
                <i class="el-icon-coin mr-2 text-gray-500"></i>
                <span class="font-medium mr-2">费用:</span>
                <span v-if="activity.cost > 0" class="text-gray-800 font-semibold">¥{{ activity.cost }}</span>
                <span v-else class="text-gray-800 font-semibold">免费</span>
              </div>
              <div class="flex items-center text-gray-600" v-if="activity.emergencyContact">
                <i class="el-icon-phone mr-2 text-gray-500"></i>
                <span class="font-medium mr-2">联系人:</span>
                <span>{{ activity.emergencyContact }}</span>
              </div>
            </div>
            
            <!-- 时间信息 -->
            <div class="space-y-2">
              <div class="flex items-center text-gray-600">
                <i class="el-icon-time mr-2 text-gray-500"></i>
                <span class="font-medium mr-2">报名时间:</span>
                <span>{{ formatDateTime(activity.registrationTime) }}</span>
              </div>
              <div class="flex items-center text-gray-600" v-if="activity.checkinTime">
                <i class="el-icon-circle-check mr-2 text-green-500"></i>
                <span class="font-medium mr-2">签到时间:</span>
                <span>{{ formatDateTime(activity.checkinTime) }}</span>
              </div>
              <div class="flex items-center text-gray-600" v-if="activity.checkoutTime">
                <i class="el-icon-circle-close mr-2 text-red-500"></i>
                <span class="font-medium mr-2">签退时间:</span>
                <span>{{ formatDateTime(activity.checkoutTime) }}</span>
              </div>
            </div>
            
            <!-- 评价信息 -->
            <div class="space-y-2">
              <div class="flex items-center text-gray-600" v-if="activity.rating">
                <i class="el-icon-star-on mr-2 text-yellow-500"></i>
                <span class="font-medium mr-2">我的评分:</span>
                <el-rate 
                  v-model="activity.rating" 
                  disabled 
                  show-score 
                  text-color="#ff9900" 
                  score-template="{value}"
                  class="inline-flex"
                ></el-rate>
              </div>
              <div class="flex flex-wrap gap-2">
                <el-button 
                  size="mini" 
                  type="primary" 
                  plain 
                  @click="viewActivityDetail(activity.activityId)"
                >
                  查看详情
                </el-button>
                <el-button 
                  v-if="activity.attendanceStatus === 'REGISTERED' && activity.activityStatus === 'PLANNED'"
                  size="mini" 
                  type="danger" 
                  plain 
                  @click="cancelRegistration(activity)"
                >
                  取消报名
                </el-button>
                <el-button 
                  v-if="activity.attendanceStatus === 'ATTENDED' && !activity.rating"
                  size="mini" 
                  type="warning" 
                  plain 
                  @click="rateActivity(activity)"
                >
                  评价活动
                </el-button>
              </div>
            </div>
          </div>
          
          <!-- 反馈信息 -->
          <div v-if="activity.feedback" class="mt-4 pt-4 border-t border-gray-100">
            <div class="flex items-start text-gray-600">
              <i class="el-icon-chat-dot-round mr-2 text-gray-500 mt-0.5"></i>
              <div>
                <span class="font-medium mr-2">我的反馈:</span>
                <p class="text-gray-700 mt-1 leading-relaxed bg-gray-50 rounded p-3">{{ activity.feedback }}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 空状态提示 -->
    <div v-if="activities.length === 0 && !loading" class="text-center py-16">
      <div class="mb-6">
        <i class="el-icon-document-remove text-8xl text-gray-300"></i>
      </div>
      <h3 class="text-xl font-medium text-gray-800 mb-3">暂无活动记录</h3>
      <p class="text-gray-500 mb-8 max-w-md mx-auto">您还没有参与任何活动，快去活动中心报名参加精彩的活动吧！</p>
      <el-button type="primary" size="medium" @click="$router.push('/front/activity')">
        <i class="el-icon-plus mr-2"></i>
        去报名活动
      </el-button>
    </div>

    <!-- 分页组件 -->
    <div class="mt-6 flex justify-center" v-if="total > 0">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-sizes="[5, 10, 20, 50]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        background
      ></el-pagination>
    </div>

    <!-- 评价对话框 -->
    <el-dialog
      title="评价活动"
      :visible.sync="ratingDialogVisible"
      width="500px"
      @close="resetRatingForm"
    >
      <div v-if="currentActivity">
        <div class="mb-4">
          <h3 class="text-lg font-medium text-gray-800 mb-2">{{ currentActivity.activityTitle }}</h3>
          <p class="text-sm text-gray-600">{{ currentActivity.activityDate }} {{ currentActivity.startTime }} - {{ currentActivity.endTime }}</p>
        </div>
        
        <el-form :model="ratingForm" label-width="80px">
          <el-form-item label="活动评分">
            <el-rate 
              v-model="ratingForm.rating" 
              show-text 
              :texts="['很差', '较差', '一般', '很好', '非常好']"
            ></el-rate>
          </el-form-item>
          <el-form-item label="活动反馈">
            <el-input 
              v-model="ratingForm.feedback" 
              type="textarea" 
              :rows="4" 
              placeholder="请分享您对这次活动的感受和建议..."
              maxlength="500"
              show-word-limit
            ></el-input>
          </el-form-item>
        </el-form>
      </div>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="ratingDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitRating" :loading="submittingRating">提交评价</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "MyActivities",
  data() {
    return {
      loading: false,
      activities: [],
      categories: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      searchForm: {
        attendanceStatus: "",
        categoryId: "",
        activityTitle: ""
      },
      statistics: {
        total: 0,
        attended: 0,
        registered: 0,
        averageRating: 0
      },
      ratingDialogVisible: false,
      currentActivity: null,
      ratingForm: {
        rating: 0,
        feedback: ""
      },
      submittingRating: false
    };
  },
  created() {
    this.fetchCategories();
    this.fetchMyActivities();
  },
  methods: {
    // 获取我的活动列表
    fetchMyActivities() {
      const userStr = localStorage.getItem('xm-user');
      if (!userStr) {
        this.$message.error('请先登录');
        this.$router.push('/login');
        return;
      }
      
      const user = JSON.parse(userStr);
      if (!user.id) {
        this.$message.error('用户信息异常');
        return;
      }
      
      this.loading = true;
      
      this.$request.get(`/activityParticipant/userRegistrations/${user.id}`)
        .then(res => {
          if (res.code === '200') {
            this.activities = res.data || [];
            this.total = this.activities.length;
            this.calculateStatistics();
            this.applyFilters();
          } else {
            this.$message.error(res.msg || '获取活动记录失败');
          }
        })
        .catch(error => {
          console.error('获取活动记录异常:', error);
          this.$message.error('获取活动记录异常');
        })
        .finally(() => {
          this.loading = false;
        });
    },
    
    // 获取活动分类
    fetchCategories() {
      this.$request.get("/activity/category/selectAll")
        .then(res => {
          if (res.code === '200') {
            this.categories = res.data || [];
          }
        })
        .catch(() => {
          console.error("获取活动分类失败");
        });
    },
    
    // 计算统计数据
    calculateStatistics() {
      const total = this.activities.length;
      const attended = this.activities.filter(item => item.attendanceStatus === 'ATTENDED').length;
      const registered = this.activities.filter(item => item.attendanceStatus === 'REGISTERED').length;
      
      const ratedActivities = this.activities.filter(item => item.rating && item.rating > 0);
      const averageRating = ratedActivities.length > 0 
        ? (ratedActivities.reduce((sum, item) => sum + item.rating, 0) / ratedActivities.length).toFixed(1)
        : 0;
      
      this.statistics = {
        total,
        attended,
        registered,
        averageRating
      };
    },
    
    // 应用筛选
    applyFilters() {
      let filteredActivities = [...this.activities];
      
      // 按状态筛选
      if (this.searchForm.attendanceStatus) {
        filteredActivities = filteredActivities.filter(item => 
          item.attendanceStatus === this.searchForm.attendanceStatus
        );
      }
      
      // 按分类筛选
      if (this.searchForm.categoryId) {
        filteredActivities = filteredActivities.filter(item => 
          item.categoryId === this.searchForm.categoryId
        );
      }
      
      // 按活动名称搜索
      if (this.searchForm.activityTitle) {
        filteredActivities = filteredActivities.filter(item => 
          item.activityTitle && item.activityTitle.toLowerCase().includes(this.searchForm.activityTitle.toLowerCase())
        );
      }
      
      this.total = filteredActivities.length;
      
      // 分页处理
      const start = (this.pageNum - 1) * this.pageSize;
      const end = start + this.pageSize;
      this.activities = filteredActivities.slice(start, end);
    },
    
    // 搜索处理
    handleSearch() {
      this.pageNum = 1;
      this.fetchMyActivities();
    },
    
    // 重置搜索
    resetSearch() {
      this.searchForm = {
        attendanceStatus: "",
        categoryId: "",
        activityTitle: ""
      };
      this.pageNum = 1;
      this.fetchMyActivities();
    },
    
    // 分页大小改变
    handleSizeChange(val) {
      this.pageSize = val;
      this.pageNum = 1;
      this.applyFilters();
    },
    
    // 页码改变
    handleCurrentChange(val) {
      this.pageNum = val;
      this.applyFilters();
    },
    
    // 取消报名
    cancelRegistration(activity) {
      this.$confirm(`确定要取消报名"${activity.activityTitle}"活动吗？`, '确认取消', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const userStr = localStorage.getItem('xm-user');
        const user = JSON.parse(userStr);
        
        this.$request.post('/activityParticipant/cancel', null, {
          params: {
            activityId: activity.activityId,
            userId: user.id
          }
        })
          .then(res => {
            if (res.code === '200') {
              this.$message.success('取消报名成功');
              this.fetchMyActivities();
            } else {
              this.$message.error(res.msg || '取消报名失败');
            }
          })
          .catch(error => {
            console.error('取消报名失败:', error);
            this.$message.error('取消报名失败，请稍后重试');
          });
      }).catch(() => {});
    },
    
    // 查看活动详情
    viewActivityDetail(activityId) {
      this.$router.push(`/front/activity/${activityId}`);
    },
    
    // 评价活动
    rateActivity(activity) {
      this.currentActivity = activity;
      this.ratingForm = {
        rating: activity.rating || 0,
        feedback: activity.feedback || ""
      };
      this.ratingDialogVisible = true;
    },
    
    // 提交评价
    submitRating() {
      if (this.ratingForm.rating === 0) {
        this.$message.warning('请选择评分');
        return;
      }
      
      this.submittingRating = true;
      
      this.$request.put('/activityParticipant/update', {
        id: this.currentActivity.id,
        rating: this.ratingForm.rating,
        feedback: this.ratingForm.feedback
      })
        .then(res => {
          if (res.code === '200') {
            this.$message.success('评价提交成功');
            this.ratingDialogVisible = false;
            this.fetchMyActivities();
          } else {
            this.$message.error(res.msg || '评价提交失败');
          }
        })
        .catch(error => {
          console.error('评价提交失败:', error);
          this.$message.error('评价提交失败，请稍后重试');
        })
        .finally(() => {
          this.submittingRating = false;
        });
    },
    
    // 重置评价表单
    resetRatingForm() {
      this.ratingForm = {
        rating: 0,
        feedback: ""
      };
      this.currentActivity = null;
    },
    
    // 导出数据
    exportData() {
      this.$message.info('导出功能开发中...');
    },
    
    // 格式化日期时间
    formatDateTime(dateTime) {
      if (!dateTime) return '';
      const date = new Date(dateTime);
      return date.getFullYear() + '-' + 
             String(date.getMonth() + 1).padStart(2, '0') + '-' + 
             String(date.getDate()).padStart(2, '0') + ' ' +
             String(date.getHours()).padStart(2, '0') + ':' + 
             String(date.getMinutes()).padStart(2, '0');
    },
    
    // 获取参与状态名称
    getAttendanceStatusName(status) {
      switch (status) {
        case 'REGISTERED':
          return '已报名';
        case 'ATTENDED':
          return '已参加';
        case 'ABSENT':
          return '缺席';
        case 'CANCELLED':
          return '已取消';
        default:
          return '未知';
      }
    },
    
    // 获取参与状态标签类型
    getAttendanceStatusTagType(status) {
      switch (status) {
        case 'REGISTERED':
          return 'info';
        case 'ATTENDED':
          return 'success';
        case 'ABSENT':
          return 'warning';
        case 'CANCELLED':
          return 'danger';
        default:
          return 'info';
      }
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
    
    // 获取参与状态样式类
    getStatusClass(status) {
      const statusClasses = {
        'REGISTERED': 'bg-blue-100 text-blue-800',
        'ATTENDED': 'bg-green-100 text-green-800',
        'ABSENT': 'bg-yellow-100 text-yellow-800',
        'CANCELLED': 'bg-red-100 text-red-800'
      }
      return statusClasses[status] || 'bg-gray-100 text-gray-800'
    },
    
    // 获取活动状态样式类
    getActivityStatusClass(status) {
      const statusClasses = {
        'PLANNED': 'bg-blue-100 text-blue-800',
        'ONGOING': 'bg-green-100 text-green-800',
        'COMPLETED': 'bg-gray-100 text-gray-800',
        'CANCELLED': 'bg-red-100 text-red-800'
      }
      return statusClasses[status] || 'bg-gray-100 text-gray-800'
    },
    
    // 获取活动头部背景样式
    getActivityHeaderClass(status) {
      switch (status) {
        case 'PLANNED':
          return 'from-blue-500 to-blue-600 bg-gradient-to-r';
        case 'ONGOING':
          return 'from-green-500 to-green-600 bg-gradient-to-r';
        case 'COMPLETED':
          return 'from-gray-500 to-gray-600 bg-gradient-to-r';
        case 'CANCELLED':
          return 'from-red-500 to-red-600 bg-gradient-to-r';
        default:
          return 'from-blue-500 to-blue-600 bg-gradient-to-r';
      }
    }
  }
};
</script>

<style scoped>
.bg-pattern {
  background-image: url("data:image/svg+xml,%3Csvg width='100' height='100' viewBox='0 0 100 100' xmlns='http://www.w3.org/2000/svg'%3E%3Cpath d='M11 18c3.866 0 7-3.134 7-7s-3.134-7-7-7-7 3.134-7 7 3.134 7 7 7zm48 25c3.866 0 7-3.134 7-7s-3.134-7-7-7-7 3.134-7 7 3.134 7 7 7zm-43-7c1.657 0 3-1.343 3-3s-1.343-3-3-3-3 1.343-3 3 1.343 3 3 3zm63 31c1.657 0 3-1.343 3-3s-1.343-3-3-3-3 1.343-3 3 1.343 3 3 3zM34 90c1.657 0 3-1.343 3-3s-1.343-3-3-3-3 1.343-3 3 1.343 3 3 3zm56-76c1.657 0 3-1.343 3-3s-1.343-3-3-3-3 1.343-3 3 1.343 3 3 3zM12 86c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm28-65c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm23-11c2.76 0 5-2.24 5-5s-2.24-5-5-5-5 2.24-5 5 2.24 5 5 5zm-6 60c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm29 22c2.76 0 5-2.24 5-5s-2.24-5-5-5-5 2.24-5 5 2.24 5 5 5zM32 63c2.76 0 5-2.24 5-5s-2.24-5-5-5-5 2.24-5 5 2.24 5 5 5zm57-13c2.76 0 5-2.24 5-5s-2.24-5-5-5-5 2.24-5 5 2.24 5 5 5zm-9-21c1.105 0 2-.895 2-2s-.895-2-2-2-2 .895-2 2 .895 2 2 2zM60 91c1.105 0 2-.895 2-2s-.895-2-2-2-2 .895-2 2 .895 2 2 2zM35 41c1.105 0 2-.895 2-2s-.895-2-2-2-2 .895-2 2 .895 2 2 2zM12 60c1.105 0 2-.895 2-2s-.895-2-2-2-2 .895-2 2 .895 2 2 2z' fill='%23ffffff' fill-opacity='0.1' fill-rule='evenodd'/%3E%3C/svg%3E");
}

/* 卡片悬浮效果 */
.hover\:shadow-md:hover {
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
}

/* 响应式网格调整 */
@media (max-width: 768px) {
  .grid-cols-1.md\:grid-cols-4 {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
  
  .grid-cols-1.md\:grid-cols-3 {
    grid-template-columns: repeat(1, minmax(0, 1fr));
  }
}

/* 文字截断 */
.truncate {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* 对话框样式优化 */
:deep(.el-dialog) {
  border-radius: 0.5rem;
  overflow: hidden;
}

:deep(.el-dialog__header) {
  padding: 1.5rem;
  border-bottom: 1px solid #f0f0f0;
  background-color: #f9fafb;
}

:deep(.el-dialog__body) {
  padding: 1.5rem;
}

:deep(.el-rate) {
  line-height: 1;
}
</style>