package com.etl.invest.common.service;

/**
 * @Author: forvoyager@outlook.com
 * @Time: 2019-10-12 16:20
 * @Description: 借款人还款服务 服务定义
 */
public interface IRepaymentService {

  /**
   * 还款
   * 
   * @param repayment_form_id 借款项目还款报表ID
   * @throws Exception
   */
  void repayment(long repayment_form_id) throws Exception;
  
}
