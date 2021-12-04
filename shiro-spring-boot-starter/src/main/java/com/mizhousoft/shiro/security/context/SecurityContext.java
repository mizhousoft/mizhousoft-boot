package com.mizhousoft.shiro.security.context;

import java.io.Serializable;

import com.mizhousoft.shiro.security.Authentication;

/**
 * 安全上下文
 *
 * @version
 */
public interface SecurityContext extends Serializable
{
	/**
	 * 获取认证信息
	 * 
	 * @return
	 */
	Authentication getAuthentication();
}
