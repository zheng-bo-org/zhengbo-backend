package org.zhengbo.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.ResourceUtils;
import org.zhengbo.backend.global_exceptions.MessageGenerator;

import java.io.File;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) throws Exception{
		File file = ResourceUtils.getFile("classpath:exceptionMessages.json");
		MessageGenerator.generateMsg(file);
		SpringApplication.run(BackendApplication.class, args);
	}

}
