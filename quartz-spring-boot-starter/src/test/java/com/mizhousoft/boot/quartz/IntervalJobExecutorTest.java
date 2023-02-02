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
 * IntervalJobExecutor Test
 *
 * @version
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = QuartzApplication.class)
public class IntervalJobExecutorTest
{
	@Autowired
	private QuartzScheduler quartzSchedulerService;

	@Test
	public void scheduleIntervalJob() throws InterruptedException
	{
		try
		{
			Student student = new Student(1, "刘德华");

			Map<String, Object> dataMap = new HashMap<>(1);
			dataMap.put("student", student);

			JobContext jobContext = JobContextBuilder.newJobContext(HelloJobExecution.class).withJobIdentity("test").build();
			quartzSchedulerService.scheduleIntervalJob(3, TimeUnit.SECONDS, dataMap, jobContext);
		}
		catch (QuartzException e)
		{
			Assertions.fail(e.getMessage());
		}

		TimeUnit.SECONDS.sleep(13);
	}
}
