CREATE DATABASE `www_etl_com` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `www_etl_com`;
create user etl_admin identified by '123456'; 
-- 分配权限 
grant all privileges on www_etl_com.* to 'etl_admin'@'%'identified by '123456' with grant option; 
-- 刷新mysql用户权限
flush privileges ; 

CREATE TABLE `etl_borrow` (
  `borrow_id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '标的id',
  `title` varchar(100) NOT NULL COMMENT '标题',
  `sub_title` varchar(100) DEFAULT '' COMMENT '副标题 ',
  `user_id` int NOT NULL COMMENT '借款人id',
  `status` smallint(4) NOT NULL DEFAULT '0' COMMENT '状态',
  `protocol_id` int DEFAULT '0' COMMENT '协议编号',
  `amount` decimal(11,2) NOT NULL DEFAULT '0.00' COMMENT '借款金额',
  `invested_amount` decimal(11,2) NOT NULL DEFAULT '0.00' COMMENT '已投金额',
  `min_invest_amount` decimal(11,0) NOT NULL DEFAULT '100' COMMENT '最小投标金额',
  `repayment_amount` decimal(11,2) NOT NULL DEFAULT '0.00' COMMENT '应还款金额',
  `repaymented_amount` decimal(11,2) NOT NULL DEFAULT '0.00' COMMENT '已还款金额',
  `period` int NOT NULL DEFAULT '0' COMMENT '借款期数',
  `repaymented_period` int NOT NULL DEFAULT '0' COMMENT '已还款期数',
  `apr` decimal(5,2) NOT NULL DEFAULT '0.00' COMMENT '利率',
  `repayment_type` int NOT NULL DEFAULT '0' COMMENT '还款方式',
  `invest_start_time` int NOT NULL DEFAULT '0' COMMENT '投标开始时间（秒）',
  `invest_end_time` int NOT NULL DEFAULT '0' COMMENT '投标结束时间（秒）',
  `per_partion_amount` decimal(5,2) DEFAULT '100.00' COMMENT '每份金额',
  `prepayment` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否提前还款 0-否 1-是',
  `bank_order_id` int DEFAULT '0' COMMENT '银行存管-标的录入订单号',
  `bank_final_id` int DEFAULT '0' COMMENT '银行存管-满标终审订单号',
  `create_time` int NOT NULL DEFAULT '0' COMMENT '创建时间（秒）',
  `update_time` int NOT NULL DEFAULT '0' COMMENT '最后更新时间（秒）',
  `version` smallint(6) DEFAULT '0' COMMENT '版本号，每次更新+1',
  PRIMARY KEY (`borrow_id`),
  KEY `user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='借款项目';

CREATE TABLE `etl_borrow_data` (
  `borrow_id` INT NOT NULL,
  `comment` varchar(500) DEFAULT '' COMMENT '借款说明',
  `check_comment` varchar(500) DEFAULT '' COMMENT '初审评语',
  `recheck_comment` varchar(500) DEFAULT '' COMMENT '复审评语',
  `risk_control_comment` varchar(500) DEFAULT '' COMMENT '复审评语',
  `create_time` int NOT NULL DEFAULT '0' COMMENT '创建时间（秒）',
  `update_time` int NOT NULL DEFAULT '0' COMMENT '最后更新时间（秒）',
  `version` smallint(6) DEFAULT '0' COMMENT '版本号，每次更新+1',
  PRIMARY KEY (`borrow_id`))
COMMENT = '借款项目相关数据';

CREATE TABLE `etl_creditor` (
  `creditor_id` int NOT NULL AUTO_INCREMENT COMMENT '债权ID',
  `borrow_id` int NOT NULL DEFAULT '0' COMMENT '标的id',
  `user_id` int NOT NULL DEFAULT '0' COMMENT '债权人',
  `invest_amount` decimal(11,2) NOT NULL DEFAULT '0.00' COMMENT '投资金额',
  `surplus_period` smallint(3) NOT NULL DEFAULT '0' COMMENT '剩余期数',
  `unpaid_amount` decimal(11,2) NOT NULL DEFAULT '0.00' COMMENT '待回收金额',
  `unpaid_capital` decimal(11,2) NOT NULL DEFAULT '0.00' COMMENT '待回收本金',
  `unpaid_interest` decimal(11,2) NOT NULL DEFAULT '0.00' COMMENT '待回收利息',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态',
  `start_time` int NOT NULL DEFAULT '0' COMMENT '债权开始日期',
  `end_time` int NOT NULL DEFAULT '0' COMMENT '债权结束日期',
  `transfer_user_id` int DEFAULT '0' COMMENT '债权转让目标用户ID',
  `transfer_start_time` int NOT NULL DEFAULT '0' COMMENT '债转开始时间',
  `transfer_end_time` int NOT NULL DEFAULT '0' COMMENT '债转结束时间',
  `transfer_transaction_time` int NOT NULL DEFAULT '0' COMMENT '债转成交时间',
  `transfer_value` decimal(11,2) NOT NULL DEFAULT '0.00' COMMENT '债转债权公允价值',
  `transfer_surplus_capital` decimal(11,2) NOT NULL DEFAULT '0.00' COMMENT '债转剩余本金',
  `transfer_unpaid_capital_interest` decimal(11,2) NOT NULL DEFAULT '0.00' COMMENT '债转待收本息',
  `transfer_surplus_period` smallint(3) NOT NULL DEFAULT '0' COMMENT '债转剩余期限',
  `partion_num` int(11) DEFAULT '0' COMMENT '持有原始本金份数',
  `create_time` int NOT NULL DEFAULT '0' COMMENT '创建时间（秒）',
  `update_time` int NOT NULL DEFAULT '0' COMMENT '最后更新时间（秒）',
  `version` smallint(6) DEFAULT '0' COMMENT '版本号，每次更新+1',
  PRIMARY KEY (`creditor_id`),
  KEY `borrow_id` (`borrow_id`) USING BTREE,
  KEY `user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='债权信息';

CREATE TABLE `etl_invest` (
  `invest_id` int NOT NULL AUTO_INCREMENT COMMENT '编号',
  `user_id` int NOT NULL DEFAULT '0' COMMENT '标投人',
  `borrow_id` int NOT NULL DEFAULT '0' COMMENT '标的id',
  `invest_amount` decimal(11,2) NOT NULL DEFAULT '0.00' COMMENT '标投金额',
  `holding_partion` int DEFAULT '0' COMMENT '持有份数',
  `channel` tinyint(1) NOT NULL DEFAULT '1' COMMENT '投资渠道',
  `create_time` int NOT NULL DEFAULT '0' COMMENT '创建时间（秒）',
  `update_time` int NOT NULL DEFAULT '0' COMMENT '最后更新时间（秒）',
  `version` smallint(6) DEFAULT '0' COMMENT '版本号，每次更新+1',
  PRIMARY KEY (`invest_id`),
  KEY `borrow_id` (`borrow_id`) USING BTREE,
  KEY `user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='投资记录';

CREATE TABLE `etl_invest_user_funds` (
  `user_id` int NOT NULL DEFAULT '0' COMMENT '用户ID',
  `available` decimal(11,2) NOT NULL DEFAULT '0.00' COMMENT '账户可用余额',
  `frozen` decimal(11,2) NOT NULL DEFAULT '0.00' COMMENT '冻结金额',
  `cumulant_invest` decimal(11,2) NOT NULL DEFAULT '0.00' COMMENT '累计投资金额',
  `cumulant_invest_income` decimal(11,2) NOT NULL DEFAULT '0.00' COMMENT '累计收益利息（正常利息+加息金额）',
  `unpaid_amount` decimal(11,2) NOT NULL DEFAULT '0.00' COMMENT '待收回金额',
  `cumulant_withdraw_amount` decimal(11,2) NOT NULL DEFAULT '0.00' COMMENT '累计提现金额',
  `cumulant_promotion_income` decimal(11,2) NOT NULL DEFAULT '0.00' COMMENT '累计推广收益',
  `create_time` int NOT NULL DEFAULT '0' COMMENT '创建时间（秒）',
  `update_time` int NOT NULL DEFAULT '0' COMMENT '最后更新时间（秒）',
  `version` smallint(6) DEFAULT '0' COMMENT '版本号，每次更新+1',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='投资用户资金账户';

CREATE TABLE `etl_invest_user_funds_data` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '流水ID',
  `user_id` int NOT NULL DEFAULT '0' COMMENT '用户号',
  `amount` decimal(11,2) NOT NULL DEFAULT '0.00' COMMENT '金额',
  `balance` decimal(11,2) NOT NULL DEFAULT '0.00' COMMENT '余额',
  `income` tinyint(1) NOT NULL DEFAULT '-1' COMMENT '标志位',
  `type` smallint(5) NOT NULL DEFAULT '0' COMMENT '交易类型',
  `ref_id` int NOT NULL DEFAULT '0' COMMENT '关联ID',
  `ref_table` smallint(3) NOT NULL DEFAULT '0' COMMENT '关联表',
  `create_time` int NOT NULL DEFAULT '0' COMMENT '创建时间（秒）',
  `update_time` int NOT NULL DEFAULT '0' COMMENT '最后更新时间（秒）',
  `version` smallint(6) DEFAULT '0' COMMENT '版本号，每次更新+1',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='投资用户资金流水';

CREATE TABLE `etl_borrow_user_funds` (
  `user_id` int NOT NULL DEFAULT '0' COMMENT '用户ID',
  `available` decimal(11,2) NOT NULL DEFAULT '0.00' COMMENT '账户可用余额',
  `frozen` decimal(11,2) NOT NULL DEFAULT '0.00' COMMENT '冻结金额',
  `arrear` decimal(11,2) NOT NULL DEFAULT '0.00' COMMENT '当前欠款',
  `cumulant_borrow_amount` decimal(11,2) NOT NULL DEFAULT '0.00' COMMENT '借款人累计借款金额',
  `cumulant_payment_interest` decimal(11,2) NOT NULL DEFAULT '0.00' COMMENT '借款人累计支付利息（正常支付的利息）',
  `cumulant_manage_fee` decimal(11,2) NOT NULL DEFAULT '0.00' COMMENT '借款人累计支付管理费（正常支付的管理费）',
  `cumulant_service_fee` decimal(11,2) NOT NULL DEFAULT '0.00' COMMENT '借款人累计支付服务费',
  `cumulant_recharge` decimal(11,2) NOT NULL DEFAULT '0.00' COMMENT '累计充值金额',
  `cumulant_overdue_period` smallint NOT NULL DEFAULT '0' COMMENT '累计逾期期数',
  `create_time` int NOT NULL DEFAULT '0' COMMENT '创建时间（秒）',
  `update_time` int NOT NULL DEFAULT '0' COMMENT '最后更新时间（秒）',
  `version` smallint(6) DEFAULT '0' COMMENT '版本号，每次更新+1',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='借款用户资金账户';

CREATE TABLE `etl_borrow_user_funds_data` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '流水ID',
  `user_id` int NOT NULL DEFAULT '0' COMMENT '用户号',
  `amount` decimal(11,2) NOT NULL DEFAULT '0.00' COMMENT '金额',
  `balance` decimal(11,2) NOT NULL DEFAULT '0.00' COMMENT '余额',
  `income` tinyint(1) NOT NULL DEFAULT '-1' COMMENT '标志位',
  `type` smallint(5) NOT NULL DEFAULT '0' COMMENT '交易类型',
  `ref_id` int NOT NULL DEFAULT '0' COMMENT '关联ID',
  `ref_table` smallint(3) NOT NULL DEFAULT '0' COMMENT '关联表',
  `create_time` int NOT NULL DEFAULT '0' COMMENT '创建时间（秒）',
  `update_time` int NOT NULL DEFAULT '0' COMMENT '最后更新时间（秒）',
  `version` smallint(6) DEFAULT '0' COMMENT '版本号，每次更新+1',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='借款用户资金流水';

CREATE TABLE `etl_user` (
  `user_id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `user_name` varchar(30) NOT NULL DEFAULT '' COMMENT '用户名',
  `email` varchar(30) NOT NULL DEFAULT '' COMMENT '邮箱',
  `mobile_number` varchar(11) NOT NULL DEFAULT '' COMMENT '手机号',
  `encrypt_salt` char(6) NOT NULL DEFAULT '' COMMENT '加密盐',
  `password` varchar(32) NOT NULL DEFAULT '' COMMENT '密码',
  `pay_password` varchar(32) NOT NULL DEFAULT '' COMMENT '支付密码',
  `user_role` tinyint(1) NOT NULL DEFAULT '1' COMMENT '用户角色 1-出借人 2-借款人 3-担保人',
  `registry_time` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '注册时间',
  `registry_channel` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '注册渠道',
  `portrait` varchar(100) NOT NULL DEFAULT '' COMMENT '头像',
  `create_time` int(11) NOT NULL DEFAULT '0' COMMENT '创建时间（秒）',
  `update_time` int(11) NOT NULL DEFAULT '0' COMMENT '最后更新时间（秒）',
  `version` smallint(6) NOT NULL DEFAULT '0' COMMENT '版本号，每次更新+1',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_name` (`user_name`) USING BTREE,
  KEY `mobile_number` (`mobile_number`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户';

CREATE TABLE `etl_user_data` (
  `user_id` int NOT NULL COMMENT '用户ID',
  `gender` tinyint(1) DEFAULT NULL COMMENT '性别(5-女，7-男)',
  `id_name` varchar(30) DEFAULT '' COMMENT '真实姓名',
  `id_number` varchar(30) DEFAULT '' COMMENT '身份证号',
  `contacts` varchar(1000) DEFAULT '' COMMENT '联系人信息，JSON格式',
  `jobs` varchar(1000) DEFAULT '' COMMENT '工作信息，JSON格式',
  `residence` varchar(1000) DEFAULT '' COMMENT '居住信息，JSON格式',
  `create_time` int NOT NULL DEFAULT '0' COMMENT '创建时间（秒）',
  `update_time` int NOT NULL DEFAULT '0' COMMENT '最后更新时间（秒）',
  `version` smallint(6) DEFAULT '0' COMMENT '版本号，每次更新+1',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户信息';

CREATE TABLE `etl_repayment_statement` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `borrow_id` int(11) NOT NULL DEFAULT '0' COMMENT '标的id',
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '借款人',
  `period` smallint(3) NOT NULL DEFAULT '0' COMMENT '第几期',
  `amount` decimal(11,2) NOT NULL DEFAULT '0.00' COMMENT '当期应还金额',
  `capital` decimal(11,2) NOT NULL DEFAULT '0.00' COMMENT '本金',
  `interest` decimal(11,2) NOT NULL DEFAULT '0.00' COMMENT '利息',
  `manage_fee` decimal(11,2) NOT NULL DEFAULT '0.00' COMMENT '管理费',
  `plan_repayment_time` int(11) NOT NULL DEFAULT '0' COMMENT '应还款日期',
  `actual_repayment_time` int(11) DEFAULT '0' COMMENT '实际还款时间',
  `overdue_days` smallint(5) NOT NULL DEFAULT '0' COMMENT '逾期天数',
  `overdue_penalty` decimal(11,2) NOT NULL DEFAULT '0.00' COMMENT '逾期罚款',
  `reduction_amount` decimal(11,2) NOT NULL DEFAULT '0.00' COMMENT '减免金额',
  `advance` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否垫付 0否 1是',
  `advance_time` int(11) NOT NULL DEFAULT '0' COMMENT '垫付时间',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态',
  `create_time` int(11) NOT NULL DEFAULT '0' COMMENT '创建时间（秒）',
  `update_time` int(11) NOT NULL DEFAULT '0' COMMENT '最后更新时间（秒）',
  `version` smallint(6) DEFAULT '0' COMMENT '版本号，每次更新+1',
  PRIMARY KEY (`id`),
  KEY `borrow_id` (`borrow_id`) USING BTREE,
  KEY `user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='借款项目还款报表';

