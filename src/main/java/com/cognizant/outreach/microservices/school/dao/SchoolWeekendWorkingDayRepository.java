/**
 * 
 */
package com.cognizant.outreach.microservices.school.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import com.cognizant.outreach.entity.SchoolWeekendWorkingDay;

/**
 *
 */
@RestResource(exported = false)
public interface SchoolWeekendWorkingDayRepository extends CrudRepository<SchoolWeekendWorkingDay, Long> {
	
	public Optional<List<SchoolWeekendWorkingDay>> findBySchoolId(Long schoolId);
	public List<Long> deleteBySchoolId(Long schoolId);
	
}
