<template>
  <div>
    <!-- 页面标题 -->
    <div class="bg-gradient-to-r from-blue-50 to-purple-50 rounded-lg p-6 mb-6">
      <div class="flex items-center">
        <i class="el-icon-wallet text-3xl text-blue-500 mr-3"></i>
        <div>
          <h2 class="text-xl font-bold text-gray-800 mb-1">费用支付</h2>
          <p class="text-gray-600">查看和支付您的入住费用、活动费用等账单</p>
        </div>
      </div>
    </div>

    <!-- 费用统计卡片 -->
    <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mb-6">
      <div class="bg-white rounded-lg shadow-sm p-6 border-l-4 border-red-400">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-sm text-gray-600 mb-1">待支付金额</p>
            <p class="text-2xl font-bold text-red-600">¥{{ unpaidAmount }}</p>
          </div>
          <div class="bg-red-100 p-3 rounded-full">
            <i class="el-icon-warning text-red-500 text-xl"></i>
          </div>
        </div>
      </div>
      
      <div class="bg-white rounded-lg shadow-sm p-6 border-l-4 border-green-400">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-sm text-gray-600 mb-1">已支付金额</p>
            <p class="text-2xl font-bold text-green-600">¥{{ paidAmount }}</p>
          </div>
          <div class="bg-green-100 p-3 rounded-full">
            <i class="el-icon-circle-check text-green-500 text-xl"></i>
          </div>
        </div>
      </div>
      
      <div class="bg-white rounded-lg shadow-sm p-6 border-l-4 border-blue-400">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-sm text-gray-600 mb-1">账单总数</p>
            <p class="text-2xl font-bold text-blue-600">{{ totalBills }}</p>
          </div>
          <div class="bg-blue-100 p-3 rounded-full">
            <i class="el-icon-document text-blue-500 text-xl"></i>
          </div>
        </div>
      </div>
    </div>

    <!-- 搜索和操作区域 -->
    <div class="bg-white rounded-lg shadow-sm p-6 mb-6">
      <div class="flex flex-wrap items-center justify-between">
        <div class="w-full lg:w-auto mb-4 lg:mb-0">
          <el-form :inline="true" :model="searchForm" ref="searchForm">
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
              <el-button type="primary" @click="handleSearch" icon="el-icon-search" size="small" class="mr-2">查询</el-button>
              <el-button @click="resetSearch" icon="el-icon-refresh" size="small">重置</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div class="w-full lg:w-auto flex justify-end space-x-3">
          <el-button 
            type="danger" 
            @click="handleBatchPay" 
            :disabled="selectedUnpaidBills.length === 0" 
            icon="el-icon-money" 
            size="small"
          >
            批量支付 ({{ selectedUnpaidBills.length }})
          </el-button>
        </div>
      </div>
    </div>

    <!-- 账单列表 -->
    <div class="bg-white rounded-lg shadow-sm overflow-hidden">
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
        <el-table-column type="selection" width="55" :selectable="isSelectableRow"></el-table-column>
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
            <span class="text-red-600 font-bold text-lg">¥{{ scope.row.amount }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="费用描述" min-width="200" show-overflow-tooltip></el-table-column>
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
        <el-table-column label="操作" fixed="right" width="180" align="center">
          <template slot-scope="scope">
            <div class="flex justify-center space-x-2">
              <el-button 
                v-if="scope.row.paymentStatus === 'UNPAID'" 
                type="success" 
                size="mini" 
                @click="handlePay(scope.row)" 
                icon="el-icon-money"
                class="transform hover:-translate-y-1 transition-transform"
              >
                立即支付
              </el-button>
              <el-button 
                type="info" 
                size="mini" 
                icon="el-icon-view" 
                circle 
                @click="handleDetail(scope.row)" 
                class="transform hover:-translate-y-1 transition-transform"
              ></el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 分页组件 -->
    <div class="bg-white rounded-lg shadow-sm p-4 flex justify-end mt-4">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-sizes="[10, 20, 50]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        background
      ></el-pagination>
    </div>

    <!-- 支付确认对话框 -->
    <el-dialog title="确认支付" :visible.sync="paymentDialogVisible" width="500px" :close-on-click-modal="false">
      <div v-if="currentBill" class="text-center">
        <div class="bg-blue-50 rounded-lg p-6 mb-6">
          <i class="el-icon-wallet text-4xl text-blue-500 mb-4"></i>
          <h3 class="text-lg font-bold text-gray-800 mb-2">支付确认</h3>
          <p class="text-gray-600">您即将支付以下费用</p>
        </div>
        
        <div class="text-left bg-gray-50 rounded-lg p-4 mb-6">
          <div class="flex justify-between items-center mb-2">
            <span class="text-gray-600">费用类型：</span>
            <el-tag :type="getBillTypeColor(currentBill.billType)" size="small">
              {{ getBillTypeName(currentBill.billType) }}
            </el-tag>
          </div>
          <div class="flex justify-between items-center mb-2">
            <span class="text-gray-600">关联项目：</span>
            <span class="font-medium">{{ currentBill.relatedName }}</span>
          </div>
          <div class="flex justify-between items-center mb-2">
            <span class="text-gray-600">费用描述：</span>
            <span class="font-medium">{{ currentBill.description }}</span>
          </div>
          <div class="flex justify-between items-center">
            <span class="text-gray-600">支付金额：</span>
            <span class="text-red-600 font-bold text-xl">¥{{ currentBill.amount }}</span>
          </div>
        </div>
        
        <div class="bg-yellow-50 border border-yellow-200 rounded-lg p-4 mb-6">
          <div class="flex items-center">
            <i class="el-icon-warning text-yellow-500 mr-2"></i>
            <span class="text-yellow-700 text-sm">这是模拟支付，实际项目中请接入真实的支付系统</span>
          </div>
        </div>
      </div>
      
      <div slot="footer" class="flex justify-center space-x-4">
        <el-button @click="paymentDialogVisible = false" icon="el-icon-close">取 消</el-button>
        <el-button type="success" @click="confirmPayment" :loading="paymentLoading" icon="el-icon-check">
          确认支付
        </el-button>
      </div>
    </el-dialog>

    <!-- 账单详情对话框 -->
    <el-dialog title="账单详情" :visible.sync="detailDialogVisible" width="600px">
      <div v-if="currentBill" class="px-5">
        <el-descriptions class="margin-top" :column="2" border>
          <el-descriptions-item>
            <template slot="label"><i class="el-icon-collection-tag"></i> 费用类型</template>
            <el-tag :type="getBillTypeColor(currentBill.billType)">{{ getBillTypeName(currentBill.billType) }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item>
            <template slot="label"><i class="el-icon-link"></i> 关联项目</template>
            {{ currentBill.relatedName }}
          </el-descriptions-item>
          <el-descriptions-item>
            <template slot="label"><i class="el-icon-money"></i> 费用金额</template>
            <span class="text-red-600 font-bold">¥{{ currentBill.amount }}</span>
          </el-descriptions-item>
          <el-descriptions-item>
            <template slot="label"><i class="el-icon-s-flag"></i> 支付状态</template>
            <el-tag :type="getPaymentStatusColor(currentBill.paymentStatus)">{{ getPaymentStatusName(currentBill.paymentStatus) }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item>
            <template slot="label"><i class="el-icon-date"></i> 生成日期</template>
            {{ currentBill.generatedDate }}
          </el-descriptions-item>
          <el-descriptions-item>
            <template slot="label"><i class="el-icon-date"></i> 支付日期</template>
            {{ formatDateTime(currentBill.paymentDate) || '未支付' }}
          </el-descriptions-item>
          <el-descriptions-item>
            <template slot="label"><i class="el-icon-user-solid"></i> 生成人</template>
            {{ currentBill.generatedBy }}
          </el-descriptions-item>
          <el-descriptions-item :span="2">
            <template slot="label"><i class="el-icon-document"></i> 费用描述</template>
            {{ currentBill.description || '无' }}
          </el-descriptions-item>
        </el-descriptions>
      </div>
      <div slot="footer" class="flex justify-end space-x-3">
        <el-button @click="detailDialogVisible = false" icon="el-icon-close">关 闭</el-button>
        <el-button 
          v-if="currentBill && currentBill.paymentStatus === 'UNPAID'" 
          type="success" 
          @click="handlePay(currentBill)" 
          icon="el-icon-money"
        >
          立即支付
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "Payment",
  data() {
    return {
      // 用户信息
      user: JSON.parse(localStorage.getItem("xm-user") || "{}"),
      
      // 搜索表单
      searchForm: {
        billType: "",
        paymentStatus: ""
      },
      
      // 表格数据
      tableData: [],
      loading: false,
      total: 0,
      pageNum: 1,
      pageSize: 10,
      
      // 选中项
      selection: [],
      selectedUnpaidBills: [],
      
      // 统计数据
      unpaidAmount: 0,
      paidAmount: 0,
      totalBills: 0,
      
      // 对话框
      paymentDialogVisible: false,
      detailDialogVisible: false,
      paymentLoading: false,
      currentBill: null
    };
  },
  created() {
    this.fetchData();
    this.fetchStatistics();
  },
  methods: {
    // 获取数据
    fetchData() {
      this.loading = true;
      
      const params = {
        userId: this.user.id,
        billType: this.searchForm.billType,
        paymentStatus: this.searchForm.paymentStatus,
        pageNum: this.pageNum,
        pageSize: this.pageSize
      };
      
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
          this.$message.error('获取数据失败');
          this.loading = false;
        });
    },
    
    // 获取统计数据
    fetchStatistics() {
      this.$request.get(`/billManagement/selectByUserId/${this.user.id}`)
        .then(res => {
          if (res.code === "200") {
            const bills = res.data || [];
            this.totalBills = bills.length;
            this.unpaidAmount = bills
              .filter(bill => bill.paymentStatus === 'UNPAID')
              .reduce((sum, bill) => sum + parseFloat(bill.amount || 0), 0);
            this.paidAmount = bills
              .filter(bill => bill.paymentStatus === 'PAID')
              .reduce((sum, bill) => sum + parseFloat(bill.amount || 0), 0);
          }
        })
        .catch(err => {
          console.error('获取统计数据失败:', err);
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
      this.selectedUnpaidBills = selection.filter(item => item.paymentStatus === 'UNPAID');
    },
    
    // 判断行是否可选中（只有未支付的可以选中）
    isSelectableRow(row) {
      return row.paymentStatus === 'UNPAID';
    },
    
    // 支付账单
    handlePay(row) {
      this.currentBill = row;
      this.paymentDialogVisible = true;
    },
    
    // 确认支付
    confirmPayment() {
      this.paymentLoading = true;
      
      this.$request.put(`/billManagement/pay/${this.currentBill.id}`)
        .then(res => {
          if (res.code === '200') {
            this.$message.success('支付成功！');
            this.paymentDialogVisible = false;
            this.fetchData();
            this.fetchStatistics();
            
            // 如果是入住费用，更新入住记录的缴费状态
            if (this.currentBill.billType === 'ACCOMMODATION') {
              this.updateCheckinPaymentStatus(this.currentBill.relatedId);
            }
          } else {
            this.$message.error(res.msg || '支付失败');
          }
          this.paymentLoading = false;
        })
        .catch(() => {
          this.$message.error('支付失败');
          this.paymentLoading = false;
        });
    },
    
    // 更新入住记录的缴费状态
    updateCheckinPaymentStatus(checkinRecordId) {
      this.$request.put('/checkinRecord/update', {
        id: checkinRecordId,
        paymentStatus: 'PAID'
      }).catch(err => {
        console.error('更新入住记录缴费状态失败:', err);
      });
    },
    
    // 批量支付
    handleBatchPay() {
      if (this.selectedUnpaidBills.length === 0) {
        this.$message.warning("请选择要支付的账单");
        return;
      }
      
      const totalAmount = this.selectedUnpaidBills.reduce((sum, bill) => sum + parseFloat(bill.amount || 0), 0);
      
      this.$confirm(`确定支付选中的 ${this.selectedUnpaidBills.length} 个账单，总金额：¥${totalAmount.toFixed(2)} 吗?`, "批量支付确认", {
        confirmButtonText: "确定支付",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        this.paymentLoading = true;
        
        const paymentPromises = this.selectedUnpaidBills.map(bill => 
          this.$request.put(`/billManagement/pay/${bill.id}`)
        );
        
        Promise.all(paymentPromises)
          .then(results => {
            const successCount = results.filter(res => res.code === '200').length;
            if (successCount === this.selectedUnpaidBills.length) {
              this.$message.success(`批量支付成功，共支付 ${successCount} 个账单！`);
              
              // 更新相关入住记录的缴费状态
              this.selectedUnpaidBills.forEach(bill => {
                if (bill.billType === 'ACCOMMODATION') {
                  this.updateCheckinPaymentStatus(bill.relatedId);
                }
              });
            } else {
              this.$message.warning(`部分支付成功，成功 ${successCount} 个，失败 ${this.selectedUnpaidBills.length - successCount} 个`);
            }
            this.fetchData();
            this.fetchStatistics();
            this.paymentLoading = false;
          })
          .catch(() => {
            this.$message.error('批量支付失败');
            this.paymentLoading = false;
          });
      }).catch(() => {});
    },
    
    // 查看详情
    handleDetail(row) {
      this.currentBill = row;
      this.detailDialogVisible = true;
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
    }
  }
};
</script>

<style scoped>
.grid {
  display: grid;
}

.grid-cols-1 {
  grid-template-columns: repeat(1, minmax(0, 1fr));
}

@media (min-width: 768px) {
  .md\:grid-cols-3 {
    grid-template-columns: repeat(3, minmax(0, 1fr));
  }
}

.gap-6 {
  gap: 1.5rem;
}

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
