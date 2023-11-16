package com.mizhousoft.boot.authentication.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.MediaType;

import com.mizhousoft.boot.authentication.SecurityConstants;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;

/**
 * WEB工具类
 *
 * @version
 */
public abstract class BMCWebUtils
{
	public static boolean isJSONRequest(ServletRequest request)
	{
		return isJSONRequest(WebUtils.toHttp(request));
	}

	/**
	 * 是否JSON请求
	 * 
	 * @param request
	 * @return
	 */
	public static boolean isJSONRequest(HttpServletRequest request)
	{
		String accept = request.getHeader(SecurityConstants.ACCEPT_HEADER);
		if (StringUtils.isBlank(accept))
		{
			return false;
		}
		else if (accept.contains(MediaType.APPLICATION_JSON_VALUE))
		{
			return true;
		}

		String contentType = request.getHeader(SecurityConstants.CONTENT_TYPE_HEADER);
		if (StringUtils.isBlank(contentType))
		{
			return false;
		}

		return contentType.contains(MediaType.APPLICATION_JSON_VALUE);
	}
}
