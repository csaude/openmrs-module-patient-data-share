package org.openmrs.module.csaude.pds.listener.dao;

import org.openmrs.api.db.hibernate.DbSession;
import org.openmrs.api.db.hibernate.DbSessionFactory;

public class DaoBase {
	
	protected DbSessionFactory sessionFactory;

	public void setSessionFactory(DbSessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public DbSession getSession() {
		return sessionFactory.getCurrentSession();
	}
}
