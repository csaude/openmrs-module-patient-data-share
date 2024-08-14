package org.openmrs.module.csaude.pds.listener.service;

import org.openmrs.Patient;
import org.openmrs.PersonAttribute;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.csaude.pds.listener.entity.DemographicDataQueue;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

public interface DemographicDataQueueService extends OpenmrsService {
	
	@Transactional
	DemographicDataQueue createDemographicDataQueue(DemographicDataQueue demographicDataQueue);
	
	@Transactional
	List<DemographicDataQueue> getAllDemographicDataQueues();
	
	@Transactional
	List<PersonAttribute> getPersonAttributeByPersonType(String personAttributeTypeUuid, Integer personId);
	
	@Transactional
	Set<Patient> getPatientsByIds(List<Integer> patientIds);
	
}
