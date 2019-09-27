package com.xr.example.producer;

import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: forvoyager@outlook.com
 * @Time: 2019-09-26 13:18
 * @Description: 
 * 当RocketMQ发现`Prepared消息`时，会根据这个Listener实现的策略来判断事务是否成功了
 * 
 */
public class TransactionListenerImpl implements TransactionListener {
  
  private AtomicInteger transactionIndex = new AtomicInteger(0);

  private ConcurrentHashMap<String, Integer> localTrans = new ConcurrentHashMap<>();

  @Override
  public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
    int value = transactionIndex.getAndIncrement();
    int status = value % 3;
    if(status == 0){
      throw new RuntimeException("模拟本地事务失败");
    }
    localTrans.put(msg.getTransactionId(), status);
    return LocalTransactionState.UNKNOW;
  }

  @Override
  public LocalTransactionState checkLocalTransaction(MessageExt msg) {
    Integer status = localTrans.get(msg.getTransactionId());
    if (null != status) {
      switch (status) {
        case 0:
          return LocalTransactionState.UNKNOW;
        case 1:
          return LocalTransactionState.COMMIT_MESSAGE;
        case 2:
          return LocalTransactionState.ROLLBACK_MESSAGE;
        default:
          return LocalTransactionState.COMMIT_MESSAGE;
      }
    }
    return LocalTransactionState.COMMIT_MESSAGE;
  }
  
}
