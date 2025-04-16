package org.openmrs.module.csaude.pds.exceptionhandler;

import org.openmrs.api.APIException;

public class ResourceNotAllowedException extends APIException {
	
	public ResourceNotAllowedException(String message) {
		super(message);
	}
}
