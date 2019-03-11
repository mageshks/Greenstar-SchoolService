/**
 * 
 */
package com.cognizant.outreach.microservices.school.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import com.cognizant.outreach.entity.School;

/**
 *
 */
@RestResource(exported = false)
public interface SchoolRepository extends CrudRepository<School, Long> {
	
	@Query("SELECT s.id,s.schoolName FROM School s")
	public Optional<List<Object[]>> getSchools();
	
	public Optional<School> findById(Long id);
	
	public Optional<List<School>> findByStateAndDistrict(String state,String district);
	
	public Optional<List<School>> findByState(String state);
	
}
