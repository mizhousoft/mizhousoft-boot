package com.mizhousoft.boot.geo.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 配置
 *
 * @version
 */
@Component
@ConfigurationProperties(prefix = "geo.tmap")
public class TMapProperties
{
	/**
	 * 应用Key
	 */
	private volatile String appKey;

	/**
	 * 获取appKey
	 * 
	 * @return
	 */
	public String getAppKey()
	{
		return appKey;
	}

	/**
	 * 设置appKey
	 * 
	 * @param appKey
	 */
	public void setAppKey(String appKey)
	{
		this.appKey = appKey;
	}
}
