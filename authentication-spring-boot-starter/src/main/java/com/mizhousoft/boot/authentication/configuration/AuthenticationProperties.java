package com.mizhousoft.boot.authentication.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 配置
 *
 * @version
 */
@Component
@ConfigurationProperties(prefix = "authentication.")
public class AuthenticationProperties
{
	// 校验请求HOST
	private boolean verifyHost;

	// 启用双因子认证
	private boolean twoFactorAuthcEnable;

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
}
