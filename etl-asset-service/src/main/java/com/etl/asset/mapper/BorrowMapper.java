package com.etl.asset.mapper;

import com.etl.base.jdbc.mapper.IBaseMapper;
import com.etl.asset.common.model.BorrowModel;
import org.apache.ibatis.annotations.Mapper;

/**
 * <b>author</b>: forvoyager@outlook.com
 * <b>time</b>: 2019-09-30 09:30:12 <br>
 * <b>description</b>: 借款项目 mapper操作 <br>
 */
@Mapper
public interface BorrowMapper extends IBaseMapper<BorrowModel> {
}
