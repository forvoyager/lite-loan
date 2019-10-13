package com.etl.invest.common.service;

import com.etl.base.jdbc.service.IBaseService;
import com.etl.invest.common.model.ProfitFormModel;

/**
 * <b>author</b>: forvoyager@outlook.com
 * <b>time</b>: 2019-09-30 11:51:35 <br>
 * <b>description</b>: 投资人收益报表 服务定义 <br>
 */
public interface IProfitFormService extends IBaseService<ProfitFormModel> {

  /**
   * 收益报表兑付
   *
   * @param form_id 收益报表id
   * @throws Exception
   */
  public void cash(long form_id) throws Exception;

  /**
   * 收益报表兑付
   *
   * @param profitForm 收益报表
   * @throws Exception
   */
  public void cash(ProfitFormModel profitForm) throws Exception;

}
