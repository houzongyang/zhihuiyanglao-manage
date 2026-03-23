<template>
  <div class="nurse-team-container">
    <!-- 页面头部 -->
    <div class="bg-gradient-to-r from-blue-500 via-purple-500 to-indigo-600 text-white rounded-2xl p-8 mb-8 shadow-xl">
      <div class="text-center">
        <div class="w-20 h-20 bg-white/20 rounded-full flex items-center justify-center mx-auto mb-4">
          <i class="el-icon-user-solid text-4xl text-white"></i>
        </div>
        <h1 class="text-3xl font-bold mb-2">我们的护理团队</h1>
        <p class="text-blue-100 text-lg">专业、贴心、值得信赖的护理服务</p>
      </div>
    </div>

    <!-- 团队统计 -->
   

    <!-- 搜索和筛选 -->
    <div class="bg-white rounded-2xl p-6 mb-8 shadow-lg">
      <div class="flex flex-wrap items-center gap-4">
        <div class="flex items-center">
          <i class="el-icon-search text-gray-400 mr-2"></i>
          <el-input
            v-model="searchForm.name"
            placeholder="搜索护士姓名"
            prefix-icon="el-icon-search"
            clearable
            size="small"
            class="w-48"
            @input="handleSearch"
          />
        </div>
        <el-select 
          v-model="searchForm.department" 
          placeholder="选择科室" 
          clearable 
          size="small"
          @change="handleSearch"
          class="w-40"
        >
          <el-option label="全部" value=""></el-option>
          <el-option label="内科" value="内科"></el-option>
          <el-option label="外科" value="外科"></el-option>
          <el-option label="儿科" value="儿科"></el-option>
          <el-option label="妇科" value="妇科"></el-option>
          <el-option label="急诊科" value="急诊科"></el-option>
          <el-option label="康复科" value="康复科"></el-option>
          <el-option label="老年科" value="老年科"></el-option>
        </el-select>
        <el-select 
          v-model="searchForm.position" 
          placeholder="选择职位" 
          clearable 
          size="small"
          @change="handleSearch"
          class="w-40"
        >
          <el-option label="全部" value=""></el-option>
          <el-option label="护士长" value="护士长"></el-option>
          <el-option label="主管护师" value="主管护师"></el-option>
          <el-option label="护师" value="护师"></el-option>
          <el-option label="护士" value="护士"></el-option>
        </el-select>
        <el-button 
          type="primary" 
          size="small" 
          @click="resetSearch"
          icon="el-icon-refresh"
        >
          重置
        </el-button>
      </div>
    </div>

    <!-- 护士卡片列表 -->
    <div v-loading="loading" class="min-h-[400px]">
      <div v-if="filteredNurses.length === 0 && !loading" class="text-center py-20">
        <div class="w-32 h-32 bg-gray-100 rounded-full flex items-center justify-center mx-auto mb-6">
          <i class="el-icon-user-solid text-4xl text-gray-400"></i>
        </div>
        <h3 class="text-xl font-medium text-gray-600 mb-2">暂无护士信息</h3>
        <p class="text-gray-400">当前没有找到符合条件的护士</p>
      </div>

      <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        <div
          v-for="nurse in paginatedNurses"
          :key="nurse.id"
          class="bg-white rounded-2xl shadow-lg hover:shadow-xl transition-all duration-300 overflow-hidden border border-gray-100 hover:border-blue-200"
        >
          <!-- 护士头部信息 -->
          <div class="relative bg-gradient-to-br from-blue-50 to-indigo-100 p-6">
            <div class="flex items-center">
              <div class="w-16 h-16 bg-gradient-to-br from-blue-400 to-indigo-500 rounded-full flex items-center justify-center text-white text-xl font-bold mr-4 shadow-lg">
                {{ nurse.name ? nurse.name.charAt(0) : 'N' }}
              </div>
              <div>
                <h3 class="text-xl font-bold text-gray-800 mb-1">{{ nurse.name }}</h3>
                <div class="flex items-center">
                  <el-tag :type="getPositionType(nurse.position)" size="mini" class="mr-2">
                    {{ nurse.position }}
                  </el-tag>
                  <el-tag type="info" size="mini">{{ nurse.department }}</el-tag>
                </div>
              </div>
            </div>
            <div class="absolute top-4 right-4">
              <el-tag :type="nurse.status === '在职' ? 'success' : 'info'" size="mini">
                {{ nurse.status || '在职' }}
              </el-tag>
            </div>
          </div>

          <!-- 护士详细信息 -->
          <div class="p-6">
            <div class="space-y-3">
              <div class="flex items-center text-gray-600">
                <i class="el-icon-phone text-blue-500 w-5 mr-3"></i>
                <span>{{ nurse.phone || '暂无联系方式' }}</span>
              </div>
              <div class="flex items-center text-gray-600">
                <i class="el-icon-medal text-green-500 w-5 mr-3"></i>
                <span>工作经验：{{ nurse.workYears ? nurse.workYears + '年' : '暂无' }}</span>
              </div>
              <div class="flex items-center text-gray-600">
                <i class="el-icon-time text-orange-500 w-5 mr-3"></i>
                <span>工作时间：{{ nurse.workSchedule || '暂无' }}</span>
              </div>
            </div>

            <!-- 专业特长 -->
            <div v-if="nurse.specialty" class="mt-4">
              <div class="text-sm font-medium text-gray-700 mb-2">专业特长</div>
              <p class="text-gray-600 text-sm bg-gray-50 p-3 rounded-lg line-clamp-2">
                {{ nurse.specialty }}
              </p>
            </div>

            <!-- 个人简介 -->
            <div v-if="nurse.introduction" class="mt-4">
              <div class="text-sm font-medium text-gray-700 mb-2">个人简介</div>
              <p class="text-gray-600 text-sm bg-gray-50 p-3 rounded-lg line-clamp-3">
                {{ nurse.introduction }}
              </p>
            </div>

            <!-- 操作按钮 -->
            <div class="mt-6 pt-4 border-t border-gray-100">
              <el-button 
                type="primary" 
                size="small" 
                @click="viewDetail(nurse)"
                class="w-full"
                plain
              >
                <i class="el-icon-view mr-1"></i>
                查看详情
              </el-button>
            </div>
          </div>
        </div>
      </div>

      <!-- 分页 -->
      <div v-if="filteredNurses.length > 0" class="flex justify-center mt-8">
        <el-pagination
          @current-change="handlePageChange"
          :current-page="currentPage"
          :page-size="pageSize"
          layout="prev, pager, next"
          :total="filteredNurses.length"
          background
        ></el-pagination>
      </div>
    </div>

    <!-- 护士详情对话框 -->
    <el-dialog
      title="护士详情"
      :visible.sync="detailDialogVisible"
      width="600px"
      :close-on-click-modal="false"
    >
      <div v-if="selectedNurse" class="space-y-6">
        <!-- 基本信息 -->
        <div class="text-center pb-6 border-b border-gray-200">
          <div class="w-20 h-20 bg-gradient-to-br from-blue-400 to-indigo-500 rounded-full flex items-center justify-center text-white text-2xl font-bold mx-auto mb-4 shadow-lg">
            {{ selectedNurse.name ? selectedNurse.name.charAt(0) : 'N' }}
          </div>
          <h3 class="text-2xl font-bold text-gray-800 mb-2">{{ selectedNurse.name }}</h3>
          <div class="flex justify-center space-x-2">
            <el-tag :type="getPositionType(selectedNurse.position)">{{ selectedNurse.position }}</el-tag>
            <el-tag type="info">{{ selectedNurse.department }}</el-tag>
            <el-tag :type="selectedNurse.status === '在职' ? 'success' : 'info'">
              {{ selectedNurse.status || '在职' }}
            </el-tag>
          </div>
        </div>

        <!-- 详细信息 -->
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div class="flex items-center">
            <i class="el-icon-phone text-blue-500 w-6 mr-3"></i>
            <div>
              <div class="text-sm text-gray-500">联系电话</div>
              <div class="font-medium">{{ selectedNurse.phone || '暂无' }}</div>
            </div>
          </div>
          <div class="flex items-center">
            <i class="el-icon-message text-blue-500 w-6 mr-3"></i>
            <div>
              <div class="text-sm text-gray-500">邮箱地址</div>
              <div class="font-medium">{{ selectedNurse.email || '暂无' }}</div>
            </div>
          </div>
          <div class="flex items-center">
            <i class="el-icon-medal text-green-500 w-6 mr-3"></i>
            <div>
              <div class="text-sm text-gray-500">工作经验</div>
              <div class="font-medium">{{ selectedNurse.workYears ? selectedNurse.workYears + '年' : '暂无' }}</div>
            </div>
          </div>
          <div class="flex items-center">
            <i class="el-icon-time text-orange-500 w-6 mr-3"></i>
            <div>
              <div class="text-sm text-gray-500">工作时间</div>
              <div class="font-medium">{{ selectedNurse.workSchedule || '暂无' }}</div>
            </div>
          </div>
        </div>

        <!-- 专业特长 -->
        <div v-if="selectedNurse.specialty" class="bg-blue-50 p-4 rounded-lg">
          <h4 class="font-medium text-gray-800 mb-2 flex items-center">
            <i class="el-icon-star-on text-blue-500 mr-2"></i>
            专业特长
          </h4>
          <p class="text-gray-700">{{ selectedNurse.specialty }}</p>
        </div>

        <!-- 个人简介 -->
        <div v-if="selectedNurse.introduction" class="bg-gray-50 p-4 rounded-lg">
          <h4 class="font-medium text-gray-800 mb-2 flex items-center">
            <i class="el-icon-user text-gray-500 mr-2"></i>
            个人简介
          </h4>
          <p class="text-gray-700">{{ selectedNurse.introduction }}</p>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import request from '@/utils/request'

