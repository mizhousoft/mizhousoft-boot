package com.mizhousoft.boot.crypto.builder;

import com.mizhousoft.boot.crypto.CryptoService;
import com.mizhousoft.boot.crypto.impl.CryptoServiceImpl;
import com.mizhousoft.commons.crypto.CryptoException;
import com.mizhousoft.commons.data.NestedRuntimeException;

/**
 * 加解密服务构建器
 *
 * @version
 */
public abstract class CryptoServiceBuilder
{
	public static CryptoService build(String rsecretPath, String workFilePath)
	{
		CryptoServiceImpl cryptoService = new CryptoServiceImpl();

		try
		{
			cryptoService.init(rsecretPath, workFilePath);
		}
		catch (CryptoException e)
		{
			throw new NestedRuntimeException("Build crypto service failed.", e);
		}

		return cryptoService;
	}
}
