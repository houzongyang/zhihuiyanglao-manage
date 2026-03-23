<template>
  <div class="h-screen w-full flex flex-col bg-gray-50">
    <!-- 顶部导航栏 -->
    <div class="h-16 bg-white border-b border-gray-100 flex justify-between items-center px-5 shadow-sm z-10">
      <div class="flex items-center">
        <div class="flex items-center bg-blue-50 text-blue-500 p-1.5 rounded-lg mr-3">
          <img src="/img/logo.svg" alt="Logo" class="w-7 h-7">
        </div>
        <span class="text-lg font-bold text-gray-800">智慧养老管理系统</span>
      </div>
      <div class="flex items-center space-x-4">
        <div class="hidden md:flex items-center space-x-3 text-gray-500">
          <div class="flex items-center px-3 py-1.5 rounded-full bg-gray-50 text-sm border border-gray-100">
            <i class="el-icon-date mr-1"></i>
            <span>{{ currentDate }}</span>
          </div>
        </div>
        <el-dropdown trigger="click" @command="handleCommand">
          <span class="flex items-center cursor-pointer px-3 py-2 rounded-full hover:bg-gray-50 transition-colors">
            <el-avatar :size="32" :src="avatar" class="mr-2 border-2 border-gray-100"></el-avatar>
            <span class="text-gray-700 mr-1 font-medium">{{ username }}</span>
            <i class="el-icon-caret-bottom text-gray-400"></i>
          </span>
          <el-dropdown-menu slot="dropdown" class="user-dropdown">
            <el-dropdown-item command="person">
              <i class="el-icon-user mr-2 text-blue-400"></i> 个人中心
            </el-dropdown-item>
            <el-dropdown-item divided command="logout">
              <i class="el-icon-switch-button mr-2 text-red-400"></i> 退出登录
            </el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </div>
    
    <div class="flex flex-1 overflow-hidden">
      <!-- 左侧菜单栏 -->
      <div class="bg-white border-r border-gray-100 transition-all duration-300 relative z-10 shadow-sm"
           :class="isCollapse ? 'w-16' : 'w-56'">
        <div class="h-12 flex items-center justify-center border-b border-gray-100">
          <div @click="toggleCollapse" class="w-full text-center text-gray-400 text-lg cursor-pointer hover:text-blue-400 transition-colors">
            <i :class="isCollapse ? 'el-icon-s-unfold' : 'el-icon-s-fold'"></i>
          </div>
        </div>
        
        <el-scrollbar class="h-[calc(100%-48px)]">
          <el-menu
            :default-active="activeMenu"
            class="border-r-0"
            :collapse="isCollapse"
            :collapse-transition="false"
            background-color="#ffffff"
            text-color="#606266"
            active-text-color="#409EFF"
            :unique-opened="true"
            router
          >
            <!-- 首页 - 护士和管理员都可见，但路径不同 -->
            <el-menu-item v-if="userRole === 'NURSE'" index="/nurse/home" class="menu-item">
              <i class="el-icon-s-home"></i>
              <span slot="title">首页</span>
            </el-menu-item>
            <el-menu-item v-if="userRole === 'ADMIN'" index="/manager/home" class="menu-item">
              <i class="el-icon-s-home"></i>
              <span slot="title">首页</span>
            </el-menu-item>
            

            
            <!-- 系统管理 - 仅管理员可见 -->
            <el-submenu v-if="userRole === 'ADMIN'" index="system" class="menu-submenu">
              <template slot="title">
                <i class="el-icon-setting"></i>
                <span>系统管理</span>
              </template>
              <el-menu-item index="/manager/user" class="submenu-item">
                <i class="el-icon-user"></i>
                <span>用户管理</span>
              </el-menu-item>
              <el-menu-item index="/manager/notice" class="submenu-item">
                <i class="el-icon-bell"></i>
                <span>公告管理</span>
              </el-menu-item>
              <el-menu-item index="/manager/nurse" class="submenu-item">
                <i class="el-icon-user-solid"></i>
                <span>护士管理</span>
              </el-menu-item>
            </el-submenu>

            <!-- 留言板管理 - 护士和管理员都可见，但护士访问nurse路径 -->
            <el-menu-item v-if="userRole === 'NURSE'" index="/nurse/message-board" class="menu-item">
              <i class="el-icon-message-solid"></i>
              <span slot="title">留言板管理</span>
            </el-menu-item>
            <el-menu-item v-if="userRole === 'ADMIN'" index="/manager/message-board" class="menu-item">
              <i class="el-icon-message-solid"></i>
              <span slot="title">留言板管理</span>
            </el-menu-item>

            <!-- 护士管理 - 护士和管理员都可见，但护士访问nurse路径 -->
            <el-menu-item v-if="userRole === 'NURSE'" index="/nurse/nurse" class="menu-item">
              <i class="el-icon-user-solid"></i>
              <span slot="title">护士管理</span>
            </el-menu-item>
            
            <!-- 健康管理 - 护士和管理员都可见，但路径不同 -->
            <el-submenu index="health" class="menu-submenu">
              <template slot="title">
                <i class="el-icon-first-aid-kit"></i>
                <span>健康管理</span>
              </template>
              <template v-if="userRole === 'NURSE'">
                <el-menu-item index="/nurse/health-record" class="submenu-item">
                  <i class="el-icon-document-checked"></i>
                  <span>健康档案管理</span>
                </el-menu-item>
                <el-menu-item index="/nurse/health-monitoring" class="submenu-item">
                  <i class="el-icon-view"></i>
                  <span>健康监控管理</span>
                </el-menu-item>
                <el-menu-item index="/nurse/user-medication" class="submenu-item">
                  <i class="el-icon-sugar"></i>
                  <span>药物管理</span>
                </el-menu-item>
              </template>
              <template v-if="userRole === 'ADMIN'">
                <el-menu-item index="/manager/health-record" class="submenu-item">
                  <i class="el-icon-document-checked"></i>
                  <span>健康档案管理</span>
                </el-menu-item>
                <el-menu-item index="/manager/health-monitoring" class="submenu-item">
                  <i class="el-icon-view"></i>
                  <span>健康监控管理</span>
                </el-menu-item>
                <el-menu-item index="/manager/user-medication" class="submenu-item">
                  <i class="el-icon-sugar"></i>
                  <span>药物管理</span>
                </el-menu-item>
              </template>
            </el-submenu>
            
            <!-- 住宿管理 - 护士和管理员都可见，但路径不同 -->
            <el-submenu index="accommodation" class="menu-submenu">
              <template slot="title">
                <i class="el-icon-house"></i>
                <span>住宿管理</span>
              </template>
              <el-menu-item v-if="userRole === 'NURSE'" index="/nurse/checkin-record" class="submenu-item">
                <i class="el-icon-office-building"></i>
                <span>入住信息管理</span>
              </el-menu-item>
              <el-menu-item v-if="userRole === 'ADMIN'" index="/manager/checkin-record" class="submenu-item">
                <i class="el-icon-office-building"></i>
                <span>入住信息管理</span>
              </el-menu-item>
            </el-submenu>
            
            <!-- 活动管理 - 仅管理员可见 -->
            <el-submenu v-if="userRole === 'ADMIN'" index="activity" class="menu-submenu">
              <template slot="title">
                <i class="el-icon-date"></i>
                <span>活动管理</span>
              </template>
              <el-menu-item index="/manager/activity-category" class="submenu-item">
                <i class="el-icon-collection-tag"></i>
                <span>活动分类管理</span>
              </el-menu-item>
              <el-menu-item index="/manager/activity" class="submenu-item">
                <i class="el-icon-files"></i>
                <span>活动信息管理</span>
              </el-menu-item>
              <el-menu-item index="/manager/activity-participant" class="submenu-item">
                <i class="el-icon-tickets"></i>
                <span>活动报名管理</span>
              </el-menu-item>
            </el-submenu>
            
            <!-- 护理管理 - 护士和管理员都可见，但路径不同 -->
            <el-submenu index="care" class="menu-submenu">
              <template slot="title">
                <i class="el-icon-s-help"></i>
                <span>护理管理</span>
              </template>
              <template v-if="userRole === 'NURSE'">
                <el-menu-item index="/nurse/care-plan" class="submenu-item">
                  <i class="el-icon-s-order"></i>
                  <span>护理计划管理</span>
                </el-menu-item>
                <el-menu-item index="/nurse/care-assignment" class="submenu-item">
                  <i class="el-icon-coordinate"></i>
                  <span>护理安排管理</span>
                </el-menu-item>
                <el-menu-item index="/nurse/care-record" class="submenu-item">
                  <i class="el-icon-notebook-2"></i>
                  <span>护理记录管理</span>
                </el-menu-item>
              </template>
              <template v-if="userRole === 'ADMIN'">
                <el-menu-item index="/manager/care-plan" class="submenu-item">
                  <i class="el-icon-s-order"></i>
                  <span>护理计划管理</span>
                </el-menu-item>
                <el-menu-item index="/manager/care-assignment" class="submenu-item">
                  <i class="el-icon-coordinate"></i>
                  <span>护理安排管理</span>
                </el-menu-item>
                <el-menu-item index="/manager/care-record" class="submenu-item">
                  <i class="el-icon-notebook-2"></i>
                  <span>护理记录管理</span>
                </el-menu-item>
              </template>
            </el-submenu>
            
            <!-- 费用管理 - 仅管理员可见 -->
            <el-submenu v-if="userRole === 'ADMIN'" index="finance" class="menu-submenu">
              <template slot="title">
                <i class="el-icon-wallet"></i>
                <span>费用管理</span>
              </template>
              <el-menu-item index="/manager/bill-management" class="submenu-item">
                <i class="el-icon-coin"></i>
                <span>费用管理</span>
              </el-menu-item>
            </el-submenu>
            
            <!-- 个人中心 - 护士和管理员都可见，但路径不同 -->
            <el-menu-item v-if="userRole === 'NURSE'" index="/nurse/person" class="menu-item">
              <i class="el-icon-user-solid"></i>
              <span slot="title">个人中心</span>
            </el-menu-item>
            <el-menu-item v-if="userRole === 'ADMIN'" index="/manager/person" class="menu-item">
              <i class="el-icon-user-solid"></i>
              <span slot="title">个人中心</span>
            </el-menu-item>
          </el-menu>
        </el-scrollbar>
      </div>
      
      <!-- 内容区域 -->
      <div class="flex-1 overflow-auto bg-gray-50 p-5 transition-all duration-300">
        <div class="flex items-center mb-5 px-4 py-3 bg-white rounded-lg shadow-sm">
          <i class="el-icon-location text-blue-400 mr-2"></i>
          <el-breadcrumb separator="/">
            <el-breadcrumb-item v-if="userRole === 'NURSE'" :to="{ path: '/nurse/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item v-if="userRole === 'ADMIN'" :to="{ path: '/manager/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item v-if="$route.meta && $route.meta.name">{{ $route.meta.name }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        
        <div class="min-h-[calc(100vh-180px)] bg-white rounded-lg shadow-sm p-5">
          <router-view />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "ManagerLayout",
  data() {
    return {
      isCollapse: false,
      avatar: "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png",
      username: "管理员",
      currentDate: '',
      userRole: 'ADMIN' // 用户角色
    };
  },
  computed: {
    activeMenu() {
      return this.$route.path;
    }
  },
  created() {
    // 检查登录状态和权限
    this.checkPermission();
    
    // 获取用户信息
    this.getUserInfo();
    
    // 设置当前日期
    this.setCurrentDate();
  },
  methods: {
    toggleCollapse() {
      this.isCollapse = !this.isCollapse;
    },
    handleCommand(command) {
      if (command === "person") {
        // 根据用户角色跳转到不同的个人中心页面
        if (this.userRole === 'NURSE') {
          this.$router.push("/nurse/person");
        } else {
          this.$router.push("/manager/person");
        }
      } else if (command === "logout") {
        localStorage.removeItem("xm-user");
        this.$router.push("/login");
      }
    },
    checkPermission() {
      const user = JSON.parse(localStorage.getItem("xm-user") || "{}");
      if (!user.token) {
        this.$router.push("/login");
        return;
      }
      
      if (user.role !== "ADMIN" && user.role !== "NURSE") {
        this.$message.error("您没有访问权限");
        this.$router.push("/login");
      }
    },
    getUserInfo() {
      // 从本地存储获取用户信息
      const user = JSON.parse(localStorage.getItem("xm-user") || "{}");
      if (user.name) {
        this.username = user.name;
      }
      if (user.avatar) {
        this.avatar = user.avatar;
      }
      if (user.role) {
        this.userRole = user.role;
        // 根据角色设置默认用户名
        if (!user.name) {
          this.username = user.role === 'NURSE' ? '护士' : '管理员';
        }
      }
    },
    setCurrentDate() {
      const now = new Date();
      const year = now.getFullYear();
      const month = String(now.getMonth() + 1).padStart(2, '0');
      const day = String(now.getDate()).padStart(2, '0');
      const weekdays = ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'];
      const weekday = weekdays[now.getDay()];
      this.currentDate = `${year}-${month}-${day} ${weekday}`;
    }
  }
};
</script>

