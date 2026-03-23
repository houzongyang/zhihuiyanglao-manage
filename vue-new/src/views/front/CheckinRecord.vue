<template>
  <div>
    <!-- 页面介绍和欢迎信息 -->
    <div class="bg-gradient-to-r from-blue-500 to-indigo-600 rounded-xl overflow-hidden shadow-lg mb-6">
      <div class="relative">
        <div class="absolute inset-0 bg-pattern opacity-10"></div>
        <div class="px-6 py-8 relative z-10">
          <div class="flex items-center">
            <div class="bg-white/20 p-3 rounded-lg shadow-inner">
              <i class="el-icon-house text-white text-2xl"></i>
            </div>
            <div class="ml-5">
              <h1 class="text-white text-2xl font-bold">我的入住信息</h1>
              <p class="text-blue-100 mt-1">查看和管理您在养老院的入住相关信息</p>
            </div>
          </div>
        </div>
        <div class="absolute bottom-0 right-0 transform translate-y-1/4">
          <svg width="180" height="180" viewBox="0 0 200 200" xmlns="http://www.w3.org/2000/svg" class="opacity-20">
            <path fill="white" d="M42,15.9L57.7,15.9L57.7,0.2L42,0.2L42,15.9ZM30.2,42L30.2,57.7L45.9,57.7L45.9,42L30.2,42ZM73.5,42L57.7,42L57.7,57.7L73.5,57.7L73.5,42ZM15.9,57.7L15.9,73.5L31.6,73.5L31.6,57.7L15.9,57.7ZM57.7,57.7L57.7,73.5L73.5,73.5L73.5,57.7L57.7,57.7ZM0.2,73.5L0.2,89.3L15.9,89.3L15.9,73.5L0.2,73.5ZM42,73.5L42,89.3L57.7,89.3L57.7,73.5L42,73.5ZM73.5,73.5L73.5,89.3L89.3,89.3L89.3,73.5L73.5,73.5ZM15.9,89.3L15.9,105L31.6,105L31.6,89.3L15.9,89.3ZM57.7,89.3L57.7,105L73.5,105L73.5,89.3L57.7,89.3ZM31.6,105L31.6,120.8L47.4,120.8L47.4,105L31.6,105ZM73.5,105L73.5,120.8L89.3,120.8L89.3,105L73.5,105ZM47.4,120.8L47.4,136.5L63.1,136.5L63.1,120.8L47.4,120.8Z"/>
          </svg>
        </div>
      </div>
      <div class="bg-gradient-to-r from-blue-700/20 to-indigo-700/20 px-6 py-4">
        <div class="flex justify-between items-center">
          <div class="flex items-center text-blue-100">
            <i class="el-icon-info text-blue-200 mr-2"></i>
            <span>有问题请联系管理员或拨打服务热线</span>
          </div>
          <div class="text-white font-bold">400-123-4567</div>
        </div>
      </div>
    </div>

    <!-- 入住状态卡片 -->
    <div v-if="currentRecord" class="mb-6">
      <div class="bg-white rounded-xl overflow-hidden shadow-md">
        <div class="p-6">
          <div class="flex justify-between items-start">
            <div>
              <h2 class="text-xl font-bold text-gray-800">您的入住状态</h2>
              <p class="text-gray-500 mt-1">Room {{ currentRecord.roomNumber || '未分配' }}</p>
            </div>
            <div>
              <el-tag
                :type="getStatusType(currentRecord.checkinStatus)"
                size="medium"
                effect="dark"
                class="rounded-full px-4 py-2"
              >
                {{ getStatusText(currentRecord.checkinStatus) }}
              </el-tag>
            </div>
          </div>

          <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mt-6">
            <div class="bg-gradient-to-br from-blue-50 to-indigo-50 rounded-xl p-5 border border-blue-100">
              <div class="flex justify-between mb-3">
                <span class="text-gray-600">入住日期</span>
                <i class="el-icon-date text-blue-400"></i>
              </div>
              <div class="text-xl font-bold text-gray-800">{{ currentRecord.checkinDate || '未入住' }}</div>
            </div>

            <div class="bg-gradient-to-br from-blue-50 to-indigo-50 rounded-xl p-5 border border-blue-100">
              <div class="flex justify-between mb-3">
                <span class="text-gray-600">缴费状态</span>
                <i class="el-icon-wallet text-blue-400"></i>
              </div>
              <div class="flex items-center">
                <div class="w-3 h-3 rounded-full mr-2" :class="{
                  'bg-green-500': currentRecord.paymentStatus === 'PAID',
                  'bg-red-500': currentRecord.paymentStatus === 'UNPAID',
                  'bg-yellow-500': currentRecord.paymentStatus === 'PARTIAL'
                }"></div>
                <span class="text-xl font-bold text-gray-800">{{ getPaymentStatusText(currentRecord.paymentStatus) }}</span>
              </div>
            </div>

            <div class="bg-gradient-to-br from-blue-50 to-indigo-50 rounded-xl p-5 border border-blue-100">
              <div class="flex justify-between mb-3">
                <span class="text-gray-600">床位号</span>
                <i class="el-icon-s-grid text-blue-400"></i>
              </div>
              <div class="text-xl font-bold text-gray-800">{{ currentRecord.bedNumber || '未分配' }}</div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 入住详情信息 -->
    <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
      <!-- 基本信息 -->
      <div class="lg:col-span-2">
        <div v-if="currentRecord" class="bg-white rounded-xl overflow-hidden shadow-md">
          <div class="border-b border-gray-100 px-6 py-4 flex justify-between items-center">
            <h3 class="text-lg font-bold text-gray-800">基本信息</h3>
            <i class="el-icon-user-solid text-blue-500"></i>
          </div>
          <div class="p-6">
            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div class="space-y-4">
                <div>
                  <div class="text-sm text-gray-500 mb-1">用户名称</div>
                  <div class="font-medium">{{ currentRecord.userName }}</div>
                </div>
                <div>
                  <div class="text-sm text-gray-500 mb-1">房间号</div>
                  <div class="font-medium">{{ currentRecord.roomNumber || '未分配' }}</div>
                </div>
                <div>
                  <div class="text-sm text-gray-500 mb-1">房间类型</div>
                  <div class="font-medium">{{ currentRecord.roomType || '标准间' }}</div>
                </div>
                <div>
                  <div class="text-sm text-gray-500 mb-1">床位号</div>
                  <div class="font-medium">{{ currentRecord.bedNumber || '未分配' }}</div>
                </div>
              </div>
              <div class="space-y-4">
                <div>
                  <div class="text-sm text-gray-500 mb-1">入住时间</div>
                  <div class="font-medium">{{ currentRecord.checkinDate }}</div>
                </div>
                <div>
                  <div class="text-sm text-gray-500 mb-1">退房时间</div>
                  <div class="font-medium">{{ currentRecord.checkoutDate || '未退房' }}</div>
                </div>
                <div>
                  <div class="text-sm text-gray-500 mb-1">入住状态</div>
                  <div class="font-medium">
                    <el-tag
                      :type="getStatusType(currentRecord.checkinStatus)"
                      size="mini"
                    >
                      {{ getStatusText(currentRecord.checkinStatus) }}
                    </el-tag>
                  </div>
                </div>
                <div>
                  <div class="text-sm text-gray-500 mb-1">特殊需求</div>
                  <div class="font-medium">{{ currentRecord.specialNeeds || '无' }}</div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 缴费信息 -->
        <div v-if="currentRecord" class="bg-white rounded-xl overflow-hidden shadow-md mt-6">
          <div class="border-b border-gray-100 px-6 py-4 flex justify-between items-center">
            <h3 class="text-lg font-bold text-gray-800">缴费信息</h3>
            <i class="el-icon-money text-blue-500"></i>
          </div>
          <div class="p-6">
            <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
              <div class="bg-gradient-to-br from-gray-50 to-gray-100 rounded-xl p-5">
                <div class="text-sm text-gray-500 mb-1">月费用</div>
                <div class="text-2xl font-bold text-gray-800">¥ {{ currentRecord.monthlyFee }}</div>
              </div>
