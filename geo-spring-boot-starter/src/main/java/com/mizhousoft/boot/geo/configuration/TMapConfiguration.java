package com.mizhousoft.boot.geo.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mizhousoft.boot.geo.properties.TMapProperties;
import com.mizhousoft.commons.restclient.service.RestClientService;
import com.mizhousoft.geo.DistrictSearchService;
import com.mizhousoft.geo.GEOCoderService;
import com.mizhousoft.geo.GEOProfile;
import com.mizhousoft.geo.tmap.coder.TMapGEOCoderServiceImpl;
import com.mizhousoft.geo.tmap.search.TMapDistrictSearchServiceImpl;

/**
 * TMapConfiguration
 *
 * @version
 */
@Configuration
public class TMapConfiguration
{
	@Autowired
	private TMapProperties tMapProperties;

	@Autowired
	private RestClientService restClientService;

	private GEOProfile profile;

	@Bean
	@ConditionalOnProperty("geo.tmap.app-key")
	public GEOCoderService getGEOCoderService()
	{
		GEOProfile profile = getGEOProfile();

		TMapGEOCoderServiceImpl geoCoderService = new TMapGEOCoderServiceImpl();
		geoCoderService.setProfile(profile);
		geoCoderService.setRestClientService(restClientService);

		return geoCoderService;
	}

	@Bean
	@ConditionalOnProperty("geo.tmap.app-key")
	public DistrictSearchService getDistrictSearchService()
	{
		GEOProfile profile = getGEOProfile();

		TMapDistrictSearchServiceImpl districtSearchService = new TMapDistrictSearchServiceImpl();
		districtSearchService.setProfile(profile);
		districtSearchService.setRestClientService(restClientService);

		return districtSearchService;
	}

	private synchronized GEOProfile getGEOProfile()
	{
		if (null == this.profile)
		{
			GEOProfile profile = new GEOProfile();
			profile.setAppKey(tMapProperties.getAppKey());

			this.profile = profile;
		}

		return this.profile;
	}
}
