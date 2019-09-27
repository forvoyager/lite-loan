package com.etl.base.common.filter;

import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcException;

/**
 * @Author: forvoyager@outlook.com
 * @Time: 2019-09-27 14:17
 * @Description: dubbo远程服务filter
 */
public class RemoteServiceFilter implements Filter {
  @Override
  public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
    Result result = invoker.invoke(invocation);
    return result;
  }
}
