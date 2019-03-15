/**
 * ${StudentService}
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

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.cognizant.outreach.microservices.school.vo.ClassVO;
import com.cognizant.outreach.microservices.school.vo.StudentSearchVO;
import com.cognizant.outreach.microservices.school.vo.TeamNameCountVO;

public interface StudentService {

	/**
	 * To list of team names and it's current count
	 * 
	 * @return List<TeamNameCountVO> 
	 */
	public List<TeamNameCountVO> getSchoolTeamList(long schoolId);

	
	/**
	 * To save or update students
	 * 
	 * @return ClassVO with updated id informations 
	 */
	public ClassVO saveStudents(ClassVO classVO);


	/**
	 * To download the excel template
	 * 
	 * 
	 * @param searchVO
	 * @return
	 * @throws IOException 
	 */
	public byte[] downloadTemplate(StudentSearchVO searchVO) throws IOException;


	/**
	 * Upload student data
	 * 
	 * @param file
	 * @param userId
	 * @return String returns the message if success else error message to be displayed
	 * @throws ParseException 
	 * @throws IOException 
	 */
	public String uploadStudentData(MultipartFile file, String userId) throws ParseException, IOException;
}
