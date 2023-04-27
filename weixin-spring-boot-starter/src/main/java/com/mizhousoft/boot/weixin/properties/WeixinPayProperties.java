package com.mizhousoft.boot.weixin.properties;

import java.util.List;

/**
 * 配置
 *
 * @version
 */
public class WeixinPayProperties
{
	/**
	 * identifier
	 */
	private volatile String identifier;

	/**
	 * 商户ID
	 */
	private volatile String mchId;

	/**
	 * api V3密钥
	 */
	private volatile String apiV3Key;

	/**
	 * 证书序列号
	 */
	private volatile String certSerialNumber;

	/**
	 * 私钥路径
	 */
	private volatile String privateKeyPath;

	/**
	 * 证书路径
	 */
	private volatile List<String> certPemFilePaths;

	/**
	 * endpoint
	 */
	private volatile String endpoint = "https://api.mch.weixin.qq.com";

	/**
	 * 支付通知url
	 */
	private volatile String notifyUrl;

	/**
	 * 退款通知url
	 */
	private volatile String refundNotifyUrl;

	/**
	 * 获取identifier
	 * 
	 * @return
	 */
	public String getIdentifier()
	{
		return identifier;
	}

	/**
	 * 设置identifier
	 * 
	 * @param identifier
	 */
	public void setIdentifier(String identifier)
	{
		this.identifier = identifier;
	}

	/**
	 * 获取mchId
	 * 
	 * @return
	 */
	public String getMchId()
	{
		return mchId;
	}

	/**
	 * 设置mchId
	 * 
	 * @param mchId
	 */
	public void setMchId(String mchId)
	{
		this.mchId = mchId;
	}

	/**
	 * 获取apiV3Key
	 * 
	 * @return
	 */
	public String getApiV3Key()
	{
		return apiV3Key;
	}

	/**
	 * 设置apiV3Key
	 * 
	 * @param apiV3Key
	 */
	public void setApiV3Key(String apiV3Key)
	{
		this.apiV3Key = apiV3Key;
	}

	/**
	 * 获取certSerialNumber
	 * 
	 * @return
	 */
	public String getCertSerialNumber()
	{
		return certSerialNumber;
	}

	/**
	 * 设置certSerialNumber
	 * 
	 * @param certSerialNumber
	 */
	public void setCertSerialNumber(String certSerialNumber)
	{
		this.certSerialNumber = certSerialNumber;
	}

	/**
	 * 获取privateKeyPath
	 * 
	 * @return
	 */
	public String getPrivateKeyPath()
	{
		return privateKeyPath;
	}

	/**
	 * 设置privateKeyPath
	 * 
	 * @param privateKeyPath
	 */
	public void setPrivateKeyPath(String privateKeyPath)
	{
		this.privateKeyPath = privateKeyPath;
	}

	/**
	 * 获取certPemFilePaths
	 * 
	 * @return
	 */
	public List<String> getCertPemFilePaths()
	{
		return certPemFilePaths;
	}

	/**
	 * 设置certPemFilePaths
	 * 
	 * @param certPemFilePaths
	 */
	public void setCertPemFilePaths(List<String> certPemFilePaths)
	{
		this.certPemFilePaths = certPemFilePaths;
	}

	/**
	 * 获取endpoint
	 * 
	 * @return
	 */
	public String getEndpoint()
	{
		return endpoint;
	}

	/**
	 * 设置endpoint
	 * 
	 * @param endpoint
	 */
	public void setEndpoint(String endpoint)
	{
		this.endpoint = endpoint;
	}

	/**
	 * 获取notifyUrl
	 * 
	 * @return
	 */
	public String getNotifyUrl()
	{
		return notifyUrl;
	}

	/**
	 * 设置notifyUrl
	 * 
	 * @param notifyUrl
	 */
	public void setNotifyUrl(String notifyUrl)
	{
		this.notifyUrl = notifyUrl;
	}

	/**
	 * 获取refundNotifyUrl
	 * 
	 * @return
	 */
	public String getRefundNotifyUrl()
	{
		return refundNotifyUrl;
	}

	/**
	 * 设置refundNotifyUrl
	 * 
	 * @param refundNotifyUrl
	 */
	public void setRefundNotifyUrl(String refundNotifyUrl)
	{
		this.refundNotifyUrl = refundNotifyUrl;
	}
}
