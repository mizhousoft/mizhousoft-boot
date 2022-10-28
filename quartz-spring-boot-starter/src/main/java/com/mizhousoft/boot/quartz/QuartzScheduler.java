package com.mizhousoft.boot.quartz;

/**
 * Quartz调度服务
 *
 * @version
 */
public interface QuartzScheduler extends OnceJobExecutor, IntervalJobExecutor, CronJobExecutor
{
	/**
	 * 检查是否存在
	 * 
	 * @param jobName
	 * @return
	 * @throws QuartzException
	 */
	boolean checkExists(String jobName) throws QuartzException;

	/**
	 * 检查是否存在
	 * 
	 * @param jobName
	 * @param jobGroup
	 * @return
	 * @throws QuartzException
	 */
	boolean checkExists(String jobName, String jobGroup) throws QuartzException;

	/**
	 * 删除任务
	 * 
	 * @param jobName
	 * @throws QuartzException
	 */
	void deleteJob(String jobName) throws QuartzException;

	/**
	 * 删除任务
	 * 
	 * @param jobName
	 * @param jobGroup
	 * @throws QuartzException
	 */
	void deleteJob(String jobName, String jobGroup) throws QuartzException;

	/**
	 * 中断任务
	 * 
	 * @param jobName
	 * @throws QuartzException
	 */
	void interruptJob(String jobName) throws QuartzException;

	/**
	 * 中断任务
	 * 
	 * @param jobName
	 * @param jobGroup
	 * @throws QuartzException
	 */
	void interruptJob(String jobName, String jobGroup) throws QuartzException;
}