<!--              <div class="bg-gradient-to-br from-gray-50 to-gray-100 rounded-xl p-5">-->
<!--                <div class="text-sm text-gray-500 mb-1">押金</div>-->
<!--                <div class="text-2xl font-bold text-gray-800">¥ {{ currentRecord.deposit }}</div>-->
<!--              </div>-->
              <div class="bg-gradient-to-br from-gray-50 to-gray-100 rounded-xl p-5">
                <div class="text-sm text-gray-500 mb-1">已缴费用</div>
                <div class="text-2xl font-bold text-gray-800">¥ {{ currentRecord.totalPaid }}</div>
              </div>
            </div>
            
<!--            <div class="mt-6">-->
<!--              <div class="flex justify-between items-center mb-2">-->
<!--                <div class="text-gray-600">缴费进度</div>-->
<!--                <div class="text-gray-600">{{ paymentPercentage }}%</div>-->
<!--              </div>-->
<!--              <el-progress -->
<!--                :percentage="paymentPercentage" -->
<!--                :color="paymentProgressColor"-->
<!--                :format="() => ''"-->
<!--                :stroke-width="10"-->
<!--                class="rounded-full overflow-hidden"-->
<!--              ></el-progress>-->
<!--            </div>-->
            
            <div class="mt-6 flex justify-end">
              <el-button v-if="currentRecord.paymentStatus !== 'PAID'" type="primary" icon="el-icon-wallet" class="rounded-lg">
                在线缴费
              </el-button>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 右侧信息 -->
      <div class="lg:col-span-1">
        <!-- 紧急联系人信息 -->
        <div v-if="currentRecord" class="bg-white rounded-xl overflow-hidden shadow-md">
          <div class="border-b border-gray-100 px-6 py-4 flex justify-between items-center">
            <h3 class="text-lg font-bold text-gray-800">紧急联系人</h3>
            <i class="el-icon-phone-outline text-blue-500"></i>
          </div>
          <div class="p-6">
            <div class="space-y-4">
              <div>
                <div class="text-sm text-gray-500 mb-1">联系人姓名</div>
                <div class="font-medium">{{ currentRecord.emergencyContact || '未设置' }}</div>
              </div>
              <div>
                <div class="text-sm text-gray-500 mb-1">联系电话</div>
                <div class="font-medium">{{ currentRecord.emergencyPhone || '未设置' }}</div>
              </div>
            </div>
            <div class="mt-6">
              <el-button type="text" icon="el-icon-edit" class="text-blue-500">
                修改联系人信息
              </el-button>
            </div>
          </div>
        </div>
        
        <!-- 备注信息 -->
        <div v-if="currentRecord" class="bg-white rounded-xl overflow-hidden shadow-md mt-6">
          <div class="border-b border-gray-100 px-6 py-4 flex justify-between items-center">
            <h3 class="text-lg font-bold text-gray-800">备注信息</h3>
            <i class="el-icon-document text-blue-500"></i>
          </div>
          <div class="p-6">
            <div class="bg-gray-50 p-4 rounded-lg border border-gray-100">
              <p class="text-gray-700">{{ currentRecord.remarks || '暂无备注信息' }}</p>
            </div>
          </div>
        </div>
        
        <!-- 服务申请 -->
