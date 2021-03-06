package com.mizhousoft.boot.authentication.service;

import java.util.Set;

/**
 * 访问控制服务
 *
 * @version
 */
public interface AccessControlService
{
	/**
	 * 根据请求路径获取角色
	 * 
	 * @param serviceId
	 * @param requestPath
	 * @return
	 */
	Set<String> getRolesByPath(String serviceId, String requestPath);
}
