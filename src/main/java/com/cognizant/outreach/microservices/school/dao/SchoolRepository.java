/**
 * 
 */
package com.cognizant.outreach.microservices.school.dao;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
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
	
	@Query("SELECT md.studentSchoolAssoc.rollId, md.studentSchoolAssoc.student.studentName, md.studentSchoolAssoc.clazz.school.schoolName, md.studentSchoolAssoc.clazz.className, md.studentSchoolAssoc.teamName, md.measurableDate, md.measurableParam.parameterTitle, md.measurableParamValue FROM MeasurableParamData md WHERE md.studentSchoolAssoc.clazz.school.id =:SCHOOL_ID AND md.studentSchoolAssoc.clazz.id=:CLASS_ID AND md.measurableDate BETWEEN :MEASURABLE_FROM_DATE AND :MEASURABLE_TO_DATE ORDER BY md.studentSchoolAssoc.rollId, md.studentSchoolAssoc.student.studentName, md.measurableDate, md.measurableParam.parameterTitle")
	public List<Object[]> listOfMeasurableParamDataBySearchParam(@Param("SCHOOL_ID") Long schoolId,
			@Param("CLASS_ID") Long classId, @Param("MEASURABLE_FROM_DATE") Date fromDate,
			@Param("MEASURABLE_TO_DATE") Date toDate);
	
}
