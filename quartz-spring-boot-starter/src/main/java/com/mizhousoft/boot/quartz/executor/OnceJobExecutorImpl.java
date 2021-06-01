package com.mizhousoft.boot.quartz.executor;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.mizhousoft.boot.quartz.JobContext;
import com.mizhousoft.boot.quartz.OnceJobExecutor;
import com.mizhousoft.boot.quartz.QuartzException;
import com.mizhousoft.boot.quartz.SchedulerConstants;

/**
 * 一次性任务执行器
 *
 * @version
 */
public class OnceJobExecutorImpl extends AbstractJobExecutor implements OnceJobExecutor
{
	private static final Logger LOG = LoggerFactory.getLogger(OnceJobExecutorImpl.class);

	/**
	 * 构造函数
	 *
	 * @param scheduler
	 * @param applicationContext
	 */
	public OnceJobExecutorImpl(Scheduler scheduler, ApplicationContext applicationContext)
	{
		super(scheduler, applicationContext);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void scheduleOnceJob(JobContext context) throws QuartzException
	{
		scheduleOnceJob(new Date(), null, context);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void scheduleOnceJob(Map<String, Object> dataMap, JobContext context) throws QuartzException
	{
		scheduleOnceJob(new Date(), dataMap, context);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void scheduleOnceJob(Date startDate, Map<String, Object> dataMap, JobContext context) throws QuartzException
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

			Trigger trigger = TriggerBuilder.newTrigger().startAt(startDate).build();

			scheduler.scheduleJob(jobDetail, trigger);

			LOG.info("Schedule once job {} successfully, start at {}.", jobDetail.getKey().toString(),
			        DateFormatUtils.format(startDate, "yyyy-MM-dd HH:mm:ss"));
		}
		catch (SchedulerException e)
		{
			throw new QuartzException(e.getMessage(), e);
		}
	}
}
