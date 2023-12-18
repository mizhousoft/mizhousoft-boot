package com.mizhousoft.boot.authentication.filter;

import java.io.IOException;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.servlet.OncePerRequestFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mizhousoft.boot.authentication.AccountDetails;
import com.mizhousoft.boot.authentication.util.ShiroUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 双因子认证检查过滤器
 *
 * @version
 */
public class TwoFactorAuthenticationCheckFilter extends OncePerRequestFilter
{
	private static final Logger LOG = LoggerFactory.getLogger(TwoFactorAuthenticationCheckFilter.class);

	// 登录URL
	private String loginUrl;

	@Override
	protected void doFilterInternal(ServletRequest request, ServletResponse response, FilterChain chain)
	        throws ServletException, IOException
	{
		Subject subject = SecurityUtils.getSubject();

		boolean isTwoFactorAuthcPassed = false;
		AccountDetails accountDetails = null;

		Object principal = subject.getPrincipal();
		if (principal instanceof AccountDetails)
		{
			accountDetails = ((AccountDetails) principal);
			isTwoFactorAuthcPassed = accountDetails.isTwoFactorAuthcPassed();
		}

		if (!isTwoFactorAuthcPassed)
		{
			HttpServletRequest httpRequest = WebUtils.toHttp(request);
			String path = WebUtils.getPathWithinApplication(httpRequest);

			LOG.error("{} has not passed two-factor authentication, request path is {}, force to logout.", accountDetails.getAccountName(),
			        path);

			ShiroUtils.logout(httpRequest, response, subject, loginUrl);
		}
		else
		{
			chain.doFilter(request, response);
		}
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
}
