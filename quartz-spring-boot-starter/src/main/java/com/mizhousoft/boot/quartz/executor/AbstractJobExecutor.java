package com.mizhousoft.boot.quartz.executor;

import org.quartz.Scheduler;
import org.springframework.context.ApplicationContext;

/**
 * 抽象工作执行器
 *
 * @version
 */
public abstract class AbstractJobExecutor
{
	protected Scheduler scheduler;

	protected ApplicationContext applicationContext;

	/**
	 * 构造函数
	 *
	 * @param scheduler
	 * @param applicationContext
	 */
	public AbstractJobExecutor(Scheduler scheduler, ApplicationContext applicationContext)
	{
		super();
		this.scheduler = scheduler;
		this.applicationContext = applicationContext;
	}
}
