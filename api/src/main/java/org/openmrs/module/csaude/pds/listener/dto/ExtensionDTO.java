package org.openmrs.module.csaude.pds.listener.dto;

public class ExtensionDTO {
	
	private String url;
	
	private String valueCode;
	
	public ExtensionDTO(String url, String valueCode) {
		this.url = url;
		this.valueCode = valueCode;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getValueCode() {
		return valueCode;
	}
	
	public void setValueCode(String valueCode) {
		this.valueCode = valueCode;
	}
}
