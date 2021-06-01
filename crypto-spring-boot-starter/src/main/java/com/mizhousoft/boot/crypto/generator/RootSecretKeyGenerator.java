package com.mizhousoft.boot.crypto.generator;

import org.apache.commons.codec.DecoderException;

import com.mizhousoft.boot.crypto.FixedBytesProvider;
import com.mizhousoft.commons.crypto.CryptoException;
import com.mizhousoft.commons.crypto.generator.PBESecretKeyGenerator;
import com.mizhousoft.commons.lang.HexUtils;

/**
 * 根密码生成器
 *
 * @version
 */
public final class RootSecretKeyGenerator
{
	/**
	 * 构造函数
	 *
	 */
	private RootSecretKeyGenerator()
	{

	}

	/**
	 * 生成根秘钥
	 * 
	 * @param size
	 * @param salt
	 * @param passSegment
	 * @param fixedBytesProvider
	 * @return
	 * @throws CryptoException
	 */
	public static byte[] generateKey(int size, String salt, String passSegment, FixedBytesProvider fixedBytesProvider)
	        throws CryptoException
	{
		try
		{
			byte[] passBytes = generatePasswd(passSegment, fixedBytesProvider);
			byte[] saltBytes = HexUtils.decodeHex(salt);
			byte[] rootKey = PBESecretKeyGenerator.deriveKey(size, passBytes, saltBytes);

			return rootKey;
		}
		catch (DecoderException e)
		{
			throw new CryptoException("Salt decode failed.", e);
		}
	}

	/**
	 * 生成密码
	 * 
	 * @return
	 * @throws CryptoException
	 */
	private static byte[] generatePasswd(String passSegment, FixedBytesProvider fixedBytesProvider)
	        throws CryptoException
	{
		byte[] fixedBytes = fixedBytesProvider.getFixedBytes();
		if (null == fixedBytes)
		{
			throw new CryptoException("Fixed bytes is null.");
		}

		try
		{
			byte[] passBytes = HexUtils.decodeHex(passSegment);
			if (passBytes.length != fixedBytes.length)
			{
				throw new CryptoException("FixedBytesProvider byte length is not equals with passSegment.");
			}

			byte[] bytes = new byte[fixedBytes.length];
			for (int i = 0; i < bytes.length; ++i)
			{
				bytes[i] = (byte) (passBytes[i] ^ fixedBytes[i]);
			}

			return bytes;
		}
		catch (DecoderException e)
		{
			throw new CryptoException("PassSegment decode failed.", e);
		}
	}
}
