package com.mizhousoft.boot.quartz.executor;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.mizhousoft.boot.quartz.IntervalJobExecutor;
import com.mizhousoft.boot.quartz.JobContext;
import com.mizhousoft.boot.quartz.JobExecution;
import com.mizhousoft.boot.quartz.QuartzException;
import com.mizhousoft.boot.quartz.SchedulerConstants;
import com.mizhousoft.boot.quartz.factory.SimpleScheduleBuilderFactory;

/**
 * 间隔任务执行器
 *
 * @version
 */
public class IntervalJobExecutorImpl extends AbstractJobExecutor implements IntervalJobExecutor
{
	private static final Logger LOG = LoggerFactory.getLogger(IntervalJobExecutorImpl.class);

	/**
	 * 构造函数
	 *
	 * @param scheduler
	 * @param applicationContext
	 */
	public IntervalJobExecutorImpl(Scheduler scheduler, ApplicationContext applicationContext)
	{
		super(scheduler, applicationContext);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void scheduleIntervalJob(long initialDelayMs, int interval, TimeUnit timeUnit, JobContext context) throws QuartzException
	{
		scheduleIntervalJob(initialDelayMs, interval, timeUnit, null, context);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void scheduleIntervalJob(int interval, TimeUnit timeUnit, Map<String, Object> dataMap, JobContext context) throws QuartzException
	{
		scheduleIntervalJob(0L, interval, timeUnit, dataMap, context);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void scheduleIntervalJob(long initialDelayMs, int interval, TimeUnit timeUnit, Map<String, Object> dataMap, JobContext context)
	        throws QuartzException
	{
		try
		{
			String jobName = context.getJobName();
			String jobGroup = context.getJobGroup();
			Class<? extends JobExecution> jobClass = context.getJobClass();

			JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroup).build();

			if (dataMap == null)
			{
				dataMap = new HashMap<>(1);
			}

			dataMap.put(SchedulerConstants.APPLICATION_CONTEXT, applicationContext);
			jobDetail.getJobDataMap().putAll(dataMap);

			long tmpTime = System.currentTimeMillis() + initialDelayMs; // 延迟启动任务时间
			Date statTime = new Date(tmpTime); // 启动时间

			String triggerName = context.getTriggerName();
			String triggerGroup = context.getTriggerGroup();

			SimpleTrigger simpleTrigger = TriggerBuilder.newTrigger().withIdentity(triggerName, triggerGroup)
			        .withSchedule(SimpleScheduleBuilderFactory.create(interval, timeUnit)).startAt(statTime).build();

			scheduler.scheduleJob(jobDetail, simpleTrigger);

			LOG.info("Schedule interval job {} successfully, initial delay millis is {}, interval is {}, timeunit is {}.",
			        jobDetail.getKey().toString(), initialDelayMs, interval, timeUnit.name());
		}
		catch (SchedulerException e)
		{
			throw new QuartzException(e.getMessage(), e);
		}
	}
}
