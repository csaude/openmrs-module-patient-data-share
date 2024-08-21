package org.openmrs.module.csaude.pds.webservices.rest.controller;

import org.apache.commons.lang.StringUtils;
import org.openmrs.api.context.Context;
import org.openmrs.module.csaude.pds.listener.config.utils.PdsConstants;
import org.openmrs.module.csaude.pds.listener.dto.ResponseDataDTO;
import org.openmrs.module.csaude.pds.webservices.rest.exceptionhandler.ResourceMissingParameterException;
import org.openmrs.module.csaude.pds.webservices.rest.exceptionhandler.ResourceUnauthorizedException;
import org.openmrs.module.csaude.pds.webservices.rest.utils.DemographicDataUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/rest/" + PdsConstants.REST_VERSION + "/patient")
public class DemographicDataController {
	
	/*
	Get created or Update patient based in the count
	 */
	@GetMapping("/info/updated-data")
	public ResponseEntity<?> getUpdateDemographicData(@RequestParam Map<String, String> params) {
		String count = params.get("count");
		String clientName = params.get("client_name");
		
		if (Context.getAuthenticatedUser() == null) {
			throw new ResourceUnauthorizedException("");
		}
		
		if (StringUtils.isBlank(count) || StringUtils.isBlank(clientName)) {
			throw new ResourceMissingParameterException( "The are missing parameters in the request: clientName or count");
		}
		
		ResponseDataDTO responseDataDTO = DemographicDataUtils.fetchPatientDemographicData(count, clientName);
		
		return new ResponseEntity<>(responseDataDTO, HttpStatus.OK);
		
	}
}
