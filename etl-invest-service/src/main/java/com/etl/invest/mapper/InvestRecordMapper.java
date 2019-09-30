package com.etl.invest.mapper;

import com.etl.base.jdbc.mapper.IBaseMapper;
import com.etl.invest.common.model.InvestRecordModel;
import org.apache.ibatis.annotations.Mapper;

/**
 * <b>author</b>: forvoyager@outlook.com
 * <b>time</b>: 2019-09-30 11:51:35 <br>
 * <b>description</b>: 投资记录 mapper操作 <br>
 */
@Mapper
public interface InvestRecordMapper extends IBaseMapper<InvestRecordModel> {
}
