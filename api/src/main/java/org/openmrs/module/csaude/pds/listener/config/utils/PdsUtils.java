package org.openmrs.module.csaude.pds.listener.config.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.openmrs.api.APIAuthenticationException;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.csaude.pds.exceptionhandler.ResourceUnauthorizedException;
import org.openmrs.module.csaude.pds.listener.dto.PatientSateDTO;
import org.openmrs.module.debezium.entity.DatabaseEvent;
import org.openmrs.module.debezium.entity.DatabaseOperation;
import org.openmrs.module.debezium.entity.DebeziumEventQueue;

import java.math.BigInteger;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Contains utility methods
 */
public class PdsUtils {
	
	public static Map<String, DatabaseOperation> DATABASE_OPERATIONS;
	
	public static Map<String, DatabaseEvent.Snapshot> SNAPSHOT;
	
	private static final ObjectMapper objectMapper = new ObjectMapper();
	
	static {
		DATABASE_OPERATIONS = new HashMap<>();
		DATABASE_OPERATIONS.put("C", DatabaseOperation.CREATE);
		DATABASE_OPERATIONS.put("D", DatabaseOperation.DELETE);
		DATABASE_OPERATIONS.put("r", DatabaseOperation.READ);
		DATABASE_OPERATIONS.put("U", DatabaseOperation.UPDATE);
		
		SNAPSHOT = new HashMap<>();
		SNAPSHOT.put("FALSE", DatabaseEvent.Snapshot.FALSE);
		SNAPSHOT.put("TRUE", DatabaseEvent.Snapshot.TRUE);
		SNAPSHOT.put("LAST", DatabaseEvent.Snapshot.LAST);
	}
	
	/**
	 * @see Paths#get(String, String...)
	 */
	public static Path createPath(String parent, String... additionalPaths) {
		return Paths.get(parent, additionalPaths);
	}
	
	public static Integer getPersonId(DebeziumEventQueue event) throws JsonProcessingException {
		
		final String tableName = event.getTableName();
		String columnName = "person_id";
		
		Set<String> tablesRelatedToPatient = new HashSet<>(Arrays.asList("patient", "patient_identifier"));
		if (tablesRelatedToPatient.contains(tableName)) {
			columnName = "patient_id";
		}
		
		Object patientId;
		if (DatabaseOperation.DELETE == DATABASE_OPERATIONS.get(event.getOperation())) {
			Map<String, Object> previousState = objectMapper.readValue(event.getPreviousState(),
			    new TypeReference<HashMap<String, Object>>() {});
			patientId = previousState.get(columnName);
		} else {
			Map<String, Object> newState = objectMapper.readValue(event.getNewState(),
			    new TypeReference<HashMap<String, Object>>() {});
			patientId = newState.get(columnName);
		}
		
		return Integer.valueOf(patientId.toString());
	}
	
	/**
	 * Retrieves the value of a global property with the specified name
	 *
	 * @param gpName the global property name
	 * @return the global property value
	 */
	public static String getGlobalPropertyValue(String gpName) {
		
		try {
			String value = Context.getAdministrationService().getGlobalProperty(gpName);
			if (StringUtils.isBlank(value)) {
				throw new APIException("No value set for the global property named: " + gpName);
			}
			
			return value;
		}
		catch (APIAuthenticationException e) {
			throw new ResourceUnauthorizedException("");
		}
	}
	
	public static String getPatientIdentifierUri(String patientIdentifierUuid) {
		Map idSystemMap = new HashMap();
		String maps = PdsConstants.IDENTIFIER_TYPE_SYSTEM_MAP;
		if (StringUtils.isNotBlank(maps)) {
			for (String map : maps.trim().split(",")) {
				String[] details = map.trim().split("\\^");
				idSystemMap.put(details[0].trim(), details[1].trim());
			}
		}
		
		return (String) idSystemMap.get(patientIdentifierUuid);
	}
	
	public static List<PatientSateDTO> getPatientSates(List<Object[]> states) {
		List<PatientSateDTO> patientSates = new ArrayList<>();
		if (states != null) {
			states.forEach(state -> {
				BigInteger PatientId = state[0] != null ? (BigInteger) state[0] : null;
				Timestamp stateDate = state[1] != null ? (Timestamp) state[1] : null;
				BigInteger permanenceStateId = state[2] != null ? (BigInteger) state[2] : null;
				String permanenceStateCode = state[3] != null ? (String) state[3] : null;
				
				PatientSateDTO patientSate = new PatientSateDTO(PatientId, stateDate, permanenceStateId,
				        permanenceStateCode);
				patientSates.add(patientSate);
			});
		}
		return patientSates;
	}
	
	public static String convertTimeStamp(Date valueDate) {
		if (valueDate != null) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			return formatter.format(valueDate);
		}
		return null;
	}
}
