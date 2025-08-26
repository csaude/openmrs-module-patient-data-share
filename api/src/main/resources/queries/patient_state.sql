SELECT 	patient_id, 
		state_date, 
		CASE estado_permanencia_code
			WHEN 'ABANDONO' THEN '1'
			WHEN 'OBITO' THEN '2'
			WHEN 'TRANSFERIDO_PARA' THEN '3'
			WHEN 'SUSPENSO' THEN '4'
			WHEN 'TRANSFERIDO_DE' THEN '5'
			WHEN 'ACTIVO' THEN '6'
		ELSE  '0' 
		END AS estado_permanencia_id,
		estado_permanencia_code,
		src		
FROM (	select pg.patient_id, 
			   ps.start_date  state_date, 
				CASE ps.state  
					 WHEN 6 THEN 'ACTIVO'
					 WHEN 7 THEN 'TRANSFERIDO_PARA'
					 WHEN 29 THEN 'TRANSFERIDO_DE'
					 WHEN 9 THEN 'ABANDONO'
					 WHEN 8 THEN 'SUSPENSO'
					 WHEN 10 THEN 'OBITO'
					 ELSE null 
				END AS estado_permanencia_code,
				CONCAT('patient_state(', ps.state, ')') AS src
		from (	select max(pg.patient_id) patient_id, max(ps.start_date) state_date 
				from  patient_program pg inner join patient_state ps on pg.patient_program_id=ps.patient_program_id
				where pg.voided = 0 and ps.voided = 0 and pg.program_id = 2 and pg.patient_id = :patient_id
		) max_state inner join patient_program pg on pg.patient_id = max_state.patient_id 
					inner join patient_state   ps on pg.patient_program_id = ps.patient_program_id
		where pg.voided=0 and ps.voided=0 and pg.program_id=2 and ps.start_date = max_state.state_date 

		union

		select 	e.patient_id,
				e.encounter_datetime state_date,
				CASE state_of_permanence.value_coded
					WHEN 6270 THEN  'ACTIVO'
					 WHEN 1706 THEN 'TRANSFERIDO_PARA'
					 WHEN 1369 THEN 'TRANSFERIDO_DE'
					 WHEN 1707 THEN 'ABANDONO'
					 WHEN 1709 THEN 'SUSPENSO'
					 WHEN 1366 THEN 'OBITO'
					 ELSE null 
				END AS estado_permanencia_code,
				CONCAT('master_card.obs(', state_of_permanence.value_coded, ')') AS src
		from (	select 	max(patient_id) patient_id, max(encounter_datetime) state_date  
				from 	encounter e inner join obs o on e.encounter_id = o.encounter_id 
				where 	e.patient_id = :patient_id 
						and e.voided = 0 
						and e.encounter_type in (53, 6)
						and o.concept_id in (6272, 6273)
			 ) max_master_card 	inner join encounter e on e.encounter_datetime = max_master_card.state_date  
								inner join obs state_of_permanence on e.encounter_id = state_of_permanence.encounter_id
		where 	e.patient_id = :patient_id 
				and e.voided = 0 
				and e.encounter_type in (53, 6)
				and state_of_permanence.concept_id in (6272, 6273)
						

		union 

		select 	home_visit.patient_id,
				home_visit.state_date,
				CASE state_of_permanence.value_coded
					 WHEN 1366 THEN 'OBITO'
				ELSE null 
				END AS estado_permanencia_code,
				CONCAT('home_visit.obs(', state_of_permanence.value_coded, ')') AS src
				
		from (	select 	max(patient_id) patient_id, max(encounter_datetime) state_date  
				from 	encounter e inner join obs o on e.encounter_id = o.encounter_id 
				where 	e.patient_id = :patient_id 
						and e.voided = 0 
						and e.encounter_type in (54, 55)
						and o.concept_id in (2031)
			 ) home_visit 	inner join encounter e on e.encounter_datetime = home_visit.state_date  
							inner join obs state_of_permanence on e.encounter_id = state_of_permanence.encounter_id
		where 	e.patient_id = :patient_id 
				and e.voided = 0 
				and e.encounter_type in (54, 55)
				and state_of_permanence.concept_id in (2031)
		union
						
		select 	person_id patient_id, 
				death_date state_date, 'OBITO' estado_permanencia_code,
				CONCAT('demografic(',  p.dead, ')') AS src
		from person p where p.dead = 1 and p.person_id = :patient_id 	
) ALL_STATES limit 2