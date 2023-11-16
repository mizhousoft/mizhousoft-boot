package com.mizhousoft.boot.driver.impl;

import java.lang.reflect.Method;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.mizhousoft.boot.driver.DatabaseDriverDestroyer;

import jakarta.annotation.PreDestroy;

/**
 * 数据库驱动销毁器
 *
 * @version
 */
@Component
public class DatabaseDriverDestroyerImpl implements DatabaseDriverDestroyer
{
	private static final Logger LOG = LoggerFactory.getLogger(DatabaseDriverDestroyerImpl.class);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void register()
	{
		LOG.info("Database driver destroyer register successfully.");
	}

	@PreDestroy
	public void destroy()
	{
		try
		{
			Enumeration<Driver> drivers = DriverManager.getDrivers();
			while (drivers.hasMoreElements())
			{
				Driver driver = drivers.nextElement();

				try
				{
					DriverManager.deregisterDriver(driver);

					LOG.info("{} driver deregister successfully.", driver.toString());
				}
				catch (SQLException e)
				{
					LOG.error("{} driver deregister failed.", driver.toString(), e);
				}
			}
		}
		catch (Throwable e)
		{
			LOG.error("Database driver deregister failed.", e);
		}

		try
		{
			Class<?> cls = Class.forName("com.mysql.cj.jdbc.AbandonedConnectionCleanupThread");
			if (null != cls)
			{
				Method mth = cls.getMethod("checkedShutdown");
				if (mth != null)
				{
					mth.invoke(null);

					LOG.info("AbandonedConnectionCleanupThread shundown successfully.");
				}
				else
				{
					LOG.error("AbandonedConnectionCleanupThread checkedShutdown method not found.");
				}
			}
			else
			{
				LOG.warn("AbandonedConnectionCleanupThread not found.");
			}
		}
		catch (Throwable e)
		{
			LOG.error("AbandonedConnectionCleanupThread shundown failed.", e);
		}
	}
}
