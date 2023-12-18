package com.mizhousoft.boot.authentication.filter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Set;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.servlet.OncePerRequestFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mizhousoft.boot.authentication.SecurityConstants;
import com.mizhousoft.boot.authentication.util.ShiroUtils;
import com.mizhousoft.commons.lang.CharEncoding;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

/**
 * RefererFilter
 *
 */
public class RefererFilter extends OncePerRequestFilter
{
	private static final Logger LOG = LoggerFactory.getLogger(RefererFilter.class);

	// 登录URL
	private String loginUrl;

	/**
	 * 支持的Referer
	 */
	private Set<String> supportReferers = Collections.emptySet();

	/**
	 * 构造函数
	 *
	 * @param loginUrl
	 * @param supportReferers
	 */
	public RefererFilter(String loginUrl, Set<String> supportReferers)
	{
		super();
		this.loginUrl = loginUrl;
		this.supportReferers = supportReferers;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doFilterInternal(ServletRequest req, ServletResponse res, FilterChain chain) throws ServletException, IOException
	{
		HttpServletRequest httpRequest = WebUtils.toHttp(req);

		String referer = httpRequest.getHeader(SecurityConstants.REFERER);
		if (isRefererAllow(referer))
		{
			chain.doFilter(httpRequest, res);
		}
		else
		{
			Subject subject = SecurityUtils.getSubject();

			ShiroUtils.logout(httpRequest, res, subject, loginUrl);
		}
	}

	private boolean isRefererAllow(String referer)
	{
		if (null == referer)
		{
			LOG.error("Referer is null.");
			return false;
		}

		try
		{
			URL url = new java.net.URL(referer);
			String host = url.getHost();

			if (!supportReferers.contains(host))
			{
				referer = StringUtils.left(referer, 100);
				LOG.error("Referer is invalid, value is {}.", Base64.encodeBase64String(referer.getBytes(CharEncoding.UTF8)));

				return false;
			}
			else
			{
				return true;
			}
		}
		catch (MalformedURLException e)
		{
			referer = StringUtils.left(referer, 100);
			LOG.error("Referer is invalid, value is {}.", Base64.encodeBase64String(referer.getBytes(CharEncoding.UTF8)));

			return false;
		}
	}
}
