package com.etl.borrow.service.impl;

import com.etl.base.jdbc.service.impl.BaseServiceImpl;
import com.etl.borrow.common.model.BorrowModel;
import com.etl.borrow.mapper.BorrowMapper;
import com.etl.borrow.service.IBorrowService;
import org.springframework.stereotype.Service;

/**
 * <b>author</b>: forvoyager@outlook.com
 * <b>time</b>: 2019-09-30 09:30:12 <br>
 * <b>description</b>: 借款项目 服务实现 <br>
 */
@Service("borrowService")
public class BorrowServiceImpl extends BaseServiceImpl<BorrowMapper, BorrowModel> implements IBorrowService {
  @Override
  protected String getPrimaryKeyName() {
    return "borrow_id";
  }
}
