package com.mizhousoft.boot.authentication.configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.session.mgt.ExecutorServiceSessionValidationScheduler;
import org.apache.shiro.web.filter.authc.AnonymousFilter;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mizhousoft.boot.authentication.AccountSessionService;
import com.mizhousoft.boot.authentication.AuthenticationFacadeService;
import com.mizhousoft.boot.authentication.filter.FirstLoginCheckFilter;
import com.mizhousoft.boot.authentication.filter.PasswordExpiredCheckFilter;
import com.mizhousoft.boot.authentication.filter.SecurityContextPersistenceFilter;
import com.mizhousoft.boot.authentication.filter.SecurityFrameworkFilter;
import com.mizhousoft.boot.authentication.filter.TwoFactorAuthenticationCheckFilter;
import com.mizhousoft.boot.authentication.filter.authc.AccountPasswordAuthenticationFilter;
import com.mizhousoft.boot.authentication.filter.authc.CustLogoutFilter;
import com.mizhousoft.boot.authentication.filter.authc.SessionAuthenticationFilter;
import com.mizhousoft.boot.authentication.filter.authz.AccessAuthorizationFilter;
import com.mizhousoft.boot.authentication.impl.AccountSessionServiceImpl;
import com.mizhousoft.boot.authentication.mgt.DefaultWebSecurityManager;
import com.mizhousoft.boot.authentication.realm.AuthenticationRealm;
import com.mizhousoft.boot.authentication.service.AccountAuthcService;
import com.mizhousoft.boot.authentication.session.SecureSessionDAO;
import com.mizhousoft.boot.authentication.session.SecureSessionFactory;
import com.mizhousoft.boot.authentication.session.SecureWebSessionManager;

/**
 * ShiroConfiguration
 *
 * @version
 */
@Configuration
public class ShiroConfiguration
{
	private static final String LOGIN_URL = "/login.action";

	private static final String UNAUTHORIZED_URL = "/unauthorized.action";

	private static final String LOGOUT_URL = "/logout.action";

	@Autowired
	private AuthenticationProperties authenticationProperties;

	@Autowired
	private AuthenticationFacadeService authenticationFacadeService;

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
		SecureWebSessionManager sessionManager = new SecureWebSessionManager();
		sessionManager.setSessionIdCookieEnabled(true);
		sessionManager.setSessionDAO(sessionDAO);
		sessionManager.setSessionFactory(sessionFactory);
		sessionManager.setGlobalSessionTimeout(1800000);
		sessionManager.setDeleteInvalidSessions(true);
		sessionManager.setSessionValidationSchedulerEnabled(true);

