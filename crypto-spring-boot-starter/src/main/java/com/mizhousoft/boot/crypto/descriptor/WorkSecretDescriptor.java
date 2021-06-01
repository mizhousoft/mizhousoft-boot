package com.mizhousoft.boot.crypto.descriptor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * 工作密钥描述符
 *
 * @version
 */
@JsonPropertyOrder(value = { "data", "sign", "description" })
@JsonIgnoreProperties(value = { "key" })
public class WorkSecretDescriptor
{
	private String key;

	private String data;

	private String sign;

	private String description;

	/**
	 * 获取key
	 * 
	 * @return
	 */
	public String getKey()
	{
		return key;
	}

	/**
	 * 设置key
	 * 
	 * @param key
	 */
	public void setKey(String key)
	{
		this.key = key;
	}

	/**
	 * 获取data
	 * 
	 * @return
	 */
	public String getData()
	{
		return data;
	}

	/**
	 * 设置data
	 * 
	 * @param data
	 */
	public void setData(String data)
	{
		this.data = data;
	}

	/**
	 * 获取sign
	 * 
	 * @return
	 */
	public String getSign()
	{
		return sign;
	}

	/**
	 * 设置sign
	 * 
	 * @param sign
	 */
	public void setSign(String sign)
	{
		this.sign = sign;
	}

	/**
	 * 获取description
	 * 
	 * @return
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * 设置description
	 * 
	 * @param description
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}
}
