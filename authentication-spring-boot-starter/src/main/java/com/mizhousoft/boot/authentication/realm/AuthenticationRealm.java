package com.mizhousoft.boot.authentication.realm;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mizhousoft.boot.authentication.AccountDetails;
import com.mizhousoft.boot.authentication.AccountSessionService;
import com.mizhousoft.boot.authentication.GrantedAuthority;
import com.mizhousoft.boot.authentication.authc.UnionAuthenticationToken;
import com.mizhousoft.boot.authentication.configuration.AuthenticationProperties;
import com.mizhousoft.boot.authentication.exception.BadCredentialsException;
import com.mizhousoft.boot.authentication.limiter.AuthFailureLimiter;
import com.mizhousoft.boot.authentication.service.AccountAuthcService;

/**
 * 认证 Realm
 * 
 * @version
 */
public class AuthenticationRealm extends AuthorizingRealm
{
	private static final Logger LOG = LoggerFactory.getLogger(AuthenticationRealm.class);

	// 帐号服务
	private AccountAuthcService accountAuthcService;

	// 帐号Session服务
	private AccountSessionService accountSessionService;

	// AuthFailureLimiter
	private AuthFailureLimiter authFailureLimiter;

	// 配置
	private AuthenticationProperties authenticationProperties;

	/**
	 * 构造函数
	 *
	 * @param accountAuthcService
	 * @param accountSessionService
	 * @param authFailureLimiter
	 * @param authenticationProperties
	 */
	public AuthenticationRealm(AccountAuthcService accountAuthcService, AccountSessionService accountSessionService,
	        AuthFailureLimiter authFailureLimiter, AuthenticationProperties authenticationProperties)
	{
		super();
		this.accountAuthcService = accountAuthcService;
		this.accountSessionService = accountSessionService;
		this.authFailureLimiter = authFailureLimiter;
		this.authenticationProperties = authenticationProperties;
	}

	/**
	 * 获取授权信息
	 * 
	 * @param principals
	 * @return
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals)
	{
		Set<String> roles = new HashSet<String>(5);

		Object primaryPrincipal = principals.getPrimaryPrincipal();
		if (primaryPrincipal instanceof AccountDetails)
		{
			Collection<? extends GrantedAuthority> grantedAuthorities = ((AccountDetails) primaryPrincipal).getAuthorities();
			for (GrantedAuthority grantedAuthority : grantedAuthorities)
			{
				roles.add(grantedAuthority.getAuthority());
			}
		}

		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo(roles);

		return authorizationInfo;
	}

	/**
	 * 获取认证信息
	 * 
	 * @param token
	 * @return
	 * @throws AuthenticationException
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException
	{
		UnionAuthenticationToken upToken = (UnionAuthenticationToken) token;
		String userName = upToken.getUsername();
		char[] passwd = upToken.getPassword();
		String code = upToken.getCode();

		try
		{
			authFailureLimiter.tryAcquire(upToken.getHost());

			AccountDetails accountDetails = null;
			if (null != passwd)
			{
				accountDetails = accountAuthcService.authenticate(userName, passwd, upToken.getHost());
			}
			else if (null != code)
			{
				accountDetails = accountAuthcService.authenticate(userName, code, upToken.getHost());
			}
			else
			{
				throw new BadCredentialsException("Authentication token is wrong.");
			}

			authFailureLimiter.clear(upToken.getHost());

			if (authenticationProperties.isSessionMutex())
			{
				accountSessionService.logoffAccount(accountDetails.getAccountId());
			}

			SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(accountDetails, null, null, getName());
			return authenticationInfo;
		}
		catch (com.mizhousoft.boot.authentication.exception.AuthenticationException e)
		{
			LOG.error("Account authenticate failed, message: {}", e.getMessage());

			authFailureLimiter.authcFailure(upToken.getHost());

			if (e instanceof com.mizhousoft.boot.authentication.exception.AccountDisabledException)
			{
				throw new DisabledAccountException(e.getMessage(), e);
			}
			else if (e instanceof com.mizhousoft.boot.authentication.exception.BadCredentialsException)
			{
				String message = null;
				String[] params = e.getCodeParams();
				if (null != params && params.length > 0)
				{
					message = params[0];
				}

				throw new IncorrectCredentialsException(message, e);
			}
			else if (e instanceof com.mizhousoft.boot.authentication.exception.AccountLockedException)
			{
				throw new LockedAccountException(e.getMessage(), e);
			}
			else
			{
				throw new UnknownAccountException(e.getMessage(), e);
			}
		}
		catch (Throwable e)
		{
			LOG.error("Account authenticate failed.", e);

			throw new UnknownAccountException(e.getMessage(), e);
		}
	}

	/**
	 * 框架帐号认证不处理，由安全管理认证
	 * 
	 * @param token
	 * @param info
	 * @throws AuthenticationException
	 */
	@Override
	protected void assertCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) throws AuthenticationException
	{

	}
}
