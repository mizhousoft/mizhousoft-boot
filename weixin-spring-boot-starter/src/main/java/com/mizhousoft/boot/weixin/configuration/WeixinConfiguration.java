package com.mizhousoft.boot.weixin.configuration;

import java.io.IOException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
import com.mizhousoft.weixin.certificate.CertificateProvider;
import com.mizhousoft.weixin.certificate.impl.CertificateProviderImpl;
import com.mizhousoft.weixin.cipher.WxPayVerifier;
import com.mizhousoft.weixin.cipher.impl.WxPayRASVerifier;
import com.mizhousoft.weixin.common.WXException;
import com.mizhousoft.weixin.credential.WxPayCredential;
import com.mizhousoft.weixin.credential.impl.WxPayRASCredential;
import com.mizhousoft.weixin.miniapp.config.MiniAppConfig;
import com.mizhousoft.weixin.miniapp.service.WxMiniAppService;
import com.mizhousoft.weixin.miniapp.service.impl.WxMiniAppServiceImpl;
import com.mizhousoft.weixin.mp.config.WxMpConfig;
import com.mizhousoft.weixin.mp.service.WxMpMessageHandler;
import com.mizhousoft.weixin.mp.service.WxMpMessageRouter;
import com.mizhousoft.weixin.mp.service.WxMpService;
import com.mizhousoft.weixin.mp.service.impl.WxMpMessageRouterImpl;
import com.mizhousoft.weixin.mp.service.impl.WxMpServiceImpl;
import com.mizhousoft.weixin.open.config.WxOpenConfig;
import com.mizhousoft.weixin.open.service.WxOpenService;
import com.mizhousoft.weixin.open.service.impl.WxOpenServiceImpl;
import com.mizhousoft.weixin.payment.WxPayConfig;
import com.mizhousoft.weixin.payment.service.WxPaymentService;
import com.mizhousoft.weixin.payment.service.impl.WxPaymentServiceImpl;
import com.mizhousoft.weixin.util.PemUtils;

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

	private WxPayConfig wxPayConfig;

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
	@ConditionalOnProperty("weixin.mp.app-id")
	public WxMpMessageRouter getWxMpMessageRouter(List<WxMpMessageHandler> msgHandlers)
	{
		WxMpMessageRouterImpl router = new WxMpMessageRouterImpl(msgHandlers);

		return router;
	}

	@Bean
	@ConditionalOnProperty("weixin.pay.mch-id")
	public WxPaymentService getWxPaymentService(WxPayCredential credential, WxPayVerifier verifier) throws WXException, IOException
	{
		WxPaymentServiceImpl wxPayService = new WxPaymentServiceImpl();
		wxPayService.setRestClientService(restClientService);
		wxPayService.setCredential(credential);
		wxPayService.setVerifier(verifier);

		return wxPayService;
	}

	@Bean
	@ConditionalOnProperty("weixin.pay.mch-id")
	public WxPayCredential getWxPayCredential() throws WXException, IOException
	{
		WxPayConfig config = getWxPayConfig();

		PrivateKey privateKey = PemUtils.loadPrivateKeyFromPath(config.getPrivateKeyPath());
		WxPayRASCredential wxPayCredential = new WxPayRASCredential(config, privateKey);

		return wxPayCredential;
	}

	@Bean
	@ConditionalOnProperty("weixin.pay.mch-id")
	public synchronized WxPayVerifier getWxPayVerifier(CertificateProvider certificateProvider) throws WXException, IOException
	{
		WxPayVerifier payVerifier = new WxPayRASVerifier(certificateProvider);

		return payVerifier;
	}

	@Bean
	@ConditionalOnProperty("weixin.pay.mch-id")
	public CertificateProvider getCertificateProvider() throws WXException, IOException
	{
		WxPayConfig config = getWxPayConfig();

		List<X509Certificate> certificates = new ArrayList<>(10);
		Set<String> certPemFilePaths = config.getCertPemFilePaths();
		for (String certPemFilePath : certPemFilePaths)
		{
			X509Certificate certificate = PemUtils.loadX509FromPath(certPemFilePath);
			certificates.add(certificate);
		}

		CertificateProvider certificateProvider = new CertificateProviderImpl(certificates);

		return certificateProvider;
	}

	private synchronized WxPayConfig getWxPayConfig() throws IOException
	{
		if (null == wxPayConfig)
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

			this.wxPayConfig = config;
		}

		return this.wxPayConfig;
	}
}
