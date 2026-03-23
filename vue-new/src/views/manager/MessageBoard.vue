<template>
  <div>
    <!-- 功能说明 -->
    <div class="bg-white rounded-lg shadow-sm p-6 mb-5">
      <div class="flex items-center">
        <i class="el-icon-chat-dot-round text-3xl text-blue-500 mr-3"></i>
        <div>
          <h2 class="text-xl font-bold text-gray-800 mb-1">留言板管理</h2>
          <p class="text-gray-600">本模块用于管理系统留言板，提供留言的查看、审核、回复、删除等功能，支持主留言和回复的层级管理</p>
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
                placeholder="请输入留言标题" 
                prefix-icon="el-icon-chat-dot-round" 
                clearable
                size="small"
              ></el-input>
            </el-form-item>
            <el-form-item>
              <el-select 
                v-model="searchForm.status" 
                placeholder="请选择状态" 
                clearable
                size="small"
              >
                <el-option label="待审核" value="PENDING"></el-option>
                <el-option label="已通过" value="APPROVED"></el-option>
                <el-option label="已拒绝" value="REJECTED"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-select 
                v-model="searchForm.userType" 
                placeholder="请选择用户类型" 
                clearable
                size="small"
              >
                <el-option label="管理员" value="ADMIN"></el-option>
                <el-option label="教师" value="TEACHER"></el-option>
                <el-option label="用户" value="USER"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSearch" icon="el-icon-search" size="small" class="mr-2">查询</el-button>
              <el-button @click="resetSearch" icon="el-icon-refresh" size="small" class="mr-2">重置</el-button>
              <el-button type="success" @click="queryAllMessages" icon="el-icon-menu" size="small">查询全部</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div class="w-full lg:w-auto flex justify-end space-x-3">
          <el-button type="success" @click="handleBatchApprove" :disabled="selection.length === 0" icon="el-icon-check" size="small">
            批量通过
          </el-button>
          <el-button type="warning" @click="handleBatchReject" :disabled="selection.length === 0" icon="el-icon-close" size="small">
            批量拒绝
          </el-button>
          <el-button type="danger" @click="handleBatchDelete" :disabled="selection.length === 0" icon="el-icon-delete" size="small">
            批量删除
          </el-button>
        </div>
      </div>
    </div>

    <!-- 留言表格 -->
    <div class="bg-white rounded-lg shadow-sm overflow-hidden mb-5">
      <el-table
        :data="tableData"
        style="width: 100%"
        v-loading="loading"
        @selection-change="handleSelectionChange"
        :border="false"
        stripe
        :cell-style="{padding: '12px 0'}"
        :header-cell-style="{background:'#f8fafc', color:'#475569', fontWeight: '600'}"
        row-key="id"
      >
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        
        <!-- 留言层级和标题 -->
        <el-table-column label="留言内容" min-width="300">
          <template slot-scope="scope">
            <div class="flex items-start">
              <!-- 层级缩进 -->
              <div v-if="scope.row.parentId > 0" class="flex items-center mr-2 text-gray-400">
                <span class="inline-block w-6 text-center">└</span>
              </div>
              
              <div class="flex-1">
                <!-- 留言标题 -->
                <div class="font-medium text-gray-800 mb-1">
                  <span v-if="scope.row.isTop" class="inline-block px-2 py-1 text-xs bg-red-100 text-red-600 rounded mr-2">置顶</span>
                  {{ scope.row.title }}
                </div>
                
                <!-- 内容预览 -->
                <div class="text-sm text-gray-600 line-clamp-2">
                  {{ scope.row.content | contentPreview }}
                </div>
              </div>
            </div>
          </template>
        </el-table-column>

        <!-- 用户信息 -->
        <el-table-column label="用户信息" width="150">
          <template slot-scope="scope">
            <div class="flex items-center">
              <el-tag 
                :type="getUserTypeColor(scope.row.userType)" 
                size="mini" 
                class="mr-2"
              >
                {{ getUserTypeName(scope.row.userType) }}
              </el-tag>
            </div>
            <div class="text-sm text-gray-600 mt-1">{{ scope.row.createdBy || '未知用户' }}</div>
          </template>
        </el-table-column>

        <!-- 状态 -->
        <el-table-column label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag 
              :type="getStatusColor(scope.row.status)" 
              size="small"
            >
              {{ getStatusName(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>

        <!-- 统计信息 -->
        <el-table-column label="统计" width="120" align="center">
          <template slot-scope="scope">
            <div class="text-sm">
              <div class="flex items-center justify-center text-gray-600 mb-1">
                <i class="el-icon-chat-dot-round mr-1"></i>
                <span>{{ scope.row.replyCount || 0 }}</span>
              </div>
              <div class="flex items-center justify-center text-gray-600">
                <i class="el-icon-view mr-1"></i>
                <span>{{ scope.row.viewCount || 0 }}</span>
              </div>
            </div>
          </template>
        </el-table-column>

        <!-- 创建时间 -->
        <el-table-column prop="createTime" label="创建时间" width="150" align="center">
          <template slot-scope="scope">
            <div class="text-sm text-gray-600">
              {{ scope.row.createTime | formatDate }}
            </div>
          </template>
        </el-table-column>

        <!-- 操作 -->
        <el-table-column label="操作" fixed="right" width="220" align="center">
          <template slot-scope="scope">
            <div class="flex justify-center space-x-1">
              <!-- 审核操作 -->
              <el-button 
                v-if="scope.row.status === 'PENDING'"
                type="success" 
                size="mini" 
                icon="el-icon-check" 
                circle 
                @click="handleApprove(scope.row)" 
                title="通过"
                class="transform hover:-translate-y-1 transition-transform"
              ></el-button>
              
              <el-button 
                v-if="scope.row.status === 'PENDING'"
                type="warning" 
                size="mini" 
                icon="el-icon-close" 
                circle 
                @click="handleReject(scope.row)" 
                title="拒绝"
                class="transform hover:-translate-y-1 transition-transform"
              ></el-button>

              <!-- 置顶操作 -->
              <el-button 
                v-if="scope.row.parentId === 0"
                :type="scope.row.isTop ? 'danger' : 'info'" 
                size="mini" 
                :icon="scope.row.isTop ? 'el-icon-bottom' : 'el-icon-top'" 
                circle 
                @click="handleToggleTop(scope.row)" 
                :title="scope.row.isTop ? '取消置顶' : '置顶'"
                class="transform hover:-translate-y-1 transition-transform"
              ></el-button>

              <!-- 查看详情 -->
              <el-button 
                type="primary" 
                size="mini" 
                icon="el-icon-view" 
                circle 
                @click="handleDetail(scope.row)" 
                title="查看详情"
                class="transform hover:-translate-y-1 transition-transform"
              ></el-button>

              <!-- 回复 -->
              <el-button 
                type="info" 
                size="mini" 
                icon="el-icon-chat-dot-round" 
                circle 
                @click="handleReply(scope.row)" 
                title="回复"
                class="transform hover:-translate-y-1 transition-transform"
              ></el-button>

              <!-- 删除 -->
              <el-button 
                type="danger" 
                size="mini" 
                icon="el-icon-delete" 
                circle 
                @click="handleDelete(scope.row)" 
                title="删除"
                class="transform hover:-translate-y-1 transition-transform"
              ></el-button>
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

    <!-- 留言详情对话框 -->
    <el-dialog title="留言详情" :visible.sync="detailDialogVisible" width="800px" custom-class="rounded-lg">
      <div v-if="currentMessage" class="px-5">
        <!-- 留言基本信息 -->
        <div class="mb-6">
          <div class="flex items-center justify-between mb-4">
            <h3 class="text-xl font-bold text-gray-800">{{ currentMessage.title }}</h3>
            <div class="flex items-center space-x-2">
              <el-tag :type="getStatusColor(currentMessage.status)">
                {{ getStatusName(currentMessage.status) }}
              </el-tag>
              <el-tag v-if="currentMessage.isTop" type="danger" size="small">置顶</el-tag>
              <el-tag v-if="!currentMessage.isPublic" type="info" size="small">私有</el-tag>
            </div>
          </div>
          
          <!-- 用户信息 -->
          <div class="flex items-center mb-4">
            <el-tag :type="getUserTypeColor(currentMessage.userType)" class="mr-3">
              {{ getUserTypeName(currentMessage.userType) }}
            </el-tag>
            <span class="text-gray-600 mr-4">{{ currentMessage.createdBy }}</span>
            <span class="text-gray-500 text-sm">{{ currentMessage.createTime | formatDate }}</span>
          </div>
          
          <!-- 留言内容 -->
          <div class="bg-gray-50 rounded-lg p-4 mb-4">
            <p class="text-gray-800 leading-relaxed whitespace-pre-wrap">{{ currentMessage.content }}</p>
          </div>

          <!-- 统计信息 -->
          <div class="flex items-center space-x-6 text-sm text-gray-600">
            <div class="flex items-center">
              <i class="el-icon-chat-dot-round mr-1"></i>
              <span>回复 {{ currentMessage.replyCount || 0 }} 次</span>
            </div>
            <div class="flex items-center">
              <i class="el-icon-view mr-1"></i>
              <span>查看 {{ currentMessage.viewCount || 0 }} 次</span>
            </div>
          </div>
        </div>

        <!-- 审核信息 -->
        <div v-if="currentMessage.reviewedBy" class="bg-yellow-50 rounded-lg p-4">
          <h4 class="font-medium text-gray-700 mb-2">审核信息</h4>
          <div class="text-sm text-gray-600">
            <p>审核人：{{ currentMessage.reviewedBy }}</p>
            <p>审核时间：{{ currentMessage.reviewedTime | formatDate }}</p>
          </div>
        </div>
      </div>
      
      <div slot="footer" class="flex justify-end space-x-3">
        <el-button @click="detailDialogVisible = false" icon="el-icon-close">关 闭</el-button>
        <el-button type="primary" @click="handleReply(currentMessage)" icon="el-icon-chat-dot-round">回复留言</el-button>
      </div>
    </el-dialog>

    <!-- 回复留言对话框 -->
    <el-dialog :title="replyDialogTitle" :visible.sync="replyDialogVisible" width="600px" :close-on-click-modal="false" custom-class="rounded-lg">
      <el-form :model="replyForm" :rules="replyRules" ref="replyForm" label-width="80px">
        <el-form-item label="回复标题" prop="title">
          <el-input v-model="replyForm.title" placeholder="请输入回复标题" prefix-icon="el-icon-edit"></el-input>
        </el-form-item>
        <el-form-item label="回复内容" prop="content">
          <el-input 
            v-model="replyForm.content" 
            type="textarea" 
            :rows="6" 
            placeholder="请输入回复内容"
            maxlength="1000"
            show-word-limit
          ></el-input>
        </el-form-item>
      </el-form>
      
      <div slot="footer" class="flex justify-end space-x-3">
        <el-button @click="replyDialogVisible = false" icon="el-icon-close">取 消</el-button>
        <el-button type="primary" @click="submitReply" :loading="submitLoading" icon="el-icon-check">发送回复</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "MessageBoardManage",
  data() {
    return {
      // 搜索表单
      searchForm: {
        title: "",
        status: "",
        userType: "",
      },
      // 表格数据
      tableData: [],
      loading: false,
      total: 0,
      pageNum: 1,
      pageSize: 10,
      
      // 选中项
      selection: [],
      
      // 详情对话框
      detailDialogVisible: false,
      currentMessage: null,
      
      // 回复对话框
      replyDialogVisible: false,
      replyDialogTitle: "回复留言",
      replyParentMessage: null,
      submitLoading: false,
      
      // 回复表单
      replyForm: {
        title: "",
        content: "",
        parentId: null,
        userType: "ADMIN", // 管理员回复
        userId: null // 当前管理员ID
      },
      
      // 回复表单验证规则
      replyRules: {
        title: [
          { required: true, message: "请输入回复标题", trigger: "blur" },
          { min: 2, max: 100, message: "长度在 2 到 100 个字符", trigger: "blur" }
        ],
        content: [
          { required: true, message: "请输入回复内容", trigger: "blur" },
          { min: 5, max: 1000, message: "长度在 5 到 1000 个字符", trigger: "blur" }
        ]
      }
    };
  },
  filters: {
    // 内容预览过滤器
    contentPreview(content) {
      if (!content) return '';
      return content.length > 50 ? content.substring(0, 50) + '...' : content;
    },
    
    // 日期格式化过滤器
    formatDate(date) {
      if (!date) return '';
      return new Date(date).toLocaleString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit'
      });
    }
  },
  created() {
    this.fetchData();
    this.getCurrentUser();
  },
  methods: {
    // 获取当前用户信息
    getCurrentUser() {
      const userInfo = JSON.parse(localStorage.getItem('xm-user') || '{}');
      this.replyForm.userId = userInfo.id;
      // 根据用户角色设置回复表单的用户类型
      this.replyForm.userType = userInfo.role || 'ADMIN';
    },

    // 获取数据
    fetchData() {
      this.loading = true;
      
      // 构建请求参数
      const params = {
        pageNum: this.pageNum,
        pageSize: this.pageSize
      };
      
      // 添加搜索条件
      if (this.searchForm.title) {
        params.title = this.searchForm.title;
      }
      if (this.searchForm.status) {
        params.status = this.searchForm.status;
      }
      if (this.searchForm.userType) {
        params.userType = this.searchForm.userType;
      }
      
      // TODO: 调用后端接口
      this.$request.get("/messageBoard/selectPage", { params })
        .then(res => {
          if (res.code === '200') {
            this.tableData = res.data.list || [];
            this.total = res.data.total || 0;
          } else {
            this.$message.error(res.msg || "获取留言数据失败");
          }
        })
        .catch(() => {
          this.$message.error("获取留言数据异常");
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
      this.$message.success("已重置搜索条件，显示所有留言数据");
    },
    
    // 查询全部留言
    queryAllMessages() {
      this.$refs.searchForm.resetFields();
      this.pageNum = 1;
      this.fetchData();
      this.$message.success("已显示所有留言数据");
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

    // 获取用户类型名称
    getUserTypeName(userType) {
      switch (userType) {
        case 'ADMIN':
          return '管理员';
        case 'NURSE':
          return '护士';
        case 'USER':
          return '用户';
        default:
          return '未知';
      }
    },

    // 获取用户类型颜色
    getUserTypeColor(userType) {
      switch (userType) {
        case 'ADMIN':
          return 'danger';
        case 'NURSE':
          return 'warning';
        case 'USER':
          return 'primary';
        default:
          return 'info';
      }
    },

    // 获取状态名称
    getStatusName(status) {
      switch (status) {
        case 'PENDING':
          return '待审核';
        case 'APPROVED':
          return '已通过';
        case 'REJECTED':
          return '已拒绝';
        default:
          return '未知';
      }
    },

    // 获取状态颜色
    getStatusColor(status) {
      switch (status) {
        case 'PENDING':
          return 'warning';
        case 'APPROVED':
          return 'success';
        case 'REJECTED':
          return 'danger';
        default:
          return 'info';
      }
    },

    // 查看详情
    handleDetail(row) {
      this.currentMessage = row;
      this.detailDialogVisible = true;
    },

    // 审核通过
    handleApprove(row) {
      this.$confirm("确定通过该留言的审核吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "success"
      }).then(() => {
        // TODO: 调用后端接口
        this.$request.put(`/messageBoard/approve/${row.id}`)
          .then(res => {
            if (res.code === '200') {
              this.$message.success("审核通过成功");
              this.fetchData();
            } else {
              this.$message.error(res.msg || "审核通过失败");
            }
          })
          .catch(() => {
            this.$message.error("审核通过请求异常");
          });
      }).catch(() => {});
    },

    // 审核拒绝
    handleReject(row) {
      this.$confirm("确定拒绝该留言的审核吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        // TODO: 调用后端接口
        this.$request.put(`/messageBoard/reject/${row.id}`)
          .then(res => {
            if (res.code === '200') {
              this.$message.success("审核拒绝成功");
              this.fetchData();
            } else {
              this.$message.error(res.msg || "审核拒绝失败");
            }
          })
          .catch(() => {
            this.$message.error("审核拒绝请求异常");
          });
      }).catch(() => {});
    },

    // 批量审核通过
    handleBatchApprove() {
      if (this.selection.length === 0) {
        this.$message.warning("请选择要审核的留言");
        return;
      }
      
      this.$confirm(`确定通过选中的 ${this.selection.length} 条留言的审核吗?`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "success"
      }).then(() => {
        const ids = this.selection.map(item => item.id);
        
        // TODO: 调用后端接口
        this.$request.put("/messageBoard/approve/batch", { ids })
          .then(res => {
            if (res.code === '200') {
              this.$message.success("批量审核通过成功");
              this.fetchData();
            } else {
              this.$message.error(res.msg || "批量审核通过失败");
            }
          })
          .catch(() => {
            this.$message.error("批量审核通过请求异常");
          });
      }).catch(() => {});
    },

    // 批量审核拒绝
    handleBatchReject() {
      if (this.selection.length === 0) {
        this.$message.warning("请选择要审核的留言");
        return;
      }
      
      this.$confirm(`确定拒绝选中的 ${this.selection.length} 条留言的审核吗?`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        const ids = this.selection.map(item => item.id);
        
        // TODO: 调用后端接口
        this.$request.put("/messageBoard/reject/batch", { ids })
          .then(res => {
            if (res.code === '200') {
              this.$message.success("批量审核拒绝成功");
              this.fetchData();
            } else {
              this.$message.error(res.msg || "批量审核拒绝失败");
            }
          })
          .catch(() => {
            this.$message.error("批量审核拒绝请求异常");
          });
      }).catch(() => {});
    },

    // 置顶/取消置顶
    handleToggleTop(row) {
      const action = row.isTop ? '取消置顶' : '置顶';
      this.$confirm(`确定${action}该留言吗?`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "info"
      }).then(() => {
        // TODO: 调用后端接口
        this.$request.put(`/messageBoard/toggleTop/${row.id}`)
          .then(res => {
            if (res.code === '200') {
              this.$message.success(`${action}成功`);
              this.fetchData();
            } else {
              this.$message.error(res.msg || `${action}失败`);
            }
          })
          .catch(() => {
            this.$message.error(`${action}请求异常`);
          });
      }).catch(() => {});
    },

    // 回复留言
    handleReply(row) {
      this.replyParentMessage = row;
      this.replyDialogTitle = `回复：${row.title}`;
      const userInfo = JSON.parse(localStorage.getItem('xm-user') || '{}');
      this.replyForm = {
        title: `回复：${row.title}`,
        content: "",
        parentId: row.parentId === 0 ? row.id : row.parentId, // 如果是回复，则指向主留言
        userType: userInfo.role || "ADMIN", // 使用当前用户的角色
        userId: this.replyForm.userId
      };
      this.replyDialogVisible = true;
      
      // 清除验证
      this.$nextTick(() => {
        if (this.$refs.replyForm) {
          this.$refs.replyForm.clearValidate();
        }
      });
    },

    // 提交回复
    submitReply() {
      this.$refs.replyForm.validate(valid => {
        if (valid) {
          this.submitLoading = true;
          
          // TODO: 调用后端接口
          this.$request.post("/messageBoard/reply", this.replyForm)
            .then(res => {
              if (res.code === '200') {
                this.$message.success("回复成功");
                this.replyDialogVisible = false;
                this.fetchData();
              } else {
                this.$message.error(res.msg || "回复失败");
              }
            })
            .catch(() => {
              this.$message.error("回复请求异常");
            })
            .finally(() => {
              this.submitLoading = false;
            });
        }
      });
    },

    // 删除留言
    handleDelete(row) {
      const message = row.replyCount > 0 
        ? `该留言有 ${row.replyCount} 条回复，删除后回复也将被删除，确定删除吗?`
        : "确定删除该留言吗?";
        
      this.$confirm(message, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        // TODO: 调用后端接口
        this.$request.delete(`/messageBoard/delete/${row.id}`)
          .then(res => {
            if (res.code === '200') {
              this.$message.success("删除成功");
              this.fetchData();
            } else {
              this.$message.error(res.msg || "删除失败");
            }
          })
          .catch(() => {
            this.$message.error("删除留言请求异常");
          });
      }).catch(() => {});
    },
    
    // 批量删除
    handleBatchDelete() {
      if (this.selection.length === 0) {
        this.$message.warning("请选择要删除的留言");
        return;
      }
      
      this.$confirm(`确定删除选中的 ${this.selection.length} 条留言吗? 相关回复也将被删除。`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        const ids = this.selection.map(item => item.id);
        
        // TODO: 调用后端接口
        this.$request.delete("/messageBoard/delete/batch", { data: ids })
          .then(res => {
            if (res.code === '200') {
              this.$message.success("批量删除成功");
              this.fetchData();
            } else {
              this.$message.error(res.msg || "批量删除失败");
            }
          })
          .catch(() => {
            this.$message.error("批量删除留言请求异常");
          });
      }).catch(() => {});
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

/* 内容截断样式 */
.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

/* 层级连接线样式 */
.hierarchy-line {
  position: relative;
}

.hierarchy-line::before {
  content: '';
  position: absolute;
  left: -10px;
  top: 50%;
  width: 15px;
  height: 1px;
  background-color: #d1d5db;
}

/* 状态标签样式 */
:deep(.el-tag) {
  border-radius: 12px;
  font-size: 12px;
}

/* 响应式适配 */
@media (max-width: 768px) {
  .flex.space-x-1 {
    flex-wrap: wrap;
    gap: 4px;
  }
  
  .flex.space-x-1 .el-button {
    margin: 0;
  }
}
</style>
