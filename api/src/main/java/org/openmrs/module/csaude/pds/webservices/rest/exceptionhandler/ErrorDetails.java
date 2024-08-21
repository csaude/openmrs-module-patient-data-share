package org.openmrs.module.csaude.pds.webservices.rest.exceptionhandler;

public class ErrorDetails {
	
	private String errorCode;
	
	private String description;
	
	private int code;
	
	public ErrorDetails(String errorCode, String description, int code) {
		this.errorCode = errorCode;
		this.description = description;
		this.code = code;
	}
	
	public String getErrorCode() {
		return errorCode;
	}
	
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public int getCode() {
		return code;
	}
	
	public void setCode(int code) {
		this.code = code;
	}
}
