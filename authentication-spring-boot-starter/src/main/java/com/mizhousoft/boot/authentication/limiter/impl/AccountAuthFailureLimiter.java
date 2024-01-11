package com.mizhousoft.boot.authentication.limiter.impl;

import com.mizhousoft.boot.authentication.exception.AccountLockedException;
import com.mizhousoft.boot.authentication.exception.AuthenticationException;
import com.mizhousoft.boot.authentication.limiter.FailureCounter;

/**
 * 认证失败限制器
 *
 * @version
 */
public class AccountAuthFailureLimiter extends AbstractAuthFailureLimiter
{
	/**
	 * 构造函数
	 *
	 * @param limitNumber
	 */
	public AccountAuthFailureLimiter(int limitNumber)
	{
		super(limitNumber);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void tryAcquire(String entity) throws AuthenticationException
	{
		FailureCounter faildCounter = counterCache.getIfPresent(entity);
		if (null != faildCounter)
		{
			int value = faildCounter.getFailedCount();
			if (value >= limitNumber)
			{
				throw new AccountLockedException(entity + " is locked.");
			}
		}
	}
}
