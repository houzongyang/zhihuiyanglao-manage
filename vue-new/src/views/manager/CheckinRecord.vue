<template>
  <div>
    <!-- 功能说明 -->
    <div class="bg-white rounded-lg shadow-sm p-6 mb-5">
      <div class="flex items-center">
        <i class="el-icon-house text-3xl text-blue-500 mr-3"></i>
        <div>
          <h2 class="text-xl font-bold text-gray-800 mb-1">入住信息管理</h2>
          <p class="text-gray-600">本模块用于管理养老院入住信息，提供入住记录的增加、修改、删除和查询功能</p>
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
                v-model="searchForm.roomNumber" 
                placeholder="请输入房间号" 
                prefix-icon="el-icon-office-building" 
                clearable
                size="small"
              ></el-input>
            </el-form-item>
            <el-form-item>
              <el-select 
                v-model="searchForm.checkinStatus" 
                placeholder="入住状态"
                clearable
                size="small"
              >
                <el-option label="已入住" value="CHECKED_IN"></el-option>
                <el-option label="已退房" value="CHECKED_OUT"></el-option>
                <el-option label="已预订" value="RESERVED"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSearch" icon="el-icon-search" size="small" class="mr-2">查询</el-button>
              <el-button @click="resetSearch" icon="el-icon-refresh" size="small" class="mr-2">重置</el-button>
              <el-button type="success" @click="queryAllRecords" icon="el-icon-menu" size="small">查询全部</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div class="w-full lg:w-auto flex justify-end space-x-3">
          <el-button type="primary" @click="handleAdd" icon="el-icon-plus" size="small">添加入住记录</el-button>
          <el-button type="danger" @click="handleBatchDelete" :disabled="selection.length === 0" icon="el-icon-delete" size="small">
            批量删除
          </el-button>
        </div>
      </div>
    </div>

    <!-- 入住记录表格 -->
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
        <el-table-column prop="roomNumber" label="房间号" min-width="120"></el-table-column>
        <el-table-column prop="checkinDate" label="入住时间" min-width="120"></el-table-column>
        <el-table-column prop="checkoutDate" label="退房时间" min-width="120">
          <template slot-scope="scope">
            <span>{{ scope.row.checkoutDate || '未退房' }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="checkinStatus" label="入住状态" min-width="120">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.checkinStatus)">
              {{ getStatusText(scope.row.checkinStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="paymentStatus" label="缴费状态" min-width="120">
          <template slot-scope="scope">
            <el-tag :type="getPaymentStatusType(scope.row.paymentStatus)">
              {{ getPaymentStatusText(scope.row.paymentStatus) }}
            </el-tag>
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

    <!-- 添加/编辑入住记录对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="750px" :close-on-click-modal="false" custom-class="rounded-lg">
      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
        <el-tabs v-model="activeTab">
          <el-tab-pane label="基本信息" name="basic">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="用户" prop="userId">
                  <el-select v-model="form.userId" placeholder="请选择用户" filterable>
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
                <el-form-item label="房间号" prop="roomNumber">
                  <el-input v-model="form.roomNumber" placeholder="请输入房间号"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="入住时间" prop="checkinDate">
                  <el-date-picker
                    v-model="form.checkinDate"
                    type="date"
                    placeholder="选择入住时间"
                    style="width: 100%;"
                    value-format="yyyy-MM-dd">
                  </el-date-picker>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="退房时间" prop="checkoutDate">
                  <el-date-picker
                    v-model="form.checkoutDate"
                    type="date"
                    placeholder="选择退房时间"
                    style="width: 100%;"
                    value-format="yyyy-MM-dd">
                  </el-date-picker>
                </el-form-item>
              </el-col>
            </el-row>
            
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="入住状态" prop="checkinStatus">
                  <el-select v-model="form.checkinStatus" placeholder="请选择入住状态">
                    <el-option label="已入住" value="CHECKED_IN"></el-option>
                    <el-option label="已退房" value="CHECKED_OUT"></el-option>
                    <el-option label="已预订" value="RESERVED"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="房间类型" prop="roomType">
                  <el-input v-model="form.roomType" placeholder="请输入房间类型"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="床位号" prop="bedNumber">
                  <el-input v-model="form.bedNumber" placeholder="请输入床位号"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="特殊需求" prop="specialNeeds">
                  <el-input v-model="form.specialNeeds" placeholder="请输入特殊需求"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </el-tab-pane>
          
          <el-tab-pane label="费用信息" name="payment">
            <el-row :gutter="20">
              <el-col :span="8">
                <el-form-item label="月费用" prop="monthlyFee">
                  <el-input-number v-model="form.monthlyFee" :precision="2" :step="100" :min="0" style="width: 100%;"></el-input-number>
                </el-form-item>
              </el-col>
<!--              <el-col :span="8">-->
<!--                <el-form-item label="押金" prop="deposit">-->
<!--                  <el-input-number v-model="form.deposit" :precision="2" :step="100" :min="0" style="width: 100%;"></el-input-number>-->
<!--                </el-form-item>-->
<!--              </el-col>-->
              <el-col :span="8">
                <el-form-item label="已缴费用" prop="totalPaid">
                  <el-input-number v-model="form.totalPaid" :precision="2" :step="100" :min="0" style="width: 100%;"></el-input-number>
                </el-form-item>
              </el-col>
            </el-row>
            
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="缴费状态" prop="paymentStatus">
                  <el-select v-model="form.paymentStatus" placeholder="请选择缴费状态">
                    <el-option label="已缴费" value="PAID"></el-option>
                    <el-option label="未缴费" value="UNPAID"></el-option>
                    <el-option label="部分缴费" value="PARTIAL"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
          </el-tab-pane>
          
          <el-tab-pane label="紧急联系人" name="emergency">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="紧急联系人" prop="emergencyContact">
                  <el-input v-model="form.emergencyContact" placeholder="请输入紧急联系人姓名"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="紧急电话" prop="emergencyPhone">
                  <el-input v-model="form.emergencyPhone" placeholder="请输入紧急联系电话"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            
            <el-row :gutter="20">
              <el-col :span="24">
                <el-form-item label="备注信息" prop="remarks">
                  <el-input type="textarea" v-model="form.remarks" placeholder="请输入备注信息" :rows="4"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </el-tab-pane>
        </el-tabs>
      </el-form>
      <div slot="footer" class="flex justify-end space-x-3">
        <el-button @click="dialogVisible = false" icon="el-icon-close">取 消</el-button>
        <el-button type="primary" @click="saveRecord" :loading="submitLoading" icon="el-icon-check">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 详情对话框 -->
    <el-dialog title="入住记录详情" :visible.sync="detailDialogVisible" width="800px" custom-class="rounded-lg">
      <div v-if="currentRecord" class="px-5">
        <el-descriptions class="margin-top" :column="2" border>
          <el-descriptions-item>
            <template slot="label"><i class="el-icon-user"></i> 用户姓名</template>
            {{ currentRecord.userName }}
          </el-descriptions-item>
          <el-descriptions-item>
            <template slot="label"><i class="el-icon-office-building"></i> 房间号</template>
            {{ currentRecord.roomNumber }}
          </el-descriptions-item>
          <el-descriptions-item>
            <template slot="label"><i class="el-icon-date"></i> 入住时间</template>
            {{ currentRecord.checkinDate }}
          </el-descriptions-item>
          <el-descriptions-item>
            <template slot="label"><i class="el-icon-date"></i> 退房时间</template>
            {{ currentRecord.checkoutDate || '未退房' }}
          </el-descriptions-item>
          <el-descriptions-item>
            <template slot="label"><i class="el-icon-s-flag"></i> 入住状态</template>
            <el-tag :type="getStatusType(currentRecord.checkinStatus)">{{ getStatusText(currentRecord.checkinStatus) }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item>
            <template slot="label"><i class="el-icon-s-home"></i> 房间类型</template>
            {{ currentRecord.roomType || '未设置' }}
          </el-descriptions-item>
          <el-descriptions-item>
            <template slot="label"><i class="el-icon-s-grid"></i> 床位号</template>
            {{ currentRecord.bedNumber || '未设置' }}
          </el-descriptions-item>
          <el-descriptions-item>
            <template slot="label"><i class="el-icon-s-finance"></i> 月费用</template>
            ¥ {{ currentRecord.monthlyFee }}
          </el-descriptions-item>
          <el-descriptions-item>
            <template slot="label"><i class="el-icon-s-finance"></i> 押金</template>
            ¥ {{ currentRecord.deposit }}
          </el-descriptions-item>
          <el-descriptions-item>
            <template slot="label"><i class="el-icon-s-finance"></i> 已缴费用</template>
            ¥ {{ currentRecord.totalPaid }}
          </el-descriptions-item>
          <el-descriptions-item>
            <template slot="label"><i class="el-icon-s-finance"></i> 缴费状态</template>
            <el-tag :type="getPaymentStatusType(currentRecord.paymentStatus)">{{ getPaymentStatusText(currentRecord.paymentStatus) }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item>
            <template slot="label"><i class="el-icon-s-custom"></i> 紧急联系人</template>
            {{ currentRecord.emergencyContact || '未设置' }}
          </el-descriptions-item>
          <el-descriptions-item>
            <template slot="label"><i class="el-icon-mobile-phone"></i> 紧急联系电话</template>
            {{ currentRecord.emergencyPhone || '未设置' }}
          </el-descriptions-item>
          <el-descriptions-item :span="2">
            <template slot="label"><i class="el-icon-s-opportunity"></i> 特殊需求</template>
            {{ currentRecord.specialNeeds || '无' }}
          </el-descriptions-item>
          <el-descriptions-item :span="2">
            <template slot="label"><i class="el-icon-notebook-2"></i> 备注信息</template>
            {{ currentRecord.remarks || '无' }}
          </el-descriptions-item>
        </el-descriptions>
      </div>
      <div slot="footer" class="flex justify-end space-x-3">
        <el-button @click="detailDialogVisible = false" icon="el-icon-close">关 闭</el-button>
        <el-button type="primary" @click="handleEdit(currentRecord)" icon="el-icon-edit">编辑信息</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "CheckinRecordManage",
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
        roomNumber: "",
        checkinStatus: ""
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
      dialogTitle: "添加入住记录",
      isEdit: false,
      submitLoading: false,
      activeTab: "basic",  // 表单选项卡默认选中基本信息
      
      // 详情对话框
      detailDialogVisible: false,
      currentRecord: null,
      
      // 用户选项（实际项目中应从后端获取）
      userOptions: [
        { value: 1, label: '张三' }
      ],
      
      // 表单
      form: {
        id: null,
        userId: null,
        roomNumber: "",
        checkinDate: "",
        checkoutDate: "",
        checkinStatus: "CHECKED_IN",
        monthlyFee: 0,
        deposit: 0,
        totalPaid: 0,
        paymentStatus: "UNPAID",
        roomType: "",
        bedNumber: "",
        specialNeeds: "",
        emergencyContact: "",
        emergencyPhone: "",
        remarks: ""
      },
      
      // 表单验证规则
      rules: {
        userId: [
          { required: true, message: "请选择用户", trigger: "change" }
        ],
        roomNumber: [
          { required: true, message: "请输入房间号", trigger: "blur" }
        ],
        checkinDate: [
          { required: true, message: "请选择入住时间", trigger: "change" }
        ],
        checkinStatus: [
          { required: true, message: "请选择入住状态", trigger: "change" }
        ],
        monthlyFee: [
          { required: true, message: "请输入月费用", trigger: "blur" }
        ],
        emergencyPhone: [
          { validator: validatePhone, trigger: "blur" }
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
      
      // 构造查询参数
      const params = {
        roomNumber: this.searchForm.roomNumber,
        checkinStatus: this.searchForm.checkinStatus,
        pageNum: this.pageNum,
        pageSize: this.pageSize
      };
      
      console.log('请求入住记录数据，参数:', params);
      
      this.$request.get('/checkinRecord/selectPage', {params})
        .then(res => {
          console.log('入住记录返回结果:', res);
          if (res.code === "200") {
            this.tableData = res.data.list || res.data;
            this.total = res.data.total || 0;
          } else {
            this.$message.error(res.msg || '获取数据失败');
            console.error('获取入住记录数据失败:', res);
          }
          this.loading = false;
        })
        .catch(err => {
          console.error('获取入住记录接口错误:', err);
          this.$message.error('获取数据失败，请检查网络连接或后端服务');
          this.loading = false;
        });
    },
    
    // 获取用户列表
    fetchUsers() {
      this.$request.get('/user/selectAll').then(res => {
        console.log('获取用户列表结果:', res);
        if (res.code === '200') {
          this.userOptions = res.data.map(item => {
            return { value: item.id, label: item.name };
          });
        } else {
          console.error('获取用户列表失败:', res);
        }
      }).catch(err => {
        console.error('获取用户列表接口错误:', err);
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
      this.$message.success("已重置搜索条件");
    },
    
    // 查询全部
    queryAllRecords() {
      this.$refs.searchForm.resetFields();
      this.pageNum = 1;
      this.fetchData();
      this.$message.success("已显示所有入住记录");
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
    
    // 添加入住记录
    handleAdd() {
      this.dialogTitle = "添加入住记录";
      this.isEdit = false;
      this.form = {
        id: null,
        userId: null,
        roomNumber: "",
        checkinDate: "",
        checkoutDate: "",
        checkinStatus: "CHECKED_IN",
        monthlyFee: 0,
        deposit: 0,
        totalPaid: 0,
        paymentStatus: "UNPAID",
        roomType: "",
        bedNumber: "",
        specialNeeds: "",
        emergencyContact: "",
        emergencyPhone: "",
        remarks: "",
        createdBy: JSON.parse(localStorage.getItem("xm-user") || "{}").name || "admin"
      };
      this.activeTab = "basic";
      this.dialogVisible = true;
      
      // 清除验证
      this.$nextTick(() => {
        if (this.$refs.form) {
          this.$refs.form.clearValidate();
        }
      });
    },
    
    // 编辑入住记录
    handleEdit(row) {
      this.dialogTitle = "编辑入住记录";
      this.isEdit = true;
      // 获取最新的数据
      this.$request.get('/checkinRecord/selectById/' + row.id).then(res => {
        if (res.code === "200") {
          this.form = res.data;
          this.dialogVisible = true;
          
          // 清除验证
          this.$nextTick(() => {
            if (this.$refs.form) {
              this.$refs.form.clearValidate();
            }
          });
        } else {
          this.$message.error(res.msg || '获取详情失败');
        }
      }).catch(() => {
        this.$message.error('获取详情失败，请检查网络连接');
      });
    },
    
    // 查看详情
    handleDetail(row) {
      // 获取最新的数据
      this.$request.get('/checkinRecord/selectById/' + row.id).then(res => {
        if (res.code === '200') {
          this.currentRecord = res.data;
          this.detailDialogVisible = true;
        } else {
          this.$message.error(res.msg || '获取详情失败');
        }
      }).catch(() => {
        this.$message.error('获取详情失败，请检查网络连接');
      });
    },
    
    // 保存入住记录（添加或更新）
    saveRecord() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.submitLoading = true;
          
          const url = this.isEdit ? '/checkinRecord/update' : '/checkinRecord/add';
          const method = this.isEdit ? 'put' : 'post';
          
          this.$request[method](url, this.form).then(res => {
            if (res.code === '200') {
              this.$message.success(this.isEdit ? '编辑成功' : '添加成功');
              this.dialogVisible = false;
              this.fetchData(); // 刷新数据
            } else {
              this.$message.error(res.msg || (this.isEdit ? '编辑失败' : '添加失败'));
            }
            this.submitLoading = false;
          }).catch(() => {
            this.$message.error((this.isEdit ? '编辑' : '添加') + '失败，请检查网络连接');
            this.submitLoading = false;
          });
        }
      });
    },
    
    // 删除入住记录
    handleDelete(row) {
      this.$confirm("确定删除该入住记录吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        this.$request.delete('/checkinRecord/delete/' + row.id).then(res => {
          if (res.code === '200') {
            this.$message.success("删除成功");
            this.fetchData(); // 刷新数据
          } else {
            this.$message.error(res.msg || '删除失败');
          }
        }).catch(() => {
          this.$message.error('删除失败，请检查网络连接');
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
        
        this.$request.delete('/checkinRecord/delete/batch', {
          data: ids
        }).then(res => {
          if (res.code === '200') {
            this.$message.success("批量删除成功");
            this.fetchData(); // 刷新数据
          } else {
            this.$message.error(res.msg || '批量删除失败');
          }
        }).catch(() => {
          this.$message.error('批量删除失败，请检查网络连接');
        });
      }).catch(() => {});
    },
    
    // 获取入住状态类型
    getStatusType(status) {
      switch (status) {
        case 'CHECKED_IN':
          return 'success';
        case 'CHECKED_OUT':
          return 'info';
        case 'RESERVED':
          return 'warning';
        default:
          return '';
      }
    },
    
    // 获取入住状态文本
    getStatusText(status) {
      switch (status) {
        case 'CHECKED_IN':
          return '已入住';
        case 'CHECKED_OUT':
          return '已退房';
        case 'RESERVED':
          return '已预订';
        default:
          return '未知';
      }
    },
    
    // 获取缴费状态类型
    getPaymentStatusType(status) {
      switch (status) {
        case 'PAID':
          return 'success';
        case 'UNPAID':
          return 'danger';
        case 'PARTIAL':
          return 'warning';
        default:
          return '';
      }
    },
    
    // 获取缴费状态文本
    getPaymentStatusText(status) {
      switch (status) {
        case 'PAID':
          return '已缴费';
        case 'UNPAID':
          return '未缴费';
        case 'PARTIAL':
          return '部分缴费';
        default:
          return '未知';
      }
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