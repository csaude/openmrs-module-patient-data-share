package org.openmrs.module.csaude.pds.listener.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

public class BaseDTO {
	
	//patient uuid
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String id;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
}
