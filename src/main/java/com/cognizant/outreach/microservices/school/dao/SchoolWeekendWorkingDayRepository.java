/**
 * 
 */
package com.cognizant.outreach.microservices.school.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import com.cognizant.outreach.entity.SchoolWeekendWorkingDay;

/**
 *
 */
@RestResource(exported = false)
public interface SchoolWeekendWorkingDayRepository extends CrudRepository<SchoolWeekendWorkingDay, Long> {
	
}
