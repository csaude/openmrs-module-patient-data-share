package org.openmrs.module.csaude.pds.listener.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.openmrs.Patient;
import org.openmrs.PersonAttribute;
import org.openmrs.api.db.hibernate.DbSession;
import org.openmrs.module.csaude.pds.listener.config.PdsEventProcessor;
import org.openmrs.module.csaude.pds.listener.entity.DemographicDataQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DemographicDataQueueDao extends DaoBase {
	
	private static final Logger logger = LoggerFactory.getLogger(PdsEventProcessor.class);
	
	public DemographicDataQueue createDemographicDataQueue(DemographicDataQueue demographicDataQueue)
	        throws RuntimeException {
		DbSession session = getSession();
		
		try {
			logger.debug("Saving Patient demographic data:  " + "uuid " + demographicDataQueue.getPatientUuid());
			session.saveOrUpdate(demographicDataQueue);
		}
		catch (Exception e) {
			throw new RuntimeException(
			        "An error occurred saving patient demographic data : " + "uuid " + demographicDataQueue.getPatientUuid(),
			        e);
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
	
	public List<PersonAttribute> getPersonAttributeByPersonType(String personAttributeTypeUuid, Integer personId) {
		DbSession session = getSession();
		Criteria criteria = session.createCriteria(PersonAttribute.class, "personAttribute");
		criteria.add(Restrictions.eq("personAttribute.person.personId", personId));
		criteria.createAlias("personAttribute.attributeType", "personAttributeType");
		criteria.add(Restrictions.eq("personAttributeType.uuid", personAttributeTypeUuid));
		
		return (List<PersonAttribute>) criteria.list();
		
	}
	
	public List<DemographicDataQueue> getAllDemographicDataQueues() {
		DbSession session = getSession();
		Criteria criteria = session.createCriteria(DemographicDataQueue.class);
		return (List<DemographicDataQueue>) criteria.list();
		
	}
	
	public Set<Patient> getPatientsByIds(List<Integer> patientIds) {
		DbSession session = getSession();
		Criteria criteria = session.createCriteria(Patient.class, "patient");
		criteria.add(Restrictions.in("patient.patientId", patientIds));
		
		return new HashSet<>(criteria.list());
		
	}
}
