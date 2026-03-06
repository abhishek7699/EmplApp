package com.EmplApp.EmplApp;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class EmplAppApplication {

	public static void main(String[] args) {

		SpringApplication.run(EmplAppApplication.class, args);
	}
	@Bean
	public ApplicationRunner runner(RabbitTemplate rabbitTemplate) {
		return args -> {
			System.out.println("RabbitMQ connected: " +
					rabbitTemplate.getConnectionFactory().toString());
		};
	}


}
