package com.mizhousoft.boot.crypto.provider;

import com.mizhousoft.boot.crypto.FixedBytesProvider;

/**
 * 默认固定字节提供者
 *
 * @version
 */
public class DefaultFixedBytesProvider implements FixedBytesProvider
{
	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] getFixedBytes()
	{
		byte[] fixedBytes = new byte[32];
		fixedBytes[0] = (byte) 0x5d;
		fixedBytes[1] = (byte) 0x2c;
		fixedBytes[2] = (byte) 0x27;
		fixedBytes[3] = (byte) 0x73;
		fixedBytes[4] = (byte) 0xa5;
		fixedBytes[5] = (byte) 0xc7;
		fixedBytes[6] = (byte) 0xb1;
		fixedBytes[7] = (byte) 0x92;
		fixedBytes[8] = (byte) 0x40;
		fixedBytes[9] = (byte) 0x24;
		fixedBytes[10] = (byte) 0x5d;
		fixedBytes[11] = (byte) 0xe2;
		fixedBytes[12] = (byte) 0x34;
		fixedBytes[13] = (byte) 0xf7;
		fixedBytes[14] = (byte) 0xb4;
		fixedBytes[15] = (byte) 0x1d;
		fixedBytes[16] = (byte) 0x5c;
		fixedBytes[17] = (byte) 0x8a;
		fixedBytes[18] = (byte) 0x35;
		fixedBytes[19] = (byte) 0x33;
		fixedBytes[20] = (byte) 0x3d;
		fixedBytes[21] = (byte) 0x7a;
		fixedBytes[22] = (byte) 0x2c;
		fixedBytes[23] = (byte) 0xba;
		fixedBytes[24] = (byte) 0x6d;
		fixedBytes[25] = (byte) 0xca;
		fixedBytes[26] = (byte) 0xe5;
		fixedBytes[27] = (byte) 0x55;
		fixedBytes[28] = (byte) 0x3d;
		fixedBytes[29] = (byte) 0x68;
		fixedBytes[30] = (byte) 0x56;
		fixedBytes[31] = (byte) 0xe2;

		return fixedBytes;
	}
}
