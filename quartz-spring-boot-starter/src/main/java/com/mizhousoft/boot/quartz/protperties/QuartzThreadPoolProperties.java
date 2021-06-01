package com.mizhousoft.boot.quartz.protperties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 配置
 *
 * @version
 */
@Component
@ConfigurationProperties(prefix = "quartz.threadpool")
public class QuartzThreadPoolProperties
{
	// 线程数量
	private int threadCount = 20;

	// 线程级别
	private int threadPriority = Thread.NORM_PRIORITY;

	/**
	 * 获取threadCount
	 * 
	 * @return
	 */
	public int getThreadCount()
	{
		return threadCount;
	}

	/**
	 * 设置threadCount
	 * 
	 * @param threadCount
	 */
	public void setThreadCount(int threadCount)
	{
		this.threadCount = threadCount;
	}

	/**
	 * 获取threadPriority
	 * 
	 * @return
	 */
	public int getThreadPriority()
	{
		return threadPriority;
	}

	/**
	 * 设置threadPriority
	 * 
	 * @param threadPriority
	 */
	public void setThreadPriority(int threadPriority)
	{
		this.threadPriority = threadPriority;
	}
}
