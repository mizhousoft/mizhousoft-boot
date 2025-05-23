package com.mizhousoft.boot.authentication.filter.authz;

import java.io.IOException;
import java.util.Set;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.RolesAuthorizationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mizhousoft.boot.authentication.AuthenticationService;
import com.mizhousoft.boot.authentication.util.BMCWebUtils;
import com.mizhousoft.boot.authentication.util.ResponseBuilder;
import com.mizhousoft.commons.lang.CollectionUtils;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 访问授权控制器
 * 
 * @version
 */
public class AccessAuthorizationFilter extends RolesAuthorizationFilter
{
	private static final Logger LOG = LoggerFactory.getLogger(AccessAuthorizationFilter.class);

	private AuthenticationService authenticationService;

	/**
	 * 是否允许访问
	 * 
	 * @param request
	 * @param response
	 * @param mappedValue
	 * @return
	 * @throws IOException
	 */
	public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws IOException
	{
		String requestPath = getPathWithinApplication(request);

		try
		{
			Set<String> roles = authenticationService.getRolesByPath(requestPath);
			if (CollectionUtils.isEmpty(roles))
			{
				LOG.error("Request path is not in any role, request path is " + requestPath + ".");
				return false;
			}

			Subject subject = getSubject(request, response);
			boolean authz = false;

			for (String role : roles)
			{
				if (subject.hasRole(role))
				{
					authz = true;
					break;
				}
			}

			return authz;
		}
		catch (Throwable e)
		{
			LOG.error("Account can not access the request path, request path is " + requestPath + '.', e);
			return false;
		}
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException
	{
		if (BMCWebUtils.isJSONRequest(request))
		{
			HttpServletResponse httpResp = WebUtils.toHttp(response);
			httpResp.setStatus(HttpServletResponse.SC_FORBIDDEN);

			String respBody = ResponseBuilder.buildForbidden(null);

			httpResp.getWriter().write(respBody);

			return false;
		}
		else
		{
			return super.onAccessDenied(request, response);
		}
	}

	/**
	 * 设置authenticationService
	 * 
	 * @param authenticationService
	 */
	public void setAuthenticationService(AuthenticationService authenticationService)
	{
		this.authenticationService = authenticationService;
	}
}
