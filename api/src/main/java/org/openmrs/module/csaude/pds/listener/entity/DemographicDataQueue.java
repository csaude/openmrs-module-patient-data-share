package org.openmrs.module.csaude.pds.listener.entity;

import com.sun.istack.NotNull;
import org.openmrs.module.csaude.pds.listener.entity.core.LifeCycle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "demographic_data_queue")
public class DemographicDataQueue extends LifeCycle {
	
	@NotNull
	@Column(name = "patient_id")
	private Long patientId;
	
	@NotNull
	@Column(name = "patient_uuid")
	private String patientUuid;
	
	public DemographicDataQueue() {
	}
	
	public Long getPatientId() {
		return patientId;
	}
	
	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}
	
	public String getPatientUuid() {
		return patientUuid;
	}
	
	public void setPatientUuid(String patientUuid) {
		this.patientUuid = patientUuid;
	}
}
