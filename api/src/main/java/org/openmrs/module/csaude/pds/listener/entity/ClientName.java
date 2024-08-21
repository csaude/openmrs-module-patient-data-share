package org.openmrs.module.csaude.pds.listener.entity;

import org.openmrs.module.csaude.pds.webservices.rest.exceptionhandler.ResourceMissingParameterException;

public enum ClientName {
	
	IDMED,
	DISA,
	CLINICALSUMMARY;
	
	public static ClientName fromName(String name) {
		for (ClientName clientName : ClientName.values()) {
			if (clientName.name().equalsIgnoreCase(name)) {
				return clientName;
			}
		}
		throw new ResourceMissingParameterException("No client registered with the name: " + name);
	}
	
}
