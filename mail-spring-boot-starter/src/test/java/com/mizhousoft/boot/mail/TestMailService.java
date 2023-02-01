package com.mizhousoft.boot.mail;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.mizhousoft.commons.mail.IMailException;
import com.mizhousoft.commons.mail.MailService;

/**
 * TestMailService
 *
 * @version
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = DemoApplication.class)
public class TestMailService
{
	@Autowired
	private MailService mailService;

	@Test
	public void sendTextMail()
	{
		String[] to = { "" };
		String subject = "test";
		String content = "test";

		try
		{
			mailService.sendTextMail(to, subject, content);
		}
		catch (IMailException e)
		{
			Assertions.fail(e);
		}
	}
}
