/**
 * 
 */
package com.cognizant.outreach.microservices.school.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.data.repository.query.Param;

import com.cognizant.outreach.entity.ClassDetail;

/**
 *
 */
@RestResource(exported = false)
public interface ClassRepository extends CrudRepository<ClassDetail, Long> {
	
	
	@Query("FROM ClassDetail c where c.school.id= :schoolId")
	public Optional<List<ClassDetail>> findClassesBySchoolId(@Param(value = "schoolId") Long schoolId);
}
