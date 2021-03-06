package com.mizhousoft.boot.sms;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.mizhousoft.cloudsdk.CloudSDKException;
import com.mizhousoft.cloudsdk.sms.CloudSmsTemplate;
import com.mizhousoft.cloudsdk.sms.SmsApplicationFactory;
import com.mizhousoft.cloudsdk.sms.SmsApplicationService;
import com.mizhousoft.cloudsdk.sms.SmsTemplateContainer;

/**
 * TestTencentSmsService Test
 *
 * @version
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public class TestTencentSmsService
{
	@Autowired
	private SmsApplicationFactory smsApplicationFactory;

	@Autowired
	private SmsTemplateContainer smsTemplateContainer;

	@Test
	public void pushNotification() throws CloudSDKException
	{
		String appId = "1400119865";
		String templateCode = "register";
		String signName = "米舟科技";
		Object templateId = "650126";
		CloudSmsTemplate template = new CloudSmsTemplate(appId, templateCode, signName, templateId);
		smsTemplateContainer.register(template);

		SmsApplicationService smsApplicationService = smsApplicationFactory.getByName("tencent");

		String phoneNumber = "+8618902844821";
		String host = "127.0.0.1";
		smsApplicationService.sendVerificationCode(phoneNumber, host, templateCode);
	}
}
