/**
 * 
 */
package com.cognizant.outreach.microservices.school.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import com.cognizant.outreach.entity.StudentSchoolAssoc;

/**
 *
 */
@RestResource(exported = false)
public interface StudentSchoolAssocRepository extends CrudRepository<StudentSchoolAssoc, Long> {
	
	
	@Query("FROM StudentSchoolAssoc sa where sa.clazz.id= :classId")
	public Optional<List<StudentSchoolAssoc>> findClassDetailByClassId(@Param(value = "classId") Long classId);
	
	
	@Query("select sa.teamName,COUNT(sa.teamName) FROM StudentSchoolAssoc sa WHERE sa.clazz.school.id=:SCHOOL_ID"
			+ " GROUP BY sa.teamName ORDER BY sa.clazz.id asc")
	public Optional<List<Object[]>> listTeamName(@Param("SCHOOL_ID") long schoolId);
}
