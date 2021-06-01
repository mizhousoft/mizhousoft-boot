package com.mizhousoft.boot.ehcache;

import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.sf.ehcache.constructs.web.ShutdownListener;

/**
 * Ehcache配置服务
 *
 * @version
 */
@Configuration
@ConditionalOnClass(ShutdownListener.class)
public class EhcacheAutoConfiguration
{
	private static final Logger LOG = LoggerFactory.getLogger(EhcacheAutoConfiguration.class);

	@Bean
	public ServletListenerRegistrationBean<ServletContextListener> getShutdownListener()
	{
		ServletListenerRegistrationBean<ServletContextListener> registrationBean = new ServletListenerRegistrationBean<>();
		registrationBean.setListener(new ShutdownListener());
		registrationBean.setOrder(100);

		LOG.info("Register ehcache ShutdownListener successfully.");

		return registrationBean;
	}
}
