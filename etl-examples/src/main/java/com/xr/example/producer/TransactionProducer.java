package com.xr.example.producer;

import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author: forvoyager@outlook.com
 * @Time: 2019-09-26 13:17
 * @Description:
 */
public class TransactionProducer {
  
  public static void main(String[] args) throws Exception {
    
    ExecutorService executorService = new ThreadPoolExecutor(2, 5, 100, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(2000), new ThreadFactory() {
      @Override
      public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setName("client-transaction-msg-check-thread");
        return thread;
      }
    });

    TransactionListener transactionListener = new TransactionListenerImpl();
    TransactionMQProducer producer = new TransactionMQProducer("GROUP_etl_producer");
    producer.setExecutorService(executorService);
    producer.setTransactionListener(transactionListener);
    producer.setNamesrvAddr("localhost:9876");
    producer.start();

    Message msg = new Message("TOPIC_etl", "TAG_etl", "KEY_etl", ("Hello RocketMQ " + System.currentTimeMillis()).getBytes(RemotingHelper.DEFAULT_CHARSET));
    SendResult sendResult = producer.sendMessageInTransaction(msg, null);
    System.out.printf("%s%n", sendResult);

    producer.shutdown();
  }
  
}
