<template>
  <div class="home-container">
    <div class="welcome-section">
      <div class="welcome-title">
        <i class="el-icon-s-home"></i> 欢迎使用智慧养老管理系统教师端
      </div>
      <div class="welcome-desc">本系统提供高效、便捷的教材管理和审核服务</div>
    </div>
    
    <el-row :gutter="20" class="stats-section">
      <el-col :span="8">
        <el-card shadow="hover" class="stats-card">
          <div class="stats-icon review">
            <i class="el-icon-s-check"></i>
          </div>
          <div class="stats-info">
            <div class="stats-title">待审核申报</div>
            <div class="stats-value">12</div>
            <div class="stats-desc">需要您审核的教材申报</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" class="stats-card">
          <div class="stats-icon textbook">
            <i class="el-icon-collection"></i>
          </div>
          <div class="stats-info">
            <div class="stats-title">相关教材</div>
            <div class="stats-value">48</div>
            <div class="stats-desc">您关联的教材数量</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" class="stats-card">
          <div class="stats-icon completed">
            <i class="el-icon-document-checked"></i>
          </div>
          <div class="stats-info">
            <div class="stats-title">已审核</div>
            <div class="stats-value">86</div>
            <div class="stats-desc">已完成的审核数量</div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    
    <el-row :gutter="20" class="content-section">
      <el-col :span="16">
        <el-card class="pending-card" shadow="hover">
          <div slot="header" class="pending-header">
            <span>待处理申报</span>
            <el-button type="text" @click="$router.push('/teacher/textbook-review')">查看全部</el-button>
          </div>
          <div v-if="pendingReviews.length > 0">
            <el-table :data="pendingReviews" style="width: 100%">
              <el-table-column prop="name" label="教材名称" width="180"></el-table-column>
              <el-table-column prop="applicant" label="申请人" width="120"></el-table-column>
              <el-table-column prop="department" label="所属院系"></el-table-column>
              <el-table-column prop="applyTime" label="申请时间" width="180"></el-table-column>
              <el-table-column label="操作" width="120">
                <template slot-scope="scope">
                  <el-button type="primary" size="mini" @click="handleReview(scope.row)">审核</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
          <div v-else class="empty-data">
            <i class="el-icon-s-order"></i>
            <p>暂无待处理申报</p>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="8">
        <el-card class="notice-card" shadow="hover">
          <div slot="header" class="notice-header">
            <span>最新通知</span>
            <el-button type="text" @click="$router.push('/teacher/notice')">更多</el-button>
          </div>
          <div v-if="notices.length > 0">
            <div v-for="(item, index) in notices" :key="index" class="notice-item">
              <div class="notice-title">{{ item.title }}</div>
              <div class="notice-time">{{ item.time }}</div>
            </div>
          </div>
          <div v-else class="empty-data">
            <i class="el-icon-info"></i>
            <p>暂无通知</p>
          </div>
        </el-card>
        
        <el-card class="shortcut-card" shadow="hover">
          <div slot="header" class="shortcut-header">
            <span>快捷入口</span>
          </div>
          <div class="shortcut-buttons">
            <el-button type="primary" icon="el-icon-s-check" @click="$router.push('/teacher/textbook-review')">教材审核</el-button>
            <el-button type="success" icon="el-icon-notebook-2" @click="$router.push('/teacher/textbook')">教材管理</el-button>
            <el-button type="info" icon="el-icon-document" @click="$router.push('/teacher/review-history')">审核历史</el-button>
            <el-button type="warning" icon="el-icon-user" @click="$router.push('/teacher/person')">个人中心</el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>
    
    <el-dialog
      title="申报审核"
      :visible.sync="reviewDialogVisible"
      width="50%">
      <div v-if="currentReview">
        <p><strong>教材名称：</strong>{{ currentReview.name }}</p>
        <p><strong>申请人：</strong>{{ currentReview.applicant }}</p>
        <p><strong>所属院系：</strong>{{ currentReview.department }}</p>
        <p><strong>申请时间：</strong>{{ currentReview.applyTime }}</p>
        
        <el-form :model="reviewForm" label-width="100px" class="review-form">
          <el-form-item label="审核结果">
            <el-radio-group v-model="reviewForm.status">
              <el-radio :label="1">通过</el-radio>
              <el-radio :label="0">不通过</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="审核意见">
            <el-input type="textarea" v-model="reviewForm.comment" rows="4"></el-input>
          </el-form-item>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="reviewDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitReview">提 交</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "TeacherHome",
  data() {
    return {
      pendingReviews: [
        { id: 1, name: '高等数学（第7版）', applicant: '张三', department: '理学院', applyTime: '2023-07-18' },
        { id: 2, name: '大学物理学', applicant: '李四', department: '物理学院', applyTime: '2023-07-15' },
        { id: 3, name: '计算机网络', applicant: '王五', department: '计算机学院', applyTime: '2023-07-10' }
      ],
      notices: [
        { title: '关于2023学年教材申报工作开始的通知', time: '2023-07-15' },
        { title: '新版教材管理系统正式上线公告', time: '2023-07-10' },
        { title: '关于加强教材质量审核的通知', time: '2023-07-05' },
        { title: '2023年教材出版社合作名单公示', time: '2023-06-30' }
      ],
      reviewDialogVisible: false,
      currentReview: null,
      reviewForm: {
        status: 1,
        comment: ''
      }
    };
  },
  methods: {
    handleReview(row) {
      this.currentReview = row;
      this.reviewForm.status = 1;
      this.reviewForm.comment = '';
      this.reviewDialogVisible = true;
    },
    submitReview() {
      // 调用审核API
      this.$message.success('审核提交成功');
      // 移除已审核的项目
      this.pendingReviews = this.pendingReviews.filter(item => item.id !== this.currentReview.id);
      this.reviewDialogVisible = false;
    }
  }
};
</script>

<style scoped>
.home-container {
  padding: 10px;
}

.welcome-section {
  margin-bottom: 20px;
}

.welcome-title {
  font-size: 22px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 10px;
}

.welcome-desc {
  font-size: 14px;
  color: #606266;
  margin-bottom: 20px;
}

.stats-section {
  margin-bottom: 20px;
}

.stats-card {
  display: flex;
  height: 100px;
  overflow: hidden;
}

.stats-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 80px;
  font-size: 36px;
  color: #fff;
}

.review {
  background-color: #E6A23C;
}

.textbook {
  background-color: #409EFF;
}

.completed {
  background-color: #67C23A;
}

.stats-info {
  flex: 1;
  padding: 15px;
}

.stats-title {
  font-size: 14px;
  color: #909399;
}

.stats-value {
  font-size: 24px;
  font-weight: bold;
  margin: 5px 0;
}

.stats-desc {
  font-size: 12px;
  color: #C0C4CC;
}

.content-section {
  margin-bottom: 20px;
}

.pending-header, .notice-header, .shortcut-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.notice-card {
  margin-bottom: 20px;
}

.notice-item {
  padding: 10px 0;
  border-bottom: 1px solid #EBEEF5;
}

.notice-title {
  font-size: 14px;
  margin-bottom: 5px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.notice-time {
  font-size: 12px;
  color: #909399;
}

.shortcut-buttons {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.shortcut-buttons .el-button {
  width: 100%;
  margin: 0;
}

.empty-data {
  padding: 30px 0;
  text-align: center;
  color: #909399;
}

.empty-data i {
  font-size: 40px;
  margin-bottom: 10px;
}

.review-form {
  margin-top: 20px;
}
</style> 