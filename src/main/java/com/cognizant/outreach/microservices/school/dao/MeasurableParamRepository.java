/**
 * 
 */
package com.cognizant.outreach.microservices.school.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import com.cognizant.outreach.entity.MeasurableParam;

/**
 *
 */
@RestResource(exported = false)
public interface MeasurableParamRepository extends CrudRepository<MeasurableParam, Long> {
	

}
