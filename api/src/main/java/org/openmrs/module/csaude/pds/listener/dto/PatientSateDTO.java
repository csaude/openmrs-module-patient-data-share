package org.openmrs.module.csaude.pds.listener.dto;

public class PatientSateDTO {
	
	private Integer patientId;
	
	private String stateData;
	
	private Integer statePermanenceId;
	
	private String statePermanenceCode;
	
	public PatientSateDTO(Integer patientId, String stateData, Integer statePermanenceId, String statePermanenceCode) {
		this.patientId = patientId;
		this.stateData = stateData;
		this.statePermanenceId = statePermanenceId;
		this.statePermanenceCode = statePermanenceCode;
	}
	
	public PatientSateDTO() {
	}
	
	public Integer getPatientId() {
		return patientId;
	}
	
	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}
	
	public String getStateData() {
		return stateData;
	}
	
	public void setStateData(String stateData) {
		this.stateData = stateData;
	}
	
	public Integer getStatePermanenceId() {
		return statePermanenceId;
	}
	
	public void setStatePermanenceId(Integer statePermanenceId) {
		this.statePermanenceId = statePermanenceId;
	}
	
	public String getStatePermanenceCode() {
		return statePermanenceCode;
	}
	
	public void setStatePermanenceCode(String statePermanenceCode) {
		this.statePermanenceCode = statePermanenceCode;
	}
}
