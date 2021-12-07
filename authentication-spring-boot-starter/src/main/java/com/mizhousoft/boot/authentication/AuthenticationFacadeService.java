package com.mizhousoft.boot.authentication;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 认证服务
 *
 * @version
 */
public interface AuthenticationFacadeService
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
	 * 获取不更新访问时间的请求路径
	 * 
	 * @return
	 */
	Map<String, String> queryNonUpdateAccessTimeRequestPaths();

	/**
	 * 根据请求路径获取角色
	 * 
	 * @param requestPath
	 * @return
	 */
	Set<String> getRolesByPath(String requestPath);
}
