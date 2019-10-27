package com.etl.invest.mapper;

import com.etl.base.jdbc.mapper.IBaseMapper;
import com.etl.invest.common.model.InvestModel;
import org.apache.ibatis.annotations.Mapper;

/**
 * <b>author</b>: forvoyager@outlook.com
 * <b>time</b>: 2019-10-26 11:44:26 <br>
 * <b>description</b>: 投资记录 mapper操作 <br>
 */
@Mapper
public interface InvestMapper extends IBaseMapper<InvestModel> {
}
