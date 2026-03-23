<template>
  <div>
    <!-- 功能说明 -->
    <div class="bg-white rounded-lg shadow-sm p-6 mb-5">
      <div class="flex items-center">
        <i class="el-icon-date text-3xl text-blue-500 mr-3"></i>
        <div>
          <h2 class="text-xl font-bold text-gray-800 mb-1">活动管理</h2>
          <p class="text-gray-600">本模块用于管理系统活动信息，提供活动的增加、修改、删除和查询功能，包括日常活动、娱乐项目、特殊节日活动等</p>
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
                v-model="searchForm.title" 
                placeholder="请输入活动标题" 
                prefix-icon="el-icon-search" 
                clearable
                size="small"
              ></el-input>
            </el-form-item>
            <el-form-item>
              <el-select 
                v-model="searchForm.categoryId" 
                placeholder="请选择活动分类" 
                clearable
                size="small"
              >
                <el-option
                  v-for="category in categoryOptions"
                  :key="category.id"
                  :label="category.name"
                  :value="category.id">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-select 
                v-model="searchForm.status" 
                placeholder="请选择活动状态" 
                clearable
                size="small"
              >
                <el-option label="计划中" value="PLANNED"></el-option>
                <el-option label="进行中" value="ONGOING"></el-option>
                <el-option label="已完成" value="COMPLETED"></el-option>
                <el-option label="已取消" value="CANCELLED"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSearch" icon="el-icon-search" size="small" class="mr-2">查询</el-button>
              <el-button @click="resetSearch" icon="el-icon-refresh" size="small" class="mr-2">重置</el-button>
              <el-button type="success" @click="queryAllActivities" icon="el-icon-menu" size="small">查询全部</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div class="w-full lg:w-auto flex justify-end space-x-3">
          <el-button type="primary" @click="handleAdd" icon="el-icon-plus" size="small">添加活动</el-button>
          <el-button type="danger" @click="handleBatchDelete" :disabled="selection.length === 0" icon="el-icon-delete" size="small">
            批量删除
          </el-button>
        </div>
      </div>
    </div>

    <!-- 活动表格 -->
    <div class="bg-white rounded-lg shadow-sm overflow-hidden mb-5">
      <el-table
        :data="tableData"
        style="width: 100%"
        v-loading="loading"
        @selection-change="handleSelectionChange"
        :border="false"
        stripe
        :cell-style="{padding: '16px 0'}"
        :header-cell-style="{background:'#f8fafc', color:'#475569', fontWeight: '600'}"
      >
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="title" label="活动标题" min-width="150"></el-table-column>
        <el-table-column label="活动分类" width="120">
          <template slot-scope="scope">
            <el-tag size="small" :type="getCategoryTagType(scope.row.categoryId)">
              {{ getCategoryName(scope.row.categoryId) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="活动时间" width="180">
          <template slot-scope="scope">
            <div class="flex items-center">
              <i class="el-icon-time text-gray-400 mr-1"></i>
              <span>{{ formatDate(scope.row.activityDate) }} {{ scope.row.startTime }}-{{ scope.row.endTime }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="location" label="活动地点" min-width="120">
          <template slot-scope="scope">
            <div class="flex items-center">
              <i class="el-icon-location text-gray-400 mr-1"></i>
              <span>{{ scope.row.location || '未设置' }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="封面图" width="80" align="center">
          <template slot-scope="scope">
            <el-image 
              v-if="scope.row.coverImage"
              :src="scope.row.coverImage" 
              :preview-src-list="[scope.row.coverImage]"
              fit="cover"
              class="w-12 h-12 rounded cursor-pointer"
              @error="handleImageError"
            >
              <div slot="error" class="h-12 flex items-center justify-center bg-gray-100 rounded">
                <i class="el-icon-picture-outline text-gray-400"></i>
              </div>
            </el-image>
            <div v-else class="h-12 flex items-center justify-center bg-gray-100 rounded">
              <i class="el-icon-picture-outline text-gray-400"></i>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="参与人数" width="120" align="center">
          <template slot-scope="scope">
            <div class="text-center">
              <span class="text-blue-600 font-medium">{{ scope.row.currentParticipants }}</span>
              <span class="text-gray-400">/{{ scope.row.maxParticipants || '∞' }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="organizer" label="组织者" width="100"></el-table-column>
        <el-table-column label="活动状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusTagType(scope.row.status)" size="small">
              {{ getStatusName(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="费用" width="80" align="center">
          <template slot-scope="scope">
            <span class="text-green-600 font-medium">{{ scope.row.cost > 0 ? '¥' + scope.row.cost : '免费' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" fixed="right" width="180" align="center">
          <template slot-scope="scope">
            <div class="flex justify-center space-x-2">
              <el-button type="primary" size="mini" icon="el-icon-edit" circle @click="handleEdit(scope.row)" class="transform hover:-translate-y-1 transition-transform"></el-button>
              <el-button type="info" size="mini" icon="el-icon-view" circle @click="handleDetail(scope.row)" class="transform hover:-translate-y-1 transition-transform"></el-button>
              <el-button type="danger" size="mini" icon="el-icon-delete" circle @click="handleDelete(scope.row)" class="transform hover:-translate-y-1 transition-transform"></el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 分页组件 -->
    <div class="bg-white rounded-lg shadow-sm p-4 flex justify-end">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        background
      ></el-pagination>
    </div>

    <!-- 添加/编辑活动对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="900px" :close-on-click-modal="false" custom-class="rounded-lg">
      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="活动标题" prop="title">
              <el-input v-model="form.title" placeholder="请输入活动标题" prefix-icon="el-icon-edit"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="活动分类" prop="categoryId">
              <el-select v-model="form.categoryId" placeholder="请选择活动分类" style="width: 100%">
                <el-option
                  v-for="category in categoryOptions"
                  :key="category.id"
                  :label="category.name"
                  :value="category.id">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="活动日期" prop="activityDate">
              <el-date-picker
                v-model="form.activityDate"
                type="date"
                placeholder="选择活动日期"
                style="width: 100%"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="开始时间" prop="startTime">
              <el-time-picker
                v-model="form.startTime"
                placeholder="选择开始时间"
                style="width: 100%"
                format="HH:mm"
                value-format="HH:mm:ss">
              </el-time-picker>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="结束时间" prop="endTime">
              <el-time-picker
                v-model="form.endTime"
                placeholder="选择结束时间"
                style="width: 100%"
                format="HH:mm"
                value-format="HH:mm:ss">
              </el-time-picker>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="活动地点" prop="location">
              <el-input v-model="form.location" placeholder="请输入活动地点" prefix-icon="el-icon-location"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="最大人数" prop="maxParticipants">
              <el-input-number v-model="form.maxParticipants" :min="0" placeholder="0表示无限制" style="width: 100%"></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="组织者" prop="organizer">
              <el-input v-model="form.organizer" placeholder="请输入组织者" prefix-icon="el-icon-user"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话" prop="organizerPhone">
              <el-input v-model="form.organizerPhone" placeholder="请输入联系电话" prefix-icon="el-icon-phone"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="活动费用" prop="cost">
              <el-input-number v-model="form.cost" :min="0" :precision="2" placeholder="活动费用" style="width: 100%"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="活动状态" prop="status">
              <el-select v-model="form.status" placeholder="请选择活动状态" style="width: 100%">
                <el-option label="计划中" value="PLANNED"></el-option>
                <el-option label="进行中" value="ONGOING"></el-option>
                <el-option label="已完成" value="COMPLETED"></el-option>
                <el-option label="已取消" value="CANCELLED"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="报名截止" prop="registrationDeadline">
          <el-date-picker
            v-model="form.registrationDeadline"
            type="datetime"
            placeholder="选择报名截止时间"
            style="width: 100%"
            format="yyyy-MM-dd HH:mm:ss"
            value-format="yyyy-MM-dd HH:mm:ss">
          </el-date-picker>
        </el-form-item>
        
        <el-form-item label="活动描述" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入活动描述"></el-input>
        </el-form-item>
        
        <el-form-item label="参与要求" prop="requirements">
          <el-input v-model="form.requirements" type="textarea" :rows="2" placeholder="请输入参与要求"></el-input>
        </el-form-item>
        
        <el-form-item label="所需物品" prop="materialsNeeded">
          <el-input v-model="form.materialsNeeded" type="textarea" :rows="2" placeholder="请输入所需物品"></el-input>
        </el-form-item>
        
        <el-form-item label="备注信息" prop="notes">
          <el-input v-model="form.notes" type="textarea" :rows="2" placeholder="请输入备注信息"></el-input>
        </el-form-item>
        
        <el-form-item label="封面图片" prop="coverImage">
          <div class="flex items-center">
            <el-upload
              class="avatar-uploader"
              action="/api/files/upload"
              :show-file-list="false"
              :on-success="handleCoverSuccess"
              :before-upload="beforeCoverUpload">
              <img v-if="this.form.coverImage" :src="this.form.coverImage" class="w-32 h-32 object-cover rounded" />
              <div v-else class="w-32 h-32 bg-gray-100 flex items-center justify-center rounded border border-gray-200 cursor-pointer hover:bg-gray-50">
                <i class="el-icon-plus text-gray-500 text-xl"></i>
                <div class="text-gray-500 text-xs mt-2">上传封面</div>
              </div>
            </el-upload>
            <div class="ml-4 text-gray-500 text-sm" v-if="form.coverImage">
              <div class="mb-2">已上传封面</div>
              <el-button type="danger" size="mini" icon="el-icon-delete" @click="removeCoverImage">删除图片</el-button>
            </div>
          </div>
          <div class="text-gray-400 text-xs mt-2">
            推荐尺寸：800x600，支持jpg、png格式，不超过2MB
          </div>
        </el-form-item>
      </el-form>
      <div slot="footer" class="flex justify-end space-x-3">
        <el-button @click="dialogVisible = false" icon="el-icon-close">取 消</el-button>
        <el-button type="primary" @click="saveActivity" :loading="submitLoading" icon="el-icon-check">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 活动详情对话框 -->
    <el-dialog title="活动详情" :visible.sync="detailDialogVisible" width="800px" custom-class="rounded-lg">
      <div v-if="currentActivity" class="px-5">
        <div class="flex items-center mb-6">
          <div class="w-16 h-16 bg-blue-50 rounded-lg flex items-center justify-center mr-4">
            <i class="el-icon-date text-2xl text-blue-500"></i>
          </div>
          <div>
            <h3 class="text-xl font-bold text-gray-800 mb-1">{{ currentActivity.title }}</h3>
            <p class="text-gray-500">{{ getCategoryName(currentActivity.categoryId) }} · {{ getStatusName(currentActivity.status) }}</p>
          </div>
        </div>
        
        <el-divider></el-divider>
        
        <div class="space-y-6">
          <div class="bg-gray-50 rounded-lg p-4">
            <h4 class="flex items-center text-gray-700 font-medium mb-3">
              <i class="el-icon-time text-blue-500 mr-2"></i> 时间安排
            </h4>
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div class="flex">
                <span class="text-gray-500 w-24">活动日期:</span>
                <span class="text-gray-800">{{ currentActivity.activityDate }}</span>
              </div>
              <div class="flex">
                <span class="text-gray-500 w-24">活动时间:</span>
                <span class="text-gray-800">{{ currentActivity.activityDate }} {{ currentActivity.startTime }} - {{ currentActivity.endTime }}</span>
              </div>
              <div class="flex">
                <span class="text-gray-500 w-24">报名截止:</span>
                <span class="text-gray-800">{{ currentActivity.registrationDeadline || '未设置' }}</span>
              </div>
            </div>
          </div>
          
          <!-- 封面图展示 -->
          <div v-if="currentActivity.coverImage" class="bg-gray-50 rounded-lg p-4">
            <h4 class="flex items-center text-gray-700 font-medium mb-3">
              <i class="el-icon-picture text-indigo-500 mr-2"></i> 活动封面
            </h4>
            <div class="flex justify-center">
              <el-image 
                :src="currentActivity.coverImage" 
                :preview-src-list="[currentActivity.coverImage]"
                fit="contain" 
                style="max-height: 300px; width: auto;"
                class="rounded shadow-sm cursor-pointer"
                @error="handleImageError"
              >
                <div slot="error" class="h-40 w-full flex items-center justify-center bg-gray-100 rounded">
                  <i class="el-icon-picture-outline text-gray-400 text-3xl"></i>
                </div>
              </el-image>
            </div>
          </div>
          
          <div class="bg-gray-50 rounded-lg p-4">
            <h4 class="flex items-center text-gray-700 font-medium mb-3">
              <i class="el-icon-location text-green-500 mr-2"></i> 活动信息
            </h4>
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div class="flex">
                <span class="text-gray-500 w-24">活动地点:</span>
                <span class="text-gray-800">{{ currentActivity.location || '未设置' }}</span>
              </div>
              <div class="flex">
                <span class="text-gray-500 w-24">组织者:</span>
                <span class="text-gray-800">{{ currentActivity.organizer || '未设置' }}</span>
              </div>
              <div class="flex">
                <span class="text-gray-500 w-24">联系电话:</span>
                <span class="text-gray-800">{{ currentActivity.organizerPhone || '未设置' }}</span>
              </div>
              <div class="flex">
                <span class="text-gray-500 w-24">活动费用:</span>
                <span class="text-gray-800">{{ currentActivity.cost > 0 ? '¥' + currentActivity.cost : '免费' }}</span>
              </div>
              <div class="flex">
                <span class="text-gray-500 w-24">参与人数:</span>
                <span class="text-gray-800">{{ currentActivity.currentParticipants }}/{{ currentActivity.maxParticipants || '无限制' }}</span>
              </div>
            </div>
          </div>
          
          <div v-if="currentActivity.description" class="bg-gray-50 rounded-lg p-4">
            <h4 class="flex items-center text-gray-700 font-medium mb-3">
              <i class="el-icon-document text-purple-500 mr-2"></i> 活动描述
            </h4>
            <p class="text-gray-800 leading-relaxed">{{ currentActivity.description }}</p>
          </div>
          
          <div v-if="currentActivity.requirements || currentActivity.materialsNeeded" class="bg-gray-50 rounded-lg p-4">
            <h4 class="flex items-center text-gray-700 font-medium mb-3">
              <i class="el-icon-warning text-orange-500 mr-2"></i> 注意事项
            </h4>
            <div class="space-y-2">
              <div v-if="currentActivity.requirements">
                <span class="text-gray-500 font-medium">参与要求:</span>
                <p class="text-gray-800">{{ currentActivity.requirements }}</p>
              </div>
              <div v-if="currentActivity.materialsNeeded">
                <span class="text-gray-500 font-medium">所需物品:</span>
                <p class="text-gray-800">{{ currentActivity.materialsNeeded }}</p>
              </div>
            </div>
          </div>
          
          <div v-if="currentActivity.notes" class="bg-gray-50 rounded-lg p-4">
            <h4 class="flex items-center text-gray-700 font-medium mb-3">
              <i class="el-icon-chat-line-square text-gray-500 mr-2"></i> 备注信息
            </h4>
            <p class="text-gray-800">{{ currentActivity.notes }}</p>
          </div>
        </div>
      </div>
      <div slot="footer" class="flex justify-end space-x-3">
        <el-button @click="detailDialogVisible = false" icon="el-icon-close">关 闭</el-button>
        <el-button type="primary" @click="handleEdit(currentActivity)" icon="el-icon-edit">编辑活动</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "ActivityManage",
  data() {
    // 手机号验证规则
    const validatePhone = (rule, value, callback) => {
      if (value && !/^1[3-9]\d{9}$/.test(value)) {
        callback(new Error("请输入正确的手机号格式"));
      } else {
        callback();
      }
    };

    return {
      // 搜索表单
      searchForm: {
        title: "",
        categoryId: "",
        status: "",
      },
      // 表格数据
      tableData: [],
      loading: false,
      total: 0,
      pageNum: 1,
      pageSize: 10,
      
      // 选中项
      selection: [],
      
      // 分类选项
      categoryOptions: [],
      
      // 对话框
      dialogVisible: false,
      dialogTitle: "添加活动",
      isEdit: false,
      submitLoading: false,
      
      // 详情对话框
      detailDialogVisible: false,
      currentActivity: null,
      
      // 表单
      form: {
        id: null,
        categoryId: "",
        title: "",
        description: "",
        activityDate: "",
        startTime: "",
        endTime: "",
        location: "",
        maxParticipants: 0,
        organizer: "",
        organizerPhone: "",
        requirements: "",
        materialsNeeded: "",
        cost: 0.00,
        status: "PLANNED",
        registrationDeadline: "",
        notes: "",
        coverImage: "" // 新增封面图片字段
      },
      
      // 表单验证规则
      rules: {
        categoryId: [
          { required: true, message: "请选择活动分类", trigger: "change" }
        ],
        title: [
          { required: true, message: "请输入活动标题", trigger: "blur" },
          { min: 2, max: 255, message: "长度在 2 到 255 个字符", trigger: "blur" }
        ],
        activityDate: [
          { required: true, message: "请选择活动日期", trigger: "change" }
        ],
        startTime: [
          { required: true, message: "请选择开始时间", trigger: "change" }
        ],
        endTime: [
          { required: true, message: "请选择结束时间", trigger: "change" }
        ],
        organizer: [
          { required: true, message: "请输入组织者", trigger: "blur" }
        ],
        organizerPhone: [
          { validator: validatePhone, trigger: "blur" }
        ],
        status: [
          { required: true, message: "请选择活动状态", trigger: "change" }
        ]
      }
    };
  },
  created() {
    this.fetchData();
    this.fetchCategories();
  },
  methods: {
    // 获取数据
    fetchData() {
      this.loading = true;
      
      // 构建请求参数
      const params = {
        pageNum: this.pageNum,
        pageSize: this.pageSize
      };
      
      // 只有当搜索表单中有值时才添加到请求参数中
      if (this.searchForm.title) {
        params.title = this.searchForm.title;
      }
      
      if (this.searchForm.categoryId) {
        params.categoryId = this.searchForm.categoryId;
      }
      
      if (this.searchForm.status) {
        params.status = this.searchForm.status;
      }
      
      this.$request.get("/activity/selectPage", { params })
        .then(res => {
          if (res.code === '200') {
            this.tableData = res.data.list || [];
            // 调试输出，检查返回的数据是否包含图片URL
            if (this.tableData.length > 0) {
              console.log("返回的第一条数据:", this.tableData[0]);
              console.log("图片URL:", this.tableData[0].coverImage);
            }
            this.total = res.data.total || 0;
          } else {
            this.$message.error(res.msg || "获取活动数据失败");
          }
        })
        .catch(() => {
          this.$message.error("获取活动数据异常");
        })
        .finally(() => {
          this.loading = false;
        });
    },
    
    // 获取活动分类
    fetchCategories() {
      this.$request.get("/activity/category/selectAll")
        .then(res => {
          if (res.code === '200') {
            this.categoryOptions = res.data || [];
          }
        })
        .catch(() => {
          this.$message.error("获取活动分类失败");
        });
    },
    
    // 查询
    handleSearch() {
      this.pageNum = 1;
      this.fetchData();
    },
    
    // 重置查询
    resetSearch() {
      // 清除搜索表单
      this.$refs.searchForm.resetFields();
      this.pageNum = 1;
      // 重新加载所有数据
      this.fetchData();
      this.$message.success("已重置搜索条件，显示所有活动数据");
    },
    
    // 查询全部活动
    queryAllActivities() {
      // 清除搜索表单
      this.$refs.searchForm.resetFields();
      this.pageNum = 1;
      this.fetchData();
      this.$message.success("已显示所有活动数据");
    },
    
    // 分页相关
    handleSizeChange(val) {
      this.pageSize = val;
      this.fetchData();
    },
    
    handleCurrentChange(val) {
      this.pageNum = val;
      this.fetchData();
    },
    
    // 表格选中项变化
    handleSelectionChange(selection) {
      this.selection = selection;
    },
    
    // 添加活动
    handleAdd() {
      this.dialogTitle = "添加活动";
      this.isEdit = false;
      this.form = {
        id: null,
        categoryId: "",
        title: "",
        description: "",
        activityDate: "",
        startTime: "",
        endTime: "",
        location: "",
        maxParticipants: 0,
        organizer: "",
        organizerPhone: "",
        requirements: "",
        materialsNeeded: "",
        cost: 0.00,
        status: "PLANNED",
        registrationDeadline: "",
        notes: "",
        coverImage: ""
      };
      this.dialogVisible = true;
      
      // 清除验证
      this.$nextTick(() => {
        if (this.$refs.form) {
          this.$refs.form.clearValidate();
        }
      });
    },
    
    // 编辑活动
    handleEdit(row) {
      this.dialogTitle = "编辑活动";
      this.isEdit = true;
      this.form = JSON.parse(JSON.stringify(row)); // 深拷贝，防止直接修改表格数据
      this.dialogVisible = true;
      
      // 清除验证
      this.$nextTick(() => {
        if (this.$refs.form) {
          this.$refs.form.clearValidate();
        }
      });
    },
    
    // 查看详情
    handleDetail(row) {
      this.currentActivity = JSON.parse(JSON.stringify(row)); // 深拷贝，防止直接修改表格数据
      console.log("查看详情:", this.currentActivity);
      console.log("封面图URL:", this.currentActivity.coverImage);
      this.detailDialogVisible = true;
    },
    
    // 保存活动（添加或更新）
    saveActivity() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.submitLoading = true;
          
          // 记录发送到服务器的表单数据
          console.log("保存活动表单数据:", JSON.parse(JSON.stringify(this.form)));
          
          const request = this.isEdit 
            ? this.$request.put("/activity/update", this.form)
            : this.$request.post("/activity/add", this.form);
            
          request.then(res => {
              if (res.code === '200') {
                this.$message.success(this.isEdit ? "编辑成功" : "添加成功");
                this.dialogVisible = false;
                this.fetchData();
              } else {
                this.$message.error(res.msg || (this.isEdit ? "编辑失败" : "添加失败"));
              }
            })
            .catch(() => {
              this.$message.error(this.isEdit ? "编辑活动请求异常" : "添加活动请求异常");
            })
            .finally(() => {
              this.submitLoading = false;
            });
        }
      });
    },
    
    // 删除活动
    handleDelete(row) {
      this.$confirm("确定删除该活动吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        this.$request.delete(`/activity/delete/${row.id}`)
          .then(res => {
            if (res.code === '200') {
              this.$message.success("删除成功");
              this.fetchData();
            } else {
              this.$message.error(res.msg || "删除失败");
            }
          })
          .catch(() => {
            this.$message.error("删除活动请求异常");
          });
      }).catch(() => {});
    },
    
    // 批量删除
    handleBatchDelete() {
      if (this.selection.length === 0) {
        this.$message.warning("请选择要删除的活动");
        return;
      }
      
      this.$confirm(`确定删除选中的 ${this.selection.length} 个活动吗?`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        const ids = this.selection.map(item => item.id);
        
        this.$request.delete("/activity/delete/batch", { data: ids })
          .then(res => {
            if (res.code === '200') {
              this.$message.success("批量删除成功");
              this.fetchData();
            } else {
              this.$message.error(res.msg || "批量删除失败");
            }
          })
          .catch(() => {
            this.$message.error("批量删除活动请求异常");
          });
      }).catch(() => {});
    },

    // 获取分类名称
    getCategoryName(categoryId) {
      const category = this.categoryOptions.find(item => item.id === categoryId);
      return category ? category.name : '未知分类';
    },
    
    // 获取分类标签类型
    getCategoryTagType(categoryId) {
      const types = ['', 'success', 'info', 'warning', 'danger'];
      return types[categoryId % types.length] || '';
    },

    // 获取状态名称
    getStatusName(status) {
      switch (status) {
        case 'PLANNED':
          return '计划中';
        case 'ONGOING':
          return '进行中';
        case 'COMPLETED':
          return '已完成';
        case 'CANCELLED':
          return '已取消';
        default:
          return '未知';
      }
    },
    
    // 获取状态标签类型
    getStatusTagType(status) {
      switch (status) {
        case 'PLANNED':
          return 'info';
        case 'ONGOING':
          return 'success';
        case 'COMPLETED':
          return '';
        case 'CANCELLED':
          return 'danger';
        default:
          return 'info';
      }
    },

    // 封面图片上传成功处理
    handleCoverSuccess(response) {
      if (response.code === "200") {
        // 检查图片URL格式并确保是完整路径
        let imageUrl = response.data;
        // 如果不是完整URL路径，则添加基础路径
        if (imageUrl && !imageUrl.startsWith('http')) {
          // 保持原样，不添加前缀
          // 服务器返回的路径可能已经是可访问的相对路径
        }
        this.form.coverImage = imageUrl;
        console.log("处理后的图片URL:", this.form.coverImage); 
        this.$message.success('封面图片上传成功');
      } else {
        this.$message.error(response.msg || '封面图片上传失败');
      }
    },

    // 封面图片上传前校验
    beforeCoverUpload(rawFile) {
      const isJPGPNG = rawFile.type === 'image/jpeg' || rawFile.type === 'image/png';
      const isLt2M = rawFile.size / 1024 / 1024 < 2;

      if (!isJPGPNG) {
        this.$message.error('封面图片只能是 JPG/PNG 格式!');
      }
      if (!isLt2M) {
        this.$message.error('封面图片大小不能超过 2MB!');
      }
      return isJPGPNG && isLt2M;
    },

    // 删除封面图片
    removeCoverImage() {
      this.form.coverImage = '';
      this.$message.success('封面图片已删除');
    },

    // 添加图片加载错误处理方法
    handleImageError(e) {
      console.error("图片加载失败:", e);
      console.log("图片URL:", e.target.src);
    },

    // 格式化日期，去除多余的时间戳
    formatDate(dateStr) {
      if (!dateStr) return '';
      // 如果日期包含时间戳，只保留日期部分
      if (dateStr.includes(' ')) {
        return dateStr.split(' ')[0];
      }
      return dateStr;
    }
  }
};
</script>

<style scoped>
/* ElementUI覆盖样式 */
:deep(.el-table) {
  @apply rounded-lg overflow-hidden border-none;
}

:deep(.el-table td) {
  @apply border-b border-gray-100 text-gray-700;
}

:deep(.el-table--striped .el-table__body tr.el-table__row--striped td) {
  @apply bg-gray-50;
}

:deep(.el-table::before) {
  @apply hidden;
}

:deep(.el-dialog) {
  @apply rounded-lg overflow-hidden;
}

:deep(.el-dialog__header) {
  @apply p-5 bg-gray-50 mb-2 border-b border-gray-100;
}

:deep(.el-dialog__title) {
  @apply font-bold text-gray-800;
}

/* 按钮过渡动画 */
:deep(.el-button) {
  @apply transition-all duration-300;
}
</style>