package com.mizhousoft.boot.authentication;

import org.apache.shiro.session.Session;

/**
 * 帐号session服务
 *
 * @version
 */
public interface AccountSessionService
{
	/**
	 * 注销帐号
	 * 
	 * @param accountId
	 */
	AccountDetails logoffAccount(long accountId);

	/**
	 * 注销其他帐号
	 * 
	 * @param excludeAccountId
	 */
	void logoffOtherAccounts(long excludeAccountId);

	/**
	 * 获取详情
	 * 
	 * @param session
	 * @return
	 */
	AccountDetails getAccountDetails(Session session);
}
