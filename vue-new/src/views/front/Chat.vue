<template>
  <div class="chat-container h-full flex flex-col">
    <!-- 背景装饰元素 -->
    <div class="decorative-elements">
      <div class="circle-1"></div>
      <div class="circle-2"></div>
      <div class="circle-3"></div>
      <div class="pattern-overlay"></div>
    </div>

    <!-- 聊天头部 -->
    <div class="chat-header backdrop-blur-sm bg-white/80 border-b border-gray-100 px-6 py-5 flex items-center justify-between relative z-10 shadow-sm">
      <div class="flex items-center">
        <div class="w-12 h-12 rounded-xl bg-gradient-to-br from-blue-500 via-indigo-500 to-purple-600 flex items-center justify-center mr-4 shadow-md transform rotate-3">
          <i class="el-icon-chat-dot-round text-white text-xl"></i>
        </div>
        <div>
          <h2 class="text-xl font-bold text-gray-800 flex items-center">
            AI智能助手
            <span class="ml-2 inline-flex items-center px-2 py-0.5 rounded-full text-xs font-medium bg-green-100 text-green-600">
              <span class="animate-pulse rounded-full h-2 w-2 bg-green-500 mr-1.5"></span>在线
            </span>
          </h2>
          <p class="text-sm text-gray-500 flex items-center mt-1">
            <i class="el-icon-magic-stick mr-1 text-indigo-500"></i>
            为您提供智能问答服务
          </p>
        </div>
      </div>
      <div class="flex items-center space-x-3">
        <el-button size="small" type="text" @click="clearChat" :disabled="messages.length === 0" class="flex items-center hover:bg-gray-100 px-3 py-2 rounded-lg transition-colors">
          <i class="el-icon-delete mr-1 text-gray-500"></i>
          <span class="text-gray-600">清空对话</span>
        </el-button>
        <div class="w-px h-6 bg-gray-200"></div>
        <el-button size="small" type="text" class="flex items-center hover:bg-gray-100 px-3 py-2 rounded-lg transition-colors">
          <i class="el-icon-help mr-1 text-blue-500"></i>
          <span class="text-gray-600">帮助</span>
        </el-button>
      </div>
    </div>

    <!-- 聊天消息区域 -->
    <div class="chat-messages flex-1 overflow-y-auto px-4 py-6 relative z-10" ref="chatContainer">
      <div class="max-w-4xl mx-auto space-y-8">
        <!-- 欢迎消息 -->
        <div v-if="messages.length === 0" class="welcome-container text-center py-16">
          <div class="welcome-animation mb-8">
            <div class="w-24 h-24 rounded-2xl bg-gradient-to-br from-blue-500 via-indigo-500 to-purple-600 flex items-center justify-center mx-auto mb-4 shadow-lg transform hover:scale-105 transition-transform duration-300 relative overflow-hidden">
              <div class="welcome-ripple"></div>
              <i class="el-icon-chat-dot-round text-white text-4xl"></i>
            </div>
          </div>
          <h3 class="text-2xl font-bold text-gray-800 mb-3">欢迎使用AI智能助手</h3>
          <p class="text-gray-500 mb-8 max-w-md mx-auto">我可以帮您解答各种问题，请输入您想了解的内容</p>
          <div class="flex flex-wrap justify-center gap-3 max-w-xl mx-auto">
            <el-tag 
              v-for="suggestion in suggestions" 
              :key="suggestion"
              class="suggestion-tag cursor-pointer px-4 py-2 text-base rounded-full hover:scale-105 transition-all duration-200 mb-2 border-0 shadow-sm"
              @click="sendSuggestion(suggestion)"
            >
              <i class="el-icon-lightning-bolt mr-1"></i>
              {{ suggestion }}
            </el-tag>
          </div>
        </div>

        <!-- 消息列表 -->
        <div v-for="(message, index) in messages" :key="index" class="message-item flex" :class="message.type === 'user' ? 'justify-end' : 'justify-start'">
          <!-- AI消息 -->
          <div v-if="message.type === 'ai'" class="flex items-start max-w-3xl group">
            <div class="w-10 h-10 rounded-lg bg-gradient-to-br from-blue-500 via-indigo-500 to-purple-600 flex items-center justify-center mr-3 flex-shrink-0 shadow-md relative overflow-hidden">
              <div class="absolute inset-0 bg-gradient-to-br from-blue-500 via-indigo-500 to-purple-600 opacity-80"></div>
              <div class="ai-avatar-glow absolute inset-0"></div>
              <i class="el-icon-chat-dot-round text-white text-sm relative z-10"></i>
            </div>
            <div class="ai-message bg-white rounded-2xl rounded-tl-md px-5 py-4 shadow-md border border-gray-100 transition-all group-hover:shadow-lg relative">
              <div class="message-text text-gray-700 leading-relaxed whitespace-pre-wrap">{{ message.content }}</div>
              <div class="flex items-center justify-between mt-3 pt-2 border-t border-gray-100">
                <div class="text-xs text-gray-400 flex items-center">
                  <i class="el-icon-time mr-1"></i>
                  {{ message.time }}
                </div>
                <div class="message-actions opacity-0 group-hover:opacity-100 transition-opacity">
                  <el-button type="text" size="mini" class="text-gray-400 hover:text-blue-500">
                    <i class="el-icon-copy-document"></i>
                  </el-button>
                  <el-button type="text" size="mini" class="text-gray-400 hover:text-blue-500">
                    <i class="el-icon-star-off"></i>
                  </el-button>
                </div>
              </div>
              <span class="absolute left-0 top-4 -ml-1.5 w-3 h-3 bg-white border-l border-t border-gray-100 transform rotate-45"></span>
            </div>
          </div>

          <!-- 用户消息 -->
          <div v-else class="flex items-start max-w-3xl group">
            <div class="user-message bg-gradient-to-r from-blue-600 to-indigo-600 text-white rounded-2xl rounded-tr-md px-5 py-4 shadow-md mr-3 relative">
              <div class="message-text leading-relaxed whitespace-pre-wrap">{{ message.content }}</div>
              <div class="flex items-center justify-between mt-3 pt-2 border-t border-blue-500/20">
                <div class="text-xs text-blue-100 flex items-center">
                  <i class="el-icon-time mr-1"></i>
                  {{ message.time }}
                </div>
                <div class="message-actions opacity-0 group-hover:opacity-100 transition-opacity">
                  <el-button type="text" size="mini" class="text-blue-200 hover:text-white">
                    <i class="el-icon-delete"></i>
                  </el-button>
                </div>
              </div>
              <span class="absolute right-0 top-4 -mr-1.5 w-3 h-3 bg-indigo-600 transform rotate-45"></span>
            </div>
            <div class="w-10 h-10 rounded-lg bg-gray-200 flex items-center justify-center flex-shrink-0 shadow-md overflow-hidden">
              <i class="el-icon-user text-gray-500 text-sm"></i>
            </div>
          </div>
        </div>

        <!-- AI正在输入 -->
        <div v-if="isLoading" class="flex justify-start">
          <div class="flex items-start max-w-3xl">
            <div class="w-10 h-10 rounded-lg bg-gradient-to-br from-blue-500 via-indigo-500 to-purple-600 flex items-center justify-center mr-3 flex-shrink-0 shadow-md relative">
              <div class="pulse-animation absolute inset-0 rounded-lg border-4 border-indigo-300/30"></div>
              <i class="el-icon-chat-dot-round text-white text-sm"></i>
            </div>
            <div class="typing-indicator bg-white rounded-2xl rounded-tl-md px-5 py-4 shadow-md border border-gray-100">
              <div class="flex items-center space-x-2">
                <div class="w-2 h-2 bg-gray-400 rounded-full animate-typing"></div>
                <div class="w-2 h-2 bg-gray-400 rounded-full animate-typing" style="animation-delay: 0.2s"></div>
                <div class="w-2 h-2 bg-gray-400 rounded-full animate-typing" style="animation-delay: 0.4s"></div>
              </div>
              <span class="absolute left-0 top-4 -ml-1.5 w-3 h-3 bg-white border-l border-t border-gray-100 transform rotate-45"></span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 输入区域 -->
    <div class="chat-input-area backdrop-blur-sm bg-white/90 border-t border-gray-100 px-4 py-5 shadow-md relative z-10">
      <div class="max-w-4xl mx-auto">
        <div class="flex items-end space-x-4">
          <div class="flex-1 relative">
            <el-input
              v-model="inputMessage"
              type="textarea"
              :rows="1"
              :autosize="{ minRows: 1, maxRows: 4 }"
              placeholder="输入您的问题..."
              @keydown.enter.exact.prevent="sendMessage"
              @keydown.enter.shift.exact="handleShiftEnter"
              :disabled="isLoading"
              ref="messageInput"
              class="chat-input"
            ></el-input>
            <div class="text-xs text-gray-400 mt-2 px-3 flex items-center">
              <i class="el-icon-info-filled mr-1 text-blue-400"></i>
              按 Enter 发送，Shift + Enter 换行
            </div>
            <div class="absolute right-3 bottom-10 flex space-x-2">
              <el-button type="text" circle size="mini" class="text-gray-400 hover:text-gray-600">
                <i class="el-icon-picture-outline"></i>
              </el-button>
              <el-button type="text" circle size="mini" class="text-gray-400 hover:text-gray-600">
                <i class="el-icon-microphone"></i>
              </el-button>
            </div>
          </div>
          <el-button 
            type="primary" 
            :loading="isLoading"
            @click="sendMessage"
            :disabled="!inputMessage.trim()"
            class="h-12 px-7 rounded-full shadow-md hover:shadow-lg transform hover:-translate-y-0.5 transition-all duration-200"
          >
            <i class="el-icon-s-promotion mr-1" v-if="!isLoading"></i>
            {{ isLoading ? '发送中' : '发送' }}
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'Chat',
  data() {
    return {
      messages: [],
      inputMessage: '',
      isLoading: false,
      suggestions: [
        '什么是人工智能？',
        '如何学习编程？',
        '推荐一些好书',
        '介绍一下Vue.js',
        '解释一下机器学习',
        '讲个笑话吧'
      ]
    }
  },
  mounted() {
    // 自动聚焦输入框
    this.$nextTick(() => {
      if (this.$refs.messageInput) {
        this.$refs.messageInput.focus()
      }
    })
  },
  methods: {
    // 发送消息
    async sendMessage() {
      if (!this.inputMessage.trim() || this.isLoading) {
        return
      }

      const userMessage = this.inputMessage.trim()
      this.inputMessage = ''

      // 添加用户消息
      this.messages.push({
        type: 'user',
        content: userMessage,
        time: this.formatTime(new Date())
      })

      // 滚动到底部
      this.scrollToBottom()

      // 调用AI接口
      this.isLoading = true
      try {
        const response = await axios.get('/api/ai/generate', {
          params: {
            message: userMessage
          }
        })

        if (response.data.code === '200') {
          // 添加AI回复
          this.messages.push({
            type: 'ai',
            content: response.data.data.generation,
            time: this.formatTime(new Date())
          })
        } else {
          throw new Error(response.data.msg || 'AI服务异常')
        }
      } catch (error) {
        console.error('AI请求失败:', error)
        this.messages.push({
          type: 'ai',
          content: '抱歉，AI服务暂时不可用，请稍后再试。',
          time: this.formatTime(new Date())
        })
        this.$message.error('AI服务异常: ' + (error.response?.data?.msg || error.message))
      } finally {
        this.isLoading = false
        this.scrollToBottom()
        // 重新聚焦输入框
        this.$nextTick(() => {
          if (this.$refs.messageInput) {
            this.$refs.messageInput.focus()
          }
        })
      }
    },

    // 发送建议问题
    sendSuggestion(suggestion) {
      this.inputMessage = suggestion
      this.sendMessage()
    },

    // 处理Shift+Enter换行
    handleShiftEnter() {
      // 允许默认行为（换行）
    },

    // 清空对话
    clearChat() {
      this.$confirm('确定要清空所有对话记录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.messages = []
        this.$message.success('对话记录已清空')
      }).catch(() => {})
    },

    // 滚动到底部
    scrollToBottom() {
      this.$nextTick(() => {
        const container = this.$refs.chatContainer
        if (container) {
          container.scrollTop = container.scrollHeight
        }
      })
    },

    // 格式化时间
    formatTime(date) {
      const hours = date.getHours().toString().padStart(2, '0')
      const minutes = date.getMinutes().toString().padStart(2, '0')
      return `${hours}:${minutes}`
    }
  }
}
</script>

