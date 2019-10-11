package com.fmcna.fhpckd.fabric;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;

@Configuration
@EnableJms
public class ReceiverConfig {

	  @Value("${activemq.broker-url}")
	  private String brokerUrl;
	  
	  @Value("${activemq.user}")
	  private String userName;
	  
	  @Value("${activemq.password}")
	  private String password;
	  
	  @Value("${activemq.maxConcurrentConsumers}")
	  private int maxConcurrentConsumers;

	  @Value("${activemq.concurrentConsumers}")
	  private String concurrentConsumers;

	  @Bean
	  public ActiveMQConnectionFactory receiverActiveMQConnectionFactory() {
	    ActiveMQConnectionFactory activeMQConnectionFactory =
	        new ActiveMQConnectionFactory();
	    activeMQConnectionFactory.setBrokerURL(brokerUrl);
	    activeMQConnectionFactory.setUserName(userName);
	    activeMQConnectionFactory.setPassword(password);
	    activeMQConnectionFactory.setMaxThreadPoolSize(maxConcurrentConsumers);
	    
	    return activeMQConnectionFactory;
	  }

	  @Bean
	  public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
	    DefaultJmsListenerContainerFactory factory =
	        new DefaultJmsListenerContainerFactory();
	    factory
	        .setConnectionFactory(receiverActiveMQConnectionFactory());

	    factory.setConcurrency(concurrentConsumers);

	    return factory;
	  }

	  @Bean
	  public Receiver receiver() {
	    return new Receiver();
	  }
}
