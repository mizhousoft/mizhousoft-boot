package com.mizhousoft.boot.quartz.executor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
import com.mizhousoft.commons.lang.LocalDateTimeUtils;

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
		scheduleOnceJob(LocalDateTime.now(), null, context);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void scheduleOnceJob(Map<String, Object> dataMap, JobContext context) throws QuartzException
	{
		scheduleOnceJob(LocalDateTime.now(), dataMap, context);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void scheduleOnceJob(LocalDateTime startDate, Map<String, Object> dataMap, JobContext context) throws QuartzException
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

			Date date = LocalDateTimeUtils.toDate(startDate);
			Trigger trigger = TriggerBuilder.newTrigger().startAt(date).build();

			scheduler.scheduleJob(jobDetail, trigger);

			LOG.info("Schedule once job {} successfully, start at {}.", jobDetail.getKey().toString(),
			        LocalDateTimeUtils.formatYmdhms(startDate));
		}
		catch (SchedulerException e)
		{
			throw new QuartzException(e.getMessage(), e);
		}
	}
}
