package com.mizhousoft.boot.crypto;

import com.mizhousoft.commons.crypto.CryptoException;
import com.mizhousoft.commons.crypto.generator.RandomGenerator;

/**
 * 固定字节生成器
 *
 * @version
 */
public class FixedBytesGenarator
{
	/**
	 * 
	 * @param args
	 * @throws CryptoException
	 */
	public static void main(String[] args) throws CryptoException
	{
		byte[] bytes = RandomGenerator.generateKey(32);

		System.out.println("byte[] fixedBytes = new byte[32];");

		for (int i = 0; i < bytes.length; ++i)
		{
			System.out.println("fixedBytes[" + i + "] = (byte) 0x" + Integer.toHexString(bytes[i] & 0xFF) + ";");
		}
	}
}
