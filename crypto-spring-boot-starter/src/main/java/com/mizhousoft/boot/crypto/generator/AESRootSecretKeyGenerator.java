package com.mizhousoft.boot.crypto.generator;

import com.mizhousoft.boot.crypto.CryptoConstants;
import com.mizhousoft.boot.crypto.FixedBytesProvider;
import com.mizhousoft.commons.crypto.CryptoException;

/**
 * AES根秘钥生成器
 *
 * @version
 */
public final class AESRootSecretKeyGenerator
{
	/**
	 * 构造函数
	 *
	 */
	private AESRootSecretKeyGenerator()
	{

	}

	/**
	 * 生成根秘钥
	 * 
	 * @return
	 * @throws CryptoException
	 */
	public static byte[] generateRootKey(String salt, String passSegment, FixedBytesProvider fixedBytesProvider)
	        throws CryptoException
	{
		int size = CryptoConstants.AES_KEY_SIZE;
		return RootSecretKeyGenerator.generateKey(size, salt, passSegment, fixedBytesProvider);
	}
}
