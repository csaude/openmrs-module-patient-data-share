package org.openmrs.module.csaude.pds.listener.dto;

import java.util.List;
import java.util.Map;

public class AddressDTO extends BaseDTO {
	
	private String country;
	
	private String state;
	
	private String district;
	
	private List<String> line;
	
	private Map<String, String> period;
	
	public String getCountry() {
		return country;
	}
	
	public void setCountry(String country) {
		this.country = country;
	}
	
	public Map<String, String> getPeriod() {
		return period;
	}
	
	public void setPeriod(Map<String, String> period) {
		this.period = period;
	}
	
	public List<String> getLine() {
		return line;
	}
	
	public void setLine(List<String> line) {
		this.line = line;
	}
	
	public String getState() {
		return state;
	}
	
	public void setState(String state) {
		this.state = state;
	}
	
	public String getDistrict() {
		return district;
	}
	
	public void setDistrict(String district) {
		this.district = district;
	}
}
