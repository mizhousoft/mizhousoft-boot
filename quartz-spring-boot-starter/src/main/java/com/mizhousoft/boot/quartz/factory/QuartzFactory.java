package com.mizhousoft.boot.quartz.factory;

import java.util.Properties;

import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

import com.mizhousoft.boot.quartz.protperties.QuartzThreadPoolProperties;

/**
 * Quartz工厂类
 *
 * @version
 */
public abstract class QuartzFactory
{
	public static StdSchedulerFactory create(QuartzThreadPoolProperties threadPoolProps) throws SchedulerException
	{
		Properties props = new Properties();

		props.setProperty("org.quartz.threadPool.threadCount", String.valueOf(threadPoolProps.getThreadCount()));
		props.setProperty("org.quartz.threadPool.threadPriority", String.valueOf(threadPoolProps.getThreadPriority()));

		props.setProperty("org.quartz.scheduler.skipUpdateCheck", "true");

		return new StdSchedulerFactory(props);
	}
}
