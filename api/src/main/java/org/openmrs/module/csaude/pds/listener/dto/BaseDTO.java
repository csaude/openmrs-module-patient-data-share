package org.openmrs.module.csaude.pds.listener.dto;

import org.codehaus.jackson.map.annotate.JsonSerialize;

public class BaseDTO {
	
	//patient uuid
	@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
	private String id;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
}
