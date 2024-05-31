package com.mizhousoft.boot.authentication.filter.authc;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mizhousoft.boot.authentication.AccountDetails;
import com.mizhousoft.boot.authentication.SecurityConstants;
import com.mizhousoft.boot.authentication.authc.AuthenticationRequest;
import com.mizhousoft.boot.authentication.authc.UnionAuthenticationToken;
import com.mizhousoft.boot.authentication.servlet.TokenCookie;
import com.mizhousoft.boot.authentication.util.BMCWebUtils;
import com.mizhousoft.boot.authentication.util.ResponseBuilder;
import com.mizhousoft.commons.json.JSONException;
import com.mizhousoft.commons.json.JSONUtils;
import com.mizhousoft.commons.web.util.CookieUtils;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 帐号密码认证过滤器
 * 
 * @version
 */
public class AccountPasswordAuthenticationFilter extends FormAuthenticationFilter
{
	private static final Logger LOG = LoggerFactory.getLogger(AccountPasswordAuthenticationFilter.class);

	/**
	 * 是否安全模式
	 */
	private boolean secureMode;

	/**
	 * 构造函数
	 *
	 * @param secureMode
	 */
	public AccountPasswordAuthenticationFilter(boolean secureMode)
	{
		super();
		this.secureMode = secureMode;
	}

	/**
	 * 保存请求
	 * 
	 * @param request
	 */
	protected void saveRequest(ServletRequest request)
	{
		// ignore
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void postHandle(ServletRequest request, ServletResponse response) throws Exception
	{
		super.postHandle(request, response);

		// 清除所有的cookie，保证前端获取不到
		Subject subject = getSubject(request, response);
		if (!subject.isAuthenticated() || null == subject.getPrincipal())
		{
			CookieUtils.removeAll(WebUtils.toHttp(request), WebUtils.toHttp(response));
		}
	}

	/**
	 * 创建AuthenticationToken
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@Override
	protected AuthenticationToken createToken(ServletRequest request, ServletResponse response)
	{
		AuthenticationToken token = null;

		HttpServletRequest httpRequest = WebUtils.toHttp(request);

		String userAgent = httpRequest.getHeader(SecurityConstants.USER_AGENT_HEADER);
		if (StringUtils.isBlank(userAgent))
		{
			LOG.error("Request User-Agent header is null.");
			return null;
		}

		if (BMCWebUtils.isJSONRequest(httpRequest))
		{
			try
			{
				String requestBody = com.mizhousoft.commons.web.util.WebUtils.getRequestBody(request);
				AuthenticationRequest authRequest = JSONUtils.parse(requestBody, AuthenticationRequest.class);

				String account = authRequest.getAccount();
				String password = authRequest.getPassword();
				char[] charPwd = password != null ? password.toCharArray() : null;

				UnionAuthenticationToken utoken = new UnionAuthenticationToken(account, charPwd, false, getHost(request));
				utoken.setCode(authRequest.getCode());

				token = utoken;
			}
			catch (IOException | JSONException e)
			{
				LOG.error("Parse request body to AccountPassword failed.");
			}
		}
		else
		{
			token = super.createToken(request, response);
		}

		return token;
	}

	/**
	 * 执行登录
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception
	{
		AuthenticationToken token = createToken(request, response);
		if (token == null)
		{
			String msg = "createToken method implementation returned null. A valid non-null AuthenticationToken "
			        + "must be created in order to execute a login attempt.";
			throw new IllegalStateException(msg);
		}

		try
		{
			CookieUtils.removeAll(WebUtils.toHttp(request), WebUtils.toHttp(response));

			Subject subject = getSubject(request, response);

			Session session = subject.getSession();
			final LinkedHashMap<Object, Object> attributes = new LinkedHashMap<Object, Object>();
			final Collection<Object> keys = session.getAttributeKeys();
			for (Object key : keys)
			{
				final Object value = session.getAttribute(key);
				if (value != null)
				{
					attributes.put(key, value);
				}
			}
			session.stop();

			subject.login(token);

			session = subject.getSession();

			for (final Object key : attributes.keySet())
			{
				session.setAttribute(key, attributes.get(key));
			}

			return onLoginSuccess(token, subject, request, response);
		}
		catch (AuthenticationException e)
		{
			return onLoginFailure(token, e, request, response);
		}
	}

	/**
	 * 登录成功后置处理
	 * 
	 * @param token
	 * @param subject
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response)
	        throws Exception
	{
		HttpServletRequest httpRequest = WebUtils.toHttp(request);

		String userAgent = httpRequest.getHeader(SecurityConstants.USER_AGENT_HEADER);
		subject.getSession().setAttribute(SecurityConstants.USER_AGENT_HEADER, userAgent);

		Object principal = subject.getPrincipal();
		if (principal instanceof AccountDetails accountDetails)
		{
			int timeout = accountDetails.getSessionIdleTimeout();

			Session currentSessison = subject.getSession();
			long maxIdleTimeInMillis = timeout * 60 * 1000;
			currentSessison.setTimeout(maxIdleTimeInMillis);
			httpRequest.getSession().setMaxInactiveInterval(timeout * 60);

			TokenCookie cookie = new TokenCookie(SecurityConstants.CSRF_TOKEN);
			cookie.setValue(accountDetails.getCsrfToken());
			cookie.setSecure(secureMode);
			cookie.setHttpOnly(false);
			cookie.saveTo(httpRequest, WebUtils.toHttp(response));
		}

		String host = com.mizhousoft.commons.web.util.WebUtils.getRemoteIPAddress(request);
		subject.getSession().setAttribute(SecurityConstants.HOST, host);

		return super.onLoginSuccess(token, subject, request, response);
	}

	/**
	 * 登录失败拦截处理
	 * 
	 * @param token
	 * @param e
	 * @param request
	 * @param response
	 * @return
	 */
	protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response)
	{
		if (e instanceof IncorrectCredentialsException)
		{
			String message = e.getMessage();
			if (!StringUtils.isBlank(message))
			{
				int leaveCount = 0;

				try
				{
					leaveCount = Integer.parseInt(message);
				}
				catch (NumberFormatException ne)
				{
					LOG.warn("Login failure leave count is invalid.");
				}

				if (0 != leaveCount)
				{
					request.setAttribute("loginFailureLeaveCount", leaveCount);
				}
			}
			else
			{
				LOG.warn("Login failure leave count is null.");
			}
		}

		return super.onLoginFailure(token, e, request, response);
	}

