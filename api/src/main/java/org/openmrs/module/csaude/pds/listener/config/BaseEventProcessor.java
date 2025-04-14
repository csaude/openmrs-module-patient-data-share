package org.openmrs.module.csaude.pds.listener.config;

import org.openmrs.module.debezium.entity.DebeziumEventQueue;

public abstract class BaseEventProcessor {
	
	/**
	 * Called to process an event
	 *
	 * @param event {@link DebeziumEventQueue} object
	 */
	public abstract void process(DebeziumEventQueue event);
	
}
