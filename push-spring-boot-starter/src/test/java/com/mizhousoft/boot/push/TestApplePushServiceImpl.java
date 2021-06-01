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
import com.mizhousoft.push.request.NotificationRequest;
import com.mizhousoft.push.result.PushResult;
import com.mizhousoft.push.union.UnifiedPushService;

/**
 * ApplePushServiceImpl Test
 *
 * @version
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public class TestApplePushServiceImpl
{
	@Autowired
	private UnifiedPushService unifiedPushService;

	private PushProvider pushProvider = PushProvider.APPLE;

	private String token = "ff320d50eb8b46501412c01a3f25fd3642d6b7406266d7124faf5fda307965d1";

	@Test
	public void pushNotification() throws JSONException
	{
		NotificationRequest request = NotificationBuilder.build(token);

		try
		{
			PushResult result = unifiedPushService.pushNotification(pushProvider, request);

			Assert.assertTrue(result.getIllegalTokens().isEmpty());
		}
		catch (PushException e)
		{
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void pushErrorNotification() throws JSONException
	{
		String errorToken = "1111320d50eb8b46501412c01a3f25fd3642d6b7406266d7124faf5fda307965d1";
		NotificationRequest request = NotificationBuilder.build(errorToken);

		try
		{
			PushResult result = unifiedPushService.pushNotification(pushProvider, request);

			Assert.assertFalse(result.getIllegalTokens().isEmpty());
		}
		catch (PushException e)
		{
			Assert.fail(e.getMessage());
		}
	}
}
