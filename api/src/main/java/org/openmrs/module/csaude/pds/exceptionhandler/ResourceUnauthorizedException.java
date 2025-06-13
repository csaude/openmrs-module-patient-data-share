package org.openmrs.module.csaude.pds.exceptionhandler;

import org.openmrs.api.APIException;

public class ResourceUnauthorizedException extends APIException {
	
	public ResourceUnauthorizedException(String message) {
		super(message);
	}
}
