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
 * 密码过期检查过滤器
 *
 * @version
 */
public class PasswordExpiredCheckFilter extends OncePerRequestFilter
{
	private static final Logger LOG = LoggerFactory.getLogger(PasswordExpiredCheckFilter.class);

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

		boolean credentialsExpired = false;

		Object principal = subject.getPrincipal();
		if (principal instanceof AccountDetails)
		{
			credentialsExpired = ((AccountDetails) principal).isCredentialsExpired();
		}

		if (credentialsExpired)
		{
			LOG.error("Account password has expired, forced to exit the system.");

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
