package com.mizhousoft.boot.authentication.configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.shiro.session.mgt.ExecutorServiceSessionValidationScheduler;
import org.apache.shiro.web.filter.authc.AnonymousFilter;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mizhousoft.boot.authentication.AccountSessionService;
import com.mizhousoft.boot.authentication.AuthenticationService;
import com.mizhousoft.boot.authentication.filter.CsrfFilter;
import com.mizhousoft.boot.authentication.filter.FirstLoginCheckFilter;
import com.mizhousoft.boot.authentication.filter.PasswordExpiredCheckFilter;
import com.mizhousoft.boot.authentication.filter.RefererFilter;
import com.mizhousoft.boot.authentication.filter.SecurityContextPersistenceFilter;
import com.mizhousoft.boot.authentication.filter.SecurityFrameworkFilter;
import com.mizhousoft.boot.authentication.filter.TwoFactorAuthenticationCheckFilter;
import com.mizhousoft.boot.authentication.filter.authc.AccountPasswordAuthenticationFilter;
import com.mizhousoft.boot.authentication.filter.authc.CustLogoutFilter;
import com.mizhousoft.boot.authentication.filter.authc.SessionAuthenticationFilter;
import com.mizhousoft.boot.authentication.filter.authz.AccessAuthorizationFilter;
import com.mizhousoft.boot.authentication.impl.AccountSessionServiceImpl;
import com.mizhousoft.boot.authentication.limiter.AuthFailureLimiter;
import com.mizhousoft.boot.authentication.limiter.impl.IPAddrAuthFailureLimiter;
import com.mizhousoft.boot.authentication.mgt.DefaultWebSecurityManager;
import com.mizhousoft.boot.authentication.realm.AuthenticationRealm;
import com.mizhousoft.boot.authentication.service.AccountAuthcService;
import com.mizhousoft.boot.authentication.service.ApplicationAuthenticationService;
import com.mizhousoft.boot.authentication.session.SecureSessionDAO;
import com.mizhousoft.boot.authentication.session.SecureSessionFactory;
import com.mizhousoft.boot.authentication.session.SecureWebSessionManager;

import jakarta.servlet.Filter;

/**
 * ShiroConfiguration
 *
 * @version
 */
@Configuration
public class ShiroConfiguration
{
	private static final Logger LOG = LoggerFactory.getLogger(ShiroConfiguration.class);

	private static final String LOGIN_URL = "/login.action";

	private static final String UNAUTHORIZED_URL = "/unauthorized.action";

	private static final String LOGOUT_URL = "/logout.action";

	@Autowired
	private AuthenticationProperties authenticationProperties;

	@Autowired
	private AuthenticationService authenticationService;

	@Autowired
	private ApplicationAuthenticationService applicationAuthService;

	@Autowired
	private ApplicationEventPublisher eventPublisher;

	private AuthFailureLimiter authFailureLimiter = new IPAddrAuthFailureLimiter(30);

	@Bean
	public SecureSessionDAO getSecureSessionDAO()
	{
		SecureSessionDAO sessionDAO = new SecureSessionDAO();
		return sessionDAO;
	}

	@Bean
	public SecureSessionFactory getSecureSessionFactory()
	{
		SecureSessionFactory sessionFactory = new SecureSessionFactory();
		return sessionFactory;
	}

	@Bean(destroyMethod = "destroy")
	public SecureWebSessionManager getSecureWebSessionManager(SecureSessionDAO sessionDAO, SecureSessionFactory sessionFactory)
	{
		SecureWebSessionManager sessionManager = new SecureWebSessionManager(authenticationProperties.isSecureMode());
		sessionManager.setSessionIdCookieEnabled(true);
		sessionManager.setSessionDAO(sessionDAO);
		sessionManager.setSessionFactory(sessionFactory);
		sessionManager.setGlobalSessionTimeout(1800000);
		sessionManager.setDeleteInvalidSessions(true);
		sessionManager.setSessionValidationSchedulerEnabled(true);

		ExecutorServiceSessionValidationScheduler sessionValidationScheduler = new ExecutorServiceSessionValidationScheduler();
		sessionValidationScheduler.setSessionManager(sessionManager);
		sessionManager.setSessionValidationScheduler(sessionValidationScheduler);

		return sessionManager;
	}

	@Bean
	public AccountSessionService getAccountSessionService(SecureSessionDAO sessionDAO)
	{
		AccountSessionServiceImpl accountSessionService = new AccountSessionServiceImpl(sessionDAO);

		return accountSessionService;
	}

