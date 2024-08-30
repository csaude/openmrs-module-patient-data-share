package org.openmrs.module.csaude.pds.listener.service;

import org.openmrs.Patient;
import org.openmrs.PersonAttribute;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.csaude.pds.listener.entity.DemographicDataOffset;
import org.openmrs.module.csaude.pds.listener.entity.DemographicDataQueue;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

public interface DemographicDataQueueService extends OpenmrsService {
	
	@Transactional
	void createDemographicDataQueue(DemographicDataQueue demographicDataQueue);
	
	@Transactional
	List<DemographicDataQueue> getAllDemographicDataQueues(Integer count, DemographicDataOffset demographicDataOffset);
	
	@Transactional
	List<PersonAttribute> getPersonAttributeByPersonType(String personAttributeTypeUuid, Integer personId);
	
	@Transactional
	Set<Patient> getPatientsByIds(List<Integer> patientIds);
	
	DemographicDataOffset getDemographicDataOffset(String clientName);
	
	void updateOrSaveDemographicOffset(DemographicDataOffset demographicDataOffset);
}
