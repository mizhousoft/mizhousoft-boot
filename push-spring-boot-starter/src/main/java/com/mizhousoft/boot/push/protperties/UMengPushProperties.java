package com.mizhousoft.boot.push.protperties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 配置
 *
 * @version
 */
@Component
@ConfigurationProperties(prefix = "push.umeng")
public class UMengPushProperties
{
	// APP ID
	private String appId;

	// APP secret
	private String appSecret;

	// 是否测试环境
	private boolean sandbox;

	/**
	 * 获取appId
	 * 
	 * @return
	 */
	public String getAppId()
	{
		return appId;
	}

	/**
	 * 设置appId
	 * 
	 * @param appId
	 */
	public void setAppId(String appId)
	{
		this.appId = appId;
	}

	/**
	 * 获取appSecret
	 * 
	 * @return
	 */
	public String getAppSecret()
	{
		return appSecret;
	}

	/**
	 * 设置appSecret
	 * 
	 * @param appSecret
	 */
	public void setAppSecret(String appSecret)
	{
		this.appSecret = appSecret;
	}

	/**
	 * 获取sandbox
	 * 
	 * @return
	 */
	public boolean isSandbox()
	{
		return sandbox;
	}

	/**
	 * 设置sandbox
	 * 
	 * @param sandbox
	 */
	public void setSandbox(boolean sandbox)
	{
		this.sandbox = sandbox;
	}
}
