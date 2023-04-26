package com.mizhousoft.boot.restclient;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 连接池属性
 *
 */
@Component
@ConfigurationProperties(prefix = "restclient.pool")
public class PoolProperties
{
	/**
	 * 连接超时时间
	 */
	private int connectTimeout = 10000;

	/**
	 * 读取超时时间
	 */
	private int readTimeout = 30000;

	/**
	 * 获取connectTimeout
	 * 
	 * @return
	 */
	public int getConnectTimeout()
	{
		return connectTimeout;
	}

	/**
	 * 设置connectTimeout
	 * 
	 * @param connectTimeout
	 */
	public void setConnectTimeout(int connectTimeout)
	{
		this.connectTimeout = connectTimeout;
	}

	/**
	 * 获取readTimeout
	 * 
	 * @return
	 */
	public int getReadTimeout()
	{
		return readTimeout;
	}

	/**
	 * 设置readTimeout
	 * 
	 * @param readTimeout
	 */
	public void setReadTimeout(int readTimeout)
	{
		this.readTimeout = readTimeout;
	}
}
