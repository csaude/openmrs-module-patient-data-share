package org.openmrs.module.csaude.pds.listener.dto.extension;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.openmrs.module.csaude.pds.listener.config.utils.PdsUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExtensionDTO extends BaseExtensionDTO {
	
	private String valueDate;
	
	private String valueCode;
	
	private List<ExtensionDTO> extension;
	
	public ExtensionDTO(String url) {
		super(url);
		this.extension = new ArrayList<ExtensionDTO>();
	}
	
	public ExtensionDTO(String url, String valueCode) {
		super(url);
		this.valueCode = valueCode;
	}
	
	public ExtensionDTO(String url, Date valueDate) {
		super(url);
		this.valueDate = PdsUtils.convertTimeStamp(valueDate);
	}
	
	public String getValueDate() {
		return valueDate;
	}
	
	public void setValueDate(String valueDate) {
		this.valueDate = valueDate;
	}
	
	public String getValueCode() {
		return valueCode;
	}
	
	public void setValueCode(String valueCode) {
		this.valueCode = valueCode;
	}
	
	public List<ExtensionDTO> getExtension() {
		return extension;
	}
	
	public void setExtension(List<ExtensionDTO> extension) {
		this.extension = extension;
	}
}
