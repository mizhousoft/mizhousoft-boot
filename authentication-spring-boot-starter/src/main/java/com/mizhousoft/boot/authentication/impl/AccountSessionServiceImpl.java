package com.mizhousoft.boot.authentication.impl;

import java.util.Collection;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mizhousoft.boot.authentication.AccountDetails;
import com.mizhousoft.boot.authentication.AccountSessionService;

/**
 * 账户session服务
 *
 * @version
 */
public class AccountSessionServiceImpl implements AccountSessionService
{
	private static final Logger LOG = LoggerFactory.getLogger(AccountSessionServiceImpl.class);

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
		Object simplePrincipalCollection = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
		if (null == simplePrincipalCollection)
		{
			return null;
		}

		if (simplePrincipalCollection instanceof SimplePrincipalCollection)
		{
			Object primaryPrincipal = ((SimplePrincipalCollection) simplePrincipalCollection).getPrimaryPrincipal();
			if (primaryPrincipal instanceof AccountDetails)
			{
				return ((AccountDetails) primaryPrincipal);
			}
			else
			{
				LOG.error("Primary Principal object is not AccountDetails, object is {}.", primaryPrincipal.toString());
			}
		}
		else
		{
			LOG.error("Session PRINCIPALS_SESSION_KEY object is not SimplePrincipalCollection, object is {}.",
			        simplePrincipalCollection.toString());
		}

		return null;
	}
}
