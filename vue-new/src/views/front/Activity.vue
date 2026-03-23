<template>
  <div>
    <!-- 顶部信息展示 -->
    <div class="mb-6">
      <div class="text-2xl font-bold text-gray-800 mb-2 flex items-center">
        <i class="el-icon-date text-blue-500 mr-2"></i>
        活动中心
      </div>
      <p class="text-gray-600">探索丰富多彩的活动，丰富您的生活，增进健康与社交</p>
    </div>

    <!-- 过滤和搜索区 -->
    <div class="bg-gray-50 rounded-lg p-4 mb-6 shadow-sm">
      <div class="flex flex-wrap items-center justify-between">
        <div class="flex flex-wrap gap-2 mb-2 sm:mb-0">
          <el-select 
            v-model="searchForm.categoryId" 
            placeholder="活动分类" 
            size="small"
            clearable
            @change="handleSearch"
          >
            <el-option
              v-for="category in categories"
              :key="category.id"
              :label="category.name"
              :value="category.id"
            ></el-option>
          </el-select>
          
          <el-select 
            v-model="searchForm.status" 
            placeholder="活动状态" 
            size="small"
            clearable
            @change="handleSearch"
          >
            <el-option label="计划中" value="PLANNED"></el-option>
            <el-option label="进行中" value="ONGOING"></el-option>
            <el-option label="已完成" value="COMPLETED"></el-option>
          </el-select>
        </div>
        
        <div class="w-full sm:w-auto">
          <el-input 
            v-model="searchForm.title" 
            placeholder="搜索活动名称" 
            prefix-icon="el-icon-search"
            size="small"
            @keyup.enter.native="handleSearch"
            clearable
          >
            <el-button slot="append" icon="el-icon-search" @click="handleSearch"></el-button>
          </el-input>
        </div>
      </div>
    </div>
    
    <!-- 活动分类标签 -->
    <div class="mb-6 flex flex-wrap gap-2">
      <el-tag 
        :type="currentCategory === '' ? 'primary' : ''" 
        effect="plain" 
        @click="filterByCategory('')"
        class="cursor-pointer hover:shadow-sm transform hover:scale-105 transition-all"
      >
        全部活动
      </el-tag>
      <el-tag 
        v-for="category in categories" 
        :key="category.id" 
        :type="currentCategory === category.id ? 'primary' : ''" 
        effect="plain" 
        @click="filterByCategory(category.id)"
        class="cursor-pointer hover:shadow-sm transform hover:scale-105 transition-all"
        :style="{ borderColor: category.color, color: currentCategory === category.id ? '' : category.color }"
      >
        {{ category.name }}
      </el-tag>
    </div>

    <!-- 活动卡片列表 -->
    <el-row :gutter="20" v-loading="loading">
      <el-col 
        v-for="activity in activities" 
        :key="activity.id" 
        :xs="24" 
        :sm="12" 
        :md="8" 
        :lg="8" 
        :xl="6" 
        class="mb-5"
      >
        <div 
          class="bg-white rounded-lg overflow-hidden h-full cursor-pointer transition-all duration-300 transform hover:-translate-y-1 border border-gray-200 hover:border-blue-300 shadow-sm hover:shadow-md relative"
          @click="viewActivityDetail(activity.id)"
        >
          <!-- 活动状态标签 - 移到卡片右上角 -->
          <div class="absolute top-2 right-2 z-20">
            <el-tag size="mini" :type="getStatusTagType(activity.status)" class="shadow-sm">
              {{ getStatusName(activity.status) }}
            </el-tag>
          </div>

          <!-- 活动头部 -->
          <div class="h-40 flex items-center justify-center relative overflow-hidden">
            <div class="absolute inset-0 opacity-20">
              <div class="absolute inset-0 bg-pattern"></div>
            </div>
            <!-- 使用封面图作为背景 -->
            <div v-if="activity.coverImage" class="absolute inset-0">
              <img :src="activity.coverImage" class="w-full h-full object-contain" />
            </div>
          </div>

          <!-- 活动内容 -->
          <div class="p-4">
            <!-- 标题和分类移到这里 -->
            <div class="mb-3">
              <div class="text-gray-800 text-xl font-bold mb-2 line-clamp-2">{{ activity.title }}</div>
              <div class="flex">
                <el-tag size="small" :style="{ backgroundColor: getCategoryColor(activity.categoryId), border: 'none', color: '#fff', boxShadow: '0 1px 2px rgba(0,0,0,0.1)' }">
                  {{ getCategoryName(activity.categoryId) }}
                </el-tag>
              </div>
            </div>
            
            <div class="flex items-center mb-3">
              <i class="el-icon-time text-gray-400 mr-2"></i>
              <div class="text-sm text-gray-700">{{ activity.activityDate }}, {{ activity.startTime }}-{{ activity.endTime }}</div>
            </div>
            
            <div class="flex items-center mb-3">
              <i class="el-icon-location text-gray-400 mr-2"></i>
              <div class="text-sm text-gray-700 truncate">{{ activity.location || '地点待定' }}</div>
            </div>
            
            <div class="flex items-center mb-3">
              <i class="el-icon-user text-gray-400 mr-2"></i>
              <div class="text-sm text-gray-700">{{ activity.organizer }}</div>
            </div>
            
            <!-- 活动简介 -->
            <div class="text-gray-600 text-sm line-clamp-2 mb-4">
              {{ activity.description || '暂无活动简介' }}
            </div>
            
            <!-- 参与情况和费用 -->
            <div class="flex justify-between items-center">
              <div class="text-sm">
                <span class="text-blue-600 font-medium">{{ activity.currentParticipants }}</span>
                <span class="text-gray-400">/{{ activity.maxParticipants || '不限' }}</span>
                <span class="text-gray-500 ml-1">人已报名</span>
              </div>
              
              <div class="text-sm">
                <span v-if="activity.cost > 0" class="text-orange-500 font-medium">¥{{ activity.cost }}</span>
                <span v-else class="text-green-500 font-medium">免费</span>
              </div>
            </div>
          </div>

          <!-- 活动底部操作区 -->
          <div class="px-4 py-3 bg-gray-50 border-t border-gray-100 flex justify-between items-center">
            <div class="flex items-center text-sm text-gray-500">
              <i class="el-icon-timer mr-1"></i>
              <span>{{ isActivityExpired(activity.registrationDeadline) ? '报名已截止' : '报名中' }}</span>
            </div>
            
            <el-button 
              v-if="!isUserRegistered(activity.id)"
              type="primary" 
              size="mini" 
              plain 
              round
              :disabled="isActivityExpired(activity.registrationDeadline) || activity.status !== 'PLANNED'"
              @click.stop="handleSignUp(activity)"
            >
              立即报名
            </el-button>
            <el-button 
              v-else
              type="success" 
              size="mini" 
              plain 
              round
              @click.stop="handleCancelRegistration(activity)"
            >
              已报名
            </el-button>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 空状态提示 -->
    <el-empty
      v-if="activities.length === 0 && !loading"
      description="暂无相关活动"
      :image-size="200"
    >
      <el-button type="primary" @click="resetSearch">查看全部活动</el-button>
    </el-empty>

    <!-- 分页组件 -->
    <div class="mt-5 flex justify-center" v-if="total > 0">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-sizes="[8, 12, 24, 48]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        background
      ></el-pagination>
    </div>
  </div>
