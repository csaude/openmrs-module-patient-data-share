package org.openmrs.module.csaude.pds.listener.config.utils;

public class PdsConstants {
	
	public final static String MODULE_ID = "pds";
	
	public final static String GP_PHONE_MOBILE = MODULE_ID + ".person.attribute.type.mobile.phone";
	
	public final static String GP_PHONE_HOME = MODULE_ID + ".person.attribute.type.home.phone";
	
	public final static String[] WATCHED_TABLES = new String[] { "person", "patient", "person_name", "person_address",
	        "patient_identifier", "person_attribute" };
	
	public final static String REST_VERSION = "v1";
	
	public final static String GP_IDENTIFIER_TYPE_SYSTEM_MAP = MODULE_ID + ".identifier.type.system.uri.mappings";
	
	public final static String GP_DEFAULT_COUNT_FOR_PATIENT_SERVICES = MODULE_ID + ".patient.default.count";
	
	public final static String GP_CLIENT_NAMES = MODULE_ID + ".patient.client.names";
	
	public final static String GP_URL_FOR_PATIENT_STATE_DATA = MODULE_ID + ".patient.state.code.url";
	
	public final static String GP_URL_FOR_PATIENT_STATE_DATE = MODULE_ID + ".patient.state.date.url";
	
}
