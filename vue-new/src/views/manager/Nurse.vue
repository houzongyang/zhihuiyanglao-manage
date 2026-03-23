<template>
  <div>
    <!-- 功能说明 -->
    <div class="bg-white rounded-lg shadow-sm p-6 mb-5">
      <div class="flex items-center">
        <i class="el-icon-user-solid text-3xl text-blue-500 mr-3"></i>
        <div>
          <h2 class="text-xl font-bold text-gray-800 mb-1">护士管理</h2>
          <p class="text-gray-600">本模块用于管理系统护士信息，提供护士信息的增加、修改、删除和查询功能</p>
        </div>
      </div>
    </div>

    <!-- 搜索和操作区域 -->
    <div class="bg-white rounded-lg shadow-sm p-6 mb-5">
      <div class="flex flex-wrap items-center justify-between">
        <div class="w-full lg:w-auto mb-4 lg:mb-0">
          <el-form :inline="true" :model="searchForm" ref="searchForm">
            <el-form-item>
              <el-input 
                v-model="searchForm.name" 
                placeholder="请输入护士姓名" 
                prefix-icon="el-icon-s-custom" 
                clearable
                size="small"
              ></el-input>
            </el-form-item>
            <el-form-item>
              <el-input 
                v-model="searchForm.phone" 
                placeholder="请输入联系电话" 
                prefix-icon="el-icon-phone" 
                clearable
                size="small"
              ></el-input>
            </el-form-item>
            <el-form-item>
              <el-select v-model="searchForm.department" placeholder="请选择科室" clearable size="small">
                <el-option label="内科" value="内科"></el-option>
                <el-option label="外科" value="外科"></el-option>
                <el-option label="儿科" value="儿科"></el-option>
                <el-option label="妇科" value="妇科"></el-option>
                <el-option label="急诊科" value="急诊科"></el-option>
                <el-option label="康复科" value="康复科"></el-option>
                <el-option label="老年科" value="老年科"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSearch" icon="el-icon-search" size="small" class="mr-2">查询</el-button>
              <el-button @click="resetSearch" icon="el-icon-refresh" size="small" class="mr-2">重置</el-button>
              <el-button type="success" @click="queryAllNurses" icon="el-icon-menu" size="small">查询全部</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div class="w-full lg:w-auto flex justify-end space-x-3">
          <el-button type="primary" @click="handleAdd" icon="el-icon-plus" size="small">添加护士</el-button>
          <el-button type="danger" @click="handleBatchDelete" :disabled="selection.length === 0" icon="el-icon-delete" size="small">
            批量删除
          </el-button>
        </div>
      </div>
    </div>

    <!-- 护士信息表格 -->
    <div class="bg-white rounded-lg shadow-sm p-6">
      <el-table 
        :data="tableData" 
        stripe 
        @selection-change="handleSelectionChange"
        v-loading="loading"
        style="width: 100%"
        :header-cell-style="{ backgroundColor: '#f5f7fa', color: '#606266' }"
      >
        <el-table-column type="selection" width="50" align="center"></el-table-column>
        <el-table-column prop="id" label="ID" width="80" align="center"></el-table-column>
        <el-table-column prop="name" label="姓名" width="120" align="center"></el-table-column>
        <el-table-column prop="phone" label="联系电话" width="130" align="center"></el-table-column>
        <el-table-column prop="email" label="邮箱" width="180" align="center"></el-table-column>
        <el-table-column prop="department" label="科室" width="120" align="center"></el-table-column>
        <el-table-column prop="position" label="职位" width="120" align="center"></el-table-column>
        <el-table-column prop="workYears" label="工作年限" width="100" align="center">
          <template slot-scope="scope">
            {{ scope.row.workYears ? scope.row.workYears + '年' : '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="specialty" label="专业特长" width="150" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="workSchedule" label="工作时间" width="120" align="center"></el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === '在职' ? 'success' : 'info'" size="mini">
              {{ scope.row.status || '在职' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="primary"
              plain
              @click="handleView(scope.row)"
              icon="el-icon-view"
            >查看</el-button>
            <el-button
              size="mini"
              type="warning"
              plain
              @click="handleEdit(scope.row)"
              icon="el-icon-edit"
            >编辑</el-button>
            <el-button
              size="mini"
              type="danger"
              plain
              @click="handleDelete(scope.row)"
              icon="el-icon-delete"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页组件 -->
      <div class="flex justify-center mt-6">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          background
        ></el-pagination>
      </div>
    </div>

    <!-- 添加/编辑护士对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="800px"
      :close-on-click-modal="false"
      @close="resetForm"
    >
      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <el-form-item label="护士姓名" prop="name">
            <el-input v-model="form.name" placeholder="请输入护士姓名"></el-input>
          </el-form-item>
          <el-form-item label="联系电话" prop="phone">
            <el-input v-model="form.phone" placeholder="请输入联系电话"></el-input>
          </el-form-item>
          <el-form-item label="邮箱地址" prop="email">
            <el-input v-model="form.email" placeholder="请输入邮箱地址"></el-input>
          </el-form-item>
          <el-form-item label="所属科室" prop="department">
            <el-select v-model="form.department" placeholder="请选择科室" class="w-full">
              <el-option label="内科" value="内科"></el-option>
              <el-option label="外科" value="外科"></el-option>
              <el-option label="儿科" value="儿科"></el-option>
              <el-option label="妇科" value="妇科"></el-option>
              <el-option label="急诊科" value="急诊科"></el-option>
              <el-option label="康复科" value="康复科"></el-option>
              <el-option label="老年科" value="老年科"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="职位" prop="position">
            <el-select v-model="form.position" placeholder="请选择职位" class="w-full">
              <el-option label="护士长" value="护士长"></el-option>
              <el-option label="主管护师" value="主管护师"></el-option>
              <el-option label="护师" value="护师"></el-option>
              <el-option label="护士" value="护士"></el-option>
              <el-option label="实习护士" value="实习护士"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="工作年限" prop="workYears">
            <el-input-number v-model="form.workYears" :min="0" :max="50" placeholder="工作年限" class="w-full"></el-input-number>
          </el-form-item>
          <el-form-item label="工作时间" prop="workSchedule">
            <el-select v-model="form.workSchedule" placeholder="请选择工作时间" class="w-full">
              <el-option label="白班" value="白班"></el-option>
              <el-option label="夜班" value="夜班"></el-option>
              <el-option label="倒班" value="倒班"></el-option>
              <el-option label="全天" value="全天"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select v-model="form.status" placeholder="请选择状态" class="w-full">
              <el-option label="在职" value="在职"></el-option>
              <el-option label="休假" value="休假"></el-option>
              <el-option label="离职" value="离职"></el-option>
            </el-select>
          </el-form-item>
        </div>
        <el-form-item label="专业特长" prop="specialty">
          <el-input v-model="form.specialty" type="textarea" :rows="3" placeholder="请输入专业特长"></el-input>
        </el-form-item>
        <el-form-item label="个人简介" prop="introduction">
          <el-input v-model="form.introduction" type="textarea" :rows="4" placeholder="请输入个人简介"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm" :loading="submitLoading">确 定</el-button>
      </span>
    </el-dialog>

    <!-- 查看护士详情对话框 -->
    <el-dialog
      title="护士详情"
      :visible.sync="viewDialogVisible"
      width="600px"
    >
      <div v-if="viewData" class="space-y-4">
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div class="flex">
            <span class="font-medium text-gray-600 w-20">姓名：</span>
            <span>{{ viewData.name }}</span>
          </div>
          <div class="flex">
            <span class="font-medium text-gray-600 w-20">电话：</span>
            <span>{{ viewData.phone }}</span>
          </div>
          <div class="flex">
            <span class="font-medium text-gray-600 w-20">邮箱：</span>
            <span>{{ viewData.email }}</span>
          </div>
          <div class="flex">
            <span class="font-medium text-gray-600 w-20">科室：</span>
            <span>{{ viewData.department }}</span>
          </div>
          <div class="flex">
            <span class="font-medium text-gray-600 w-20">职位：</span>
            <span>{{ viewData.position }}</span>
          </div>
          <div class="flex">
            <span class="font-medium text-gray-600 w-20">工作年限：</span>
            <span>{{ viewData.workYears ? viewData.workYears + '年' : '-' }}</span>
          </div>
          <div class="flex">
            <span class="font-medium text-gray-600 w-20">工作时间：</span>
            <span>{{ viewData.workSchedule }}</span>
          </div>
          <div class="flex">
            <span class="font-medium text-gray-600 w-20">状态：</span>
            <el-tag :type="viewData.status === '在职' ? 'success' : 'info'" size="mini">
              {{ viewData.status || '在职' }}
            </el-tag>
          </div>
        </div>
        <div v-if="viewData.specialty">
          <span class="font-medium text-gray-600">专业特长：</span>
          <p class="mt-2 p-3 bg-gray-50 rounded">{{ viewData.specialty }}</p>
        </div>
        <div v-if="viewData.introduction">
          <span class="font-medium text-gray-600">个人简介：</span>
          <p class="mt-2 p-3 bg-gray-50 rounded">{{ viewData.introduction }}</p>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import request from '@/utils/request'

export default {
  name: 'NurseManage',
  data() {
    return {
      loading: false,
      submitLoading: false,
      tableData: [],
      selection: [],
      currentPage: 1,
      pageSize: 10,
      total: 0,
      searchForm: {
        name: '',
        phone: '',
        department: ''
      },
      dialogVisible: false,
      viewDialogVisible: false,
      dialogTitle: '',
      isEdit: false,
      viewData: null,
      form: {
        id: null,
        name: '',
        phone: '',
        email: '',
        department: '',
        position: '',
        workYears: null,
        specialty: '',
        workSchedule: '',
        status: '在职',
        introduction: ''
      },
      rules: {
        name: [
          { required: true, message: '请输入护士姓名', trigger: 'blur' }
        ],
        phone: [
          { required: true, message: '请输入联系电话', trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
        ],
        email: [
          { required: true, message: '请输入邮箱地址', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
        ],
        department: [
          { required: true, message: '请选择科室', trigger: 'change' }
        ],
        position: [
          { required: true, message: '请选择职位', trigger: 'change' }
        ]
      }
    }
  },
  created() {
    this.load()
  },
  methods: {
    // 加载数据
    load() {
      this.loading = true
      request.get('/nurse/selectAll', {

      }).then(res => {
        if (res.code === '200') {
          this.tableData = res.data
          // this.total = res.data.total || 0
        } else {
          this.$message.error(res.msg || '查询失败')
        }
      }).catch(() => {
        this.$message.error('查询异常')
      }).finally(() => {
        this.loading = false
      })
    },

    // 搜索
    handleSearch() {
      this.currentPage = 1
      this.load()
    },

    // 重置搜索
    resetSearch() {
      this.$refs.searchForm.resetFields()
      this.searchForm = {
        name: '',
        phone: '',
        department: ''
      }
      this.currentPage = 1
      this.load()
    },

    // 查询全部
    queryAllNurses() {
      this.resetSearch()
    },

    // 添加
    handleAdd() {
      this.isEdit = false
      this.dialogTitle = '添加护士'
      this.dialogVisible = true
      this.resetForm()
    },

    // 编辑
    handleEdit(row) {
      this.isEdit = true
      this.dialogTitle = '编辑护士'
      this.dialogVisible = true
      this.form = { ...row }
    },

    // 查看详情
    handleView(row) {
      this.viewData = { ...row }
      this.viewDialogVisible = true
    },

    // 删除
    handleDelete(row) {
      this.$confirm('确认删除该护士吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        request.delete(`/nurse/delete/${row.id}`).then(res => {
          if (res.code === '200') {
            this.$message.success('删除成功')
            this.load()
          } else {
            this.$message.error(res.msg || '删除失败')
          }
        })
      })
    },

    // 批量删除
    handleBatchDelete() {
      if (this.selection.length === 0) {
        this.$message.warning('请选择要删除的数据')
        return
      }
      
      this.$confirm(`确认删除选中的${this.selection.length}条记录吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const ids = this.selection.map(item => item.id)
        request.delete('/nurse/delete/batch', { data: ids }).then(res => {
          if (res.code === '200') {
            this.$message.success('批量删除成功')
            this.load()
          } else {
            this.$message.error(res.msg || '批量删除失败')
          }
        })
      })
    },

    // 提交表单
    submitForm() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.submitLoading = true
          const url = this.isEdit ? '/nurse/update' : '/nurse/add'
          const method = this.isEdit ? 'put' : 'post'
          
          request[method](url, this.form).then(res => {
            if (res.code === '200') {
              this.$message.success(this.isEdit ? '更新成功' : '添加成功')
              this.dialogVisible = false
              this.load()
            } else {
              this.$message.error(res.msg || '操作失败')
            }
          }).catch(() => {
            this.$message.error('操作异常')
          }).finally(() => {
            this.submitLoading = false
          })
        }
      })
    },

    // 重置表单
    resetForm() {
      if (this.$refs.form) {
        this.$refs.form.resetFields()
      }
      this.form = {
        id: null,
        name: '',
        phone: '',
        email: '',
        department: '',
        position: '',
        workYears: null,
        specialty: '',
        workSchedule: '',
        status: '在职',
        introduction: ''
      }
    },

    // 选择改变
    handleSelectionChange(selection) {
      this.selection = selection
    },

    // 分页大小改变
    handleSizeChange(val) {
      this.pageSize = val
      this.currentPage = 1
      this.load()
    },

    // 当前页改变
    handleCurrentChange(val) {
      this.currentPage = val
      this.load()
    }
  }
}
</script>

<style scoped>
.grid {
  display: grid;
}

.grid-cols-1 {
  grid-template-columns: repeat(1, minmax(0, 1fr));
}

.grid-cols-2 {
  grid-template-columns: repeat(2, minmax(0, 1fr));
}

.gap-4 {
  gap: 1rem;
}

.space-y-4 > * + * {
  margin-top: 1rem;
}

@media (min-width: 768px) {
  .md\:grid-cols-2 {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}
</style> 