<style scoped>
.chat-container {
  position: relative;
  background: linear-gradient(135deg, #f8fafc 0%, #eef2ff 100%);
  overflow: hidden;
}

/* 装饰性元素 */
.decorative-elements {
  position: absolute;
  inset: 0;
  z-index: 0;
  overflow: hidden;
  opacity: 0.7;
  pointer-events: none;
}

.circle-1 {
  position: absolute;
  width: 300px;
  height: 300px;
  border-radius: 50%;
  background: radial-gradient(circle, rgba(99, 102, 241, 0.1) 0%, rgba(99, 102, 241, 0) 70%);
  top: -100px;
  right: -50px;
}

.circle-2 {
  position: absolute;
  width: 500px;
  height: 500px;
  border-radius: 50%;
  background: radial-gradient(circle, rgba(79, 70, 229, 0.05) 0%, rgba(79, 70, 229, 0) 70%);
  bottom: -200px;
  left: -100px;
}

.circle-3 {
  position: absolute;
  width: 200px;
  height: 200px;
  border-radius: 50%;
  background: radial-gradient(circle, rgba(59, 130, 246, 0.1) 0%, rgba(59, 130, 246, 0) 70%);
  top: 40%;
  left: 60%;
}

.pattern-overlay {
  position: absolute;
  inset: 0;
  background-image: url("data:image/svg+xml,%3Csvg width='60' height='60' viewBox='0 0 60 60' xmlns='http://www.w3.org/2000/svg'%3E%3Cg fill='none' fill-rule='evenodd'%3E%3Cg fill='%236366f1' fill-opacity='0.03'%3E%3Cpath d='M36 34v-4h-2v4h-4v2h4v4h2v-4h4v-2h-4zm0-30V0h-2v4h-4v2h4v4h2V6h4V4h-4zM6 34v-4H4v4H0v2h4v4h2v-4h4v-2H6zM6 4V0H4v4H0v2h4v4h2V6h4V4H6z'/%3E%3C/g%3E%3C/g%3E%3C/svg%3E");
}

/* 欢迎界面动画 */
.welcome-ripple {
  position: absolute;
  inset: 0;
  border-radius: inherit;
  background: radial-gradient(circle, rgba(255,255,255,0.3) 0%, rgba(255,255,255,0) 70%);
  animation: ripple 3s ease-in-out infinite;
}

@keyframes ripple {
  0% {
    transform: scale(0.9);
    opacity: 0.6;
  }
  50% {
    transform: scale(1.1);
    opacity: 0;
  }
  100% {
    transform: scale(0.9);
    opacity: 0.6;
  }
}

/* 消息气泡效果 */
.ai-message, .user-message {
  transition: all 0.2s ease;
}

.ai-avatar-glow {
  background: linear-gradient(45deg, rgba(255,255,255,0.1), rgba(255,255,255,0.3));
  animation: pulse 2s ease-in-out infinite;
}

@keyframes pulse {
  0% {
    opacity: 0.2;
  }
  50% {
    opacity: 0.4;
  }
  100% {
    opacity: 0.2;
  }
}

/* 正在输入的动画 */
@keyframes typing {
  0%, 100% {
    transform: translateY(0);
    opacity: 0.5;
  }
  50% {
    transform: translateY(-5px);
    opacity: 1;
  }
}

.animate-typing {
  animation: typing 1.2s infinite ease-in-out;
}

.pulse-animation {
  animation: pulse-ring 1.5s cubic-bezier(0.215, 0.61, 0.355, 1) infinite;
}

@keyframes pulse-ring {
  0% {
    transform: scale(0.8);
    opacity: 0.4;
  }
  50% {
    transform: scale(1);
    opacity: 0.2;
  }
  100% {
    transform: scale(0.8);
    opacity: 0.4;
  }
}

/* 建议标签样式 */
.suggestion-tag {
  background: linear-gradient(135deg, #f0f9ff 0%, #e0f2fe 100%);
  color: #3b82f6;
}

.suggestion-tag:hover {
  background: linear-gradient(135deg, #dbeafe 0%, #bfdbfe 100%);
}

/* 自定义输入框样式 */
:deep(.chat-input .el-textarea__inner) {
  border: 1px solid #e2e8f0;
  border-radius: 1rem;
  padding: 14px 18px;
  line-height: 1.6;
  resize: none;
  box-shadow: 0 1px 2px 0 rgba(0, 0, 0, 0.05);
  transition: all 0.2s ease;
}

:deep(.chat-input .el-textarea__inner:focus) {
  border-color: #3b82f6;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.15);
}

:deep(.chat-input .el-textarea__inner:hover:not(:focus)) {
  border-color: #cbd5e1;
}

/* 滚动条样式 */
.overflow-y-auto::-webkit-scrollbar {
  width: 6px;
}

.overflow-y-auto::-webkit-scrollbar-track {
  background: rgba(241, 245, 249, 0.8);
  border-radius: 3px;
}

.overflow-y-auto::-webkit-scrollbar-thumb {
  background: #cbd5e1;
  border-radius: 3px;
}

.overflow-y-auto::-webkit-scrollbar-thumb:hover {
  background: #94a3b8;
}
</style>