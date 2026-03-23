<template>
  <div>
    <!-- 功能说明 -->
    <div class="bg-white rounded-lg shadow-sm p-6 mb-5">
      <div class="flex items-center">
        <i class="el-icon-user text-3xl text-blue-500 mr-3"></i>
        <div>
          <h2 class="text-xl font-bold text-gray-800 mb-1">活动报名管理</h2>
          <p class="text-gray-600">本模块用于管理活动报名信息，包括报名记录查看、状态更新、签到签退、评分反馈等功能</p>
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
                v-model="searchForm.activityTitle" 
                placeholder="请输入活动标题" 
                prefix-icon="el-icon-search" 
                clearable
                size="small"
              ></el-input>
            </el-form-item>
            <el-form-item>
              <el-input 
                v-model="searchForm.userName" 
                placeholder="请输入用户姓名" 
                prefix-icon="el-icon-user" 
                clearable
                size="small"
              ></el-input>
            </el-form-item>
            <el-form-item>
              <el-select 
                v-model="searchForm.attendanceStatus" 
                placeholder="请选择出席状态" 
                clearable
                size="small"
              >
                <el-option label="已报名" value="REGISTERED"></el-option>
                <el-option label="已参加" value="ATTENDED"></el-option>
                <el-option label="缺席" value="ABSENT"></el-option>
                <el-option label="已取消" value="CANCELLED"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-date-picker
                v-model="searchForm.dateRange"
                type="daterange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
                size="small"
              >
              </el-date-picker>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSearch" icon="el-icon-search" size="small" class="mr-2">查询</el-button>
              <el-button @click="resetSearch" icon="el-icon-refresh" size="small" class="mr-2">重置</el-button>
              <el-button type="success" @click="queryAllParticipants" icon="el-icon-menu" size="small">查询全部</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div class="w-full lg:w-auto flex justify-end space-x-3">
          <el-button type="danger" @click="handleBatchDelete" :disabled="selection.length === 0" icon="el-icon-delete" size="small">
            批量删除
          </el-button>
        </div>
      </div>
    </div>

    <!-- 报名记录表格 -->
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
        <el-table-column prop="activityTitle" label="活动标题" min-width="150"></el-table-column>
        <el-table-column prop="userName" label="用户姓名" width="120"></el-table-column>
        <el-table-column label="报名时间" width="160">
          <template slot-scope="scope">
            <div class="flex items-center">
              <i class="el-icon-time text-gray-400 mr-1"></i>
              <span>{{ formatDateTime(scope.row.registrationTime) }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="出席状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusTagType(scope.row.attendanceStatus)" size="small">
              {{ getStatusName(scope.row.attendanceStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="签到时间" width="160">
          <template slot-scope="scope">
            <div class="flex items-center" v-if="scope.row.checkinTime">
              <i class="el-icon-circle-check text-green-400 mr-1"></i>
              <span>{{ formatDateTime(scope.row.checkinTime) }}</span>
            </div>
            <span v-else class="text-gray-400">未签到</span>
          </template>
        </el-table-column>
        <el-table-column label="签退时间" width="160">
          <template slot-scope="scope">
            <div class="flex items-center" v-if="scope.row.checkoutTime">
              <i class="el-icon-circle-close text-red-400 mr-1"></i>
              <span>{{ formatDateTime(scope.row.checkoutTime) }}</span>
            </div>
            <span v-else class="text-gray-400">未签退</span>
          </template>
        </el-table-column>
        <el-table-column label="评分" width="80" align="center">
          <template slot-scope="scope">
            <div v-if="scope.row.rating" class="flex items-center justify-center">
              <el-rate
                v-model="scope.row.rating"
                disabled
                show-score
                text-color="#ff9900"
                score-template="{value}"
                :max="5"
                size="small"
              >
              </el-rate>
            </div>
            <span v-else class="text-gray-400">未评分</span>
          </template>
        </el-table-column>
        <el-table-column prop="activityDate" label="活动日期" width="120"></el-table-column>
        <el-table-column prop="activityLocation" label="活动地点" min-width="120">
          <template slot-scope="scope">
            <div class="flex items-center">
              <i class="el-icon-location text-gray-400 mr-1"></i>
              <span>{{ scope.row.activityLocation || '未设置' }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="操作" fixed="right" width="200" align="center">
          <template slot-scope="scope">
            <div class="flex justify-center space-x-1">
              <el-button type="text" size="small" @click="handleDetail(scope.row)" class="text-blue-600 hover:text-blue-800">
                <i class="el-icon-view mr-1"></i>详情
              </el-button>
              <el-button 
                v-if="scope.row.attendanceStatus === 'REGISTERED'" 
                type="text" 
                size="small" 
                @click="handleCheckin(scope.row)" 
                class="text-green-600 hover:text-green-800"
              >
                <i class="el-icon-check mr-1"></i>签到
              </el-button>
              <el-button 
                v-if="scope.row.attendanceStatus === 'ATTENDED' && !scope.row.checkoutTime" 
                type="text" 
                size="small" 
                @click="handleCheckout(scope.row)" 
                class="text-orange-600 hover:text-orange-800"
              >
                <i class="el-icon-close mr-1"></i>签退
              </el-button>
              <el-button type="text" size="small" @click="handleEdit(scope.row)" class="text-purple-600 hover:text-purple-800">
                <i class="el-icon-edit mr-1"></i>编辑
              </el-button>
              <el-button type="text" size="small" @click="handleDelete(scope.row)" class="text-red-600 hover:text-red-800">
                <i class="el-icon-delete mr-1"></i>删除
              </el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
      
      <!-- 分页 -->
      <div class="flex justify-between items-center p-4 bg-gray-50">
        <div class="text-sm text-gray-600">
          共 {{ total }} 条记录，当前第 {{ pageNum }} 页，每页 {{ pageSize }} 条
        </div>
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pageSize"
          layout="sizes, prev, pager, next, jumper"
          :total="total"
          background
        >
        </el-pagination>
      </div>
    </div>

    <!-- 编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="600px"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
    >
      <el-form :model="form" :rules="rules" ref="form" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="出席状态" prop="attendanceStatus">
              <el-select v-model="form.attendanceStatus" placeholder="请选择出席状态" style="width: 100%">
                <el-option label="已报名" value="REGISTERED"></el-option>
                <el-option label="已参加" value="ATTENDED"></el-option>
                <el-option label="缺席" value="ABSENT"></el-option>
                <el-option label="已取消" value="CANCELLED"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="活动评分" prop="rating">
              <el-rate
                v-model="form.rating"
                :max="5"
                show-text
                :texts="['极差', '失望', '一般', '满意', '惊喜']"
              >
              </el-rate>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="签到时间" prop="checkinTime">
              <el-date-picker
                v-model="form.checkinTime"
                type="datetime"
                placeholder="选择签到时间"
                style="width: 100%"
                format="yyyy-MM-dd HH:mm:ss"
                value-format="yyyy-MM-dd HH:mm:ss"
              >
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="签退时间" prop="checkoutTime">
              <el-date-picker
                v-model="form.checkoutTime"
                type="datetime"
                placeholder="选择签退时间"
                style="width: 100%"
                format="yyyy-MM-dd HH:mm:ss"
                value-format="yyyy-MM-dd HH:mm:ss"
              >
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="紧急联系人" prop="emergencyContact">
              <el-input v-model="form.emergencyContact" placeholder="请输入紧急联系人" prefix-icon="el-icon-user"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="紧急联系电话" prop="emergencyPhone">
              <el-input v-model="form.emergencyPhone" placeholder="请输入紧急联系电话" prefix-icon="el-icon-phone"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="活动反馈" prop="feedback">
          <el-input v-model="form.feedback" type="textarea" :rows="3" placeholder="请输入活动反馈"></el-input>
        </el-form-item>
        
        <el-form-item label="特殊需求" prop="specialNeeds">
          <el-input v-model="form.specialNeeds" type="textarea" :rows="2" placeholder="请输入特殊需求"></el-input>
        </el-form-item>
        
        <el-form-item label="备注信息" prop="notes">
          <el-input v-model="form.notes" type="textarea" :rows="2" placeholder="请输入备注信息"></el-input>
        </el-form-item>
      </el-form>
      
      <div slot="footer" class="flex justify-end space-x-3">
        <el-button @click="dialogVisible = false" icon="el-icon-close">取 消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading" icon="el-icon-check">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 详情对话框 -->
    <el-dialog
      title="报名详情"
      :visible.sync="detailDialogVisible"
      width="700px"
      :close-on-click-modal="false"
    >
      <div v-if="currentParticipant" class="space-y-6">
        <div class="bg-gradient-to-r from-blue-50 to-purple-50 rounded-lg p-6">
          <h3 class="flex items-center text-lg font-bold text-gray-800 mb-4">
            <i class="el-icon-user text-blue-500 mr-2"></i> 基本信息
          </h3>
          <div class="grid grid-cols-2 gap-4">
            <div class="space-y-3">
              <div class="flex">
                <span class="text-gray-500 w-24">活动标题:</span>
                <span class="text-gray-800 font-medium">{{ currentParticipant.activityTitle }}</span>
              </div>
              <div class="flex">
                <span class="text-gray-500 w-24">用户姓名:</span>
                <span class="text-gray-800 font-medium">{{ currentParticipant.userName }}</span>
              </div>
              <div class="flex">
                <span class="text-gray-500 w-24">报名时间:</span>
                <span class="text-gray-800">{{ formatDateTime(currentParticipant.registrationTime) }}</span>
              </div>
              <div class="flex">
                <span class="text-gray-500 w-24">出席状态:</span>
                <el-tag :type="getStatusTagType(currentParticipant.attendanceStatus)" size="small">
                  {{ getStatusName(currentParticipant.attendanceStatus) }}
                </el-tag>
              </div>
            </div>
            <div class="space-y-3">
              <div class="flex">
                <span class="text-gray-500 w-24">活动日期:</span>
                <span class="text-gray-800">{{ currentParticipant.activityDate || '未设置' }}</span>
              </div>
              <div class="flex">
                <span class="text-gray-500 w-24">活动地点:</span>
                <span class="text-gray-800">{{ currentParticipant.activityLocation || '未设置' }}</span>
              </div>
              <div class="flex">
                <span class="text-gray-500 w-24">签到时间:</span>
                <span class="text-gray-800">{{ formatDateTime(currentParticipant.checkinTime) || '未签到' }}</span>
              </div>
              <div class="flex">
                <span class="text-gray-500 w-24">签退时间:</span>
                <span class="text-gray-800">{{ formatDateTime(currentParticipant.checkoutTime) || '未签退' }}</span>
              </div>
            </div>
          </div>
        </div>
        
        <div v-if="currentParticipant.rating || currentParticipant.feedback" class="bg-gray-50 rounded-lg p-4">
          <h4 class="flex items-center text-gray-700 font-medium mb-3">
            <i class="el-icon-star-on text-yellow-500 mr-2"></i> 评价反馈
          </h4>
          <div class="space-y-2">
            <div v-if="currentParticipant.rating" class="flex items-center">
              <span class="text-gray-500 font-medium mr-3">活动评分:</span>
              <el-rate
                v-model="currentParticipant.rating"
                disabled
                show-score
                text-color="#ff9900"
                score-template="{value}分"
                :max="5"
              >
              </el-rate>
            </div>
            <div v-if="currentParticipant.feedback">
              <span class="text-gray-500 font-medium">活动反馈:</span>
              <p class="text-gray-800 mt-1">{{ currentParticipant.feedback }}</p>
            </div>
          </div>
        </div>
        
        <div v-if="currentParticipant.specialNeeds || currentParticipant.emergencyContact" class="bg-gray-50 rounded-lg p-4">
          <h4 class="flex items-center text-gray-700 font-medium mb-3">
            <i class="el-icon-warning text-orange-500 mr-2"></i> 特殊信息
          </h4>
          <div class="space-y-2">
            <div v-if="currentParticipant.specialNeeds">
              <span class="text-gray-500 font-medium">特殊需求:</span>
              <p class="text-gray-800 mt-1">{{ currentParticipant.specialNeeds }}</p>
            </div>
            <div v-if="currentParticipant.emergencyContact">
              <span class="text-gray-500 font-medium">紧急联系人:</span>
              <span class="text-gray-800 ml-2">{{ currentParticipant.emergencyContact }}</span>
              <span v-if="currentParticipant.emergencyPhone" class="text-gray-600 ml-2">({{ currentParticipant.emergencyPhone }})</span>
            </div>
          </div>
        </div>
        
        <div v-if="currentParticipant.notes" class="bg-gray-50 rounded-lg p-4">
          <h4 class="flex items-center text-gray-700 font-medium mb-3">
            <i class="el-icon-chat-line-square text-gray-500 mr-2"></i> 备注信息
          </h4>
          <p class="text-gray-800">{{ currentParticipant.notes }}</p>
        </div>
      </div>
      <div slot="footer" class="flex justify-end space-x-3">
        <el-button @click="detailDialogVisible = false" icon="el-icon-close">关 闭</el-button>
        <el-button type="primary" @click="handleEdit(currentParticipant)" icon="el-icon-edit">编辑记录</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "ActivityParticipantManage",
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
        activityTitle: "",
        userName: "",
        attendanceStatus: "",
        dateRange: []
      },
      // 表格数据
      tableData: [],
      loading: false,
      total: 0,
      pageNum: 1,
      pageSize: 10,
      
      // 选中项
      selection: [],
      
      // 对话框
      dialogVisible: false,
      dialogTitle: "编辑报名记录",
      submitLoading: false,
      
      // 详情对话框
      detailDialogVisible: false,
      currentParticipant: null,
      
      // 表单数据
      form: {
        id: null,
        activityId: null,
        userId: null,
        attendanceStatus: "REGISTERED",
        checkinTime: "",
        checkoutTime: "",
        rating: null,
        feedback: "",
        specialNeeds: "",
        emergencyContact: "",
        emergencyPhone: "",
        notes: ""
      },
      
      // 表单验证规则
      rules: {
        attendanceStatus: [
          { required: true, message: "请选择出席状态", trigger: "change" }
        ],
        emergencyPhone: [
          { validator: validatePhone, trigger: "blur" }
        ]
      }
    };
  },
  
  mounted() {
    this.fetchData();
  },
  
  methods: {
    // 获取数据
    fetchData() {
      this.loading = true;
      
      const params = {
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        ...this.searchForm
      };
      
      // 处理日期范围
      if (this.searchForm.dateRange && this.searchForm.dateRange.length === 2) {
        params.startDate = this.searchForm.dateRange[0];
        params.endDate = this.searchForm.dateRange[1];
        delete params.dateRange;
      }
      
      this.$request.get("/activityParticipant/selectPage", { params })
        .then(res => {
          if (res.code === '200') {
            this.tableData = res.data.list || [];
            this.total = res.data.total || 0;
          } else {
            this.$message.error(res.msg || "获取数据失败");
          }
        })
        .catch(() => {
          this.$message.error("获取数据失败");
        })
        .finally(() => {
          this.loading = false;
        });
    },
    
    // 查询
    handleSearch() {
      this.pageNum = 1;
      this.fetchData();
    },
    
    // 重置查询
    resetSearch() {
      this.$refs.searchForm.resetFields();
      this.pageNum = 1;
      this.fetchData();
      this.$message.success("已重置搜索条件，显示所有报名数据");
    },
    
    // 查询全部报名记录
    queryAllParticipants() {
      this.$refs.searchForm.resetFields();
      this.pageNum = 1;
      this.fetchData();
      this.$message.success("已显示所有报名数据");
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
    
    // 查看详情
    handleDetail(row) {
      this.currentParticipant = row;
      this.detailDialogVisible = true;
    },
    
    // 编辑记录
    handleEdit(row) {
      this.dialogTitle = "编辑报名记录";
      this.form = JSON.parse(JSON.stringify(row));
      this.dialogVisible = true;
      
      this.$nextTick(() => {
        if (this.$refs.form) {
          this.$refs.form.clearValidate();
        }
      });
    },
    
    // 签到
    handleCheckin(row) {
      this.$confirm('确认为该用户签到？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const now = new Date();
        const checkinTime = now.getFullYear() + '-' + 
                           String(now.getMonth() + 1).padStart(2, '0') + '-' + 
                           String(now.getDate()).padStart(2, '0') + ' ' + 
                           String(now.getHours()).padStart(2, '0') + ':' + 
                           String(now.getMinutes()).padStart(2, '0') + ':' + 
                           String(now.getSeconds()).padStart(2, '0');
        
        const updateData = {
          id: row.id,
          attendanceStatus: 'ATTENDED',
          checkinTime: checkinTime
        };
        
        this.$request.put("/activityParticipant/update", updateData)
          .then(res => {
            if (res.code === '200') {
              this.$message.success("签到成功");
              this.fetchData();
            } else {
              this.$message.error(res.msg || "签到失败");
            }
          })
          .catch(() => {
            this.$message.error("签到失败");
          });
      });
    },
    
    // 签退
    handleCheckout(row) {
      this.$confirm('确认为该用户签退？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const now = new Date();
        const checkoutTime = now.getFullYear() + '-' + 
                            String(now.getMonth() + 1).padStart(2, '0') + '-' + 
                            String(now.getDate()).padStart(2, '0') + ' ' + 
                            String(now.getHours()).padStart(2, '0') + ':' + 
                            String(now.getMinutes()).padStart(2, '0') + ':' + 
                            String(now.getSeconds()).padStart(2, '0');
        
        const updateData = {
          id: row.id,
          checkoutTime: checkoutTime
        };
        
        this.$request.put("/activityParticipant/update", updateData)
          .then(res => {
            if (res.code === '200') {
              this.$message.success("签退成功");
              this.fetchData();
            } else {
              this.$message.error(res.msg || "签退失败");
            }
          })
          .catch(() => {
            this.$message.error("签退失败");
          });
      });
    },
    
    // 删除记录
    handleDelete(row) {
      this.$confirm('此操作将永久删除该报名记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$request.delete(`/activityParticipant/delete/${row.id}`)
          .then(res => {
            if (res.code === '200') {
              this.$message.success("删除成功");
              this.fetchData();
            } else {
              this.$message.error(res.msg || "删除失败");
            }
          })
          .catch(() => {
            this.$message.error("删除失败");
          });
      });
    },
    

    
    // 批量删除
    handleBatchDelete() {
      if (this.selection.length === 0) {
        this.$message.warning("请选择要删除的记录");
        return;
      }
      
      this.$confirm(`此操作将永久删除选中的 ${this.selection.length} 条记录, 是否继续?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const ids = this.selection.map(item => item.id);
        this.$request.delete("/activityParticipant/delete/batch", { data: ids })
          .then(res => {
            if (res.code === '200') {
              this.$message.success(`批量删除成功，共删除 ${this.selection.length} 条记录`);
              this.fetchData();
            } else {
              this.$message.error(res.msg || "批量删除失败");
            }
          })
          .catch(() => {
            this.$message.error("批量删除失败");
          });
      });
    },
    
    // 提交表单
    handleSubmit() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.submitLoading = true;
          
          this.$request.put("/activityParticipant/update", this.form)
            .then(res => {
              if (res.code === '200') {
                this.$message.success("更新成功");
                this.dialogVisible = false;
                this.fetchData();
              } else {
                this.$message.error(res.msg || "更新失败");
              }
            })
            .catch(() => {
              this.$message.error("更新失败");
            })
            .finally(() => {
              this.submitLoading = false;
            });
        }
      });
    },
    
    // 格式化日期时间
    formatDateTime(dateTime) {
      if (!dateTime) return '';
      return dateTime.replace('T', ' ').substring(0, 19);
    },
    
    // 获取状态标签类型
    getStatusTagType(status) {
      const statusMap = {
        'REGISTERED': 'info',
        'ATTENDED': 'success',
        'ABSENT': 'warning',
        'CANCELLED': 'danger'
      };
      return statusMap[status] || 'info';
    },
    
    // 获取状态名称
    getStatusName(status) {
      const statusMap = {
        'REGISTERED': '已报名',
        'ATTENDED': '已参加',
        'ABSENT': '缺席',
        'CANCELLED': '已取消'
      };
      return statusMap[status] || '未知';
    }
  }
};
</script>

<style scoped>
.el-rate {
  display: inline-flex;
  align-items: center;
}

.el-table .cell {
  padding: 0 8px;
}

.space-x-1 > * + * {
  margin-left: 0.25rem;
}

.space-x-3 > * + * {
  margin-left: 0.75rem;
}

.space-y-2 > * + * {
  margin-top: 0.5rem;
}

.space-y-3 > * + * {
  margin-top: 0.75rem;
}

.space-y-6 > * + * {
  margin-top: 1.5rem;
}

.grid {
  display: grid;
}

.grid-cols-2 {
  grid-template-columns: repeat(2, minmax(0, 1fr));
}

.gap-4 {
  gap: 1rem;
}
</style>