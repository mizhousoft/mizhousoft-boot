package com.mizhousoft.boot.restclient;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 配置属性
 *
 * @version
 */
@Component
@ConfigurationProperties(prefix = "restclient.trust.keystore")
public class RestClientProperties
{
	// 文件路径，为空的话，加载内置的信任文件
	private String path;

	// 密码
	private String password;

	/**
	 * 获取path
	 * 
	 * @return
	 */
	public String getPath()
	{
		return path;
	}

	/**
	 * 设置path
	 * 
	 * @param path
	 */
	public void setPath(String path)
	{
		this.path = path;
	}

	/**
	 * 获取password
	 * 
	 * @return
	 */
	public String getPassword()
	{
		return password;
	}

	/**
	 * 设置password
	 * 
	 * @param password
	 */
	public void setPassword(String password)
	{
		this.password = password;
	}
}
