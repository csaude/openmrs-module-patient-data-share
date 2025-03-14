package org.openmrs.module.csaude.pds.webservices.rest.controller;

import org.openmrs.module.csaude.pds.listener.config.utils.PdsConstants;
import org.openmrs.module.csaude.pds.listener.config.utils.PdsUtils;
import org.openmrs.module.csaude.pds.listener.dto.ResponseDataDTO;
import org.openmrs.module.csaude.pds.listener.entity.ClientNameManager;
import org.openmrs.module.csaude.pds.webservices.rest.utils.DemographicDataUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

import static org.openmrs.module.csaude.pds.webservices.rest.controller.Utils.resumedExceptionHandler;
import static org.openmrs.module.csaude.pds.webservices.rest.controller.Utils.validateUserAccess;

@Controller
@RequestMapping("/rest/" + PdsConstants.REST_VERSION + "/patient/info/updated-data")
public class DemographicDataController {
	
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getUpdateDemographicData(@RequestParam Map<String, String> params) {
		
		String count = PdsUtils.getGlobalPropertyValue(PdsConstants.GP_DEFAULT_COUNT_FOR_PATIENT_SERVICES);
		
		String clientName = null;
		try {
			clientName = ClientNameManager.fromName(params.get("client_name"));
		}
		catch (Exception ex) {
			return resumedExceptionHandler(ex);
		}
		
		ResponseEntity<?> response = validate(count, clientName);
		if (response == null) {
			try {
				ResponseDataDTO responseDataDTO = DemographicDataUtils.fetchPatientDemographicData(count, clientName);
				return new ResponseEntity<>(responseDataDTO, HttpStatus.OK);
				
			}
			catch (Exception ex) {
				return resumedExceptionHandler(ex);
			}
		}
		return response;
	}
	
	/*
	Commit offset for data posted by services
	*/
	@ExceptionHandler
	@PostMapping
	@RequestMapping(method = RequestMethod.POST, value = "/commit")
	public ResponseEntity<?> commitOffset(@RequestParam Map<String, String> params) {
		
		String count = PdsUtils.getGlobalPropertyValue(PdsConstants.GP_DEFAULT_COUNT_FOR_PATIENT_SERVICES);
		String clientName = null;
		try {
			clientName = ClientNameManager.fromName(params.get("client_name"));
		}
		catch (Exception ex) {
			return resumedExceptionHandler(ex);
		}
		
		ResponseEntity<?> response = validate(count, clientName);
		
		if (response == null) {
			try {
				DemographicDataUtils.commitOffset(clientName);
				return ResponseEntity.noContent().build();
			}
			catch (Exception ex) {
				return resumedExceptionHandler(ex);
			}
		}
		return response;
	}
	
	public ResponseEntity<?> validate(String count, String clientName) {
		try {
			validateUserAccess(count, clientName);
		}
		catch (Exception ex) {
			return resumedExceptionHandler(ex);
		}
		
		return null;
	}
	
}
