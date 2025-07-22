package com.example.customerservice;

import com.example.customerservice.entities.Customer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import com.example.customerservice.repository.CustomerRepository;

@SpringBootApplication
//@EnableJpaRepositories(basePackages = "repository")
//@EntityScan(basePackages = "com/example/customerservice/entities")
public class CustomerServiceApplication {

    public static void main(String[] args) {

        SpringApplication.run(CustomerServiceApplication.class, args);
    }
    @Bean
    CommandLineRunner start(CustomerRepository customerRepository){
        return args -> {
            customerRepository.save(Customer.builder().name("mehdi").email("mehdi@gmail.com").build());
            customerRepository.save(Customer.builder().name("hamid").email("hamid@gmail.com").build());
        };
    }

}
