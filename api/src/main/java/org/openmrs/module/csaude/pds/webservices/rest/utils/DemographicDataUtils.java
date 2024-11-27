package org.openmrs.module.csaude.pds.webservices.rest.utils;

import org.codehaus.plexus.util.StringUtils;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.Person;
import org.openmrs.PersonAddress;
import org.openmrs.PersonAttribute;
import org.openmrs.PersonAttributeType;
import org.openmrs.PersonName;
import org.openmrs.api.PersonService;
import org.openmrs.api.context.Context;
import org.openmrs.module.csaude.pds.listener.config.utils.PdsConstants;
import org.openmrs.module.csaude.pds.listener.config.utils.PdsUtils;
import org.openmrs.module.csaude.pds.listener.dto.AddressDTO;
import org.openmrs.module.csaude.pds.listener.dto.DemographicDataDTO;
import org.openmrs.module.csaude.pds.listener.dto.IdentifierDTO;
import org.openmrs.module.csaude.pds.listener.dto.NameDTO;
import org.openmrs.module.csaude.pds.listener.dto.PatientSateDTO;
import org.openmrs.module.csaude.pds.listener.dto.ResponseDataDTO;
import org.openmrs.module.csaude.pds.listener.dto.TelecomDTO;
import org.openmrs.module.csaude.pds.listener.entity.ClientNameManager;
import org.openmrs.module.csaude.pds.listener.entity.DemographicDataOffset;
import org.openmrs.module.csaude.pds.listener.entity.DemographicDataQueue;
import org.openmrs.module.csaude.pds.listener.service.DemographicDataQueueService;
import org.openmrs.module.csaude.pds.webservices.rest.exceptionhandler.ResourceNotFoundException;
import org.openmrs.util.PrivilegeConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DemographicDataUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(DemographicDataUtils.class);
	
	private static final PersonService personService = Context.getService(PersonService.class);
	
	private static final DemographicDataQueueService demographicDataQueueService = Context
	        .getService(DemographicDataQueueService.class);
	
	private static final String START_DATE = "start";
	
	private static final String END_DATE = "end";
	
	public static ResponseDataDTO fetchPatientDemographicData(String count, String clientName) {
		
		DemographicDataOffset demographicDataOffset = demographicDataQueueService.getDemographicDataOffset(clientName);
		
		List<DemographicDataQueue> demographicData = demographicDataQueueService
		        .getAllDemographicDataQueues(Integer.valueOf(count), demographicDataOffset);
		
		try {
			openSessionWithPrivileges();
			
			if (demographicData.isEmpty()) {
				throw new ResourceNotFoundException("No demographic data found for client: " + clientName);
			}
			Set<Integer> ids = demographicData.stream().map(DemographicDataQueue::getPatientId).collect(Collectors.toSet());
			List<Integer> idsAsSet = new ArrayList<>(ids);
			Set<Patient> patients = demographicDataQueueService.getPatientsByIds(idsAsSet);
			
			updateLastRead(demographicDataOffset, clientName, demographicData.get(0).getId(),
			    demographicData.get(demographicData.size() - 1).getId());
			return createResponseDataDTO(patients);
		}
		finally {
			closeSessionWithPrivileges();
		}
	}
	
	private static void updateLastRead(DemographicDataOffset demographicDataOffset, String clientName, Integer firstRead,
	        Integer lastRead) {
		if (demographicDataOffset == null) {
			demographicDataOffset = new DemographicDataOffset();
			demographicDataOffset.setFirstRead(firstRead);
			demographicDataOffset.setLastRead(lastRead);
			demographicDataOffset.setClientName(clientName);
		} else {
			demographicDataOffset.setLastRead(lastRead);
		}
		demographicDataQueueService.updateOrSaveDemographicOffset(demographicDataOffset);
	}
	
	public static void commitOffset(String client) {
		
		String clientName = ClientNameManager.fromName(client);
		DemographicDataOffset demographicDataOffset = demographicDataQueueService.getDemographicDataOffset(clientName);
		
		if (demographicDataOffset == null) {
			noPageFoundMessage(clientName);
		}
		
		if (demographicDataOffset.getLastRead() == null) {
			noPageFoundMessage(clientName);
		}
		demographicDataOffset.setFirstRead(demographicDataOffset.getLastRead());
		demographicDataOffset.setLastRead(null);
		demographicDataQueueService.updateOrSaveDemographicOffset(demographicDataOffset);
	}
	
	private static void noPageFoundMessage(String clientName) {
		throw new ResourceNotFoundException("No demographic data found for client: " + clientName);
	}
	
	public static ResponseDataDTO createResponseDataDTO(Set<Patient> patients) {
		
		ResponseDataDTO responseDataDTO = new ResponseDataDTO();
		patients.forEach(patient -> responseDataDTO.addEntry(createDemographicDataDTO(patient)));
		
		return responseDataDTO;
	}
	
	private static DemographicDataDTO createDemographicDataDTO(Patient patient) {
		DemographicDataDTO demographicDataDTO = new DemographicDataDTO();
		demographicDataDTO.setResourceType(DemographicDataDTO.RESOURCE_TYPE);
		demographicDataDTO.setBirthDate(patient.getBirthdate().toString());
		demographicDataDTO.setDeceasedBoolean(patient.getDead());
		demographicDataDTO.setGender(patient.getGender());
		demographicDataDTO.setActive(patient.getVoided());
		
		List<AddressDTO> addressDTOs = getAddress(patient);
		demographicDataDTO.setAddress(addressDTOs);
		
		List<NameDTO> nameDTOS = getNames(patient);
		demographicDataDTO.setName(nameDTOS);
		
		List<IdentifierDTO> identifierDTOS = getPatientIdentifiers(patient);
		demographicDataDTO.setIdentifier(identifierDTOS);
		
		List<TelecomDTO> telecomDTOs = getPhones(patient.getPerson());
		demographicDataDTO.setTelecom(telecomDTOs);
		
		PatientSateDTO patientSateDTO = getPatientStateDTO(patient.getPatientId());
		
		return demographicDataDTO;
		
	}
	
	private static PatientSateDTO getPatientStateDTO(Integer patientId) {
		PatientSateDTO patientSateDTO = null;
		try {
			patientSateDTO = demographicDataQueueService.fetchPatientState(patientId);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
		return patientSateDTO;
	}
	
	private static List<AddressDTO> getAddress(Patient patient) {
		List<AddressDTO> addressDTOS = new ArrayList<>();
		Set<PersonAddress> personAddresses = patient.getAddresses();
		personAddresses.forEach(personAddress -> {
			AddressDTO addressDTO = createAddressDTO(personAddress);
			if (addressDTO != null) {
				addressDTOS.add(addressDTO);
			}
		});
		return addressDTOS;
	}
	
	private static AddressDTO createAddressDTO(PersonAddress personAddress) {
		AddressDTO addressDTO = new AddressDTO();
		if (personAddress != null && !personAddress.getVoided()) {
			
			Map<String, String> period = new HashMap<>();
			if (personAddress.getStartDate() != null && personAddress.getEndDate() != null) {
				period.put(START_DATE, personAddress.getStartDate().toString());
				period.put(END_DATE, personAddress.getEndDate().toString());
				addressDTO.setPeriod(period);
			}
			
			List<String> line = new ArrayList<>();
			addIfNotNull(line, personAddress.getAddress1());
			addIfNotNull(line, personAddress.getAddress2());
			addIfNotNull(line, personAddress.getAddress3());
			addIfNotNull(line, personAddress.getAddress4());
			addIfNotNull(line, personAddress.getAddress5());
			addIfNotNull(line, personAddress.getAddress6());
			
			addressDTO.setCountry(personAddress.getCountry());
			addressDTO.setLine(line);
			addressDTO.setDistrict(personAddress.getCountyDistrict());
			addressDTO.setState(personAddress.getStateProvince());
			addressDTO.setId(personAddress.getUuid());
			
			return addressDTO;
		}
		return null;
	}
	
	private static void addIfNotNull(List<String> line, String address) {
		if (address != null) {
			line.add(address);
		}
	}
	
	private static List<NameDTO> getNames(Patient patient) {
		
		List<NameDTO> nameDTOS = new ArrayList<>();
		Set<PersonName> names = patient.getPerson().getNames();
		names.forEach(personName -> {
			NameDTO nameDTO = createNameDTO(personName);
			if (nameDTO != null) {
				nameDTOS.add(nameDTO);
			}
		});
		
		return nameDTOS;
	}
	
	private static NameDTO createNameDTO(PersonName personName) {
		NameDTO nameDTO = new NameDTO();
		List<String> givenNames = new ArrayList<>();
		
		nameDTO.setUse(NameType.OFFICIAL.toString().toLowerCase());
		
		if (personName != null && !personName.getVoided()) {
			nameDTO.setId(personName.getUuid());
			nameDTO.setFamily(personName.getFamilyName());
			nameDTO.setGiven(givenNames);
			
			if (personName.getGivenName() != null) {
				givenNames.add(personName.getGivenName());
			}
			if (personName.getMiddleName() != null) {
				givenNames.add(personName.getMiddleName());
			}
			return nameDTO;
		}
		
		return null;
	}
	
	private static List<IdentifierDTO> getPatientIdentifiers(Patient patient) {
		List<IdentifierDTO> identifierDTOS = new ArrayList<>();
		Set<PatientIdentifier> patientIdentifiers = patient.getIdentifiers();
		patientIdentifiers.forEach(patientIdentifier -> identifierDTOS.add(createPatientIdentifierDTO(patientIdentifier)));
		
		return identifierDTOS;
	}
	
	private static IdentifierDTO createPatientIdentifierDTO(PatientIdentifier patientIdentifier) {
		IdentifierDTO identifierDTO = new IdentifierDTO();
		identifierDTO.setSystem(PdsUtils.getPatientIdentifierUri(patientIdentifier.getIdentifierType().getUuid()));
		identifierDTO.setId(patientIdentifier.getUuid());
		identifierDTO.setValue(patientIdentifier.getIdentifier());
		return identifierDTO;
	}
	
	private static List<TelecomDTO> getPhones(Person person) {
		List<TelecomDTO> telecomDTOs = new ArrayList<TelecomDTO>();
		String mobilePhoneIdentifier = PdsUtils.getGlobalPropertyValue(PdsConstants.GP_PHONE_MOBILE);
		String homePhoneIdentifier = PdsUtils.getGlobalPropertyValue(PdsConstants.GP_PHONE_HOME);
		
		//{ PrivilegeConstants.GET_PERSON_ATTRIBUTE_TYPES
		PersonAttributeType mobilePersonAttributeType = personService.getPersonAttributeTypeByUuid(mobilePhoneIdentifier);
		PersonAttributeType homePersonAttributeType = personService.getPersonAttributeTypeByUuid(homePhoneIdentifier);
		
		if (mobilePersonAttributeType != null) {
			List<PersonAttribute> mobilesPhones = demographicDataQueueService
			        .getPersonAttributeByPersonType(mobilePersonAttributeType.getUuid(), person.getPersonId());
			if (!mobilesPhones.isEmpty()) {
				mobilesPhones
				        .forEach(personAttribute -> telecomDTOs.add(createTelecomDTO(personAttribute, TelecomType.MOBILE)));
			}
		}
		
		if (homePersonAttributeType != null) {
			List<PersonAttribute> homePhones = demographicDataQueueService
			        .getPersonAttributeByPersonType(homePersonAttributeType.getUuid(), person.getPersonId());
			if (!homePhones.isEmpty()) {
				homePhones.forEach(personAttribute -> telecomDTOs.add(createTelecomDTO(personAttribute, TelecomType.HOME)));
			}
		}
		
		return telecomDTOs;
	}
	
	private static TelecomDTO createTelecomDTO(PersonAttribute personAttribute, TelecomType telecomType) {
		TelecomDTO telecomDTO = new TelecomDTO();
		
		String useType = "";
		
		if (telecomType.equals(TelecomType.MOBILE)) {
			useType = StringUtils.lowerCase(TelecomType.MOBILE.toString());
		} else if (telecomType.equals(TelecomType.HOME)) {
			useType = StringUtils.lowerCase(TelecomType.HOME.toString());
		}
		telecomDTO.setValue(personAttribute.getValue());
		telecomDTO.setUse(StringUtils.lowerCase(useType));
		telecomDTO.setSystem(StringUtils.lowerCase(TelecomType.PHONE.toString()));
		telecomDTO.setId(personAttribute.getUuid());
		
		return telecomDTO;
	}
	
	private static void openSessionWithPrivileges() {
		if (!Context.isSessionOpen()) {
			Context.openSession();
			Context.addProxyPrivilege(PrivilegeConstants.GET_PERSON_ATTRIBUTE_TYPES);
			Context.addProxyPrivilege(PrivilegeConstants.GET_PATIENTS);
			logger.debug("Session opened.");
		}
	}
	
	private static void closeSessionWithPrivileges() {
		if (Context.isSessionOpen()) {
			try {
				Context.removeProxyPrivilege(PrivilegeConstants.GET_PERSON_ATTRIBUTE_TYPES);
				Context.removeProxyPrivilege(PrivilegeConstants.GET_PATIENTS);
			}
			finally {
				Context.clearSession();
				Context.closeSession();
			}
			logger.debug("Session closed.");
		}
	}
	
	public enum TelecomType {
		MOBILE,
		HOME,
		PHONE
	}
	
	public enum NameType {
		OFFICIAL
	}
}