export default {
  name: 'NurseTeam',
  data() {
    return {
      loading: false,
      nurses: [],
      filteredNurses: [],
      currentPage: 1,
      pageSize: 9,
      searchForm: {
        name: '',
        department: '',
        position: ''
      },
      detailDialogVisible: false,
      selectedNurse: null
    }
  },
  computed: {
    // 总护士数
    totalNurses() {
      return this.nurses.length
    },
    // 高级护师数量
    seniorNurses() {
      return this.nurses.filter(nurse => 
        nurse.position === '护士长' || nurse.position === '主管护师'
      ).length
    },
    // 平均工作经验
    avgExperience() {
      const totalYears = this.nurses.reduce((sum, nurse) => sum + (nurse.workYears || 0), 0)
      return this.nurses.length > 0 ? Math.round(totalYears / this.nurses.length) : 0
    },
    // 当前页护士列表
    paginatedNurses() {
      const start = (this.currentPage - 1) * this.pageSize
      const end = start + this.pageSize
      return this.filteredNurses.slice(start, end)
    }
  },
  created() {
    this.loadNurses()
  },
  methods: {
    // 加载护士数据
    async loadNurses() {
      try {
        this.loading = true
        const res = await request.get('/nurse/selectAll')
        if (res.code === '200') {
          this.nurses = res.data || []
          this.filteredNurses = [...this.nurses]
        } else {
          this.$message.error('加载护士信息失败')
        }
      } catch (error) {
        this.$message.error('加载护士信息异常')
      } finally {
        this.loading = false
      }
    },

    // 搜索和筛选
    handleSearch() {
      this.filteredNurses = this.nurses.filter(nurse => {
        const nameMatch = !this.searchForm.name || 
          nurse.name.toLowerCase().includes(this.searchForm.name.toLowerCase())
        const departmentMatch = !this.searchForm.department || 
          nurse.department === this.searchForm.department
        const positionMatch = !this.searchForm.position || 
          nurse.position === this.searchForm.position
        return nameMatch && departmentMatch && positionMatch
      })
      this.currentPage = 1
    },

    // 重置搜索
    resetSearch() {
      this.searchForm = {
        name: '',
        department: '',
        position: ''
      }
      this.filteredNurses = [...this.nurses]
      this.currentPage = 1
    },

    // 查看详情
    viewDetail(nurse) {
      this.selectedNurse = nurse
      this.detailDialogVisible = true
    },

    // 分页改变
    handlePageChange(page) {
      this.currentPage = page
    },

    // 获取职位类型
    getPositionType(position) {
      const typeMap = {
        '护士长': 'danger',
        '主管护师': 'warning',
        '护师': 'success',
        '护士': 'primary',
        '实习护士': 'info'
      }
      return typeMap[position] || 'primary'
    }
  }
}
</script>

