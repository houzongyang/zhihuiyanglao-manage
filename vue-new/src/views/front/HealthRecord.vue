<template>
  <div class="health-record-container bg-gradient-to-br from-blue-50 to-gray-50 min-h-screen p-6">
    <div class="max-w-7xl mx-auto">
      <!-- 页面标题区域 - 使用更高级的设计 -->
      <div class="bg-white rounded-xl shadow-md mb-8 p-5 flex items-center justify-between">
        <div class="flex items-center">
          <div class="bg-gradient-to-r from-blue-600 to-indigo-600 rounded-lg p-3 shadow-lg mr-4">
            <i class="el-icon-document-checked text-3xl text-white"></i>
          </div>
          <div>
            <h1 class="text-2xl font-bold text-gray-800 mb-1">健康档案管理</h1>
            <p class="text-sm text-gray-500">全面记录并管理您的健康信息</p>
          </div>
        </div>
        <div class="flex space-x-3">
<!--          <el-button type="primary" icon="el-icon-plus" class="bg-gradient-to-r from-blue-600 to-indigo-600 border-0 shadow-md hover:shadow-lg" @click="openDialog">-->
<!--            更新健康记录-->
<!--          </el-button>-->
          <el-button type="success" icon="el-icon-data-analysis" class="bg-gradient-to-r from-green-600 to-emerald-600 border-0 shadow-md hover:shadow-lg" @click="generateHealthAssessment" :loading="assessmentLoading">
            {{ assessmentLoading ? '评估中...' : 'AI健康评估' }}
          </el-button>
        </div>
      </div>

      <!-- 健康档案卡片 - 增加更多细节和层次感 -->
      <div class="bg-white rounded-xl shadow-lg overflow-hidden mb-8 border border-gray-100">
        <div class="bg-gradient-to-r from-blue-50 to-indigo-50 p-6 border-b border-gray-200 relative">
          <div class="absolute top-0 right-0 h-full w-32 opacity-10 flex justify-center items-center">
            <i class="el-icon-user text-6xl text-blue-600"></i>
          </div>
          <div class="flex items-center justify-between relative z-10">
            <div class="flex items-center">
              <div class="bg-white rounded-full p-2 shadow-md mr-3">
                <i class="el-icon-user text-xl text-blue-600"></i>
              </div>
              <h2 class="text-xl font-semibold text-gray-800">基本健康信息</h2>
            </div>
            <el-tag type="success" class="text-sm shadow-sm">
              <i class="el-icon-time mr-1"></i>
              最近更新: {{ formatDate(healthRecord.updateTime) }}
            </el-tag>
          </div>
        </div>

        <div class="p-6" v-if="healthRecord.id">
          <el-descriptions border :column="3" class="w-full custom-descriptions">
            <el-descriptions-item label="血型" label-class-name="font-medium" content-class-name="text-gray-800">
              <el-tag type="danger" class="shadow-sm" effect="dark">{{ healthRecord.bloodType || '未知' }}</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="身高(cm)" label-class-name="font-medium" content-class-name="text-gray-800">
              <div class="flex items-center">
                <i class="el-icon-top text-blue-500 mr-1"></i>
                {{ healthRecord.height || '未记录' }}
              </div>
            </el-descriptions-item>
            <el-descriptions-item label="体重(kg)" label-class-name="font-medium" content-class-name="text-gray-800">
              <div class="flex items-center">
                <i class="el-icon-data-line text-green-500 mr-1"></i>
                {{ healthRecord.weight || '未记录' }}
              </div>
            </el-descriptions-item>
            <el-descriptions-item label="慢性疾病" :span="3" label-class-name="font-medium" content-class-name="text-gray-800">
              <template v-if="healthRecord.chronicDisease">
                <el-tag v-for="(disease, index) in healthRecord.chronicDisease.split(',')" :key="index"
                  type="warning" class="mr-2 mb-2 shadow-sm" effect="light">
                  <i class="el-icon-warning-outline mr-1"></i>
                  {{ disease }}
                </el-tag>
              </template>
              <span v-else class="text-gray-500 flex items-center">
                <i class="el-icon-check text-green-500 mr-1"></i>无
              </span>
            </el-descriptions-item>
            <el-descriptions-item label="紧急联系人" label-class-name="font-medium" content-class-name="text-gray-800">
              <div class="flex items-center">
                <i class="el-icon-user text-indigo-500 mr-1"></i>
                {{ healthRecord.emergencyContact || '未设置' }}
              </div>
            </el-descriptions-item>
            <el-descriptions-item label="紧急联系电话" :span="2" label-class-name="font-medium" content-class-name="text-gray-800">
              <div class="flex items-center">
                <i class="el-icon-phone text-indigo-500 mr-1"></i>
                {{ healthRecord.emergencyPhone || '未设置' }}
              </div>
            </el-descriptions-item>
          </el-descriptions>
        </div>
        <div class="p-6 flex justify-center items-center h-40 text-gray-500" v-else>
          <div class="text-center bg-blue-50 p-6 rounded-lg shadow-inner w-full max-w-lg">
            <i class="el-icon-warning-outline text-4xl mb-3 text-amber-500"></i>
            <p class="text-gray-600">您还没有创建健康档案，请点击"更新健康记录"按钮创建</p>
          </div>
        </div>
      </div>

      <!-- AI健康评估报告 -->
      <transition name="health-assessment">
        <div v-if="healthAssessment" class="bg-white rounded-xl shadow-lg overflow-hidden mb-8 border border-gray-100">
          <div class="bg-gradient-to-r from-emerald-500 to-green-600 p-6 border-b border-gray-200 relative">
            <div class="absolute top-0 right-0 h-full w-32 opacity-10 flex justify-center items-center">
              <i class="el-icon-data-analysis text-6xl text-white"></i>
            </div>
            <div class="flex items-center justify-between relative z-10">
              <div class="flex items-center">
                <div class="bg-white rounded-full p-2 shadow-md mr-3">
                  <i class="el-icon-data-analysis text-xl text-emerald-600"></i>
                </div>
                <h2 class="text-xl font-semibold text-white">AI健康评估报告</h2>
              </div>
              <div class="flex items-center space-x-2">
                <el-tag type="info" class="text-sm shadow-sm bg-white text-emerald-600">
                  <i class="el-icon-time mr-1"></i>
                  评估时间: {{ formatDate(healthAssessment.assessmentTime) }}
                </el-tag>
                <el-button 
                  type="text" 
                  icon="el-icon-refresh-right" 
                  class="text-white hover:bg-white/20 p-2"
                  @click="generateHealthAssessment"
                  :loading="assessmentLoading"
                  title="重新生成评估">
                </el-button>
              </div>
            </div>
          </div>
          
          <div class="p-6">
            <div class="health-assessment-content" v-html="formatAssessmentContent(healthAssessment.aiAssessment)"></div>
          </div>
        </div>
      </transition>

      <!-- 详细健康记录 - 改进卡片设计和图标使用 -->
      <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <!-- 病史记录 -->
        <div class="bg-white rounded-xl shadow-lg overflow-hidden transform transition-all hover:shadow-xl border border-gray-100">
          <div class="bg-gradient-to-r from-blue-500 to-blue-600 p-4 flex items-center text-white">
            <div class="bg-white/30 rounded-full p-2 mr-3">
              <i class="el-icon-notebook-2 text-lg text-white"></i>
            </div>
            <h3 class="text-lg font-medium">病史记录</h3>
          </div>
          <div class="p-6 min-h-40">
            <div v-if="healthRecord.medicalHistory" class="text-gray-700 whitespace-pre-line bg-gray-50 p-3 rounded-lg border border-gray-100">
              {{ healthRecord.medicalHistory }}
            </div>
            <div v-else class="text-gray-500 flex justify-center items-center h-32 bg-gray-50 rounded-lg border border-gray-100">
              <div class="text-center">
                <i class="el-icon-document text-3xl text-gray-300 mb-2"></i>
                <p>暂无病史记录</p>
              </div>
            </div>
          </div>
        </div>

        <!-- 药物过敏史 -->
        <div class="bg-white rounded-xl shadow-lg overflow-hidden transform transition-all hover:shadow-xl border border-gray-100">
          <div class="bg-gradient-to-r from-red-500 to-red-600 p-4 flex items-center text-white">
            <div class="bg-white/30 rounded-full p-2 mr-3">
              <i class="el-icon-warning text-lg text-white"></i>
            </div>
            <h3 class="text-lg font-medium">药物过敏史</h3>
          </div>
          <div class="p-6 min-h-40">
            <div v-if="healthRecord.drugAllergies" class="text-gray-700 whitespace-pre-line bg-gray-50 p-3 rounded-lg border border-gray-100">
              {{ healthRecord.drugAllergies }}
            </div>
            <div v-else class="text-gray-500 flex justify-center items-center h-32 bg-gray-50 rounded-lg border border-gray-100">
              <div class="text-center">
                <i class="el-icon-circle-check text-3xl text-gray-300 mb-2"></i>
                <p>暂无药物过敏记录</p>
              </div>
            </div>
          </div>
        </div>

        <!-- 常规体检记录 -->
        <div class="bg-white rounded-xl shadow-lg overflow-hidden transform transition-all hover:shadow-xl border border-gray-100">
          <div class="bg-gradient-to-r from-green-500 to-green-600 p-4 flex items-center text-white">
            <div class="bg-white/30 rounded-full p-2 mr-3">
              <i class="el-icon-data-analysis text-lg text-white"></i>
            </div>
            <h3 class="text-lg font-medium">常规体检记录</h3>
          </div>
          <div class="p-6 min-h-40">
            <div v-if="healthRecord.physicalExamination" class="text-gray-700 whitespace-pre-line bg-gray-50 p-3 rounded-lg border border-gray-100">
              {{ healthRecord.physicalExamination }}
            </div>
            <div v-else class="text-gray-500 flex justify-center items-center h-32 bg-gray-50 rounded-lg border border-gray-100">
              <div class="text-center">
                <i class="el-icon-data-line text-3xl text-gray-300 mb-2"></i>
                <p>暂无体检记录</p>
              </div>
            </div>
          </div>
        </div>

        <!-- 病历记录 -->
        <div class="bg-white rounded-xl shadow-lg overflow-hidden transform transition-all hover:shadow-xl border border-gray-100">
          <div class="bg-gradient-to-r from-purple-500 to-purple-600 p-4 flex items-center text-white">
            <div class="bg-white/30 rounded-full p-2 mr-3">
              <i class="el-icon-document text-lg text-white"></i>
            </div>
            <h3 class="text-lg font-medium">病历记录</h3>
          </div>
          <div class="p-6 min-h-40">
            <div v-if="healthRecord.medicalRecords" class="text-gray-700 whitespace-pre-line bg-gray-50 p-3 rounded-lg border border-gray-100">
              {{ healthRecord.medicalRecords }}
            </div>
            <div v-else class="text-gray-500 flex justify-center items-center h-32 bg-gray-50 rounded-lg border border-gray-100">
              <div class="text-center">
                <i class="el-icon-collection text-3xl text-gray-300 mb-2"></i>
                <p>暂无病历记录</p>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 健康档案更新对话框 - 美化表单 -->
      <el-dialog 
        :title="更新对话框"
        :visible.sync="dialogVisible" 
        width="70%" 
        :close-on-click-modal="false"
        custom-class="health-record-dialog"
      >
        <div class="p-4 bg-gradient-to-r from-blue-500 to-indigo-600 rounded-t-lg text-white mb-6 -mt-6 -mx-4 flex items-center">
          <i class="el-icon-edit-outline text-2xl mr-3"></i>
          <span class="text-xl font-medium">更新健康档案</span>
        </div>

        <el-form :model="form" label-width="120px" label-position="right" class="health-form">
          <div class="grid grid-cols-1 md:grid-cols-2 gap-4 mb-4">
            <el-form-item label="血型" class="form-item-with-icon">
              <i class="el-icon-cherry form-item-icon text-red-500"></i>
              <el-select v-model="form.bloodType" placeholder="请选择血型" class="w-full">
                <el-option label="A" value="A"></el-option>
                <el-option label="B" value="B"></el-option>
                <el-option label="O" value="O"></el-option>
                <el-option label="AB" value="AB"></el-option>
                <el-option label="未知" value="未知"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="身高(cm)" class="form-item-with-icon">
              <i class="el-icon-top form-item-icon text-blue-500"></i>
              <el-input-number v-model="form.height" :precision="2" :step="0.01" :min="0" class="w-full" controls-position="right"></el-input-number>
            </el-form-item>
            <el-form-item label="体重(kg)" class="form-item-with-icon">
              <i class="el-icon-data-line form-item-icon text-green-500"></i>
              <el-input-number v-model="form.weight" :precision="2" :step="0.01" :min="0" class="w-full" controls-position="right"></el-input-number>
            </el-form-item>
            <el-form-item label="慢性疾病" class="form-item-with-icon">
              <i class="el-icon-warning-outline form-item-icon text-amber-500"></i>
              <el-input v-model="form.chronicDisease" placeholder="多个疾病请用逗号分隔"></el-input>
            </el-form-item>
            <el-form-item label="紧急联系人" class="form-item-with-icon">
              <i class="el-icon-user form-item-icon text-indigo-500"></i>
              <el-input v-model="form.emergencyContact" placeholder="请输入紧急联系人姓名"></el-input>
            </el-form-item>
            <el-form-item label="紧急联系电话" class="form-item-with-icon">
              <i class="el-icon-phone form-item-icon text-indigo-500"></i>
              <el-input v-model="form.emergencyPhone" placeholder="请输入紧急联系人电话"></el-input>
            </el-form-item>
          </div>

          <div class="bg-gray-50 p-4 rounded-lg mb-4">
            <h3 class="text-lg font-medium text-gray-800 mb-4 flex items-center">
              <i class="el-icon-document text-blue-600 mr-2"></i>
              详细健康记录
            </h3>
            
            <el-form-item label="病史记录" class="form-item-with-icon">
              <i class="el-icon-notebook-2 form-item-icon text-blue-500"></i>
              <el-input type="textarea" v-model="form.medicalHistory" :rows="4" placeholder="请详细描述您的病史"></el-input>
            </el-form-item>
            <el-form-item label="药物过敏史" class="form-item-with-icon">
              <i class="el-icon-warning form-item-icon text-red-500"></i>
              <el-input type="textarea" v-model="form.drugAllergies" :rows="4" placeholder="请详细描述您的药物过敏情况"></el-input>
            </el-form-item>
            <el-form-item label="常规体检记录" class="form-item-with-icon">
              <i class="el-icon-data-analysis form-item-icon text-green-500"></i>
              <el-input type="textarea" v-model="form.physicalExamination" :rows="4" placeholder="请填写您的体检记录"></el-input>
            </el-form-item>
            <el-form-item label="病历记录" class="form-item-with-icon">
              <i class="el-icon-document form-item-icon text-purple-500"></i>
              <el-input type="textarea" v-model="form.medicalRecords" :rows="4" placeholder="请填写您的病历记录"></el-input>
            </el-form-item>
          </div>
        </el-form>
        
        <span slot="footer" class="dialog-footer">
          <el-button @click="dialogVisible = false" class="border-gray-300 hover:bg-gray-100">取 消</el-button>
          <el-button 
            type="primary" 
            @click="saveHealthRecord" 
            class="bg-gradient-to-r from-blue-600 to-indigo-600 border-0 shadow-md hover:shadow-lg">
            保存档案
          </el-button>
        </span>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import request from '@/utils/request'

