package com.mizhousoft.boot.authentication.impl;

import java.util.Collection;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;

import com.mizhousoft.boot.authentication.AccountDetails;
import com.mizhousoft.boot.authentication.AccountSessionService;
import com.mizhousoft.boot.authentication.util.ShiroUtils;

/**
 * 账户session服务
 *
 * @version
 */
public class AccountSessionServiceImpl implements AccountSessionService
{
	private SessionDAO sessionDAO;

	/**
	 * 构造函数
	 *
	 * @param sessionDAO
	 */
	public AccountSessionServiceImpl(SessionDAO sessionDAO)
	{
		super();
		this.sessionDAO = sessionDAO;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AccountDetails logoffAccount(long accountId)
	{
		Collection<Session> activeSessions = sessionDAO.getActiveSessions();
		for (Session session : activeSessions)
		{
			AccountDetails accountDetails = getAccountDetails(session);
			if (null != accountDetails)
			{
				if (accountDetails.getAccountId() == accountId)
				{
					sessionDAO.delete(session);

					return accountDetails;
				}
			}
		}

		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void logoffOtherAccounts(long excludeAccountId)
	{
		Collection<Session> activeSessions = sessionDAO.getActiveSessions();
		for (Session session : activeSessions)
		{
			AccountDetails accountDetails = getAccountDetails(session);
			if (null != accountDetails)
			{
				if (accountDetails.getAccountId() != excludeAccountId)
				{
					sessionDAO.delete(session);
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AccountDetails getAccountDetails(Session session)
	{
		return ShiroUtils.getAccountDetails(session);
	}
}
