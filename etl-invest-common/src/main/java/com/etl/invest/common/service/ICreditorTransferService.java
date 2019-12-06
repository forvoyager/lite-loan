package com.etl.invest.common.service;

import com.etl.base.jdbc.service.IBaseService;
import com.etl.invest.common.model.CreditorTransferModel;

/**
 * <b>author</b>: forvoyager@outlook.com
 * <b>time</b>: 2019-12-05 17:02:07 <br>
 * <b>description</b>: 债权转让信息 服务定义 <br>
 */
public interface ICreditorTransferService extends IBaseService<CreditorTransferModel> {

  /**
   * 申请债权转让
   * @param creditor_id 债权id
   * @param partition 转出份数
   * @throws Exception
   */
  void apply(long creditor_id, int partition) throws Exception;

}
