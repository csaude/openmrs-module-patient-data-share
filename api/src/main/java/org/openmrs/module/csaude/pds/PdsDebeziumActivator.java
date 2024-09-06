package org.openmrs.module.csaude.pds;

import org.openmrs.module.BaseModuleActivator;
import org.openmrs.module.debezium.DebeziumActivator;

public class PdsDebeziumActivator extends BaseModuleActivator {
	
	private DebeziumActivator debeziumActivator;
	
	private PdsActivator pdsActivator;
	
	@Override
	public void started() {
		debeziumActivator = new DebeziumActivator();
		pdsActivator = new PdsActivator();
		debeziumActivator.started();
		pdsActivator.started();
	}
	
	@Override
	public void stopped() {
		debeziumActivator.stopped();
		pdsActivator.stopped();
	}
}
