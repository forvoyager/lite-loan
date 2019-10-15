package com.etl.asset.mapper;

import com.etl.asset.common.model.RepaymentFormModel;
import com.etl.base.jdbc.mapper.IBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <b>author</b>: forvoyager@outlook.com
 * <b>time</b>: 2019-09-30 09:30:12 <br>
 * <b>description</b>: 借款项目还款报表 mapper操作 <br>
 */
@Mapper
public interface RepaymentFormMapper extends IBaseMapper<RepaymentFormModel> {
}
