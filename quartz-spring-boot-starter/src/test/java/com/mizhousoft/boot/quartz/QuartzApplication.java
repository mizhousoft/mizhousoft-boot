package com.mizhousoft.boot.quartz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * QuartzApplication
 *
 * @version
 */
@SpringBootApplication()
@ComponentScan("com.mizhousoft")
public class QuartzApplication
{
	public static void main(String[] args)
	{
		SpringApplication.run(QuartzApplication.class, args);
	}
}
