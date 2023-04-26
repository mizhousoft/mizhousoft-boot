package com.mizhousoft.boot.restclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mizhousoft.commons.restclient.TruststoreLoader;
import com.mizhousoft.commons.restclient.service.RestClientService;
import com.mizhousoft.commons.restclient.service.impl.HttpsRestClientServiceImpl;

/**
 * 配置
 *
 * @version
 */
@Configuration
public class RestClientConfiguration
{
	@Autowired
	private PoolProperties poolProperties;

	@Bean
	public RestClientService getRestClientService(TruststoreLoader truststoreLoader)
	{
		TruststoreLoader[] truststoreLoaders = { truststoreLoader };

		HttpsRestClientServiceImpl restClientService = new HttpsRestClientServiceImpl();
		restClientService.setTruststoreLoaders(truststoreLoaders);

		restClientService.init(poolProperties.getConnectTimeout(), poolProperties.getReadTimeout());

		return restClientService;
	}
}
