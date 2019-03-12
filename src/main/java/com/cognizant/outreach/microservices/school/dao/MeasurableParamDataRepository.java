/**
 * 
 */
package com.cognizant.outreach.microservices.school.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import com.cognizant.outreach.entity.MeasurableParamData;

/**
 *
 */
@RestResource(exported = false)
public interface MeasurableParamDataRepository extends CrudRepository<MeasurableParamData, Long> {
	public List<Long> deleteByStudentSchoolAssocClazzId(Long id);
	public List<Long> deleteByStudentSchoolAssocId(Long id);
}
