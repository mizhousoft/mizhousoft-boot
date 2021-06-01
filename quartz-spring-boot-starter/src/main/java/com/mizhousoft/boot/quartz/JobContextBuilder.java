package com.mizhousoft.boot.quartz;

import org.apache.commons.lang3.StringUtils;

/**
 * JobContext构建器
 *
 * @version
 */
public class JobContextBuilder
{
	private JobContext jobContext;

	protected JobContextBuilder()
	{

	}

	public static JobContextBuilder newJobContext(Class<? extends JobExecution> jobClass)
	{
		JobContext jobContext = new JobContext();
		jobContext.setJobClass(jobClass);

		JobContextBuilder builder = new JobContextBuilder();
		builder.jobContext = jobContext;

		return builder;
	}

	public JobContextBuilder withJobIdentity(String jobName)
	{
		this.jobContext.setJobName(jobName);

		return this;
	}

	public JobContextBuilder withJobIdentity(String jobName, String jobGroup)
	{
		this.jobContext.setJobName(jobName);
		this.jobContext.setJobGroup(jobGroup);

		return this;
	}

	public JobContextBuilder withTriggerIdentity(String triggerName)
	{
		this.jobContext.setTriggerName(triggerName);

		return this;
	}

	public JobContextBuilder withTriggerIdentity(String triggerName, String triggerGroup)
	{
		this.jobContext.setTriggerName(triggerName);
		this.jobContext.setTriggerGroup(triggerGroup);

		return this;
	}

	public JobContext build()
	{
		if (StringUtils.isBlank(jobContext.getJobGroup()))
		{
			jobContext.setJobGroup(SchedulerConstants.DEFAULT_JOB_GROUP_NAME);
		}

		if (StringUtils.isBlank(jobContext.getTriggerName()))
		{
			jobContext.setTriggerName(jobContext.getJobName());
		}

		if (StringUtils.isBlank(jobContext.getTriggerGroup()))
		{
			jobContext.setTriggerGroup(SchedulerConstants.DEFAULT_JOB_GROUP_NAME);
		}

		return jobContext;
	}
}
