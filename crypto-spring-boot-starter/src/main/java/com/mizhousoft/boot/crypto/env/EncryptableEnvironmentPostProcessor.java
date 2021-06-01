package com.mizhousoft.boot.crypto.env;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

import com.mizhousoft.boot.crypto.CryptoService;
import com.mizhousoft.boot.crypto.builder.CryptoServiceBuilder;
import com.mizhousoft.commons.crypto.CryptoException;
import com.mizhousoft.commons.data.NestedRuntimeException;

/**
 * 默认的环境变量后置处理器
 *
 * @version
 */
public final class EncryptableEnvironmentPostProcessor implements EnvironmentPostProcessor
{
	private static final Logger LOG = LoggerFactory.getLogger(EncryptableEnvironmentPostProcessor.class);

	private static final String CLASSPATH_PREFIX = "classpath:";

	private static final String ROOT_SECRET_PATH_KEY = "crypto.root.secret.path";

	private static final String WORK_SECRET_FILE_PATH_KEY = "crypto.work.secret.file.path";

	private static final String ENCRYPT_PREFIX = "ENC:";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application)
	{
		MutablePropertySources mutablePropertySources = environment.getPropertySources();
		String[] activeProfiles = environment.getActiveProfiles();

		PropertySource<?> defaultProfileSource = getDefaultProfileSource(mutablePropertySources);
		List<PropertySource<?>> activeProfileSources = getActiveProfileSouces(mutablePropertySources, activeProfiles);

		if (null != defaultProfileSource)
		{
			String rootPath = getSecretPath(ROOT_SECRET_PATH_KEY, defaultProfileSource);
			String workFilePath = getSecretPath(WORK_SECRET_FILE_PATH_KEY, defaultProfileSource);

			CryptoService cryptoService = CryptoServiceBuilder.build(rootPath, workFilePath);

			PropertySource<?> newPropertySource = rebuildEncryptablePropertySource(defaultProfileSource, cryptoService);
			environment.getPropertySources().replace(newPropertySource.getName(), newPropertySource);

			activeProfileSources.forEach(activeProfileSource -> {
				PropertySource<?> newPs = rebuildEncryptablePropertySource(activeProfileSource, cryptoService);
				environment.getPropertySources().replace(newPs.getName(), newPs);
			});
		}

		LOG.info("Process encryptable environment successfully.");
	}

	private PropertySource<?> rebuildEncryptablePropertySource(PropertySource<?> profileSource, CryptoService cryptoService)
	{
		MapPropertySource mapPropertySource = null;
		if (profileSource instanceof MapPropertySource)
		{
			mapPropertySource = (MapPropertySource) profileSource;
		}
		else
		{
			throw new NestedRuntimeException(profileSource.getClass().getName() + " is not MapPropertySource.");
		}

		Map<String, Object> sourceMap = mapPropertySource.getSource();

		Map<String, Object> destMap = new HashedMap<>(sourceMap);
		OriginTrackedMapPropertySource newPropertySource = new OriginTrackedMapPropertySource(mapPropertySource.getName(), destMap);

		Iterator<Entry<String, Object>> iter = destMap.entrySet().iterator();
		while (iter.hasNext())
		{
			Entry<String, Object> entry = iter.next();
			String fieldKey = entry.getKey();
			String fieldValue = entry.getValue().toString();

			if (isEncryptString(fieldValue))
			{
				String cipherText = getEncryptString(fieldValue);

				String plainText = decryptToString(cipherText, fieldKey, cryptoService);
				destMap.put(fieldKey, plainText);
			}
		}

		return newPropertySource;
	}

	private PropertySource<?> getDefaultProfileSource(MutablePropertySources mutablePropertySources)
	{
		Iterator<PropertySource<?>> iter = mutablePropertySources.iterator();
		while (iter.hasNext())
		{
			PropertySource<?> ps = iter.next();
			if (!(ps instanceof MapPropertySource))
			{
				continue;
			}

			// 获取默认的配置
			if (ps.containsProperty(ROOT_SECRET_PATH_KEY) && ps.containsProperty(WORK_SECRET_FILE_PATH_KEY))
			{
				return ps;
			}
		}

		return null;
	}

	private List<PropertySource<?>> getActiveProfileSouces(MutablePropertySources mutablePropertySources, String[] activeProfiles)
	{
		List<String> profiles = new ArrayList<>(5);
		if (null != activeProfiles)
		{
			for (String activeProfile : activeProfiles)
			{
				profiles.add("application-" + activeProfile + ".properties");
			}
		}

		List<PropertySource<?>> propertySources = new ArrayList<>(5);

		Iterator<PropertySource<?>> iter = mutablePropertySources.iterator();
		while (iter.hasNext())
		{
			PropertySource<?> ps = iter.next();
			String name = ps.getName();

			for (String profile : profiles)
			{
				if (name.contains(profile))
				{
					propertySources.add(ps);
				}
			}
		}

		return propertySources;
	}

	private String getSecretPath(String secretId, PropertySource<?> propertySource)
	{
		String rootPath = (String) propertySource.getProperty(secretId);
		if (StringUtils.startsWith(rootPath, CLASSPATH_PREFIX))
		{
			rootPath = rootPath.substring(CLASSPATH_PREFIX.length());

			URL url = this.getClass().getClassLoader().getResource(rootPath);
			if (null == url)
			{
				throw new NestedRuntimeException(secretId + " does not exist.");
			}
			else
			{
				rootPath = url.getPath();
			}
		}

		return rootPath;
	}

	private boolean isEncryptString(String data)
	{
		return StringUtils.startsWith(data, ENCRYPT_PREFIX);
	}

	private String getEncryptString(String data)
	{
		return StringUtils.substring(data, ENCRYPT_PREFIX.length());
	}

	private String decryptToString(String data, String secretId, CryptoService cryptoService)
	{
		if (StringUtils.isBlank(data))
		{
			return data;
		}

		try
		{
			String plainText = cryptoService.decryptToString(data, secretId);
			return plainText;
		}
		catch (CryptoException e)
		{
			throw new NestedRuntimeException("Decrypt data failed, secret id is " + secretId);
		}
	}
}
