package org.openmrs.module.csaude.pds.listener.dto;

import java.util.ArrayList;
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
	
	private List<ExtensionDTO> extension;
	
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
	
	public List<ExtensionDTO> getExtension() {
		return extension;
	}
	
	public void setExtension(List<ExtensionDTO> extension) {
		this.extension = extension;
	}
	
	public void addPatientState(PatientSateDTO patientSateDTO, String patientStateUrl) {
		if (this.extension == null) {
			this.extension = new ArrayList<>();
		}
		this.extension.add(new ExtensionDTO(patientStateUrl, patientSateDTO.getStatePermanenceCode()));
	}
}
