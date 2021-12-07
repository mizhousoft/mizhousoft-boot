package com.mizhousoft.boot.authentication.service;

import java.util.Set;

/**
 * 认证服务提供者
 *
 * @version
 */
public interface AuthenticationServiceProvider
{
	/**
	 * 获取主服务ID
	 * 
	 * @return
	 */
	String getMainServiceId();

	/**
	 * 提供服务ID
	 * 
	 * @return
	 */
	Set<String> listServiceIds();
}
