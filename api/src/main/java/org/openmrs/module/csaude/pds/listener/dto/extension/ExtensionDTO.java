package org.openmrs.module.csaude.pds.listener.dto.extension;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExtensionDTO extends BaseExtensionDTO {
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
	private Date valueDate;
	
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
		this.valueDate = valueDate;
	}
	
	public Date getValueDate() {
		return valueDate;
	}
	
	public void setValueDate(Date valueDate) {
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
