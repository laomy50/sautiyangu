package com.example.sautiyangu;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication

public class SautiyanguApplication {

	@Bean
	public ModelMapper modelMapper(){
		ModelMapper modelMapper= new ModelMapper();
		modelMapper.getConfiguration().setFieldMatchingEnabled(true).setAmbiguityIgnored(true);
		return new ModelMapper();
	}
	public static void main(String[] args) {
		SpringApplication.run(SautiyanguApplication.class, args);
	}

}
