package com.bearxyz;

import com.bearxyz.common.activiti.modeler.explorer.JsonpCallbackFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication
@EnableJpaAuditing
@ComponentScan({"org.activiti.rest.diagram", "com.bearxyz"})
@EnableAutoConfiguration(exclude = {
        org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration.class,
        org.activiti.spring.boot.SecurityAutoConfiguration.class,
        org.springframework.boot.actuate.autoconfigure.ManagementWebSecurityAutoConfiguration.class
})
public class ErpApplication {

    @Bean
    public JsonpCallbackFilter filter(){
        return new JsonpCallbackFilter();
    }

    public static void main(String[] args) {
        SpringApplication.run(ErpApplication.class, args);
    }
}
