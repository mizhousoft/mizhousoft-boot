package com.mizhousoft.boot.quartz.impl;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.mizhousoft.boot.quartz.CronJobExecutor;
import com.mizhousoft.boot.quartz.IntervalJobExecutor;
import com.mizhousoft.boot.quartz.JobContext;
import com.mizhousoft.boot.quartz.OnceJobExecutor;
import com.mizhousoft.boot.quartz.QuartzException;
import com.mizhousoft.boot.quartz.QuartzScheduler;
import com.mizhousoft.boot.quartz.SchedulerConstants;
import com.mizhousoft.boot.quartz.executor.CronJobExecutorImpl;
import com.mizhousoft.boot.quartz.executor.IntervalJobExecutorImpl;
import com.mizhousoft.boot.quartz.executor.OnceJobExecutorImpl;
import com.mizhousoft.boot.quartz.factory.QuartzFactory;
import com.mizhousoft.boot.quartz.protperties.QuartzThreadPoolProperties;

/**
 * Quartz调度服务
 *
 * @version
 */
@Component
public class DefaultQuartzScheduler implements QuartzScheduler
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultQuartzScheduler.class);

	@Autowired
	private QuartzThreadPoolProperties threadPoolProps;

	@Autowired
	private ApplicationContext applicationContext;

	private Scheduler scheduler;

	private OnceJobExecutor onceJobScheduler;

	private IntervalJobExecutor intervalJobScheduler;

	private CronJobExecutor cronJobExecutor;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void scheduleOnceJob(JobContext context) throws QuartzException
	{
		onceJobScheduler.scheduleOnceJob(context);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void scheduleOnceJob(Map<String, Object> dataMap, JobContext context) throws QuartzException
	{
		onceJobScheduler.scheduleOnceJob(dataMap, context);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void scheduleOnceJob(Date startDate, Map<String, Object> dataMap, JobContext context) throws QuartzException
	{
		onceJobScheduler.scheduleOnceJob(startDate, dataMap, context);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void scheduleIntervalJob(long initialDelayMs, int interval, TimeUnit timeUnit, JobContext context) throws QuartzException
	{
		intervalJobScheduler.scheduleIntervalJob(initialDelayMs, interval, timeUnit, context);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void scheduleIntervalJob(int interval, TimeUnit timeUnit, Map<String, Object> dataMap, JobContext context) throws QuartzException
	{
		intervalJobScheduler.scheduleIntervalJob(interval, timeUnit, dataMap, context);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void scheduleIntervalJob(long initialDelayMs, int interval, TimeUnit timeUnit, Map<String, Object> dataMap, JobContext context)
	        throws QuartzException
	{
		intervalJobScheduler.scheduleIntervalJob(initialDelayMs, interval, timeUnit, dataMap, context);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void scheduleCronJob(String cronExpression, JobContext context) throws QuartzException
	{
		cronJobExecutor.scheduleCronJob(cronExpression, context);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void scheduleCronJob(String cronExpression, Map<String, Object> dataMap, JobContext context) throws QuartzException
	{
		cronJobExecutor.scheduleCronJob(cronExpression, dataMap, context);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean checkExists(String jobName) throws QuartzException
	{
		return checkExists(jobName, SchedulerConstants.DEFAULT_JOB_GROUP_NAME);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean checkExists(String jobName, String jobGroup) throws QuartzException
	{
		JobKey jobKey = new JobKey(jobName, jobGroup);

		try
		{
			return scheduler.checkExists(jobKey);
		}
		catch (SchedulerException e)
		{
			throw new QuartzException(e.getMessage(), e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteJob(String jobName) throws QuartzException
	{
		deleteJob(jobName, SchedulerConstants.DEFAULT_JOB_GROUP_NAME);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteJob(String jobName, String jobGroup) throws QuartzException
	{
		JobKey jobKey = new JobKey(jobName, jobGroup);

		try
		{
			scheduler.deleteJob(jobKey);

			LOG.info("Delete job {} successfully.", jobKey.toString());
		}
		catch (SchedulerException e)
		{
			throw new QuartzException(e.getMessage(), e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void interruptJob(String jobName) throws QuartzException
	{
		interruptJob(jobName, SchedulerConstants.DEFAULT_JOB_GROUP_NAME);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void interruptJob(String jobName, String jobGroup) throws QuartzException
	{
		JobKey jobKey = new JobKey(jobName, jobGroup);

		try
		{
			scheduler.interrupt(jobKey);

			LOG.info("Interrupt job {} successfully.", jobKey.toString());
		}
		catch (SchedulerException e)
		{
			throw new QuartzException(e.getMessage(), e);
		}
	}

	@PostConstruct
	public void initialize() throws Exception
	{
		StdSchedulerFactory schedulerFactory = QuartzFactory.create(threadPoolProps);
		scheduler = schedulerFactory.getScheduler();
		scheduler.start();

		onceJobScheduler = new OnceJobExecutorImpl(scheduler, applicationContext);
		intervalJobScheduler = new IntervalJobExecutorImpl(scheduler, applicationContext);
		cronJobExecutor = new CronJobExecutorImpl(scheduler, applicationContext);

		LOG.info("Startup quartz scheduler successfully.");
	}

	@PreDestroy
	public void destroy()
	{
		if (null != scheduler)
		{
			try
			{
				scheduler.shutdown(true);

				LOG.info("Shutdown quartz successfully.");
			}
			catch (SchedulerException e)
			{
				LOG.error("Shutdown quartz failed.", e);
			}
		}
	}

}
