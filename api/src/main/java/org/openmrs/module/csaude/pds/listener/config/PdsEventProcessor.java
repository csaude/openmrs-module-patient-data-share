package org.openmrs.module.csaude.pds.listener.config;

import org.openmrs.Person;
import org.openmrs.api.PersonService;
import org.openmrs.api.context.Context;
import org.openmrs.module.csaude.pds.listener.config.utils.PdsUtils;
import org.openmrs.module.csaude.pds.listener.entity.DemographicDataQueue;
import org.openmrs.module.csaude.pds.listener.service.DemographicDataQueueService;
import org.openmrs.module.debezium.entity.DebeziumEventQueue;
import org.openmrs.util.PrivilegeConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class PdsEventProcessor extends BaseEventProcessor {
	
	private static final Logger logger = LoggerFactory.getLogger(PdsEventProcessor.class);
	
	@Override
	public void process(DebeziumEventQueue event) {
		logger.info("Processing database event -> " + event);
		final long start = System.currentTimeMillis();
		DemographicDataQueueService demographicDataQueueService = Context.getService(DemographicDataQueueService.class);
		PersonService personService = Context.getService(PersonService.class);
		
		try {
			if (!Context.isSessionOpen()) {
				Context.openSession();
				Context.addProxyPrivilege(PrivilegeConstants.GET_PERSON_ATTRIBUTE_TYPES);
				Context.addProxyPrivilege(PrivilegeConstants.GET_PERSONS);
			}
			
			Person person = personService.getPerson(PdsUtils.getPersonId(event));
			if (person != null && person.getIsPatient()) {
				DemographicDataQueue demographicDataQueue = new DemographicDataQueue();
				demographicDataQueue.setPatientId(person.getPersonId());
				demographicDataQueue.setPatientUuid(person.getUuid());
				demographicDataQueue.setCreatedAt(new Date());
				demographicDataQueue.setActive(Boolean.TRUE);
				
				demographicDataQueueService.createDemographicDataQueue(demographicDataQueue);
				
				logger.info("Done Processing database event for patient -> " + demographicDataQueue.getPatientUuid());
				logger.debug("Time taken to process database event -> " + (System.currentTimeMillis() - start));
			}
		}
		catch (Exception e) {
			throw new RuntimeException("Error processing event -> " + event, e);
		}
		finally {
			try {
				Context.removeProxyPrivilege(PrivilegeConstants.GET_PERSON_ATTRIBUTE_TYPES);
				Context.removeProxyPrivilege(PrivilegeConstants.GET_PERSONS);
				
			}
			finally {
				Context.closeSession();
			}
		}
		
	}
}