	@Bean
	public AuthenticationRealm getAuthenticationRealm(AccountAuthcService accountAuthcService, AccountSessionService accountSessionService)
	{
		AuthenticationRealm authenticationRealm = new AuthenticationRealm(accountAuthcService, accountSessionService, authFailureLimiter,
		        authenticationProperties);

		return authenticationRealm;
	}

	@Bean
	public DefaultWebSecurityManager getDefaultWebSecurityManager(SecureWebSessionManager sessionManager,
	        AuthenticationRealm authenticationRealm)
	{
		DefaultWebSecurityManager webSecurityManager = new DefaultWebSecurityManager();
		webSecurityManager.setRememberMeManager(null);
		webSecurityManager.setSessionManager(sessionManager);
		webSecurityManager.setRealm(authenticationRealm);

		return webSecurityManager;
	}

	@Bean
	public DefaultFilterChainManager getDefaultFilterChainManager()
	{
		DefaultFilterChainManager filterChain = new DefaultFilterChainManager();

		SessionAuthenticationFilter sessionAuthenticationFilter = new SessionAuthenticationFilter();
		sessionAuthenticationFilter.setLoginUrl(LOGIN_URL);
		sessionAuthenticationFilter.setVerifyHost(authenticationProperties.isVerifyHost());

		LogoutFilter logoutFilter = new CustLogoutFilter(eventPublisher);
		logoutFilter.setRedirectUrl(LOGIN_URL);

		AccountPasswordAuthenticationFilter accountAuthenticationFilter = new AccountPasswordAuthenticationFilter(
		        authenticationProperties.isSecureMode());
		accountAuthenticationFilter.setLoginUrl(LOGIN_URL);
		accountAuthenticationFilter.setSuccessUrl("/");

		SecurityContextPersistenceFilter securityContextFilter = new SecurityContextPersistenceFilter();
		securityContextFilter.setLoginUrl(LOGIN_URL);
		securityContextFilter.setCheckAccessIpAddr(authenticationProperties.isVerifyHost());

		AnonymousFilter anonFilter = new AnonymousFilter();

		AccessAuthorizationFilter accessAuthorizationFilter = new AccessAuthorizationFilter();
		accessAuthorizationFilter.setAuthenticationService(authenticationService);
		accessAuthorizationFilter.setLoginUrl(LOGIN_URL);
		accessAuthorizationFilter.setUnauthorizedUrl(UNAUTHORIZED_URL);

		FirstLoginCheckFilter firstLoginFilter = new FirstLoginCheckFilter();
		firstLoginFilter.setLoginUrl(LOGIN_URL);

		PasswordExpiredCheckFilter passwordExpiredCheckFilter = new PasswordExpiredCheckFilter();
		passwordExpiredCheckFilter.setLoginUrl(LOGIN_URL);

		TwoFactorAuthenticationCheckFilter twoFactorAuthcCheckFilter = new TwoFactorAuthenticationCheckFilter();
		twoFactorAuthcCheckFilter.setLoginUrl(LOGIN_URL);

		String referers = authenticationProperties.getReferers();
		LOG.info("Application properties referers are {}.", referers);

		List<String> refererList = Arrays.asList(referers.split(","));
		RefererFilter refererFilter = new RefererFilter(LOGIN_URL, new HashSet<>(refererList));

		List<String> anonRequestPaths = applicationAuthService.getAnonRequestPaths();
		List<String> csrfExcludePaths = applicationAuthService.getCsrfExcludeRequestPaths();
		csrfExcludePaths.add(LOGIN_URL);
		csrfExcludePaths.addAll(anonRequestPaths);
		CsrfFilter csrfFilter = new CsrfFilter(LOGIN_URL, csrfExcludePaths);

		Map<String, Filter> filters = new HashMap<String, Filter>(10);
		filters.put("refererFilter", refererFilter);
		filters.put("anon", anonFilter);
		filters.put("logout", logoutFilter);
		filters.put("authc", accountAuthenticationFilter);
		filters.put("authz", accessAuthorizationFilter);
		filters.put("securityContextFilter", securityContextFilter);
		filters.put("sessionAuthc", sessionAuthenticationFilter);
		filters.put("firstLoginCheckFilter", firstLoginFilter);
		filters.put("passwordExpiredCheckFilter", passwordExpiredCheckFilter);
		filters.put("twoFactorAuthcCheckFilter", twoFactorAuthcCheckFilter);
		filters.put("csrfFilter", csrfFilter);
		filterChain.setFilters(filters);

		buildFilterChainManager(filterChain);

		return filterChain;
	}

