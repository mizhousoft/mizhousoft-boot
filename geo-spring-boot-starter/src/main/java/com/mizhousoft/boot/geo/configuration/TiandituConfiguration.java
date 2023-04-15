package com.mizhousoft.boot.geo.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mizhousoft.boot.geo.properties.TiandituProperties;
import com.mizhousoft.commons.restclient.service.RestClientService;
import com.mizhousoft.geo.GEOCoder;
import com.mizhousoft.geo.GEOProfile;
import com.mizhousoft.geo.tianditu.coder.TiandituGEOCoder;

/**
 * WeixinConfiguration
 *
 * @version
 */
@Configuration
public class TiandituConfiguration
{
	@Autowired
	private TiandituProperties tdtProperties;

	@Autowired
	private RestClientService restClientService;

	@Bean
	@ConditionalOnProperty("geo.tianditu.app-key")
	public GEOCoder getGEOCoder()
	{
		GEOProfile profile = new GEOProfile();
		profile.setAppKey(tdtProperties.getAppKey());

		TiandituGEOCoder geoCoder = new TiandituGEOCoder();
		geoCoder.setProfile(profile);
		geoCoder.setRestClientService(restClientService);

		return geoCoder;
	}
}
