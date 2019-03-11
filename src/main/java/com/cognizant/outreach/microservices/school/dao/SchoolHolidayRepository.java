/**
 * 
 */
package com.cognizant.outreach.microservices.school.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import com.cognizant.outreach.entity.SchoolHoliday;

/**
 *
 */
@RestResource(exported = false)
public interface SchoolHolidayRepository extends CrudRepository<SchoolHoliday, Long> {
	
	public Optional<List<SchoolHoliday>> findBySchoolId(Long schoolId);
	
	public List<Long> deleteBySchoolId(Long schoolId);
}
