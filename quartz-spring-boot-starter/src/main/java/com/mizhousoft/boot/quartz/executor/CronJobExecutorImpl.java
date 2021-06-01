package com.mizhousoft.boot.quartz.executor;

import java.util.HashMap;
import java.util.Map;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.mizhousoft.boot.quartz.CronJobExecutor;
import com.mizhousoft.boot.quartz.JobContext;
import com.mizhousoft.boot.quartz.QuartzException;
import com.mizhousoft.boot.quartz.SchedulerConstants;

/**
 * 表达式任务执行器
 *
 * @version
 */
public class CronJobExecutorImpl extends AbstractJobExecutor implements CronJobExecutor
{
	private static final Logger LOG = LoggerFactory.getLogger(CronJobExecutorImpl.class);

	/**
	 * 构造函数
	 *
	 * @param scheduler
	 * @param applicationContext
	 */
	public CronJobExecutorImpl(Scheduler scheduler, ApplicationContext applicationContext)
	{
		super(scheduler, applicationContext);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void scheduleCronJob(String cronExpression, JobContext context) throws QuartzException
	{
		scheduleCronJob(cronExpression, null, context);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void scheduleCronJob(String cronExpression, Map<String, Object> dataMap, JobContext context) throws QuartzException
	{
		try
		{
			JobDetail jobDetail = JobBuilder.newJob(context.getJobClass()).withIdentity(context.getJobName(), context.getJobGroup())
			        .build();

			if (dataMap == null)
			{
				dataMap = new HashMap<>(1);
			}

			dataMap.put(SchedulerConstants.APPLICATION_CONTEXT, applicationContext);
			jobDetail.getJobDataMap().putAll(dataMap);

			CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(context.getTriggerName(), context.getTriggerGroup())
			        .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)).build();

			scheduler.scheduleJob(jobDetail, cronTrigger);

			LOG.info("Schedule cron job {} successfully, cron expression is {}.", jobDetail.getKey().toString(), cronExpression);
		}
		catch (SchedulerException e)
		{
			throw new QuartzException(e.getMessage(), e);
		}
	}
}
