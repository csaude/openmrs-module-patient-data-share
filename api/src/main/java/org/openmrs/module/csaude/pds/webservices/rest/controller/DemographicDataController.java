package org.openmrs.module.csaude.pds.webservices.rest.controller;

import org.openmrs.module.csaude.pds.listener.config.utils.PdsConstants;
import org.openmrs.module.csaude.pds.listener.dto.ResponseDataDTO;
import org.openmrs.module.csaude.pds.webservices.rest.utils.DemographicDataUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/" + PdsConstants.REST_VERSION + "/patient")
public class DemographicDataController {
	
	/*
	Get created or Update patient based in the count
	 */
	@GetMapping("/info/data")
	public ResponseEntity<?> getUpdateDemographicData() {
		try {
			
			ResponseDataDTO responseDataDTO = DemographicDataUtils.fetchPatientDemographicData();
			
			return new ResponseEntity<>(responseDataDTO, HttpStatus.OK);
		}
		catch (Exception e) {
			return new ResponseEntity<>(e.fillInStackTrace(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
}
