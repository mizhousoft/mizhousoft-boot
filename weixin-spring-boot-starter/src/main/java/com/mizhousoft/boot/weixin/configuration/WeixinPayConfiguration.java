package com.mizhousoft.boot.weixin.configuration;

import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import com.mizhousoft.boot.weixin.properties.WeixinPayListProperties;
import com.mizhousoft.commons.restclient.service.RestClientService;
import com.mizhousoft.weixin.certificate.CertificateProvider;
import com.mizhousoft.weixin.certificate.impl.CertificateProviderImpl;
import com.mizhousoft.weixin.cipher.WxPayVerifier;
import com.mizhousoft.weixin.cipher.impl.RSAPrivacyDecryptor;
import com.mizhousoft.weixin.cipher.impl.RSAPrivacyEncryptor;
import com.mizhousoft.weixin.cipher.impl.WxPayRASVerifier;
import com.mizhousoft.weixin.common.WXException;
import com.mizhousoft.weixin.credential.WxPayCredential;
import com.mizhousoft.weixin.payment.WxPayConfig;
import com.mizhousoft.weixin.payment.service.WxPaymentService;
import com.mizhousoft.weixin.payment.service.impl.WxPaymentServiceImpl;
import com.mizhousoft.weixin.transfer.service.MerchantTransferService;
import com.mizhousoft.weixin.transfer.service.impl.MerchantTransferServiceImpl;
import com.mizhousoft.weixin.util.PemUtils;

/**
 * WeixinConfiguration
 *
 * @version
 */
@Configuration
public class WeixinPayConfiguration
{
	@Autowired
	private WeixinPayListProperties payProperties;

	@Autowired
	private RestClientService restClientService;

	@Autowired
	private ResourceLoader resourceLoader;

	private WxPayConfig wxPayConfig;

	@Bean
	@ConditionalOnProperty("weixin.pay.mch-id")
	public WxPaymentService getWxPaymentService(WxPayCredential credential, WxPayVerifier verifier) throws WXException, IOException
	{
		WxPayConfig config = getWxPayConfig();

		WxPaymentServiceImpl wxPayService = new WxPaymentServiceImpl();
		wxPayService.setRestClientService(restClientService);
		wxPayService.setPayConfig(config);
		wxPayService.setVerifier(verifier);

		return wxPayService;
	}

	@Bean
	@ConditionalOnProperty("weixin.pay.mch-id")
	public MerchantTransferService getMerchantTransferService(WxPayCredential credential, WxPayVerifier verifier,
	        CertificateProvider certificateProvider) throws WXException, IOException
	{
		WxPayConfig config = getWxPayConfig();

		PrivateKey privateKey = PemUtils.loadPrivateKeyFromPath(config.getPrivateKeyPath());
		RSAPrivacyDecryptor privacyDecryptor = new RSAPrivacyDecryptor(privateKey);

		String serialNumber = config.getCertSerialNumber();
		X509Certificate certificate = certificateProvider.getCertificate(serialNumber);
		RSAPrivacyEncryptor privacyEncryptor = new RSAPrivacyEncryptor(certificate.getPublicKey(), serialNumber);

		MerchantTransferServiceImpl merchantTransferService = new MerchantTransferServiceImpl();
		merchantTransferService.setRestClientService(restClientService);
		merchantTransferService.setCredential(credential);
		merchantTransferService.setVerifier(verifier);
		merchantTransferService.setDecryptor(privacyDecryptor);
		merchantTransferService.setEncryptor(privacyEncryptor);

		return merchantTransferService;
	}

	@Bean
	@ConditionalOnProperty("weixin.pay.mch-id")
	public WxPayVerifier getWxPayVerifier(CertificateProvider certificateProvider) throws WXException, IOException
	{
		WxPayVerifier payVerifier = new WxPayRASVerifier(certificateProvider);

		return payVerifier;
	}

	private synchronized WxPayConfig getWxPayConfig() throws IOException, WXException
	{
		if (null == wxPayConfig)
		{
			List<X509Certificate> certificates = new ArrayList<>(10);
			for (String certPemFilePath : payProperties.getCertPemFilePaths())
			{
				Resource resource = resourceLoader.getResource("classpath:" + certPemFilePath);
				String certPemPath = resource.getFile().getAbsolutePath();

				X509Certificate certificate = PemUtils.loadX509FromPath(certPemPath);
				certificates.add(certificate);
			}

			CertificateProvider certificateProvider = new CertificateProviderImpl(certificates);

			Resource resource = resourceLoader.getResource("classpath:" + payProperties.getPrivateKeyPath());
			String privKeyPath = resource.getFile().getAbsolutePath();
			PrivateKey privateKey = PemUtils.loadPrivateKeyFromPath(privKeyPath);

			String serialNumber = payProperties.getCertSerialNumber();
			X509Certificate certificate = certificateProvider.getCertificate(serialNumber);
			PublicKey publicKey = certificate.getPublicKey();

			WxPayConfig config = new WxPayConfig();
			config.setMchId(payProperties.getMchId());
			config.setApiV3Key(payProperties.getApiV3Key());
			config.setCertSerialNumber(payProperties.getCertSerialNumber());
			config.setPrivateKey(privateKey);
			config.setPublicKey(publicKey);
			config.setCertProvider(certificateProvider);
			config.setPayNotifyUrl(payProperties.getNotifyUrl());
			config.setRefundNotifyUrl(payProperties.getRefundNotifyUrl());

			this.wxPayConfig = config;
		}

		return this.wxPayConfig;
	}
}
