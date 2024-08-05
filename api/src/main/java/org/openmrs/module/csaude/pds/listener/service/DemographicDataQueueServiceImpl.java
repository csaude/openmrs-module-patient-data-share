package org.openmrs.module.csaude.pds.listener.service;

import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.csaude.pds.listener.dao.DemographicDataQueueDao;
import org.openmrs.module.csaude.pds.listener.entity.DemographicDataQueue;

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
}
