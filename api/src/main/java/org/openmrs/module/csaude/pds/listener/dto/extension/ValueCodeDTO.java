package org.openmrs.module.csaude.pds.listener.dto.extension;

public class ValueCodeDTO extends BaseExtensionDTO {
	
	private String valueCode;
	
	public ValueCodeDTO(String url, String valueCode) {
		super(url);
		this.valueCode = valueCode;
	}
	
	public String getValueCode() {
		return valueCode;
	}
	
	public void setValueCode(String valueCode) {
		this.valueCode = valueCode;
	}
}
