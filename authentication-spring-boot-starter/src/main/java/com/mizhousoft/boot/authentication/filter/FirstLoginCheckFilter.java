package com.mizhousoft.boot.authentication.filter;

import java.io.IOException;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.servlet.OncePerRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mizhousoft.boot.authentication.AccountDetails;
import com.mizhousoft.boot.authentication.util.ShiroUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

/**
 * 第一次登录校验过滤器
 *
 * @version
 */
public class FirstLoginCheckFilter extends OncePerRequestFilter
{
	private static final Logger LOG = LoggerFactory.getLogger(FirstLoginCheckFilter.class);

	// 登录URL
	private String loginUrl;

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

		boolean isFirstLogin = false;
		AccountDetails accountDetails = null;

		Object principal = subject.getPrincipal();
		if (principal instanceof AccountDetails)
		{
			accountDetails = ((AccountDetails) principal);
			isFirstLogin = accountDetails.isFirstLogin();
		}

		if (isFirstLogin)
		{
			LOG.error("The first time {} login unmodified password, force to logout.", accountDetails.getAccountName());

			ShiroUtils.logout(request, response, subject, loginUrl);
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
