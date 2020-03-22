package cn.zym;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Consumer {
    private static String brokerURL = "tcp://localhost:61616";
    private static String TOPIC = "zym-topic";

    public static void main(String[] args) throws Exception{
        start();
    }

    static public void start() throws JMSException {
        System.out.println("消费点启动...");
        // 创建ActiveMQConnectionFactory 会话工厂
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(
                ActiveMQConnection.DEFAULT_USER, ActiveMQConnection.DEFAULT_PASSWORD, brokerURL);
        Connection connection = activeMQConnectionFactory.createConnection();
        // 启动JMS 连接
        connection.start();
        // 不开启消息事物，消息只要发送消费者,则表示消息已经签收
        //消息只有确认签收之后，才认为被成功消费，否则会重复的消费。
        //消息事务：事务提交成功，消息被签收。事务回滚，消息会再次传递
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // 创建一个队列
        Topic topic = session.createTopic(TOPIC);
        MessageConsumer consumer = session.createConsumer(topic);
        // consumer.setMessageListener(new MsgListener());
        while (true) {
            TextMessage textMessage = (TextMessage) consumer.receive();
            if (textMessage != null) {
                System.out.println("接受到消息:" + textMessage.getText());
                // textMessage.acknowledge();// 手动签收
                // session.commit();
            } else {
                break;
            }
        }
        connection.close();
    }
}
