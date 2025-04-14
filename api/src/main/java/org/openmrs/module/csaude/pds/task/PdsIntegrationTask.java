package org.openmrs.module.csaude.pds.task;

import org.openmrs.api.context.Context;
import org.openmrs.module.csaude.pds.listener.config.BaseEventProcessor;
import org.openmrs.module.csaude.pds.listener.config.PdsEventProcessor;
import org.openmrs.module.debezium.entity.DebeziumEventQueue;
import org.openmrs.module.debezium.service.DebeziumEventQueueService;
import org.openmrs.scheduler.tasks.AbstractTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class PdsIntegrationTask extends AbstractTask {
	
	private static final Logger log = LoggerFactory.getLogger(PdsIntegrationTask.class);
	
	private final static String APPLICATION_NAME = "PDS";
	
	private BaseEventProcessor eventProcessor;
	
	@Override
	public void execute() {
		log.info("Starting PDS integration task");
		if (!isExecuting) {
			DebeziumEventQueueService eventQueueService = Context.getService(DebeziumEventQueueService.class);
			
			boolean keepFetching = true;
			eventProcessor = new PdsEventProcessor();

			while (keepFetching) {
				Set<DebeziumEventQueue> eventQueueSet = eventQueueService.getApplicationEvents(APPLICATION_NAME);

				if (eventQueueSet.isEmpty()) {
					keepFetching = Boolean.FALSE;
				} else {
					eventQueueSet.forEach((eventQueue) -> {
                        log.debug("Received database event -> {}", eventQueue);
						eventProcessor.process(eventQueue);
					});
					eventQueueService.commitEventQueue(APPLICATION_NAME);
				}
			}
		} else {
			log.info("PDS integration task is already executing");
		}
		log.info("Execution of PDS integration task finished");
	}
}
