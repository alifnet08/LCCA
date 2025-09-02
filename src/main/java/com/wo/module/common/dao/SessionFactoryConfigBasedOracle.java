package com.wo.module.common.dao;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

//@ManagedBean(name="configSessionFactory")
//@ApplicationScoped
public class SessionFactoryConfigBasedOracle {
	private SessionFactory sessionFactory;
	private static Configuration configuration;
	private static String _configFilePath = "/hibernateEgOracle.cfg.xml";
	
	public SessionFactoryConfigBasedOracle() {
		sessionFactory = new Configuration().configure(_configFilePath).buildSessionFactory();
	}
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public static Configuration getConfiguration() {
		return configuration;
	}

	public static void setConfiguration(Configuration configuration) {
		SessionFactoryConfigBasedOracle.configuration = configuration;
	}
}
