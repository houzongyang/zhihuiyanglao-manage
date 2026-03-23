/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80042
 Source Host           : localhost:3306
 Source Schema         : zhihuiyanglao-manage

 Target Server Type    : MySQL
 Target Server Version : 80042
 File Encoding         : 65001

 Date: 25/08/2025 12:28:58
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for activity
-- ----------------------------
DROP TABLE IF EXISTS `activity`;
CREATE TABLE `activity`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '活动ID',
  `category_id` int(0) NOT NULL COMMENT '活动分类ID',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '活动标题',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '活动描述',
  `activity_date` date NOT NULL COMMENT '活动日期',
  `start_time` time(0) NOT NULL COMMENT '开始时间',
  `end_time` time(0) NOT NULL COMMENT '结束时间',
  `location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '活动地点',
  `max_participants` int(0) NULL DEFAULT 0 COMMENT '最大参与人数，0表示无限制',
  `current_participants` int(0) NULL DEFAULT 0 COMMENT '当前参与人数',
  `organizer` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '组织者',
  `organizer_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '组织者电话',
  `requirements` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '参与要求',
  `materials_needed` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '所需物品',
  `cost` decimal(10, 2) NULL COMMENT '活动费用',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'PLANNED' COMMENT '活动状态：PLANNED-计划中，ONGOING-进行中，COMPLETED-已完成，CANCELLED-已取消',
  `registration_deadline` datetime(0) NULL DEFAULT NULL COMMENT '报名截止时间',
  `notes` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '备注信息',
  `image_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '活动图片',
  `cover_image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '活动封面图片URL',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `created_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_category_id`(`category_id`) USING BTREE,
  INDEX `idx_activity_date`(`activity_date`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE,
  INDEX `idx_registration_deadline`(`registration_deadline`) USING BTREE,
  CONSTRAINT `fk_activity_category` FOREIGN KEY (`category_id`) REFERENCES `activity_category` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '活动信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of activity
-- ----------------------------
INSERT INTO `activity` VALUES (1, 1, '晨练太极拳', '在公园进行太极拳晨练，适合所有老年人参与', '2024-05-01', '07:00:00', '08:00:00', '社区公园', 30, 15, '李教练', '13800001234', '身体健康，无严重心脏病', '运动服，运动鞋', 3.00, 'PLANNED', '2024-04-30 18:00:00', '请准时参加', NULL, NULL, '2024-04-25 10:00:00', '2025-08-10 15:54:04', 'admin');
INSERT INTO `activity` VALUES (2, 2, '卡拉OK欢唱会', '老年人卡拉OK聚会，大家一起唱歌娱乐', '2024-05-03', '14:00:00', '16:00:00', '活动中心KTV室', 20, 8, '王老师', '13800005678', '无特殊要求', '无', 4.00, 'PLANNED', '2024-05-02 12:00:00', '费用包含茶水', NULL, NULL, '2024-04-25 10:00:00', '2025-08-10 15:54:02', 'admin');
INSERT INTO `activity` VALUES (3, 3, '母亲节庆祝活动', '为老年女性举办的母亲节特别庆祝活动', '2025-07-27', '10:00:00', '22:00:00', '多功能厅', 50, 1, '张主任', '13800009999', '女性老年人优先', '鲜花，礼品', 4.00, 'PLANNED', '2026-05-21 18:00:00', '准备精美礼品', NULL, 'http://localhost:9090/files/1753587669793-0432.jpg_wh860.jpg', '2024-04-25 10:00:00', '2025-08-10 15:53:54', 'admin');
INSERT INTO `activity` VALUES (4, 4, '健康知识讲座', '老年人常见疾病预防与保健知识讲座', '2024-05-05', '09:30:00', '11:30:00', '会议室', 100, 45, '刘医生', '13800002468', '无特殊要求', '笔记本，笔', 3.00, 'PLANNED', '2024-05-04 18:00:00', '专业医生主讲', NULL, 'http://localhost:9090/files/1753587677425-4898.jpg_wh860.jpg', '2024-04-25 10:00:00', '2025-08-10 15:54:00', 'admin');
INSERT INTO `activity` VALUES (5, 5, '书法练习班', '老年人书法练习，提高文化修养', '2024-05-06', '15:00:00', '17:00:00', '书法室', 15, 10, '陈老师', '13800007890', '有一定书法基础', '毛笔，墨水，宣纸', 4.00, 'PLANNED', '2024-05-05 18:00:00', '材料费用', NULL, 'http://localhost:9090/files/1753587672991-4881.jpg_wh860.jpg', '2024-04-25 10:00:00', '2025-08-10 15:53:57', 'admin');

-- ----------------------------
-- Table structure for activity_category
-- ----------------------------
DROP TABLE IF EXISTS `activity_category`;
CREATE TABLE `activity_category`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '分类名称',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '分类描述',
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '分类图标',
  `color` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '分类颜色标识',
  `sort_order` int(0) NULL DEFAULT 0 COMMENT '排序顺序',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'ACTIVE' COMMENT '状态：ACTIVE-启用，INACTIVE-禁用',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `created_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE,
  INDEX `idx_sort_order`(`sort_order`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '活动分类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of activity_category
-- ----------------------------
INSERT INTO `activity_category` VALUES (1, '日常活动', '老年人日常健身、阅读、社交等活动', 'daily-icon', '#4CAF50', 1, 'ACTIVE', '2024-01-01 10:00:00', '2025-07-27 10:38:01', 'admin');
INSERT INTO `activity_category` VALUES (2, '娱乐项目', '卡拉OK、唱歌、跳舞等娱乐活动', 'entertainment-icon', '#FF9800', 2, 'ACTIVE', '2024-01-01 10:00:00', '2025-07-27 10:38:01', 'admin');
INSERT INTO `activity_category` VALUES (3, '特殊节日活动', '生日会、节日聚会等特别活动', 'festival-icon', '#E91E63', 3, 'ACTIVE', '2024-01-01 10:00:00', '2025-07-27 10:38:01', 'admin');
INSERT INTO `activity_category` VALUES (4, '健康保健', '健康讲座、体检、康复训练等', 'health-icon', '#2196F3', 4, 'ACTIVE', '2024-01-01 10:00:00', '2025-07-27 10:38:01', 'admin');
INSERT INTO `activity_category` VALUES (5, '文化教育', '书法、绘画、手工制作等文化活动', 'culture-icon', '#9C27B0', 5, 'ACTIVE', '2024-01-01 10:00:00', '2025-07-27 10:38:01', 'admin');

-- ----------------------------
-- Table structure for activity_participant
-- ----------------------------
DROP TABLE IF EXISTS `activity_participant`;
CREATE TABLE `activity_participant`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '参与记录ID',
  `activity_id` int(0) NOT NULL COMMENT '活动ID',
  `user_id` int(0) NOT NULL COMMENT '用户ID',
  `registration_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '报名时间',
  `attendance_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'REGISTERED' COMMENT '出席状态：REGISTERED-已报名，ATTENDED-已参加，ABSENT-缺席，CANCELLED-已取消',
  `check_in_time` datetime(0) NULL DEFAULT NULL COMMENT '签到时间',
  `check_out_time` datetime(0) NULL DEFAULT NULL COMMENT '签退时间',
  `rating` int(0) NULL DEFAULT NULL COMMENT '活动评分(1-5分)',
  `feedback` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '活动反馈',
  `special_needs` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '特殊需求',
  `emergency_contact` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '紧急联系人',
  `emergency_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '紧急联系电话',
  `notes` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '备注信息',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_activity_user`(`activity_id`, `user_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_attendance_status`(`attendance_status`) USING BTREE,
  INDEX `idx_registration_time`(`registration_time`) USING BTREE,
  CONSTRAINT `fk_participant_activity` FOREIGN KEY (`activity_id`) REFERENCES `activity` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_participant_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '活动参与记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of activity_participant
-- ----------------------------
INSERT INTO `activity_participant` VALUES (7, 3, 1, '2025-08-10 15:55:58', 'ATTENDED', '2025-08-10 15:56:11', NULL, NULL, NULL, NULL, '张三1', '18899997777', NULL, '2025-08-10 15:55:57', '2025-08-10 15:56:11');

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '密码',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '姓名',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像',
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '角色标识',
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '电话',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '管理员' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES (1, 'admin', 'admin', '管理员', 'http://localhost:9090/files/1697438073596-avatar.png', 'ADMIN', '13677889922', 'admin@xm.com');

-- ----------------------------
-- Table structure for bill_management
-- ----------------------------
DROP TABLE IF EXISTS `bill_management`;
CREATE TABLE `bill_management`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` int(0) NOT NULL COMMENT '用户ID',
  `user_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户姓名',
  `bill_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '账单类型：ACCOMMODATION入住费用,ACTIVITY活动费用',
  `related_id` int(0) NULL DEFAULT NULL COMMENT '关联ID：入住记录ID或活动参与ID',
  `related_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '关联名称：房间号或活动名称',
  `amount` decimal(10, 2) NOT NULL COMMENT '费用金额',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '费用描述',
  `payment_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'UNPAID' COMMENT '支付状态：UNPAID未支付,PAID已支付',
  `generated_date` date NOT NULL COMMENT '生成日期',
  `payment_date` datetime(0) NULL DEFAULT NULL COMMENT '支付日期',
  `generated_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '生成人（管理员）',
  `created_at` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `updated_at` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_bill_type`(`bill_type`) USING BTREE,
  INDEX `idx_payment_status`(`payment_status`) USING BTREE,
  INDEX `idx_generated_date`(`generated_date`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '费用管理表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of bill_management
-- ----------------------------
INSERT INTO `bill_management` VALUES (5, 1, '张三1', 'ACCOMMODATION', 1, 'A101房间', 3500.00, 'A101房间入住费用 (月费: ¥3500)', 'PAID', '2025-08-10', '2025-08-11 13:18:48', '管理员', '2025-08-10 16:28:58', '2025-08-11 13:18:48');
INSERT INTO `bill_management` VALUES (6, 1, '张三1', 'ACTIVITY', 7, '母亲节庆祝活动', 4.00, '母亲节庆祝活动活动参与费用 (费用: ¥4)', 'PAID', '2025-08-10', '2025-08-21 14:21:35', '管理员', '2025-08-10 16:28:58', '2025-08-21 14:21:35');

-- ----------------------------
-- Table structure for care_assignment
-- ----------------------------
DROP TABLE IF EXISTS `care_assignment`;
CREATE TABLE `care_assignment`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '护理安排ID',
  `user_id` int(0) NOT NULL COMMENT '老人ID',
  `nurse_id` int(0) NOT NULL COMMENT '护理人员ID）',
  `care_plan_id` int(0) NULL DEFAULT NULL COMMENT '护理计划ID',
  `shift_date` date NOT NULL COMMENT '安排日期',
  `start_time` time(0) NOT NULL COMMENT '开始时间',
  `end_time` time(0) NOT NULL COMMENT '结束时间',
  `shift_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'MORNING' COMMENT '班次：MORNING-早班，AFTERNOON-中班，NIGHT-夜班',
  `work_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '工作内容',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'SCHEDULED' COMMENT '状态：SCHEDULED-已安排，COMPLETED-已完成，CANCELLED-已取消',
  `completion_notes` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '完成说明',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '护理安排表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of care_assignment
-- ----------------------------
INSERT INTO `care_assignment` VALUES (1, 1, 1, 1, '2024-01-20', '08:00:00', '12:00:00', 'MORNING', '早晨护理：协助起床，测量生命体征，协助用餐', 'SCHEDULED', NULL, '2025-08-01 23:16:53', '2025-08-01 23:16:53');
INSERT INTO `care_assignment` VALUES (2, 1, 2, 1, '2024-01-20', '14:00:00', '18:00:00', 'AFTERNOON', '下午护理：陪伴聊天，协助运动，协助晚餐', 'SCHEDULED', NULL, '2025-08-01 23:16:53', '2025-08-01 23:16:53');

-- ----------------------------
-- Table structure for care_plan
-- ----------------------------
DROP TABLE IF EXISTS `care_plan`;
CREATE TABLE `care_plan`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '护理计划ID',
  `user_id` int(0) NOT NULL COMMENT '老人ID',
  `plan_title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '计划标题',
  `plan_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '计划内容',
  `care_level` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'LEVEL2' COMMENT '护理等级：LEVEL1-一级护理，LEVEL2-二级护理，LEVEL3-三级护理',
  `diet_plan` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '饮食计划',
  `exercise_plan` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '运动计划',
  `start_date` date NOT NULL COMMENT '开始日期',
  `end_date` date NULL DEFAULT NULL COMMENT '结束日期',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'ACTIVE' COMMENT '状态：ACTIVE-执行中，COMPLETED-已完成，CANCELLED-已取消',
  `created_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '制定人',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE,
  CONSTRAINT `fk_care_plan_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '护理计划表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of care_plan
-- ----------------------------
INSERT INTO `care_plan` VALUES (1, 1, '张三日常护理计划', '针对张三老人制定的基础护理计划，包括日常生活照料、健康监测等', 'LEVEL2', '清淡饮食，少盐少油，多吃蔬菜水果', '每日散步30分钟，适当做健身操', '2024-01-15', '2024-07-15', 'ACTIVE', 'admin', '2025-08-01 23:16:53', '2025-08-01 23:16:53');

-- ----------------------------
-- Table structure for care_record
-- ----------------------------
DROP TABLE IF EXISTS `care_record`;
CREATE TABLE `care_record`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '护理记录ID',
  `user_id` int(0) NOT NULL COMMENT '老人ID',
  `nurse_id` int(0) NOT NULL COMMENT '护理人员ID',
  `care_assignment_id` int(0) NULL DEFAULT NULL COMMENT '护理安排ID',
  `record_date` date NOT NULL COMMENT '记录日期',
  `care_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '护理类型：DAILY_CARE-日常照顾，DIET-饮食护理，EXERCISE-运动护理，MEDICAL-医疗护理',
  `care_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '护理内容',
  `blood_pressure` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '血压',
  `heart_rate` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '心率',
  `temperature` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '体温',
  `diet_record` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '饮食记录',
  `exercise_record` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '运动记录',
  `mood_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'GOOD' COMMENT '情绪状态：GOOD-良好，NORMAL-一般，POOR-较差',
  `abnormal_situation` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '异常情况',
  `remarks` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '备注',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '护理记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of care_record
-- ----------------------------
INSERT INTO `care_record` VALUES (1, 1, 1, 1, '2025-08-02', 'DAILY_CARE', '协助老人起床，测量血压和体温，督促服药', '130/80', '72', '36.5', '早餐：小米粥、鸡蛋', '无', 'GOOD', NULL, '老人精神状态良好', '2025-08-01 23:16:53', '2025-08-02 00:22:02');
INSERT INTO `care_record` VALUES (2, 1, 1, 1, '2025-08-02', 'EXERCISE', '陪同老人在花园散步', '125/78', '75', '36.6', '午餐：米饭、蒸蛋、青菜', '散步30分钟', 'GOOD', NULL, '运动后老人感觉很好', '2025-08-01 23:16:53', '2025-08-02 00:22:09');

-- ----------------------------
-- Table structure for checkin_record
-- ----------------------------
DROP TABLE IF EXISTS `checkin_record`;
CREATE TABLE `checkin_record`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '入住记录ID',
  `user_id` int(0) NOT NULL COMMENT '关联用户ID',
  `room_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '房间号',
  `checkin_date` date NOT NULL COMMENT '入住时间',
  `checkout_date` date NULL DEFAULT NULL COMMENT '退房时间',
  `checkin_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'CHECKED_IN' COMMENT '入住状态：CHECKED_IN-已入住，CHECKED_OUT-已退房，RESERVED-已预订',
  `monthly_fee` decimal(10, 2) NOT NULL COMMENT '月费用',
  `deposit` decimal(10, 2) NULL COMMENT '押金',
  `total_paid` decimal(10, 2) NULL COMMENT '已缴费用',
  `payment_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'UNPAID' COMMENT '缴费状态：PAID-已缴费，UNPAID-未缴费，PARTIAL-部分缴费',
  `room_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '房间类型',
  `bed_number` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '床位号',
  `special_needs` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '特殊需求',
  `emergency_contact` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '紧急联系人',
  `emergency_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '紧急联系电话',
  `remarks` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '备注信息',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `created_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_room_number`(`room_number`) USING BTREE,
  INDEX `idx_checkin_status`(`checkin_status`) USING BTREE,
  INDEX `idx_checkin_date`(`checkin_date`) USING BTREE,
  CONSTRAINT `fk_checkin_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '入住信息管理表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of checkin_record
-- ----------------------------
INSERT INTO `checkin_record` VALUES (1, 1, 'A101', '2024-01-15', '2025-08-10', 'CHECKED_IN', 3500.00, 0.00, 3500.00, 'PAID', '标准单人间', 'A101-1', '需要轮椅通道', '李四', '13900001111', '老人身体状况良好，无特殊医疗需求', '2024-01-15 10:30:00', '2025-08-10 16:32:27', 'admin');

-- ----------------------------
-- Table structure for health_monitoring
-- ----------------------------
DROP TABLE IF EXISTS `health_monitoring`;
CREATE TABLE `health_monitoring`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '监控记录ID',
  `user_id` int(0) NOT NULL COMMENT '用户ID（老人）',
  `record_date` date NOT NULL COMMENT '记录日期',
  `record_time` time(0) NOT NULL COMMENT '记录时间',
  `body_temperature` decimal(4, 1) NULL DEFAULT NULL COMMENT '体温（℃）',
  `systolic_pressure` int(0) NULL DEFAULT NULL COMMENT '收缩压（mmHg）',
  `diastolic_pressure` int(0) NULL DEFAULT NULL COMMENT '舒张压（mmHg）',
  `heart_rate` int(0) NULL DEFAULT NULL COMMENT '心率（次/分）',
  `blood_sugar` decimal(5, 2) NULL DEFAULT NULL COMMENT '血糖（mmol/L）',
  `blood_sugar_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '血糖类型：FASTING-空腹，POSTPRANDIAL-餐后，RANDOM-随机',
  `blood_oxygen` int(0) NULL DEFAULT NULL COMMENT '血氧饱和度（%）',
  `weight` decimal(5, 2) NULL DEFAULT NULL COMMENT '体重（kg）',
  `sleep_hours` decimal(3, 1) NULL DEFAULT NULL COMMENT '睡眠时长（小时）',
  `sleep_quality` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '睡眠质量：EXCELLENT-优秀，GOOD-良好，FAIR-一般，POOR-较差',
  `mood_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '情绪状态：HAPPY-愉快，NORMAL-正常，ANXIOUS-焦虑，DEPRESSED-抑郁',
  `physical_activity` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '体力活动记录',
  `abnormal_symptoms` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '异常症状描述',
  `measured_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '测量人员',
  `remarks` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '备注信息',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_record_date`(`record_date`) USING BTREE,
  INDEX `idx_user_date`(`user_id`, `record_date`) USING BTREE,
  CONSTRAINT `fk_health_monitoring_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '健康监控记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of health_monitoring
-- ----------------------------
INSERT INTO `health_monitoring` VALUES (1, 1, '2025-08-02', '21:44:00', 35.0, 80, 40, 40, 2.00, 'FASTING', 70, 30.00, 2.0, 'EXCELLENT', 'HAPPY', '12233', '112', '护士1', '112', '2025-08-02 21:44:08', '2025-08-02 21:50:28');
INSERT INTO `health_monitoring` VALUES (2, 1, '2025-08-10', '14:08:00', 35.0, 80, 40, 40, 2.00, 'FASTING', 70, 30.00, 0.0, 'EXCELLENT', 'HAPPY', '11', '12', '11', '2', '2025-08-10 14:08:16', '2025-08-10 14:08:16');
INSERT INTO `health_monitoring` VALUES (3, 1, '2025-08-10', '14:41:00', 35.0, 80, 40, 40, 2.00, 'FASTING', 70, 30.00, 1.0, 'EXCELLENT', 'HAPPY', '3', '33', 'ad', '3', '2025-08-10 14:42:05', '2025-08-10 14:42:05');
INSERT INTO `health_monitoring` VALUES (4, 1, '2025-08-21', '14:09:00', 35.0, 80, 40, 40, 2.00, 'FASTING', 70, 30.00, 0.0, 'EXCELLENT', 'HAPPY', 'test', 'test', 'ad', 'testtesttest', '2025-08-21 14:09:16', '2025-08-21 14:09:16');

-- ----------------------------
-- Table structure for health_record
-- ----------------------------
DROP TABLE IF EXISTS `health_record`;
CREATE TABLE `health_record`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '档案ID',
  `user_id` int(0) NOT NULL COMMENT '关联用户ID',
  `medical_history` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '病史记录',
  `drug_allergies` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '药物过敏史',
  `physical_examination` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '常规体检记录',
  `medical_records` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '病历记录',
  `blood_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '血型',
  `height` decimal(5, 2) NULL DEFAULT NULL COMMENT '身高(cm)',
  `weight` decimal(5, 2) NULL DEFAULT NULL COMMENT '体重(kg)',
  `chronic_disease` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '慢性疾病',
  `emergency_contact` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '紧急联系人',
  `emergency_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '紧急联系电话',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  CONSTRAINT `fk_health_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '健康档案表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of health_record
-- ----------------------------
INSERT INTO `health_record` VALUES (1, 1, '2010年确诊高血压，2015年做过胆囊切除手术1', '青霉素过敏1', '2023年4月体检结果：血压140/90mmHg，血糖5.8mmol/L', '2023年6月因感冒在社区医院就诊，开具感冒药一周', 'A', 165.00, 62.50, '高血压，糖尿病前期', '李四', '13912345678', '2023-06-10 09:00:00', '2025-07-26 22:21:25');
INSERT INTO `health_record` VALUES (2, 10, '无', '无', '无', '无', 'A', 0.04, 0.03, '1', '张三', '15032839721', '2025-08-11 13:43:06', '2025-08-11 13:43:06');

-- ----------------------------
-- Table structure for health_reminder
-- ----------------------------
DROP TABLE IF EXISTS `health_reminder`;
CREATE TABLE `health_reminder`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '提醒记录ID',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '提醒内容',
  `sender_id` int(0) NOT NULL COMMENT '发送人ID',
  `sender_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '发送人昵称',
  `sender_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '发送人类型：ADMIN-管理员，TEACHER-护理人员，USER-用户',
  `receiver_id` int(0) NULL DEFAULT NULL COMMENT '接收人ID（可为空，表示所有人）',
  `receiver_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'ALL' COMMENT '接收人类型：ALL-所有人，MEDICAL_STAFF-医护人员，FAMILY-家属，USER-用户',
  `health_record_id` int(0) NULL DEFAULT NULL COMMENT '关联的健康档案ID',
  `related_user_id` int(0) NULL DEFAULT NULL COMMENT '相关用户ID（老人）',
  `reminder_type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'HEALTH_ALERT' COMMENT '提醒类型：HEALTH_ALERT-健康警报，MEDICATION-用药提醒，CHECK_UP-体检提醒，EMERGENCY-紧急情况',
  `priority` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'NORMAL' COMMENT '优先级：HIGH-高，NORMAL-普通，LOW-低',
  `is_read` tinyint(1) NULL DEFAULT 0 COMMENT '是否已读：0-未读，1-已读',
  `read_time` datetime(0) NULL DEFAULT NULL COMMENT '阅读时间',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'ACTIVE' COMMENT '状态：ACTIVE-有效，RESOLVED-已处理，EXPIRED-已过期',
  `expire_time` datetime(0) NULL DEFAULT NULL COMMENT '过期时间',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_sender_id`(`sender_id`) USING BTREE,
  INDEX `idx_receiver_id`(`receiver_id`) USING BTREE,
  INDEX `idx_health_record_id`(`health_record_id`) USING BTREE,
  INDEX `idx_related_user_id`(`related_user_id`) USING BTREE,
  INDEX `idx_is_read`(`is_read`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE,
  INDEX `idx_reminder_type`(`reminder_type`) USING BTREE,
  INDEX `idx_create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '健康提醒记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of health_reminder
-- ----------------------------
INSERT INTO `health_reminder` VALUES (1, '患者张三的血压异常（150/95mmHg），请医护人员关注并及时处理', 1, '系统管理员', 'ADMIN', NULL, 'MEDICAL_STAFF', 1, 1, 'HEALTH_ALERT', 'HIGH', 0, NULL, 'ACTIVE', NULL, '2025-08-10 15:14:07', '2025-08-10 15:14:07');
INSERT INTO `health_reminder` VALUES (2, '老人李四今日未按时服用降压药，请家属提醒', 2, '护理员小王', 'TEACHER', NULL, 'FAMILY', NULL, 2, 'MEDICATION', 'NORMAL', 0, NULL, 'ACTIVE', NULL, '2025-08-10 15:14:07', '2025-08-10 15:14:07');
INSERT INTO `health_reminder` VALUES (3, '王奶奶下周二需要进行常规体检，请提前安排', 1, '系统管理员', 'ADMIN', NULL, 'MEDICAL_STAFF', 3, 3, 'CHECK_UP', 'NORMAL', 0, NULL, 'ACTIVE', NULL, '2025-08-10 15:14:07', '2025-08-10 15:14:07');
INSERT INTO `health_reminder` VALUES (4, '赵爷爷昨晚睡眠质量较差，情绪有些焦虑，建议关注心理状态', 3, '夜班护士', 'TEACHER', NULL, 'ALL', 4, 4, 'HEALTH_ALERT', 'NORMAL', 1, '2025-08-10 15:14:50', 'ACTIVE', NULL, '2025-08-10 15:14:07', '2025-08-10 15:14:50');
INSERT INTO `health_reminder` VALUES (5, '你该吃药了你该吃药了\n你该吃药了\n你该吃药了\n', 1, '管理员', 'ADMIN', NULL, 'USER', 1, 1, 'HEALTH_ALERT', 'NORMAL', 1, '2025-08-10 15:14:45', 'ACTIVE', NULL, '2025-08-10 15:14:35', '2025-08-10 15:14:45');
INSERT INTO `health_reminder` VALUES (6, '该吃药了该吃药了该吃药了该吃药了该吃药了', 1, '张教授', 'NURSE', NULL, 'ALL', 1, 1, 'MEDICATION', 'NORMAL', 1, '2025-08-21 14:17:38', 'ACTIVE', NULL, '2025-08-21 14:17:30', '2025-08-21 14:17:38');

-- ----------------------------
-- Table structure for message_board
-- ----------------------------
DROP TABLE IF EXISTS `message_board`;
CREATE TABLE `message_board`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '留言ID',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '留言标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '留言内容',
  `user_id` int(0) NOT NULL COMMENT '用户ID',
  `user_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户类型：ADMIN-管理员，TEACHER-教师，USER-普通用户',
  `parent_id` int(0) NULL DEFAULT 0 COMMENT '父留言ID，0表示主留言，其他表示回复',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'PENDING' COMMENT '留言状态：PENDING-待审核，APPROVED-已通过，REJECTED-已拒绝',
  `is_public` tinyint(1) NULL DEFAULT 1 COMMENT '是否公开显示：1-公开，0-不公开',
  `is_top` tinyint(1) NULL DEFAULT 0 COMMENT '是否置顶：1-置顶，0-不置顶',
  `reply_count` int(0) NULL DEFAULT 0 COMMENT '回复数量',
  `view_count` int(0) NULL DEFAULT 0 COMMENT '查看次数',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `created_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人姓名',
  `reviewed_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '审核人',
  `reviewed_time` datetime(0) NULL DEFAULT NULL COMMENT '审核时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_type_id`(`user_type`, `user_id`) USING BTREE,
  INDEX `idx_parent_id`(`parent_id`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE,
  INDEX `idx_create_time`(`create_time`) USING BTREE,
  INDEX `idx_is_public`(`is_public`) USING BTREE,
  INDEX `idx_is_top`(`is_top`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '留言板表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of message_board
-- ----------------------------
INSERT INTO `message_board` VALUES (1, '欢迎使用智慧养老服务平台留言板', '尊敬的用户，欢迎使用我们的留言板功能！您可以在这里发表意见、建议或进行交流讨论。我们会认真对待每一条留言，并及时回复。', 1, 'ADMIN', 0, 'APPROVED', 1, 1, 2, 157, '2024-01-15 09:00:00', '2025-08-02 21:14:38', '管理员', '管理员', '2024-01-15 09:00:00');
INSERT INTO `message_board` VALUES (2, '关于健康档案功能的使用建议', '我觉得健康档案功能很实用，但希望能增加更多的健康指标记录，比如血压、血糖的历史趋势图。', 1, 'USER', 0, 'APPROVED', 1, 0, 1, 45, '2024-01-16 10:30:00', '2024-01-16 10:30:00', '张三1', '管理员', '2024-01-16 11:00:00');
INSERT INTO `message_board` VALUES (3, '感谢您的建议', '您好！非常感谢您的建议，健康指标趋势图功能我们已经列入开发计划，预计下个版本会上线。请继续关注我们的更新公告。', 1, 'ADMIN', 2, 'APPROVED', 1, 0, 0, 23, '2024-01-16 14:00:00', '2024-01-16 14:00:00', '管理员', '管理员', '2024-01-16 14:00:00');
INSERT INTO `message_board` VALUES (4, '关于活动报名系统的优化建议', '作为一名教师，我发现当前的活动报名系统在批量审核方面还有优化空间，希望能添加批量操作功能。', 1, 'TEACHER', 0, 'APPROVED', 1, 0, 1, 67, '2024-01-17 09:15:00', '2024-01-17 09:15:00', '张教授', '管理员', '2024-01-17 10:00:00');
INSERT INTO `message_board` VALUES (5, '我们会尽快优化', '张教授您好，您提出的批量操作功能确实很有必要，我们技术团队会在近期版本中加入这个功能，谢谢您的建议！', 1, 'ADMIN', 4, 'APPROVED', 1, 0, 0, 12, '2024-01-17 15:30:00', '2024-01-17 15:30:00', '管理员', '管理员', '2024-01-17 15:30:00');
INSERT INTO `message_board` VALUES (6, '界面很友好，但希望增加夜间模式', '平台整体使用体验很好，就是希望能增加夜间模式，这样晚上使用眼睛会更舒服一些。', 1, 'USER', 1, 'APPROVED', 1, 0, 0, 34, '2024-01-18 20:45:00', '2024-01-18 20:45:00', '张三1', '管理员', '2024-01-18 21:00:00');
INSERT INTO `message_board` VALUES (7, '111111', '111111111111111111', 1, 'USER', 0, 'APPROVED', 1, 0, 3, 6, '2025-08-02 21:11:00', '2025-08-02 21:51:21', '张三1', NULL, '2025-08-02 21:11:10');
INSERT INTO `message_board` VALUES (8, '回复：111111', '111111111', 1, 'USER', 7, 'PENDING', 1, 0, 0, 0, '2025-08-02 21:14:49', '2025-08-02 21:14:49', '张三1', NULL, NULL);
INSERT INTO `message_board` VALUES (9, '回复：111111', '222222222222', 1, 'ADMIN', 7, 'APPROVED', 1, 0, 0, 0, '2025-08-02 21:18:24', '2025-08-02 21:18:24', NULL, NULL, NULL);
INSERT INTO `message_board` VALUES (10, '回复：111111', '222222222', 10, 'USER', 7, 'PENDING', 1, 0, 0, 0, '2025-08-02 21:51:17', '2025-08-02 21:51:17', '李', NULL, NULL);

-- ----------------------------
-- Table structure for notice
-- ----------------------------
DROP TABLE IF EXISTS `notice`;
CREATE TABLE `notice`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '公告标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '公告内容',
  `time` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建时间',
  `user` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_time`(`time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '公告信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of notice
-- ----------------------------
INSERT INTO `notice` VALUES (1, '系统正式上线通知', '尊敬的用户：\r\n\r\n我们很高兴地通知您，智慧养老服务平台已正式上线！本平台旨在为老年人提供便捷、高效的养老服务，包括健康管理、生活照料、紧急救助等多项功能。\r\n\r\n平台特色：\r\n1. 健康档案管理\r\n2. 在线问诊服务\r\n3. 生活服务预约\r\n4. 紧急呼叫系统\r\n\r\n如有任何问题，请随时联系客服热线：400-123-4567\r\n\r\n感谢您的支持！', '2023-05-15', 'admin');
INSERT INTO `notice` VALUES (2, '端午节放假安排', '各位用户：\r\n\r\n根据国家法定节假日安排，现将端午节放假安排通知如下：\r\n\r\n1. 放假时间：2023年6月22日至6月24日，共3天\r\n2. 上班时间：6月25日（周日）正常服务\r\n\r\n放假期间，平台紧急服务热线24小时值守，如有紧急情况请拨打：400-123-4567\r\n\r\n祝大家端午节快乐！', '2023-06-10', 'admin');
INSERT INTO `notice` VALUES (3, '健康讲座预告', '主题：《老年人夏季保健知识》\r\n\r\n时间：2023年7月15日 上午9:30-11:30\r\n地点：社区活动中心多功能厅\r\n主讲人：王医生（市第一人民医院老年科主任）\r\n\r\n内容简介：\r\n1. 老年人夏季常见疾病预防\r\n2. 合理饮食与营养搭配\r\n3. 适宜运动方式推荐\r\n4. 健康生活习惯养成\r\n\r\n请有兴趣的老年朋友提前报名参加，名额有限！\r\n报名电话：0123-4567890', '2023-07-01', 'admin');
INSERT INTO `notice` VALUES (4, '系统维护通知', '尊敬的用户：\r\n\r\n为了提升系统性能和用户体验，我们计划于2023年8月10日凌晨2:00-5:00进行系统维护升级。维护期间，平台将暂停服务，请您合理安排使用时间。\r\n\r\n维护内容：\r\n1. 服务器升级\r\n2. 数据库优化\r\n3. 新功能上线准备\r\n\r\n给您带来的不便，敬请谅解！\r\n\r\n智慧养老服务平台运维团队', '2023-08-05', 'admin');
INSERT INTO `notice` VALUES (5, '重阳节活动邀请', '尊敬的老年用户：\r\n\r\n在重阳佳节来临之际，我们诚挚邀请您参加\"敬老爱老 情暖重阳\"主题活动。\r\n\r\n活动时间：2023年10月23日 上午9:00-11:30\r\n活动地点：市民广场\r\n活动内容：\r\n1. 健康义诊\r\n2. 文艺表演\r\n3. 趣味游戏\r\n4. 免费合影留念\r\n\r\n参与活动的老年朋友将获赠精美纪念品一份！\r\n\r\n期待您的参与！', '2023-10-15', 'admin');
INSERT INTO `notice` VALUES (6, '冬季防寒保暖提示', '各位老年朋友：\r\n\r\n随着气温逐渐降低，冬季即将来临，请注意做好防寒保暖工作：\r\n\r\n1. 及时添加衣物，注意保暖\r\n2. 室内保持通风，但避免长时间开窗\r\n3. 饮食宜热不宜凉，多喝热水\r\n4. 适当运动，增强体质\r\n5. 注意监测血压，预防心脑血管疾病\r\n\r\n如有不适，请及时就医或联系我们的在线医生咨询。\r\n\r\n祝您冬日健康！', '2023-11-20', 'admin');
INSERT INTO `notice` VALUES (7, '春节期间服务安排', '尊敬的用户：\r\n\r\n值此新春佳节来临之际，向您致以节日的问候和新年的祝福！\r\n\r\n春节期间（2024年2月10日至2月17日）服务安排如下：\r\n\r\n1. 线上平台：24小时正常运行\r\n2. 客服热线：9:00-18:00有人值守\r\n3. 上门服务：除夕至初三暂停，初四恢复正常\r\n4. 紧急救助：24小时响应\r\n\r\n春节期间如有紧急情况，请拨打应急热线：400-123-4567\r\n\r\n祝您新春快乐，阖家幸福！', '2024-01-25', 'admin');
INSERT INTO `notice` VALUES (8, '免费体检活动通知', '关于开展2024年度老年人免费体检活动的通知\r\n\r\n为了关爱老年人健康，我们将组织开展年度免费体检活动：\r\n\r\n体检时间：2024年3月1日至3月31日\r\n体检地点：各社区卫生服务中心\r\n体检对象：60岁以上老年人\r\n体检项目：\r\n1. 常规体检（血压、血糖等）\r\n2. 心电图检查\r\n3. B超检查\r\n4. 血常规检查\r\n\r\n请携带身份证和老年证提前预约，预约电话：0123-4567890\r\n\r\n温馨提示：体检前一天晚上8点后请勿进食。', '2024-02-20', 'admin');
INSERT INTO `notice` VALUES (9, '智能手环使用培训', '智能健康手环使用培训通知\r\n\r\n为帮助老年用户更好地使用智能健康手环，我们将举办使用培训课程：\r\n\r\n培训时间：2024年4月15日 上午9:30-11:00\r\n培训地点：社区活动中心\r\n培训内容：\r\n1. 智能手环基本功能介绍\r\n2. 日常使用方法讲解\r\n3. 数据查看与健康管理\r\n4. 常见问题解答\r\n\r\n请有需要的老年朋友带上您的智能手环参加培训。\r\n\r\n联系电话：0123-4567890', '2024-04-01', 'admin');

-- ----------------------------
-- Table structure for nurse
-- ----------------------------
DROP TABLE IF EXISTS `nurse`;
CREATE TABLE `nurse`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '老师ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '姓名',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '电话',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  `department` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '院系',
  `position` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '职位',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `role` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'TEACHER' COMMENT '角色标识',
  `work_years` int(0) NULL DEFAULT 0 COMMENT '工作年限(年)',
  `specialty` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '专业特长',
  `work_schedule` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '白班' COMMENT '工作时间(白班/夜班/倒班/全天)',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '在职' COMMENT '状态(在职/休假/离职)',
  `introduction` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '个人简介',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_teacher_username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '护士信息表（原teacher表）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of nurse
-- ----------------------------
INSERT INTO `nurse` VALUES (4, 'nurse001', '123456', '张丽华', '/avatar/nurse001.jpg', '13812345001', 'zhanglh@hospital.com', '内科', '护士长', '2025-08-25 12:25:02', 'NURSE', 15, '老年护理、慢性病管理、心理疏导', '白班', '在职', '从事护理工作15年，擅长老年护理和慢性病管理，具有丰富的临床经验和良好的沟通能力。');
INSERT INTO `nurse` VALUES (5, 'nurse002', '123456', '王美丽', '/avatar/nurse002.jpg', '13812345002', 'wangml@hospital.com', '内科', '主管护师', '2025-08-25 12:25:02', 'NURSE', 8, '糖尿病护理、营养指导', '白班', '在职', '专注于内分泌疾病护理，对糖尿病患者的日常管理和营养指导有独到见解。');
INSERT INTO `nurse` VALUES (6, 'nurse003', '123456', '李小芳', '/avatar/nurse003.jpg', '13812345003', 'lixf@hospital.com', '内科', '护师', '2025-08-25 12:25:02', 'NURSE', 5, '呼吸系统护理、康复训练', '倒班', '在职', '年轻有活力的护师，在呼吸系统疾病护理方面表现突出，善于指导患者进行康复训练。');
INSERT INTO `nurse` VALUES (7, 'nurse004', '123456', '陈雅静', '/avatar/nurse004.jpg', '13812345004', 'chenyj@hospital.com', '外科', '护士长', '2025-08-25 12:25:02', 'NURSE', 12, '手术室护理、无菌操作、急救护理', '全天', '在职', '外科护理专家，具有丰富的手术室工作经验，精通各种无菌操作技术和急救护理。');
INSERT INTO `nurse` VALUES (8, 'nurse005', '123456', '刘晓敏', '/avatar/nurse005.jpg', '13812345005', 'liuxm@hospital.com', '外科', '主管护师', '2025-08-25 12:25:02', 'NURSE', 7, '创伤护理、术后康复', '白班', '在职', '专业从事外科护理工作，在创伤护理和术后康复方面经验丰富。');
INSERT INTO `nurse` VALUES (9, 'nurse006', '123456', '赵春花', '/avatar/nurse006.jpg', '13812345006', 'zhaoch@hospital.com', '儿科', '护士长', '2025-08-25 12:25:02', 'NURSE', 10, '儿童护理、疫苗接种、家长教育', '白班', '在职', '资深儿科护士，具有丰富的儿童护理经验，善于与儿童和家长沟通，深受患儿喜爱。');
INSERT INTO `nurse` VALUES (10, 'nurse007', '123456', '孙美玲', '/avatar/nurse007.jpg', '13812345007', 'sunml@hospital.com', '儿科', '护师', '2025-08-25 12:25:02', 'NURSE', 4, '新生儿护理、早产儿护理', '夜班', '在职', '专注于新生儿护理，在早产儿护理方面有特殊专长，工作细致认真。');
INSERT INTO `nurse` VALUES (11, 'nurse008', '123456', '周慧敏', '/avatar/nurse008.jpg', '13812345008', 'zhouhm@hospital.com', '妇科', '主管护师', '2025-08-25 12:25:02', 'NURSE', 9, '妇科手术护理、产前产后护理', '倒班', '在职', '妇科护理专家，在妇科手术护理和产前产后护理方面经验丰富。');
INSERT INTO `nurse` VALUES (12, 'nurse009', '123456', '吴雪梅', '/avatar/nurse009.jpg', '13812345009', 'wuxm@hospital.com', '妇科', '护师', '2025-08-25 12:25:02', 'NURSE', 6, '母婴护理、健康宣教', '白班', '在职', '温柔细心的妇科护师，擅长母婴护理和健康宣教，深受产妇信赖。');
INSERT INTO `nurse` VALUES (13, 'nurse010', '123456', '马丽娜', '/avatar/nurse010.jpg', '13812345010', 'maln@hospital.com', '急诊科', '护士长', '2025-08-25 12:25:02', 'NURSE', 13, '急救护理、心肺复苏、外伤处理', '全天', '在职', '急诊科资深护士长，具有丰富的急救护理经验，熟练掌握各种急救技能。');
INSERT INTO `nurse` VALUES (14, 'nurse011', '123456', '朱静雯', '/avatar/nurse011.jpg', '13812345011', 'zhujw@hospital.com', '急诊科', '护师', '2025-08-25 12:25:02', 'NURSE', 3, '创伤急救、重症监护', '夜班', '在职', '年轻的急诊科护师，反应敏捷，在创伤急救和重症监护方面表现优异。');
INSERT INTO `nurse` VALUES (15, 'nurse012', '123456', '胡晓霞', '/avatar/nurse012.jpg', '13812345012', 'huxe@hospital.com', '康复科', '主管护师', '2025-08-25 12:25:02', 'NURSE', 11, '康复训练、物理治疗、功能评估', '白班', '在职', '康复护理专家，在康复训练和功能评估方面有深入研究，帮助众多患者重获健康。');
INSERT INTO `nurse` VALUES (16, 'nurse013', '123456', '郭婷婷', '/avatar/nurse013.jpg', '13812345013', 'guott@hospital.com', '康复科', '护师', '2025-08-25 12:25:02', 'NURSE', 2, '运动康复、作业治疗', '白班', '在职', '新入职的康复科护师，充满活力，在运动康复和作业治疗方面学习能力强。');
INSERT INTO `nurse` VALUES (17, 'nurse014', '123456', '黄丽萍', '/avatar/nurse014.jpg', '13812345014', 'huanglp@hospital.com', '老年科', '护士长', '2025-08-25 12:25:02', 'NURSE', 16, '老年综合护理、认知障碍护理、临终关怀', '白班', '在职', '资深老年科护士长，在老年综合护理和认知障碍护理方面有丰富经验，富有爱心和耐心。');
INSERT INTO `nurse` VALUES (18, 'nurse015', '123456', '林小青', '/avatar/nurse015.jpg', '13812345015', 'linxq@hospital.com', '老年科', '主管护师', '2025-08-25 12:25:02', 'NURSE', 8, '老年慢性病护理、心理护理', '倒班', '在职', '专业的老年科护理人员，在老年慢性病护理和心理护理方面有独特见解。');
INSERT INTO `nurse` VALUES (19, 'nurse016', '123456', '徐小雨', '/avatar/nurse016.jpg', '13812345016', 'xuxy@hospital.com', '内科', '实习护士', '2025-08-25 12:25:02', 'NURSE', 0, '基础护理、生活照料', '白班', '在职', '刚毕业的实习护士，学习态度认真，基础理论扎实，正在各科室轮转学习。');
INSERT INTO `nurse` VALUES (20, 'nurse017', '123456', '田甜甜', '/avatar/nurse017.jpg', '13812345017', 'tiantt@hospital.com', '外科', '护士', '2025-08-25 12:25:02', 'NURSE', 1, '基础护理、术前术后护理', '夜班', '在职', '新入职护士，工作认真负责，在基础护理和术前术后护理方面进步很快。');
INSERT INTO `nurse` VALUES (21, 'nurse018', '123456', '韩雪儿', '/avatar/nurse018.jpg', '13812345018', 'hanxe@hospital.com', '妇科', '护师', '2025-08-25 12:25:02', 'NURSE', 5, '产科护理、新生儿护理', '白班', '休假', '优秀的妇科护师，目前正在休产假，预计下月返岗工作。');
INSERT INTO `nurse` VALUES (22, 'nurse019', '123456', '冯雅楠', '/avatar/nurse019.jpg', '13812345019', 'fengyn@hospital.com', '急诊科', '主管护师', '2025-08-25 12:25:02', 'NURSE', 6, '急救护理、ICU护理', '全天', '在职', '急诊科骨干护师，能够适应全天候工作，在ICU护理方面有专长。');
INSERT INTO `nurse` VALUES (23, 'nurse020', '123456', '宋美华', '/avatar/nurse020.jpg', '13812345020', 'songmh@hospital.com', '老年科', '护师', '2025-08-25 12:25:02', 'NURSE', 4, '老年护理、家庭护理指导', '倒班', '在职', '年轻的老年科护师，善于与老年患者沟通，在家庭护理指导方面有特长。');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '密码',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '姓名',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像',
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '角色',
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '电话',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  `level` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '身份',
  `gender` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '性别',
  `age` int(0) NULL DEFAULT NULL COMMENT '年龄',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '住址',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '学生信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'zhangsan', '123456', '张三1', 'http://localhost:9090/files/1700466407524-柴犬.jpeg', 'USER', '18899997777', 'zhangsan@xm.com', '学生', '男', 72, '河北省');
INSERT INTO `user` VALUES (10, 'lisi', '123123', '李', 'http://localhost:9090/files/1754887484590-1.jpg', 'USER', NULL, NULL, '学生', NULL, 77, NULL);

-- ----------------------------
-- Table structure for user_activity_preference
-- ----------------------------
DROP TABLE IF EXISTS `user_activity_preference`;
CREATE TABLE `user_activity_preference`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '偏好记录ID',
  `user_id` int(0) NOT NULL COMMENT '用户ID',
  `category_id` int(0) NOT NULL COMMENT '活动分类ID',
  `preference_level` int(0) NULL DEFAULT 3 COMMENT '偏好等级(1-5分，5分最高)',
  `total_participations` int(0) NULL DEFAULT 0 COMMENT '总参与次数',
  `total_attended` int(0) NULL DEFAULT 0 COMMENT '实际出席次数',
  `last_participation_date` date NULL DEFAULT NULL COMMENT '最后参与日期',
  `average_rating` decimal(3, 2) NULL DEFAULT NULL COMMENT '平均评分',
  `favorite_time_slot` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '偏好时间段：MORNING-上午，AFTERNOON-下午，EVENING-晚上',
  `notes` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '偏好备注',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_category`(`user_id`, `category_id`) USING BTREE,
  INDEX `idx_category_id`(`category_id`) USING BTREE,
  INDEX `idx_preference_level`(`preference_level`) USING BTREE,
  CONSTRAINT `fk_preference_category` FOREIGN KEY (`category_id`) REFERENCES `activity_category` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_preference_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户活动偏好表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_activity_preference
-- ----------------------------
INSERT INTO `user_activity_preference` VALUES (1, 1, 1, 5, 8, 7, '2024-04-20', 4.50, 'MORNING', '喜欢晨练，身体状况良好', '2024-01-15 10:00:00', '2025-07-27 10:38:01');
INSERT INTO `user_activity_preference` VALUES (2, 1, 2, 4, 3, 3, '2024-04-15', 4.00, 'AFTERNOON', '偶尔参加娱乐活动', '2024-01-15 10:00:00', '2025-07-27 10:38:01');
INSERT INTO `user_activity_preference` VALUES (3, 1, 4, 5, 5, 5, '2024-04-10', 4.80, 'MORNING', '非常关注健康知识', '2024-01-15 10:00:00', '2025-07-27 10:38:01');

-- ----------------------------
-- Table structure for user_medication
-- ----------------------------
DROP TABLE IF EXISTS `user_medication`;
CREATE TABLE `user_medication`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '用药记录ID',
  `user_id` int(0) NOT NULL COMMENT '用户ID（老人）',
  `medication_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '药物名称',
  `medication_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '药物类型：CARDIOVASCULAR-心血管，DIABETES-糖尿病，ANALGESIC-止痛，ANTIBIOTIC-抗生素等',
  `dosage` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '单次剂量（如：1片，5ml）',
  `frequency` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用药频率（如：每日3次，每12小时1次）',
  `administration_times` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '具体用药时间（如：08:00,12:00,18:00）',
  `meal_relation` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'WITH_MEAL' COMMENT '与餐食关系：BEFORE_MEAL-餐前，WITH_MEAL-餐中，AFTER_MEAL-餐后，EMPTY_STOMACH-空腹',
  `start_date` date NOT NULL COMMENT '开始用药日期',
  `end_date` date NULL DEFAULT NULL COMMENT '结束用药日期',
  `prescribed_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '开药医生',
  `prescription_date` date NULL DEFAULT NULL COMMENT '开药日期',
  `purpose` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '用药目的',
  `special_instructions` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '特殊用药说明',
  `side_effects` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '副作用记录',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'ACTIVE' COMMENT '状态：ACTIVE-进行中，COMPLETED-已完成，STOPPED-已停药',
  `stop_reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '停药原因',
  `remarks` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '备注信息',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `created_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_medication_name`(`medication_name`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE,
  INDEX `idx_start_date`(`start_date`) USING BTREE,
  CONSTRAINT `fk_user_medication_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户药物管理表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_medication
-- ----------------------------
INSERT INTO `user_medication` VALUES (1, 1, '阿司匹林肠溶片', 'CARDIOVASCULAR', '1片', '每日1次', '08:00', 'AFTER_MEAL', '2024-01-16', '2024-07-16', '李医生', '2024-01-15', '预防心脑血管疾病1', '餐后服用，避免空腹', NULL, 'STOPPED', '用户主动停药', NULL, '2025-08-02 22:18:13', '2025-08-21 14:13:23', 'admin');
INSERT INTO `user_medication` VALUES (2, 1, '二甲双胍片', 'DIABETES', '1片', '每日2次', '08:00,18:00', 'WITH_MEAL', '2024-01-16', '2024-07-16', '李医生', '2024-01-15', '控制血糖', '随餐服用，定期监测血糖', NULL, 'ACTIVE', NULL, NULL, '2025-08-02 22:18:13', '2025-08-02 22:18:13', 'admin');
INSERT INTO `user_medication` VALUES (3, 1, '氨氯地平片', 'CARDIOVASCULAR', '1片', '每日1次', '08:00', 'AFTER_MEAL', '2024-01-11', NULL, '王医生', '2024-01-10', '控制血压', '固定时间服用，监测血压变化', NULL, 'ACTIVE', NULL, NULL, '2025-08-02 22:18:13', '2025-08-02 22:18:13', 'admin');
INSERT INTO `user_medication` VALUES (4, 1, '维生素D3', 'SUPPLEMENT', '1粒', '每日1次', '08:00', 'WITH_MEAL', '2024-01-01', '2024-06-30', '李医生', '2023-12-28', '补充维生素D', '有助于钙的吸收', NULL, 'ACTIVE', NULL, NULL, '2025-08-02 22:18:13', '2025-08-02 22:18:13', 'admin');

SET FOREIGN_KEY_CHECKS = 1;