export default {
  name: 'HealthRecord',
  data() {
    return {
      user: JSON.parse(localStorage.getItem("xm-user") || "{}"),
      healthRecord: {},
      dialogVisible: false,
      healthAssessment: null,
      assessmentLoading: false,
      form: {
        bloodType: '',
        height: null,
        weight: null,
        chronicDisease: '',
        emergencyContact: '',
        emergencyPhone: '',
        medicalHistory: '',
        drugAllergies: '',
        physicalExamination: '',
        medicalRecords: ''
      }
    }
  },
  created() {
    if (this.user.id) {
      this.fetchHealthRecord()
    }
  },
  methods: {
    fetchHealthRecord() {
      console.log('获取健康档案...')
      // 使用request发起请求
      request.get('/health-record/selectByUserId/' + this.user.id).then(res => {
        if (res.code === '200') {
          this.healthRecord = res.data || {}
        } else {
          this.$message.error(res.msg || '获取健康档案失败')
        }
      })
    },
    openDialog() {
      this.form = { ...this.healthRecord }
      this.dialogVisible = true
    },
    saveHealthRecord() {
      console.log('保存健康档案...', this.form)
      // 设置用户ID
      this.form.userId = this.user.id
      // 发起请求保存数据
      request.post('/health-record/save', this.form).then(res => {
        if (res.code === '200') {
          this.$message.success('保存成功')
          this.dialogVisible = false
          this.fetchHealthRecord()
        } else {
          this.$message.error(res.msg || '保存失败')
        }
      })
    },
    formatDate(dateStr) {
      if (!dateStr) return '未知'
      const date = new Date(dateStr)
      return date.toLocaleDateString() + ' ' + date.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' })
    },
    
    // 生成AI健康评估报告
    async generateHealthAssessment() {
      if (!this.user.id) {
        this.$message.error('请先登录')
        return
      }
      
      if (!this.healthRecord.id) {
        this.$message.error('请先完善健康档案信息')
        return
      }
      
      this.assessmentLoading = true
      try {
        const response = await request.get(`/ai/healthAssessment?userId=${this.user.id}`)
        if (response.code === '200') {
          this.healthAssessment = response.data
          this.$message.success('健康评估报告生成成功！')
          // 滚动到评估报告区域
          this.$nextTick(() => {
            const assessmentElement = document.querySelector('.health-assessment-content')
            if (assessmentElement) {
              assessmentElement.scrollIntoView({ behavior: 'smooth', block: 'start' })
            }
          })
        } else {
          this.$message.error(response.msg || '生成健康评估报告失败')
        }
      } catch (error) {
        console.error('生成健康评估报告时发生错误:', error)
        this.$message.error('健康评估服务暂时不可用，请稍后重试')
      } finally {
        this.assessmentLoading = false
      }
    },
    
    // 格式化AI评估内容，将文本转换为HTML格式
    formatAssessmentContent(content) {
      if (!content) return ''
      
      // 将【】标记的部分转换为标题格式
      let formattedContent = content.replace(/【([^】]+)】/g, '<h3 class="text-lg font-semibold text-gray-800 mb-3 mt-6 flex items-center"><i class="el-icon-right text-emerald-600 mr-2"></i>$1</h3>')
      
      // 将换行符转换为HTML换行
      formattedContent = formattedContent.replace(/\n/g, '<br>')
      
      // 处理段落
      formattedContent = formattedContent.replace(/(<br>){2,}/g, '</p><p class="text-gray-700 leading-relaxed mb-4">')
      
      // 包装在段落标签中
      formattedContent = '<div class="assessment-content"><p class="text-gray-700 leading-relaxed mb-4">' + formattedContent + '</p></div>'
      
      // 处理空段落
      formattedContent = formattedContent.replace(/<p class="text-gray-700 leading-relaxed mb-4"><\/p>/g, '')
      
      return formattedContent
    }
  }
}
</script>

