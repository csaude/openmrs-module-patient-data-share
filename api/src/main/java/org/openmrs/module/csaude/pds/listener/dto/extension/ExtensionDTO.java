package org.openmrs.module.csaude.pds.listener.dto.extension;

import java.util.ArrayList;
import java.util.List;

public class ExtensionDTO {
	
	private String url;
	
	private List<BaseExtensionDTO> extension;
	
	public ExtensionDTO(String url) {
		this.url = url;
		extension = new ArrayList<BaseExtensionDTO>();
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public List<BaseExtensionDTO> getExtension() {
		return extension;
	}
	
	public void setExtension(List<BaseExtensionDTO> extension) {
		this.extension = extension;
	}
}
