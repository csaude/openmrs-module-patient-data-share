package org.openmrs.module.csaude.pds.listener.service;

import org.openmrs.Patient;
import org.openmrs.PersonAttribute;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.csaude.pds.listener.dao.DemographicDataQueueDao;
import org.openmrs.module.csaude.pds.listener.entity.DemographicDataQueue;

import java.util.List;
import java.util.Set;

public class DemographicDataQueueServiceImpl extends BaseOpenmrsService implements DemographicDataQueueService {
	
	DemographicDataQueueDao dao;
	
	public DemographicDataQueueServiceImpl() {
	}
	
	/**
	 * Injected in moduleApplicationContext.xml
	 */
	public void setDao(DemographicDataQueueDao dao) {
		this.dao = dao;
	}
	
	@Override
	public DemographicDataQueue createDemographicDataQueue(DemographicDataQueue demographicDataQueue) {
		return dao.createDemographicDataQueue(demographicDataQueue);
	}
	
	@Override
	public List<PersonAttribute> getPersonAttributeByPersonType(String personAttributeTypeUuid, Integer personId) {
		return dao.getPersonAttributeByPersonType(personAttributeTypeUuid, personId);
	}
	
	@Override
	public Set<Patient> getPatientsByIds(List<Integer> patientIds) {
		return dao.getPatientsByIds(patientIds);
	}
	
	@Override
	public List<DemographicDataQueue> getAllDemographicDataQueues() {
		return dao.getAllDemographicDataQueues();
	}
}
