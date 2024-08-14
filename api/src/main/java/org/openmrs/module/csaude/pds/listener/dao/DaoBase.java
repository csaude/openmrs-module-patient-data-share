package org.openmrs.module.csaude.pds.listener.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.transform.ResultTransformer;
import org.openmrs.api.db.hibernate.DbSession;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.csaude.pds.IPagingInfo;

import java.util.List;

public class DaoBase {
	
	protected DbSessionFactory sessionFactory;
	
	public void setSessionFactory(DbSessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public DbSession getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	protected Criteria createPagingCriteria(IPagingInfo pagingInfo, Criteria criteria) {
		if (criteria != null && pagingInfo != null && pagingInfo.getPageIndex() != null && pagingInfo.getPageSize() != null
		        && pagingInfo.getPageIndex() >= 0 && pagingInfo.getPageSize() >= 0) {
			criteria.setFirstResult((pagingInfo.getPageIndex()) * pagingInfo.getPageSize());
			criteria.setMaxResults(pagingInfo.getPageSize());
			criteria.setFetchSize(pagingInfo.getPageSize());
		}
		return criteria;
	}
	
	@SuppressWarnings({ "unchecked" })
	protected <T> List<T> executeCriteria(Criteria criteria, IPagingInfo pagingInfo, Order... orderBy) {
		
		loadPagingTotal(pagingInfo, criteria);
		
		if (orderBy != null && orderBy.length > 0) {
			for (Order order : orderBy) {
				criteria.addOrder(order);
			}
		}
		return createPagingCriteria(pagingInfo, criteria).list();
	}
	
	/**
	 * Loads the record count for the specified criteria into the specified paging object.
	 *
	 * @param pagingInfo The {@link IPagingInfo} object to load with the record count.
	 * @param criteria The {@link Criteria} to execute against the hibernate data source or {@code null}
	 *            to create a new one.
	 */
	protected void loadPagingTotal(IPagingInfo pagingInfo, Criteria criteria) {
		if (pagingInfo != null && pagingInfo.getPageIndex() != null && pagingInfo.getPageSize() != null
		        && pagingInfo.getPageIndex() >= 0 && pagingInfo.getPageSize() >= 0) {
			if (criteria == null) {
				return;
			}
			
			if (pagingInfo.shouldLoadRecordCount()) {
				Projection projection = null;
				ResultTransformer transformer = null;
				
				if (criteria instanceof CriteriaImpl) {
					CriteriaImpl criteriaImpl = (CriteriaImpl) criteria;
					projection = criteriaImpl.getProjection();
					transformer = criteriaImpl.getResultTransformer();
				}
				
				try {
					criteria.setProjection(Projections.rowCount());
					
					Long count = (Long) criteria.uniqueResult();
					pagingInfo.setTotalRecordCount(count == null ? 0 : count);
					pagingInfo.setLoadRecordCount(false);
				}
				finally {
					// Reset the criteria projection and transformer to return the result rather than the row count
					criteria.setProjection(projection);
					criteria.setResultTransformer(transformer);
				}
			}
		}
	}
}
