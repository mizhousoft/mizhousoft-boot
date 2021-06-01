package com.mizhousoft.boot.crypto.loader;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mizhousoft.boot.crypto.descriptor.WorkSecretDescriptor;
import com.mizhousoft.commons.crypto.CryptoException;
import com.mizhousoft.commons.json.JSONException;
import com.mizhousoft.commons.json.JSONUtils;
import com.mizhousoft.commons.lang.CharEncoding;

/**
 * 工作密钥描述符加载器
 *
 * @version
 */
public class WorkSecretDescriptorLoader
{
	public static Map<String, WorkSecretDescriptor> load(File workFile) throws CryptoException
	{
		try
		{
			if (!workFile.exists())
			{
				return new HashMap<String, WorkSecretDescriptor>(5);
			}

			String data = FileUtils.readFileToString(workFile, CharEncoding.UTF8);
			Map<String, WorkSecretDescriptor> map = JSONUtils.parse(data, new TypeReference<Map<String, WorkSecretDescriptor>>()
			{
			});

			return map;
		}
		catch (IOException e)
		{
			throw new CryptoException("Read work key file failed.", e);
		}
		catch (JSONException e)
		{
			throw new CryptoException("Read work key file failed.", e);
		}
	}
}
