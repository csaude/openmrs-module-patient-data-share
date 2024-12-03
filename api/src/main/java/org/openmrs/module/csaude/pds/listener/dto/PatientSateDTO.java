package org.openmrs.module.csaude.pds.listener.dto;

import java.math.BigInteger;
import java.sql.Timestamp;

public class PatientSateDTO {
	
	private BigInteger patientId;
	
	private Timestamp stateDate;
	
	private BigInteger statePermanenceId;
	
	private String statePermanenceCode;
	
	public PatientSateDTO(BigInteger patientId, Timestamp stateData, BigInteger statePermanenceId,
	    String statePermanenceCode) {
		this.patientId = patientId;
		this.stateDate = stateData;
		this.statePermanenceId = statePermanenceId;
		this.statePermanenceCode = statePermanenceCode;
	}
	
	public PatientSateDTO() {
	}
	
	public BigInteger getPatientId() {
		return patientId;
	}
	
	public void setPatientId(BigInteger patientId) {
		this.patientId = patientId;
	}
	
	public Timestamp getStateDate() {
		return stateDate;
	}
	
	public void setStateDate(Timestamp stateData) {
		this.stateDate = stateData;
	}
	
	public BigInteger getStatePermanenceId() {
		return statePermanenceId;
	}
	
	public void setStatePermanenceId(BigInteger statePermanenceId) {
		this.statePermanenceId = statePermanenceId;
	}
	
	public String getStatePermanenceCode() {
		return statePermanenceCode;
	}
	
	public void setStatePermanenceCode(String statePermanenceCode) {
		this.statePermanenceCode = statePermanenceCode;
	}
}
