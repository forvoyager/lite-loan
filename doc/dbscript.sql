CREATE DATABASE `www_etl_com` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `www_etl_com`;
create user etl_admin identified by '123456'; 
-- 分配权限 
grant all privileges on www_etl_com.* to 'etl_admin'@'%'identified by '123456' with grant option;
grant all privileges on www_etl_com.* to 'etl_admin'@'localhost'identified by '123456' with grant option;
-- 刷新mysql用户权限
flush privileges ; 

-- user
CREATE TABLE `etl_user` (
  `user_id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `user_name` varchar(30) NOT NULL DEFAULT '' COMMENT '用户名',
  `email` varchar(30) NOT NULL DEFAULT '' COMMENT '邮箱',
  `mobile_number` varchar(11) NOT NULL DEFAULT '' COMMENT '手机号',
  `encrypt_salt` char(6) NOT NULL DEFAULT '' COMMENT '加密盐',
  `password` varchar(32) NOT NULL DEFAULT '' COMMENT '密码',
  `pay_password` varchar(32) NOT NULL DEFAULT '' COMMENT '支付密码',
  `user_role` tinyint(1) NOT NULL DEFAULT '0' COMMENT '用户角色 0-出借人 1-借款人 2-担保人',
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

CREATE TABLE `etl_user_account` (
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `available` int(11) NOT NULL DEFAULT '0' COMMENT '可用余额（分）',
  `frozen` int(11) NOT NULL DEFAULT '0' COMMENT '冻结金额（分）',
  `id_name` varchar(30) NOT NULL DEFAULT '' COMMENT '真实姓名',
  `id_card` varchar(30) NOT NULL DEFAULT '' COMMENT '身份证号',
  `create_time` int(11) NOT NULL DEFAULT '0' COMMENT '创建时间（秒）',
  `update_time` int(11) NOT NULL DEFAULT '0' COMMENT '最后更新时间（秒）',
  `version` smallint(6) NOT NULL DEFAULT '0' COMMENT '版本号，每次更新+1',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户账户';

CREATE TABLE `etl_user_account_data` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '流水ID',
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户号',
  `amount` int(11) NOT NULL DEFAULT '0' COMMENT '金额（分）',
  `balance` int(11) NOT NULL DEFAULT '0' COMMENT '余额（分）',
  `income` tinyint(1) NOT NULL DEFAULT '-1' COMMENT '标志位',
  `type` smallint(5) NOT NULL DEFAULT '0' COMMENT '交易类型',
  `ref_id` int(11) NOT NULL DEFAULT '0' COMMENT '关联ID',
  `ref_table` smallint(3) NOT NULL DEFAULT '0' COMMENT '关联表',
  `create_time` int(11) NOT NULL DEFAULT '0' COMMENT '创建时间（秒）',
  `update_time` int(11) NOT NULL DEFAULT '0' COMMENT '最后更新时间（秒）',
  `version` smallint(6) DEFAULT '0' COMMENT '版本号，每次更新+1',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='账户流水';

-- borrow
CREATE TABLE `etl_borrow` (
  `borrow_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '标的id',
  `user_id` int(11) NOT NULL COMMENT '借款人id',
  `title` varchar(100) NOT NULL COMMENT '标题',
  `status` smallint(4) NOT NULL DEFAULT '0' COMMENT '状态',
  `amount` int(11) NOT NULL DEFAULT '0' COMMENT '借款金额（分）',
  `period` tinyint(4) NOT NULL DEFAULT '0' COMMENT '借款期数',
  `apr` decimal(5,2) NOT NULL DEFAULT '0.00' COMMENT '利率',
  `partion_amount` int(11) NOT NULL DEFAULT '0' COMMENT '每份金额（分）',
  `repayment_type` int(11) NOT NULL DEFAULT '0' COMMENT '还款方式',
  `invest_start_time` int(11) NOT NULL DEFAULT '0' COMMENT '投标开始时间（秒）',
  `invest_end_time` int(11) NOT NULL DEFAULT '0' COMMENT '投标结束时间（秒）',
  `create_time` int(11) NOT NULL DEFAULT '0' COMMENT '创建时间（秒）',
  `update_time` int(11) NOT NULL DEFAULT '0' COMMENT '最后更新时间（秒）',
  `version` smallint(6) NOT NULL DEFAULT '0' COMMENT '版本号，每次更新+1',
  PRIMARY KEY (`borrow_id`),
  KEY `user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='借款项目';

CREATE TABLE `etl_repayment_form` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `borrow_id` int(11) NOT NULL DEFAULT '0' COMMENT '标的id',
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '借款人',
  `period` smallint(3) NOT NULL DEFAULT '0' COMMENT '第几期',
  `capital` int(11) NOT NULL DEFAULT '0' COMMENT '本金（分）',
  `interest` int(11) NOT NULL DEFAULT '0' COMMENT '利息（分）',
  `manage_fee` int(11) NOT NULL DEFAULT '0' COMMENT '管理费（分）',
  `plan_repayment_time` int(11) NOT NULL DEFAULT '0' COMMENT '应还款日期',
  `actual_repayment_time` int(11) NOT NULL DEFAULT '0' COMMENT '实际还款时间',
  `overdue_days` smallint(5) NOT NULL DEFAULT '0' COMMENT '逾期天数',
  `overdue_penalty` int(11) NOT NULL DEFAULT '0' COMMENT '逾期罚款（分）',
  `reduction_amount` int(11) NOT NULL DEFAULT '0' COMMENT '减免金额（分）',
  `advance` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否垫付 0否 1是',
  `advance_time` int(11) NOT NULL DEFAULT '0' COMMENT '垫付时间',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态',
  `create_time` int(11) NOT NULL DEFAULT '0' COMMENT '创建时间（秒）',
  `update_time` int(11) NOT NULL DEFAULT '0' COMMENT '最后更新时间（秒）',
  `version` smallint(6) NOT NULL DEFAULT '0' COMMENT '版本号，每次更新+1',
  PRIMARY KEY (`id`),
  KEY `borrow_id` (`borrow_id`) USING BTREE,
  KEY `user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='借款项目还款报表';

-- invest
CREATE TABLE `etl_creditor` (
  `creditor_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '债权ID',
  `parent_creditor_id` int(11) NOT NULL COMMENT '父债权ID',
  `user_id` int(11) NOT NULL COMMENT '债权人',
  `borrow_id` int(11) NOT NULL COMMENT '标的id',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态',
  `capital` int(11) NOT NULL DEFAULT '0' COMMENT '本金（分）',
  `interest` int(11) NOT NULL DEFAULT '0' COMMENT '利息（分）',
  `period` tinyint(4) NOT NULL DEFAULT '0' COMMENT '总期数',
  `surplus_period` smallint(3) NOT NULL DEFAULT '0' COMMENT '剩余期数',
  `unpaid_capital` int(11) NOT NULL DEFAULT '0' COMMENT '待回收本金（分）',
  `unpaid_interest` int(11) NOT NULL DEFAULT '0' COMMENT '待回收利息（分）',
  `start_time` int(11) NOT NULL DEFAULT '0' COMMENT '债权开始日期',
  `end_time` int(11) NOT NULL DEFAULT '0' COMMENT '债权结束日期',
  `partion` int(11) NOT NULL DEFAULT '0' COMMENT '持有份数',
  `create_time` int(11) NOT NULL DEFAULT '0' COMMENT '创建时间（秒）',
  `update_time` int(11) NOT NULL DEFAULT '0' COMMENT '最后更新时间（秒）',
  `version` smallint(6) NOT NULL DEFAULT '0' COMMENT '版本号，每次更新+1',
  PRIMARY KEY (`creditor_id`),
  KEY `borrow_id` (`borrow_id`) USING BTREE,
  KEY `user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='债权信息';

CREATE TABLE `etl_profit_form` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '收益ID',
  `creditor_id` int(11) NOT NULL COMMENT '债权ID',
  `user_id` int(11) NOT NULL COMMENT '债权人',
  `borrow_id` int(11) NOT NULL COMMENT '标的id',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态 -1无效 0未还 1已还',
  `capital` int(11) NOT NULL DEFAULT '0' COMMENT '本金（分）',
  `interest` int(11) NOT NULL DEFAULT '0' COMMENT '利息（分）',
  `period` tinyint(4) NOT NULL DEFAULT '0' COMMENT '期数',
  `create_time` int(11) NOT NULL DEFAULT '0' COMMENT '创建时间（秒）',
  `update_time` int(11) NOT NULL DEFAULT '0' COMMENT '最后更新时间（秒）',
  `version` smallint(6) NOT NULL DEFAULT '0' COMMENT '版本号，每次更新+1',
  PRIMARY KEY (`id`),
  KEY `creditor_id` (`creditor_id`) USING BTREE,
  KEY `borrow_id` (`borrow_id`) USING BTREE,
  KEY `user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='投资人收益报表';

CREATE TABLE `etl_invest_record` (
  `invest_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '标投人',
  `borrow_id` int(11) NOT NULL DEFAULT '0' COMMENT '标的id',
  `invest_amount` int(11) NOT NULL DEFAULT '0' COMMENT '标投金额（分）',
  `partion` int(11) NOT NULL DEFAULT '0' COMMENT '份数',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态 0待处理 1成功 2失败',
  `channel` tinyint(1) NOT NULL DEFAULT '0' COMMENT '投资渠道',
  `create_time` int(11) NOT NULL DEFAULT '0' COMMENT '创建时间（秒）',
  `update_time` int(11) NOT NULL DEFAULT '0' COMMENT '最后更新时间（秒）',
  `version` smallint(6) NOT NULL DEFAULT '0' COMMENT '版本号，每次更新+1',
  PRIMARY KEY (`invest_id`),
  KEY `borrow_id` (`borrow_id`) USING BTREE,
  KEY `user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='投资记录';




