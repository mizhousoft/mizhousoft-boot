package com.mizhousoft.boot.crypto.impl;

import org.apache.commons.codec.DecoderException;

import com.mizhousoft.boot.crypto.CryptoService;
import com.mizhousoft.boot.crypto.SecretManager;
import com.mizhousoft.commons.crypto.AESEncryptor;
import com.mizhousoft.commons.crypto.CryptoException;
import com.mizhousoft.commons.lang.CharEncoding;
import com.mizhousoft.commons.lang.HexUtils;

/**
 * 加解密服务
 *
 * @version
 */
public class CryptoServiceImpl implements CryptoService
{
	private SecretManager secretManager;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String decryptToString(String cipherText, String keyId) throws CryptoException
	{
		try
		{
			byte[] secret = secretManager.getSecret(keyId);
			byte[] bytes = AESEncryptor.decrypt(HexUtils.decodeHex(cipherText), secret);
			String plainText = new String(bytes, CharEncoding.UTF8);

			return plainText;
		}
		catch (DecoderException e)
		{
			throw new CryptoException("CipherText decode failed.");
		}
	}

	/**
	 * 初始化
	 * 
	 * @param rsecretPath
	 * @param workFilePath
	 * @throws CryptoException
	 */
	public void init(String rsecretPath, String workFilePath) throws CryptoException
	{
		SecretManagerImpl secretManager = new SecretManagerImpl();
		secretManager.loadSecretFile(rsecretPath, workFilePath);

		this.secretManager = secretManager;
	}
}
