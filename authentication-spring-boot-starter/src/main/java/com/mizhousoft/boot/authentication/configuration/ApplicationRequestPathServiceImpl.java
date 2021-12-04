package com.mizhousoft.boot.authentication.configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mizhousoft.boot.authentication.service.ApplicationRequestPathService;
import com.mizhousoft.boot.authentication.service.RequestPathService;

/**
 * 应用请求路径服务
 *
 * @version
 */
@Service
public class ApplicationRequestPathServiceImpl implements ApplicationRequestPathService
{
	@Autowired
	private List<RequestPathService> requestPathServices;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> queryAuthcRequestPaths()
	{
		List<String> requestPaths = new ArrayList<>(10);

		for (RequestPathService requestPathService : requestPathServices)
		{
			List<String> paths = requestPathService.queryAuthcRequestPaths();
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

		for (RequestPathService requestPathService : requestPathServices)
		{
			List<String> paths = requestPathService.queryAuthzRequestPaths();
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

		for (RequestPathService requestPathService : requestPathServices)
		{
			List<String> paths = requestPathService.queryLoginAuditRequestPaths();
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

		for (RequestPathService requestPathService : requestPathServices)
		{
			List<String> paths = requestPathService.queryNonUpdateAccessTimeRequestPaths();
			if (null != paths)
			{
				paths.forEach(path -> requestPathMap.put(path, path));
			}
		}

		return requestPathMap;
	}
}
