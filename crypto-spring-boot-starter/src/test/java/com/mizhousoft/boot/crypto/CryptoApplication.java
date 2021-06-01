package com.mizhousoft.boot.crypto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.mizhousoft")
@SpringBootApplication
public class CryptoApplication
{
	public static void main(String[] args)
	{
		SpringApplication.run(CryptoApplication.class, args);
	}
}
