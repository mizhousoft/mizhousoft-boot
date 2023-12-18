package com.mizhousoft.boot.authentication.filter;

import java.io.IOException;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.servlet.OncePerRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mizhousoft.boot.authentication.AccountDetails;
import com.mizhousoft.boot.authentication.Authentication;
import com.mizhousoft.boot.authentication.SecurityConstants;
import com.mizhousoft.boot.authentication.context.SecurityContextHolder;
import com.mizhousoft.boot.authentication.util.ShiroUtils;
import com.mizhousoft.commons.web.util.WebUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

/**
 * RefererFilter
 *
 */
public class CsrfFilter extends OncePerRequestFilter
{
	private static final Logger LOG = LoggerFactory.getLogger(CsrfFilter.class);

	// 登录URL
	private String loginUrl;

	// 排除路径
	private List<String> excludePaths;

	/**
	 * 构造函数
	 *
	 * @param loginUrl
	 * @param excludePaths
	 */
	public CsrfFilter(String loginUrl, List<String> excludePaths)
	{
		super();
		this.loginUrl = loginUrl;
		this.excludePaths = excludePaths;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doFilterInternal(ServletRequest req, ServletResponse res, FilterChain chain) throws ServletException, IOException
	{
		HttpServletRequest httpRequest = WebUtils.getHttpRequest(req);

		String requestPath = WebUtils.getPathWithinApplication(httpRequest);
		if (excludePaths.contains(requestPath))
		{
			chain.doFilter(req, res);

			return;
		}

		Subject subject = SecurityUtils.getSubject();

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (null == authentication)
		{
			ShiroUtils.logout(httpRequest, res, subject, loginUrl);

			return;
		}

		AccountDetails accountDetails = authentication.getPrincipal();
		String csrfToken = accountDetails.getCsrfToken();

		String requestCsrfToken = httpRequest.getHeader(SecurityConstants.X_CSRF_TOKEN);
		if (csrfToken.equals(requestCsrfToken))
		{
			chain.doFilter(req, res);
		}
		else if (null == requestCsrfToken)
		{
			LOG.error("Request csrf token is null.");

			ShiroUtils.logout(httpRequest, res, subject, loginUrl);
		}
		else
		{
			LOG.error("Request csrf token is invalid.");

			ShiroUtils.logout(httpRequest, res, subject, loginUrl);
		}
	}
}
