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

import com.mizhousoft.boot.authentication.AuthenticationFacadeService;
import com.mizhousoft.boot.authentication.service.AccessControlService;
import com.mizhousoft.boot.authentication.service.ApplicationServiceIdProvider;
import com.mizhousoft.boot.authentication.service.RequestPathService;

/**
 * 认证服务
 *
 * @version
 */
@Service
public class AuthenticationFacadeServiceImpl implements AuthenticationFacadeService
{
	private static final Logger LOG = LoggerFactory.getLogger(AuthenticationFacadeServiceImpl.class);

	@Autowired
	private List<RequestPathService> requestPathServices;

	@Autowired
	private ApplicationServiceIdProvider serviceIdProvider;

	@Autowired
	private AccessControlService accessControlService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> queryAuthcRequestPaths()
	{
		List<String> requestPaths = new ArrayList<>(10);

		String serviceId = serviceIdProvider.getServiceId();

		for (RequestPathService requestPathService : requestPathServices)
		{
			List<String> paths = requestPathService.queryAuthcRequestPaths(serviceId);
			if (null != paths)
			{
				requestPaths.addAll(paths);
			}
		}

		return requestPaths;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> queryAuthzRequestPaths()
	{
		List<String> requestPaths = new ArrayList<>(10);

		String serviceId = serviceIdProvider.getServiceId();

		for (RequestPathService requestPathService : requestPathServices)
		{
			List<String> paths = requestPathService.queryAuthzRequestPaths(serviceId);
			if (null != paths)
			{
				requestPaths.addAll(paths);
			}
		}

		return requestPaths;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> queryLoginAuditRequestPaths()
	{
		List<String> requestPaths = new ArrayList<>(10);

		String serviceId = serviceIdProvider.getServiceId();

		for (RequestPathService requestPathService : requestPathServices)
		{
			List<String> paths = requestPathService.queryLoginAuditRequestPaths(serviceId);
			if (null != paths)
			{
				requestPaths.addAll(paths);
			}
		}

		return requestPaths;
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public Map<String, String> queryNonUpdateAccessTimeRequestPaths()
	{
		Map<String, String> requestPathMap = new HashMap<>(10);

		String serviceId = serviceIdProvider.getServiceId();

		for (RequestPathService requestPathService : requestPathServices)
		{
			List<String> paths = requestPathService.queryNonUpdateAccessTimeRequestPaths(serviceId);
			if (null != paths)
			{
				paths.forEach(path -> requestPathMap.put(path, path));
			}
		}

		return requestPathMap;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<String> getRolesByPath(String requestPath)
	{
		Set<String> roles = new HashSet<>(5);

		String serviceId = serviceIdProvider.getServiceId();

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
		String serviceId = serviceIdProvider.getServiceId();
		if (StringUtils.isBlank(serviceId))
		{
			throw new IllegalArgumentException("Application service id null.");
		}

		LOG.info("Application service id is {}.", serviceId);
	}
}