	/**
	 * 登陆成功，跳转到成功页面
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	protected void issueSuccessRedirect(ServletRequest request, ServletResponse response) throws Exception
	{
		if (BMCWebUtils.isJSONRequest(request))
		{
			HttpServletResponse httpResp = WebUtils.toHttp(response);
			httpResp.setStatus(HttpServletResponse.SC_OK);

			AccountDetails accountDetails = null;
			Subject subject = SecurityUtils.getSubject();
			Object principal = subject.getPrincipal();
			if (principal instanceof AccountDetails)
			{
				accountDetails = ((AccountDetails) principal);
			}

			String loginUrl = getLoginUrl();
			String respBody = ResponseBuilder.buildSucceed(httpResp.encodeRedirectURL(loginUrl), accountDetails);

			httpResp.getWriter().write(respBody);
		}
		else
		{
			super.issueSuccessRedirect(request, response);
		}
	}

	/**
	 * 跳转到登录页面
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@Override
	protected void redirectToLogin(ServletRequest request, ServletResponse response) throws IOException
	{
		if (BMCWebUtils.isJSONRequest(request))
		{
			HttpServletResponse httpResp = WebUtils.toHttp(response);
			httpResp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

			String loginUrl = getLoginUrl();
			String respBody = ResponseBuilder.buildUnauthorized(httpResp.encodeRedirectURL(loginUrl), null);

			httpResp.getWriter().write(respBody);
		}
		else
		{
			super.redirectToLogin(request, response);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getHost(ServletRequest request)
	{
		return com.mizhousoft.commons.web.util.WebUtils.getRemoteIPAddress(request);
	}
}
