package org.openmrs.module.csaude.pds.listener.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.openmrs.Patient;
import org.openmrs.PersonAttribute;
import org.openmrs.api.db.hibernate.DbSession;
import org.openmrs.module.csaude.pds.listener.entity.DemographicDataOffset;
import org.openmrs.module.csaude.pds.listener.entity.DemographicDataQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DemographicDataQueueDao extends DaoBase {
	
	private static final Logger logger = LoggerFactory.getLogger(DemographicDataQueueDao.class);
	
	public void createDemographicDataQueue(DemographicDataQueue demographicDataQueue) throws RuntimeException {
		DbSession session = getSession();
		
		try {
			logger.debug("Saving Patient demographic data:  uuid {} ", demographicDataQueue.getPatientUuid());
			session.saveOrUpdate(demographicDataQueue);
		}
		catch (Exception e) {
			throw new RuntimeException(
			        "An error occurred saving patient demographic data : " + "uuid " + demographicDataQueue.getPatientUuid(),
			        e);
		}
		
	}
	
	public List<PersonAttribute> getPersonAttributeByPersonType(String personAttributeTypeUuid, Integer personId) {
		DbSession session = getSession();
		Criteria criteria = session.createCriteria(PersonAttribute.class, "personAttribute");
		criteria.add(Restrictions.eq("personAttribute.person.personId", personId));
		criteria.createAlias("personAttribute.attributeType", "personAttributeType");
		criteria.add(Restrictions.eq("personAttributeType.uuid", personAttributeTypeUuid));
		
		return (List<PersonAttribute>) criteria.list();
		
	}
	
	public List<DemographicDataQueue> getAllDemographicDataQueues(Integer count,
	        DemographicDataOffset demographicDataOffset) {
		DbSession session = getSession();
		Criteria criteria = session.createCriteria(DemographicDataQueue.class);
		
		if (demographicDataOffset != null) {
			criteria.add(Restrictions.gt("id", demographicDataOffset.getFirstRead()));
		}
		if (count != null) {
			criteria.setMaxResults(count);
		}
		criteria.add(Restrictions.eq("active", true));
		criteria.addOrder(Order.asc("id"));
		
		return (List<DemographicDataQueue>) criteria.list();
		
	}
	
	public Set<Patient> getPatientsByIds(List<Integer> patientIds) {
		DbSession session = getSession();
		Criteria criteria = session.createCriteria(Patient.class, "patient");
		criteria.add(Restrictions.in("patient.patientId", patientIds));
		
		return new HashSet<>(criteria.list());
		
	}
	
	public DemographicDataOffset getDemographicDataOffset(String clientName) {
		DbSession session = getSession();
		Criteria criteria = session.createCriteria(DemographicDataOffset.class, "offset");
		criteria.add(Restrictions.eq("offset.clientName", clientName));
		
		return (DemographicDataOffset) criteria.uniqueResult();
	}
	
	public void updateOrSaveDemographicOffset(DemographicDataOffset demographicDataOffset) {
		DbSession session = getSession();
		
		try {
			
			logger.debug("Saving Patient demographic offset: {} for client: {}",
			    " Start from " + demographicDataOffset.getFirstRead() + "to " + demographicDataOffset.getLastRead(),
			    demographicDataOffset.getClientName());
			if (demographicDataOffset.isCreated()) {
				session.update(demographicDataOffset);
			} else {
				session.save(demographicDataOffset);
			}
		}
		catch (Exception e) {
			throw new RuntimeException("An error occurred saving patient demographic offset : " + " Start from "
			        + demographicDataOffset.getFirstRead() + "to " + demographicDataOffset.getLastRead() + " for client: "
			        + demographicDataOffset.getClientName(), e);
		}
		
	}
}