	@Bean
	public PathMatchingFilterChainResolver getPathMatchingFilterChainResolver(DefaultFilterChainManager filterChainManager)
	{
		PathMatchingFilterChainResolver filterChainResolver = new PathMatchingFilterChainResolver();
		filterChainResolver.setFilterChainManager(filterChainManager);

		return filterChainResolver;
	}

	public DefaultFilterChainManager buildFilterChainManager(DefaultFilterChainManager filterChain)
	{
		List<String> authzPaths = authenticationService.getAuthzRequestPaths();
		for (String authzPath : authzPaths)
		{
			filterChain.addToChain(authzPath, "refererFilter");
			filterChain.addToChain(authzPath, "authc");
			filterChain.addToChain(authzPath, "authz");
			filterChain.addToChain(authzPath, "securityContextFilter");
			filterChain.addToChain(authzPath, "csrfFilter");
			filterChain.addToChain(authzPath, "sessionAuthc");
			filterChain.addToChain(authzPath, "firstLoginCheckFilter");
			filterChain.addToChain(authzPath, "passwordExpiredCheckFilter");

			if (authenticationProperties.isTwoFactorAuthcEnable())
			{
				filterChain.addToChain(authzPath, "twoFactorAuthcCheckFilter");
			}
		}

		List<String> authcPaths = authenticationService.getAuthcRequestPaths();
		for (String authcPath : authcPaths)
		{
			filterChain.addToChain(authcPath, "refererFilter");
			filterChain.addToChain(authcPath, "authc");
			filterChain.addToChain(authcPath, "securityContextFilter");
			filterChain.addToChain(authcPath, "csrfFilter");
			filterChain.addToChain(authcPath, "sessionAuthc");
			filterChain.addToChain(authcPath, "firstLoginCheckFilter");
			filterChain.addToChain(authcPath, "passwordExpiredCheckFilter");

			if (authenticationProperties.isTwoFactorAuthcEnable())
			{
				filterChain.addToChain(authcPath, "twoFactorAuthcCheckFilter");
			}
		}

		List<String> onlyAuthcPaths = authenticationService.getLoginAuditRequestPaths();
		for (String onlyAuthcPath : onlyAuthcPaths)
		{
			filterChain.addToChain(onlyAuthcPath, "refererFilter");
			filterChain.addToChain(onlyAuthcPath, "authc");
			filterChain.addToChain(onlyAuthcPath, "securityContextFilter");
			filterChain.addToChain(onlyAuthcPath, "csrfFilter");
			filterChain.addToChain(onlyAuthcPath, "sessionAuthc");
		}

		filterChain.addToChain(LOGIN_URL, "refererFilter");
		filterChain.addToChain(LOGIN_URL, "authc");

		filterChain.addToChain(LOGOUT_URL, "refererFilter");
		filterChain.addToChain(LOGOUT_URL, "authc");
		filterChain.addToChain(LOGOUT_URL, "securityContextFilter");
		filterChain.addToChain(LOGOUT_URL, "csrfFilter");
		filterChain.addToChain(LOGOUT_URL, "logout");

		List<String> anonRequestPaths = applicationAuthService.getAnonRequestPaths();
		for (String anonRequestPath : anonRequestPaths)
		{
			filterChain.addToChain(anonRequestPath, "anon");
		}

		filterChain.addToChain("/**", "refererFilter");
		filterChain.addToChain("/**", "authc");
		filterChain.addToChain("/**", "authz");
		filterChain.addToChain("/**", "securityContextFilter");
		filterChain.addToChain("/**", "csrfFilter");
		filterChain.addToChain("/**", "sessionAuthc");
		filterChain.addToChain("/**", "firstLoginCheckFilter");
		filterChain.addToChain("/**", "passwordExpiredCheckFilter");

		if (authenticationProperties.isTwoFactorAuthcEnable())
		{
			filterChain.addToChain("/**", "twoFactorAuthcCheckFilter");
		}

		return filterChain;
	}

	@Bean
	public FilterRegistrationBean<Filter> filterRegistrationBean()
	{
		FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<Filter>();
		SecurityFrameworkFilter filter = new SecurityFrameworkFilter();
		registrationBean.setFilter(filter);

		Map<String, String> requestPathMap = authenticationService.getNonUpdateAccessTimeRequestPaths();
		filter.setNonUpdateAccessTimeURIMap(requestPathMap);

		List<String> urlPatterns = new ArrayList<String>(1);
		urlPatterns.add("*.action");
		registrationBean.setUrlPatterns(urlPatterns);

		return registrationBean;
	}
}
