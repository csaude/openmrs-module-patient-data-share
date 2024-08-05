package org.openmrs.module.csaude.pds.listener.entity.core;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class LifeCycle extends Auditable {
	
	@Column(name = "is_active", nullable = false)
	private boolean active;
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public boolean isActive() {
		return active;
	}
}
