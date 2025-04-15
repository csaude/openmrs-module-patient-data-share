package org.openmrs.module.csaude.pds.exceptionhandler;

import org.openmrs.api.APIException;

public class ResourceMissingParameterException extends APIException {
	
	public ResourceMissingParameterException(String message) {
		super(message);
	}
}
