package com.mizhousoft.boot.authentication.event;

import org.springframework.context.ApplicationEvent;

import com.mizhousoft.boot.authentication.Authentication;

/**
 * 帐号退出事件
 *
 */
public class AccountLogoutEvent extends ApplicationEvent
{
	private static final long serialVersionUID = 1013337619923730996L;

	/**
	 * Authentication
	 */
	private Authentication authentication;

	/**
	 * 构造函数
	 *
	 * @param source
	 */
	public AccountLogoutEvent(Authentication authentication)
	{
		super(authentication);

		this.authentication = authentication;
	}

	/**
	 * 获取authentication
	 * 
	 * @return
	 */
	public Authentication getAuthentication()
	{
		return authentication;
	}
}
