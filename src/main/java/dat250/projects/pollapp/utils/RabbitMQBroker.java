package dat250.projects.pollapp.utils;

import com.rabbitmq.client.BuiltinExchangeType;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import com.rabbitmq.client.DeliverCallback;
/**
 * Rabbit helper class
 * 
 * @author Biljan
 */
public class RabbitMQBroker {
	 private final ConnectionFactory factory;
    
    public RabbitMQBroker(String host) {
        factory = new ConnectionFactory();
        factory.setHost(host);
    }
    
    
 // CreateTopic for voting
    /**
	 * Creates topic
	 *
	 * @param topicName
	 * 
	 */
    public void createTopic(String topicName) throws Exception {
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(topicName, BuiltinExchangeType.FANOUT, true);
        }
    }
    
 // Publish results for voting
    /**
   	 * Publish topic
   	 *
   	 * @param topicName
   	 * @param message
   	 * 
   	 */
    public void publish(String topicName, String message) throws Exception {
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.basicPublish(topicName, "", null, message.getBytes());
        }
    }
    
    // Subscribe to a topic and print received messages
    /**
   	 * Subscribe topic
   	 *
   	 * @param topicName
   	 * 
   	 */
    public void subscribe(String topicName) {
        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.exchangeDeclare(topicName, BuiltinExchangeType.FANOUT, true);
            String queueName = channel.queueDeclare().getQueue();
            channel.queueBind(queueName, topicName, "");

            DeliverCallback callback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println("Received: " + message);
            };
           // Automatic  ACK
            channel.basicConsume(queueName, true, callback, consumerTag -> {});

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
