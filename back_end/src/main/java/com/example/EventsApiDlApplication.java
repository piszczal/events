package com.example;

import com.example.config.JwtFilter;
import it.ozimov.springboot.mail.configuration.EnableEmailTools;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableEmailTools
@EnableAutoConfiguration
public class EventsApiDlApplication {

	@Bean
	public FilterRegistrationBean jwtFilter() {
		final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		registrationBean.setFilter(new JwtFilter());
		registrationBean.addUrlPatterns("/api/events/*");
        registrationBean.addUrlPatterns("/api/user/*");

		return registrationBean;
	}


	public static void main(String[] args) {
		SpringApplication.run(EventsApiDlApplication.class, args);
	}
}
