package com.mizhousoft.boot.authentication.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mizhousoft.boot.authentication.AuthenticationService;
import com.mizhousoft.boot.authentication.service.AccessControlService;
import com.mizhousoft.boot.authentication.service.ApplicationAuthenticationService;
import com.mizhousoft.boot.authentication.service.AuthenticationPathService;

/**
 * 认证服务
 *
 * @version
 */
@Service
public class AuthenticationServiceImpl implements AuthenticationService
{
	private static final Logger LOG = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

	@Autowired
	private AuthenticationPathService authenticationPathService;

	@Autowired
	private ApplicationAuthenticationService applicationAuthService;

	@Autowired
	private AccessControlService accessControlService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> getAuthcRequestPaths()
	{
		List<String> requestPaths = new ArrayList<>(10);

		List<String> paths = authenticationPathService.getAuthcRequestPaths();
		requestPaths.addAll(paths);

		return requestPaths;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> getAuthzRequestPaths()
	{
		List<String> requestPaths = new ArrayList<>(10);

		List<String> paths = authenticationPathService.getAuthzRequestPaths();
		requestPaths.addAll(paths);

		return requestPaths;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> getLoginAuditRequestPaths()
	{
		List<String> requestPaths = new ArrayList<>(10);

		List<String> paths = authenticationPathService.getLoginRequestPaths();
		requestPaths.addAll(paths);

		paths = applicationAuthService.getExtendedLoginRequestPaths();
		requestPaths.addAll(paths);

		return requestPaths;
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public Map<String, String> getNonUpdateAccessTimeRequestPaths()
	{
		Map<String, String> requestPathMap = new HashMap<>(10);

		List<String> paths = authenticationPathService.getNonUpdateAccessTimeRequestPaths();
		paths.forEach(path -> requestPathMap.put(path, path));

		paths = applicationAuthService.getNonUpdateAccessTimeRequestPaths();
		paths.forEach(path -> requestPathMap.put(path, path));

		return requestPathMap;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<String> getRolesByPath(String requestPath)
	{
		Set<String> roles = new HashSet<>(5);

		String serviceId = applicationAuthService.getServiceId();

		Set<String> list = accessControlService.getRolesByPath(serviceId, requestPath);
		roles.addAll(list);

		if (roles.isEmpty())
		{
			LOG.warn("Permission not found, path is {}.", requestPath);
		}

		return roles;
	}

	@PostConstruct
	public void initialize()
	{
		String serviceId = applicationAuthService.getServiceId();
		if (StringUtils.isBlank(serviceId))
		{
			throw new IllegalArgumentException("Application service id null.");
		}

		LOG.info("Application service id is {}.", serviceId);
	}
}
