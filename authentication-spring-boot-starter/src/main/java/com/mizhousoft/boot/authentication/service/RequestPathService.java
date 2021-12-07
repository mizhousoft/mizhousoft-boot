package com.mizhousoft.boot.authentication.service;

import java.util.List;

/**
 * 请求路径服务
 *
 * @version
 */
public interface RequestPathService
{
	/**
	 * 查询仅仅要认证的请求路径
	 * 
	 * @param serviceId
	 * @return
	 */
	List<String> queryAuthcRequestPaths(String serviceId);

	/**
	 * 查询要认证和鉴权的请求路径
	 * 
	 * @param serviceId
	 * @return
	 */
	List<String> queryAuthzRequestPaths(String serviceId);

	/**
	 * 获取登录审计请求路径
	 * 
	 * @param serviceId
	 * @return
	 */
	List<String> queryLoginAuditRequestPaths(String serviceId);

	/**
	 * 查询不更新访问时间的请求路径
	 * 
	 * @param serviceId
	 * @return
	 */
	List<String> queryNonUpdateAccessTimeRequestPaths(String serviceId);
}
