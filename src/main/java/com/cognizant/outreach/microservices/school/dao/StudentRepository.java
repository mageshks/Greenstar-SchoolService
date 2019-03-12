/**
 * 
 */
package com.cognizant.outreach.microservices.school.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import com.cognizant.outreach.entity.Student;

/**
 *
 */
@RestResource(exported = false)
public interface StudentRepository extends CrudRepository<Student, Long> {

}
