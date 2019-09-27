package com.etl.user.mapper;

import com.etl.base.jdbc.mapper.IBaseMapper;
import com.etl.user.common.model.UserModel;
import org.apache.ibatis.annotations.Mapper;

/**
 * <b>author</b>: forvoyager@outlook.com
 * <b>time</b>: 2019-09-27 14:05:16 <br>
 * <b>description</b>: 用户 mapper操作 <br>
 */
@Mapper
public interface UserMapper extends IBaseMapper<UserModel> {
}
