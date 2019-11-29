# lite-loan
P2P借款撮合系统。

# 模块划分
* etl_user_service
* etl_asset_service
* etl_invest_service
* etl_message_service

# 功能列表及开发进度

-[x] 已完成
-[ ] 未完成

* 用户模块

  -[x] 注册
  -[x] 登陆
  -[x] 退出
  -[ ] 会员等级
  -[ ] 用户账户
    -[x] 入账
    -[x] 支出（冻结、解冻、支出）
    -[x] 充值
    -[ ] 提现

* 借款模块

  -[x] 申请
  -[x] 审核
  -[x] 发标
  -[x] 终审
  -[ ] 还款
    -[x] 正常还款
    -[ ] 代偿
    -[ ] 还代偿

* 投资模块

  -[x] 投标
  -[ ] 购买债权
  -[ ] 发起债权转让

# 技术栈
- mysql 8.0
- springboot 2.1.8.RELEASE
- apache dubbo 2.7.0
- hessian 4.0.38
- zookeeper 3.5.5
- netty 4.0.42.Final
- jackson 2.9.9
- seata 0.8.1

# 参考

