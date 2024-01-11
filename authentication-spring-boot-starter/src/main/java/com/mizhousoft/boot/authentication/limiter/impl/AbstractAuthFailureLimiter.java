package com.mizhousoft.boot.authentication.limiter.impl;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.mizhousoft.boot.authentication.limiter.AuthFailureLimiter;
import com.mizhousoft.boot.authentication.limiter.FailureCounter;

/**
 * 认证失败限制器
 *
 * @version
 */
public abstract class AbstractAuthFailureLimiter implements AuthFailureLimiter
{
	/**
	 * 计数器集合
	 */
	protected Cache<String, FailureCounter> counterCache = Caffeine.newBuilder().expireAfterWrite(5, TimeUnit.MINUTES).build();

	/**
	 * 限制次数
	 */
	protected int limitNumber = 0;

	/**
	 * 构造函数
	 *
	 * @param limitNumber
	 */
	public AbstractAuthFailureLimiter(int limitNumber)
	{
		super();
		this.limitNumber = limitNumber;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int authcFailure(String entity)
	{
		FailureCounter faildCounter = counterCache.getIfPresent(entity);
		if (null == faildCounter)
		{
			faildCounter = new FailureCounter();
			faildCounter.setFailedCount(1);
			faildCounter.setFailedTime(LocalDateTime.now());

			counterCache.put(entity, faildCounter);
		}
		else
		{
			faildCounter.setFailedCount(faildCounter.getFailedCount() + 1);
		}

		int leaveNum = limitNumber - faildCounter.getFailedCount();
		if (leaveNum < 0)
		{
			leaveNum = 0;
		}

		return leaveNum;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clear(String entity)
	{
		counterCache.invalidate(entity);
	}
}
