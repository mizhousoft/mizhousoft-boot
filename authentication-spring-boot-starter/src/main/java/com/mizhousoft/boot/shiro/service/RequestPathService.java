package com.mizhousoft.boot.shiro.service;

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
	 * @return
	 */
	List<String> queryAuthcRequestPaths();

	/**
	 * 查询要认证和鉴权的请求路径
	 * 
	 * @return
	 */
	List<String> queryAuthzRequestPaths();

	/**
	 * 获取登录审计请求路径
	 * 
	 * @return
	 */
	List<String> queryLoginAuditRequestPaths();

	/**
	 * 查询不更新访问时间的请求路径
	 * 
	 * @return
	 */
	List<String> queryNonUpdateAccessTimeRequestPaths();
}
