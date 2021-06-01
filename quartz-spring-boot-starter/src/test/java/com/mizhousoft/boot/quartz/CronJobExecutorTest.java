package com.mizhousoft.boot.quartz;

import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * CronJobExecutor Test
 *
 * @version
 */
@RunWith(SpringJUnit4ClassRunner.class)
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
			fail(e.getMessage());
		}

		TimeUnit.SECONDS.sleep(20);
	}
}
