package org.openmrs.module.csaude.pds.listener.dto;

import org.openmrs.module.csaude.pds.listener.config.utils.PdsConstants;
import org.openmrs.module.csaude.pds.listener.config.utils.PdsUtils;
import org.openmrs.module.csaude.pds.listener.dto.extension.BaseExtensionDTO;
import org.openmrs.module.csaude.pds.listener.dto.extension.ValueCodeDTO;
import org.openmrs.module.csaude.pds.listener.dto.extension.ValueDateDTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DemographicDataDTO {
	
	public static final String RESOURCE_TYPE = "patient";
	
	private String resourceType;
	
	private String birthDate;
	
	private boolean deceasedBoolean;
	
	private String gender;
	
	private boolean active;
	
	private List<IdentifierDTO> identifier;
	
	private List<NameDTO> name;
	
	private List<AddressDTO> address;
	
	private List<TelecomDTO> telecom;
	
	private List<BaseExtensionDTO> extension;
	
	public String getResourceType() {
		return resourceType;
	}
	
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}
	
	public String getBirthDate() {
		return birthDate;
	}
	
	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}
	
	public String getGender() {
		return gender;
	}
	
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	public boolean isDeceasedBoolean() {
		return deceasedBoolean;
	}
	
	public void setDeceasedBoolean(boolean deceasedBoolean) {
		this.deceasedBoolean = deceasedBoolean;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public List<IdentifierDTO> getIdentifier() {
		return identifier;
	}
	
	public void setIdentifier(List<IdentifierDTO> identifier) {
		this.identifier = identifier;
	}
	
	public List<NameDTO> getName() {
		return name;
	}
	
	public void setName(List<NameDTO> name) {
		this.name = name;
	}
	
	public List<AddressDTO> getAddress() {
		return address;
	}
	
	public void setAddress(List<AddressDTO> address) {
		this.address = address;
	}
	
	public List<TelecomDTO> getTelecom() {
		return telecom;
	}
	
	public void setTelecom(List<TelecomDTO> telecom) {
		this.telecom = telecom;
	}
	
	public List<BaseExtensionDTO> getExtension() {
		return extension;
	}
	
	public void setExtension(List<BaseExtensionDTO> extension) {
		this.extension = extension;
	}
	
	public void addPatientState(PatientSateDTO patientSateDTO) {
		if (this.extension == null) {
			this.extension = new ArrayList<>();
		}
		
		String valueCodeUrl = PdsUtils.getGlobalPropertyValue(PdsConstants.GP_URL_FOR_PATIENT_STATE_DATA);
		String valueDateUrl = PdsUtils.getGlobalPropertyValue(PdsConstants.GP_URL_FOR_PATIENT_STATE_DATE);
		
		this.extension.add(new ValueCodeDTO(valueCodeUrl, patientSateDTO.getStatePermanenceCode()));
		this.extension.add(new ValueDateDTO(valueDateUrl, new Date(patientSateDTO.getStateDate().getTime())));
	}
}
