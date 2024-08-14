package org.openmrs.module.csaude.pds.listener.dto;

public class TelecomDTO extends BaseDTO {
	
	private String system;
	
	private String use;
	
	private String value;
	
	public String getSystem() {
		return system;
	}
	
	public void setSystem(String system) {
		this.system = system;
	}
	
	public String getUse() {
		return use;
	}
	
	public void setUse(String use) {
		this.use = use;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
}
