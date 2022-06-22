package com.mizhousoft.boot.authentication;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 认证服务
 *
 * @version
 */
public interface AuthenticationService
{
	/**
	 * 获取仅仅要认证的请求路径
	 * 
	 * @return
	 */
	List<String> getAuthcRequestPaths();

	/**
	 * 获取要认证和鉴权的请求路径
	 * 
	 * @return
	 */
	List<String> getAuthzRequestPaths();

	/**
	 * 获取登录请求路径
	 * 
	 * @return
	 */
	List<String> getLoginAuditRequestPaths();

	/**
	 * 获取不更新访问时间的请求路径
	 * 
	 * @return
	 */
	Map<String, String> getNonUpdateAccessTimeRequestPaths();

	/**
	 * 根据请求路径获取角色
	 * 
	 * @param requestPath
	 * @return
	 */
	Set<String> getRolesByPath(String requestPath);
}
