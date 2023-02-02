package com.mizhousoft.boot.quartz;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * CronJobExecutor Test
 *
 * @version
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = QuartzApplication.class)
public class CronJobExecutorTest
{
	@Autowired
	private QuartzScheduler quartzScheduler;

	@Test
	public void scheduleOnceJob() throws InterruptedException
	{
		try
		{
			Student student = new Student(1, "刘德华");

			Map<String, Object> dataMap = new HashMap<>(1);
			dataMap.put("student", student);

			JobContext jobContext = JobContextBuilder.newJobContext(HelloJobExecution.class).withJobIdentity("test").build();
			quartzScheduler.scheduleCronJob("*/5 * * * * ?", dataMap, jobContext);
		}
		catch (QuartzException e)
		{
			Assertions.fail(e.getMessage());
		}

		TimeUnit.SECONDS.sleep(20);
	}
}
