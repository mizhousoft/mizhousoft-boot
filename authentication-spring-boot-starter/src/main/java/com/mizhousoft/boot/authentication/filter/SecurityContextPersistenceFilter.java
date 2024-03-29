package com.mizhousoft.boot.authentication.filter;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.servlet.OncePerRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mizhousoft.boot.authentication.AccountAuthentication;
import com.mizhousoft.boot.authentication.AccountDetails;
import com.mizhousoft.boot.authentication.context.SecurityContextHolder;
import com.mizhousoft.boot.authentication.context.SecurityContextImpl;
import com.mizhousoft.boot.authentication.util.ShiroUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

/**
 * SecurityContext持久化过滤器
 *
 * @version
 */
public class SecurityContextPersistenceFilter extends OncePerRequestFilter
{
	private static final Logger LOG = LoggerFactory.getLogger(SecurityContextPersistenceFilter.class);

	// 登录URL
	private String loginUrl;

	// 校验访问IP地址
	private boolean checkAccessIpAddr = true;

	/**
	 * 过滤
	 * 
	 * @param req
	 * @param res
	 * @param chain
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected void doFilterInternal(ServletRequest req, ServletResponse res, FilterChain chain) throws ServletException, IOException
	{
		Subject subject = SecurityUtils.getSubject();

		// 未认证不做处理
		if (!subject.isAuthenticated())
		{
			// 再次退出
			ShiroUtils.logout(req, res, subject, loginUrl);
			return;
		}

		Object principal = subject.getPrincipal();
		AccountDetails accountDetails = null;
		if (principal instanceof AccountDetails)
		{
			accountDetails = (AccountDetails) principal;
		}
		else
		{
			// 再次退出
			ShiroUtils.logout(req, res, subject, loginUrl);
			return;
		}

		String remoteIPAddress = com.mizhousoft.commons.web.util.WebUtils.getRemoteIPAddress(req);
		if (StringUtils.isBlank(remoteIPAddress))
		{
			remoteIPAddress = subject.getSession().getHost();
		}

		if (checkAccessIpAddr && !StringUtils.equals(accountDetails.getLoginIpAddr(), remoteIPAddress))
		{
			LOG.error("Account login ip address is {}, access ip address is {} now, force to logout.", accountDetails.getLoginIpAddr(),
			        remoteIPAddress);
			ShiroUtils.logout(req, res, subject, loginUrl);

			return;
		}

		AccountAuthentication authentication = new AccountAuthentication(accountDetails);
		authentication.setAccessIPAddress(remoteIPAddress);
		authentication.setAuthenticated(subject.isAuthenticated());

		SecurityContextImpl securityContext = new SecurityContextImpl();
		securityContext.setAuthentication(authentication);

		try
		{
			SecurityContextHolder.setContext(securityContext);

			chain.doFilter(req, res);
		}
		finally
		{
			SecurityContextHolder.clearContext();
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

	/**
	 * 设置checkAccessIpAddr
	 * 
	 * @param checkAccessIpAddr
	 */
	public void setCheckAccessIpAddr(boolean checkAccessIpAddr)
	{
		this.checkAccessIpAddr = checkAccessIpAddr;
	}
}
