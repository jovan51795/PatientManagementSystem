package com.pms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.pms"})
@EnableJpaRepositories
public class PatientManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(PatientManagementSystemApplication.class, args);
	}

}
