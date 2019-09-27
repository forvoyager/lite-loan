package com.xr.example.producer;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * @Author: forvoyager@outlook.com
 * @Time: 2019-09-26 15:42
 * @Description:
 */
public class TransactionConsumer {

  public static void main(String[] args) throws InterruptedException, MQClientException {

    // Instantiate with specified consumer group name.
    DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("GROUP_etl_consumer");

    // Specify name server addresses.
    consumer.setNamesrvAddr("localhost:9876");

    // Subscribe one more more topics to consume.
    consumer.subscribe("TOPIC_etl", "*");
    // Register callback to execute on arrival of messages fetched from brokers.
    consumer.registerMessageListener(new MessageListenerConcurrently() {

      @Override
      public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        System.out.printf("%s Receive New Messages: %s %n", Thread.currentThread().getName(), msgs);
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
      }
    });

    //Launch the consumer instance.
    consumer.start();

    System.out.printf("Consumer Started.%n");
  }
  
}
