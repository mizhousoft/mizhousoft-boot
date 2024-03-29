package com.mizhousoft.boot.authentication.session;

import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.SessionFactory;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionContext;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mizhousoft.commons.lang.CharEncoding;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * 安全的SessionFactory
 * 
 * @version
 */
public class SecureSessionFactory implements SessionFactory
{
	private static final Logger LOG = LoggerFactory.getLogger(SecureSessionFactory.class);

	/**
	 * 创建Session
	 * 
	 * @param initData
	 * @return
	 */
	public Session createSession(SessionContext initData)
	{
		String sessionId = null;

		if (null != initData)
		{
			if (initData instanceof DefaultWebSessionContext)
			{
				DefaultWebSessionContext dcontext = (DefaultWebSessionContext) initData;
				ServletRequest sr = dcontext.getServletRequest();
				if (sr instanceof ShiroHttpServletRequest)
				{
					ShiroHttpServletRequest ssr = (ShiroHttpServletRequest) sr;
					ServletRequest request = ssr.getRequest();
					HttpServletRequest hrequest = WebUtils.toHttp(request);

					HttpSession session = hrequest.getSession(false);
					if (null == session)
					{
						session = hrequest.getSession(true);
						sessionId = session.getId();
					}
					else
					{
						try
						{
							session.invalidate();
							session = hrequest.getSession(true);
						}
						catch (Throwable e)
						{
							LOG.error("Session invalide failed.", e);
						}
					}

					byte[] bytes = session.getId().getBytes(CharEncoding.UTF8);
					UUID uid = UUID.nameUUIDFromBytes(bytes);
					sessionId = Base64.encodeBase64String(uid.toString().getBytes(CharEncoding.UTF8));
				}
			}
		}

		SimpleSession session = new SimpleSession();
		session.setId(sessionId);

		if (initData != null)
		{
			String host = initData.getHost();
			if (host != null)
			{
				session.setHost(host);
			}
		}

		return session;
	}
}
