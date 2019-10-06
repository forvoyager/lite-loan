package com.etl.base.common.filter;

import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcException;

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
