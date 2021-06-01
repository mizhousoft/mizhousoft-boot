package com.mizhousoft.boot.crypto.impl;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mizhousoft.commons.crypto.AESEncryptor;
import com.mizhousoft.commons.crypto.CryptoException;
import com.mizhousoft.commons.crypto.digest.HmacSHA256Digest;
import com.mizhousoft.commons.lang.CharEncoding;
import com.mizhousoft.commons.lang.HexUtils;
import com.mizhousoft.boot.crypto.SecretManager;
import com.mizhousoft.boot.crypto.descriptor.RootSecretDescriptor;
import com.mizhousoft.boot.crypto.descriptor.WorkSecretDescriptor;
import com.mizhousoft.boot.crypto.domain.WorkSecret;
import com.mizhousoft.boot.crypto.generator.RootSecretGenerator;
import com.mizhousoft.boot.crypto.loader.RootSecretDescriptorLoader;
import com.mizhousoft.boot.crypto.loader.WorkSecretDescriptorLoader;

/**
 * 秘钥管理器
 *
 * @version
 */
class SecretManagerImpl implements SecretManager
{
	private static final Logger LOG = LoggerFactory.getLogger(SecretManagerImpl.class);

	private Map<String, WorkSecret> secretMap = new HashMap<String, WorkSecret>(5);

	private boolean loaded;

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public byte[] getSecret(String keyId) throws CryptoException
	{
		WorkSecret workSecret = secretMap.get(keyId);
		if (null == workSecret)
		{
			throw new CryptoException(keyId + " secret not found.");
		}

		return workSecret.getSecret();
	}

	public synchronized void loadSecretFile(String rsecretPath, String workFilePath) throws CryptoException
	{
		if (loaded)
		{
			LOG.warn("Secret already load successfully.");
			return;
		}

		File rootDir = new File(rsecretPath);
		RootSecretDescriptor rootDescriptor = RootSecretDescriptorLoader.load(rootDir);

		byte[] rootKey = RootSecretGenerator.gen(rootDescriptor);

		File workFile = new File(workFilePath);
		Map<String, WorkSecretDescriptor> map = WorkSecretDescriptorLoader.load(workFile);

		try
		{
			Iterator<Entry<String, WorkSecretDescriptor>> iter = map.entrySet().iterator();
			while (iter.hasNext())
			{
				Entry<String, WorkSecretDescriptor> entry = iter.next();

				String workKeyId = entry.getKey();
				WorkSecretDescriptor workDescriptor = map.get(workKeyId);

				String value = workDescriptor.getData();
				String[] values = value.split(":");
				byte[] macKeyBytes = HexUtils.decodeHex(values[0]);
				byte[] encWorkKey = HexUtils.decodeHex(values[1]);
				byte[] workKey = AESEncryptor.decrypt(encWorkKey, rootKey);

				byte[] idbytes = workKeyId.getBytes(CharEncoding.UTF8);
				byte[] signBytes = new byte[idbytes.length + workKey.length];
				System.arraycopy(idbytes, 0, signBytes, 0, idbytes.length);
				System.arraycopy(workKey, 0, signBytes, idbytes.length - 1, workKey.length);

				byte[] macBytes = HmacSHA256Digest.hash(macKeyBytes, signBytes);
				String macText = HexUtils.encodeHexString(macBytes, false);

				String sign = workDescriptor.getSign();
				if (!StringUtils.equals(sign, macText))
				{
					throw new CryptoException(workKeyId + " secret is wrong.");
				}

				WorkSecret secret = new WorkSecret();
				secret.setDescriptor(workDescriptor);
				secret.setSecret(workKey);

				secretMap.put(workKeyId, secret);
			}

			loaded = true;
			LOG.info("Secret load successfully.");
		}
		catch (DecoderException e)
		{
			throw new CryptoException("Secret decode failed.");
		}
	}
}
