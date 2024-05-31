package com.mizhousoft.boot.authentication.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 配置
 *
 * @version
 */
@Component
@ConfigurationProperties(prefix = "authentication")
public class AuthenticationProperties
{
	// 校验请求HOST
	private boolean verifyHost;

	// 启用双因子认证
	private boolean twoFactorAuthcEnable;

	// Session互斥，同一时间1个账户只能单点登录
	private boolean sessionMutex = true;

	/**
	 * referer domain
	 */
	private String referers;

	/**
	 * 是否安全模式
	 */
	private boolean secureMode = true;

	/**
	 * 获取verifyHost
	 * 
	 * @return
	 */
	public boolean isVerifyHost()
	{
		return verifyHost;
	}

	/**
	 * 设置verifyHost
	 * 
	 * @param verifyHost
	 */
	public void setVerifyHost(boolean verifyHost)
	{
		this.verifyHost = verifyHost;
	}

	/**
	 * 获取twoFactorAuthcEnable
	 * 
	 * @return
	 */
	public boolean isTwoFactorAuthcEnable()
	{
		return twoFactorAuthcEnable;
	}

	/**
	 * 设置twoFactorAuthcEnable
	 * 
	 * @param twoFactorAuthcEnable
	 */
	public void setTwoFactorAuthcEnable(boolean twoFactorAuthcEnable)
	{
		this.twoFactorAuthcEnable = twoFactorAuthcEnable;
	}

	/**
	 * 获取sessionMutex
	 * 
	 * @return
	 */
	public boolean isSessionMutex()
	{
		return sessionMutex;
	}

	/**
	 * 设置sessionMutex
	 * 
	 * @param sessionMutex
	 */
	public void setSessionMutex(boolean sessionMutex)
	{
		this.sessionMutex = sessionMutex;
	}

	/**
	 * 获取referers
	 * 
	 * @return
	 */
	public String getReferers()
	{
		return referers;
	}

	/**
	 * 设置referers
	 * 
	 * @param referers
	 */
	public void setReferers(String referers)
	{
		this.referers = referers;
	}

	/**
	 * 获取secureMode
	 * 
	 * @return
	 */
	public boolean isSecureMode()
	{
		return secureMode;
	}

	/**
	 * 设置secureMode
	 * 
	 * @param secureMode
	 */
	public void setSecureMode(boolean secureMode)
	{
		this.secureMode = secureMode;
	}
}
