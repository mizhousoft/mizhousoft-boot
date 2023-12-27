package com.mizhousoft.boot.authentication.filter.authc;

import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;

import com.mizhousoft.boot.authentication.Authentication;
import com.mizhousoft.boot.authentication.context.SecurityContextHolder;
import com.mizhousoft.boot.authentication.event.AccountLogoutEvent;
import com.mizhousoft.boot.authentication.util.BMCWebUtils;
import com.mizhousoft.boot.authentication.util.ResponseBuilder;
import com.mizhousoft.commons.web.util.CookieUtils;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 定制注销过滤器
 *
 * @version
 */
public class CustLogoutFilter extends LogoutFilter
{
	private static final Logger LOG = LoggerFactory.getLogger(CustLogoutFilter.class);

	/**
	 * 事件发布器
	 */
	private ApplicationEventPublisher eventPublisher;

	/**
	 * 构造函数
	 *
	 * @param eventPublisher
	 */
	public CustLogoutFilter(ApplicationEventPublisher eventPublisher)
	{
		super();
		this.eventPublisher = eventPublisher;
	}

	/**
	 * 跳转
	 * 
	 * @param request
	 * @param response
	 * @param redirectUrl
	 * @throws Exception
	 */
	protected void issueRedirect(ServletRequest request, ServletResponse response, String redirectUrl) throws Exception
	{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (null != authentication)
		{
			try
			{
				AccountLogoutEvent event = new AccountLogoutEvent(authentication);
				eventPublisher.publishEvent(event);
			}
			catch (Throwable e)
			{
				LOG.error("Publish AccountLogoutEvent failed.", e);
			}
		}

		HttpServletResponse httpResp = WebUtils.toHttp(response);
		CookieUtils.removeAll(WebUtils.toHttp(request), httpResp);

		if (BMCWebUtils.isJSONRequest(request))
		{
			httpResp.setStatus(HttpServletResponse.SC_OK);

			String respBody = ResponseBuilder.buildSucceed(httpResp.encodeRedirectURL(redirectUrl));
			httpResp.getWriter().write(respBody);
		}
		else
		{
			super.issueRedirect(request, response, redirectUrl);
		}
	}
}
