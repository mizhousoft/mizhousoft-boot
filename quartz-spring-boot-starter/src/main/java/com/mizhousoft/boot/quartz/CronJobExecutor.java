package com.mizhousoft.boot.quartz;

import java.util.Map;

/**
 * 表达式任务执行器
 *
 * @version
 */
public interface CronJobExecutor
{
	/**
	 * 调度任务
	 * 
	 * @param cronExpression
	 * @param context
	 * @throws QuartzException
	 */
	public void scheduleCronJob(String cronExpression, JobContext context) throws QuartzException;

	/**
	 * 调度任务
	 * 
	 * @param cronExpression
	 * @param dataMap
	 * @param context
	 * @throws QuartzException
	 */
	public void scheduleCronJob(String cronExpression, Map<String, Object> dataMap, JobContext context) throws QuartzException;
}
