<template>
  <div class="message-board-page min-h-screen bg-gradient-to-br from-blue-50 via-white to-green-50">
    <!-- 页面头部 -->
    <div class="relative mb-8">
      <!-- 装饰性背景元素 -->
      <div class="absolute top-0 right-0 w-40 h-40 bg-gradient-to-br from-blue-100 to-blue-50 rounded-full -mr-20 -mt-20 opacity-60"></div>
      <div class="absolute bottom-0 left-0 w-32 h-32 bg-gradient-to-br from-green-100 to-green-50 rounded-full -ml-16 -mb-16 opacity-60"></div>
      
      <div class="relative z-10">
        <!-- 页面标题 -->
        <div class="flex justify-between items-center mb-6">
          <div class="flex items-center">
            <div class="w-12 h-12 rounded-full bg-gradient-to-r from-blue-500 to-purple-500 flex items-center justify-center mr-4 shadow-lg">
              <i class="el-icon-chat-dot-round text-white text-xl"></i>
            </div>
            <div>
              <h1 class="text-2xl font-bold text-gray-800">留言板</h1>
              <p class="text-gray-600 text-sm mt-1">分享想法，交流心得</p>
            </div>
          </div>
          
          <!-- 发布留言按钮 -->
          <el-button type="primary" size="large" @click="showPublishDialog = true" 
                     class="px-6 py-3 rounded-lg shadow-lg hover:shadow-xl transform hover:-translate-y-0.5 transition-all duration-300">
            <i class="el-icon-edit mr-2"></i>
            发布留言
          </el-button>
        </div>

        <!-- 搜索区域 -->
        <div class="bg-white rounded-xl p-6 shadow-lg border border-gray-100">
          <div class="flex flex-wrap items-center justify-between">
            <div class="flex items-center space-x-4 mb-4 md:mb-0">
              <el-input
                v-model="searchForm.title"
                placeholder="搜索留言标题..."
                class="w-64"
                clearable
                prefix-icon="el-icon-search"
                @input="handleSearch"
              ></el-input>
              
              <el-select
                v-model="searchForm.userType"
                placeholder="选择用户类型"
                clearable
                class="w-32"
                @change="handleSearch"
              >
                <el-option label="全部" value=""></el-option>
                <el-option label="管理员" value="ADMIN"></el-option>
                <el-option label="教师" value="TEACHER"></el-option>
                <el-option label="用户" value="USER"></el-option>
              </el-select>
              
              <el-button type="primary" @click="handleSearch" icon="el-icon-search">搜索</el-button>
              <el-button @click="resetSearch" icon="el-icon-refresh">重置</el-button>
            </div>
            
            <div class="flex items-center text-sm text-gray-500">
              <i class="el-icon-document mr-1"></i>
              共 {{ total }} 条留言
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 留言列表 -->
    <div class="message-list">
      <div v-if="loading" class="flex justify-center items-center h-60">
        <el-skeleton :rows="5" animated class="w-full" />
      </div>
      
      <div v-else-if="!messageList.length" class="flex flex-col justify-center items-center h-60 text-gray-400">
        <i class="el-icon-chat-dot-round text-6xl mb-4 opacity-50"></i>
        <span class="text-lg">暂无留言</span>
        <p class="text-sm mt-2">成为第一个留言的人吧！</p>
      </div>
      
      <div v-else class="space-y-6">
        <div 
          v-for="message in messageList" 
          :key="message.id"
          class="message-card bg-white rounded-xl p-6 shadow-lg border border-gray-100 hover:shadow-xl transition-all duration-300 transform hover:-translate-y-1"
          @click="viewMessageDetail(message)"
        >
          <!-- 留言头部 -->
          <div class="flex items-center justify-between mb-4">
            <div class="flex items-center">
              <div class="w-10 h-10 rounded-full bg-gradient-to-r from-purple-400 to-pink-400 flex items-center justify-center mr-3 shadow-md">
                <i class="el-icon-user text-white"></i>
              </div>
              <div>
                <h3 class="text-lg font-semibold text-gray-800">{{ message.title }}</h3>
                <div class="flex items-center text-sm text-gray-500 mt-1">
                  <el-tag :type="getUserTypeTagType(message.userType)" size="mini" class="mr-2">
                    {{ getUserTypeName(message.userType) }}
                  </el-tag>
                  <span>{{ message.createdBy }}</span>
                  <span class="mx-2">•</span>
                  <i class="el-icon-time mr-1"></i>
                  {{ formatTime(message.createTime) }}
                </div>
              </div>
            </div>
            
            <div class="flex items-center space-x-4 text-sm text-gray-500">
              <div class="flex items-center" v-if="message.isTop === 1">
                <i class="el-icon-top text-red-500 mr-1"></i>
                <span class="text-red-500">置顶</span>
              </div>
              <div class="flex items-center">
                <i class="el-icon-view mr-1"></i>
                {{ message.viewCount || 0 }}
              </div>
              <div class="flex items-center">
                <i class="el-icon-chat-dot-square mr-1"></i>
                {{ message.replyCount || 0 }}
              </div>
            </div>
          </div>

          <!-- 留言内容 -->
          <div class="mb-4">
            <p class="text-gray-700 leading-relaxed line-clamp-3">{{ message.content }}</p>
          </div>

          <!-- 留言底部 -->
          <div class="flex items-center justify-between pt-4 border-t border-gray-100">
            <div class="flex items-center space-x-4">
              <el-button type="text" size="small" @click.stop="replyToMessage(message)" 
                         class="text-blue-500 hover:text-blue-700">
                <i class="el-icon-chat-dot-round mr-1"></i>
                回复
              </el-button>
              <el-button type="text" size="small" @click.stop="shareMessage(message)" 
                         class="text-green-500 hover:text-green-700">
                <i class="el-icon-share mr-1"></i>
                分享
              </el-button>
            </div>
            
            <span class="text-blue-500 hover:text-blue-700 text-sm cursor-pointer flex items-center">
              查看详情
              <i class="el-icon-right ml-1"></i>
            </span>
          </div>

          <!-- 回复列表预览 -->
          <div v-if="message.replies && message.replies.length > 0" class="mt-4 pt-4 border-t border-gray-50">
            <div class="space-y-3">
              <div 
                v-for="reply in message.replies.slice(0, 2)" 
                :key="reply.id"
                class="bg-gray-50 rounded-lg p-4 ml-4 border-l-4 border-blue-200"
              >
                <div class="flex items-center justify-between mb-2">
                  <div class="flex items-center text-sm">
                    <el-tag :type="getUserTypeTagType(reply.userType)" size="mini" class="mr-2">
                      {{ getUserTypeName(reply.userType) }}
                    </el-tag>
                    <span class="font-medium text-gray-700">{{ reply.createdBy }}</span>
                    <span class="mx-2 text-gray-400">•</span>
                    <span class="text-gray-500">{{ formatTime(reply.createTime) }}</span>
                  </div>
                </div>
                <p class="text-gray-700 text-sm">{{ reply.content }}</p>
              </div>
              
              <div v-if="message.replyCount > 2" class="text-center">
                <span class="text-blue-500 hover:text-blue-700 text-sm cursor-pointer" 
                      @click.stop="viewMessageDetail(message)">
                  查看全部 {{ message.replyCount }} 条回复 
                  <i class="el-icon-arrow-down ml-1"></i>
                </span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 分页 -->
      <div class="flex justify-center mt-8" v-if="total > 0">
        <el-pagination
          background
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[5, 10, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          class="rounded-lg"
        ></el-pagination>
      </div>
    </div>

    <!-- 发布留言对话框 -->
    <el-dialog
      title="发布留言"
      :visible.sync="showPublishDialog"
      width="600px"
      :close-on-click-modal="false"
      custom-class="rounded-lg"
    >
      <el-form :model="publishForm" :rules="publishRules" ref="publishForm" label-width="80px">
        <el-form-item label="留言标题" prop="title">
          <el-input 
            v-model="publishForm.title" 
            placeholder="请输入留言标题..."
            maxlength="100"
            show-word-limit
          ></el-input>
        </el-form-item>
        
        <el-form-item label="留言内容" prop="content">
          <el-input
            type="textarea"
            v-model="publishForm.content"
            placeholder="请输入留言内容..."
            :rows="6"
            maxlength="1000"
            show-word-limit
          ></el-input>
        </el-form-item>
      </el-form>
      
      <div slot="footer" class="flex justify-end space-x-3">
        <el-button @click="showPublishDialog = false">取消</el-button>
        <el-button type="primary" @click="publishMessage" :loading="publishLoading">
          发布留言
        </el-button>
      </div>
    </el-dialog>

    <!-- 回复留言对话框 -->
    <el-dialog
      :title="`回复：${replyTarget.title}`"
      :visible.sync="showReplyDialog"
      width="600px"
      :close-on-click-modal="false"
      custom-class="rounded-lg"
    >
      <el-form :model="replyForm" :rules="replyRules" ref="replyForm" label-width="80px">
        <el-form-item label="回复内容" prop="content">
          <el-input
            type="textarea"
            v-model="replyForm.content"
            placeholder="请输入回复内容..."
            :rows="4"
            maxlength="500"
            show-word-limit
          ></el-input>
        </el-form-item>
      </el-form>
      
      <div slot="footer" class="flex justify-end space-x-3">
        <el-button @click="showReplyDialog = false">取消</el-button>
        <el-button type="primary" @click="submitReply" :loading="replyLoading">
          发布回复
        </el-button>
      </div>
    </el-dialog>

    <!-- 留言详情对话框 -->
    <el-dialog
      title="留言详情"
      :visible.sync="showDetailDialog"
      width="800px"
      :close-on-click-modal="false"
      custom-class="rounded-lg"
    >
      <div v-if="currentMessage" class="message-detail">
        <!-- 主留言 -->
        <div class="bg-gray-50 rounded-lg p-6 mb-6">
          <div class="flex items-center justify-between mb-4">
            <div class="flex items-center">
              <div class="w-12 h-12 rounded-full bg-gradient-to-r from-purple-400 to-pink-400 flex items-center justify-center mr-4">
                <i class="el-icon-user text-white text-lg"></i>
              </div>
              <div>
                <h3 class="text-xl font-semibold text-gray-800">{{ currentMessage.title }}</h3>
                <div class="flex items-center text-sm text-gray-500 mt-1">
                  <el-tag :type="getUserTypeTagType(currentMessage.userType)" size="mini" class="mr-2">
                    {{ getUserTypeName(currentMessage.userType) }}
                  </el-tag>
                  <span>{{ currentMessage.createdBy }}</span>
                  <span class="mx-2">•</span>
                  <span>{{ formatTime(currentMessage.createTime) }}</span>
                </div>
              </div>
            </div>
            
            <div class="flex items-center space-x-4 text-sm text-gray-500">
              <span><i class="el-icon-view mr-1"></i>{{ currentMessage.viewCount || 0 }}</span>
              <span><i class="el-icon-chat-dot-square mr-1"></i>{{ currentMessage.replyCount || 0 }}</span>
            </div>
          </div>
          
          <div class="text-gray-700 leading-relaxed">
            {{ currentMessage.content }}
          </div>
        </div>

        <!-- 回复列表 -->
        <div v-if="currentReplies.length > 0">
          <h4 class="text-lg font-semibold text-gray-800 mb-4 flex items-center">
            <i class="el-icon-chat-dot-square mr-2"></i>
            回复列表 ({{ currentReplies.length }})
          </h4>
          
          <div class="space-y-4">
            <div 
              v-for="reply in currentReplies" 
              :key="reply.id"
              class="bg-white border border-gray-200 rounded-lg p-4"
            >
              <div class="flex items-center justify-between mb-3">
                <div class="flex items-center">
                  <div class="w-8 h-8 rounded-full bg-gradient-to-r from-blue-400 to-green-400 flex items-center justify-center mr-3">
                    <i class="el-icon-user text-white text-sm"></i>
                  </div>
                  <div class="flex items-center text-sm">
                    <el-tag :type="getUserTypeTagType(reply.userType)" size="mini" class="mr-2">
                      {{ getUserTypeName(reply.userType) }}
                    </el-tag>
                    <span class="font-medium text-gray-700">{{ reply.createdBy }}</span>
                    <span class="mx-2 text-gray-400">•</span>
                    <span class="text-gray-500">{{ formatTime(reply.createTime) }}</span>
                  </div>
                </div>
              </div>
              
              <div class="text-gray-700 pl-11">
                {{ reply.content }}
              </div>
            </div>
          </div>
        </div>

        <div v-else class="text-center py-8 text-gray-400">
          <i class="el-icon-chat-dot-round text-4xl mb-2"></i>
          <p>暂无回复，快来抢沙发吧！</p>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'MessageBoard',
  data() {
    return {
      // 搜索表单
      searchForm: {
        title: '',
        userType: ''
      },
      
      // 分页数据
      messageList: [],
      loading: false,
      total: 0,
      pageNum: 1,
      pageSize: 10,
      
      // 发布留言
      showPublishDialog: false,
      publishLoading: false,
      publishForm: {
        title: '',
        content: ''
      },
      publishRules: {
        title: [
          { required: true, message: '请输入留言标题', trigger: 'blur' },
          { min: 5, max: 100, message: '长度在 5 到 100 个字符', trigger: 'blur' }
        ],
        content: [
          { required: true, message: '请输入留言内容', trigger: 'blur' },
          { min: 10, max: 1000, message: '长度在 10 到 1000 个字符', trigger: 'blur' }
        ]
      },
      
      // 回复留言
      showReplyDialog: false,
      replyLoading: false,
      replyTarget: {},
      replyForm: {
        content: ''
      },
      replyRules: {
        content: [
          { required: true, message: '请输入回复内容', trigger: 'blur' },
          { min: 5, max: 500, message: '长度在 5 到 500 个字符', trigger: 'blur' }
        ]
      },
      
      // 留言详情
      showDetailDialog: false,
      currentMessage: null,
      currentReplies: []
    }
  },
  
  created() {
    this.loadMessages()
  },
  
  methods: {
    // 加载留言列表
    async loadMessages() {
      this.loading = true
      try {
        const params = {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          status: 'APPROVED', // 只显示已审核通过的留言
          parentId: 0, // 只查询主留言
          ...this.searchForm
        }
        
        const response = await this.$request.get('/messageBoard/selectPage', { params })
        if (response.code === '200') {
          this.messageList = response.data.list || []
          this.total = response.data.total || 0
          
          // 为每个主留言加载前几条回复
          for (let message of this.messageList) {
            await this.loadMessageReplies(message)
          }
        } else {
          this.$message.error(response.msg || '加载留言失败')
        }
      } catch (error) {
        console.error('加载留言失败:', error)
        this.$message.error('加载留言失败')
      } finally {
        this.loading = false
      }
    },
    
    // 加载留言的回复
    async loadMessageReplies(message) {
      try {
        const response = await this.$request.get('/messageBoard/selectAll', {
          params: { parentId: message.id, status: 'APPROVED' }
        })
        if (response.code === '200') {
          message.replies = response.data || []
        }
      } catch (error) {
        console.error('加载回复失败:', error)
      }
    },
    
    // 搜索
    handleSearch() {
      this.pageNum = 1
      this.loadMessages()
    },
    
    // 重置搜索
    resetSearch() {
      this.searchForm = {
        title: '',
        userType: ''
      }
      this.pageNum = 1
      this.loadMessages()
    },
    
    // 分页
    handleSizeChange(val) {
      this.pageSize = val
      this.loadMessages()
    },
    
    handleCurrentChange(val) {
      this.pageNum = val
      this.loadMessages()
    },
    
    // 发布留言
    publishMessage() {
      this.$refs.publishForm.validate(async (valid) => {
        if (valid) {
          this.publishLoading = true
          try {
            const user = JSON.parse(localStorage.getItem('xm-user') || '{}')
            const data = {
              ...this.publishForm,
              userId: user.id,
              userType: user.role,
              createdBy: user.name,
              parentId: 0
            }
            
            const response = await this.$request.post('/messageBoard/add', data)
            if (response.code === '200') {
              this.$message.success('留言发布成功，等待管理员审核')
              this.showPublishDialog = false
              this.publishForm = { title: '', content: '' }
              this.loadMessages()
            } else {
              this.$message.error(response.msg || '发布失败')
            }
          } catch (error) {
            console.error('发布留言失败:', error)
            this.$message.error('发布留言失败')
          } finally {
            this.publishLoading = false
          }
        }
      })
    },
    
    // 回复留言
    replyToMessage(message) {
      this.replyTarget = message
      this.showReplyDialog = true
    },
    
    submitReply() {
      this.$refs.replyForm.validate(async (valid) => {
        if (valid) {
          this.replyLoading = true
          try {
            const user = JSON.parse(localStorage.getItem('xm-user') || '{}')
            const data = {
              title: `回复：${this.replyTarget.title}`,
              content: this.replyForm.content,
              userId: user.id,
              userType: user.role,
              createdBy: user.name,
              parentId: this.replyTarget.id
            }
            
            const response = await this.$request.post('/messageBoard/add', data)
            if (response.code === '200') {
              this.$message.success('回复发布成功，等待管理员审核')
              this.showReplyDialog = false
              this.replyForm = { content: '' }
              this.loadMessages()
            } else {
              this.$message.error(response.msg || '回复失败')
            }
          } catch (error) {
            console.error('回复失败:', error)
            this.$message.error('回复失败')
          } finally {
            this.replyLoading = false
          }
        }
      })
    },
    
    // 查看留言详情
    async viewMessageDetail(message) {
      this.currentMessage = message
      this.showDetailDialog = true
      
      // 增加查看次数
      try {
        await this.$request.put(`/messageBoard/view/${message.id}`)
        message.viewCount = (message.viewCount || 0) + 1
      } catch (error) {
        console.error('更新查看次数失败:', error)
      }
      
      // 加载所有回复
      try {
        const response = await this.$request.get('/messageBoard/selectAll', {
          params: { parentId: message.id, status: 'APPROVED' }
        })
        if (response.code === '200') {
          this.currentReplies = response.data || []
        }
      } catch (error) {
        console.error('加载回复失败:', error)
      }
    },
    
    // 分享留言
    shareMessage(message) {
      const url = window.location.origin + window.location.pathname + `#/front/message-board?id=${message.id}`
      
      if (navigator.share) {
        navigator.share({
          title: message.title,
          text: message.content.substring(0, 100) + '...',
          url: url
        })
      } else {
        // 复制到剪贴板
        navigator.clipboard.writeText(url).then(() => {
          this.$message.success('链接已复制到剪贴板')
        }).catch(() => {
          this.$message.info('请手动复制链接：' + url)
        })
      }
    },
    
    // 获取用户类型标签类型
    getUserTypeTagType(userType) {
      switch (userType) {
        case 'ADMIN': return 'danger'
        case 'TEACHER': return 'warning'
        case 'USER': return 'info'
        default: return 'info'
      }
    },
    
    // 获取用户类型名称
    getUserTypeName(userType) {
      switch (userType) {
        case 'ADMIN': return '管理员'
        case 'TEACHER': return '教师'
        case 'USER': return '用户'
        default: return '未知'
      }
    },
    
    // 格式化时间
    formatTime(time) {
      if (!time) return ''
      const date = new Date(time)
      const now = new Date()
      const diff = now - date
      
      if (diff < 60000) { // 1分钟内
        return '刚刚'
      } else if (diff < 3600000) { // 1小时内
        return Math.floor(diff / 60000) + '分钟前'
      } else if (diff < 86400000) { // 1天内
        return Math.floor(diff / 3600000) + '小时前'
      } else if (diff < 604800000) { // 1周内
        return Math.floor(diff / 86400000) + '天前'
      } else {
        return date.toLocaleDateString()
      }
    }
  }
}
</script>

<style scoped>
/* 文本截断样式 */
.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.line-clamp-3 {
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

/* 自定义滚动条 */
:deep(.el-dialog__body) {
  max-height: 60vh;
  overflow-y: auto;
}

/* 优化对话框样式 */
:deep(.el-dialog) {
  border-radius: 12px;
  overflow: hidden;
}

:deep(.el-dialog__header) {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 16px 20px;
}

:deep(.el-dialog__title) {
  color: white;
  font-weight: 600;
}

/* 优化表单样式 */
:deep(.el-form-item__label) {
  font-weight: 500;
  color: #374151;
}

:deep(.el-input__inner) {
  border-radius: 8px;
}

:deep(.el-textarea__inner) {
  border-radius: 8px;
}

/* 优化分页样式 */
:deep(.el-pagination) {
  text-align: center;
}

:deep(.el-pager li) {
  border-radius: 6px;
  margin: 0 2px;
}

/* 优化标签样式 */
:deep(.el-tag) {
  border-radius: 12px;
  font-weight: 500;
}

/* 响应式适配 */
@media (max-width: 768px) {
  .message-card {
    margin: 0 -1rem;
    border-radius: 0;
  }
  
  .bg-gradient-to-br {
    background: white;
  }
}
</style> 