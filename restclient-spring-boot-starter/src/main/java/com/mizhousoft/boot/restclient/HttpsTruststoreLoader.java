package com.mizhousoft.boot.restclient;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.mizhousoft.commons.restclient.TruststoreLoader;

/**
 * Https信任证书加载器
 *
 * @version
 */
@Service
public class HttpsTruststoreLoader implements TruststoreLoader
{
	@Autowired
	private ResourceLoader resourceLoader;

	@Autowired
	private RestClientProperties properties;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public KeyStore loadTrustStore() throws GeneralSecurityException
	{
		try (InputStream instream = loadInputStream())
		{
			KeyStore keyStore = KeyStore.getInstance("JKS");
			keyStore.load(instream, properties.getPassword().toCharArray());

			return keyStore;
		}
		catch (IOException e)
		{
			throw new GeneralSecurityException("Load https trust store failed.");
		}
	}

	private InputStream loadInputStream() throws IOException
	{
		InputStream istream = null;
		if (!StringUtils.isBlank(properties.getPath()))
		{
			Resource resource = resourceLoader.getResource("classpath:" + properties.getPath());
			istream = resource.getInputStream();
		}

		if (null == istream)
		{
			istream = getClass().getClassLoader().getResourceAsStream("trust-keystore.jks");
		}

		return istream;
	}
}
