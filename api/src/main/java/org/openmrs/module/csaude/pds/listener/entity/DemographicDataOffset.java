package org.openmrs.module.csaude.pds.listener.entity;

import com.sun.istack.NotNull;
import org.openmrs.module.csaude.pds.listener.entity.core.Base;

import javax.persistence.*;

@Entity
@Table(name = "demographic_data_offset")
public class DemographicDataOffset extends Base {
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "offset_id")
	private DemographicDataQueue offsetId;
	
	@NotNull
	@Column(name = "client_name")
	private ClientName clientName;
	
	public DemographicDataOffset() {
	}
	
	public DemographicDataQueue getOffsetId() {
		return offsetId;
	}
	
	public void setOffsetId(DemographicDataQueue offsetId) {
		this.offsetId = offsetId;
	}
	
	public ClientName getClientName() {
		return clientName;
	}
	
	public void setClientName(ClientName clientName) {
		this.clientName = clientName;
	}
}
