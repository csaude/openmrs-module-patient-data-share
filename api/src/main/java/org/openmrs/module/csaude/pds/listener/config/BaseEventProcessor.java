package org.openmrs.module.csaude.pds.listener.config;

import org.openmrs.module.debezium.DatabaseEvent;

public abstract class BaseEventProcessor {
	
	/**
	 * Called to process an event
	 *
	 * @param event {@link DatabaseEvent} object
	 */
	public abstract void process(DatabaseEvent event);
	
}
