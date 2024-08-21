package org.openmrs.module.csaude.pds.webservices.rest.exceptionhandler;

import org.openmrs.api.APIException;

public class ResourceUnauthorizedException extends APIException {
	
	public ResourceUnauthorizedException(String message) {
		super(message);
	}
}
