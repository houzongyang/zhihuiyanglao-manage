<template>
  <div class="min-h-screen flex flex-col bg-gray-50">
    <!-- 顶部导航栏 -->
    <div class="h-16 bg-white shadow-sm flex justify-between items-center px-6 sticky top-0 z-50">
      <div class="flex items-center">
        <div class="flex items-center mr-12">
          <img src="/img/logo.svg" alt="logo" class="h-9 w-9 mr-3 rounded-lg object-contain" />
          <div class="text-lg font-semibold text-blue-500">智慧养老管理系统</div>
        </div>
        
        <div class="flex h-full">
          <div
            class="flex items-center px-5 h-16 text-gray-600 cursor-pointer transition-all duration-300 hover:bg-gray-50 hover:text-blue-500 relative"
            :class="{ 'text-blue-500 font-medium': $route.path === '/front/home' }"
            @click="$router.push('/front/home')"
          >
            <i class="el-icon-s-home mr-1.5"></i>
            <span>首页</span>
            <div v-if="$route.path === '/front/home'" class="absolute bottom-0 left-1/5 w-3/5 h-0.5 bg-blue-500 rounded-t"></div>
          </div>
          
          <div
            class="flex items-center px-5 h-16 text-gray-600 cursor-pointer transition-all duration-300 hover:bg-gray-50 hover:text-blue-500 relative"
            :class="{ 'text-blue-500 font-medium': $route.path === '/front/checkin-record' }"
            @click="$router.push('/front/checkin-record')"
          >
            <i class="el-icon-house mr-1.5"></i>
            <span>入住信息</span>
            <div v-if="$route.path === '/front/checkin-record'" class="absolute bottom-0 left-1/5 w-3/5 h-0.5 bg-blue-500 rounded-t"></div>
          </div>
          
          <div
            class="flex items-center px-5 h-16 text-gray-600 cursor-pointer transition-all duration-300 hover:bg-gray-50 hover:text-blue-500 relative"
            :class="{ 'text-blue-500 font-medium': $route.path.includes('/front/notice') }"
            @click="$router.push('/front/notice')"
          >
            <i class="el-icon-bell mr-1.5"></i>
            <span>通知公告</span>
            <div v-if="$route.path.includes('/front/notice')" class="absolute bottom-0 left-1/5 w-3/5 h-0.5 bg-blue-500 rounded-t"></div>
          </div>
          
          <div
            class="flex items-center px-5 h-16 text-gray-600 cursor-pointer transition-all duration-300 hover:bg-gray-50 hover:text-blue-500 relative"
            :class="{ 'text-blue-500 font-medium': $route.path === '/front/health-record' }"
            @click="$router.push('/front/health-record')"
          >
            <i class="el-icon-document-checked mr-1.5"></i>
            <span>健康档案</span>
            <div v-if="$route.path === '/front/health-record'" class="absolute bottom-0 left-1/5 w-3/5 h-0.5 bg-blue-500 rounded-t"></div>
          </div>
          
          <div
            class="flex items-center px-5 h-16 text-gray-600 cursor-pointer transition-all duration-300 hover:bg-gray-50 hover:text-blue-500 relative"
            :class="{ 'text-blue-500 font-medium': $route.path === '/front/health-monitoring' }"
            @click="$router.push('/front/health-monitoring')"
          >
            <i class="el-icon-data-analysis mr-1.5"></i>
            <span>健康监控</span>
            <div v-if="$route.path === '/front/health-monitoring'" class="absolute bottom-0 left-1/5 w-3/5 h-0.5 bg-blue-500 rounded-t"></div>
          </div>
          
          <div
            class="flex items-center px-5 h-16 text-gray-600 cursor-pointer transition-all duration-300 hover:bg-gray-50 hover:text-blue-500 relative"
            :class="{ 'text-blue-500 font-medium': $route.path === '/front/medication' }"
            @click="$router.push('/front/medication')"
          >
            <i class="el-icon-medicine mr-1.5"></i>
            <span>药物管理</span>
            <div v-if="$route.path === '/front/medication'" class="absolute bottom-0 left-1/5 w-3/5 h-0.5 bg-blue-500 rounded-t"></div>
          </div>
          
          <div
            class="flex items-center px-5 h-16 text-gray-600 cursor-pointer transition-all duration-300 hover:bg-gray-50 hover:text-blue-500 relative"
            :class="{ 'text-blue-500 font-medium': $route.path === '/front/activity' }"
            @click="$router.push('/front/activity')"
          >
            <i class="el-icon-date mr-1.5"></i>
            <span>活动中心</span>
            <div v-if="$route.path === '/front/activity'" class="absolute bottom-0 left-1/5 w-3/5 h-0.5 bg-blue-500 rounded-t"></div>
          </div>
          
          <div
            class="flex items-center px-5 h-16 text-gray-600 cursor-pointer transition-all duration-300 hover:bg-gray-50 hover:text-blue-500 relative"
            :class="{ 'text-blue-500 font-medium': $route.path === '/front/care-record' }"
            @click="$router.push('/front/care-record')"
          >
            <i class="el-icon-notebook-2 mr-1.5"></i>
            <span>护理记录</span>
            <div v-if="$route.path === '/front/care-record'" class="absolute bottom-0 left-1/5 w-3/5 h-0.5 bg-blue-500 rounded-t"></div>
          </div>

          <div
            class="flex items-center px-5 h-16 text-gray-600 cursor-pointer transition-all duration-300 hover:bg-gray-50 hover:text-blue-500 relative"
            :class="{ 'text-blue-500 font-medium': $route.path === '/front/nurse' }"
            @click="$router.push('/front/nurse')"
          >
            <i class="el-icon-user-solid mr-1.5"></i>
            <span>护理团队</span>
            <div v-if="$route.path === '/front/nurse'" class="absolute bottom-0 left-1/5 w-3/5 h-0.5 bg-blue-500 rounded-t"></div>
          </div>

          <div
            class="flex items-center px-5 h-16 text-gray-600 cursor-pointer transition-all duration-300 hover:bg-gray-50 hover:text-blue-500 relative"
            :class="{ 'text-blue-500 font-medium': $route.path === '/front/chat' }"
            @click="$router.push('/front/chat')"
          >
            <i class="el-icon-chat-dot-round mr-1.5"></i>
            <span>AI助手</span>
            <div v-if="$route.path === '/front/chat'" class="absolute bottom-0 left-1/5 w-3/5 h-0.5 bg-blue-500 rounded-t"></div>
          </div>
        </div>
      </div>
      
      <div class="flex items-center">
        <div class="flex mr-4">
          <div class="w-10 h-10 flex items-center justify-center text-gray-500 rounded-full hover:bg-gray-100 cursor-pointer transition-colors duration-200" 
               @click="toggleFullScreen" title="全屏">
            <i class="el-icon-full-screen"></i>
          </div>
          
          <div class="w-10 h-10 flex items-center justify-center text-gray-500 rounded-full hover:bg-gray-100 cursor-pointer transition-colors duration-200" 
               @click="refreshPage" title="刷新">
            <i class="el-icon-refresh"></i>
          </div>
          
          <div class="w-10 h-10 flex items-center justify-center text-gray-500 rounded-full hover:bg-gray-100 cursor-pointer transition-colors duration-200" 
               @click="showMessages" title="健康提醒">
            <el-badge :value="unreadCount" :max="99" :hidden="unreadCount === 0" class="flex">
              <i class="el-icon-message"></i>
            </el-badge>
          </div>
        </div>
        
        <el-dropdown trigger="click" placement="bottom">
          <div class="flex items-center py-1.5 px-3 rounded-full cursor-pointer hover:bg-gray-100 transition-colors duration-200">
            <img
              :src="
                user.avatar ||
                'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'
              "
              class="w-9 h-9 rounded-full object-cover border border-gray-100 shadow"
            />
            <div class="ml-2 flex items-center">
              <span class="text-sm font-medium text-gray-700 max-w-[100px] truncate">{{ user.name || "同学" }}</span>
              <i class="el-icon-arrow-down text-xs text-gray-400 ml-1.5"></i>
            </div>
          </div>
          <el-dropdown-menu slot="dropdown" class="user-dropdown">
            <el-dropdown-item @click.native="$router.push('/front/person')">
              <i class="el-icon-user text-blue-500 mr-2"></i> 个人中心
            </el-dropdown-item>
            <el-dropdown-item @click.native="$router.push('/front/my-activities')">
              <i class="el-icon-date text-blue-500 mr-2"></i> 我的活动
            </el-dropdown-item>
            <el-dropdown-item @click.native="$router.push('/front/payment')">
              <i class="el-icon-wallet text-blue-500 mr-2"></i> 费用支付
            </el-dropdown-item>
            <el-dropdown-item @click.native="$router.push('/front/message-board')">
              <i class="el-icon-chat-dot-round text-blue-500 mr-2"></i> 留言板
            </el-dropdown-item>
            <el-dropdown-item @click.native="$router.push('/front/settings')">
              <i class="el-icon-setting text-blue-500 mr-2"></i> 设置
            </el-dropdown-item>
            <el-dropdown-item divided @click.native="logout">
              <i class="el-icon-switch-button text-red-500 mr-2"></i> 退出登录
            </el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </div>

    <!-- 面包屑导航 -->
    <div class="bg-white rounded-lg shadow-sm mx-4 mt-4 mb-0 p-4 flex justify-between items-center">
      <div>
        <el-breadcrumb separator="/">
          <el-breadcrumb-item :to="{ path: '/front/home' }">首页</el-breadcrumb-item>
          <template v-if="$route.name === 'FrontNoticeDetail'">
            <el-breadcrumb-item :to="{ path: '/front/notice' }">通知公告</el-breadcrumb-item>
            <el-breadcrumb-item :to="{ path: $route.path }">公告详情</el-breadcrumb-item>
          </template>
          <el-breadcrumb-item v-else :to="{ path: $route.path }">{{
            $route.meta.name || '当前页面'
          }}</el-breadcrumb-item>
        </el-breadcrumb>
      </div>
      <div class="flex items-center text-lg font-medium text-gray-800">
        <i :class="[getPageIcon(), 'text-blue-500 mr-2']"></i>
        <span>{{ $route.name === 'FrontNoticeDetail' ? '公告详情' : ($route.meta.name || '当前页面') }}</span>
      </div>
    </div>

    <!-- 内容区域 -->
    <div class="flex-1 overflow-auto px-4 pb-4">
      <transition name="fade-transform" mode="out-in">
        <div class="bg-white rounded-lg shadow-sm p-6 mt-4">
          <router-view class="page-container"></router-view>
        </div>
      </transition>
    </div>

    <!-- 页脚 -->
    <div class="bg-white border-t border-gray-100 py-4 px-6 mt-4">
      <div class="text-center">
        <div class="mb-2">
          <a href="#" class="text-gray-600 text-sm hover:text-blue-500 transition-colors">关于我们</a>
          <span class="mx-3 text-gray-300">|</span>
          <a href="#" class="text-gray-600 text-sm hover:text-blue-500 transition-colors">帮助中心</a>
          <span class="mx-3 text-gray-300">|</span>
          <a href="#" class="text-gray-600 text-sm hover:text-blue-500 transition-colors">联系我们</a>
        </div>
        <div class="text-xs text-gray-400">
          © 2023 智慧养老管理系统 版权所有
        </div>
      </div>
    </div>

    <!-- 消息抽屉 -->
    <el-drawer
      title="健康提醒消息"
      :visible.sync="messagesVisible"
      direction="rtl"
      size="450px"
      :modal-append-to-body="false"
    >
      <div class="h-full flex flex-col">
        <!-- 消息列表 -->
        <div class="flex-1 overflow-y-auto px-4" v-loading="loading">
          <div v-if="messages.length === 0 && !loading" class="text-center text-gray-400 py-12">
            <i class="el-icon-chat-dot-round text-4xl mb-4"></i>
            <div>暂无消息</div>
          </div>
          
          <div v-for="message in messages" :key="message.id" class="message-item mb-4 p-4 rounded-lg border cursor-pointer transition-all duration-200 hover:shadow-md"
               :class="[message.isRead ? 'bg-gray-50 border-gray-200' : 'bg-blue-50 border-blue-200', getPriorityClass(message.priority)]"
               @click="markAsRead(message)">
            <!-- 消息头部 -->
            <div class="flex items-center justify-between mb-2">
              <div class="flex items-center">
                <el-tag :type="message.reminderType === 'EMERGENCY' ? 'danger' : (message.reminderType === 'HEALTH_ALERT' ? 'warning' : 'info')" size="mini" class="mr-2">
                  {{ getReminderTypeLabel(message.reminderType) }}
                </el-tag>
                <el-tag v-if="message.priority === 'HIGH'" type="danger" size="mini" effect="dark">
                  高优先级
                </el-tag>
                <el-tag v-else-if="message.priority === 'NORMAL'" type="warning" size="mini">
                  普通
                </el-tag>
                <el-tag v-else type="info" size="mini">
                  低优先级
                </el-tag>
              </div>
              <div class="text-xs text-gray-400">
                {{ formatTime(message.createTime) }}
              </div>
            </div>
            
            <!-- 消息内容 -->
            <div class="text-gray-700 text-sm mb-2 line-clamp-3">
              {{ message.content }}
            </div>
            
            <!-- 消息底部 -->
            <div class="flex items-center justify-between text-xs text-gray-500">
              <div class="flex items-center">
                <i class="el-icon-user mr-1"></i>
                <span>{{ message.senderName }}</span>
              </div>
              <div v-if="!message.isRead" class="flex items-center text-blue-500">
                <i class="el-icon-chat-dot-round mr-1"></i>
                <span>未读</span>
              </div>
              <div v-else class="flex items-center text-gray-400">
                <i class="el-icon-circle-check mr-1"></i>
                <span>已读</span>
              </div>
            </div>
          </div>
        </div>
        
        <!-- 底部操作 -->
        <div class="border-t border-gray-200 p-4">
          <el-button type="primary" size="small" @click="getMessages" icon="el-icon-refresh" class="w-full">
            刷新消息
          </el-button>
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import request from '@/utils/request'