		ExecutorServiceSessionValidationScheduler sessionValidationScheduler = new ExecutorServiceSessionValidationScheduler();
		sessionValidationScheduler.setInterval(1800000);
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
		AuthenticationRealm authenticationRealm = new AuthenticationRealm(accountAuthcService, accountSessionService,
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

		LogoutFilter logoutFilter = new CustLogoutFilter();
		logoutFilter.setRedirectUrl(LOGIN_URL);

		AccountPasswordAuthenticationFilter accountAuthenticationFilter = new AccountPasswordAuthenticationFilter();
		accountAuthenticationFilter.setLoginUrl(LOGIN_URL);
		accountAuthenticationFilter.setSuccessUrl("/");

		SecurityContextPersistenceFilter securityContextFilter = new SecurityContextPersistenceFilter();
		securityContextFilter.setLoginUrl(LOGIN_URL);
		securityContextFilter.setCheckAccessIpAddr(authenticationProperties.isVerifyHost());

		AnonymousFilter anonFilter = new AnonymousFilter();

		AccessAuthorizationFilter accessAuthorizationFilter = new AccessAuthorizationFilter();
		accessAuthorizationFilter.setAuthenticationFacadeService(authenticationFacadeService);
		accessAuthorizationFilter.setLoginUrl(LOGIN_URL);
		accessAuthorizationFilter.setUnauthorizedUrl(UNAUTHORIZED_URL);

		FirstLoginCheckFilter firstLoginFilter = new FirstLoginCheckFilter();
		firstLoginFilter.setLoginUrl(LOGIN_URL);

		PasswordExpiredCheckFilter passwordExpiredCheckFilter = new PasswordExpiredCheckFilter();
		passwordExpiredCheckFilter.setLoginUrl(LOGIN_URL);

		TwoFactorAuthenticationCheckFilter twoFactorAuthcCheckFilter = new TwoFactorAuthenticationCheckFilter();
		twoFactorAuthcCheckFilter.setLoginUrl(LOGIN_URL);

		Map<String, Filter> filters = new HashMap<String, Filter>(10);
		filters.put("anon", anonFilter);
		filters.put("logout", logoutFilter);
		filters.put("authc", accountAuthenticationFilter);
		filters.put("authz", accessAuthorizationFilter);
		filters.put("securityContextFilter", securityContextFilter);
		filters.put("sessionAuthc", sessionAuthenticationFilter);
		filters.put("firstLoginCheckFilter", firstLoginFilter);
		filters.put("passwordExpiredCheckFilter", passwordExpiredCheckFilter);
		filters.put("twoFactorAuthcCheckFilter", twoFactorAuthcCheckFilter);
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
		List<String> authzPaths = authenticationFacadeService.queryAuthzRequestPaths();
		for (String authzPath : authzPaths)
		{
			filterChain.addToChain(authzPath, "authc");
			filterChain.addToChain(authzPath, "authz");
			filterChain.addToChain(authzPath, "securityContextFilter");
			filterChain.addToChain(authzPath, "sessionAuthc");
			filterChain.addToChain(authzPath, "firstLoginCheckFilter");
			filterChain.addToChain(authzPath, "passwordExpiredCheckFilter");

			if (authenticationProperties.isTwoFactorAuthcEnable())
			{
				filterChain.addToChain(authzPath, "twoFactorAuthcCheckFilter");
			}
		}

		List<String> authcPaths = authenticationFacadeService.queryAuthcRequestPaths();
		for (String authcPath : authcPaths)
		{
			filterChain.addToChain(authcPath, "authc");
			filterChain.addToChain(authcPath, "securityContextFilter");
			filterChain.addToChain(authcPath, "sessionAuthc");
			filterChain.addToChain(authcPath, "firstLoginCheckFilter");
			filterChain.addToChain(authcPath, "passwordExpiredCheckFilter");

			if (authenticationProperties.isTwoFactorAuthcEnable())
			{
				filterChain.addToChain(authcPath, "twoFactorAuthcCheckFilter");
			}
		}

		List<String> onlyAuthcPaths = authenticationFacadeService.queryLoginAuditRequestPaths();
		for (String onlyAuthcPath : onlyAuthcPaths)
		{
			filterChain.addToChain(onlyAuthcPath, "authc");
			filterChain.addToChain(onlyAuthcPath, "securityContextFilter");
			filterChain.addToChain(onlyAuthcPath, "sessionAuthc");
		}

		filterChain.addToChain(LOGIN_URL, "authc");

		filterChain.addToChain(LOGOUT_URL, "authc");
		filterChain.addToChain(LOGOUT_URL, "logout");

		filterChain.addToChain("/**", "authc");
		filterChain.addToChain("/**", "authz");
		filterChain.addToChain("/**", "securityContextFilter");
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

		Map<String, String> requestPathMap = authenticationFacadeService.queryNonUpdateAccessTimeRequestPaths();
		filter.setNonUpdateAccessTimeURIMap(requestPathMap);

		List<String> urlPatterns = new ArrayList<String>(1);
		urlPatterns.add("*.action");
		registrationBean.setUrlPatterns(urlPatterns);

		return registrationBean;
	}
}
