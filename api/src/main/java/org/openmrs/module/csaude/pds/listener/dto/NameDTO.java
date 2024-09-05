package org.openmrs.module.csaude.pds.listener.dto;

import java.util.List;

public class NameDTO extends BaseDTO {
	
	private String family;
	
	private List<String> given;
	
	private String use;
	
	public String getFamily() {
		return family;
	}
	
	public void setFamily(String family) {
		this.family = family;
	}
	
	public List<String> getGiven() {
		return given;
	}
	
	public void setGiven(List<String> given) {
		this.given = given;
	}
	
	public String getUse() {
		return use;
	}
	
	public void setUse(String use) {
		this.use = use;
	}
}
