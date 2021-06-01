package com.mizhousoft.boot.quartz.factory;

import java.util.concurrent.TimeUnit;

import org.quartz.SimpleScheduleBuilder;

/**
 * SimpleScheduleBuilder工厂类
 *
 * @version
 */
public abstract class SimpleScheduleBuilderFactory
{
	public static SimpleScheduleBuilder create(int interval, TimeUnit timeUnit)
	{
		switch (timeUnit)
		{
			case SECONDS:
				return SimpleScheduleBuilder.repeatSecondlyForever(interval);
			case MINUTES:
				return SimpleScheduleBuilder.repeatMinutelyForever(interval);
			case HOURS:
				return SimpleScheduleBuilder.repeatHourlyForever(interval);
			default:
				throw new IllegalArgumentException("TimeUnit is illegal.");
		}
	}
}
