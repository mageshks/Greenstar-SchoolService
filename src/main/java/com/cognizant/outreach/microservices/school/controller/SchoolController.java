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

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cognizant.outreach.microservices.school.service.SchoolService;
import com.cognizant.outreach.microservices.school.service.StudentService;
import com.cognizant.outreach.microservices.school.vo.ClassVO;
import com.cognizant.outreach.microservices.school.vo.SchoolSearchVO;
import com.cognizant.outreach.microservices.school.vo.SchoolVO;
import com.cognizant.outreach.microservices.school.vo.StateVO;
import com.cognizant.outreach.microservices.school.vo.StudentSearchVO;

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

	@Autowired
	StudentService studentService;

	protected Logger logger = LoggerFactory.getLogger(SchoolController.class);

	/**
	 * To get the list of schools to display in dropdown
	 * 
	 * @return List of schools if present else null
	 */
	@RequestMapping(method = RequestMethod.POST, path = "/school/getSchools")
	public ResponseEntity<List<SchoolVO>> getSchools() {
		List<SchoolVO> schools = schoolService.getSchools().get();
		logger.debug("Retrieved school count ==> ", null == schools ? null : schools.size());
		return ResponseEntity.status(HttpStatus.OK).body(schools);
	}

	/**
	 * To get the list of class for the given name
	 * 
	 * @return List of class if present else null
	 */
	@RequestMapping(method = RequestMethod.POST, path = "/school/getClassList")
	public ResponseEntity<List<ClassVO>> getClassDetail(@RequestBody SchoolVO schoolVO) {
		List<ClassVO> classVOs = schoolService.getClassBySchoolId(schoolVO.getId()).get();
		logger.debug("Retrieved class count ==> {} for schoolId {}", null == classVOs ? null : classVOs.size(),
				schoolVO.getId());
		return ResponseEntity.status(HttpStatus.OK).body(classVOs);
	}

	/**
	 * To get the list of student and team detail for the given class
	 * 
	 * @return class detail if present else null
	 */
	@RequestMapping(method = RequestMethod.POST, path = "/school/getClassDetail")
	public ResponseEntity<ClassVO> getClassDetail(@RequestBody ClassVO classVO) {
		ClassVO classDetail = schoolService.getStudentAndTeamDetailsByClassId(classVO.getId()).get();
		if (classVO.getSchoolId() != 0L) {
			classDetail.setSchoolTeamList((studentService.getSchoolTeamList(classVO.getSchoolId())));
		}
		logger.debug("Retrieved student count ==> {} for classId {}",
				null == classDetail ? null : classDetail.getStudentList().size(), classVO.getId());
		return ResponseEntity.status(HttpStatus.OK).body(classDetail);
	}

	/**
	 * To get the list of states
	 * 
	 * @return statelist
	 */
	@RequestMapping(method = RequestMethod.POST, path = "/school/getStates")
	public ResponseEntity<List<StateVO>> getStates() {
		List<StateVO> states = schoolService.getStates();
		logger.debug("Retreived state count {}", states.size());
		return ResponseEntity.status(HttpStatus.OK).body(states);
	}

	/**
	 * To get the list of school for state and district
	 * 
	 * @return schoolVO
	 */
	@RequestMapping(method = RequestMethod.POST, path = "/school/getSchoolsForSearch")
	public ResponseEntity<List<SchoolVO>> getSchoolList(@RequestBody SchoolSearchVO schoolSearchVO) {
		List<SchoolVO> schools = schoolService.getSchoolsForSearch(schoolSearchVO);
		logger.debug("Retreived school count {}", schools.size());
		return ResponseEntity.status(HttpStatus.OK).body(schools);
	}

	/**
	 * To save the school
	 * 
	 * @return schoolVO
	 */
	@RequestMapping(method = RequestMethod.POST, path = "/school/submitschool")
	public ResponseEntity<SchoolVO> submitSchool(@RequestBody SchoolVO schoolVO) {
		try {
			if (schoolVO.getAction().equalsIgnoreCase("create")) {
				schoolService.saveSchool(schoolVO);
				logger.debug("School Saved successfully with id {} ==> " + schoolVO.getId());
			}
			if (schoolVO.getAction().equalsIgnoreCase("edit")) {
				schoolService.updateSchool(schoolVO);
				logger.debug("School Updated successfully with id {} ==> " + schoolVO.getId());
			}
		} catch (ParseException e) {
			logger.debug("Exception occured while parsing the input dates from ui", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}

		return ResponseEntity.status(HttpStatus.OK).body(schoolVO);
	}

	/**
	 * To get the school details
	 * 
	 * @return schoolVO
	 */
	@RequestMapping(method = RequestMethod.POST, path = "/school/getschooldetail")
	public ResponseEntity<SchoolVO> getSchoolList(@RequestBody long schoolId) {
		SchoolVO schoolVO;
		try {
			schoolVO = schoolService.getSchoolDetail(schoolId);
		} catch (ParseException e) {
			logger.debug("Exception occured while retrieving the school data for id {} {}", schoolId, e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}
		logger.debug("School retrieved with class count {} ==> " + schoolVO.getClassList().size());
		return ResponseEntity.status(HttpStatus.OK).body(schoolVO);
	}

	/**
	 * To save or update list of students in a class
	 * 
	 * @return schoolVO
	 */
	@RequestMapping(method = RequestMethod.POST, path = "/school/saveclassstudents")
	public ResponseEntity<ClassVO> saveStudents(@RequestBody ClassVO classVO) {
		classVO = studentService.saveStudents(classVO);
		// Refresh the state list based on the update
		classVO.setSchoolTeamList((studentService.getSchoolTeamList(classVO.getSchoolId())));
		logger.debug("Student details updated. student count {} ==> " + classVO.getStudentList().size());
		return ResponseEntity.status(HttpStatus.OK).body(classVO);
	}

	/**
	 * Method to download the bulk upload template excel file format.
	 * 
	 * @param StudentSearchVO
	 * @return excel byte array
	 * @throws IOException
	 */
	@PostMapping("/school/student/downloadtemplate")
	public ResponseEntity<byte[]> downloadTemplate(@RequestBody StudentSearchVO searchVO) throws IOException {
		byte[] excelBytes = null;

		try {
			excelBytes = studentService.downloadTemplate(searchVO);
		} catch (Exception exception) {
			logger.debug("Exception occured while downloading template for school ==> {}", searchVO.getSchoolId());
			logger.debug(exception.getMessage());
			ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(excelBytes);
		}
		logger.debug("Excel template retrival successfull for school ==> {}", searchVO.getSchoolId());
		return ResponseEntity.status(HttpStatus.OK).body(excelBytes);
	}

	/**
	 * Method to upload the bulk upload student data file.
	 * 
	 * @param file
	 * @param userId
	 * @return
	 * @throws IOException
	 */
	@PostMapping("/school/student/uploadbulkdata")
	public ResponseEntity<String> bulkUploadStudentData(@RequestParam("file") MultipartFile file,
			@RequestParam("userId") String userId, @RequestParam("schoolId") String schoolId) throws IOException {
		String responseMessage = null;
		try {
			responseMessage = studentService.uploadStudentData(file, userId);

		} catch (Exception e) {
			logger.debug("Exception occured while uploading students for school ==> {}", schoolId);
			logger.debug(e.getMessage());
			ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("");
		}
		logger.debug("Bulk uplaod processed for school ==> {} with the response message {}", schoolId,responseMessage);
		return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
	}
}
