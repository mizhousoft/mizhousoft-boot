package com.mizhousoft.boot.crypto.domain;

import com.mizhousoft.boot.crypto.descriptor.WorkSecretDescriptor;

/**
 * 工作秘钥
 *
 * @version
 */
public class WorkSecret
{
	private byte[] secret;

	private WorkSecretDescriptor descriptor;

	/**
	 * 获取secret
	 * 
	 * @return
	 */
	public byte[] getSecret()
	{
		return secret;
	}

	/**
	 * 设置secret
	 * 
	 * @param secret
	 */
	public void setSecret(byte[] secret)
	{
		this.secret = secret;
	}

	/**
	 * 获取descriptor
	 * 
	 * @return
	 */
	public WorkSecretDescriptor getDescriptor()
	{
		return descriptor;
	}

	/**
	 * 设置descriptor
	 * 
	 * @param descriptor
	 */
	public void setDescriptor(WorkSecretDescriptor descriptor)
	{
		this.descriptor = descriptor;
	}
}
