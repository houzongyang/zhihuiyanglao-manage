<template>
  <div class="layout-container">
    <!-- 顶部导航栏 -->
    <div class="layout-header">
      <div class="logo-container">
        <img src="/img/logo.png" alt="Logo" class="logo-image">
        <span class="logo-text"></span>
      </div>
      <div class="header-right">
        <el-dropdown trigger="click" @command="handleCommand">
          <span class="dropdown-link">
            <el-avatar size="small" :src="avatar" class="user-avatar"></el-avatar>
            <span class="username">{{ username }}</span>
            <i class="el-icon-caret-bottom"></i>
          </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="person">
              <i class="el-icon-user"></i> 个人中心
            </el-dropdown-item>
            <el-dropdown-item command="logout">
              <i class="el-icon-switch-button"></i> 退出登录
            </el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </div>
    
    <div class="layout-main">
      <!-- 左侧菜单栏 -->
      <div class="layout-sidebar" :class="{ 'is-collapsed': isCollapse }">
        <div class="sidebar-top">
          <el-tooltip effect="dark" :content="isCollapse ? '展开菜单' : '收起菜单'" placement="right">
            <div class="collapse-btn" @click="toggleCollapse">
              <i :class="isCollapse ? 'el-icon-s-unfold' : 'el-icon-s-fold'"></i>
            </div>
          </el-tooltip>
        </div>
        
        <el-scrollbar class="sidebar-scrollbar">
          <el-menu
            :default-active="activeMenu"
            class="sidebar-menu"
            :collapse="isCollapse"
            :collapse-transition="false"
            background-color="#002140"
            text-color="#bfcbd9"
            active-text-color="#1890ff"
            :unique-opened="true"
            router
          >
            <el-menu-item index="/teacher/home">
              <i class="el-icon-s-home"></i>
              <span slot="title">首页</span>
            </el-menu-item>
            
            <el-submenu index="textbook">
              <template slot="title">
                <i class="el-icon-collection"></i>
                <span>教材管理</span>
              </template>
              <el-menu-item index="/teacher/textbook">
                <i class="el-icon-notebook-2"></i>
                <span>教材列表</span>
              </el-menu-item>
              <el-menu-item index="/teacher/textbook-application">
                <i class="el-icon-document-checked"></i>
                <span>教材申报</span>
              </el-menu-item>
            </el-submenu>
            
            <el-submenu index="teaching">
              <template slot="title">
                <i class="el-icon-reading"></i>
                <span>教学管理</span>
              </template>
              <el-menu-item index="/teacher/course">
                <i class="el-icon-date"></i>
                <span>课程管理</span>
              </el-menu-item>
              <el-menu-item index="/teacher/class">
                <i class="el-icon-school"></i>
                <span>班级管理</span>
              </el-menu-item>
            </el-submenu>
            
            <el-menu-item index="/teacher/notice">
              <i class="el-icon-bell"></i>
              <span slot="title">通知公告</span>
            </el-menu-item>
            
            <el-menu-item index="/teacher/person">
              <i class="el-icon-user-solid"></i>
              <span slot="title">个人中心</span>
            </el-menu-item>
          </el-menu>
        </el-scrollbar>
      </div>
      
      <!-- 内容区域 -->
      <div class="layout-content" :class="{ 'content-expanded': isCollapse }">
        <el-breadcrumb separator="/" class="breadcrumb">
          <el-breadcrumb-item :to="{ path: '/teacher/home' }">首页</el-breadcrumb-item>
          <el-breadcrumb-item v-if="$route.meta && $route.meta.name">{{ $route.meta.name }}</el-breadcrumb-item>
        </el-breadcrumb>
        
        <div class="content-container">
          <router-view />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "TeacherLayout",
  data() {
    return {
      isCollapse: false,
      avatar: "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png",
      username: "教师"
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
  },
  methods: {
    toggleCollapse() {
      this.isCollapse = !this.isCollapse;
    },
    handleCommand(command) {
      if (command === "person") {
        this.$router.push("/teacher/person");
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
      
      if (user.role !== "TEACHER") {
        this.$message.error("您没有教师权限");
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
    }
  }
};
</script>

<style scoped>
.layout-container {
  height: 100vh;
  width: 100%;
  display: flex;
  flex-direction: column;
}

/* 顶部导航栏样式 */
.layout-header {
  height: 60px;
  background-color: #ffffff;
  border-bottom: 1px solid #f0f0f0;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  z-index: 10;
}

.logo-container {
  display: flex;
  align-items: center;
}

.logo-image {
  width: 32px;
  height: 32px;
  margin-right: 10px;
}

.logo-text {
  font-size: 18px;
  font-weight: bold;
  color: #002140;
}

.header-right {
  display: flex;
  align-items: center;
}

.dropdown-link {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 5px 10px;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.dropdown-link:hover {
  background-color: #f5f7fa;
}

.user-avatar {
  margin-right: 8px;
}

.username {
  font-size: 14px;
  color: #606266;
  margin-right: 5px;
}

/* 主要内容区样式 */
.layout-main {
  display: flex;
  flex: 1;
  overflow: hidden;
}

/* 侧边栏样式 */
.layout-sidebar {
  width: 220px;
  height: 100%;
  background-color: #002140;
  transition: width 0.3s;
  position: relative;
  z-index: 9;
  box-shadow: 2px 0 8px 0 rgba(29, 35, 41, 0.05);
}

.is-collapsed {
  width: 64px;
}

.sidebar-top {
  height: 50px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-bottom: 1px solid rgba(255, 255, 255, 0.05);
}

.collapse-btn {
  color: #bfcbd9;
  font-size: 16px;
  cursor: pointer;
  width: 100%;
  text-align: center;
  transition: color 0.3s;
}

.collapse-btn:hover {
  color: #1890ff;
}

.sidebar-scrollbar {
  height: calc(100% - 50px);
}

.sidebar-menu {
  border-right: none;
}

.el-menu-item, .el-submenu__title {
  height: 50px;
  line-height: 50px;
}

.el-menu-item i, .el-submenu__title i {
  color: #bfcbd9;
  font-size: 18px;
  margin-right: 8px;
}

.el-menu-item:hover i, .el-submenu__title:hover i {
  color: #1890ff;
}

.el-menu-item.is-active {
  background-color: #1a2d3f !important;
  border-right: 3px solid #1890ff;
}

.el-menu-item.is-active i {
  color: #1890ff;
}

/* 内容区样式 */
.layout-content {
  flex: 1;
  overflow: auto;
  padding: 20px;
  transition: margin-left 0.3s;
  background-color: #f0f2f5;
}

.content-expanded {
  margin-left: -156px;
}

.breadcrumb {
  margin-bottom: 20px;
  padding: 10px;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
}

.content-container {
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  min-height: calc(100vh - 180px);
}
</style> 