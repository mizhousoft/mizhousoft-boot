package com.mizhousoft.boot.authentication.service;

import com.mizhousoft.boot.authentication.AccountDetails;
import com.mizhousoft.boot.authentication.exception.AuthenticationException;
import com.mizhousoft.boot.authentication.exception.BadCredentialsException;

/**
 * 帐号认证服务
 *
 * @version
 */
public interface AccountAuthcService
{
	/**
	 * 认证帐号
	 * 
	 * @param account
	 * @param passwd
	 * @param host
	 * @return
	 * @throws AuthenticationException
	 */
	AccountDetails authenticate(String account, char[] passwd, String host) throws AuthenticationException;

	/**
	 * 认证帐号
	 * 
	 * @param account
	 * @param code
	 * @param host
	 * @return
	 * @throws AuthenticationException
	 */
	default AccountDetails authenticate(String account, String code, String host) throws AuthenticationException
	{
		throw new BadCredentialsException("Subclass implementation.");
	}
}
