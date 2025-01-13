package org.openmrs.module.csaude.pds.listener.dto.extension;

public class BaseExtensionDTO {
	
	private String url;
	
	public BaseExtensionDTO(String url) {
		this.url = url;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
}
