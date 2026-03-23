<template>
  <div class="notice-page">
    <!-- 列表页面 -->
    <div v-if="!showDetail" class="notice-list-container">
      <!-- 页面标题 -->
      <div class="flex justify-between items-center mb-6">
        <div class="text-xl font-medium text-gray-800 flex items-center">
          <i class="el-icon-bell text-blue-500 mr-2"></i>
          <span>通知公告</span>
        </div>
        <div class="flex">
          <el-input
            v-model="params.title"
            placeholder="请输入公告标题关键词"
            class="w-64 mr-3"
            clearable
            @clear="loadData"
            prefix-icon="el-icon-search"
          ></el-input>
          <el-button type="primary" @click="loadData" icon="el-icon-search" class="mr-2">搜索</el-button>
          <el-button type="default" @click="resetSearch" icon="el-icon-refresh-left">重置</el-button>
        </div>
      </div>

      <!-- 装饰元素 -->
      <div class="relative mb-6 overflow-hidden">
        <div class="absolute top-0 right-0 w-32 h-32 bg-blue-50 rounded-full -mr-16 -mt-16 opacity-70"></div>
        <div class="absolute bottom-0 left-0 w-24 h-24 bg-green-50 rounded-full -ml-12 -mb-12 opacity-70"></div>
      </div>

      <!-- 公告列表 -->
      <div>
        <div v-if="loading" class="flex justify-center items-center h-60">
          <el-skeleton :rows="6" animated />
        </div>
        
        <div v-else-if="!tableData.length" class="flex flex-col justify-center items-center h-60 text-gray-400">
          <i class="el-icon-document text-6xl mb-4"></i>
          <span>暂无通知公告</span>
        </div>
        
        <div v-else class="notice-list">
          <div 
            v-for="item in tableData" 
            :key="item.id"
            class="bg-white rounded-lg p-5 mb-4 border border-gray-100 hover:shadow-md transition-all cursor-pointer transform hover:-translate-y-1"
            @click="viewNoticeDetail(item.id)"
          >
            <div class="flex items-center justify-between">
              <div class="flex items-center">
                <div class="w-10 h-10 rounded-full bg-gradient-to-r from-blue-400 to-blue-500 flex items-center justify-center mr-3 shadow-sm">
                  <i class="el-icon-bell text-white"></i>
                </div>
                <h3 class="text-lg font-medium text-gray-800">{{ item.title }}</h3>
                <el-tag size="mini" type="success" class="ml-3" v-if="isNew(item.time)">NEW</el-tag>
              </div>
              <div class="text-sm text-gray-500 flex items-center">
                <i class="el-icon-time mr-1"></i>
                {{ formatDate(item.time) }}
              </div>
            </div>
            
            <div class="pl-13 mt-3">
              <p class="text-gray-600 mb-3 line-clamp-2 text-sm leading-relaxed">{{ item.content }}</p>
              <div class="flex items-center justify-between text-sm">
                <span class="text-gray-500">
                  <i class="el-icon-user mr-1"></i>
                  {{ item.createBy || '系统管理员' }}
                </span>
                <span class="text-blue-500 hover:text-blue-700 flex items-center">
                  查看详情 
                  <i class="el-icon-right ml-1"></i>
                </span>
              </div>
            </div>
          </div>
        </div>

        <!-- 分页 -->
        <div class="flex justify-center mt-6" v-if="total > 0">
          <el-pagination
            background
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="params.pageNum"
            :page-sizes="[5, 10, 20, 50]"
            :page-size="params.pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total">
          </el-pagination>
        </div>
      </div>
    </div>

    <!-- 详情页面 -->
    <div v-else class="notice-detail-page">
      <!-- 返回按钮 -->
      <div class="mb-6">
        <el-button type="text" icon="el-icon-back" @click="showDetail = false">返回列表</el-button>
      </div>

      <div class="bg-white rounded-xl shadow-md overflow-hidden">
        <!-- 标题区域 -->
        <div class="relative bg-gradient-to-r from-blue-500 to-blue-600 py-10 px-8 text-white">
          <h1 class="text-2xl md:text-3xl font-bold mb-3">{{ detailData.title }}</h1>
          <div class="flex flex-wrap items-center text-sm opacity-85">
            <div class="flex items-center mr-6 mb-2">
              <i class="el-icon-user mr-2"></i>
              <span>{{ detailData.createBy || '系统管理员' }}</span>
            </div>
            <div class="flex items-center mr-6 mb-2">
              <i class="el-icon-time mr-2"></i>
              <span>{{ formatDate(detailData.time) }}</span>
            </div>
<!--            <div class="flex items-center mb-2">-->
<!--              <i class="el-icon-view mr-2"></i>-->
<!--              <span>阅读量: {{ detailData.viewCount || 0 }}</span>-->
<!--            </div>-->
          </div>
          
          <!-- 装饰元素 -->
          <div class="absolute top-0 right-0 w-40 h-40 bg-white opacity-10 rounded-full -mr-10 -mt-10"></div>
          <div class="absolute bottom-0 left-0 w-24 h-24 bg-white opacity-10 rounded-full -ml-10 -mb-10"></div>
        </div>
        
        <!-- 内容区域 -->
        <div class="p-8">
          <div v-if="detailLoading" class="flex justify-center my-10">
            <el-skeleton :rows="10" animated />
          </div>
          
          <div v-else class="notice-content text-gray-700 leading-relaxed whitespace-pre-wrap min-h-[300px]">
            {{ detailData.content }}
          </div>
          
          <!-- 底部工具栏 -->
          <div class="mt-8 pt-6 border-t border-gray-100 flex justify-between items-center">
