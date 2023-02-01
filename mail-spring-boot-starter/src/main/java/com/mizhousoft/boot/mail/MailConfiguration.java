package com.mizhousoft.boot.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

import com.mizhousoft.commons.mail.MailService;
import com.mizhousoft.commons.mail.impl.MailServiceImpl;

/**
 * 配置
 *
 * @version
 */
@Configuration
public class MailConfiguration
{
	@Value("${spring.mail.from}")
	private String from;

	@Bean
	public MailService getRestClientService(JavaMailSender mailSender)
	{
		MailServiceImpl mailService = new MailServiceImpl();
		mailService.setMailSender(mailSender);
		mailService.setFrom(from);

		return mailService;
	}
}
