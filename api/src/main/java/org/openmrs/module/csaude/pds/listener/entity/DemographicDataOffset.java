package org.openmrs.module.csaude.pds.listener.entity;

import com.sun.istack.NotNull;
import org.openmrs.module.csaude.pds.listener.entity.core.Base;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "demographic_data_offset")
public class DemographicDataOffset extends Base {
	
	@NotNull
	@Column(name = "first_read")
	private Integer firstRead;
	
	@NotNull
	@Column(name = "last_read")
	private Integer lastRead;
	
	@NotNull
	@Column(name = "client_name")
	private String clientName;
	
	public DemographicDataOffset() {
	}
	
	public String getClientName() {
		return clientName;
	}
	
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	
	public Integer getFirstRead() {
		return firstRead;
	}
	
	public void setFirstRead(Integer firstRead) {
		this.firstRead = firstRead;
	}
	
	public Integer getLastRead() {
		return lastRead;
	}
	
	public void setLastRead(Integer lastRead) {
		this.lastRead = lastRead;
	}
}
