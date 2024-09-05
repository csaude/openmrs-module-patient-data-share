package org.openmrs.module.csaude.pds.webservices.rest.exceptionhandler;

import org.openmrs.api.APIException;

public class ResourceNotFoundException extends APIException {
	
	public ResourceNotFoundException(String message) {
		super(message);
	}
}
