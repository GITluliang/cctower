SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for account
-- ----------------------------
DROP TABLE IF EXISTS `account`;
CREATE TABLE `account` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `balance` decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '余额',
  `service_charge` int(5) DEFAULT '0' COMMENT '服务费折扣',
  `card` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '银行卡号',
  `bank` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '银行',
  `bank_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '开户行',
  `parking_id` bigint(11) NOT NULL COMMENT '停车场ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_parking_id` (`parking_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of account
-- ----------------------------
BEGIN;
INSERT INTO `account` VALUES (1, 0.00, 0, NULL, NULL, NULL, 1, '2019-05-20 00:12:23');
COMMIT;

-- ----------------------------
-- Table structure for billing
-- ----------------------------
DROP TABLE IF EXISTS `billing`;
CREATE TABLE `billing` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `parking_id` bigint(11) NOT NULL,
  `monthly_price` decimal(11,2) DEFAULT NULL COMMENT '月租费用',
  `unit_price` decimal(10,2) DEFAULT NULL COMMENT '计费标准(分钟)',
  `free_time` int(11) DEFAULT NULL COMMENT '免费停车时间(分钟)',
  `paid_free_time` int(11) DEFAULT NULL COMMENT '付款后的出厂预留时间',
  `wechat_discount` int(5) DEFAULT NULL COMMENT '微信折扣百分比',
  `alipay_discount` int(5) DEFAULT NULL COMMENT '支付宝折扣百分比',
  `day_cost` decimal(10,2) DEFAULT NULL COMMENT '24小时最高收费',
  `user_id` bigint(20) NOT NULL COMMENT '所属物业公司ID',
  PRIMARY KEY (`id`),
  KEY `idx_parking_id` (`parking_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='停车计费逻辑表';

-- ----------------------------
-- Table structure for billing_detail
-- ----------------------------
DROP TABLE IF EXISTS `billing_detail`;
CREATE TABLE `billing_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parking_id` bigint(20) NOT NULL,
  `type` tinyint(4) NOT NULL COMMENT '0:分时计费 1：分段计费',
  `unit_type` tinyint(4) DEFAULT NULL COMMENT '0：分钟 1：小时',
  `unit_price` decimal(10,2) DEFAULT NULL COMMENT '单价',
  `one` decimal(10,2) DEFAULT NULL,
  `two` decimal(10,2) DEFAULT NULL,
  `three` decimal(10,2) DEFAULT NULL,
  `four` decimal(10,2) DEFAULT NULL,
  `five` decimal(10,2) DEFAULT NULL,
  `six` decimal(10,2) DEFAULT NULL,
  `seven` decimal(10,2) DEFAULT NULL,
  `eight` decimal(10,2) DEFAULT NULL,
  `nine` decimal(10,2) DEFAULT NULL,
  `ten` decimal(10,2) DEFAULT NULL,
  `eleven` decimal(10,2) DEFAULT NULL,
  `twelve` decimal(10,2) DEFAULT NULL,
  `thirteen` decimal(10,2) DEFAULT NULL,
  `fourteen` decimal(10,2) DEFAULT NULL,
  `fifteen` decimal(10,2) DEFAULT NULL,
  `sixteen` decimal(10,2) DEFAULT NULL,
  `seventeen` decimal(10,2) DEFAULT NULL,
  `eighteen` decimal(10,2) DEFAULT NULL,
  `nineteen` decimal(10,2) DEFAULT NULL,
  `twenty` decimal(10,2) DEFAULT NULL,
  `twenty_one` decimal(10,2) DEFAULT NULL,
  `twenty_two` decimal(10,2) DEFAULT NULL,
  `twenty_three` decimal(10,2) DEFAULT NULL,
  `twenty_four` decimal(10,2) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `user_id` bigint(20) NOT NULL COMMENT '所属物业公司ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for business_transaction_record
-- ----------------------------
DROP TABLE IF EXISTS `business_transaction_record`;
CREATE TABLE `business_transaction_record` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(11) NOT NULL,
  `type` tinyint(2) NOT NULL COMMENT '0: 支出 1：充值',
  `car_number` varchar(50) DEFAULT NULL,
  `amount` decimal(12,2) NOT NULL COMMENT '交易金额',
  `balance` decimal(12,2) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for car
-- ----------------------------
DROP TABLE IF EXISTS `car`;
CREATE TABLE `car` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `number` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '车牌号',
  `parking_id` bigint(11) DEFAULT NULL COMMENT '停车场ID',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '车主姓名',
  `phone` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '联系方式',
  `corporate_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '公司名称',
  `remarks` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  `classification` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '月租车自定义分类',
  `parking_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0:临时车 1:包月 2:VIP 3:商户车辆\n',
  `monthly_parking_start` datetime DEFAULT NULL COMMENT '包月车辆开始时间',
  `monthly_parking_end` datetime DEFAULT NULL COMMENT '包月车辆结束时间',
  `status` tinyint(2) DEFAULT NULL COMMENT '1：正常 0：禁用',
  `infield_permission` tinyint(2) DEFAULT '0' COMMENT '1：有 0：无',
  `create_id` bigint(11) DEFAULT NULL COMMENT '创建人',
  `open_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '小程序openId',
  `free_time` int(11) DEFAULT NULL COMMENT '商户车辆免费时长',
  `cost` decimal(12,2) DEFAULT NULL COMMENT '商户车辆费用',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_number` (`number`) USING BTREE,
  KEY `idx_parking_type` (`parking_type`) USING BTREE,
  KEY `idx_parking_id` (`parking_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='车辆表';

-- ----------------------------
-- Table structure for deposit_record
-- ----------------------------
DROP TABLE IF EXISTS `deposit_record`;
CREATE TABLE `deposit_record` (
  `id` bigint(12) NOT NULL AUTO_INCREMENT,
  `amount` decimal(12,2) NOT NULL COMMENT '提现金额',
  `service_charge` decimal(12,2) DEFAULT NULL COMMENT '手续费',
  `user_id` bigint(12) DEFAULT NULL COMMENT '用户ID',
  `account_id` bigint(12) NOT NULL COMMENT '账户ID',
  `parking_id` bigint(12) DEFAULT NULL COMMENT '停车场ID',
  `card` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '提现卡号',
  `bank_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '开户行',
  `status` tinyint(2) DEFAULT NULL COMMENT '0：未转账 1：转账成功',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `transfer_time` datetime DEFAULT NULL COMMENT '转账完成时间',
  PRIMARY KEY (`id`),
  KEY `idx_account_id` (`account_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for member
-- ----------------------------
DROP TABLE IF EXISTS `member`;
CREATE TABLE `member` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `balance` decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '余额',
  `open_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'openId',
  `phone` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '手机号',
  `nick_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '用户名',
  `avatar_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '头像地址',
  `province` varchar(30) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '省份',
  `city` varchar(30) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '城市',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `user_open_id` (`open_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='ToC用户表';

-- ----------------------------
-- Table structure for member_car
-- ----------------------------
DROP TABLE IF EXISTS `member_car`;
CREATE TABLE `member_car` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `member_id` bigint(11) NOT NULL COMMENT 'customer_user_id',
  `car_id` bigint(11) NOT NULL COMMENT 'car_id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `user_id` (`member_id`) USING BTREE,
  KEY `car_id` (`car_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户-车牌关系表';

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(11) DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '菜单名称',
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '菜单URL',
  `perms` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '授权(多个用逗号分隔，如：user:list,user:create)',
  `type` tinyint(4) DEFAULT NULL COMMENT '类型   0：目录   1：菜单   2：按钮',
  `icon` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '菜单图标',
  `order_num` int(11) DEFAULT NULL COMMENT '排序',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='菜单';

-- ----------------------------
-- Records of menu
-- ----------------------------
BEGIN;
INSERT INTO `menu` VALUES (1, 0, '系统管理', '', '', 0, 'fa fa-desktop', 2, '2019-03-03 14:05:39', '2019-04-27 13:53:13');
INSERT INTO `menu` VALUES (2, 1, '系统菜单', 'sys/menu/', 'sys:menu:menu', 1, 'fa fa-th-list', 1, '2019-03-03 14:05:39', '2019-03-03 14:05:39');
INSERT INTO `menu` VALUES (3, 1, '用户管理', 'sys/user/', 'sys:user:user', 1, 'fa fa-user', 3, '2019-03-03 14:05:39', '2019-03-03 14:05:39');
INSERT INTO `menu` VALUES (4, 2, '新增', NULL, 'sys:menu:add', 2, NULL, 0, '2019-03-03 14:05:39', '2019-03-03 14:05:39');
INSERT INTO `menu` VALUES (5, 2, '编辑', NULL, 'sys:menu:edit', 2, NULL, 1, '2019-03-03 14:05:39', '2019-03-03 14:05:39');
INSERT INTO `menu` VALUES (6, 2, '删除', NULL, 'sys:menu:remove', 2, NULL, 2, '2019-03-03 14:05:39', '2019-03-03 14:05:39');
INSERT INTO `menu` VALUES (7, 3, '新增', NULL, 'sys:user:add', 2, NULL, 0, '2019-03-03 14:05:39', '2019-03-03 14:05:39');
INSERT INTO `menu` VALUES (8, 3, '编辑', NULL, 'sys:user:edit', 2, NULL, 1, '2019-03-03 14:05:39', '2019-03-03 14:05:39');
INSERT INTO `menu` VALUES (9, 3, '删除', NULL, 'sys:user:remove', 2, NULL, 2, '2019-03-03 14:05:39', '2019-03-03 14:05:39');
INSERT INTO `menu` VALUES (10, 1, '角色管理', 'sys/role', 'sys:role:role', 1, 'a fa-paw', 2, '2019-03-03 14:05:39', '2019-03-03 14:05:39');
INSERT INTO `menu` VALUES (11, 10, '新增', NULL, 'sys:role:add', 2, NULL, 0, '2019-03-03 14:05:39', '2019-03-03 14:05:39');
INSERT INTO `menu` VALUES (12, 10, '编辑', NULL, 'sys:role:edit', 2, NULL, 1, '2019-03-03 14:05:39', '2019-03-03 14:05:39');
INSERT INTO `menu` VALUES (13, 10, '删除', NULL, 'sys:role:remove', 2, NULL, 2, '2019-03-03 14:05:39', '2019-03-03 14:05:39');
INSERT INTO `menu` VALUES (14, 2, '批量删除', NULL, 'sys:menu:batchRemove', 2, NULL, 3, '2019-03-03 14:05:39', '2019-03-03 14:05:39');
INSERT INTO `menu` VALUES (15, 3, '批量删除', NULL, 'sys:user:batchRemove', 2, NULL, 3, '2019-03-03 14:05:39', '2019-03-03 14:05:39');
INSERT INTO `menu` VALUES (16, 10, '批量删除', NULL, 'sys:role:batchRemove', 2, NULL, 3, '2019-03-03 14:05:39', '2019-03-03 14:05:39');
INSERT INTO `menu` VALUES (17, 0, '系统工具', '', '', 0, 'fa fa-gear', 3, '2019-03-10 14:05:39', '2019-04-27 13:53:24');
INSERT INTO `menu` VALUES (18, 17, '代码生成', 'common/generator', 'common:generator', 1, 'fa fa-code', 0, '2019-03-10 14:05:39', '2019-03-10 14:05:39');
INSERT INTO `menu` VALUES (19, 0, '停车管理', '', '', 0, 'fa fa-automobile', 0, '2019-03-10 12:49:59', '2019-03-11 11:10:54');
INSERT INTO `menu` VALUES (20, 19, '停车场管理', 'sys/parking', 'sys:parking:parking', 1, 'fa fa-file-powerpoint-o', 0, '2019-03-10 13:12:16', '2019-03-10 13:12:16');
INSERT INTO `menu` VALUES (21, 19, '商户管理', 'sys/business', 'sys:business:business', 1, 'fa fa-institution', 4, '2019-03-10 13:14:54', '2019-04-01 11:34:41');
INSERT INTO `menu` VALUES (22, 19, '长租车辆', 'sys/car/long', 'sys:car:car', 1, 'fa fa-cab', 3, '2019-03-10 13:16:15', '2019-04-01 11:34:28');
INSERT INTO `menu` VALUES (23, 19, '商户车辆', 'sys/car/business', 'sys:car:car', 1, 'fa fa-bus', 6, '2019-03-11 09:52:16', '2019-05-05 01:33:24');
INSERT INTO `menu` VALUES (24, 20, '新增', '', 'sys:parking:add', 2, '', 0, '2019-03-11 11:08:53', '2019-03-11 11:08:53');
INSERT INTO `menu` VALUES (25, 20, '编辑', '', 'sys:parking:edit', 2, '', 1, '2019-03-11 11:09:33', '2019-03-11 11:09:33');
INSERT INTO `menu` VALUES (26, 20, '删除', '', 'sys:parking:remove', 2, '', 2, '2019-03-11 11:10:04', '2019-03-11 11:10:04');
INSERT INTO `menu` VALUES (27, 20, '批量删除', '', 'sys:parking:batchRemove', 2, '', 3, '2019-03-11 11:10:30', '2019-03-12 11:47:25');
INSERT INTO `menu` VALUES (28, 21, '新增', '', 'sys:business:add', 2, '', 0, '2019-03-12 11:48:35', '2019-03-12 11:48:35');
INSERT INTO `menu` VALUES (29, 21, '编辑', '', 'sys:business:edit', 2, '', 1, '2019-03-12 11:49:03', '2019-03-12 11:49:03');
INSERT INTO `menu` VALUES (30, 21, '删除', '', 'sys:business:remove', 2, '', 2, '2019-03-12 11:49:32', '2019-03-12 11:49:32');
INSERT INTO `menu` VALUES (31, 21, '批量删除', '', 'sys:business:batchRemove', 2, '', 3, '2019-03-12 11:49:55', '2019-03-12 11:49:55');
INSERT INTO `menu` VALUES (32, 22, '新增', '', 'sys:car:add', 2, '', 0, '2019-03-14 12:07:01', '2019-03-15 10:08:29');
INSERT INTO `menu` VALUES (33, 22, '编辑', '', 'sys:car:edit', 2, '', 1, '2019-03-14 12:08:11', '2019-03-15 10:08:58');
INSERT INTO `menu` VALUES (34, 22, '删除', '', 'sys:car:remove', 2, '', 2, '2019-03-14 12:08:43', '2019-03-15 10:08:46');
INSERT INTO `menu` VALUES (35, 22, '批量删除', '', 'sys:car:batchRemove', 2, '', 3, '2019-03-14 12:09:07', '2019-03-15 10:09:10');
INSERT INTO `menu` VALUES (36, 23, '新增', '', 'sys:car:add', 2, '', 0, '2019-03-14 12:09:35', '2019-03-15 09:48:05');
INSERT INTO `menu` VALUES (37, 23, '编辑', '', 'sys:car:edit', 2, '', 1, '2019-03-14 12:09:52', '2019-03-15 09:48:26');
INSERT INTO `menu` VALUES (38, 23, '删除', '', 'sys:car:edit', 2, '', 2, '2019-03-14 12:10:15', '2019-03-15 09:48:41');
INSERT INTO `menu` VALUES (39, 23, '批量删除', '', 'sys:car:batchRemove', 2, '', 3, '2019-03-14 12:10:50', '2019-03-15 09:48:55');
INSERT INTO `menu` VALUES (40, 19, '出入口管理', 'sys/passageway', 'sys:passageway:passageway', 1, 'fa fa-exchange', 2, '2019-03-16 23:03:07', '2019-04-01 11:33:34');
INSERT INTO `menu` VALUES (41, 40, '新增', '', 'sys:passageway:add', 2, '', 0, '2019-03-16 23:05:00', '2019-03-16 23:05:00');
INSERT INTO `menu` VALUES (42, 40, '编辑', '', 'sys:passageway:edit', 2, '', 1, '2019-03-16 23:05:31', '2019-03-16 23:05:31');
INSERT INTO `menu` VALUES (43, 40, '删除', '', 'sys:passageway:remove', 2, '', 2, '2019-03-16 23:05:56', '2019-03-16 23:05:56');
INSERT INTO `menu` VALUES (44, 40, '批量删除', '', 'sys:passageway:batchRemove', 2, '', 3, '2019-03-16 23:06:20', '2019-03-16 23:06:20');
INSERT INTO `menu` VALUES (46, 45, '新增', '', 'sys:billing:add', 2, '', 0, '2019-04-01 11:35:40', '2019-04-01 11:35:40');
INSERT INTO `menu` VALUES (47, 45, '编辑', '', 'sys:billing:edit', 2, '', 1, '2019-04-01 11:36:08', '2019-04-01 11:36:08');
INSERT INTO `menu` VALUES (48, 45, '删除', '', 'sys:billing:remove', 2, '', 2, '2019-04-01 11:36:36', '2019-04-01 11:36:36');
INSERT INTO `menu` VALUES (49, 45, '批量删除', '', 'sys:billing:batchRemove', 2, '', 3, '2019-04-01 11:36:57', '2019-04-01 11:36:57');
INSERT INTO `menu` VALUES (50, 0, '财务管理', '', '', 0, 'fa fa-credit-card', 1, '2019-04-27 13:53:03', '2019-04-27 14:06:51');
INSERT INTO `menu` VALUES (51, 50, '临时车流水', 'sys/finance', 'sys:finance:finance', 1, 'fa fa-credit-card-alt', 2, '2019-04-27 14:09:08', '2019-05-26 22:46:45');
INSERT INTO `menu` VALUES (52, 50, '账户管理', 'sys/account', 'sys:account:account', 1, 'fa fa-credit-card-alt', 0, '2019-04-27 15:01:27', '2019-04-27 15:01:27');
INSERT INTO `menu` VALUES (53, 52, '新增', '', 'sys:account:add', 2, '', 0, '2019-04-27 16:49:34', '2019-04-27 16:49:34');
INSERT INTO `menu` VALUES (54, 52, '编辑', '', 'sys:account:edit', 2, '', 1, '2019-04-27 16:50:07', '2019-04-27 16:50:07');
INSERT INTO `menu` VALUES (55, 52, '删除', '', 'sys:account:remove', 2, '', 2, '2019-04-27 16:50:27', '2019-04-27 16:50:39');
INSERT INTO `menu` VALUES (56, 52, '批量删除', '', 'sys:account:batchRemove', 2, '', 3, '2019-04-27 16:51:03', '2019-04-27 16:51:03');
INSERT INTO `menu` VALUES (57, 50, '提现记录', 'sys/depositRecord', 'sys:depositRecord:depositRecord', 1, '', 5, '2019-04-29 23:55:50', '2019-05-26 22:47:14');
INSERT INTO `menu` VALUES (58, 57, '新增', '', 'sys:depositRecord:add', 2, '', 0, '2019-04-29 23:56:47', '2019-04-30 00:25:29');
INSERT INTO `menu` VALUES (59, 57, '编辑', '', 'sys:depositRecord:edit', 2, '', 1, '2019-04-29 23:57:12', '2019-04-30 00:25:48');
INSERT INTO `menu` VALUES (60, 57, '删除', '', 'sys:depositRecord:remove', 2, '', 2, '2019-04-29 23:57:43', '2019-04-30 00:26:00');
INSERT INTO `menu` VALUES (61, 57, '批量删除', '', 'sys:depositRecord:batchRemove', 2, '', 3, '2019-04-29 23:58:17', '2019-04-30 00:26:13');
INSERT INTO `menu` VALUES (62, 57, '完成转账', '', 'sys:depositRecord:finish', 2, '', 4, '2019-04-30 01:37:44', '2019-04-30 01:37:44');
INSERT INTO `menu` VALUES (63, 57, '列表', '', 'sys:depositRecord:depositRecord', 2, '', 5, '2019-05-01 10:57:36', '2019-05-01 10:57:36');
INSERT INTO `menu` VALUES (64, 50, '商户充值', 'sys/tenantTopUp', 'sys:tenantTopUp:tenantTopUp', 1, '', 4, '2019-05-04 11:48:48', '2019-05-26 22:47:03');
INSERT INTO `menu` VALUES (65, 64, '充值', '', 'sys:tenantTopUp:edit', 2, '', 0, '2019-05-04 21:25:24', '2019-05-04 21:48:03');
INSERT INTO `menu` VALUES (66, 19, '商户流水', 'sys/businessTransaction', 'sys:businessTransaction:businessTransaction', 1, 'fa fa-calculator', 5, '2019-05-05 01:33:11', '2019-05-05 01:33:11');
INSERT INTO `menu` VALUES (67, 50, '月租车流水', 'sys/renew', 'sys:renew:renew', 1, '', 3, '2019-05-15 00:23:48', '2019-05-26 22:46:55');
INSERT INTO `menu` VALUES (68, 50, '交易记录', 'sys/trading', 'sys:trading:trading', 1, '', 1, '2019-05-15 23:34:28', '2019-05-15 23:34:28');
INSERT INTO `menu` VALUES (69, 1, '服务费配置', 'sys/charge', 'sys:charge:charge', 1, '', 4, '2019-05-15 23:39:40', '2019-05-15 23:39:54');
INSERT INTO `menu` VALUES (70, 0, '计费管理', '', '', 0, 'fa fa-calculator', 1, '2019-07-03 23:00:33', '2019-07-03 23:00:33');
INSERT INTO `menu` VALUES (71, 70, '基础计费', 'sys/billing', 'sys:billing:billing', 1, '', 0, '2019-07-03 23:02:09', '2019-07-03 23:02:09');
INSERT INTO `menu` VALUES (72, 70, '计费方式A', 'sys/billing/typeA', 'sys:billingDetail:billingDetail', 1, '', 1, '2019-07-03 23:04:09', '2019-07-03 23:04:24');
INSERT INTO `menu` VALUES (73, 70, '计费方式B', 'sys/billing/typeB', 'sys:billingDetail:billingDetail', 1, '', 2, '2019-07-03 23:05:03', '2019-07-03 23:05:03');
INSERT INTO `menu` VALUES (74, 71, '新增', '', 'sys:billing:add', 2, '', 0, '2019-07-06 11:02:55', '2019-07-06 11:02:55');
INSERT INTO `menu` VALUES (75, 71, '编辑', '', 'sys:billing:edit', 2, '', 1, '2019-07-06 11:03:23', '2019-07-06 11:03:23');
INSERT INTO `menu` VALUES (76, 71, '删除', '', 'sys:billing:remove', 2, '', 2, '2019-07-06 11:03:46', '2019-07-06 11:03:46');
INSERT INTO `menu` VALUES (77, 71, '批量删除', '', 'sys:billing:batchRemove', 2, '', 3, '2019-07-06 11:04:08', '2019-07-06 11:04:08');
INSERT INTO `menu` VALUES (78, 72, '新增', '', 'sys:billingDetail:add', 2, '', 0, '2019-07-06 11:05:11', '2019-07-06 11:05:11');
INSERT INTO `menu` VALUES (79, 72, '编辑', '', 'sys:billingDetail:edit', 2, '', 1, '2019-07-06 11:05:26', '2019-07-06 11:05:26');
INSERT INTO `menu` VALUES (80, 72, '删除', '', 'sys:billingDetail:remove', 2, '', 2, '2019-07-06 11:05:45', '2019-07-06 11:05:45');
INSERT INTO `menu` VALUES (81, 72, '批量删除', '', 'sys:billingDetail:batchRemove', 2, '', 3, '2019-07-06 11:06:11', '2019-07-06 11:06:11');
INSERT INTO `menu` VALUES (82, 73, '新增', '', 'sys:billingDetail:add', 2, '', 0, '2019-07-06 11:06:26', '2019-07-06 11:06:26');
INSERT INTO `menu` VALUES (83, 73, '编辑', '', 'sys:billingDetail:edit', 2, '', 1, '2019-07-06 11:06:44', '2019-07-06 11:06:44');
INSERT INTO `menu` VALUES (84, 73, '删除', '', 'sys:billingDetail:remove', 2, '', 2, '2019-07-06 11:07:02', '2019-07-06 11:07:02');
INSERT INTO `menu` VALUES (85, 73, '批量删除', '', 'sys:billingDetail:batchRemove', 2, '', 3, '2019-07-06 11:07:24', '2019-07-06 11:07:24');
COMMIT;

-- ----------------------------
-- Table structure for mq_config
-- ----------------------------
DROP TABLE IF EXISTS `mq_config`;
CREATE TABLE `mq_config` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `parking_id` bigint(11) DEFAULT NULL,
  `queue` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of mq_config
-- ----------------------------
BEGIN;
INSERT INTO `mq_config` VALUES (1, 1, 'zhongliang');
COMMIT;

-- ----------------------------
-- Table structure for parking
-- ----------------------------
DROP TABLE IF EXISTS `parking`;
CREATE TABLE `parking` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(150) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '停车场名称',
  `user_id` bigint(20) NOT NULL COMMENT '所属物业公司ID',
  `location` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '位置',
  `phone` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '负责人联系方式',
  `contacts` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '联系人姓名',
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '负责人邮箱',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='停车场管理';

-- ----------------------------
-- Records of parking
-- ----------------------------
BEGIN;
INSERT INTO `parking` VALUES (1, '京粮大厦停车场', 3, '北京市朝阳区东三环中路16号', '', '2019-05-12 01:11:46', '2019-05-12 01:11:46');
COMMIT;

-- ----------------------------
-- Table structure for parking_record
-- ----------------------------
DROP TABLE IF EXISTS `parking_record`;
CREATE TABLE `parking_record` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `open_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `order_sn` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '最终支付订单号',
  `applet_order_sn` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '小程序支付订单号',
  `qr_code_order_sn` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL '付款码支付订单',
  `prepay_id` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `pay_id` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `car_number` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '车牌号',
  `parking_id` bigint(11) NOT NULL COMMENT '停车场ID',
  `entrance_id` bigint(11) DEFAULT NULL COMMENT '入口ID',
  `exit_id` bigint(11) DEFAULT NULL COMMENT '出口ID',
  `unit_price` decimal(5,2) DEFAULT NULL COMMENT '单价(分钟)',
  `cost_time` int(11) DEFAULT NULL COMMENT '停车时长',
  `cost` decimal(10,2) DEFAULT NULL COMMENT '共消费',
  `pay_status` tinyint(2) DEFAULT NULL COMMENT '0:未支付 1：支付成功',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否出场 0：否 1：是 2：待出场',
  `in_time` datetime NOT NULL COMMENT '入场时间',
  `pay_time` datetime DEFAULT NULL COMMENT '付款时间',
  `out_time` datetime DEFAULT NULL COMMENT '出厂时间',
  PRIMARY KEY (`id`),
  KEY `idx_car_number` (`car_number`) USING BTREE,
  KEY `idx_in_time` (`in_time`) USING BTREE,
  KEY `idx_order_sn` (`order_sn`) USING BTREE,
  KEY `idx_parking_id` (`parking_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='停车记录';


-- ----------------------------
-- Table structure for parking_trading_record
-- ----------------------------
DROP TABLE IF EXISTS `parking_trading_record`;
CREATE TABLE `parking_trading_record` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `parking_id` bigint(11) NOT NULL COMMENT '停车场ID',
  `amount` decimal(12,2) DEFAULT NULL COMMENT '交易金额',
  `type` tinyint(2) DEFAULT NULL COMMENT '0:提现 1:收入',
  `income_type` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '交易类型',
  `pay_time` datetime DEFAULT NULL COMMENT '交易时间',
  PRIMARY KEY (`id`),
  KEY `idx_parking_id` (`parking_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COMMENT='停车场交易记录\n';

-- ----------------------------
-- Table structure for passageway
-- ----------------------------
DROP TABLE IF EXISTS `passageway`;
CREATE TABLE `passageway` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '名字',
  `ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '出入口摄像头IP',
  `parking_id` bigint(11) DEFAULT NULL COMMENT '停车场ID',
  `type` tinyint(2) DEFAULT NULL COMMENT '0：入口，1：出口',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通道';

-- ----------------------------
-- Table structure for renew_record
-- ----------------------------
DROP TABLE IF EXISTS `renew_record`;
CREATE TABLE `renew_record` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `open_id` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '小程序用open_id',
  `order_sn` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '订单编号',
  `prepay_id` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '准备支付ID',
  `pay_id` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '支付ID',
  `car_number` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '车牌号',
  `parking_id` bigint(11) DEFAULT NULL COMMENT '停车场ID',
  `month_count` int(10) NOT NULL COMMENT '续费月数',
  `cost` decimal(11,2) DEFAULT NULL COMMENT '续费金额',
  `pay_status` tinyint(2) NOT NULL DEFAULT '0' COMMENT '支付状态',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_order_sn` (`order_sn`) USING BTREE,
  KEY `idx_open_id` (`open_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='小程序续费记录';

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `sign` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色表';

-- ----------------------------
-- Records of role
-- ----------------------------
BEGIN;
INSERT INTO `role` VALUES (1, '超级用户', 'admin', '拥有最高权限', '2019-03-03 14:05:39', '2019-05-05 01:33:43');
INSERT INTO `role` VALUES (2, '管理员', NULL, '拥有管理员权限', '2019-03-15 09:05:23', '2019-05-05 01:33:51');
INSERT INTO `role` VALUES (3, '物业公司', NULL, '拥有停车管理和财务管理的权限', '2019-03-11 11:28:34', '2019-05-05 01:33:58');
INSERT INTO `role` VALUES (4, '商户', NULL, '仅拥有商户车辆管理功能', '2019-03-14 10:09:48', '2019-05-05 01:34:08');
COMMIT;

-- ----------------------------
-- Table structure for role_menu
-- ----------------------------
DROP TABLE IF EXISTS `role_menu`;
CREATE TABLE `role_menu` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(11) DEFAULT NULL,
  `menu_id` bigint(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色-菜单表';

-- ----------------------------
-- Records of role_menu
-- ----------------------------
BEGIN;
INSERT INTO `role_menu` VALUES (1, 1, 68);
INSERT INTO `role_menu` VALUES (2, 1, 67);
INSERT INTO `role_menu` VALUES (3, 1, 65);
INSERT INTO `role_menu` VALUES (4, 1, 63);
INSERT INTO `role_menu` VALUES (5, 1, 62);
INSERT INTO `role_menu` VALUES (6, 1, 61);
INSERT INTO `role_menu` VALUES (7, 1, 60);
INSERT INTO `role_menu` VALUES (8, 1, 59);
INSERT INTO `role_menu` VALUES (9, 1, 58);
INSERT INTO `role_menu` VALUES (10, 1, 56);
INSERT INTO `role_menu` VALUES (11, 1, 55);
INSERT INTO `role_menu` VALUES (12, 1, 54);
INSERT INTO `role_menu` VALUES (13, 1, 53);
INSERT INTO `role_menu` VALUES (14, 1, 51);
INSERT INTO `role_menu` VALUES (15, 1, 66);
INSERT INTO `role_menu` VALUES (16, 1, 49);
INSERT INTO `role_menu` VALUES (17, 1, 48);
INSERT INTO `role_menu` VALUES (18, 1, 47);
INSERT INTO `role_menu` VALUES (19, 1, 46);
INSERT INTO `role_menu` VALUES (20, 1, 44);
INSERT INTO `role_menu` VALUES (21, 1, 43);
INSERT INTO `role_menu` VALUES (22, 1, 42);
INSERT INTO `role_menu` VALUES (23, 1, 41);
INSERT INTO `role_menu` VALUES (24, 1, 39);
INSERT INTO `role_menu` VALUES (25, 1, 38);
INSERT INTO `role_menu` VALUES (26, 1, 37);
INSERT INTO `role_menu` VALUES (27, 1, 36);
INSERT INTO `role_menu` VALUES (28, 1, 35);
INSERT INTO `role_menu` VALUES (29, 1, 34);
INSERT INTO `role_menu` VALUES (30, 1, 33);
INSERT INTO `role_menu` VALUES (31, 1, 32);
INSERT INTO `role_menu` VALUES (32, 1, 31);
INSERT INTO `role_menu` VALUES (33, 1, 30);
INSERT INTO `role_menu` VALUES (34, 1, 29);
INSERT INTO `role_menu` VALUES (35, 1, 28);
INSERT INTO `role_menu` VALUES (36, 1, 27);
INSERT INTO `role_menu` VALUES (37, 1, 26);
INSERT INTO `role_menu` VALUES (38, 1, 25);
INSERT INTO `role_menu` VALUES (39, 1, 24);
INSERT INTO `role_menu` VALUES (40, 1, 18);
INSERT INTO `role_menu` VALUES (41, 1, 69);
INSERT INTO `role_menu` VALUES (42, 1, 16);
INSERT INTO `role_menu` VALUES (43, 1, 13);
INSERT INTO `role_menu` VALUES (44, 1, 12);
INSERT INTO `role_menu` VALUES (45, 1, 11);
INSERT INTO `role_menu` VALUES (46, 1, 15);
INSERT INTO `role_menu` VALUES (47, 1, 9);
INSERT INTO `role_menu` VALUES (48, 1, 8);
INSERT INTO `role_menu` VALUES (49, 1, 7);
INSERT INTO `role_menu` VALUES (50, 1, 14);
INSERT INTO `role_menu` VALUES (51, 1, 6);
INSERT INTO `role_menu` VALUES (52, 1, 5);
INSERT INTO `role_menu` VALUES (53, 1, 4);
INSERT INTO `role_menu` VALUES (54, 1, 64);
INSERT INTO `role_menu` VALUES (55, 1, 57);
INSERT INTO `role_menu` VALUES (56, 1, 52);
INSERT INTO `role_menu` VALUES (57, 1, 50);
INSERT INTO `role_menu` VALUES (58, 1, 45);
INSERT INTO `role_menu` VALUES (59, 1, 40);
INSERT INTO `role_menu` VALUES (60, 1, 23);
INSERT INTO `role_menu` VALUES (61, 1, 22);
INSERT INTO `role_menu` VALUES (62, 1, 21);
INSERT INTO `role_menu` VALUES (63, 1, 20);
INSERT INTO `role_menu` VALUES (64, 1, 19);
INSERT INTO `role_menu` VALUES (65, 1, 17);
INSERT INTO `role_menu` VALUES (66, 1, 10);
INSERT INTO `role_menu` VALUES (67, 1, 3);
INSERT INTO `role_menu` VALUES (68, 1, 2);
INSERT INTO `role_menu` VALUES (69, 1, 1);
INSERT INTO `role_menu` VALUES (70, 1, 70);
INSERT INTO `role_menu` VALUES (71, 1, 85);
INSERT INTO `role_menu` VALUES (72, 1, 84);
INSERT INTO `role_menu` VALUES (73, 1, 83);
INSERT INTO `role_menu` VALUES (74, 1, 82);
INSERT INTO `role_menu` VALUES (75, 1, 81);
INSERT INTO `role_menu` VALUES (76, 1, 80);
INSERT INTO `role_menu` VALUES (77, 1, 79);
INSERT INTO `role_menu` VALUES (78, 1, 78);
INSERT INTO `role_menu` VALUES (79, 1, 77);
INSERT INTO `role_menu` VALUES (80, 1, 76);
INSERT INTO `role_menu` VALUES (81, 1, 75);
INSERT INTO `role_menu` VALUES (82, 1, 74);
INSERT INTO `role_menu` VALUES (83, 1, 73);
INSERT INTO `role_menu` VALUES (84, 1, 72);
INSERT INTO `role_menu` VALUES (85, 1, 71);
INSERT INTO `role_menu` VALUES (86, 1, -1);
INSERT INTO `role_menu` VALUES (87, 2, 68);
INSERT INTO `role_menu` VALUES (88, 2, 67);
INSERT INTO `role_menu` VALUES (89, 2, 65);
INSERT INTO `role_menu` VALUES (90, 2, 63);
INSERT INTO `role_menu` VALUES (91, 2, 62);
INSERT INTO `role_menu` VALUES (92, 2, 61);
INSERT INTO `role_menu` VALUES (93, 2, 60);
INSERT INTO `role_menu` VALUES (94, 2, 59);
INSERT INTO `role_menu` VALUES (95, 2, 58);
INSERT INTO `role_menu` VALUES (96, 2, 56);
INSERT INTO `role_menu` VALUES (97, 2, 55);
INSERT INTO `role_menu` VALUES (98, 2, 54);
INSERT INTO `role_menu` VALUES (99, 2, 53);
INSERT INTO `role_menu` VALUES (100, 2, 51);
INSERT INTO `role_menu` VALUES (101, 2, 66);
INSERT INTO `role_menu` VALUES (102, 2, 49);
INSERT INTO `role_menu` VALUES (103, 2, 48);
INSERT INTO `role_menu` VALUES (104, 2, 47);
INSERT INTO `role_menu` VALUES (105, 2, 46);
INSERT INTO `role_menu` VALUES (106, 2, 44);
INSERT INTO `role_menu` VALUES (107, 2, 43);
INSERT INTO `role_menu` VALUES (108, 2, 42);
INSERT INTO `role_menu` VALUES (109, 2, 41);
INSERT INTO `role_menu` VALUES (110, 2, 39);
INSERT INTO `role_menu` VALUES (111, 2, 38);
INSERT INTO `role_menu` VALUES (112, 2, 37);
INSERT INTO `role_menu` VALUES (113, 2, 36);
INSERT INTO `role_menu` VALUES (114, 2, 35);
INSERT INTO `role_menu` VALUES (115, 2, 34);
INSERT INTO `role_menu` VALUES (116, 2, 33);
INSERT INTO `role_menu` VALUES (117, 2, 32);
INSERT INTO `role_menu` VALUES (118, 2, 31);
INSERT INTO `role_menu` VALUES (119, 2, 30);
INSERT INTO `role_menu` VALUES (120, 2, 29);
INSERT INTO `role_menu` VALUES (121, 2, 28);
INSERT INTO `role_menu` VALUES (122, 2, 27);
INSERT INTO `role_menu` VALUES (123, 2, 26);
INSERT INTO `role_menu` VALUES (124, 2, 25);
INSERT INTO `role_menu` VALUES (125, 2, 24);
INSERT INTO `role_menu` VALUES (126, 2, 69);
INSERT INTO `role_menu` VALUES (127, 2, 16);
INSERT INTO `role_menu` VALUES (128, 2, 13);
INSERT INTO `role_menu` VALUES (129, 2, 12);
INSERT INTO `role_menu` VALUES (130, 2, 11);
INSERT INTO `role_menu` VALUES (131, 2, 15);
INSERT INTO `role_menu` VALUES (132, 2, 9);
INSERT INTO `role_menu` VALUES (133, 2, 8);
INSERT INTO `role_menu` VALUES (134, 2, 7);
INSERT INTO `role_menu` VALUES (135, 2, 64);
INSERT INTO `role_menu` VALUES (136, 2, 57);
INSERT INTO `role_menu` VALUES (137, 2, 52);
INSERT INTO `role_menu` VALUES (138, 2, 50);
INSERT INTO `role_menu` VALUES (139, 2, 45);
INSERT INTO `role_menu` VALUES (140, 2, 40);
INSERT INTO `role_menu` VALUES (141, 2, 23);
INSERT INTO `role_menu` VALUES (142, 2, 22);
INSERT INTO `role_menu` VALUES (143, 2, 21);
INSERT INTO `role_menu` VALUES (144, 2, 20);
INSERT INTO `role_menu` VALUES (145, 2, 19);
INSERT INTO `role_menu` VALUES (146, 2, 10);
INSERT INTO `role_menu` VALUES (147, 2, 3);
INSERT INTO `role_menu` VALUES (148, 2, 70);
INSERT INTO `role_menu` VALUES (149, 2, 85);
INSERT INTO `role_menu` VALUES (150, 2, 84);
INSERT INTO `role_menu` VALUES (151, 2, 83);
INSERT INTO `role_menu` VALUES (152, 2, 82);
INSERT INTO `role_menu` VALUES (153, 2, 81);
INSERT INTO `role_menu` VALUES (154, 2, 80);
INSERT INTO `role_menu` VALUES (155, 2, 79);
INSERT INTO `role_menu` VALUES (156, 2, 78);
INSERT INTO `role_menu` VALUES (157, 2, 77);
INSERT INTO `role_menu` VALUES (158, 2, 76);
INSERT INTO `role_menu` VALUES (159, 2, 75);
INSERT INTO `role_menu` VALUES (160, 2, 74);
INSERT INTO `role_menu` VALUES (161, 2, 73);
INSERT INTO `role_menu` VALUES (162, 2, 72);
INSERT INTO `role_menu` VALUES (163, 2, 71);
INSERT INTO `role_menu` VALUES (164, 2, -1);
INSERT INTO `role_menu` VALUES (165, 2, 1);
INSERT INTO `role_menu` VALUES (166, 3, 68);
INSERT INTO `role_menu` VALUES (167, 3, 67);
INSERT INTO `role_menu` VALUES (168, 3, 65);
INSERT INTO `role_menu` VALUES (169, 3, 63);
INSERT INTO `role_menu` VALUES (170, 3, 56);
INSERT INTO `role_menu` VALUES (171, 3, 55);
INSERT INTO `role_menu` VALUES (172, 3, 54);
INSERT INTO `role_menu` VALUES (173, 3, 53);
INSERT INTO `role_menu` VALUES (174, 3, 51);
INSERT INTO `role_menu` VALUES (175, 3, 66);
INSERT INTO `role_menu` VALUES (176, 3, 49);
INSERT INTO `role_menu` VALUES (177, 3, 48);
INSERT INTO `role_menu` VALUES (178, 3, 47);
INSERT INTO `role_menu` VALUES (179, 3, 46);
INSERT INTO `role_menu` VALUES (180, 3, 44);
INSERT INTO `role_menu` VALUES (181, 3, 43);
INSERT INTO `role_menu` VALUES (182, 3, 42);
INSERT INTO `role_menu` VALUES (183, 3, 41);
INSERT INTO `role_menu` VALUES (184, 3, 39);
INSERT INTO `role_menu` VALUES (185, 3, 38);
INSERT INTO `role_menu` VALUES (186, 3, 37);
INSERT INTO `role_menu` VALUES (187, 3, 36);
INSERT INTO `role_menu` VALUES (188, 3, 35);
INSERT INTO `role_menu` VALUES (189, 3, 34);
INSERT INTO `role_menu` VALUES (190, 3, 33);
INSERT INTO `role_menu` VALUES (191, 3, 32);
INSERT INTO `role_menu` VALUES (192, 3, 31);
INSERT INTO `role_menu` VALUES (193, 3, 30);
INSERT INTO `role_menu` VALUES (194, 3, 29);
INSERT INTO `role_menu` VALUES (195, 3, 28);
INSERT INTO `role_menu` VALUES (196, 3, 27);
INSERT INTO `role_menu` VALUES (197, 3, 26);
INSERT INTO `role_menu` VALUES (198, 3, 25);
INSERT INTO `role_menu` VALUES (199, 3, 24);
INSERT INTO `role_menu` VALUES (200, 3, 64);
INSERT INTO `role_menu` VALUES (201, 3, 52);
INSERT INTO `role_menu` VALUES (202, 3, 45);
INSERT INTO `role_menu` VALUES (203, 3, 40);
INSERT INTO `role_menu` VALUES (204, 3, 23);
INSERT INTO `role_menu` VALUES (205, 3, 22);
INSERT INTO `role_menu` VALUES (206, 3, 21);
INSERT INTO `role_menu` VALUES (207, 3, 20);
INSERT INTO `role_menu` VALUES (208, 3, 19);
INSERT INTO `role_menu` VALUES (209, 3, 70);
INSERT INTO `role_menu` VALUES (210, 3, 85);
INSERT INTO `role_menu` VALUES (211, 3, 84);
INSERT INTO `role_menu` VALUES (212, 3, 83);
INSERT INTO `role_menu` VALUES (213, 3, 82);
INSERT INTO `role_menu` VALUES (214, 3, 81);
INSERT INTO `role_menu` VALUES (215, 3, 80);
INSERT INTO `role_menu` VALUES (216, 3, 79);
INSERT INTO `role_menu` VALUES (217, 3, 78);
INSERT INTO `role_menu` VALUES (218, 3, 77);
INSERT INTO `role_menu` VALUES (219, 3, 76);
INSERT INTO `role_menu` VALUES (220, 3, 75);
INSERT INTO `role_menu` VALUES (221, 3, 74);
INSERT INTO `role_menu` VALUES (222, 3, 73);
INSERT INTO `role_menu` VALUES (223, 3, 72);
INSERT INTO `role_menu` VALUES (224, 3, 71);
INSERT INTO `role_menu` VALUES (225, 3, -1);
INSERT INTO `role_menu` VALUES (226, 3, 50);
INSERT INTO `role_menu` VALUES (227, 3, 57);
INSERT INTO `role_menu` VALUES (228, 4, 23);
INSERT INTO `role_menu` VALUES (229, 4, 39);
INSERT INTO `role_menu` VALUES (230, 4, 38);
INSERT INTO `role_menu` VALUES (231, 4, 37);
INSERT INTO `role_menu` VALUES (232, 4, 36);
INSERT INTO `role_menu` VALUES (233, 4, -1);
INSERT INTO `role_menu` VALUES (234, 4, 19);
COMMIT;

-- ----------------------------
-- Table structure for top_up_record
-- ----------------------------
DROP TABLE IF EXISTS `top_up_record`;
CREATE TABLE `top_up_record` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `open_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '用户open_id',
  `order_sn` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `prepay_id` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `pay_id` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `parking_id` bigint(11) DEFAULT NULL,
  `billing_type` tinyint(4) NOT NULL COMMENT '1:充值 0：消费',
  `amount` decimal(12,2) NOT NULL COMMENT '消费金额',
  `balance` decimal(12,2) DEFAULT NULL COMMENT '消费后账户余额',
  `pay_status` tinyint(2) NOT NULL DEFAULT '0' COMMENT '支付状态：0：未支付 1：支付成功',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_open_id` (`open_id`) USING BTREE,
  KEY `idx_parking_id` (`parking_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='账单记录';

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `balance` decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '余额',
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '密码',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '物业公司名字',
  `province` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '省',
  `city` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '城市',
  `district` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '区',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '物业公司具体地址',
  `principal` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '负责人名字',
  `phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '负责人联系方式',
  `type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '用户类型：0：物业公司 1：商户',
  `parking_id` bigint(20) DEFAULT NULL COMMENT '商户对应的停车场ID',
  `remaining_time` int(20) DEFAULT NULL COMMENT '商户剩余停车时长',
  `status` tinyint(2) DEFAULT NULL COMMENT '状态：0：禁用 1：启用',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_type` (`type`) USING BTREE,
  KEY `idx_parking_id` (`parking_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表：物业公司';

-- ----------------------------
-- Records of user
-- ----------------------------
BEGIN;
INSERT INTO `user` VALUES (1, 0.00, 'super', '80447fd06af0d2dc400600701ab53dd4', '超级管理员', '北京市', '北京市市辖区', '通州区', '北京通州', '贾顺', '186008088111', 0, NULL, NULL, 1, '2019-03-03 21:40:39', '2019-05-03 17:59:31');
INSERT INTO `user` VALUES (2, 0.00, 'admin', '59d781bd6192385fe38f679299b5852a', '诺泽科技', NULL, NULL, NULL, '北京朝阳双桥', '谭玉龙', '18888888888', 0, NULL, NULL, 1, '2019-05-03 17:48:36', '2019-05-03 17:59:42');
INSERT INTO `user` VALUES (3, 0.00, 'jl', 'a6b7b6359a9af01efe71678d9774948f', '京粮大厦停车场', NULL, NULL, NULL, '北京市朝阳区东三环中路16号', '', '', 0, NULL, NULL, 1, '2019-05-12 01:09:03', '2019-05-12 01:09:34');
COMMIT;

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(11) DEFAULT NULL,
  `role_id` bigint(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户-角色表';

-- ----------------------------
-- Records of user_role
-- ----------------------------
BEGIN;
INSERT INTO `user_role` VALUES (1, 1, 1);
INSERT INTO `user_role` VALUES (2, 2, 2);
INSERT INTO `user_role` VALUES (3, 3, 3);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;

-- -------------------------------------
DROP TABLE IF EXISTS order_number;
create table order_number (
	id bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  order_sn varchar(200) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '订单编号',
	parking_record_id bigint(11) NOT NULL COMMENT '停车记录ID',
	create_time datetime DEFAULT NULL COMMENT '创建时间',
	PRIMARY KEY (id),
	KEY idx_order_sn (order_sn) USING BTREE
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';
