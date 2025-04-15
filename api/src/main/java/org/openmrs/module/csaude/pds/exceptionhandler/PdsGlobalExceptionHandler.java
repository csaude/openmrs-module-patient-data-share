package org.openmrs.module.csaude.pds.exceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class PdsGlobalExceptionHandler {
	
	@ExceptionHandler(ResourceMissingParameterException.class)
	public ResponseEntity<?> handleMissingParameter(ResourceMissingParameterException ex, WebRequest request) {
		ErrorDetails errorDetails = new ErrorDetails(ErrorCode.MISSING_PARAMETER.toString(), ex.getMessage(),
		        HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> handleNotFound(ResourceNotFoundException ex, WebRequest request) {
		ErrorDetails errorDetails = new ErrorDetails(ErrorCode.NOT_FOUND.toString(), "No data found on the current page.",
		        HttpStatus.NOT_FOUND.value());
		return new ResponseEntity<>(errorDetails, HttpStatus.NO_CONTENT);
	}
	
	@ExceptionHandler(ResourceUnauthorizedException.class)
	public ResponseEntity<?> handleUnauthorized(ResourceUnauthorizedException ex, WebRequest request) {
		ErrorDetails errorDetails = new ErrorDetails(ErrorCode.UNAUTHORIZED.toString(),
		        "Invalid or missing authorization token.", HttpStatus.UNAUTHORIZED.value());
		return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler(ResourceNotAllowedException.class)
	public ResponseEntity<?> handlePermittedResource(ResourceNotAllowedException ex, WebRequest request) {
		ErrorDetails errorDetails = new ErrorDetails(ErrorCode.UNAUTHORIZED.toString(),
		        "The user is not allowed to access the resource of " + ex.getMessage(), HttpStatus.UNAUTHORIZED.value());
		return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
	}
}
