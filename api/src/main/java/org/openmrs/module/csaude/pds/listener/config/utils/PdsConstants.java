package org.openmrs.module.csaude.pds.listener.config.utils;

public class PdsConstants {
	
	public final static String MODULE_ID = "pds";
	
	public final static String GP_PHONE_MOBILE = MODULE_ID + ".person.attribute.type.mobile.phone";
	
	public final static String GP_PHONE_HOME = MODULE_ID + ".person.attribute.type.home.phone";
	
	public final static String[] WATCHED_TABLES = new String[] { "person", "patient", "person_name", "person_address",
	        "patient_identifier", "person_attribute" };
	
	public final static String REST_VERSION = "v1";
	
	public final static String IDENTIFIER_TYPE_SYSTEM_MAP = "8d793bee-c2cc-11de-8d13-0010c6dffd0f^http://metadata.epts.e-saude.net/dictionary/patient-identifiers/openmrs-num, e2b966d0-1d5f-11e0-b929-000c29ad1d07^http://metadata.epts.e-saude.net/dictionary/patient-identifiers/nid-tarv, e2b9682e-1d5f-11e0-b929-000c29ad1d07^http://metadata.epts.e-saude.net/dictionary/patient-identifiers/bi, e2b9698c-1d5f-11e0-b929-000c29ad1d07^http://metadata.epts.e-saude.net/dictionary/patient-identifiers/cdg-ats, e2b96ad6-1d5f-11e0-b929-000c29ad1d07^http://metadata.epts.e-saude.net/dictionary/patient-identifiers/cdg-ptv-pre-natal, e2b96c16-1d5f-11e0-b929-000c29ad1d07^http://metadata.epts.e-saude.net/dictionary/patient-identifiers/cdg-its, e2b96d56-1d5f-11e0-b929-000c29ad1d07^http://metadata.epts.e-saude.net/dictionary/patient-identifiers/cdg-ptc-maternidade, e2b97b70-1d5f-11e0-b929-000c29ad1d07^http://metadata.epts.e-saude.net/dictionary/patient-identifiers/nid-ccr, e2b97cec-1d5f-11e0-b929-000c29ad1d07^http://metadata.epts.e-saude.net/dictionary/patient-identifiers/pcr-num-reg, e2b97e40-1d5f-11e0-b929-000c29ad1d07^http://metadata.epts.e-saude.net/dictionary/patient-identifiers/nit-tb, e2b97f8a-1d5f-11e0-b929-000c29ad1d07^http://metadata.epts.e-saude.net/dictionary/patient-identifiers/num-cancro-cervical, e89c8925-35cc-4a29-9002-6b36bf3fd47f^http://metadata.epts.e-saude.net/dictionary/patient-identifiers/nuic, 79ad599a-50df-48f8-865c-0095ec9a9d01^http://metadata.epts.e-saude.net/dictionary/patient-identifiers/nid-disa, 1c72703d-fb55-439e-af4f-ef39a1049e19^http://metadata.epts.e-saude.net/dictionary/patient-identifiers/cram-id, bce7c891-27e9-42ec-abb0-aec3a641175e^http://metadata.epts.e-saude.net/dictionary/patient-identifiers/nid-prep, a5d38e09-efcb-4d91-a526-50ce1ba5011a^http://metadata.epts.e-saude.net/dictionary/patient-identifiers/openempi-id, 05a29f94-c0ed-11e2-94be-8c13b969e334^http://metadata.epts.e-saude.net/dictionary/patient-identifiers/openmrs-id";
	
	public final static String GP_DEFAULT_COUNT_FOR_PATIENT_SERVICES = MODULE_ID + ".patient.default.count";
	
	public final static String GP_CLIENT_NAMES = MODULE_ID + ".patient.client.names";
	
	public final static String URL_FOR_PATIENT_STATE = "http://metadata.epts.e-saude.net/dictionary/patient/clinical/state";
	
	public final static String URL_FOR_PATIENT_STATE_CODE = "http://metadata.epts.e-saude.net/dictionary/patient/clinical/state/code";
	
	public final static String URL_FOR_PATIENT_STATE_DATE = "http://metadata.epts.e-saude.net/dictionary/patient/clinical/state/date";
	
	public static final String PATIENT_UUID_SYSTEM_VALUE = "https://metadata.epts.e-saude.net/dictionary/patient-identifier/patient-uuid";
}
