package com.mizhousoft.boot.quartz;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 间隔任务执行器
 *
 * @version
 */
public interface IntervalJobExecutor
{
	/**
	 * 调度任务
	 * 
	 * @param initialDelayMs
	 * @param interval
	 * @param timeUnit
	 * @param context
	 * @throws QuartzException
	 */
	void scheduleIntervalJob(long initialDelayMs, int interval, TimeUnit timeUnit, JobContext context) throws QuartzException;

	/**
	 * 调度任务
	 * 
	 * @param interval
	 * @param timeUnit
	 * @param dataMap
	 * @param context
	 * @throws QuartzException
	 */
	void scheduleIntervalJob(int interval, TimeUnit timeUnit, Map<String, Object> dataMap, JobContext context) throws QuartzException;

	/**
	 * 调度任务
	 * 
	 * @param initialDelayMs
	 * @param interval
	 * @param timeUnit
	 * @param dataMap
	 * @param context
	 * @throws QuartzException
	 */
	void scheduleIntervalJob(long initialDelayMs, int interval, TimeUnit timeUnit, Map<String, Object> dataMap, JobContext context)
	        throws QuartzException;
}
