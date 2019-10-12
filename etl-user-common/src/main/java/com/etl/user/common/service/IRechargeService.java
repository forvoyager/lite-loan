package com.etl.user.common.service;

import com.etl.base.common.enums.AccessChannel;

/**
 * @Author: forvoyager@outlook.com
 * @Time: 2019-10-12 10:24
 * @Description: 充值服务 服务定义
 * 
 * 充值流程：
 * 1、系统中初始化充值订单IRechargeService#initOrder(...)，得到订单id。
 * 2、根据用户的充值信息跳转第三方进行充值（带上充值订单）。
 * 3、第三方充值完成后进行回调，系统根据回调信息做处理（成功则给用户账户加钱、订单置为成功，失败则将订单置为失败）。
 */
public interface IRechargeService {

  /**
   * 初始化充值订单
   * 
   * @param user_id 用户id
   * @param amount 充值金额（分）
   * @param channel 充值渠道
   * @return 充值订单id
   * @throws Exception
   */
  public long initOrder(long user_id, long amount, AccessChannel channel) throws Exception;

  /**
   * 充值完成后回调处理
   * 
   * @param order_id 充值订单id
   * @param status 第三方处理结果转换成系统中对应的状态 1成功 2失败
   * @param trace_id 第三方交易流水ID
   * @throws Exception
   */
  public void callback(long order_id, int status, String trace_id) throws Exception;
  
}
