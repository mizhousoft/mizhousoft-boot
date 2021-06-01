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
 * UMengPushServiceImpl Test
 *
 * @version
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public class TestUMengPushServiceImpl
{
	@Autowired
	private UnifiedPushService unifiedPushService;

	private PushProvider pushProvider = PushProvider.UMENG;

	private String token = "16089525960272241575710";

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
}
