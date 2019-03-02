/**
 * ${SchoolServiceImpl}
 *
 *  2019 Cognizant Technology Solutions. All Rights Reserved.
 *
 *  This software is the confidential and proprietary information of Cognizant Technology
 *  Solutions("Confidential Information").  You shall not disclose or use Confidential
 *  Information without the express written agreement of Cognizant Technology Solutions.
 *  Modification Log:
 *  -----------------
 *  Date                   Author           Description
 *  02/Mar/2019            371793        Developed base code structure
 *  ---------------------------------------------------------------------------
 */
package com.cognizant.outreach.microservices.school.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cognizant.outreach.microservices.school.dao.SchoolRepository;
import com.cognizant.outreach.microservices.school.vo.SchoolVO;

/**
 * Service to do crud operation on school details
 * 
 * @author 371793
 */
@Service
public class SchoolServiceImpl implements SchoolService{

	@Autowired
	SchoolRepository schoolRespository;
	
	@Override
	public Optional<List<SchoolVO>> getSchools() {
		Optional<List<Object[]>> schools = schoolRespository.getSchools();
		List<SchoolVO> schoolList = null; 
		if(schools.isPresent()) {
			schoolList = new ArrayList<SchoolVO>();
			for (Object[] row : schools.get()) {
				SchoolVO schoolDetail = new SchoolVO();
				schoolDetail.setId((long) row[0]);
				schoolDetail.setSchoolName((String) row[1]);
				schoolList.add(schoolDetail);
			}
		}
		return Optional.of(schoolList);
	}}
