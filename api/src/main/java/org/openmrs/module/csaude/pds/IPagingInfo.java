package org.openmrs.module.csaude.pds;

public interface IPagingInfo {
	
	Integer getPageIndex();
	
	Integer getPageSize();
	
	void setTotalRecordCount(Long totalRecordCount);
	
	boolean shouldLoadRecordCount();
	
	void setLoadRecordCount(boolean loadRecordCount);
	
}
