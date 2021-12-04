package com.mizhousoft.boot.authentication.context;

import java.io.Serializable;

import com.mizhousoft.boot.authentication.Authentication;

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
