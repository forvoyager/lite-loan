package com.etl.user.service.impl;

import com.etl.base.common.enums.Cluster;
import com.etl.base.common.enums.RefTable;
import com.etl.base.common.util.AssertUtils;
import com.etl.base.common.util.DateUtils;
import com.etl.base.common.util.Utils;
import com.etl.base.jdbc.service.impl.BaseServiceImpl;
import com.etl.user.common.enums.FundsOperateType;
import com.etl.user.common.model.UserAccountDataModel;
import com.etl.user.common.model.UserAccountModel;
import com.etl.user.common.service.IUserAccountDataService;
import com.etl.user.common.service.IUserAccountService;
import com.etl.user.mapper.UserAccountMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;

/**
 * <b>author</b>: forvoyager@outlook.com
 * <b>time</b>: 2019-09-29 17:46:22 <br>
 * <b>description</b>: 用户账户 服务实现 <br>
 */
@Service("userAccountService")
public class UserAccountServiceImpl extends BaseServiceImpl<UserAccountMapper, UserAccountModel> implements IUserAccountService {
  @Override
  protected String getPrimaryKeyName() {
    return "user_id";
  }

  @Transactional
  @Override
  public void frozen(long user_id, long amount, FundsOperateType biz_type, RefTable ref_table, long ref_id) throws Exception {
    AssertUtils.notNull(biz_type, "非法的业务操作");
    AssertUtils.notNull(ref_table, "非法的业务关联");
    if(amount <= 0){ return; }
    
    UserAccountModel userAccount = this.selectById(user_id, Cluster.master);
    AssertUtils.notNull(userAccount, "账户不存在");
    if(userAccount.getAvailable() < amount){
      Utils.throwsBizException("余额不足，冻结失败。");
    }

    long current = DateUtils.currentTimeInSecond();
    
    // 可用金额减少，冻结金额增加
    Map updateData =  Utils.newHashMap(
            UserAccountModel.USER_ID, user_id,
            "frozen_amount", amount,
            UserAccountModel.UPDATE_TIME, current,
            UserAccountModel.WHERE_VERSION, userAccount.getVersion()
    );
    if( 1 != this.updateByMap(updateData)){
      Utils.throwsBizException("资金冻结失败，稍后重试。");
    }

    // 记录流水
    UserAccountDataModel accountData = new UserAccountDataModel();
    accountData.setUser_id(user_id);
    accountData.setAmount(amount);
    accountData.setBalance(userAccount.getAvailable()-amount);
    accountData.setIncome(biz_type.getFlag());
    accountData.setType(biz_type.getCode());
    accountData.setRef_table(ref_table.getCode());
    accountData.setRef_id(ref_id);
    accountData.setCreate_time(current);
    accountData.setUpdate_time(current);
    userAccountDataService.insert(accountData);
  }

  @Transactional
  @Override
  public void unfrozen(long user_id, long amount, FundsOperateType biz_type, RefTable ref_table, long ref_id) throws Exception {
    AssertUtils.notNull(biz_type, "非法的业务操作");
    AssertUtils.notNull(ref_table, "非法的业务关联");
    if(amount <= 0){ return; }

    UserAccountModel userAccount = this.selectById(user_id, Cluster.master);
    AssertUtils.notNull(userAccount, "账户不存在");
    if(userAccount.getFrozen() < amount){
      Utils.throwsBizException("冻结金额不足，解冻失败。");
    }
    
    long current = DateUtils.currentTimeInSecond();

    // 冻结金额减少，可用金额增加
    Map updateData =  Utils.newHashMap(
            UserAccountModel.USER_ID, user_id,
            "unfrozen_amount", amount,
            UserAccountModel.UPDATE_TIME, current,
            UserAccountModel.WHERE_VERSION, userAccount.getVersion()
    );
    if( 1 != this.updateByMap(updateData)){
      Utils.throwsBizException("资金解冻失败，稍后重试。");
    }

    // 记录流水
    UserAccountDataModel accountData = new UserAccountDataModel();
    accountData.setUser_id(user_id);
    accountData.setAmount(amount);
    accountData.setBalance(userAccount.getAvailable()+amount);
    accountData.setIncome(biz_type.getFlag());
    accountData.setType(biz_type.getCode());
    accountData.setRef_table(ref_table.getCode());
    accountData.setRef_id(ref_id);
    accountData.setCreate_time(current);
    accountData.setUpdate_time(current);
    userAccountDataService.insert(accountData);
  }

