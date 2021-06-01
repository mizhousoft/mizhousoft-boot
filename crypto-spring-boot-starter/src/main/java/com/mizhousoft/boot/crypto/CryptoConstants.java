package com.mizhousoft.boot.crypto;

/**
 * 安全常量
 *
 * @version
 */
public interface CryptoConstants
{
	/**
	 * AES秘钥大小
	 */
	int AES_KEY_SIZE = 16;

	/**
	 * 盐值大小
	 */
	int SALT_SIZE = 32;

	/**
	 * 密码片大小
	 */
	int PASSWD_SEGMENT = 32;

	/**
	 * Mac key大小
	 */
	int HMAC_KEY_SIZE = 16;
}
