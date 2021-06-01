package com.mizhousoft.boot.quartz;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

/**
 * 任务执行
 *
 * @version
 */
public abstract class JobExecution implements Job
{
	private static final Logger LOG = LoggerFactory.getLogger(JobExecution.class);

	protected JobExecutionContext executionContext;

	protected ApplicationContext applicationContext;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void execute(JobExecutionContext context) throws JobExecutionException
	{
		JobDetail jobDetail = context.getJobDetail();

		executionContext = context;
		applicationContext = getJobData(SchedulerConstants.APPLICATION_CONTEXT, ApplicationContext.class);

		try
		{
			LOG.info("Job {} start to execute.", jobDetail.getKey().toString());

			initialize();

			executeInternal();

			LOG.info("Job execute successfully.");
		}
		catch (Throwable e)
		{
			LOG.error("Job execute failed.", e);

			throw new JobExecutionException(e.getMessage(), e);
		}
		finally
		{
			jobDetail.getJobDataMap().clear();
		}
	}

	protected void initialize() throws JobExecutionException
	{

	}

	protected abstract void executeInternal() throws JobExecutionException;

	protected <T> T getJobData(String key, Class<T> clazz)
	{
		Object object = executionContext.getJobDetail().getJobDataMap().get(key);
		if (clazz.isInstance(object))
		{
			return clazz.cast(object);
		}

		return null;
	}
}
