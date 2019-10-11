package com.fmcna.fhpckd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableJms
@EnableScheduling
@PropertySource("file:/apps/common/sbootapps/sf-services/fhp-ckd-client/config/fhp-ckd-client.properties")
public class SfCkdApplication {

	public static void main(String[] args) {
		SpringApplication.run(SfCkdApplication.class, args);
	}


}