</template>

<script>
export default {
  name: "FrontActivity",
  data() {
    return {
      loading: false,
      activities: [],
      categories: [],
      currentCategory: '',
      total: 0,
      pageNum: 1,
      pageSize: 8,
      searchForm: {
        title: "",
        categoryId: "",
        status: ""
      },
      userRegistrations: [] // 用户已报名的活动列表
    };
  },
  created() {
    this.fetchCategories();
    this.fetchActivities();
    this.fetchUserRegistrations();
  },
  methods: {
    // 获取活动数据
    fetchActivities() {
      this.loading = true;
      
      const params = {
        pageNum: this.pageNum,
        pageSize: this.pageSize
      };
      
      if (this.searchForm.title) {
        params.title = this.searchForm.title;
      }
      
      if (this.searchForm.categoryId) {
        params.categoryId = this.searchForm.categoryId;
      }
      
      if (this.searchForm.status) {
        params.status = this.searchForm.status;
      }
      
      this.$request.get("/activity/selectPage", { params })
        .then(res => {
          if (res.code === '200') {
            this.activities = res.data.list || [];
            this.total = res.data.total || 0;
          } else {
            this.$message.error(res.msg || "获取活动数据失败");
          }
        })
        .catch(() => {
          this.$message.error("获取活动数据异常");
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
          this.$message.error("获取活动分类失败");
        });
    },
    
    // 获取用户报名记录
    fetchUserRegistrations() {
      const userStr = localStorage.getItem('xm-user');
      if (!userStr) {
        return;
      }
      
      const user = JSON.parse(userStr);
      if (!user.id) {
        return;
      }
      
      this.$request.get(`/activityParticipant/userRegistrations/${user.id}`)
        .then(res => {
          if (res.code === '200') {
            this.userRegistrations = res.data || [];
          }
        })
        .catch(() => {
          // 静默处理错误，不影响页面正常显示
        });
    },
    
    // 检查用户是否已报名某活动
    isUserRegistered(activityId) {
      return this.userRegistrations.some(registration => 
        registration.activityId === activityId && 
        registration.attendanceStatus !== 'CANCELLED'
      );
    },
    
    // 按分类筛选
    filterByCategory(categoryId) {
      this.currentCategory = categoryId;
      this.searchForm.categoryId = categoryId;
      this.pageNum = 1;
      this.fetchActivities();
    },
    
    // 处理搜索
    handleSearch() {
      this.pageNum = 1;
      this.fetchActivities();
    },
    
    // 重置搜索
    resetSearch() {
      this.searchForm = {
        title: "",
        categoryId: "",
        status: ""
      };
      this.currentCategory = '';
      this.pageNum = 1;
      this.fetchActivities();
    },
    
    // 分页相关
    handleSizeChange(val) {
      this.pageSize = val;
      this.fetchActivities();
    },
    
    handleCurrentChange(val) {
      this.pageNum = val;
      this.fetchActivities();
    },
    
    // 获取分类名称
    getCategoryName(categoryId) {
      const category = this.categories.find(item => item.id === categoryId);
      return category ? category.name : '未分类';
    },
    
    // 获取分类颜色
    getCategoryColor(categoryId) {
      const category = this.categories.find(item => item.id === categoryId);
      return category?.color || '#909399';
    },
    
    // 获取状态名称
    getStatusName(status) {
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
    
    // 获取状态标签类型
    getStatusTagType(status) {
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
    
    // 判断活动是否已截止报名
    isActivityExpired(deadline) {
      if (!deadline) return false;
      console.log(new Date(deadline));
      return new Date(deadline) < new Date();
    },
    
    // 报名活动
    handleSignUp(activity) {
      // 获取当前登录用户信息
      const userStr = localStorage.getItem('xm-user');
      if (!userStr) {
        this.$message.error('请先登录');
        this.$router.push('/login');
        return;
      }
      
      const user = JSON.parse(userStr);
      if (!user.id) {
        this.$message.error('用户信息异常，请重新登录');
        return;
      }

      this.$confirm(`确定要报名参加"${activity.title}"活动吗?`, '报名确认', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        // 调用报名接口
        const registrationData = {
          activityId: activity.id,
          userId: user.id,
          specialRequirements: '',
          emergencyContact: user.name || '',
          emergencyPhone: user.phone || '',
          remarks: ''
        };
        
        this.$request.post('/activityParticipant/register', registrationData)
          .then(res => {
            if (res.code === '200') {
              this.$message({
                type: 'success',
                message: res.data || '报名成功'
              });
              // 刷新活动列表以更新参与人数
               this.fetchActivities();
               // 刷新用户报名记录
               this.fetchUserRegistrations();
            } else {
              this.$message.error(res.msg || '报名失败');
            }
          })
          .catch(error => {
            console.error('报名失败:', error);
            this.$message.error('报名失败，请稍后重试');
          });
      }).catch(() => {});
    },
    
    // 取消报名
    handleCancelRegistration(activity) {
      const userStr = localStorage.getItem('xm-user');
      if (!userStr) {
        this.$message.error('请先登录');
        return;
      }
      
      const user = JSON.parse(userStr);
      if (!user.id) {
        this.$message.error('用户信息异常，请重新登录');
        return;
      }

      this.$confirm(`确定要取消报名"${activity.title}"活动吗?`, '取消报名确认', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$request.post('/activityParticipant/cancel', null, {
          params: {
            activityId: activity.id,
            userId: user.id
          }
        })
          .then(res => {
            if (res.code === '200') {
              this.$message({
                type: 'success',
                message: res.data || '取消报名成功'
              });
              // 刷新活动列表以更新参与人数
              this.fetchActivities();
              // 刷新用户报名记录
              this.fetchUserRegistrations();
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
    viewActivityDetail(id) {
      this.$router.push(`/front/activity/${id}`);
    }
  }
};
</script>

<style scoped>
.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.bg-pattern {
  background-image: url("data:image/svg+xml,%3Csvg width='100' height='100' viewBox='0 0 100 100' xmlns='http://www.w3.org/2000/svg'%3E%3Cpath d='M11 18c3.866 0 7-3.134 7-7s-3.134-7-7-7-7 3.134-7 7 3.134 7 7 7zm48 25c3.866 0 7-3.134 7-7s-3.134-7-7-7-7 3.134-7 7 3.134 7 7 7zm-43-7c1.657 0 3-1.343 3-3s-1.343-3-3-3-3 1.343-3 3 1.343 3 3 3zm63 31c1.657 0 3-1.343 3-3s-1.343-3-3-3-3 1.343-3 3 1.343 3 3 3zM34 90c1.657 0 3-1.343 3-3s-1.343-3-3-3-3 1.343-3 3 1.343 3 3 3zm56-76c1.657 0 3-1.343 3-3s-1.343-3-3-3-3 1.343-3 3 1.343 3 3 3zM12 86c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm28-65c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm23-11c2.76 0 5-2.24 5-5s-2.24-5-5-5-5 2.24-5 5 2.24 5 5 5zm-6 60c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm29 22c2.76 0 5-2.24 5-5s-2.24-5-5-5-5 2.24-5 5 2.24 5 5 5zM32 63c2.76 0 5-2.24 5-5s-2.24-5-5-5-5 2.24-5 5 2.24 5 5 5zm57-13c2.76 0 5-2.24 5-5s-2.24-5-5-5-5 2.24-5 5 2.24 5 5 5zm-9-21c1.105 0 2-.895 2-2s-.895-2-2-2-2 .895-2 2 .895 2 2 2zM60 91c1.105 0 2-.895 2-2s-.895-2-2-2-2 .895-2 2 .895 2 2 2zM35 41c1.105 0 2-.895 2-2s-.895-2-2-2-2 .895-2 2 .895 2 2 2zM12 60c1.105 0 2-.895 2-2s-.895-2-2-2-2 .895-2 2 .895 2 2 2z' fill='%23ffffff' fill-opacity='0.1' fill-rule='evenodd'/%3E%3C/svg%3E");
}
</style>