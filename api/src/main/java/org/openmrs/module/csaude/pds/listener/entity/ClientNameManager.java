package org.openmrs.module.csaude.pds.listener.entity;

import org.apache.logging.log4j.util.Strings;
import org.openmrs.module.csaude.pds.listener.config.utils.PdsConstants;
import org.openmrs.module.csaude.pds.listener.config.utils.PdsUtils;
import org.openmrs.module.csaude.pds.webservices.rest.exceptionhandler.ResourceMissingParameterException;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ClientNameManager {
	
	private static Set<String> clientNames = new HashSet<>();
	
	static {
		clientNames.add("IDMED");
		loadClientNames();
	}
	
	public static void loadClientNames() {
		String namesFromProperties = PdsUtils.getGlobalPropertyValue(PdsConstants.GP_CLIENT_NAMES);
		if (!Strings.isBlank(namesFromProperties)) {
			String[] clientNameArray = namesFromProperties.split(",");
			Collections.addAll(clientNames, clientNameArray);
		}
		
	}
	
	public static boolean isValidClientName(String clientName) {
		return clientNames.contains(clientName.toUpperCase());
	}
	
	public static String fromName(String name) {
		if (isValidClientName(name)) {
			return name.toUpperCase();
		}
		
		throw new ResourceMissingParameterException("No client registered with the name: " + name);
	}
}