<!--            <div class="flex space-x-2">-->
<!--              <el-button type="text" icon="el-icon-printer" @click="printNotice">打印</el-button>-->
<!--              <el-button type="text" icon="el-icon-share" @click="shareNotice">分享</el-button>-->
<!--            </div>-->
            <div>
              <el-button type="primary" plain icon="el-icon-back" @click="showDetail = false">返回列表</el-button>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 相关公告 -->
      <div class="mt-6">
        <h3 class="text-lg font-medium text-gray-800 mb-4 flex items-center">
          <i class="el-icon-connection text-blue-500 mr-2"></i>
          相关公告
        </h3>
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div 
            v-for="item in relatedNotices" 
            :key="item.id"
            class="bg-white p-4 rounded-lg border border-gray-100 hover:shadow-md cursor-pointer transition-all"
            @click="viewNoticeDetail(item.id)"
          >
            <div class="flex items-center">
              <i class="el-icon-document text-blue-500 mr-2"></i>
              <div class="flex-1 truncate">{{ item.title }}</div>
              <div class="text-xs text-gray-500 ml-2">{{ formatDate(item.time, 'short') }}</div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 分享对话框 -->

  </div>
</template>

<script>
import request from '@/utils/request'

export default {
  name: 'FrontNotice',
  data() {
    return {
      loading: false,
      detailLoading: false,
      showDetail: false,
      tableData: [],
      total: 0,
      params: {
        pageNum: 1,
        pageSize: 10,
        title: '',
        status: '1' // 默认查询已发布的公告
      },
      detailData: {},
      relatedNotices: [],
      shareDialogVisible: false,
      noticeId: null
    }
  },
  created() {
    // 检查URL参数中是否有ID
    const id = this.$route.params.id
    if (id) {
      this.viewNoticeDetail(id)
    } else {
      this.loadData()
    }
  },
  methods: {
    // 加载公告数据
    async loadData() {
      this.loading = true
      try {
        const res = await request.get('/notice/selectPage', { params: this.params })
        if (res.code === '200') {
          this.tableData = res.data.list || []
          this.total = res.data.total
        } else {
          this.$message.error(res.msg || '获取公告列表失败')
        }
      } catch (error) {
        console.error('加载公告数据出错', error)
        this.$message.error('获取公告列表失败')
      } finally {
        this.loading = false
      }
    },
    
    // 重置搜索条件
    resetSearch() {
      this.params = {
        pageNum: 1,
        pageSize: 10,
        title: '',
        status: '1'
      }
      this.loadData()
    },
    
    // 查看公告详情
    async viewNoticeDetail(id) {
      if (!id) return
      
      this.noticeId = id
      this.detailLoading = true
      this.showDetail = true
      
      try {
        // 更新URL但不重新加载页面
        this.$router.push({
          path: `/front/notice/${id}`,
          replace: true
        })
        
        const res = await request.get(`/notice/selectById/${id}`)
        if (res.code === '200') {
          this.detailData = res.data || {}
          this.loadRelatedNotices()
        } else {
          this.$message.error(res.msg || '获取公告详情失败')
        }
      } catch (error) {
        console.error('获取公告详情出错', error)
        this.$message.error('获取公告详情失败')
      } finally {
        this.detailLoading = false
      }
    },
    
    // 加载相关公告
    async loadRelatedNotices() {
      try {
        const res = await request.get('/notice/recent', { params: { limit: 4 } })
        if (res.code === '200') {
          // 过滤掉当前正在查看的公告
          this.relatedNotices = (res.data || [])
            .filter(item => item.id !== this.noticeId)
            .slice(0, 4)
        }
      } catch (error) {
        console.error('获取相关公告出错', error)
      }
    },
    
    // 每页条数变化
    handleSizeChange(val) {
      this.params.pageSize = val
      this.loadData()
    },
    
    // 当前页变化
    handleCurrentChange(val) {
      this.params.pageNum = val
      this.loadData()
    },
    
    // 格式化日期
    formatDate(dateStr, type = 'full') {
      if (!dateStr) return '未知时间'
      
      const date = new Date(dateStr)
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      const hours = String(date.getHours()).padStart(2, '0')
      const minutes = String(date.getMinutes()).padStart(2, '0')
      
      if (type === 'short') {
        return `${month}-${day}`
      }
      
      return `${year}-${month}-${day} ${hours}:${minutes}`
    },
    
    // 判断是否为新通知（7天内）
    isNew(dateStr) {
      if (!dateStr) return false
      
      const noticeDate = new Date(dateStr)
      const now = new Date()
      const diffDays = Math.floor((now - noticeDate) / (1000 * 60 * 60 * 24))
      
      return diffDays <= 7
    },
    
    // 打印通知
    printNotice() {
      window.print()
    },
    
    // 分享通知
    shareNotice() {
      this.shareDialogVisible = true
    }
  }
}
</script>

<style scoped>
.notice-page {
  min-height: 80vh;
}

.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

.notice-content {
  font-size: 15px;
  line-height: 1.8;
}

/* 分享图标样式 */
.share-icon {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s;
}

.share-icon:hover {
  transform: scale(1.1);
}

/* 打印时隐藏不必要的元素 */
@media print {
  .el-button, 
  .el-pagination,
  .notice-list-container,
  .share-icon {
    display: none !important;
  }
  
  .notice-detail-page {
    padding: 0;
  }
}
</style> 