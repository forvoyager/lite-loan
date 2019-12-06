package com.etl.invest.mapper;

import com.etl.base.jdbc.mapper.IBaseMapper;
import com.etl.invest.common.model.CreditorTransferModel;
import org.apache.ibatis.annotations.Mapper;

/**
 * <b>author</b>: forvoyager@outlook.com
 * <b>time</b>: 2019-12-05 17:02:07 <br>
 * <b>description</b>: 债权转让信息 mapper操作 <br>
 */
@Mapper
public interface CreditorTransferMapper extends IBaseMapper<CreditorTransferModel> {
}
