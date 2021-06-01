package com.mizhousoft.boot.quartz;

import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.quartz.JobExecutionException;

/**
 * HelloJobExecutor
 *
 * @version
 */
public class HelloJobExecution extends JobExecution
{
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void executeInternal() throws JobExecutionException
	{
		Student student = getJobData("student", Student.class);

		System.out.println(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss") + "  " + student);
	}
}
