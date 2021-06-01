package com.mizhousoft.boot.quartz;

import static org.junit.Assert.fail;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * OnceJobExecutorImpl Test
 *
 * @version
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = QuartzApplication.class)
public class OnceJobExecutorTest
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

			Date startDate = new Date(System.currentTimeMillis() + 5000);

			JobContext jobContext = JobContextBuilder.newJobContext(HelloJobExecution.class).withJobIdentity("test").build();
			quartzScheduler.scheduleOnceJob(startDate, dataMap, jobContext);
		}
		catch (QuartzException e)
		{
			fail(e.getMessage());
		}

		TimeUnit.SECONDS.sleep(10);
	}
}
