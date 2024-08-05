package org.openmrs.module.csaude.pds.listener.service;

import org.openmrs.api.OpenmrsService;
import org.openmrs.module.csaude.pds.listener.entity.DemographicDataQueue;
import org.springframework.transaction.annotation.Transactional;

public interface DemographicDataQueueService extends OpenmrsService {
	
	@Transactional
	DemographicDataQueue createDemographicDataQueue(DemographicDataQueue demographicDataQueue);
}
