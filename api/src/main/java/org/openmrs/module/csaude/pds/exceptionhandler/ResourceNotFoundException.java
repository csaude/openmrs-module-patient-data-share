package org.openmrs.module.csaude.pds.exceptionhandler;

import org.openmrs.api.APIException;

public class ResourceNotFoundException extends APIException {
	
	public ResourceNotFoundException(String message) {
		super(message);
	}
}
