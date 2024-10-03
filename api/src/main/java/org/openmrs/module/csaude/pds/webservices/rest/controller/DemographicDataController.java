package org.openmrs.module.csaude.pds.webservices.rest.controller;

import org.apache.commons.lang.StringUtils;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.csaude.pds.listener.config.utils.PdsConstants;
import org.openmrs.module.csaude.pds.listener.config.utils.PdsUtils;
import org.openmrs.module.csaude.pds.listener.dto.ResponseDataDTO;
import org.openmrs.module.csaude.pds.listener.entity.ClientNameManager;
import org.openmrs.module.csaude.pds.webservices.rest.exceptionhandler.ResourceMissingParameterException;
import org.openmrs.module.csaude.pds.webservices.rest.exceptionhandler.ResourceNotAllowedException;
import org.openmrs.module.csaude.pds.webservices.rest.exceptionhandler.ResourceUnauthorizedException;
import org.openmrs.module.csaude.pds.webservices.rest.utils.DemographicDataUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/rest/" + PdsConstants.REST_VERSION + "/patient")
public class DemographicDataController {
	
	@GetMapping("/info/updated-data")
	public ResponseEntity<?> getUpdateDemographicData(@RequestParam Map<String, String> params) {
		
		String count = PdsUtils.getGlobalPropertyValue(PdsConstants.GP_DEFAULT_COUNT_FOR_PATIENT_SERVICES);
		String clientName = ClientNameManager.fromName(params.get("client_name"));
		validateUserAccess(count, clientName);
		
		ResponseDataDTO responseDataDTO = DemographicDataUtils.fetchPatientDemographicData(count, clientName);
		
		return new ResponseEntity<>(responseDataDTO, HttpStatus.OK);
	}
	
	/*
	Commit offset for data posted by services
	*/
	@PostMapping("/info/updated-data/commit")
	public ResponseEntity<?> commitOffset(@RequestParam Map<String, String> params) {
		
		String count = PdsUtils.getGlobalPropertyValue(PdsConstants.GP_DEFAULT_COUNT_FOR_PATIENT_SERVICES);
		String clientName = ClientNameManager.fromName(params.get("client_name"));
		
		validateUserAccess(count, clientName);
		DemographicDataUtils.commitOffset(clientName);
		return ResponseEntity.noContent().build();
	}
	
	private void validateUserAccess(String count, String clientName) {
		
		User user = Context.getAuthenticatedUser();
		
		if (StringUtils.isBlank(count) || StringUtils.isBlank(clientName)) {
			throw new ResourceMissingParameterException("The are missing parameters in the request: clientName");
		}
		
		if (user == null) {
			throw new ResourceUnauthorizedException("");
		}
		
		if (!user.getUsername().toUpperCase().equals(clientName)) {
			throw new ResourceNotAllowedException(clientName);
		}
	}
}
