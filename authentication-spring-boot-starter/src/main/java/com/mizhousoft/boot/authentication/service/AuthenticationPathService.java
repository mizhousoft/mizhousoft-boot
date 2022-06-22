package com.mizhousoft.boot.authentication.service;

import java.util.List;

/**
 * 认证路径服务
 *
 * @version
 */
public interface AuthenticationPathService
{
	/**
	 * 查询仅仅要认证的请求路径
	 * 
	 * @return
	 */
	List<String> getAuthcRequestPaths();

	/**
	 * 查询要认证和鉴权的请求路径
	 * 
	 * @return
	 */
	List<String> getAuthzRequestPaths();

	/**
	 * 获取登录请求路径
	 * 
	 * @return
	 */
	List<String> getLoginRequestPaths();

	/**
	 * 获取不更新访问时间的请求路径
	 * 
	 * @return
	 */
	List<String> getNonUpdateAccessTimeRequestPaths();
}
