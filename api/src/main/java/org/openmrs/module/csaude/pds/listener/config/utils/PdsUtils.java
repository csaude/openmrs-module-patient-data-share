package org.openmrs.module.csaude.pds.listener.config.utils;

import org.openmrs.module.debezium.DatabaseEvent;
import org.openmrs.module.debezium.DatabaseOperation;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
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
}