<style scoped>
/* 覆盖element-ui菜单样式 */
:deep(.el-menu) {
  border-right: none !important;
}

:deep(.el-menu-item.is-active) {
  background-color: #e6f7ff !important;
  border-right: 3px solid #1890ff;
  color: #1890ff !important;
}

:deep(.el-menu-item.is-active i) {
  color: #1890ff !important;
}

:deep(.el-menu-item), :deep(.el-submenu__title) {
  height: 50px;
  line-height: 50px;
  margin: 4px 0;
  border-radius: 4px;
  margin-right: 12px;
  color: #606266;
}

:deep(.el-menu-item:hover), :deep(.el-submenu__title:hover) {
  background-color: #f0f9ff !important;
  color: #1890ff !important;
}

:deep(.el-menu-item i), :deep(.el-submenu__title i) {
  color: #909399;
  font-size: 18px;
  margin-right: 8px;
}

:deep(.el-menu-item:hover i), :deep(.el-submenu__title:hover i) {
  color: #1890ff !important;
}

:deep(.el-menu--inline) {
  background-color: #fafafa !important;
  border-radius: 4px;
  margin: 0 12px 8px 12px;
}

:deep(.el-submenu.is-opened > .el-submenu__title) {
  color: #1890ff !important;
}

:deep(.el-submenu.is-opened > .el-submenu__title i) {
  color: #1890ff !important;
}

:deep(.user-dropdown .el-dropdown-menu__item:hover) {
  background-color: #f0f9ff;
}

/* 内容区域添加浅色背景 */
.content-box {
  background-color: #ffffff;
}
</style>