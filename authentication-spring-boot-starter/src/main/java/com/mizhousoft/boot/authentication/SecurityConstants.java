package com.mizhousoft.boot.authentication;

/**
 * 安全常量
 *
 * @version
 */
public interface SecurityConstants
{
	/**
	 * User-Agent
	 */
	String USER_AGENT_HEADER = "User-Agent";

	/**
	 * ACCEPT
	 */
	String ACCEPT_HEADER = "Accept";

	/**
	 * Content-Type
	 */
	String CONTENT_TYPE_HEADER = "Content-Type";

	/**
	 * 主机
	 */
	String HOST = "HOST";

	/**
	 * 默认Session闲时超时时间
	 */
	int DEFAULT_SESSION_IDLE_TIMEOUT = 30;

	/**
	 * Referer
	 */
	String REFERER = "Referer";

	/**
	 * X-Csrf-Token
	 */
	String X_CSRF_TOKEN = "X-Csrf-Token";
}