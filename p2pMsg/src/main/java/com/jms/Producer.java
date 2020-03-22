package com.jms;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Producer {

    public static void main(String[] args) throws JMSException {

        //ConnectionFactory:连接工厂。JMS用它创建连接
        ConnectionFactory connectionFactory;
        //Connection：JMS 客户端到JMS Privider的连接
        Connection connection=null;
        //Session :一个发送或接收消息的线程
        Session session;
        //Destination :消息的目的地，消息发送给谁
        Destination destination;
        //消息的发送者
        MessageProducer producer;
        //构造ConnectionFactory实例对象，此处采用ActiveMq的实现jar
        connectionFactory=new ActiveMQConnectionFactory(
                ActiveMQConnectionFactory.DEFAULT_USER,
                ActiveMQConnectionFactory.DEFAULT_PASSWORD,
                "tcp://localhost:61616"
        );
        //从构造工厂获取链接对象
        try {
            connection=connectionFactory.createConnection();
            //启动
            connection.start();
            // Session： 一个发送或接收消息的线程
            session=connection.createSession(Boolean.TRUE,Session.AUTO_ACKNOWLEDGE);
            // Destination ：消息的目的地;消息发送给谁.
            // 获取session注意参数值zym-queue是Query的名字
            destination=session.createQueue("zym-queue");
            //   destination=session.createTopic("FirstTopic");
            // MessageProducer：消息生产者
            producer=session.createProducer(destination);
            // 设置不持久化
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);//如果不需要持久化设置为DeliveryMode.PERSISTENT

            // 发送一条消息
            for (int i = 1; i <= 5; i++) {
                sendMsg(session, producer, i);
            }
            session.commit();

        } catch (JMSException e) {
            e.printStackTrace();
        }finally {
            if(null!=connection){
                try {
                    connection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void sendMsg(Session session,MessageProducer producer,int i) throws JMSException {
        // 创建一条文本消息
        TextMessage message = session.createTextMessage("Hello ActiveMQ！" + i);
        // 通过消息生产者发出消息
        producer.send(message);
    }

}
