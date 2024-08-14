package org.openmrs.module.csaude.pds.listener.dto;

import java.util.ArrayList;
import java.util.List;

public class ResponseDataDTO {
	
	private List<DemographicDataDTO> entry;
	
	public List<DemographicDataDTO> getEntry() {
		return entry;
	}
	
	public void setEntry(List<DemographicDataDTO> entry) {
		this.entry = entry;
	}
	
	public void addEntry(DemographicDataDTO entry) {
		if (this.entry == null) {
			this.entry = new ArrayList<DemographicDataDTO>();
			this.addEntry(entry);
		} else {
			this.entry.add(entry);
		}
	}
}
