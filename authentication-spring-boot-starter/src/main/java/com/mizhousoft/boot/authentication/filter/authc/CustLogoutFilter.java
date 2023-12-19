package com.mizhousoft.boot.authentication.filter.authc;

import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.apache.shiro.web.util.WebUtils;

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
