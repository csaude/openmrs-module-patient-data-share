package org.openmrs.module.csaude.pds.listener.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.openmrs.api.db.hibernate.DbSession;
import org.openmrs.module.csaude.pds.listener.config.PdsEventProcessor;
import org.openmrs.module.csaude.pds.listener.entity.DemographicDataQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DemographicDataQueueDao extends DaoBase {
	
	private static final Logger logger = LoggerFactory.getLogger(PdsEventProcessor.class);
	
	public DemographicDataQueue createDemographicDataQueue(DemographicDataQueue demographicDataQueue) throws RuntimeException {
		DbSession session = getSession();
		
		try {
				logger.debug("Saving Patient demographic data:  " + "uuid " + demographicDataQueue.getPatientUuid());
				session.saveOrUpdate(demographicDataQueue);
		}
		catch (Exception e) {
			throw new RuntimeException(
			    "An error occurred saving patient demographic data : " + "uuid " + demographicDataQueue.getPatientUuid(), e);
		}
		
		return demographicDataQueue;
	}
	
	public DemographicDataQueue getDemographicDataQueueByUuid(String patientUuid) {
		DbSession session = getSession();
		Criteria criteria = session.createCriteria(DemographicDataQueue.class, "demoData");
		criteria.add(Restrictions.eq("demoData.patientUuid", patientUuid));
		
		DemographicDataQueue demographicDataQueue = (DemographicDataQueue) criteria.uniqueResult();
		return demographicDataQueue;
	}
}
