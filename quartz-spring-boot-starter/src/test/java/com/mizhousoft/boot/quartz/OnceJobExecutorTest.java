package com.mizhousoft.boot.quartz;

import java.time.LocalDateTime;
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
 * OnceJobExecutorImpl Test
 *
 * @version
 */
@ExtendWith(SpringExtension.class)
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

			LocalDateTime startDate = LocalDateTime.now().plusSeconds(5);

			JobContext jobContext = JobContextBuilder.newJobContext(HelloJobExecution.class).withJobIdentity("test").build();
			quartzScheduler.scheduleOnceJob(startDate, dataMap, jobContext);
		}
		catch (QuartzException e)
		{
			Assertions.fail(e.getMessage());
		}

		TimeUnit.SECONDS.sleep(10);
	}
}
