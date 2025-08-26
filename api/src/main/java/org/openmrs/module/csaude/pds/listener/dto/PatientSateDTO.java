package org.openmrs.module.csaude.pds.listener.dto;

import java.sql.Timestamp;

public class PatientSateDTO {
	
	private Integer patientId;
	
	private Timestamp stateDate;
	
	private Integer statePermanenceId;
	
	private String statePermanenceCode;
	
	public PatientSateDTO(Integer patientId, Timestamp stateData, Integer statePermanenceId, String statePermanenceCode) {
		this.patientId = patientId;
		this.stateDate = stateData;
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
	
	public Timestamp getStateDate() {
		return stateDate;
	}
	
	public void setStateDate(Timestamp stateData) {
		this.stateDate = stateData;
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
