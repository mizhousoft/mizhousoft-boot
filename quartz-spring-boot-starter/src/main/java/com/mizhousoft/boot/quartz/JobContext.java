package com.mizhousoft.boot.quartz;

/**
 * Job Context
 *
 * @version
 */
public class JobContext
{
	// Job名称
	private String jobName;

	// Job组名
	private String jobGroup;

	// Trigger名称
	private String triggerName;

	// Trigger组名
	private String triggerGroup;

	// Job类
	private Class<? extends JobExecution> JobClass;

	/**
	 * 获取jobName
	 * 
	 * @return
	 */
	public String getJobName()
	{
		return jobName;
	}

	/**
	 * 设置jobName
	 * 
	 * @param jobName
	 */
	public void setJobName(String jobName)
	{
		this.jobName = jobName;
	}

	/**
	 * 获取jobGroup
	 * 
	 * @return
	 */
	public String getJobGroup()
	{
		return jobGroup;
	}

	/**
	 * 设置jobGroup
	 * 
	 * @param jobGroup
	 */
	public void setJobGroup(String jobGroup)
	{
		this.jobGroup = jobGroup;
	}

	/**
	 * 获取triggerName
	 * 
	 * @return
	 */
	public String getTriggerName()
	{
		return triggerName;
	}

	/**
	 * 设置triggerName
	 * 
	 * @param triggerName
	 */
	public void setTriggerName(String triggerName)
	{
		this.triggerName = triggerName;
	}

	/**
	 * 获取triggerGroup
	 * 
	 * @return
	 */
	public String getTriggerGroup()
	{
		return triggerGroup;
	}

	/**
	 * 设置triggerGroup
	 * 
	 * @param triggerGroup
	 */
	public void setTriggerGroup(String triggerGroup)
	{
		this.triggerGroup = triggerGroup;
	}

	/**
	 * 获取JobClass
	 * 
	 * @return
	 */
	public Class<? extends JobExecution> getJobClass()
	{
		return JobClass;
	}

	/**
	 * 设置JobClass
	 * 
	 * @param jobClass
	 */
	public void setJobClass(Class<? extends JobExecution> jobClass)
	{
		JobClass = jobClass;
	}
}
