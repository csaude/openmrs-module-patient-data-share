package org.openmrs.module.csaude.pds.listener.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.openmrs.Patient;
import org.openmrs.PersonAttribute;
import org.openmrs.module.csaude.pds.listener.config.utils.PdsUtils;
import org.openmrs.module.csaude.pds.listener.dto.PatientSateDTO;
import org.openmrs.module.csaude.pds.listener.entity.DemographicDataOffset;
import org.openmrs.module.csaude.pds.listener.entity.DemographicDataQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DemographicDataQueueDao extends DaoBase {
	
	private static final Logger logger = LoggerFactory.getLogger(DemographicDataQueueDao.class);
	
	public void createDemographicDataQueue(DemographicDataQueue demographicDataQueue) throws RuntimeException {
		executeWithTransaction(sessionFactory, session -> {
			try {
				logger.debug("Saving Patient demographic data:  uuid {} ", demographicDataQueue.getPatientUuid());
				session.saveOrUpdate(demographicDataQueue);
			}
			catch (Exception e) {
				throw new RuntimeException("An error occurred saving patient demographic data : " + "uuid "
				        + demographicDataQueue.getPatientUuid(), e);
			}
			return null;
		});
	}
	
	public List<PersonAttribute> getPersonAttributeByPersonType(String personAttributeTypeUuid, Integer personId) {
		return executeWithTransaction(sessionFactory, session -> {
			Criteria criteria = session.createCriteria(PersonAttribute.class, "personAttribute");
			criteria.add(Restrictions.eq("personAttribute.person.personId", personId));
			criteria.add(Restrictions.eq("personAttribute.voided", false));
			criteria.createAlias("personAttribute.attributeType", "personAttributeType");
			criteria.add(Restrictions.eq("personAttributeType.uuid", personAttributeTypeUuid));
			
			return (List<PersonAttribute>) criteria.list();
			
		});
	}
	
	public List<DemographicDataQueue> getAllDemographicDataQueues(Integer count,
	        DemographicDataOffset demographicDataOffset) {
		
		return executeWithTransaction(sessionFactory, session -> {
			
			Criteria criteria = session.createCriteria(DemographicDataQueue.class);

			if (demographicDataOffset != null) {
				if (demographicDataOffset.getLastRead() != null) {
					criteria.add(Restrictions.ge("id", demographicDataOffset.getFirstRead()));
				} else {
					criteria.add(Restrictions.gt("id", demographicDataOffset.getFirstRead()));
				}
			}

			if (count != null) {
				criteria.setMaxResults(count);
			}
			criteria.add(Restrictions.eq("active", true));
			criteria.addOrder(Order.asc("id"));
			
			return (List<DemographicDataQueue>) criteria.list();
			
		});
	}
	
	public Set<Patient> getPatientsByIds(List<Integer> patientIds) {
		return executeWithTransaction(sessionFactory, session -> {
			
			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Patient> patientCriteriaQuery = criteriaBuilder.createQuery(Patient.class);
			Root<Patient> patient = patientCriteriaQuery.from(Patient.class);
			patient.fetch("identifiers", JoinType.LEFT).fetch("identifierType", JoinType.LEFT);
			patientCriteriaQuery.select(patient).distinct(true).where(patient.get("patientId").in(patientIds));
			
			List<Patient> patientList = session.createQuery(patientCriteriaQuery).getResultList();
			return new HashSet<>(patientList);
			
		});
		
	}
	
	public DemographicDataOffset getDemographicDataOffset(String clientName) {
		return executeWithTransaction(sessionFactory, session -> {
			
			Criteria criteria = session.createCriteria(DemographicDataOffset.class, "offset");
			criteria.add(Restrictions.eq("offset.clientName", clientName));
			
			return (DemographicDataOffset) criteria.uniqueResult();
		});
	}
	
	public void updateOrSaveDemographicOffset(DemographicDataOffset demographicDataOffset) {
		executeWithTransaction(sessionFactory, session -> {
			try {
				
				logger.debug("Saving Patient demographic offset: {} for client: {}",
				    " Start from " + demographicDataOffset.getFirstRead() + "to " + demographicDataOffset.getLastRead(),
				    demographicDataOffset.getClientName());
				
				if (demographicDataOffset.isCreated()) {
					
					Query query = session.createQuery(
					    "UPDATE DemographicDataOffset SET firstRead=:firstRead, lastRead=:lastRead WHERE id = :id");
					query.setParameter("firstRead", demographicDataOffset.getFirstRead());
					query.setParameter("lastRead", demographicDataOffset.getLastRead());
					query.setParameter("id", demographicDataOffset.getId());
					query.executeUpdate();
				} else {
					session.save(demographicDataOffset);
				}
			}
			catch (Exception e) {
				throw new RuntimeException("An error occurred saving patient demographic offset : " + " Start from "
				        + demographicDataOffset.getFirstRead() + "to " + demographicDataOffset.getLastRead()
				        + " for client: " + demographicDataOffset.getClientName(), e);
			}
			return null;
		});
	}
	
	public List<PatientSateDTO> fetchPatientState(Integer patientId) throws IOException {
		return executeWithTransaction(sessionFactory, session -> {
			Path sqlPath = new ClassPathResource("queries/patient_state.sql").getFile().toPath();
			String sql = Files.readString(sqlPath);
			
			var query = session.createSQLQuery(sql);
			query.setParameter("patientId", patientId);
			List<Object[]> rel = query.list();
			return PdsUtils.getPatientSates(rel);
		});
		
	}
}
