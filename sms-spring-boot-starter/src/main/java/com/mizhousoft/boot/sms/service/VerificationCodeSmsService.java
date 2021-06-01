package com.mizhousoft.boot.sms.service;

import com.mizhousoft.cloudsdk.CloudSDKException;
import com.mizhousoft.cloudsdk.sms.CloudSmsTemplate;

/**
 * 验证码短信服务
 *
 * @version
 */
public interface VerificationCodeSmsService
{
	/**
	 * 发送
	 * 
	 * @param phoneNumber
	 * @param host
	 * @param smsTemplate
	 * @return
	 * @throws CloudSDKException
	 */
	String sendVerificationCode(String phoneNumber, String host, CloudSmsTemplate smsTemplate) throws CloudSDKException;

	/**
	 * 校验
	 * 
	 * @param phoneNumber
	 * @param code
	 * @param smsTemplate
	 * @throws CloudSDKException
	 */
	void verify(String phoneNumber, String code, CloudSmsTemplate smsTemplate) throws CloudSDKException;
}
