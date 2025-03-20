select *
from
(
	select *
	from 
	(
	 	# START _ABANDONOS
		select max(abandono.patient_id) patient_id, date_add(max(abandono.data_proximo_levantamento), interval 1 day) data_estado, max(estado_permanencia_id) estado_permanencia_id, max(estado_permanencia_code) estado_permanencia_code 
		from 
		(
			select inner_abandono.patient_id, inner_abandono.data_proximo_levantamento, 1 as estado_permanencia_id, 'ABANDONO' as estado_permanencia_code 
			from 
			( 
				select  max(patient_id) patient_id, max(data_proximo_levantamento) data_proximo_levantamento
				from ( 
						select 	max(e.patient_id) patient_id, date_add(max(o.value_datetime), interval 30 day) data_proximo_levantamento  
						from  	patient p
								inner join encounter e				on p.patient_id = e.patient_id
								inner join obs o 					on o.encounter_id = e.encounter_id 
								inner join obs obsLevantou 			on obsLevantou.encounter_id=e.encounter_id
								inner join patient_identifier pid 	on pid.location_id=e.location_id
						where e.voided = 0 and p.voided = 0 and o.voided = 0 and obsLevantou.voided=0 and obsLevantou.concept_id=23865 and obsLevantou.value_coded = 1065 and o.concept_id = 23866 and e.encounter_type=52 and p.patient_id = :patientId
							
						union 
						
					 	select 	max(p.patient_id) patient_id, max(o.value_datetime) data_proximo_levantamento                                                                                            
						from 	patient p                                                                                                                                   
								inner join encounter e on e.patient_id= p.patient_id 
								inner join obs o on o.encounter_id = e.encounter_id
								inner join patient_identifier pid on pid.location_id=e.location_id
						where 	p.voided= 0 and e.voided=0 and o.voided = 0 and e.encounter_type=18 and o.concept_id = 5096 and p.patient_id = :patientId                                                                              
				) proximo_levantamento
			) inner_abandono 
			where date_add(inner_abandono.data_proximo_levantamento, INTERVAL 28 DAY) < now() 
		) abandono
	
		left join 
		(	#START _ABANDONOS_EXCLUSOES
			select transferido_para.patient_id 
			from
			(
				
				select max(inner_transferido_para.patient_id) patient_id, max(data_transferencia) data_transferencia 
				from 
				( 
					select maxEstado.patient_id,maxEstado.data_transferencia 
					from
					( 
						select max(pg.patient_id) patient_id, max(ps.start_date) data_transferencia 
						from patient p 
					    inner join patient_program pg on p.patient_id=pg.patient_id 
					    inner join patient_state ps on pg.patient_program_id=ps.patient_program_id
					    inner join patient_identifier pid on pid.location_id=pg.location_id
						where pg.voided=0 and ps.voided=0 and p.voided=0 and pg.program_id=2 and p.patient_id = :patientId
					 )  maxEstado
					
					inner join patient_program pg2 on pg2.patient_id=maxEstado.patient_id 
					inner join patient_state ps2 on pg2.patient_program_id=ps2.patient_program_id
					inner join patient_identifier pid on pid.location_id=pg2.location_id
					
					where pg2.voided=0 and ps2.voided=0 and pg2.program_id=2 and ps2.start_date=maxEstado.data_transferencia and ps2.state=7 
	
					union
	
					select max(p.patient_id) patient_id,max(e.encounter_datetime) data_transferidopara
					from 	patient p 																		
		           			inner join encounter e on p.patient_id=e.patient_id 																								
		            		inner join obs o on o.encounter_id=e.encounter_id
		            		inner join patient_identifier pid on pid.location_id=e.location_id
	            	where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=21 and o.concept_id=2016 and o.value_coded in (1706,23863) and p.patient_id = :patientId
	
	            	union
					 
					select max(p.patient_id) patient_id, max(e.encounter_datetime) data_transferencia 
					from patient p 
						inner join encounter e on p.patient_id=e.patient_id 
					   	inner join obs o on o.encounter_id=e.encounter_id 
					   	inner join patient_identifier pid on pid.location_id=e.location_id
					where e.voided=0 and p.voided=0 and o.voided=0 and o.concept_id=6273 and o.value_coded=1706 and e.encounter_type=6 and p.patient_id = :patientId 
					
					union 
				
					select max(p.patient_id) patient_id, max(o.obs_datetime) data_transferencia 
					from patient p 
						inner join encounter e on p.patient_id=e.patient_id 
					    	inner join obs o on o.encounter_id=e.encounter_id 
					    	inner join patient_identifier pid on pid.location_id=e.location_id
					where e.voided=0 and p.voided=0 and o.voided=0 and o.concept_id=6272 and o.value_coded=1706 and e.encounter_type=53 and p.patient_id = :patientId 
					    	
	    		) inner_transferido_para
		      
		    	left join
			     ( 
				 	select ultimo_fila.patient_id, ultimo_fila.max_date
					from 
					(
						select max(p.patient_id) patient_id,max(encounter_datetime) max_date                                                                                                
						from patient p                                                                                                                                   
							inner join person pe on pe.person_id = p.patient_id                                                                                         
							inner join encounter e on e.patient_id=p.patient_id
							inner join patient_identifier pid on pid.location_id=e.location_id
						where p.voided=0 and pe.voided = 0 and e.voided=0 and e.encounter_type=18 and p.patient_id = :patientId 
					) ultimo_fila
					
					inner join 
					(
						select max(patient_id) patient_id, max(data_ultimo_levantamento)  data_ultimo_levantamento    
		               	from
		               	(
	         				select max(p.patient_id) patient_id, date_add(max(o.value_datetime), interval 1 day) data_ultimo_levantamento                                                                                            
							from patient p                                                                                                                                   
								inner join encounter e on e.patient_id= p.patient_id 
								inner join obs o on o.encounter_id = e.encounter_id
								inner join patient_identifier pid on pid.location_id=e.location_id
							where p.voided= 0 and e.voided=0 and o.voided = 0 and e.encounter_type=18 and o.concept_id = 5096 and p.patient_id = :patientId 
	         
	         				union
	         
	              			select max(p.patient_id) patient_id, date_add(max(value_datetime), interval 31 day) data_ultimo_levantamento                                                                                     
	              			from patient p                                                                                                                                   
	               				inner join person pe on pe.person_id = p.patient_id                                                                                         
	                    		inner join encounter e on p.patient_id=e.patient_id                                                                                         
	                    		inner join obs o on e.encounter_id=o.encounter_id
	                    		inner join patient_identifier pid on pid.location_id=e.location_id
	              			where p.voided=0 and pe.voided = 0 and e.voided=0 and o.voided=0 and e.encounter_type=52 and o.concept_id=23866 and o.value_datetime is not null and p.patient_id = :patientId
		               ) ultimo_levantamento
			     	) ultimo_levantamento on ultimo_levantamento.patient_id = ultimo_fila.patient_id
				) ultimo_fila on inner_transferido_para.patient_id  = ultimo_fila.patient_id 
		
				where inner_transferido_para.data_transferencia >= ultimo_fila.max_date or ultimo_fila.max_date is null
			) transferido_para
	
			inner join
			(
				
				select max(patient_id) patient_id, max(data_ultimo_levantamento)  data_ultimo_levantamento    
		      	from(
						select max(p.patient_id) patient_id, date_add(max(o.value_datetime), interval 1 day) data_ultimo_levantamento                                                                                            
						from patient p                                                                                                                                   
							inner join encounter e on e.patient_id= p.patient_id 
							inner join obs o on o.encounter_id = e.encounter_id
							inner join patient_identifier pid on pid.location_id=e.location_id
						where p.voided= 0 and e.voided=0 and o.voided = 0 and e.encounter_type=18 and o.concept_id = 5096 and  p.patient_id = :patientId
			
						union
			
						select max(p.patient_id) patient_id, date_add(max(value_datetime), interval 31 day) data_ultimo_levantamento                                                                                     
			       		from patient p                                                                                                                                   
							inner join person pe on pe.person_id = p.patient_id                                                                                         
				     		inner join encounter e on p.patient_id=e.patient_id                                                                                         
				     		inner join obs o on e.encounter_id=o.encounter_id
				     		inner join patient_identifier pid on pid.location_id=e.location_id
			       		where p.voided=0 and pe.voided = 0 and e.voided=0 and o.voided=0 and e.encounter_type=52 and o.concept_id=23866 and o.value_datetime is not null and  p.patient_id = :patientId  
				) ultimo_levantamento
			) ultimo_levantamento on ultimo_levantamento.patient_id = transferido_para.patient_id
		
			union
					
			select patient_id 
			from 
			(
				select max(suspenso.patient_id) patient_id, max(data_suspencao) data_suspencao 
				from
				( 
					select maxEstado.patient_id,maxEstado.data_suspencao 
					from
					( 
							select max(pg.patient_id) patient_id,max(ps.start_date) data_suspencao 
							from patient p 
						    		inner join patient_program pg on p.patient_id=pg.patient_id 
						        	inner join patient_state ps on pg.patient_program_id=ps.patient_program_id
						        	inner join patient_identifier pid on pid.location_id=pg.location_id
						    where pg.voided=0 and ps.voided=0 and p.voided=0 and pg.program_id=2  and  p.patient_id = :patientId
					) maxEstado 
					
					inner join patient_program pg2 on pg2.patient_id=maxEstado.patient_id 
					inner join patient_state ps2 on pg2.patient_program_id=ps2.patient_program_id
					inner join patient_identifier pid on pid.location_id=pg2.location_id
					where pg2.voided=0 and ps2.voided=0 and pg2.program_id=2 and ps2.start_date=maxEstado.data_suspencao and ps2.state=8 
				
					union 
		        
		        	select max(p.patient_id) patient_id, max(e.encounter_datetime) data_suspencao 
		        	from patient p 
		        		inner join encounter e on p.patient_id=e.patient_id 
		        		inner join obs o on o.encounter_id=e.encounter_id
		        		inner join patient_identifier pid on pid.location_id=e.location_id
		        	where e.voided=0 and p.voided=0 and e.encounter_datetime<=now() and o.voided=0 and o.concept_id=6273 and o.value_coded=1709 and e.encounter_type=6 and p.patient_id = :patientId
				
					union 
		        
		        	select max(p.patient_id), max(o.obs_datetime) data_suspencao 
		        	from patient p 
						inner join encounter e on p.patient_id=e.patient_id 
		        		inner join obs o on o.encounter_id=e.encounter_id
		        		inner join patient_identifier pid on pid.location_id=e.location_id
		        	where e.voided=0 and p.voided=0 and o.voided=0 and o.concept_id=6272 and o.value_coded=1709 and e.encounter_type=53 and p.patient_id = :patientId
				) suspenso 
					
				inner join 
				( 
					select max(p.patient_id) patient_id, max(e.encounter_datetime) encounter_datetime 
		   			from patient p 
		   				inner join encounter e on e.patient_id=p.patient_id
		   				inner join patient_identifier pid on pid.location_id=e.location_id
		   			where p.voided=0 and e.voided=0 and e.encounter_type=18 and p.patient_id = :patientId
				) consultaOuARV on suspenso.patient_id=consultaOuARV.patient_id 
				where consultaOuARV.encounter_datetime<=suspenso.data_suspencao 
			) suspensos
	
			union
		
			select patient_id
			from
			(
			
				select max(obito.patient_id) patient_id, max(data_obito) data_obito 
				from 
				( 
			 		select maxEstado.patient_id,maxEstado.data_obito 
			 		from 
		 			( 
			 			select max(pg.patient_id) patient_id, max(ps.start_date) data_obito 
			 			from patient p 
					 		inner join patient_program pg on p.patient_id = pg.patient_id 
					 		inner join patient_state ps on pg.patient_program_id = ps.patient_program_id
					 		inner join patient_identifier pid on pid.location_id=pg.location_id
					 	where pg.voided = 0 and ps.voided = 0 and p.voided = 0 and pg.program_id = 2 and p.patient_id = :patientId 
					) maxEstado 
					inner join patient_program pg2 on pg2.patient_id = maxEstado.patient_id 
				 	inner join patient_state ps2 on pg2.patient_program_id = ps2.patient_program_id
				 	inner join patient_identifier pid on pid.location_id=pg2.location_id
					where pg2.voided = 0 and ps2.voided = 0 and pg2.program_id = 2  and ps2.start_date = maxEstado.data_obito and ps2.state = 10 
			        
			        union 
		 		
			 		select max(p.patient_id) patient_id, max(o.obs_datetime) data_obito 
			 		from patient p 
			 			inner join encounter e on p.patient_id = e.patient_id 
			 			inner join obs o on o.encounter_id = e.encounter_id 
			 			inner join patient_identifier pid on pid.location_id=e.location_id
			 		where e.voided = 0 and p.voided = 0 and o.voided = 0 and o.concept_id = 6272 and o.value_coded = 1366 and e.encounter_type = 53 and p.patient_id = :patientId 
			 			
					union
			 
			 		select max(p.patient_id) patient_id, max(e.encounter_datetime) data_obito 
			 		from patient p 
			 			inner join encounter e on p.patient_id = e.patient_id 
			 			inner join obs o on o.encounter_id = e.encounter_id 
			 			inner join patient_identifier pid on pid.location_id=e.location_id
			 		where e.voided = 0 and p.voided = 0 and o.voided = 0 and o.concept_id = 6273 and o.value_coded = 1366 and e.encounter_type = 6 and p.patient_id = :patientId 
			
					union 
				 	
					select person_id patient_id, death_date data_obito 
					from person p where p.dead = 1 and p.person_id = :patientId 
				) obito
		
				inner join 
				( 
					
			 		select max(p.patient_id) patient_id, max(e.encounter_datetime) encounter_datetime 
			 		from patient p 
			 			inner join encounter e on e.patient_id = p.patient_id
			 			inner join patient_identifier pid on pid.location_id=e.location_id
			 		where p.voided = 0 and e.voided = 0 and e.encounter_type in (18,6,9)  and p.patient_id = :patientId 
				) consultaOuARV on obito.patient_id = consultaOuARV.patient_id 
			
		    	where consultaOuARV.encounter_datetime <= obito.data_obito 
			) obitos 
			
			#END _ABANDONOS_EXCLUSOES
		) exclusoes on abandono.patient_id = exclusoes.patient_id 
		where exclusoes.patient_id is null
		
		# END ABANDONOS
	) abandonos
		
	union
	
	# START _OBITO
	select *
	from
	(
		select obito.patient_id, obito.data_obito, 2 as estado_permanencia_id, 'OBITO' as estado_permanencia_code 
		from 
		( 
			select max(patient_id) patient_id, max(data_obito) data_obito 
			from 
			( 
		 		select maxEstado.patient_id,maxEstado.data_obito 
		 		from 
		 		( 
		 			select max(pg.patient_id) patient_id, max(ps.start_date) data_obito 
		 			from patient p 
				 		inner join patient_program pg on p.patient_id = pg.patient_id 
				 		inner join patient_state ps on pg.patient_program_id = ps.patient_program_id
				 		inner join patient_identifier pid on pid.location_id=pg.location_id
				 	where pg.voided = 0 and ps.voided = 0 and p.voided = 0 and pg.program_id = 2 and p.patient_id = :patientId 
				) maxEstado 
				inner join patient_program pg2 on pg2.patient_id = maxEstado.patient_id 
				inner join patient_state ps2 on pg2.patient_program_id = ps2.patient_program_id
				inner join patient_identifier pid on pid.location_id=pg2.location_id
				where pg2.voided = 0 and ps2.voided = 0 and pg2.program_id = 2  and ps2.start_date = maxEstado.data_obito and ps2.state = 10 
		        
		        union 
		 		
		 		select max(p.patient_id) patient_id, max(o.obs_datetime) data_obito 
		 		from patient p 
		 			inner join encounter e on p.patient_id = e.patient_id 
		 			inner join obs o on o.encounter_id = e.encounter_id 
		 			inner join patient_identifier pid on pid.location_id=e.location_id
		 		where e.voided = 0 and p.voided = 0 and o.voided = 0 and o.concept_id = 6272 and o.value_coded = 1366 and e.encounter_type = 53 and p.patient_id = :patientId 
		 		
		 		union
		 		 
		 		select max(p.patient_id) patient_id, max(e.encounter_datetime) data_obito 
		 		from patient p 
					inner join encounter e on p.patient_id = e.patient_id 
		 			inner join obs o on o.encounter_id = e.encounter_id
		 			inner join patient_identifier pid on pid.location_id=e.location_id
		 		where e.voided = 0 and p.voided = 0 and o.voided = 0 and o.concept_id = 6273 and o.value_coded = 1366 and e.encounter_type = 6 and p.patient_id = :patientId 
		 		
		 		union 
			 	
			 	select person_id patient_id, death_date data_obito 
			 	from person p where p.dead = 1 and p.person_id = :patientId 
			) inner_obito 
		) obito 
		letf join
		( 
			select max(p.patient_id) patient_id, max(e.encounter_datetime) encounter_datetime 
	 		from patient p 
	 			inner join encounter e on e.patient_id = p.patient_id 
	 			inner join patient_identifier pid on pid.location_id=e.location_id
	 		where p.voided = 0 and e.voided = 0 and e.encounter_type in (18,6,9) and p.patient_id = :patientId 
		) consultaOuARV on obito.patient_id = consultaOuARV.patient_id 
		where consultaOuARV.encounter_datetime <= obito.data_obito or consultaOuARV.encounter_datetime is null
	) obitos
	
	# END _OBITO
	
	union
	
	# START _TRANSFERENCIA
	select transferido_para.patient_id, data_transferencia, transferido_para.estado_permanencia_id, estado_permanencia_code 
	from 
	(
		select transferido_para.patient_id, data_transferencia, estado_permanencia_id, estado_permanencia_code 
		from 
		( 
			
			select max(transferido_para.patient_id) patient_id, max(data_transferencia) data_transferencia, 3 as estado_permanencia_id, 'TRANSFERIDO_PARA' as estado_permanencia_code  
			from 
			( 
				select maxEstado.patient_id,maxEstado.data_transferencia 
				from 
				( 
						select max(pg.patient_id) patient_id, max(ps.start_date) data_transferencia 
						from patient p 
					    inner join patient_program pg on p.patient_id=pg.patient_id 
					    inner join patient_state ps on pg.patient_program_id=ps.patient_program_id
					    inner join patient_identifier pid on pid.location_id=pg.location_id
						where pg.voided=0 and ps.voided=0 and p.voided=0 and pg.program_id=2 and p.patient_id = :patientId 
				) maxEstado 
				inner join patient_program pg2 on pg2.patient_id=maxEstado.patient_id 
				inner join patient_state ps2 on pg2.patient_program_id=ps2.patient_program_id
				inner join patient_identifier pid on pid.location_id=pg2.location_id
				where pg2.voided=0 and ps2.voided=0 and pg2.program_id=2 and ps2.start_date=maxEstado.data_transferencia and ps2.state=7 
	
				union
	
				select max(p.patient_id) patient_id, max(e.encounter_datetime) data_transferidopara 
				from patient p 																		
	       			inner join encounter e on p.patient_id=e.patient_id 																								
	        		inner join obs o on o.encounter_id=e.encounter_id 																								
	        	where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=21 and o.concept_id=2016 and o.value_coded in (1706,23863) and p.patient_id = :patientId 
	        		
	        	union
				 
				select max(p.patient_id) patient_id, max(e.encounter_datetime) data_transferencia 
				from patient p 
					inner join encounter e on p.patient_id=e.patient_id 
				   	inner join obs o on o.encounter_id=e.encounter_id
				   	inner join patient_identifier pid on pid.location_id=e.location_id
				where e.voided=0 and p.voided=0 and o.voided=0 and o.concept_id=6273 and o.value_coded=1706 and e.encounter_type=6 and p.patient_id = :patientId 
			
				union 
			
				select max(p.patient_id) patient_id, max(o.obs_datetime) data_transferencia 
				from patient p 
					inner join encounter e on p.patient_id=e.patient_id 
				    	inner join obs o on o.encounter_id=e.encounter_id
				    	inner join patient_identifier pid on pid.location_id=e.location_id
				where e.voided=0 and p.voided=0 and o.voided=0 and o.concept_id=6272 and o.value_coded=1706 and e.encounter_type=53 and p.patient_id = :patientId 
				    	
			) transferido_para
			 
			left join
			(
				select ultimo_fila.patient_id, ultimo_fila.max_date
				from
				(
					select max(p.patient_id) patient_id, max(encounter_datetime) max_date                                                                                                
					from patient p                                                                                                                                   
						inner join person pe on pe.person_id = p.patient_id                                                                                         
						inner join encounter e on e.patient_id=p.patient_id
						inner join patient_identifier pid on pid.location_id=e.location_id
					where p.voided=0 and pe.voided = 0 and e.voided=0 and e.encounter_type=18 and p.patient_id = :patientId 
				) ultimo_fila
				inner join
				(
					select patient_id , data_ultimo_levantamento    
					from(  	
			       			select max(patient_id) patient_id, max(data_ultimo_levantamento)  data_ultimo_levantamento    
			            	from(
				         			select max(p.patient_id) patient_id, date_add(max(o.value_datetime), interval 1 day) data_ultimo_levantamento                                                                                            
									from patient p                                                                                                                                   
										inner join encounter e on e.patient_id= p.patient_id 
										inner join obs o on o.encounter_id = e.encounter_id
										inner join patient_identifier pid on pid.location_id=e.location_id
									where p.voided= 0 and e.voided=0 and o.voided = 0 and e.encounter_type=18 and o.concept_id = 5096 and p.patient_id = :patientId 
				         
				         			union
				         
			              			select max(p.patient_id), date_add(max(value_datetime), interval 31 day) data_ultimo_levantamento                                                                                     
			              			from patient p                                                                                                                                   
			               			inner join person pe on pe.person_id = p.patient_id                                                                                         
			                    		inner join encounter e on p.patient_id=e.patient_id                                                                                         
			                    		inner join obs o on e.encounter_id=o.encounter_id
			                    		inner join patient_identifier pid on pid.location_id=e.location_id
			              			where p.voided=0 and pe.voided = 0 and e.voided=0 and o.voided=0 and e.encounter_type=52 and o.concept_id=23866 and o.value_datetime is not null and p.patient_id = :patientId 
			               ) ultimo_levantamento
			      	) ultimo_levantamento
			    ) ultimo_levantamento on ultimo_levantamento.patient_id = ultimo_fila.patient_id
	
			) ultimo_fila on transferido_para.patient_id  = ultimo_fila.patient_id 
			where transferido_para.data_transferencia >= ultimo_fila.max_date
		) transferido_para
		inner join
		(
			select patient_id , data_ultimo_levantamento    
			from
			(  	
					select max(patient_id) patient_id, max(data_ultimo_levantamento)  data_ultimo_levantamento    
					from
					(
						select max(p.patient_id) patient_id, date_add(max(o.value_datetime), interval 1 day) data_ultimo_levantamento                                                                                            
						from patient p                                                                                                                                   
							inner join encounter e on e.patient_id= p.patient_id 
							inner join obs o on o.encounter_id = e.encounter_id
							inner join patient_identifier pid on pid.location_id=e.location_id
						where p.voided= 0 and e.voided=0 and o.voided = 0 and e.encounter_type=18 and o.concept_id = 5096  and p.patient_id = :patientId 
	
						union
					
						select max(p.patient_id) patient_id, date_add(max(value_datetime), interval 31 day) data_ultimo_levantamento                                                                                     
						from patient p                                                                                                                                   
							inner join person pe on pe.person_id = p.patient_id                                                                                         
					 		inner join encounter e on p.patient_id=e.patient_id                                                                                         
					 		inner join obs o on e.encounter_id=o.encounter_id
					 		inner join patient_identifier pid on pid.location_id=e.location_id
						where p.voided=0 and pe.voided = 0 and e.voided=0 and o.voided=0 and e.encounter_type=52 and o.concept_id=23866 and o.value_datetime is not null  and p.patient_id = :patientId 
					) ultimo_levantamento
		 	) ultimo_levantamento
		) ultimo_levantamento on ultimo_levantamento.patient_id = transferido_para.patient_id
	) transferido_para 
	
	
	left join
	(
		
		select max(obito.patient_id) patient_id, max(data_obito) data_obito, 2 as estado_permanencia_id 
		from 
			( 
		 		select maxEstado.patient_id,maxEstado.data_obito 
		 		from 
		 		( 
		 			select max(pg.patient_id) patient_id, max(ps.start_date) data_obito 
		 			from patient p 
				 		inner join patient_program pg on p.patient_id = pg.patient_id 
				 		inner join patient_state ps on pg.patient_program_id = ps.patient_program_id 
				 		inner join patient_identifier pid on pid.location_id=pg.location_id
				 	where pg.voided = 0 and ps.voided = 0 and p.voided = 0 and pg.program_id = 2  and p.patient_id = :patientId 
				) maxEstado 
				inner join patient_program pg2 on pg2.patient_id = maxEstado.patient_id 
			 	inner join patient_state ps2 on pg2.patient_program_id = ps2.patient_program_id
			 	inner join patient_identifier pid on pid.location_id=pg2.location_id
				where pg2.voided = 0 and ps2.voided = 0 and pg2.program_id = 2  and ps2.start_date = maxEstado.data_obito and ps2.state = 10 
		        
	        	union 
	 		
		 		select max(p.patient_id) patient_id, max(o.obs_datetime) data_obito 
		 		from patient p 
		 			inner join encounter e on p.patient_id = e.patient_id 
		 			inner join obs o on o.encounter_id = e.encounter_id
		 			inner join patient_identifier pid on pid.location_id=e.location_id
		 		where e.voided = 0 and p.voided = 0 and o.voided = 0 and o.concept_id = 6272 and o.value_coded = 1366 and e.encounter_type = 53  and p.patient_id = :patientId 
	 		
		 		union
		 		 
		 		select max(p.patient_id) patient_id, max(e.encounter_datetime) data_obito 
		 		from patient p 
		 			inner join encounter e on p.patient_id = e.patient_id 
		 			inner join obs o on o.encounter_id = e.encounter_id 
		 			inner join patient_identifier pid on pid.location_id=e.location_id
		 		where e.voided = 0 and p.voided = 0 and o.voided = 0 and o.concept_id = 6273 and o.value_coded = 1366 and e.encounter_type = 6  and p.patient_id = :patientId  
	 		
	 			union 
		 	
			 	select person_id patient_id, death_date data_obito 
			 	from person p where p.dead = 1 and p.person_id = :patientId 
		) obito
			
		inner join 
		( 
			select patient_id, encounter_datetime
			from( 
		 		select max(p.patient_id) patient_id, max(e.encounter_datetime) encounter_datetime 
		 		from patient p 
		 			inner join encounter e on e.patient_id = p.patient_id 
		 		where p.voided = 0 and e.voided = 0 and e.encounter_type in (18,6,9) and p.patient_id = :patientId 
			) consultaLev
		) consultaOuARV on obito.patient_id = consultaOuARV.patient_id 
		 where consultaOuARV.encounter_datetime <= obito.data_obito 
	) obitos on obitos.patient_id = transferido_para.patient_id
	where obitos.patient_id is null
	
	#END _TRANSFERENCIA	
	
	union
	
	#START _SUSPENSO
	select *
	from
	(
		select suspenso.patient_id, data_suspencao, 4 as estado_permanencia_id, 'SUSPENSO' as estado_permanencia_code  
		from
		( 
			select max(patient_id) patient_id, max(data_suspencao) data_suspencao 
			from
			( 
				select maxEstado.patient_id,maxEstado.data_suspencao 
				from
				( 
					select max(pg.patient_id) patient_id,max(ps.start_date) data_suspencao 
					from patient p 
			    		inner join patient_program pg on p.patient_id=pg.patient_id 
			        	inner join patient_state ps on pg.patient_program_id=ps.patient_program_id
			        	inner join patient_identifier pid on pid.location_id=pg.location_id
				    where pg.voided=0 and ps.voided=0 and p.voided=0 and pg.program_id=2 and p.patient_id = :patientId 
				) maxEstado 
				inner join patient_program pg2 on pg2.patient_id=maxEstado.patient_id 
				inner join patient_state ps2 on pg2.patient_program_id=ps2.patient_program_id 
				inner join patient_identifier pid on pid.location_id=pg2.location_id
				where pg2.voided=0 and ps2.voided=0 and pg2.program_id=2  and ps2.start_date=maxEstado.data_suspencao and ps2.state=8 
				
				union 
		        
	        	select max(p.patient_id) patient_id,max(e.encounter_datetime) data_suspencao 
	        	from patient p 
	        		inner join encounter e on p.patient_id=e.patient_id 
	        		inner join obs o on o.encounter_id=e.encounter_id
	        		inner join patient_identifier pid on pid.location_id=e.location_id
	        	where e.voided=0 and p.voided=0 and o.voided=0 and o.concept_id=6273 and o.value_coded=1709 and e.encounter_type=6  and p.patient_id = :patientId 
				
				union 
		        
	        	select max(p.patient_id) patient_id,max(o.obs_datetime) data_suspencao 
	        	from patient p 
				inner join encounter e on p.patient_id=e.patient_id 
	        		inner join obs o on o.encounter_id=e.encounter_id
	        		inner join patient_identifier pid on pid.location_id=e.location_id
	        	where e.voided=0 and p.voided=0 and o.voided=0 and o.concept_id=6272 and o.value_coded=1709 and e.encounter_type=53  and p.patient_id = :patientId 
			) inner_suspenso 
		) suspenso
			
		inner join 
		( 
	   		select patient_id, encounter_datetime
	   		from
	   		( 
	   			select max(p.patient_id) patient_id, max(e.encounter_datetime) encounter_datetime 
	   			from patient p 
	   				inner join encounter e on e.patient_id=p.patient_id
	   				inner join patient_identifier pid on pid.location_id=e.location_id
	   			where p.voided=0 and e.voided=0 and e.encounter_type=18 and p.patient_id = :patientId 
	   		) consultaLev
	   	) consultaOuARV on suspenso.patient_id=consultaOuARV.patient_id 
	   	where consultaOuARV.encounter_datetime<=suspenso.data_suspencao 
	) suspensos
	
	#END _SUSPENSO
	
) all_

where data_estado is not null;
