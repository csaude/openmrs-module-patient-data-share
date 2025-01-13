package org.openmrs.module.csaude.pds.listener.dto.extension;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class ValueDateDTO extends BaseExtensionDTO {
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
	private Date valueDate;
	
	public ValueDateDTO(String url, Date valueDate) {
		super(url);
		this.valueDate = valueDate;
	}
	
	public Date getValueDate() {
		return valueDate;
	}
	
	public void setValueDate(Date valueDate) {
		this.valueDate = valueDate;
	}
}
