package com.etl.api.controller;

import com.etl.asset.common.enums.BorrowStatus;
import com.etl.asset.common.model.BorrowModel;
import com.etl.asset.common.service.IBorrowService;
import com.etl.base.common.dto.ResultDto;
import com.etl.base.common.enums.Cluster;
import com.etl.base.common.util.AssertUtils;
import com.etl.base.common.util.DateUtils;
import com.etl.base.common.util.Utils;
import com.etl.invest.common.service.IInvestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <b>author</b>: forvoyager@outlook.com
 * <b>time</b>: 2020-01-13 17:10:00 <br>
 * <b>description</b>: 投资操作<br>
 */
@RestController
@RequestMapping("/invest")
public class InvestController extends EtlBaseController {

  @Autowired
  private IInvestService investService;

  @Autowired
  private IBorrowService borrowService;

  @RequestMapping("/bid")
  public ResultDto bid(long borrow_id, long amount) throws Exception{

    BorrowModel borrow = borrowService.selectById(borrow_id, Cluster.slave);
    AssertUtils.notNull(borrow, "标的不存在");

    long current = DateUtils.currentTimeInSecond();
    if(
            current < borrow.getInvest_start_time() ||
                    BorrowStatus.parse(borrow.getStatus()) != BorrowStatus.IN_BID ||
                    borrow.getAvailable_amount().longValue() == 0){
      Utils.throwsBizException("当前标的不可投");
    }
    if(amount > borrow.getAvailable_amount()){
      Utils.throwsBizException("剩余最大可投金额"+borrow.getAvailable_amount()/100);
    }
    investService.bid(super.getSessionUserId(), borrow_id, amount, super.getAccessChannel());

    return ResultDto.success("投标成功");
  }
}
