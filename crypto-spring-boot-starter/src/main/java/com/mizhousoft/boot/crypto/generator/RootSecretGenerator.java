package com.mizhousoft.boot.crypto.generator;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.lang3.StringUtils;

import com.mizhousoft.commons.crypto.CryptoException;
import com.mizhousoft.commons.lang.HexUtils;
import com.mizhousoft.boot.crypto.FixedBytesProvider;
import com.mizhousoft.boot.crypto.descriptor.RootSecretDescriptor;
import com.mizhousoft.boot.crypto.provider.DefaultFixedBytesProvider;

/**
 * 根密钥生成器
 *
 * @version
 */
public class RootSecretGenerator
{
	public static byte[] gen(RootSecretDescriptor descriptor) throws CryptoException
	{
		String passSegment = genPasswordSegment(descriptor);
		String salt = genSalt(descriptor);
		FixedBytesProvider fixedBytesProvider = new DefaultFixedBytesProvider();

		byte[] rootKey = AESRootSecretKeyGenerator.generateRootKey(salt, passSegment, fixedBytesProvider);

		return rootKey;
	}

	private static String genSalt(RootSecretDescriptor descriptor) throws CryptoException
	{
		try
		{
			byte[] dog = HexUtils.decodeHex(descriptor.getDog());
			byte[] pig = HexUtils.decodeHex(descriptor.getPig());

			byte[] bytes = new byte[dog.length];
			for (int i = 0; i < bytes.length; ++i)
			{
				bytes[i] = (byte) (dog[i] & pig[i]);
			}

			return HexUtils.encodeHexString(bytes, false);
		}
		catch (DecoderException e)
		{
			throw new CryptoException("Decorde secre failed.");
		}
	}

	private static String genPasswordSegment(RootSecretDescriptor descriptor) throws CryptoException
	{
		try
		{
			byte[] dog = HexUtils.decodeHex(descriptor.getDog());
			byte[] cat = HexUtils.decodeHex(descriptor.getCat());

			byte[] bytes1 = new byte[dog.length];
			for (int i = 0; i < bytes1.length; ++i)
			{
				bytes1[i] = (byte) (dog[i] ^ cat[i]);
			}

			byte[] bird = HexUtils.decodeHex(StringUtils.reverse(descriptor.getBird()));

			byte[] bytes2 = new byte[bird.length];
			for (int i = 0; i < bytes2.length; ++i)
			{
				bytes2[i] = (byte) (bytes2[i] & bird[i]);
			}

			byte[] pig = HexUtils.decodeHex(descriptor.getPig());

			byte[] bytes3 = new byte[pig.length];
			for (int i = 0; i < bytes3.length; ++i)
			{
				bytes3[i] = (byte) (bytes3[i] ^ pig[i]);
			}

			return HexUtils.encodeHexString(bytes3, false);
		}
		catch (DecoderException e)
		{
			throw new CryptoException("Decorde secre failed.");
		}
	}
}
