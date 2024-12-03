package org.openmrs.module.csaude.pds.listener.config.utils;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.csaude.pds.listener.dto.PatientSateDTO;
import org.openmrs.module.debezium.DatabaseEvent;
import org.openmrs.module.debezium.DatabaseOperation;

import java.math.BigInteger;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Contains utility methods
 */
public class PdsUtils {
	
	/**
	 * @see Paths#get(String, String...)
	 */
	public static Path createPath(String parent, String... additionalPaths) {
		return Paths.get(parent, additionalPaths);
	}
	
	public static Integer getPersonId(DatabaseEvent event) {
		final String tableName = event.getTableName();
		String columnName = "person_id";
		
		Set<String> tablesRelatedToPatient = new HashSet<>(Arrays.asList("patient", "patient_identifier"));
		if (tablesRelatedToPatient.contains(tableName)) {
			columnName = "patient_id";
		}
		
		Object patientId;
		if (DatabaseOperation.DELETE == event.getOperation()) {
			patientId = event.getPreviousState().get(columnName);
		} else {
			patientId = event.getNewState().get(columnName);
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
		String value = Context.getAdministrationService().getGlobalProperty(gpName);
		if (StringUtils.isBlank(value)) {
			throw new APIException("No value set for the global property named: " + gpName);
		}
		
		return value;
	}
	
	public static String getPatientIdentifierUri(String patientIdentifierUuid) {
		Map idSystemMap = new HashMap();
		String maps = getGlobalPropertyValue(PdsConstants.GP_IDENTIFIER_TYPE_SYSTEM_MAP);
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
}
