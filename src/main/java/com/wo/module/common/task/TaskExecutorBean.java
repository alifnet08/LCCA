package com.wo.module.common.task;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

//@ManagedBean(eager = true)
//@ApplicationScoped
public class TaskExecutorBean {

	private static Logger logger = Logger.getLogger(TaskExecutorBean.class);

	private ExecutorService executor;

	private int corePoolSize = 3; // default value
	private int maxPoolSize = 5; // default value
	private long keepAliveTime = 30 * 60; // default value

	@PostConstruct
	public void perApplicationConstructed() {
		ExternalContext externalContext = FacesContext.getCurrentInstance()
				.getExternalContext();
		try {
			if (StringUtils.isNotBlank(externalContext
					.getInitParameter("corePoolSize"))) {
				corePoolSize = Integer.parseInt(externalContext
						.getInitParameter("corePoolSize"));
			}
		} catch (Exception ex) {
			logger.error(
					"Unable to get param corePoolSize from web.xml config, use default...",
					ex);
		}

		try {
			if (StringUtils.isNotBlank(externalContext
					.getInitParameter("maxPoolSize"))) {
				maxPoolSize = Integer.parseInt(externalContext
						.getInitParameter("maxPoolSize"));
			}
		} catch (Exception ex) {
			logger.error(
					"Unable to get param maxPoolSize from web.xml config, use default...",
					ex);
		}

		try {
			if (StringUtils.isNotBlank(externalContext
					.getInitParameter("keepAliveTime"))) {
				keepAliveTime = Long.parseLong(externalContext
						.getInitParameter("keepAliveTime"));
			}
		} catch (Exception ex) {
			logger.error(
					"Unable to get param keepAliveTime from web.xml config, use default...",
					ex);
		}

		this.createExecutor();

		logger.info("Task Executor service bean started...");
		logger.info("Configuration Parameters :");
		logger.info("corePoolSize = " + corePoolSize);
		logger.info("maxPoolSize = " + maxPoolSize);
		logger.info("keepAliveTime = " + keepAliveTime + " seconds");
	}

	@PreDestroy
	public void perApplicationDestroyed() {
		executor.shutdown();
		logger.info("Task Executor service bean shutdown...");
	}

	public synchronized void submitTask(Runnable runnable) {
		if (executor == null) {
			this.createExecutor();
		}
		executor.submit(runnable);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public synchronized Future<String> submitTask(Callable callable) {
		if (executor == null) {
			this.createExecutor();
		}
		return executor.submit(callable);
	}

	private void createExecutor() {
		executor = new ThreadPoolExecutor(corePoolSize, maxPoolSize,
				keepAliveTime, TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>());

	}

}
