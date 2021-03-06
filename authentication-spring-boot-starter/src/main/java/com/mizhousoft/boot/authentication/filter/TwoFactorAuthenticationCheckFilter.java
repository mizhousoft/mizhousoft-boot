package com.mizhousoft.boot.authentication.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.servlet.OncePerRequestFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mizhousoft.boot.authentication.AccountDetails;
import com.mizhousoft.boot.authentication.util.BMCWebUtils;
import com.mizhousoft.boot.authentication.util.ResponseBuilder;

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

			// 再次退出
			try
			{
				subject.logout();
			}
			catch (Throwable e)
			{
				LOG.error("Subject logout failed.", e);
			}

			if (BMCWebUtils.isJSONRequest(request))
			{
				HttpServletResponse httpResp = WebUtils.toHttp(response);
				httpResp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

				String respBody = ResponseBuilder.buildUnauthorized(httpResp.encodeRedirectURL(loginUrl), null);

				httpResp.getWriter().write(respBody);
			}
			else
			{
				WebUtils.issueRedirect(request, response, loginUrl);
			}
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
