package org.openmrs.module.csaude.pds.listener.dto;

public class IdentifierDTO extends BaseDTO {
	
	private String value;
	
	private String system;
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getSystem() {
		return system;
	}
	
	public void setSystem(String system) {
		this.system = system;
	}
}
