<template>
  <div>
    <!-- 返回按钮 -->
    <div class="mb-6">
      <el-button 
        icon="el-icon-arrow-left" 
        @click="$router.push('/front/activity')" 
        plain 
        size="small"
        class="hover:shadow-sm transition-all"
      >
        返回活动列表
      </el-button>
    </div>

    <div v-loading="loading">
      <!-- 活动详情卡片 -->
      <div v-if="activity" class="bg-white rounded-lg overflow-hidden mb-6">
        <!-- 活动头部 -->
        <div 
          class="relative h-48 bg-gradient-to-r px-6 pt-6 pb-12 flex flex-col justify-between"
          :class="getActivityHeaderClass(activity.status)"
        >
          <div class="absolute inset-0 opacity-20">
            <div class="absolute inset-0 bg-pattern"></div>
          </div>
          
          <!-- 使用封面图作为背景 -->
          <!-- <div v-if="activity.coverImage" class="absolute inset-0">
            <img :src="activity.coverImage" class="w-full h-full object-cover opacity-40" />
          </div> -->
          
          <!-- 活动标题和分类 -->
          <div class="relative flex justify-between items-start">
            <div>
              <div class="flex items-center mb-2">
                <el-tag size="small" :style="{ backgroundColor: getCategoryColor(activity.categoryId), border: 'none', color: '#fff' }">
                  {{ getCategoryName(activity.categoryId) }}
                </el-tag>
                <el-tag size="small" class="ml-2" :type="getStatusTagType(activity.status)">
                  {{ getStatusName(activity.status) }}
                </el-tag>
              </div>
              <h1 class="text-2xl font-bold text-white mb-1">{{ activity.title }}</h1>
            </div>
            
            <div>
              <span class="text-white text-xl font-bold" v-if="activity.cost > 0">¥{{ activity.cost }}</span>
              <span class="text-white text-xl font-bold" v-else>免费</span>
            </div>
          </div>
          
          <!-- 活动基本信息 -->
          <div class="relative flex flex-wrap text-white opacity-90 text-sm gap-4">
            <div class="flex items-center">
              <i class="el-icon-time mr-1.5"></i>
              <span>{{ activity.activityDate }}, {{ activity.startTime }}-{{ activity.endTime }}</span>
            </div>
            <div class="flex items-center">
              <i class="el-icon-location mr-1.5"></i>
              <span>{{ activity.location || '地点待定' }}</span>
            </div>
            <div class="flex items-center">
              <i class="el-icon-user mr-1.5"></i>
              <span>{{ activity.organizer }}</span>
            </div>
          </div>
        </div>
        
        <!-- 报名状态卡片，突出显示在活动头部和内容之间 -->
        <div class="relative px-6 -mt-6 mb-6">
          <div class="bg-white rounded-lg p-4 shadow-md flex justify-between items-center">
            <div class="flex items-center">
              <div class="w-12 h-12 bg-blue-100 rounded-full flex items-center justify-center mr-3">
                <i class="el-icon-user-solid text-xl text-blue-500"></i>
              </div>
              <div>
                <div class="text-gray-500 text-sm mb-1">已报名人数</div>
                <div class="flex items-end">
                  <span class="text-blue-600 text-xl font-bold mr-1">{{ activity.currentParticipants || 0 }}</span>
                  <span class="text-gray-400 text-sm">/ {{ activity.maxParticipants || '不限' }}</span>
                </div>
              </div>
            </div>
            
            <div>
              <el-button 
                v-if="!isUserRegistered"
                type="primary" 
                :disabled="isActivityExpired(activity.registrationDeadline) || activity.status !== 'PLANNED'"
                @click="handleSignUp"
                class="px-6"
                round
              >
                {{ isActivityExpired(activity.registrationDeadline) ? '报名已截止' : '立即报名' }}
              </el-button>
              <el-button 
                v-else
                type="success" 
                @click="handleCancelRegistration"
                class="px-6"
                round
                plain
              >
                已报名
              </el-button>
            </div>
          </div>
        </div>
        
        <!-- 活动内容区 -->
        <div class="px-6 pb-6">
          <!-- 活动标签页 -->
          <el-tabs>
            <el-tab-pane label="活动详情">
              <div class="space-y-6">
                <!-- 封面图展示 -->
                <div v-if="activity.coverImage" class="mb-6 text-center">
                  <el-image 
                    :src="activity.coverImage" 
                    fit="contain" 
                    class="rounded-lg shadow-sm"
                    style="max-width: 80%; max-height: 350px; background-color: #f8f9fa;"
                    :preview-src-list="[activity.coverImage]"
                  >
                  </el-image>
                </div>
                <!-- 活动描述 -->
                <div v-if="activity.description">
                  <h2 class="text-lg font-medium text-gray-800 mb-3 flex items-center">
                    <i class="el-icon-document text-blue-500 mr-2"></i> 活动介绍
                  </h2>
                  <div class="bg-gray-50 rounded-lg p-4 text-gray-700 leading-relaxed whitespace-pre-wrap">
                    {{ activity.description }}
                  </div>
                </div>
                
                <!-- 活动要求 -->
                <div v-if="activity.requirements">
                  <h2 class="text-lg font-medium text-gray-800 mb-3 flex items-center">
                    <i class="el-icon-warning text-orange-500 mr-2"></i> 参与要求
                  </h2>
                  <div class="bg-gray-50 rounded-lg p-4 text-gray-700 leading-relaxed whitespace-pre-wrap">
                    {{ activity.requirements }}
                  </div>
                </div>
                
                <!-- 所需物品 -->
                <div v-if="activity.materialsNeeded">
                  <h2 class="text-lg font-medium text-gray-800 mb-3 flex items-center">
                    <i class="el-icon-suitcase text-green-500 mr-2"></i> 所需物品
                  </h2>
                  <div class="bg-gray-50 rounded-lg p-4 text-gray-700 leading-relaxed whitespace-pre-wrap">
                    {{ activity.materialsNeeded }}
                  </div>
                </div>
                
                <!-- 备注信息 -->
                <div v-if="activity.notes">
                  <h2 class="text-lg font-medium text-gray-800 mb-3 flex items-center">
                    <i class="el-icon-info text-blue-500 mr-2"></i> 备注信息
                  </h2>
                  <div class="bg-gray-50 rounded-lg p-4 text-gray-700 leading-relaxed whitespace-pre-wrap">
                    {{ activity.notes }}
                  </div>
                </div>
              </div>
            </el-tab-pane>
            
            <el-tab-pane label="联系方式">
              <div class="bg-gray-50 rounded-lg p-4 mb-6">
                <h2 class="text-lg font-medium text-gray-800 mb-3 flex items-center">
                  <i class="el-icon-phone text-green-500 mr-2"></i> 联系方式
                </h2>
                
                <div class="space-y-4">
                  <div class="flex items-center">
                    <div class="w-8 h-8 bg-blue-100 rounded-full flex items-center justify-center mr-3">
                      <i class="el-icon-user text-blue-500"></i>
                    </div>
                    <div>
                      <div class="text-gray-500 text-sm">活动组织者</div>
                      <div class="text-gray-800">{{ activity.organizer }}</div>
                    </div>
                  </div>
                  
                  <div class="flex items-center" v-if="activity.organizerPhone">
                    <div class="w-8 h-8 bg-green-100 rounded-full flex items-center justify-center mr-3">
                      <i class="el-icon-phone text-green-500"></i>
                    </div>
                    <div>
                      <div class="text-gray-500 text-sm">联系电话</div>
                      <div class="text-gray-800">{{ activity.organizerPhone }}</div>
                    </div>
                  </div>
                </div>
              </div>
            </el-tab-pane>
            
            <el-tab-pane label="参与指南">
              <div class="bg-gray-50 rounded-lg p-4 mb-6">
                <h2 class="text-lg font-medium text-gray-800 mb-3 flex items-center">
                  <i class="el-icon-guide text-purple-500 mr-2"></i> 参与指南
                </h2>
                
                <div class="space-y-4">
                  <div class="flex">
                    <div class="w-8 h-8 bg-blue-100 rounded-full flex items-center justify-center flex-shrink-0 mr-3">
                      <span class="text-blue-500 font-medium">1</span>
                    </div>
                    <div>
                      <h3 class="text-gray-800 font-medium mb-1">在线报名</h3>
                      <p class="text-gray-600">点击页面上的"立即报名"按钮，填写报名信息，完成在线报名。</p>
                    </div>
                  </div>
                  
                  <div class="flex">
                    <div class="w-8 h-8 bg-blue-100 rounded-full flex items-center justify-center flex-shrink-0 mr-3">
                      <span class="text-blue-500 font-medium">2</span>
                    </div>
                    <div>
                      <h3 class="text-gray-800 font-medium mb-1">收到确认</h3>
                      <p class="text-gray-600">报名成功后，您将收到系统发送的确认信息。</p>
                    </div>
                  </div>
                  
                  <div class="flex">
                    <div class="w-8 h-8 bg-blue-100 rounded-full flex items-center justify-center flex-shrink-0 mr-3">
                      <span class="text-blue-500 font-medium">3</span>
                    </div>
                    <div>
                      <h3 class="text-gray-800 font-medium mb-1">按时参加</h3>
                      <p class="text-gray-600">请提前15分钟到达活动地点，出示报名确认信息参加活动。</p>
                    </div>
                  </div>
                </div>
                
                <div class="mt-6 p-4 bg-orange-50 rounded-lg border border-orange-100">
                  <div class="flex items-start">
                    <i class="el-icon-warning text-orange-500 mr-2 mt-0.5"></i>
                    <div>
                      <h3 class="text-gray-800 font-medium mb-1">温馨提示</h3>
                      <p class="text-gray-600">如需取消报名，请至少提前24小时联系活动组织者。请务必遵守活动规则，尊重他人，共同营造良好的活动氛围。</p>
                    </div>
                  </div>
                </div>
              </div>
            </el-tab-pane>
          </el-tabs>
        </div>
      </div>
      
      <!-- 空状态 -->
      <el-empty 
        v-if="!loading && !activity"
        description="未找到活动信息"
      >
        <el-button type="primary" @click="$router.push('/front/activity')">返回活动列表</el-button>
      </el-empty>

      <!-- 相关活动推荐 -->
      <div v-if="relatedActivities.length > 0" class="mt-8">
        <h2 class="text-xl font-bold text-gray-800 mb-4 flex items-center">
          <i class="el-icon-connection text-blue-500 mr-2"></i> 相关活动推荐
        </h2>
        
        <el-row :gutter="20">
          <el-col 
            v-for="item in relatedActivities" 
            :key="item.id" 
            :xs="24" 
            :sm="12" 
            :md="8" 
            :lg="8" 
            :xl="6" 
            class="mb-5"
          >
            <div 
              class="bg-white rounded-lg overflow-hidden cursor-pointer transition-all duration-300 transform hover:-translate-y-1 border border-gray-200 hover:border-blue-300 shadow-sm hover:shadow-md relative"
              @click="viewActivityDetail(item.id)"
            >
              <div class="h-24 relative p-4 overflow-hidden"
                   :class="getActivityHeaderClass(item.status)">
                <div class="absolute inset-0 opacity-20">
                  <div class="absolute inset-0 bg-pattern"></div>
                </div>
                <div class="relative z-10">
                  <div class="text-white text-lg font-bold mb-1 truncate">{{ item.title }}</div>
                  <div class="flex items-center">
                    <el-tag size="mini" :style="{ backgroundColor: getCategoryColor(item.categoryId), border: 'none', color: '#fff', boxShadow: '0 1px 2px rgba(0,0,0,0.1)' }">
                      {{ getCategoryName(item.categoryId) }}
                    </el-tag>
                    <span class="text-white text-xs ml-2">{{ item.activityDate }}</span>
                  </div>
                </div>
              </div>
              <div class="p-3">
                <div class="flex justify-between items-center text-sm">
                  <div class="text-gray-600">
                    <i class="el-icon-location mr-1"></i>
                    <span class="truncate">{{ item.location || '地点待定' }}</span>
                  </div>
                  <div>
                    <span v-if="item.cost > 0" class="text-orange-500">¥{{ item.cost }}</span>
                    <span v-else class="text-green-500">免费</span>
                  </div>
                </div>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "FrontActivityDetail",
  data() {
    return {
      loading: false,
      activity: null,
      categories: [],
      relatedActivities: [],
      isUserRegistered: false
    };
  },
  created() {
    this.fetchCategories();
    this.fetchActivityDetail();
    this.checkUserRegistration();
  },
  methods: {
    // 获取活动详情
    fetchActivityDetail() {
      const id = this.$route.params.id;
      if (!id) {
        this.$message.error("活动ID不能为空");
        return;
      }
      
      this.loading = true;
      
      this.$request.get(`/activity/selectById/${id}`)
        .then(res => {
          if (res.code === '200') {
            this.activity = res.data;
            this.fetchRelatedActivities();
          } else {
            this.$message.error(res.msg || "获取活动详情失败");
          }
        })
        .catch(() => {
          this.$message.error("获取活动详情异常");
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
    
    // 获取相关活动
    fetchRelatedActivities() {
      if (!this.activity) return;
      
      const params = {
        pageNum: 1,
        pageSize: 4,
        categoryId: this.activity.categoryId
      };
      
      this.$request.get("/activity/selectPage", { params })
        .then(res => {
          if (res.code === '200') {
            // 过滤掉当前活动
            this.relatedActivities = (res.data.list || [])
              .filter(item => item.id !== this.activity.id)
              .slice(0, 4);  // 最多显示4个相关活动
          }
        })
        .catch(() => {
          console.error("获取相关活动失败");
        });
    },
    
    // 检查用户报名状态
    checkUserRegistration() {
      const userStr = localStorage.getItem('xm-user');
      const activityId = this.$route.params.id;
      
      if (!userStr || !activityId) {
        this.isUserRegistered = false;
        return;
      }
      
      const user = JSON.parse(userStr);
      if (!user.id) {
        this.isUserRegistered = false;
        return;
      }
      
      this.$request.get('/activityParticipant/checkRegistration', {
        params: {
          activityId: activityId,
          userId: user.id
        }
      })
        .then(res => {
          if (res.code === '200') {
            this.isUserRegistered = res.data === true;
          } else {
            this.isUserRegistered = false;
          }
        })
        .catch(() => {
          this.isUserRegistered = false;
        });
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
    
    // 获取活动头部背景样式
    getActivityHeaderClass(status) {
      switch (status) {
        case 'PLANNED':
          return 'from-blue-500 to-blue-600';
        case 'ONGOING':
          return 'from-green-500 to-green-600';
        case 'COMPLETED':
          return 'from-gray-500 to-gray-600';
        case 'CANCELLED':
          return 'from-red-500 to-red-600';
        default:
          return 'from-blue-500 to-blue-600';
      }
    },
    
    // 判断活动是否已截止报名
    isActivityExpired(deadline) {
      if (!deadline) return false;
      return new Date(deadline) < new Date();
    },
    
    // 报名活动
    handleSignUp() {
      if (!this.activity) return;
      
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
      
      this.$confirm(`确定要报名参加"${this.activity.title}"活动吗?`, '报名确认', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        // 格式化当前时间为 yyyy-MM-dd HH:mm:ss 格式
        const now = new Date();
        const year = now.getFullYear();
        const month = String(now.getMonth() + 1).padStart(2, '0');
        const day = String(now.getDate()).padStart(2, '0');
        const hours = String(now.getHours()).padStart(2, '0');
        const minutes = String(now.getMinutes()).padStart(2, '0');
        const seconds = String(now.getSeconds()).padStart(2, '0');
        const formattedTime = `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
        
        const registrationData = {
          activityId: this.activity.id,
          userId: user.id,
          registrationTime: formattedTime,
          attendanceStatus: 'REGISTERED'
        };
        
        this.$request.post('/activityParticipant/register', registrationData)
          .then(res => {
            if (res.code === '200') {
              this.$message({
                type: 'success',
                message: res.data || `已成功报名参加"${this.activity.title}"活动`
              });
              // 刷新活动详情以更新参与人数
              this.fetchActivityDetail();
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
    
    // 查看其他活动详情
    viewActivityDetail(id) {
      // 如果是同一个活动，不做处理
      if (this.$route.params.id == id) return;
      
      // 跳转到新的活动详情页
      this.$router.push(`/front/activity/${id}`);
      
      // 重新获取活动详情
      this.fetchActivityDetail();
    }
  }
};
</script>

<style scoped>
.bg-pattern {
  background-image: url("data:image/svg+xml,%3Csvg width='100' height='100' viewBox='0 0 100 100' xmlns='http://www.w3.org/2000/svg'%3E%3Cpath d='M11 18c3.866 0 7-3.134 7-7s-3.134-7-7-7-7 3.134-7 7 3.134 7 7 7zm48 25c3.866 0 7-3.134 7-7s-3.134-7-7-7-7 3.134-7 7 3.134 7 7 7zm-43-7c1.657 0 3-1.343 3-3s-1.343-3-3-3-3 1.343-3 3 1.343 3 3 3zm63 31c1.657 0 3-1.343 3-3s-1.343-3-3-3-3 1.343-3 3 1.343 3 3 3zM34 90c1.657 0 3-1.343 3-3s-1.343-3-3-3-3 1.343-3 3 1.343 3 3 3zm56-76c1.657 0 3-1.343 3-3s-1.343-3-3-3-3 1.343-3 3 1.343 3 3 3zM12 86c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm28-65c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm23-11c2.76 0 5-2.24 5-5s-2.24-5-5-5-5 2.24-5 5 2.24 5 5 5zm-6 60c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm29 22c2.76 0 5-2.24 5-5s-2.24-5-5-5-5 2.24-5 5 2.24 5 5 5zM32 63c2.76 0 5-2.24 5-5s-2.24-5-5-5-5 2.24-5 5 2.24 5 5 5zm57-13c2.76 0 5-2.24 5-5s-2.24-5-5-5-5 2.24-5 5 2.24 5 5 5zm-9-21c1.105 0 2-.895 2-2s-.895-2-2-2-2 .895-2 2 .895 2 2 2zM60 91c1.105 0 2-.895 2-2s-.895-2-2-2-2 .895-2 2 .895 2 2 2zM35 41c1.105 0 2-.895 2-2s-.895-2-2-2-2 .895-2 2 .895 2 2 2zM12 60c1.105 0 2-.895 2-2s-.895-2-2-2-2 .895-2 2 .895 2 2 2z' fill='%23ffffff' fill-opacity='0.1' fill-rule='evenodd'/%3E%3C/svg%3E");
}

.whitespace-pre-wrap {
  white-space: pre-wrap;
}

.truncate {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>