<!--        <div class="bg-white rounded-xl overflow-hidden shadow-md mt-6">-->
<!--          <div class="border-b border-gray-100 px-6 py-4 flex justify-between items-center">-->
<!--            <h3 class="text-lg font-bold text-gray-800">快捷服务</h3>-->
<!--            <i class="el-icon-s-cooperation text-blue-500"></i>-->
<!--          </div>-->
<!--          <div class="p-6">-->
<!--            <div class="grid grid-cols-2 gap-4">-->
<!--              <div class="bg-blue-50 hover:bg-blue-100 transition-colors p-4 rounded-lg cursor-pointer flex flex-col items-center justify-center">-->
<!--                <i class="el-icon-first-aid-kit text-2xl text-blue-500 mb-2"></i>-->
<!--                <span class="text-sm text-gray-700">医疗服务</span>-->
<!--              </div>-->
<!--              <div class="bg-green-50 hover:bg-green-100 transition-colors p-4 rounded-lg cursor-pointer flex flex-col items-center justify-center">-->
<!--                <i class="el-icon-dish text-2xl text-green-500 mb-2"></i>-->
<!--                <span class="text-sm text-gray-700">餐饮服务</span>-->
<!--              </div>-->
<!--              <div class="bg-amber-50 hover:bg-amber-100 transition-colors p-4 rounded-lg cursor-pointer flex flex-col items-center justify-center">-->
<!--                <i class="el-icon-toilet-paper text-2xl text-amber-500 mb-2"></i>-->
<!--                <span class="text-sm text-gray-700">清洁服务</span>-->
<!--              </div>-->
<!--              <div class="bg-purple-50 hover:bg-purple-100 transition-colors p-4 rounded-lg cursor-pointer flex flex-col items-center justify-center">-->
<!--                <i class="el-icon-suitcase text-2xl text-purple-500 mb-2"></i>-->
<!--                <span class="text-sm text-gray-700">设施报修</span>-->
<!--              </div>-->
<!--            </div>-->
<!--          </div>-->
<!--        </div>-->
      </div>
    </div>
    
    <!-- 无入住信息时的提示 -->
    <div v-if="!currentRecord && !loading" class="flex flex-col items-center justify-center bg-white rounded-xl p-10 shadow-md">
      <div class="text-8xl text-gray-200 mb-4">
        <i class="el-icon-house"></i>
      </div>
      <h3 class="text-xl font-medium text-gray-600 mb-2">您暂无入住信息</h3>
      <p class="text-gray-500 mb-6 text-center max-w-md">您目前没有任何入住记录，如需入住请联系管理员进行登记</p>
      <el-button type="primary" icon="el-icon-phone" class="rounded-lg">
        联系管理员
      </el-button>
    </div>
    
    <!-- 加载状态 -->
    <div v-if="loading" class="flex items-center justify-center p-12">
      <div class="text-center">
        <i class="el-icon-loading text-blue-500 text-3xl mb-4"></i>
        <p class="text-gray-500">正在加载您的入住信息...</p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "FrontCheckinRecord",
  data() {
    return {
      loading: false,
      currentRecord: null,
      userId: null
    };
  },
  computed: {
    // 计算缴费进度百分比
    paymentPercentage() {
      if (!this.currentRecord || !this.currentRecord.monthlyFee || this.currentRecord.monthlyFee <= 0) {
        return 0;
      }
      
      const totalPaid = this.currentRecord.totalPaid || 0;
      const monthlyFee = this.currentRecord.monthlyFee || 1; // 避免除以零
      
      let percentage = Math.round((totalPaid / monthlyFee) * 100);
      // 限制百分比在0-100之间
      return Math.min(Math.max(percentage, 0), 100);
    },
    
    // 根据百分比设置进度条颜色
    paymentProgressColor() {
      const percentage = this.paymentPercentage;
      if (percentage < 30) {
        return '#f56c6c'; // 红色
      } else if (percentage < 80) {
        return '#e6a23c'; // 黄色
      } else {
        return '#67c23a'; // 绿色
      }
    }
  },
  created() {
    // 从本地存储获取用户ID
    const userInfo = JSON.parse(localStorage.getItem("xm-user") || "{}");
    this.userId = userInfo.id;
    
    // 获取当前用户的入住信息
    this.fetchUserCheckinRecord();
  },
  methods: {
    // 获取当前用户的入住信息
    fetchUserCheckinRecord() {
      if (!this.userId) {
        this.$message.warning('获取用户信息失败，请重新登录');
        return;
      }
      
      this.loading = true;
      
      // 构造查询参数，按照用户ID查询
      const params = {
        userId: this.userId
      };
      
      this.$request.get('/checkinRecord/selectAll', { params })
        .then(res => {
          console.log('获取用户入住记录结果:', res);
          if (res.code === "200") {
            if (res.data && res.data.length > 0) {
              // 默认显示第一条记录（如果有多条可以考虑添加切换功能）
              this.currentRecord = res.data[0];
            } else {
              this.currentRecord = null;
              this.$message.info('暂无入住记录');
            }
          } else {
            this.$message.error(res.msg || '获取入住记录失败');
          }
          this.loading = false;
        })
        .catch(err => {
          console.error('获取入住记录接口错误:', err);
          this.$message.error('获取数据失败，请检查网络连接或后端服务');
          this.loading = false;
        });
    },
    
    // 获取入住状态类型
    getStatusType(status) {
      switch (status) {
        case 'CHECKED_IN':
          return 'success';
        case 'CHECKED_OUT':
          return 'info';
        case 'RESERVED':
          return 'warning';
        default:
          return '';
      }
    },
    
    // 获取入住状态文本
    getStatusText(status) {
      switch (status) {
        case 'CHECKED_IN':
          return '已入住';
        case 'CHECKED_OUT':
          return '已退房';
        case 'RESERVED':
          return '已预订';
        default:
          return '未知';
      }
    },
    
    // 获取缴费状态类型
    getPaymentStatusType(status) {
      switch (status) {
        case 'PAID':
          return 'success';
        case 'UNPAID':
          return 'danger';
        case 'PARTIAL':
          return 'warning';
        default:
          return '';
      }
    },
    
    // 获取缴费状态文本
    getPaymentStatusText(status) {
      switch (status) {
        case 'PAID':
          return '已缴费';
        case 'UNPAID':
          return '未缴费';
        case 'PARTIAL':
          return '部分缴费';
        default:
          return '未知';
      }
    }
  }
};
</script>

<style scoped>
/* 背景图案 */
.bg-pattern {
  background-image: url("data:image/svg+xml,%3Csvg width='60' height='60' viewBox='0 0 60 60' xmlns='http://www.w3.org/2000/svg'%3E%3Cg fill='none' fill-rule='evenodd'%3E%3Cg fill='%23ffffff' fill-opacity='0.4'%3E%3Cpath d='M36 34v-4h-2v4h-4v2h4v4h2v-4h4v-2h-4zm0-30V0h-2v4h-4v2h4v4h2V6h4V4h-4zM6 34v-4H4v4H0v2h4v4h2v-4h4v-2H6zM6 4V0H4v4H0v2h4v4h2V6h4V4H6z'/%3E%3C/g%3E%3C/g%3E%3C/svg%3E");
}

/* ElementUI覆盖样式 */
:deep(.el-progress-bar__outer) {
  @apply rounded-full;
}

:deep(.el-progress-bar__inner) {
  @apply rounded-full;
}
</style> 