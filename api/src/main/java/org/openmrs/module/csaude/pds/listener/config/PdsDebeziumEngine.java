package org.openmrs.module.csaude.pds.listener.config;

import org.openmrs.module.csaude.pds.listener.config.utils.PdsConstants;
import org.openmrs.module.debezium.DatabaseEvent;
import org.openmrs.module.debezium.DebeziumConstants;
import org.openmrs.module.debezium.DebeziumEngineConfig;
import org.openmrs.module.debezium.SnapshotMode;
import org.openmrs.module.debezium.mysql.MySqlSnapshotMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Component(DebeziumConstants.ENGINE_CONFIG_BEAN_NAME)
public class PdsDebeziumEngine implements DebeziumEngineConfig {
	
	private static final Logger log = LoggerFactory.getLogger(PdsDebeziumEngine.class);
	
	private BaseEventProcessor eventProcessor;
	
	@Override
	public void init() {
		eventProcessor = new PdsEventProcessor();
	}
	
	@Override
	public SnapshotMode getSnapshotMode() {
		return MySqlSnapshotMode.SCHEMA_ONLY;
	}
	
	@Override
	public Consumer<DatabaseEvent> getEventListener() {
		return event -> {
			if (log.isDebugEnabled()) {
				log.debug("Received database event -> " + event);
			}
			eventProcessor.process(event);
		};
	}
	
	@Override
	public Set<String> getTablesToInclude() {
		return Arrays.stream(PdsConstants.WATCHED_TABLES).collect(Collectors.toSet());
	}
}
