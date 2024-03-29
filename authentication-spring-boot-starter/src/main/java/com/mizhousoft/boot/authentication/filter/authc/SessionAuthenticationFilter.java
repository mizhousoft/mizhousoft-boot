package com.mizhousoft.boot.authentication.filter.authc;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.servlet.OncePerRequestFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mizhousoft.boot.authentication.SecurityConstants;
import com.mizhousoft.boot.authentication.util.ShiroUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

/**
 * Session认证过滤器
 *
 * @version
 */
public class SessionAuthenticationFilter extends OncePerRequestFilter
{
	private static final Logger LOG = LoggerFactory.getLogger(SessionAuthenticationFilter.class);

	// 登录URL
	private String loginUrl;

	// 是否校验Host
	private boolean verifyHost = true;

	/**
	 * 过滤
	 * 
	 * @param request
	 * @param response
	 * @param chain
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected void doFilterInternal(ServletRequest request, ServletResponse response, FilterChain chain)
	        throws ServletException, IOException
	{
		Subject subject = SecurityUtils.getSubject();

		// 判断Subject是否认证成功
		boolean isAuthcSucceed = isSubjectAuthcSucceed(subject, request);
		if (!isAuthcSucceed)
		{
			ShiroUtils.logout(request, response, subject, loginUrl);
		}
		else
		{
			chain.doFilter(request, response);
		}
	}

	/**
	 * 判断Session是否认证成功
	 * 
	 * @param subject
	 * @param request
	 * @return
	 */
	private boolean isSubjectAuthcSucceed(Subject subject, ServletRequest request)
	{
		if (!subject.isAuthenticated())
		{
			LOG.error("Subject is not authenticated.");
			return false;
		}

		// 登录时的数据
		String userAgent = (String) subject.getSession().getAttribute(SecurityConstants.USER_AGENT_HEADER);
		if (StringUtils.isBlank(userAgent))
		{
			LOG.error("Subject UserAgent is null.");
			return false;
		}

		String host = (String) subject.getSession().getAttribute(SecurityConstants.HOST);
		if (StringUtils.isBlank(host))
		{
			LOG.error("Subject host is null.");
			return false;
		}

		// 当前请求的数据
		String reqUserAgent = WebUtils.toHttp(request).getHeader(SecurityConstants.USER_AGENT_HEADER);
		if (StringUtils.isBlank(reqUserAgent))
		{
			LOG.error("Request UserAgent is null.");
			return false;
		}

		if (!userAgent.equals(reqUserAgent))
		{
			LOG.error("Subject UserAgent is not the same with request UserAgent.");
			return false;
		}

		if (verifyHost)
		{
			String reqHost = com.mizhousoft.commons.web.util.WebUtils.getRemoteIPAddress(request);
			if (StringUtils.isBlank(reqHost))
			{
				LOG.error("Request host is null.");
				return false;
			}

			if (!host.equals(reqHost))
			{
				LOG.error("Subject host is not the same with request host.");
				return false;
			}
		}

		return true;
	}

	/**
	 * 设置loginUrl
	 * 
	 * @param loginUrl
	 */
	public void setLoginUrl(String loginUrl)
	{
		this.loginUrl = loginUrl;
	}

	/**
	 * 设置verifyHost
	 * 
	 * @param verifyHost
	 */
	public void setVerifyHost(boolean verifyHost)
	{
		this.verifyHost = verifyHost;
	}
}
