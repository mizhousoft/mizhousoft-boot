package com.mizhousoft.boot.crypto;

import com.mizhousoft.commons.crypto.CryptoException;

/**
 * 加解密服务
 *
 * @version
 */
public interface CryptoService
{
	/**
	 * 加密成字符串
	 * 
	 * @param plainText
	 * @param keyId
	 * @return
	 * @throws CryptoException
	 */
	String encrypt(String plainText, String keyId) throws CryptoException;

	/**
	 * 解密成字符串
	 * 
	 * @param cipherText
	 * @param keyId
	 * @return
	 * @throws CryptoException
	 */
	String decryptToString(String cipherText, String keyId) throws CryptoException;
}
