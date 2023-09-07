package com.mizhousoft.boot.authentication.service;

import java.time.LocalDateTime;

import com.mizhousoft.boot.authentication.AccountDetails;

/**
 * 双因子认证服务
 *
 * @version
 */
public interface TwoFactorAuthenticationService
{
	/**
	 * 决策是否内部认证通过
	 * 
	 * @param accountDetails
	 * @param lastAccessIpAddr
	 * @param lastAccessTime
	 * @return
	 */
	boolean determineInternalAuthcPass(AccountDetails accountDetails, String lastAccessIpAddr, LocalDateTime lastAccessTime);
}