<style scoped>
/* 使用TailwindCSS的样式，补充一些Element UI的自定义样式 */
:deep(.el-descriptions__label) {
  @apply text-gray-600;
}

:deep(.el-descriptions__content) {
  @apply text-gray-800;
}

:deep(.custom-descriptions) {
  border-radius: 0.5rem;
  overflow: hidden;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
}

:deep(.custom-descriptions .el-descriptions-item__label) {
  background-color: #f8fafc;
  padding: 12px 15px;
}

:deep(.custom-descriptions .el-descriptions-item__content) {
  padding: 12px 15px;
}

/* 表单项带图标的样式 */
.form-item-with-icon {
  position: relative;
}

.form-item-icon {
  position: absolute;
  left: 10px;
  top: 13px;
  z-index: 1;
}

.form-item-with-icon :deep(.el-input__inner),
.form-item-with-icon :deep(.el-textarea__inner) {
  padding-left: 35px;
}

.form-item-with-icon :deep(.el-input-number__decrease),
.form-item-with-icon :deep(.el-input-number__increase) {
  background-color: #f9fafb;
}

/* 对话框样式自定义 */
:deep(.health-record-dialog) {
  border-radius: 0.75rem;
  overflow: hidden;
}

:deep(.health-record-dialog .el-dialog__header) {
  padding: 0;
}

