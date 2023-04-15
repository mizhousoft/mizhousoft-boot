package com.mizhousoft.boot.weixin.configuration;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import com.mizhousoft.boot.weixin.properties.WeixinMPProperties;
import com.mizhousoft.boot.weixin.properties.WeixinMiniProperties;
import com.mizhousoft.boot.weixin.properties.WeixinOpenProperties;
import com.mizhousoft.boot.weixin.properties.WeixinPayProperties;
import com.mizhousoft.commons.restclient.service.RestClientService;
import com.mizhousoft.weixin.common.WXException;
import com.mizhousoft.weixin.miniapp.config.MiniAppConfig;
import com.mizhousoft.weixin.miniapp.service.WxMiniAppService;
import com.mizhousoft.weixin.miniapp.service.impl.WxMiniAppServiceImpl;
import com.mizhousoft.weixin.mp.config.WxMpConfig;
import com.mizhousoft.weixin.mp.service.WxMpService;
import com.mizhousoft.weixin.mp.service.impl.WxMpServiceImpl;
import com.mizhousoft.weixin.open.config.WxOpenConfig;
import com.mizhousoft.weixin.open.service.WxOpenService;
import com.mizhousoft.weixin.open.service.impl.WxOpenServiceImpl;
import com.mizhousoft.weixin.payment.WxPayConfig;
import com.mizhousoft.weixin.payment.service.WxPaymentService;
import com.mizhousoft.weixin.payment.service.impl.WxPaymentServiceImpl;

/**
 * WeixinConfiguration
 *
 * @version
 */
@Configuration
public class WeixinConfiguration
{
	@Autowired
	private WeixinOpenProperties openProperties;

	@Autowired
	private WeixinMPProperties mpProperties;

	@Autowired
	private WeixinPayProperties payProperties;

	@Autowired
	private WeixinMiniProperties miniProperties;

	@Autowired
	private RestClientService restClientService;

	@Autowired
	private ResourceLoader resourceLoader;

	@Bean
	@ConditionalOnProperty("weixin.open.app-id")
	public WxOpenService getWxOpenService()
	{
		WxOpenConfig wxOpenConfig = new WxOpenConfig();
		wxOpenConfig.setAppId(openProperties.getAppId());
		wxOpenConfig.setAppSecret(openProperties.getAppSecret());

		WxOpenServiceImpl wxOpenService = new WxOpenServiceImpl();
		wxOpenService.setConfig(wxOpenConfig);
		wxOpenService.setRestClientService(restClientService);

		return wxOpenService;
	}

	@Bean
	@ConditionalOnProperty("weixin.miniapp.app-id")
	public WxMiniAppService getWxMiniAppService()
	{
		WxMiniAppServiceImpl wxMiniAppService = new WxMiniAppServiceImpl();

		MiniAppConfig config = new MiniAppConfig();
		config.setAppId(miniProperties.getAppId());
		config.setAppSecret(miniProperties.getAppSecret());
		config.setToken(miniProperties.getToken());
		config.setAesKey(miniProperties.getAesKey());

		wxMiniAppService.setConfig(config);
		wxMiniAppService.setRestClientService(restClientService);

		return wxMiniAppService;
	}

	@Bean
	@ConditionalOnProperty("weixin.mp.app-id")
	public WxMpService getWxMpService()
	{
		WxMpConfig config = new WxMpConfig();
		config.setAppId(mpProperties.getAppId());
		config.setAppSecret(mpProperties.getAppSecret());
		config.setToken(mpProperties.getToken());
		config.setAesKey(mpProperties.getAesKey());

		WxMpServiceImpl wxMpService = new WxMpServiceImpl();
		wxMpService.setRestClientService(restClientService);
		wxMpService.setConfig(config);

		return wxMpService;
	}

	@Bean
	@ConditionalOnProperty("weixin.pay.mch-id")
	public WxPaymentService getWxPaymentService() throws WXException, IOException
	{
		Resource resource = resourceLoader.getResource("classpath:" + payProperties.getPrivateKeyPath());
		String privKeyPath = resource.getFile().getAbsolutePath();

		Set<String> certPemPaths = new HashSet<>(10);
		for (String certPemFilePath : payProperties.getCertPemFilePaths())
		{
			resource = resourceLoader.getResource("classpath:" + certPemFilePath);
			String certPemPath = resource.getFile().getAbsolutePath();
			certPemPaths.add(certPemPath);
		}

		WxPayConfig config = new WxPayConfig();
		config.setMchId(payProperties.getMchId());
		config.setApiV3Key(payProperties.getApiV3Key());
		config.setCertSerialNumber(payProperties.getCertSerialNumber());
		config.setPrivateKeyPath(privKeyPath);
		config.setCertPemFilePaths(certPemPaths);
		config.setPayNotifyUrl(payProperties.getNotifyUrl());
		config.setRefundNotifyUrl(payProperties.getRefundNotifyUrl());

		WxPaymentServiceImpl wxPayService = new WxPaymentServiceImpl();
		wxPayService.init(config, restClientService);

		return wxPayService;
	}
}
