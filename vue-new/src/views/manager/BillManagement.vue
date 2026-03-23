<template>
  <div>
    <!-- 功能说明 -->
    <div class="bg-white rounded-lg shadow-sm p-6 mb-5">
      <div class="flex items-center">
        <i class="el-icon-wallet text-3xl text-blue-500 mr-3"></i>
        <div>
          <h2 class="text-xl font-bold text-gray-800 mb-1">费用管理</h2>
          <p class="text-gray-600">本模块用于管理老年人的入住费用、活动费用等，支持生成账单、查看支付状态等功能</p>
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
                v-model="searchForm.userName" 
                placeholder="请输入用户姓名" 
                prefix-icon="el-icon-user" 
                clearable
                size="small"
              ></el-input>
            </el-form-item>
            <el-form-item>
              <el-select 
                v-model="searchForm.billType" 
                placeholder="费用类型"
                clearable
                size="small"
              >
                <el-option label="入住费用" value="ACCOMMODATION"></el-option>
                <el-option label="活动费用" value="ACTIVITY"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-select 
                v-model="searchForm.paymentStatus" 
                placeholder="支付状态"
                clearable
                size="small"
              >
                <el-option label="未支付" value="UNPAID"></el-option>
                <el-option label="已支付" value="PAID"></el-option>
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
              <el-button type="success" @click="queryAllBills" icon="el-icon-menu" size="small">查询全部</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div class="w-full lg:w-auto flex justify-end space-x-3">
          <el-button type="success" @click="handleBatchGenerate" icon="el-icon-wallet" size="small">批量生成账单</el-button>
          <el-button type="danger" @click="handleBatchDelete" :disabled="selection.length === 0" icon="el-icon-delete" size="small">
            批量删除
          </el-button>
        </div>
      </div>
    </div>

    <!-- 费用记录表格 -->
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
        <el-table-column prop="userName" label="用户姓名" min-width="120"></el-table-column>
        <el-table-column label="费用类型" width="120">
          <template slot-scope="scope">
            <el-tag :type="getBillTypeColor(scope.row.billType)" size="small">
              {{ getBillTypeName(scope.row.billType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="relatedName" label="关联项目" min-width="150"></el-table-column>
        <el-table-column label="费用金额" width="120" align="center">
          <template slot-scope="scope">
            <span class="text-red-600 font-bold">¥{{ scope.row.amount }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="费用描述" min-width="180" show-overflow-tooltip></el-table-column>
        <el-table-column label="支付状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getPaymentStatusColor(scope.row.paymentStatus)" size="small">
              {{ getPaymentStatusName(scope.row.paymentStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="generatedDate" label="生成日期" width="120"></el-table-column>
        <el-table-column label="支付日期" width="160">
          <template slot-scope="scope">
            <span v-if="scope.row.paymentDate">{{ formatDateTime(scope.row.paymentDate) }}</span>
            <span v-else class="text-gray-400">未支付</span>
          </template>
        </el-table-column>
        <el-table-column prop="generatedBy" label="生成人" width="100"></el-table-column>
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

    <!-- 添加/编辑账单对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="600px" :close-on-click-modal="false" custom-class="rounded-lg">
      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="用户" prop="userId">
              <el-select v-model="form.userId" placeholder="请选择用户" filterable @change="handleUserChange">
                <el-option
                  v-for="item in userOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="费用类型" prop="billType">
              <el-select v-model="form.billType" placeholder="请选择费用类型" @change="handleBillTypeChange">
                <el-option label="入住费用" value="ACCOMMODATION"></el-option>
                <el-option label="活动费用" value="ACTIVITY"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="关联项目" prop="relatedId">
              <el-select v-model="form.relatedId" placeholder="请选择关联项目" filterable @change="handleRelatedChange">
                <el-option
                  v-for="item in relatedOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="费用金额" prop="amount">
              <el-input-number v-model="form.amount" :precision="2" :step="1" :min="0" style="width: 100%;"></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="生成日期" prop="generatedDate">
              <el-date-picker
                v-model="form.generatedDate"
                type="date"
                placeholder="选择生成日期"
                style="width: 100%;"
                value-format="yyyy-MM-dd">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="支付状态" prop="paymentStatus">
              <el-select v-model="form.paymentStatus" placeholder="请选择支付状态">
                <el-option label="未支付" value="UNPAID"></el-option>
                <el-option label="已支付" value="PAID"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="费用描述" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入费用描述"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="flex justify-end space-x-3">
        <el-button @click="dialogVisible = false" icon="el-icon-close">取 消</el-button>
        <el-button type="primary" @click="saveBill" :loading="submitLoading" icon="el-icon-check">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 详情对话框 -->
    <el-dialog title="账单详情" :visible.sync="detailDialogVisible" width="600px" custom-class="rounded-lg">
      <div v-if="currentRecord" class="px-5">
        <el-descriptions class="margin-top" :column="2" border>
          <el-descriptions-item>
            <template slot="label"><i class="el-icon-user"></i> 用户姓名</template>
            {{ currentRecord.userName }}
          </el-descriptions-item>
          <el-descriptions-item>
            <template slot="label"><i class="el-icon-collection-tag"></i> 费用类型</template>
            <el-tag :type="getBillTypeColor(currentRecord.billType)">{{ getBillTypeName(currentRecord.billType) }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item>
            <template slot="label"><i class="el-icon-link"></i> 关联项目</template>
            {{ currentRecord.relatedName }}
          </el-descriptions-item>
          <el-descriptions-item>
            <template slot="label"><i class="el-icon-money"></i> 费用金额</template>
            <span class="text-red-600 font-bold">¥{{ currentRecord.amount }}</span>
          </el-descriptions-item>
          <el-descriptions-item>
            <template slot="label"><i class="el-icon-s-flag"></i> 支付状态</template>
            <el-tag :type="getPaymentStatusColor(currentRecord.paymentStatus)">{{ getPaymentStatusName(currentRecord.paymentStatus) }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item>
            <template slot="label"><i class="el-icon-date"></i> 生成日期</template>
            {{ currentRecord.generatedDate }}
          </el-descriptions-item>
          <el-descriptions-item>
            <template slot="label"><i class="el-icon-date"></i> 支付日期</template>
            {{ formatDateTime(currentRecord.paymentDate) || '未支付' }}
          </el-descriptions-item>
          <el-descriptions-item>
            <template slot="label"><i class="el-icon-user-solid"></i> 生成人</template>
            {{ currentRecord.generatedBy }}
          </el-descriptions-item>
          <el-descriptions-item :span="2">
            <template slot="label"><i class="el-icon-document"></i> 费用描述</template>
            {{ currentRecord.description || '无' }}
          </el-descriptions-item>
        </el-descriptions>
      </div>
      <div slot="footer" class="flex justify-end space-x-3">
        <el-button @click="detailDialogVisible = false" icon="el-icon-close">关 闭</el-button>
        <el-button type="primary" @click="handleEdit(currentRecord)" icon="el-icon-edit">编辑账单</el-button>
      </div>
    </el-dialog>

    <!-- 批量生成账单对话框 -->
    <el-dialog title="批量生成账单" :visible.sync="batchGenerateDialogVisible" width="800px" :close-on-click-modal="false" custom-class="rounded-lg">
      <div class="space-y-4">
        <!-- 用户选择 -->
        <div class="bg-gray-50 p-4 rounded-lg">
          <el-form :inline="true">
            <el-form-item label="选择用户：" label-width="80px">
              <el-select v-model="batchForm.selectedUserId" placeholder="请选择用户" @change="loadUserBillableItems" clearable filterable style="width: 300px;">
                <el-option
                  v-for="item in userOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-form>
        </div>

        <!-- 可收费项目 -->
        <div v-if="batchForm.selectedUserId" class="space-y-4">
          <!-- 入住费用 -->
          <div class="bg-white border border-gray-200 rounded-lg p-4">
            <h4 class="text-lg font-semibold text-gray-800 mb-3 flex items-center">
              <i class="el-icon-house text-blue-500 mr-2"></i>
              入住费用项目
            </h4>
            <div v-if="billableItems.accommodation.length > 0" class="space-y-2">
              <div v-for="item in billableItems.accommodation" :key="'acc-' + item.id" 
                   class="flex items-center justify-between p-3 border rounded hover:bg-gray-50">
                <div class="flex items-center">
                  <el-checkbox v-model="item.selected" @change="updateTotalAmount"></el-checkbox>
                  <div class="ml-3">
                    <div class="font-medium text-gray-900">{{ item.roomNumber }}房间</div>
                    <div class="text-sm text-gray-500">入住日期: {{ item.checkinDate }}</div>
                  </div>
                </div>
                <div class="text-right">
                  <div class="text-sm text-gray-500">月费用: ¥{{ parseFloat(item.amount || 0).toFixed(2) }}</div>
                  <el-input-number v-model="item.amount" :precision="2" :step="100" :min="0" size="mini" 
                                   @change="updateTotalAmount" style="width: 120px;" placeholder="可修改"></el-input-number>
                </div>
              </div>
            </div>
            <div v-else class="text-gray-500 text-center py-4">该用户暂无未收费的入住记录</div>
          </div>

          <!-- 活动费用 -->
          <div class="bg-white border border-gray-200 rounded-lg p-4">
            <h4 class="text-lg font-semibold text-gray-800 mb-3 flex items-center">
              <i class="el-icon-trophy text-green-500 mr-2"></i>
              活动费用项目
            </h4>
            <div v-if="billableItems.activity.length > 0" class="space-y-2">
              <div v-for="item in billableItems.activity" :key="'act-' + item.id" 
                   class="flex items-center justify-between p-3 border rounded hover:bg-gray-50">
                <div class="flex items-center">
                  <el-checkbox v-model="item.selected" @change="updateTotalAmount"></el-checkbox>
                  <div class="ml-3">
                    <div class="font-medium text-gray-900">{{ item.activityTitle }}</div>
                    <div class="text-sm text-gray-500">活动日期: {{ item.activityDate || '未设置' }}</div>
                  </div>
                </div>
                <div class="text-right">
                  <div class="text-sm text-gray-500">活动费用: ¥{{ parseFloat(item.amount || 0).toFixed(2) }}</div>
                  <el-input-number v-model="item.amount" :precision="2" :step="10" :min="0" size="mini" 
                                   @change="updateTotalAmount" style="width: 120px;" placeholder="可修改"></el-input-number>
                </div>
              </div>
            </div>
            <div v-else class="text-gray-500 text-center py-4">该用户暂无已参加且未收费的活动</div>
          </div>

          <!-- 总计 -->
          <div class="bg-blue-50 border border-blue-200 rounded-lg p-4">
            <div class="flex justify-between items-center">
              <span class="text-lg font-semibold text-gray-800">选中项目总金额：</span>
              <span class="text-2xl font-bold text-red-600">¥{{ batchForm.totalAmount.toFixed(2) }}</span>
            </div>
            <div class="text-sm text-gray-600 mt-1">
              已选择 {{ getSelectedItemsCount() }} 个费用项目
            </div>
          </div>
        </div>
      </div>

      <div slot="footer" class="flex justify-end space-x-3">
        <el-button @click="closeBatchGenerateDialog" icon="el-icon-close">取 消</el-button>
        <el-button type="primary" @click="confirmBatchGenerate" :loading="batchSubmitLoading" 
                   :disabled="getSelectedItemsCount() === 0" icon="el-icon-check">
          生成 {{ getSelectedItemsCount() }} 个账单
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "BillManagement",
  data() {
    return {
      // 搜索表单
      searchForm: {
        userName: "",
        billType: "",
        paymentStatus: "",
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
      dialogTitle: "生成账单",
      isEdit: false,
      submitLoading: false,
      
      // 详情对话框
      detailDialogVisible: false,
      currentRecord: null,
      
      // 批量生成账单对话框
      batchGenerateDialogVisible: false,
      batchSubmitLoading: false,
      batchForm: {
        selectedUserId: null,
        totalAmount: 0
      },
      // 可收费项目
      billableItems: {
        accommodation: [], // 入住费用项目
        activity: []       // 活动费用项目
      },
      
      // 用户选项
      userOptions: [],
      // 关联项目选项
      relatedOptions: [],
      
      // 表单
      form: {
        id: null,
        userId: null,
        userName: "",
        billType: "",
        relatedId: null,
        relatedName: "",
        amount: 0,
        description: "",
        paymentStatus: "UNPAID",
        generatedDate: "",
        generatedBy: ""
      },
      
      // 表单验证规则
      rules: {
        userId: [
          { required: true, message: "请选择用户", trigger: "change" }
        ],
        billType: [
          { required: true, message: "请选择费用类型", trigger: "change" }
        ],
        relatedId: [
          { required: true, message: "请选择关联项目", trigger: "change" }
        ],
        amount: [
          { required: true, message: "请输入费用金额", trigger: "blur" }
        ],
        generatedDate: [
          { required: true, message: "请选择生成日期", trigger: "change" }
        ]
      }
    };
  },
  created() {
    this.fetchData();
    this.fetchUsers();
  },
  methods: {
    // 获取数据
    fetchData() {
      this.loading = true;
      
      const params = {
        userName: this.searchForm.userName,
        billType: this.searchForm.billType,
        paymentStatus: this.searchForm.paymentStatus,
        pageNum: this.pageNum,
        pageSize: this.pageSize
      };

      // 处理日期范围
      if (this.searchForm.dateRange && this.searchForm.dateRange.length === 2) {
        params.startDate = this.searchForm.dateRange[0];
        params.endDate = this.searchForm.dateRange[1];
      }
      
      this.$request.get('/billManagement/selectPage', {params})
        .then(res => {
          if (res.code === "200") {
            this.tableData = res.data.list || res.data;
            this.total = res.data.total || 0;
          } else {
            this.$message.error(res.msg || '获取数据失败');
          }
          this.loading = false;
        })
        .catch(err => {
          console.error('获取费用数据失败:', err);
          this.$message.error('获取数据失败，请检查网络连接');
          this.loading = false;
        });
    },
    
    // 获取用户列表
    fetchUsers() {
      this.$request.get('/user/selectAll').then(res => {
        if (res.code === '200') {
          this.userOptions = res.data.map(item => {
            return { value: item.id, label: item.name };
          });
        }
      }).catch(err => {
        console.error('获取用户列表失败:', err);
      });
    },

    // 根据费用类型加载关联项目
    loadRelatedOptions(billType, userId) {
      this.relatedOptions = [];
      if (!billType || !userId) return;

      if (billType === 'ACCOMMODATION') {
        // 加载入住记录
        this.$request.get('/checkinRecord/selectAll', {
          params: { userId: userId }
        }).then(res => {
          if (res.code === '200') {
            this.relatedOptions = res.data.map(item => ({
              value: item.id,
              label: `${item.roomNumber}房间`,
              name: `${item.roomNumber}房间`
            }));
          }
        });
      } else if (billType === 'ACTIVITY') {
        // 加载已参加的活动（只有已参加的活动才能收费）
        this.$request.get('/activityParticipant/selectAll', {
          params: { userId: userId, attendanceStatus: 'ATTENDED' }
        }).then(res => {
          if (res.code === '200') {
            if (res.data && res.data.length > 0) {
              this.relatedOptions = res.data.map(item => ({
                value: item.id,
                label: `${item.activityTitle} (${item.activityDate || '未设置日期'})`,
                name: item.activityTitle
              }));
            } else {
              this.relatedOptions = [];
              this.$message.info('该用户暂无已参加的活动，无法生成活动费用账单');
            }
          }
        }).catch(() => {
          this.relatedOptions = [];
          this.$message.warning('获取活动参与记录失败');
        });
      }
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
      this.$message.success("已重置搜索条件");
    },
    
    // 查询全部
    queryAllBills() {
      this.$refs.searchForm.resetFields();
      this.pageNum = 1;
      this.fetchData();
      this.$message.success("已显示所有费用记录");
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
    
    // 生成账单
    handleAdd() {
      this.dialogTitle = "生成账单";
      this.isEdit = false;
      this.form = {
        id: null,
        userId: null,
        userName: "",
        billType: "",
        relatedId: null,
        relatedName: "",
        amount: 0,
        description: "",
        paymentStatus: "UNPAID",
        generatedDate: new Date().toISOString().split('T')[0],
        generatedBy: JSON.parse(localStorage.getItem("xm-user") || "{}").name || "admin"
      };
      this.relatedOptions = [];
      this.dialogVisible = true;
      
      this.$nextTick(() => {
        if (this.$refs.form) {
          this.$refs.form.clearValidate();
        }
      });
    },
    
    // 编辑账单
    handleEdit(row) {
      this.dialogTitle = "编辑账单";
      this.isEdit = true;
      this.$request.get('/billManagement/selectById/' + row.id).then(res => {
        if (res.code === "200") {
          this.form = res.data;
          // 加载关联项目选项
          this.loadRelatedOptions(this.form.billType, this.form.userId);
          this.dialogVisible = true;
          
          this.$nextTick(() => {
            if (this.$refs.form) {
              this.$refs.form.clearValidate();
            }
          });
        } else {
          this.$message.error(res.msg || '获取详情失败');
        }
      }).catch(() => {
        this.$message.error('获取详情失败');
      });
    },
    
    // 查看详情
    handleDetail(row) {
      this.$request.get('/billManagement/selectById/' + row.id).then(res => {
        if (res.code === '200') {
          this.currentRecord = res.data;
          this.detailDialogVisible = true;
        } else {
          this.$message.error(res.msg || '获取详情失败');
        }
      }).catch(() => {
        this.$message.error('获取详情失败');
      });
    },
    
    // 用户选择变化
    handleUserChange(userId) {
      const user = this.userOptions.find(item => item.value === userId);
      if (user) {
        this.form.userName = user.label;
      }
      // 重新加载关联项目
      if (this.form.billType) {
        this.loadRelatedOptions(this.form.billType, userId);
      }
      this.form.relatedId = null;
      this.form.relatedName = "";
    },

    // 费用类型变化
    handleBillTypeChange(billType) {
      if (this.form.userId) {
        this.loadRelatedOptions(billType, this.form.userId);
      }
      this.form.relatedId = null;
      this.form.relatedName = "";
    },

    // 关联项目变化
    handleRelatedChange(relatedId) {
      const related = this.relatedOptions.find(item => item.value === relatedId);
      if (related) {
        this.form.relatedName = related.name;
      }
    },
    
    // 保存账单
    saveBill() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.submitLoading = true;
          
          const url = this.isEdit ? '/billManagement/update' : '/billManagement/add';
          const method = this.isEdit ? 'put' : 'post';
          
          this.$request[method](url, this.form).then(res => {
            if (res.code === '200') {
              this.$message.success(this.isEdit ? '编辑成功' : '生成成功');
              this.dialogVisible = false;
              this.fetchData();
            } else {
              this.$message.error(res.msg || (this.isEdit ? '编辑失败' : '生成失败'));
            }
            this.submitLoading = false;
          }).catch(() => {
            this.$message.error((this.isEdit ? '编辑' : '生成') + '失败');
            this.submitLoading = false;
          });
        }
      });
    },
    
    // 删除账单
    handleDelete(row) {
      this.$confirm("确定删除该费用记录吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        this.$request.delete('/billManagement/delete/' + row.id).then(res => {
          if (res.code === '200') {
            this.$message.success("删除成功");
            this.fetchData();
          } else {
            this.$message.error(res.msg || '删除失败');
          }
        }).catch(() => {
          this.$message.error('删除失败');
        });
      }).catch(() => {});
    },
    
    // 批量删除
    handleBatchDelete() {
      if (this.selection.length === 0) {
        this.$message.warning("请选择要删除的记录");
        return;
      }
      
      this.$confirm(`确定删除选中的 ${this.selection.length} 条记录吗?`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        const ids = this.selection.map(item => item.id);
        
        this.$request.delete('/billManagement/delete/batch', {
          data: ids
        }).then(res => {
          if (res.code === '200') {
            this.$message.success("批量删除成功");
            this.fetchData();
          } else {
            this.$message.error(res.msg || '批量删除失败');
          }
        }).catch(() => {
          this.$message.error('批量删除失败');
        });
      }).catch(() => {});
    },
    
    // 格式化日期时间
    formatDateTime(dateTime) {
      if (!dateTime) return '';
      return dateTime.replace('T', ' ').substring(0, 19);
    },
    
    // 获取费用类型颜色
    getBillTypeColor(billType) {
      switch (billType) {
        case 'ACCOMMODATION':
          return 'primary';
        case 'ACTIVITY':
          return 'success';
        default:
          return '';
      }
    },
    
    // 获取费用类型名称
    getBillTypeName(billType) {
      switch (billType) {
        case 'ACCOMMODATION':
          return '入住费用';
        case 'ACTIVITY':
          return '活动费用';
        default:
          return '未知';
      }
    },
    
    // 获取支付状态颜色
    getPaymentStatusColor(status) {
      switch (status) {
        case 'PAID':
          return 'success';
        case 'UNPAID':
          return 'danger';
        default:
          return '';
      }
    },
    
    // 获取支付状态名称
    getPaymentStatusName(status) {
      switch (status) {
        case 'PAID':
          return '已支付';
        case 'UNPAID':
          return '未支付';
        default:
          return '未知';
      }
    },

    // 批量生成账单
    handleBatchGenerate() {
      this.batchGenerateDialogVisible = true;
      this.batchForm = {
        selectedUserId: null,
        totalAmount: 0
      };
      this.billableItems = {
        accommodation: [],
        activity: []
      };
    },

    // 加载用户可收费项目
    loadUserBillableItems(userId) {
      if (!userId) {
        this.billableItems = { accommodation: [], activity: [] };
        return;
      }

      const selectedUser = this.userOptions.find(user => user.value === userId);
      if (!selectedUser) return;

      // 加载入住费用项目（未收费的入住记录）
      this.$request.get('/checkinRecord/selectAll', {
        params: { userId: userId }
      }).then(res => {
        if (res.code === '200') {
          // 过滤掉已经生成过账单的入住记录
          this.billableItems.accommodation = res.data.map(item => ({
            id: item.id,
            roomNumber: item.roomNumber,
            checkinDate: item.checkinDate,
            amount: parseFloat(item.monthlyFee || 0), // 使用真实的月费用
            selected: false,
            type: 'ACCOMMODATION',
            relatedName: `${item.roomNumber}房间`,
            description: `${item.roomNumber}房间入住费用 (月费: ¥${item.monthlyFee || 0})`
          }));
        }
      }).catch(() => {
        this.$message.warning('获取入住记录失败');
      });

      // 加载活动费用项目（已参加未收费的活动）
      this.$request.get('/activityParticipant/selectAll', {
        params: { userId: userId, attendanceStatus: 'ATTENDED' }
      }).then(res => {
        if (res.code === '200') {
          this.billableItems.activity = res.data.map(item => ({
            id: item.id,
            activityTitle: item.activityTitle,
            activityDate: item.activityDate,
            amount: parseFloat(item.cost || 0), // 使用真实的活动费用
            selected: false,
            type: 'ACTIVITY',
            relatedName: item.activityTitle,
            description: `${item.activityTitle}活动参与费用 (费用: ¥${item.cost || 0})`
          }));
        }
      }).catch(() => {
        this.$message.warning('获取活动参与记录失败');
      });
    },

    // 更新总金额
    updateTotalAmount() {
      let total = 0;
      
      // 计算选中的入住费用
      this.billableItems.accommodation.forEach(item => {
        if (item.selected) {
          total += parseFloat(item.amount || 0);
        }
      });
      
      // 计算选中的活动费用
      this.billableItems.activity.forEach(item => {
        if (item.selected) {
          total += parseFloat(item.amount || 0);
        }
      });
      
      this.batchForm.totalAmount = total;
    },

    // 获取选中项目数量
    getSelectedItemsCount() {
      let count = 0;
      count += this.billableItems.accommodation.filter(item => item.selected).length;
      count += this.billableItems.activity.filter(item => item.selected).length;
      return count;
    },

    // 关闭批量生成对话框
    closeBatchGenerateDialog() {
      this.batchGenerateDialogVisible = false;
      this.batchForm = {
        selectedUserId: null,
        totalAmount: 0
      };
      this.billableItems = {
        accommodation: [],
        activity: []
      };
    },

    // 确认批量生成账单
    confirmBatchGenerate() {
      if (!this.batchForm.selectedUserId) {
        this.$message.warning('请先选择用户');
        return;
      }

      const selectedItems = [];
      
      // 收集选中的入住费用
      this.billableItems.accommodation.forEach(item => {
        if (item.selected) {
          selectedItems.push({
            userId: this.batchForm.selectedUserId,
            userName: this.userOptions.find(u => u.value === this.batchForm.selectedUserId).label,
            billType: 'ACCOMMODATION',
            relatedId: item.id,
            relatedName: item.relatedName,
            amount: item.amount,
            description: item.description,
            paymentStatus: 'UNPAID',
            generatedDate: new Date().toISOString().split('T')[0],
            generatedBy: JSON.parse(localStorage.getItem("xm-user") || "{}").name || "admin"
          });
        }
      });
      
      // 收集选中的活动费用
      this.billableItems.activity.forEach(item => {
        if (item.selected) {
          selectedItems.push({
            userId: this.batchForm.selectedUserId,
            userName: this.userOptions.find(u => u.value === this.batchForm.selectedUserId).label,
            billType: 'ACTIVITY',
            relatedId: item.id,
            relatedName: item.relatedName,
            amount: item.amount,
            description: item.description,
            paymentStatus: 'UNPAID',
            generatedDate: new Date().toISOString().split('T')[0],
            generatedBy: JSON.parse(localStorage.getItem("xm-user") || "{}").name || "admin"
          });
        }
      });

      if (selectedItems.length === 0) {
        this.$message.warning('请至少选择一个费用项目');
        return;
      }

      this.batchSubmitLoading = true;

      // 批量提交账单
      this.$request.post('/billManagement/batchAdd', selectedItems).then(res => {
        if (res.code === '200') {
          this.$message.success(`成功生成 ${selectedItems.length} 个账单！`);
          this.closeBatchGenerateDialog();
          this.fetchData(); // 刷新列表
        } else {
          this.$message.error(res.msg || '批量生成账单失败');
        }
        this.batchSubmitLoading = false;
      }).catch(() => {
        this.$message.error('批量生成账单失败');
        this.batchSubmitLoading = false;
      });
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
