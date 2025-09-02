package com.wo.module.common.dao;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

//@ManagedBean(name="configSessionFactory")
//@ApplicationScoped
public class SessionFactoryConfigBasedOther {
	private SessionFactory sessionFactory;
	private static Configuration configuration;
	private static String _configFilePath = "/hibernateEg.cfg.xml";
	
	public SessionFactoryConfigBasedOther() {
		sessionFactory = new Configuration().configure(_configFilePath).buildSessionFactory();
	}
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public static Configuration getConfiguration() {
		return configuration;
	}

	public static void setConfiguration(Configuration configuration) {
		SessionFactoryConfigBasedOther.configuration = configuration;
	}
}
