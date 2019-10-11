package com.fmcna.fhpckd.fabric;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;

@Configuration
public class SenderConfig {

	  @Value("${activemq.broker-url}")
	  private String brokerUrl;
	  
	  @Value("${activemq.user}")
	  private String userName;
	  
	  @Value("${activemq.password}")
	  private String password;
	  
	  @Bean
	  public ActiveMQConnectionFactory senderActiveMQConnectionFactory() {
	    ActiveMQConnectionFactory activeMQConnectionFactory =
	        new ActiveMQConnectionFactory();
	    activeMQConnectionFactory.setBrokerURL(brokerUrl);
	    activeMQConnectionFactory.setUserName(userName);
	    activeMQConnectionFactory.setPassword(password);
	    
	    return activeMQConnectionFactory;
	  }

	  @Bean
	  public CachingConnectionFactory cachingConnectionFactory() {
		  CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory( senderActiveMQConnectionFactory());
		  cachingConnectionFactory.setCacheProducers(false);
		  return cachingConnectionFactory;
	  }

	  @Bean
	  public JmsTemplate jmsTemplate() {
	    return new JmsTemplate(cachingConnectionFactory());
	  }

	  @Bean
	  public Sender sender() {
	    return new Sender();
	  }
}
