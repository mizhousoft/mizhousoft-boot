package com.mizhousoft.boot.push;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.mizhousoft.commons.json.JSONException;
import com.mizhousoft.push.PushProvider;
import com.mizhousoft.push.exception.PushException;
import com.mizhousoft.push.request.MessageRequest;
import com.mizhousoft.push.request.NotificationRequest;
import com.mizhousoft.push.result.PushResult;
import com.mizhousoft.push.union.UnifiedPushService;

/**
 * Test HuaweiPushServiceImpl
 *
 * @version
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public class TestHuaweiPushServiceImpl
{
	@Autowired
	private UnifiedPushService unifiedPushService;

	private PushProvider pushProvider = PushProvider.HUAWEI;

	private String token = "0867540035995064300009603500CN01";

	@Test
	public void pushNotification() throws JSONException
	{
		NotificationRequest request = NotificationBuilder.build(token);

		try
		{
			PushResult result = unifiedPushService.pushNotification(pushProvider, request);

			Assert.assertNotNull(result.getTraceId());
		}
		catch (PushException e)
		{
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void pushNotifications() throws JSONException
	{
		String errorToken1 = "IQAAAACy05W_WlYU9cryX1-QfnYGA";
		NotificationRequest request = NotificationBuilder.build(token, errorToken1);

		try
		{
			PushResult result = unifiedPushService.pushNotification(pushProvider, request);

			Assert.assertNotNull(result.getTraceId());
		}
		catch (PushException e)
		{
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void pushErrorNotification() throws JSONException
	{
		String errorToken = "IQAAAACy05W_WlYU9cryX1-QfnYGA";
		NotificationRequest request = NotificationBuilder.build(errorToken);

		try
		{
			PushResult result = unifiedPushService.pushNotification(pushProvider, request);

			Assert.assertNotNull(result.getTraceId());
		}
		catch (PushException e)
		{
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void pushErrorNotifications() throws JSONException
	{
		String errorToken1 = "IQAAAACy05W_WlYU9cryX1-QfnYGA";
		String errorToken2 = "IQAAAACy05W_WlYU9cryX2-QfnYGA";
		NotificationRequest request = NotificationBuilder.build(errorToken1, errorToken2);

		try
		{
			PushResult result = unifiedPushService.pushNotification(pushProvider, request);

			Assert.assertNotNull(result.getTraceId());
		}
		catch (PushException e)
		{
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void pushMessage()
	{
		MessageRequest request = MessageBuilder.build(token);

		try
		{
			PushResult result = unifiedPushService.pushMessage(pushProvider, request);

			Assert.assertNotNull(result.getIllegalTokens());
		}
		catch (PushException e)
		{
			Assert.fail(e.getMessage());
		}
	}
}