:deep(.health-record-dialog .el-dialog__headerbtn) {
  top: 16px;
  right: 16px;
  z-index: 10;
}

:deep(.health-record-dialog .el-dialog__headerbtn .el-dialog__close) {
  color: white;
  font-weight: bold;
}

/* 添加一些过渡效果 */
.health-record-container {
  transition: all 0.3s ease;
}

.health-record-container .bg-white {
  transition: all 0.3s ease;
}

.health-record-container .bg-white:hover {
  box-shadow: 0 15px 30px -10px rgba(0, 0, 0, 0.1);
}

/* 自定义元素样式 */
:deep(.el-tag) {
  border-radius: 4px;
  font-weight: 500;
}

:deep(.el-button) {
  border-radius: 6px;
  font-weight: 500;
}

:deep(.el-input__inner), 
:deep(.el-textarea__inner) {
  border-radius: 6px;
  transition: all 0.2s ease;
}

:deep(.el-input__inner:focus), 
:deep(.el-textarea__inner:focus) {
  box-shadow: 0 0 0 2px rgba(59, 130, 246, 0.3);
}

:deep(.el-textarea__inner) {
  padding: 10px;
  line-height: 1.5;
}

/* 健康评估内容样式 */
:deep(.health-assessment-content) {
  line-height: 1.7;
}

:deep(.health-assessment-content h3) {
  color: #374151;
  border-bottom: 2px solid #10b981;
  padding-bottom: 8px;
  margin-bottom: 16px !important;
}

:deep(.health-assessment-content .assessment-content) {
  background: linear-gradient(135deg, #f0f9ff 0%, #e0f2fe 100%);
  padding: 20px;
  border-radius: 12px;
  border-left: 4px solid #10b981;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

:deep(.health-assessment-content p) {
  margin-bottom: 12px;
  text-indent: 0;
}

:deep(.health-assessment-content h3:first-child) {
  margin-top: 0 !important;
}

/* 健康评估卡片动画效果 */
.health-assessment-enter-active {
  transition: all 0.5s ease;
}

.health-assessment-enter {
  opacity: 0;
  transform: translateY(30px);
}

.health-assessment-enter-to {
  opacity: 1;
  transform: translateY(0);
}

/* 媒体查询 - 移动端优化 */
@media (max-width: 640px) {
  .health-record-container {
    padding: 1rem;
  }
  
  :deep(.health-assessment-content .assessment-content) {
    padding: 15px;
  }
  
  :deep(.health-assessment-content h3) {
    font-size: 1rem;
  }
}
</style> 