<style scoped>
.nurse-team-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.grid {
  display: grid;
}

.grid-cols-1 {
  grid-template-columns: repeat(1, minmax(0, 1fr));
}

.grid-cols-2 {
  grid-template-columns: repeat(2, minmax(0, 1fr));
}

.grid-cols-3 {
  grid-template-columns: repeat(3, minmax(0, 1fr));
}

.grid-cols-4 {
  grid-template-columns: repeat(4, minmax(0, 1fr));
}

.gap-4 {
  gap: 1rem;
}

.gap-6 {
  gap: 1.5rem;
}

.space-y-3 > * + * {
  margin-top: 0.75rem;
}

.space-y-4 > * + * {
  margin-top: 1rem;
}

.space-y-6 > * + * {
  margin-top: 1.5rem;
}

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

@media (min-width: 768px) {
  .md\:grid-cols-2 {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
  
  .md\:grid-cols-4 {
    grid-template-columns: repeat(4, minmax(0, 1fr));
  }
}

@media (min-width: 1024px) {
  .lg\:grid-cols-3 {
    grid-template-columns: repeat(3, minmax(0, 1fr));
  }
}

/* 卡片悬浮效果 */
.bg-white:hover {
  transform: translateY(-2px);
}

/* 渐变背景 */
.bg-gradient-to-r {
  background: linear-gradient(to right, var(--tw-gradient-stops));
}

.bg-gradient-to-br {
  background: linear-gradient(to bottom right, var(--tw-gradient-stops));
}

.from-blue-500 {
  --tw-gradient-from: #3b82f6;
  --tw-gradient-stops: var(--tw-gradient-from), var(--tw-gradient-to, rgb(59 130 246 / 0));
}

.via-purple-500 {
  --tw-gradient-stops: var(--tw-gradient-from), #8b5cf6, var(--tw-gradient-to, rgb(139 92 246 / 0));
}

.to-indigo-600 {
  --tw-gradient-to: #4f46e5;
}
</style> 