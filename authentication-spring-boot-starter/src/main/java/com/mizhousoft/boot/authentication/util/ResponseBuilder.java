package com.mizhousoft.boot.authentication.util;

import java.util.HashMap;
import java.util.Map;

import com.mizhousoft.boot.authentication.AccountDetails;
import com.mizhousoft.commons.json.JSONException;
import com.mizhousoft.commons.json.JSONUtils;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 响应构建器
 *
 * @version
 */
public abstract class ResponseBuilder
{
	/**
	 * 构建成功响应
	 * 
	 * @param location
	 * @param accountDetails
	 * @return
	 * @throws JSONException
	 */
	public static String buildSucceed(String location, AccountDetails accountDetails) throws JSONException
	{
		Map<String, Object> data = new HashMap<String, Object>(10);
		data.put("okey", true);
		data.put("status", HttpServletResponse.SC_OK);
		data.put("location", location);
		data.put("credentialsExpired", accountDetails.isCredentialsExpired());
		data.put("firstLogin", accountDetails.isFirstLogin());
		data.put("remindModifyPasswd", accountDetails.isRemindModifyPasswd());
		data.put("twoFactorAuthcPassed", accountDetails.isTwoFactorAuthcPassed());

		String result = JSONUtils.toJSONString(data);

		return result;
	}

	/**
	 * 构建成功响应
	 * 
	 * @param location
	 * @return
	 */
	public static String buildSucceed(String location)
	{
		StringBuilder resp = new StringBuilder(50);
		resp.append("{\"okey\": true").append(", \"status\": ").append(HttpServletResponse.SC_OK).append(", \"location\": \"")
		        .append(location).append("\"}");

		return resp.toString();
	}

	/**
	 * 构建失败
	 * 
	 * @param location
	 * @param error
	 * @return
	 */
	public static String buildUnauthorized(String location, String error)
	{
		StringBuilder resp = new StringBuilder(50);
		resp.append("{\"okey\": false").append(", \"status\": ").append(HttpServletResponse.SC_UNAUTHORIZED).append(", \"error\": \"")
		        .append(error).append("\", \"location\": \"").append(location).append("\"}");

		return resp.toString();
	}

	/**
	 * 构建未授权响应
	 * 
	 * @param error
	 * @return
	 */
	public static String buildForbidden(String error)
	{
		StringBuilder resp = new StringBuilder(50);
		resp.append("{\"okey\": false").append(", \"status\": ").append(HttpServletResponse.SC_FORBIDDEN).append(", \"error\": \"")
		        .append(error).append("\"}");

		return resp.toString();
	}
}
