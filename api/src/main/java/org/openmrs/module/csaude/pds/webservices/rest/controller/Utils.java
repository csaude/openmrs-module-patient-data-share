package org.openmrs.module.csaude.pds.webservices.rest.controller;

import org.apache.commons.lang.StringUtils;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.csaude.pds.webservices.rest.exceptionhandler.ErrorCode;
import org.openmrs.module.csaude.pds.webservices.rest.exceptionhandler.ErrorDetails;
import org.openmrs.module.csaude.pds.webservices.rest.exceptionhandler.ResourceMissingParameterException;
import org.openmrs.module.csaude.pds.webservices.rest.exceptionhandler.ResourceNotAllowedException;
import org.openmrs.module.csaude.pds.webservices.rest.exceptionhandler.ResourceNotFoundException;
import org.openmrs.module.csaude.pds.webservices.rest.exceptionhandler.ResourceUnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class Utils {
	
	public static ResponseEntity<?> resumedExceptionHandler(Exception ex) {
		if (ex instanceof ResourceMissingParameterException) {
			ErrorDetails errorDetails = new ErrorDetails(ErrorCode.MISSING_PARAMETER.toString(), ex.getMessage(), 400);
			
			return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
		} else if (ex instanceof ResourceUnauthorizedException) {
			ErrorDetails errorDetails = new ErrorDetails(ErrorCode.UNAUTHORIZED.toString(),
			        "Invalid or missing authorization token.", 401);
			
			return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
		} else if (ex instanceof ResourceNotAllowedException) {
			ErrorDetails errorDetails = new ErrorDetails(ErrorCode.UNAUTHORIZED.toString(),
			        "The user is not allowed to access the resource of " + ex.getMessage(), 403);
			
			return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
			
		} else if (ex instanceof ResourceNotFoundException) {
			ErrorDetails errorDetails = new ErrorDetails(ErrorCode.NOT_FOUND.toString(),
			        "No data found on the current page.", 204);
			
			return new ResponseEntity<>(errorDetails, HttpStatus.NO_CONTENT);
		} else {
			ErrorDetails errorDetails = new ErrorDetails("INTERNAL_SERVER_ERROR",
			        "An unexpected error occurred: " + ex.getMessage(), 500);
			
			return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	public static void validateUserAccess(String count, String clientName) {
		
		User user = Context.getAuthenticatedUser();
		
		if (StringUtils.isBlank(count) || StringUtils.isBlank(clientName)) {
			throw new ResourceMissingParameterException("The are missing parameters in the request: " + clientName);
		}
		
		if (user == null) {
			throw new ResourceUnauthorizedException("");
		}
		
		if (!user.getUsername().toUpperCase().equals(clientName)) {
			throw new ResourceNotAllowedException(clientName);
		}
	}
}
