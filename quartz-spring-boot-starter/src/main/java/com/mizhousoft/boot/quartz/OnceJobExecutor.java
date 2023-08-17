package com.mizhousoft.boot.quartz;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 一次性任务执行器
 *
 * @version
 */
public interface OnceJobExecutor
{
	/**
	 * 调度一次性任务
	 * 
	 * @param context
	 * @throws QuartzException
	 */
	void scheduleOnceJob(JobContext context) throws QuartzException;

	/**
	 * 调度一次性任务
	 * 
	 * @param dataMap
	 * @param context
	 * @throws QuartzException
	 */
	void scheduleOnceJob(Map<String, Object> dataMap, JobContext context) throws QuartzException;

	/**
	 * 调度一次性任务
	 * 
	 * @param startDate
	 * @param dataMap
	 * @param context
	 * @throws QuartzException
	 */
	void scheduleOnceJob(LocalDateTime startDate, Map<String, Object> dataMap, JobContext context) throws QuartzException;
}
