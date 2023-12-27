package com.mizhousoft.boot.authentication.service;

import java.util.ArrayList;
import java.util.List;

/**
 * 应用认证服务
 *
 * @version
 */
public interface ApplicationAuthenticationService
{
	/**
	 * 获取服务ID
	 * 
	 * @return
	 */
	String getServiceId();

	/**
	 * 获取扩展的登录请求路径
	 * 
	 * @return
	 */
	default List<String> getExtendedLoginRequestPaths()
	{
		return new ArrayList<>(0);
	}

	/**
	 * 获取不更新访问时间的请求路径
	 * 
	 * @return
	 */
	default List<String> getNonUpdateAccessTimeRequestPaths()
	{
		return new ArrayList<>(0);
	}

	/**
	 * 获取CSRF校验排除路径
	 * 
	 * @return
	 */
	default List<String> getCsrfExcludeRequestPaths()
	{
		return new ArrayList<>(0);
	}
}
