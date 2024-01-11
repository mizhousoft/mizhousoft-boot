package com.mizhousoft.boot.authentication.limiter;

import java.time.LocalDateTime;

/**
 * 失败计数器
 *
 * @version
 */
public class FailureCounter
{
	// 失败次数
	private int failedCount;

	// 失败时间
	private LocalDateTime failedTime;

	/**
	 * 获取failedCount
	 * 
	 * @return
	 */
	public int getFailedCount()
	{
		return failedCount;
	}

	/**
	 * 设置failedCount
	 * 
	 * @param failedCount
	 */
	public void setFailedCount(int failedCount)
	{
		this.failedCount = failedCount;
	}

	/**
	 * 获取failedTime
	 * 
	 * @return
	 */
	public LocalDateTime getFailedTime()
	{
		return failedTime;
	}

	/**
	 * 设置failedTime
	 * 
	 * @param failedTime
	 */
	public void setFailedTime(LocalDateTime failedTime)
	{
		this.failedTime = failedTime;
	}
}
