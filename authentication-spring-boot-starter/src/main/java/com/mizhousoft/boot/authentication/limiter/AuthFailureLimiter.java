package com.mizhousoft.boot.authentication.limiter;

import com.mizhousoft.boot.authentication.exception.AuthenticationException;

/**
 * 认证失败限制器
 *
 * @version
 */
public interface AuthFailureLimiter
{
	/**
	 * 获取许可
	 * 
	 * @param entity
	 * @throws AuthenticationException
	 */
	void tryAcquire(String entity) throws AuthenticationException;

	/**
	 * 认证失败
	 * 
	 * @param entity
	 */
	int authcFailure(String entity);

	/**
	 * 清除
	 * 
	 * @param entity
	 */
	void clear(String entity);
}
