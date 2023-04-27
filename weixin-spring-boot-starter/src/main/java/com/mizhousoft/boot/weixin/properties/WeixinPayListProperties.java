package com.mizhousoft.boot.weixin.properties;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 配置
 *
 * @version
 */
@Component
@ConfigurationProperties(prefix = "weixin.pay")
public class WeixinPayListProperties
{
	private List<WeixinPayProperties> list;

	/**
	 * 获取list
	 * 
	 * @return
	 */
	public List<WeixinPayProperties> getList()
	{
		return list;
	}

	/**
	 * 设置list
	 * 
	 * @param list
	 */
	public void setList(List<WeixinPayProperties> list)
	{
		this.list = list;
	}
}