export default {
  name: "FrontLayout",
  data() {
    return {
      user: JSON.parse(localStorage.getItem("xm-user") || "{}"),
      unreadCount: 0,
      messagesVisible: false,
      messages: [],
      loading: false
    };
  },
  created() {
    this.checkPermission();
    this.getUnreadCount();
    // 每30秒刷新一次未读消息数量
    this.messageTimer = setInterval(() => {
      this.getUnreadCount();
    }, 30000);
  },
  beforeDestroy() {
    if (this.messageTimer) {
      clearInterval(this.messageTimer);
    }
  },
  methods: {
    // 检查权限
    checkPermission() {
      const user = this.user;
      if (!user || !user.token) {
        this.$message.error('请先登录');
        this.$router.push('/login');
        return;
      }
      
      if (user.role !== 'USER') {
        this.$message.error('您没有学生用户权限');
        // 根据角色跳转到对应页面
        if (user.role === 'ADMIN') {
          this.$router.push('/manager/home');
        } else if (user.role === 'TEACHER') {
          this.$router.push('/teacher/home');
        } else {
          this.$router.push('/login');
        }
      }
    },
    
    // 获取当前页面图标
    getPageIcon() {
      const path = this.$route.path;
      if (path === '/front/home') return 'el-icon-s-home';
      if (path === '/front/person') return 'el-icon-user';
      if (path === '/front/checkin-record') return 'el-icon-house';
      if (path.includes('/front/textbook') || path.includes('/front/textbooks')) return 'el-icon-notebook-2';
      if (path === '/front/textbook-application') return 'el-icon-document-checked';
      if (path.includes('/front/notice')) return 'el-icon-bell';
      if (path === '/front/health-record') return 'el-icon-document-checked';
      if (path === '/front/health-monitoring') return 'el-icon-data-analysis';
      if (path === '/front/medication') return 'el-icon-medicine';
      if (path.includes('/front/activity')) return 'el-icon-date';
      if (path === '/front/care-record') return 'el-icon-notebook-2';
      if (path === '/front/nurse') return 'el-icon-user-solid';
      if (path === '/front/chat') return 'el-icon-chat-dot-round';
      if (path === '/front/message-board') return 'el-icon-chat-dot-round';
      return 'el-icon-document';
    },
    
    // 全屏切换
    toggleFullScreen() {
      if (!document.fullscreenElement) {
        document.documentElement.requestFullscreen();
      } else {
        if (document.exitFullscreen) {
          document.exitFullscreen();
        }
      }
    },
    
    // 刷新页面
    refreshPage() {
      location.reload();
    },
    
    // 获取未读消息数量
    getUnreadCount() {
      const user = this.user;
      if (!user || !user.role) return;
      
      // 根据用户角色获取对应的消息类型
      let userType = 'USER';
      if (user.role === 'ADMIN') {
        userType = 'ADMIN';
      } else if (user.role === 'TEACHER') {
        userType = 'MEDICAL_STAFF';
      }
      
      request.get('/health-reminder/getUnreadCount', {
        params: { userType }
      }).then(res => {
        if (res.code === '200') {
          this.unreadCount = res.data || 0;
        }
      }).catch(() => {
        // 静默处理错误
      });
    },
    
    // 显示消息列表
    showMessages() {
      this.messagesVisible = true;
      this.getMessages();
    },
    
    // 获取消息列表
    getMessages() {
      this.loading = true;
      const user = this.user;
      
      let receiverType = 'USER';
      if (user.role === 'ADMIN') {
        receiverType = 'ADMIN';
      } else if (user.role === 'TEACHER') {
        receiverType = 'MEDICAL_STAFF';
      }
      
      request.get('/health-reminder/selectByReceiverInfo', {
        params: { 
          receiverType,
          receiverId: user.id 
        }
      }).then(res => {
        if (res.code === '200') {
          this.messages = res.data || [];
        } else {
          this.$message.error('获取消息失败');
        }
      }).catch(() => {
        this.$message.error('获取消息异常');
      }).finally(() => {
        this.loading = false;
      });
    },
    
    // 标记消息为已读
    markAsRead(message) {
      if (message.isRead === 1) return;
      
      request.put(`/health-reminder/markAsRead/${message.id}`)
        .then(res => {
          if (res.code === '200') {
            message.isRead = 1;
            message.readTime = new Date().toISOString();
            this.getUnreadCount(); // 刷新未读数量
          }
        });
    },
    
    // 获取优先级样式
    getPriorityClass(priority) {
      const classMap = {
        'HIGH': 'priority-high',
        'NORMAL': 'priority-normal', 
        'LOW': 'priority-low'
      };
      return classMap[priority] || 'priority-normal';
    },
    
    // 获取提醒类型标签
    getReminderTypeLabel(type) {
      const labelMap = {
        'HEALTH_ALERT': '健康警报',
        'MEDICATION': '用药提醒',
        'CHECK_UP': '体检提醒',
        'EMERGENCY': '紧急情况'
      };
      return labelMap[type] || '未知类型';
    },
    
    // 格式化时间
    formatTime(timeStr) {
      if (!timeStr) return '';
      const date = new Date(timeStr);
      const now = new Date();
      const diff = now - date;
      
      if (diff < 60000) return '刚刚';
      if (diff < 3600000) return `${Math.floor(diff / 60000)}分钟前`;
      if (diff < 86400000) return `${Math.floor(diff / 3600000)}小时前`;
      if (diff < 604800000) return `${Math.floor(diff / 86400000)}天前`;
      
      return date.toLocaleDateString();
    },
    
    // 退出登录
    logout() {
      this.$confirm('确定要退出登录吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        roundButton: true,
      }).then(() => {
        localStorage.removeItem("xm-user");
        this.$router.push("/login");
        this.$message({
          type: 'success',
          message: '您已成功退出登录'
        });
      }).catch(() => {});
    }
  }
};
</script>

<style scoped>
/* Tailwind CSS已覆盖所有样式，只保留必要的transition样式 */
.fade-transform-enter-active,
.fade-transform-leave-active {
  transition: all 0.3s;
}

.fade-transform-enter {
  opacity: 0;
  transform: translateX(20px);
}

.fade-transform-leave-to {
  opacity: 0;
  transform: translateX(-20px);
}

/* Element UI的深度选择器样式覆盖 */
:deep(.user-dropdown) {
  @apply rounded-lg shadow-lg;
  padding: 6px 0;
}

/* 消息相关样式 */
.message-item {
  border-left: 4px solid transparent;
}

.priority-high {
  border-left-color: #f56c6c !important;
}

.priority-normal {
  border-left-color: #e6a23c !important;
}

.priority-low {
  border-left-color: #67c23a !important;
}

.line-clamp-3 {
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

/* 抽屉样式优化 */
:deep(.el-drawer__header) {
  @apply bg-blue-500 text-white py-4 px-6 mb-0;
}

:deep(.el-drawer__title) {
  @apply text-white font-medium;
}

:deep(.el-drawer__close-btn) {
  @apply text-white;
}

:deep(.el-drawer__body) {
  @apply p-0;
}
</style>