  @Transactional
  @Override
  public void pay(long user_id, long amount, FundsOperateType biz_type, RefTable ref_table, long ref_id) throws Exception {
    AssertUtils.notNull(biz_type, "非法的业务操作");
    AssertUtils.notNull(ref_table, "非法的业务关联");
    if(amount <= 0){ return; }

    UserAccountModel userAccount = this.selectById(user_id, Cluster.master);
    AssertUtils.notNull(userAccount, "账户不存在");
    if(userAccount.getAvailable() < amount){
      Utils.throwsBizException("可用不足，支出失败。");
    }

    long current = DateUtils.currentTimeInSecond();

    // 可用金额不变，冻结金额减少
    Map updateData =  Utils.newHashMap(
            UserAccountModel.USER_ID, user_id,
            "pay_amount", amount,
            UserAccountModel.UPDATE_TIME, current,
            UserAccountModel.WHERE_VERSION, userAccount.getVersion()
    );
    if( 1 != this.updateByMap(updateData)){
      Utils.throwsBizException("支出失败，稍后重试。");
    }

    // 记录流水
    UserAccountDataModel accountData = new UserAccountDataModel();
    accountData.setUser_id(user_id);
    accountData.setAmount(amount);
    accountData.setBalance(userAccount.getAvailable() - amount); // 可用余额不变，从冻结金额里支出
    accountData.setIncome(biz_type.getFlag());
    accountData.setType(biz_type.getCode());
    accountData.setRef_table(ref_table.getCode());
    accountData.setRef_id(ref_id);
    accountData.setCreate_time(current);
    accountData.setUpdate_time(current);
    userAccountDataService.insert(accountData);
  }

  @Transactional
  @Override
  public void changeAvailable(long user_id, long amount, FundsOperateType biz_type, RefTable ref_table, long ref_id) throws Exception {

    AssertUtils.notNull(biz_type, "非法的业务操作");
    AssertUtils.notNull(ref_table, "非法的业务关联");
    
    if(amount == 0){
      return;
    }
    
    UserAccountModel userAccount = this.selectById(user_id, Cluster.master);
    AssertUtils.notNull(userAccount, "账户不存在");
    
    long current = DateUtils.currentTimeInSecond();

    // 修改可用余额
    long current_available = userAccount.getAvailable() + amount;
    if(current_available < 0){
      Utils.throwsBizException("可用余额不足，扣减失败。");
    }
    Map updateData =  Utils.newHashMap(
            UserAccountModel.USER_ID, user_id,
            UserAccountModel.AVAILABLE, current_available,
            UserAccountModel.UPDATE_TIME, current,
            UserAccountModel.WHERE_VERSION, userAccount.getVersion()
    );
    if( 1 != this.updateByMap(updateData)){
      Utils.throwsBizException("增加可用资金失败，稍后重试。");
    }

    // 记录流水
    UserAccountDataModel accountData = new UserAccountDataModel();
    accountData.setUser_id(user_id);
    accountData.setAmount(amount);
    accountData.setBalance(current_available);
    accountData.setIncome(biz_type.getFlag());
    accountData.setType(biz_type.getCode());
    accountData.setRef_table(ref_table.getCode());
    accountData.setRef_id(ref_id);
    accountData.setCreate_time(current);
    accountData.setUpdate_time(current);
    userAccountDataService.insert(accountData);
  }

  @Resource
  private IUserAccountDataService userAccountDataService;
}
