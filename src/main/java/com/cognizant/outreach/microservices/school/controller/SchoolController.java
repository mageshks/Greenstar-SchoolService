/**
 * ${SecurityController}
 *
 *  2019 Cognizant Technology Solutions. All Rights Reserved.
 *
 *  This software is the confidential and proprietary information of Cognizant Technology
 *  Solutions("Confidential Information").  You shall not disclose or use Confidential
 *  Information without the express written agreement of Cognizant Technology Solutions.
 *  Modification Log:
 *  -----------------
 *  Date                   Author           Description
 *  18/Feb/2019            371793        Developed base code structure
 *  ---------------------------------------------------------------------------
 */
package com.cognizant.outreach.microservices.school.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.outreach.microservices.school.service.SchoolService;
import com.cognizant.outreach.microservices.school.vo.SchoolVO;



/**
 * Controller for school module 
 * 
 * @author 371793
 */
@RestController
@CrossOrigin
public class SchoolController {
	
	@Autowired
	SchoolService schoolService;
	
	protected Logger logger = LoggerFactory.getLogger(SchoolController.class);
	
	/**
	 * To get the list of schools to display in dropdown
	 * 
	 * @return  List of schools if present else null 
	 */
	@RequestMapping(method=RequestMethod.POST,path="/school/getSchools")
	public ResponseEntity<List<SchoolVO>> getSchools() {
		List<SchoolVO> schools = schoolService.getSchools().get();
		logger.debug("Retrieved school count ==> ", null == schools ? null : schools.size() );
		return ResponseEntity.status(HttpStatus.OK).body(schools);
	}
}
