import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

// 解决导航栏或者底部导航tabBar中的vue-router在3.0版本以上频繁点击菜单报错的问题
const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err)
}

const routes = [
  // 管理员后台路由
  {
    path: '/manager',
    name: 'Manager',
    component: () => import('../views/manager/Layout.vue'),
    redirect: '/manager/home',
    meta: { requireAuth: true, role: 'ADMIN' },
    children: [
      {
        path: 'home',
        name: 'ManagerHome',
        component: () => import('../views/manager/Home.vue'),
        meta: { name: '系统首页', requireAuth: true, role: 'ADMIN' }
      },
      {
        path: 'person',
        name: 'ManagerPerson',
        component: () => import('../views/manager/Person.vue'),
        meta: { name: '个人信息', requireAuth: true, role: 'ADMIN' }
      },
      {
        path: 'user',
        name: 'UserManage',
        component: () => import('../views/manager/User.vue'),
        meta: { name: '用户管理', requireAuth: true, role: 'ADMIN' }
      },

      {
        path: 'notice',
        name: 'NoticeManage',
        component: () => import('../views/manager/Notice.vue'),
        meta: { name: '公告管理', requireAuth: true, role: 'ADMIN' }
      },
      {
        path: 'message-board',
        name: 'MessageBoardManage',
        component: () => import('../views/manager/MessageBoard.vue'),
        meta: { name: '留言板管理', requireAuth: true, role: 'ADMIN' }
      },
      {
        path: 'health-record',
        name: 'HealthRecordManage',
        component: () => import('../views/manager/HealthRecord.vue'),
        meta: { name: '健康档案管理', requireAuth: true, role: 'ADMIN' }
      },
      {
        path: 'checkin-record',
        name: 'CheckinRecordManage',
        component: () => import('../views/manager/CheckinRecord.vue'),
        meta: { name: '入住信息管理', requireAuth: true, role: 'ADMIN' }
      },
      {
        path: 'activity-category',
        name: 'ActivityCategoryManage',
        component: () => import('../views/manager/ActivityCategory.vue'),
        meta: { name: '活动分类管理', requireAuth: true, role: 'ADMIN' }
      },
      {
        path: 'activity',
        name: 'ActivityManage',
        component: () => import('../views/manager/Activity.vue'),
        meta: { name: '活动信息管理', requireAuth: true, role: 'ADMIN' }
      },
      {
        path: 'activity-participant',
        name: 'ActivityParticipantManage',
        component: () => import('../views/manager/ActivityParticipant.vue'),
        meta: { name: '活动报名管理', requireAuth: true, role: 'ADMIN' }
      },
      {
        path: 'care-plan',
        name: 'CarePlanManage',
        component: () => import('../views/manager/CarePlan.vue'),
        meta: { name: '护理计划管理', requireAuth: true, role: 'ADMIN' }
      },
      {
        path: 'care-assignment',
        name: 'CareAssignmentManage',
        component: () => import('../views/manager/CareAssignment.vue'),
        meta: { name: '护理安排管理', requireAuth: true, role: 'ADMIN' }
      },
      {
        path: 'care-record',
        name: 'CareRecordManage',
        component: () => import('../views/manager/CareRecord.vue'),
        meta: { name: '护理记录管理', requireAuth: true, role: 'ADMIN' }
      },
      {
        path: 'health-monitoring',
        name: 'HealthMonitoringManage',
        component: () => import('../views/manager/HealthMonitoring.vue'),
        meta: { name: '健康监控管理', requireAuth: true, role: 'ADMIN' }
      },
      {
        path: 'user-medication',
        name: 'UserMedicationManage',
        component: () => import('../views/manager/UserMedication.vue'),
        meta: { name: '药物管理', requireAuth: true, role: 'ADMIN' }
      },
      {
        path: 'bill-management',
        name: 'BillManagementManage',
        component: () => import('../views/manager/BillManagement.vue'),
        meta: { name: '费用管理', requireAuth: true, role: 'ADMIN' }
      },
      {
        path: 'nurse',
        name: 'NurseManage',
        component: () => import('../views/manager/Nurse.vue'),
        meta: { name: '护士管理', requireAuth: true, role: 'ADMIN' }
      }
    ]
  },

  // 前台用户路由
  {
    path: '/front',
    name: 'Front',
    component: () => import('../views/front/Layout.vue'),
    redirect: '/front/home',
    meta: { requireAuth: true, role: 'USER' },
    children: [
      {
        path: 'home',
        name: 'FrontHome',
        component: () => import('../views/front/Home.vue'),
        meta: { name: '首页', requireAuth: true, role: 'USER' }
      },
      {
        path: 'person',
        name: 'FrontPerson',
        component: () => import('../views/front/Person.vue'),
        meta: { name: '个人信息', requireAuth: true, role: 'USER' }
      },
      {
        path: 'checkin-record',
        name: 'FrontCheckinRecord',
        component: () => import('../views/front/CheckinRecord.vue'),
        meta: { name: '入住信息', requireAuth: true, role: 'USER' }
      },
      {
        path: 'notice',
        name: 'FrontNotice',
        component: () => import('../views/front/Notice.vue'),
        meta: { name: '通知公告', requireAuth: true, role: 'USER' }
      },
      {
        path: 'notice/:id',
        name: 'FrontNoticeDetail',
        component: () => import('../views/front/Notice.vue'),
        meta: { name: '公告详情', requireAuth: true, role: 'USER' }
      },
      {
        path: 'chat',
        name: 'FrontChat',
        component: () => import('../views/front/Chat.vue'),
        meta: { name: 'AI智能助手', requireAuth: true, role: 'USER' }
      },
      {
        path: 'health-record',
        name: 'FrontHealthRecord',
        component: () => import('../views/front/HealthRecord.vue'),
        meta: { name: '健康档案管理', requireAuth: true, role: 'USER' }
      },
      {
        path: 'health-monitoring',
        name: 'FrontHealthMonitoring',
        component: () => import('../views/front/HealthMonitoring.vue'),
        meta: { name: '健康监控', requireAuth: true, role: 'USER' }
      },
      {
        path: 'medication',
        name: 'FrontMedication',
        component: () => import('../views/front/Medication.vue'),
        meta: { name: '药物管理', requireAuth: true, role: 'USER' }
      },
      {
        path: 'activity',
        name: 'FrontActivity',
        component: () => import('../views/front/Activity.vue'),
        meta: { name: '活动中心', requireAuth: true, role: 'USER' }
      },
      {
        path: 'activity/:id',
        name: 'FrontActivityDetail',
        component: () => import('../views/front/ActivityDetail.vue'),
        meta: { name: '活动详情', requireAuth: true, role: 'USER' }
      },
      {
        path: 'my-activities',
        name: 'FrontMyActivities',
        component: () => import('../views/front/MyActivities.vue'),
        meta: { name: '我的活动', requireAuth: true, role: 'USER' }
      },
      {
        path: 'care-record',
        name: 'FrontCareRecord',
        component: () => import('../views/front/CareRecord.vue'),
        meta: { name: '护理记录', requireAuth: true, role: 'USER' }
      },
      {
        path: 'message-board',
        name: 'FrontMessageBoard',
        component: () => import('../views/front/MessageBoard.vue'),
        meta: { name: '留言板', requireAuth: true, role: 'USER' }
      },
      {
        path: 'payment',
        name: 'FrontPayment',
        component: () => import('../views/front/Payment.vue'),
        meta: { name: '费用支付', requireAuth: true, role: 'USER' }
      },
      {
        path: 'nurse',
        name: 'FrontNurse',
        component: () => import('../views/front/Nurse.vue'),
        meta: { name: '护理团队', requireAuth: true, role: 'USER' }
      }
    ]
  },

  // 护士路由
  {
    path: '/nurse',
    name: 'Nurse',
    component: () => import('../views/manager/Layout.vue'),
    redirect: '/nurse/home',
    meta: { requireAuth: true, role: 'NURSE' },
    children: [
      {
        path: 'home',
        name: 'NurseHome',
        component: () => import('../views/manager/Home.vue'),
        meta: { name: '系统首页', requireAuth: true, role: 'NURSE' }
      },
      {
        path: 'person',
        name: 'NursePerson',
        component: () => import('../views/manager/Person.vue'),
        meta: { name: '个人信息', requireAuth: true, role: 'NURSE' }
      },
      // 健康管理模块 - 复用manager页面
      {
        path: 'health-record',
        name: 'NurseHealthRecord',
        component: () => import('../views/manager/HealthRecord.vue'),
        meta: { name: '健康档案管理', requireAuth: true, role: 'NURSE' }
      },
      {
        path: 'health-monitoring',
        name: 'NurseHealthMonitoring',
        component: () => import('../views/manager/HealthMonitoring.vue'),
        meta: { name: '健康监控管理', requireAuth: true, role: 'NURSE' }
      },
      {
        path: 'user-medication',
        name: 'NurseUserMedication',
        component: () => import('../views/manager/UserMedication.vue'),
        meta: { name: '药物管理', requireAuth: true, role: 'NURSE' }
      },
      // 住宿管理模块 - 复用manager页面
      {
        path: 'checkin-record',
        name: 'NurseCheckinRecord',
        component: () => import('../views/manager/CheckinRecord.vue'),
        meta: { name: '入住信息管理', requireAuth: true, role: 'NURSE' }
      },
      // 护理管理模块 - 复用manager页面
      {
        path: 'care-plan',
        name: 'NurseCarePlan',
        component: () => import('../views/manager/CarePlan.vue'),
        meta: { name: '护理计划管理', requireAuth: true, role: 'NURSE' }
      },
      {
        path: 'care-assignment',
        name: 'NurseCareAssignment',
        component: () => import('../views/manager/CareAssignment.vue'),
        meta: { name: '护理安排管理', requireAuth: true, role: 'NURSE' }
      },
      {
        path: 'care-record',
        name: 'NurseCareRecord',
        component: () => import('../views/manager/CareRecord.vue'),
        meta: { name: '护理记录管理', requireAuth: true, role: 'NURSE' }
      },
      // 留言板管理 - 复用manager页面
      {
        path: 'message-board',
        name: 'NurseMessageBoard',
        component: () => import('../views/manager/MessageBoard.vue'),
        meta: { name: '留言板管理', requireAuth: true, role: 'NURSE' }
      },
      // 护士管理 - 复用manager页面
      {
        path: 'nurse',
        name: 'NurseManage',
        component: () => import('../views/manager/Nurse.vue'),
        meta: { name: '护士管理', requireAuth: true, role: 'NURSE' }
      }
    ]
  },

  // 公共路由
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue')
  },
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '*',
    name: 'NotFound',
    component: () => import('../views/NotFound.vue')
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

// 全局路由守卫，判断是否需要登录权限，以及权限是否满足
router.beforeEach((to, from, next) => {
  console.log('路由跳转:', from.path, '->', to.path);
  
  // 如果需要认证
  if (to.meta.requireAuth) {
    // 获取本地存储的用户信息
    const userInfoStr = localStorage.getItem('xm-user');
    if (!userInfoStr) {
      console.log('未登录，跳转到登录页');
      next('/login');
      return;
    }
    
    // 解析用户信息，检查角色
    const userInfo = JSON.parse(userInfoStr);
    if (!userInfo.token) {
      console.log('token不存在，跳转到登录页');
      next('/login');
      return;
    }
    
    // 如果路由有角色要求，判断用户角色是否匹配
    if (to.meta.role && to.meta.role !== userInfo.role) {
      console.log('角色不匹配，跳转到对应角色的首页');
      
      // 根据用户角色跳转到对应的首页
      switch (userInfo.role) {
        case 'ADMIN':
          next('/manager/home');
          break;
        case 'USER':
          next('/front/home');
          break;
        case 'NURSE':
          next('/nurse/home');
          break;
        default:
          next('/login');
          break;
      }
      return;
    }
  }
  
  // 放行
  next();
})

export default router