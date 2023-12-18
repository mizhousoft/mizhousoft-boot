package com.mizhousoft.boot.authentication.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.shiro.session.Session;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mizhousoft.boot.authentication.AccountDetails;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Shiro Session工具类
 *
 * @version
 */
public abstract class ShiroUtils
{
	private static final Logger LOG = LoggerFactory.getLogger(ShiroUtils.class);

	/**
	 * 获取所有的AccountDetails
	 * 
	 * @param sessions
	 * @return
	 */
	public static List<AccountDetails> getAccountDetailsList(Collection<Session> sessions)
	{
		List<AccountDetails> accountDetailsList = new ArrayList<AccountDetails>(10);

		for (Session session : sessions)
		{
			AccountDetails accountDetails = getAccountDetails(session);
			if (null != accountDetails)
			{
				accountDetailsList.add(accountDetails);
			}
		}

		return accountDetailsList;
	}

	/**
	 * 根据session获取AccountDetails
	 * 
	 * @param session
	 * @return
	 */
	public static AccountDetails getAccountDetails(Session session)
	{
		Object simplePrincipalCollection = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
		if (simplePrincipalCollection instanceof SimplePrincipalCollection)
		{
			Object primaryPrincipal = ((SimplePrincipalCollection) simplePrincipalCollection).getPrimaryPrincipal();
			if (primaryPrincipal instanceof AccountDetails)
			{
				return ((AccountDetails) primaryPrincipal);
			}
			else
			{
				LOG.error("Primary Principal object is not AccountDetails.");
			}
		}
		else
		{
			LOG.error("Session PRINCIPALS_SESSION_KEY object is not SimplePrincipalCollection.");
		}

		return null;
	}

	public static void logout(ServletRequest req, ServletResponse res, Subject subject, String loginUrl) throws IOException
	{
		// 再次退出
		try
		{
			subject.logout();
		}
		catch (Throwable e)
		{
			LOG.error("Subject logout failed.", e);
		}

		if (BMCWebUtils.isJSONRequest(req))
		{
			HttpServletResponse httpResp = WebUtils.toHttp(res);
			httpResp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

			String respBody = ResponseBuilder.buildUnauthorized(httpResp.encodeRedirectURL(loginUrl), null);

			httpResp.getWriter().write(respBody);
		}
		else
		{
			WebUtils.issueRedirect(req, res, loginUrl);
		}
	}
}
