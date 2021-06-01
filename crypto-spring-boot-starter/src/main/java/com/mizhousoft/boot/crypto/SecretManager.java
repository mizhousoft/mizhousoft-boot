package com.mizhousoft.boot.crypto;

import com.mizhousoft.commons.crypto.CryptoException;

/**
 * 秘钥管理器
 *
 * @version
 */
public interface SecretManager
{
	/**
	 * 获取秘钥
	 * 
	 * @param keyId
	 * @return
	 * @throws CryptoException
	 */
	byte[] getSecret(String keyId) throws CryptoException;
}
