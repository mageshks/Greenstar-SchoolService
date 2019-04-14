/**
 * ${StudentHelper}
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
package com.cognizant.outreach.microservices.school.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.cognizant.outreach.microservices.school.service.StudentService;
import com.cognizant.outreach.microservices.school.vo.ClassVO;
import com.cognizant.outreach.microservices.school.vo.StudentVO;
import com.cognizant.outreach.microservices.school.vo.TeamNameCountVO;

/**
 * Student helper method for student service
 */
public class StudentHelper {
	
	public static final String CLASS_COUNT_MISMATCH = "It seems the template meta data is modified. Either class or instruction sheet is removed while loading.";
	public static final String CLASS_NAME_NOT_EXIST = "It seems the template meta data is modified. There is no class exist in database with name :: ";
	public static final String SHEET_HEADER_METADATA_MODIFIED = "It seems the template meta data is modified. The header is modified in the sheet :: ";
	public static final String STUDENT_ID_MODIFIED = "Student id in hidden feild is modified as it is not valid student id in database in sheet :: ";
	public static final String AT_ROW_NUM = " at row number :: ";
	public static final String STUDENT_NAME_EMPTY = "Student name is empty in sheet :: ";
	public static final String TEAM_NAME_EMPTY = "Team name is empty in sheet :: ";
	public static final String BLANK = "";
	public static final String BULK_UPLOAD_SUCCESS = "Bulk Uplaod Successful!";
	public static final String TEAM_NAME = "Team Name :: ";
	public static final String TEAM_AREADY_CHOOSED = " already choosen for class :: ";
	public static final String TEAM_HAS_5_STUDENTS = " have more than 5 students which is the maximum :: ";
	public static final String TEAM_COUNT_NOT_VALID = "Number of students on a team should be minimum 3 and maximum 5, please check class :: ";
	public static final String WITH_TEAM_NAME = " with team name :: ";
	public static final String HAS_COUNT = " has count :: ";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(StudentHelper.class);
	
	/**
	 * Method checks if all the students in the school not have team names if so system assign names
	 * 
	 * @param excelClasses
	 * @return isGroupRandomGenerated true if random generated
	 */
	public static boolean groupByRandomTeam(List<ClassVO> excelClasses, List<String> excelTeamNames) {
		boolean isGroupRandomGenerated = false;
		// Check if school is valid for random team generation
		if (checkSchoolForRandomGrouping(excelClasses)) {
			List<String> teamNames = excelTeamNames.size()> 0 ? excelTeamNames : initTeamName();
			
			// Iterate through each map and form as map with key as team name and value as list of students
			for (ClassVO classVO : excelClasses) {
				List<StudentVO> students = classVO.getStudentList();
				Map<String, List<StudentVO>> classTeamMap = new HashMap<>();

				LOGGER.debug("Student Count ==> ", students.size());
				if (students.size() <= 10) {
					groupClassTeam(students, teamNames, classTeamMap);
				} else {
					int teamSize = students.size();
					// If not less than 10
					int nearest5Mod = teamSize - ((teamSize % 5) + 5);

					LOGGER.debug("Nearest Mod ==> ", nearest5Mod);

					List<StudentVO> studentModFiveList = new ArrayList<>();
					studentModFiveList.addAll(students.subList(0, nearest5Mod));
					LOGGER.debug("Nearest Mod 5 List count==> ", studentModFiveList.size());
					LOGGER.debug("First Student==> {} :: Last Student==> ",
							studentModFiveList.get(0).getStudentName() + studentModFiveList.get(studentModFiveList.size() - 1));

					List<StudentVO> studentLeftList = new ArrayList<>();
					studentLeftList.addAll(students.subList(nearest5Mod, teamSize));
					LOGGER.debug("StudentLeftMod5 List count", studentLeftList.size());
					LOGGER.debug("First Student==> {} :: Last Student==> ", studentLeftList.get(0),
							studentLeftList.get(studentLeftList.size() - 1));

					groupClassTeam(studentModFiveList, teamNames, classTeamMap);
					groupClassTeam(studentLeftList, teamNames, classTeamMap);
				}
				setTeamName(classTeamMap,classVO);
			}
			isGroupRandomGenerated = true;
		}
		return isGroupRandomGenerated;
	}
	
	private static void setTeamName(Map<String, List<StudentVO>> classTeamMap, ClassVO classVO) {
		List<StudentVO> studentVOs = new ArrayList<>();
		
		for (Map.Entry<String, List<StudentVO>> entry : classTeamMap.entrySet()) {
			for (StudentVO studentVO : entry.getValue()) {
				studentVO.setTeamName(entry.getKey());
				studentVOs.add(studentVO);
			}
		}
		classVO.setStudentList(studentVOs);
	}
	
	private static List<String> initTeamName() {
		List<String> teamNames = new ArrayList<>();
		for (int i = 0; i < StudentService.defaultTeamNames.length; i++) {
			teamNames.add(StudentService.defaultTeamNames[i]);
		}
		return teamNames;
	}

	/**
	 * To check if any of the student has team name if so auto grouping is not possible
	 * This scenario can be used on first time setup alone
	 * 
	 * @param excelClasses
	 * @return
	 */
	private static boolean checkSchoolForRandomGrouping(List<ClassVO> excelClasses) {
		for (ClassVO classVO : excelClasses) {
			for (StudentVO studentVO : classVO.getStudentList()) {
				if(!StringUtils.isEmpty(studentVO.getTeamName())) {
					return false;
				}
			}
		}
		return true;
	}
	
	private static void groupClassTeam(List<StudentVO> students, List<String> teamNames,
			Map<String, List<StudentVO>> classTeamMap) {
		if (students.size() == 7) {
			formTeamSizeWith7And9(students, teamNames, classTeamMap, 7);
		} else if (students.size() == 9) {
			formTeamSizeWith7And9(students, teamNames, classTeamMap, 9);
		} else if (students.size() % 5 == 0) {
			formTeamWithSize(students, teamNames, classTeamMap, 5);
		} else if (students.size() % 4 == 0) {
			formTeamWithSize(students, teamNames, classTeamMap, 4);
		} else if (students.size() % 3 == 0) {
			formTeamWithSize(students, teamNames, classTeamMap, 3);
		}
	}

	/**
	 * Method to split the team size with 7 and 9
	 * 
	 * @param students
	 * @param teamNames
	 * @param classTeamMap
	 * @param size either 7 or 9
	 */
	private static void formTeamSizeWith7And9(List<StudentVO> students, List<String> teamNames,
			Map<String, List<StudentVO>> classTeamMap, int size) {
		LOGGER.debug("Team Name count Before ==> {}",teamNames.size());
		LOGGER.debug("Student count Before ==> {}" , students.size());
		if (size != 7 && size != 9)
			return;
		List<StudentVO> studentList = new ArrayList<>();
		int[] splits = (size == 7) ? new int[] { 4, 3 } : new int[] { 5, 4 };
		int teamNameArrayIndex = 0;
		Iterator<StudentVO> studentIterator = students.iterator();
		int splitSize = splits[0];
		while (studentIterator.hasNext()) {
			studentList.add(studentIterator.next());
			// Remove the student from the list once added to the team
			studentIterator.remove();
			if (studentList.size() == splitSize) {
				List<StudentVO> classTeam = new ArrayList<>();
				classTeam.addAll(studentList);
				classTeamMap.put(teamNames.get(teamNameArrayIndex), classTeam);
				// Remove the team name once assigned to a class
				teamNames.remove(teamNameArrayIndex);
				teamNameArrayIndex++;
				splitSize = splits[1];
				studentList.clear();
			}
		}
		LOGGER.debug("Team Name count After ==> {}" , teamNames.size());
		LOGGER.debug("Student count After ==> {}" , students.size());
	}

	/**
	 * Form team size with the given size
	 * 
	 * @param students
	 * @param teamNames
	 * @param classTeamMap
	 * @param size
	 */
	private static void formTeamWithSize(List<StudentVO> students, List<String> teamNames,
			Map<String, List<StudentVO>> classTeamMap, int size) {
		LOGGER.debug("Team Name count Before ==> {}" , teamNames.size());
		LOGGER.debug("Student count Before ==> {}" , students.size());
		LOGGER.debug("Team Size request ==> {}" , size);
		List<StudentVO> studentList = new ArrayList<>();
		int teamNameArrayIndex = 0;
		Iterator<StudentVO> studentIterator = students.iterator();
		while (studentIterator.hasNext()) {
			studentList.add(studentIterator.next());
			// Remove the student from the list once added to the team
			studentIterator.remove();
			if (studentList.size() == size) {
				List<StudentVO> classTeam = new ArrayList<>();
				classTeam.addAll(studentList);
				classTeamMap.put(teamNames.get(teamNameArrayIndex), classTeam);
				// Remove the team name once assigned to a class
				teamNames.remove(teamNameArrayIndex);
				teamNameArrayIndex++;
				studentList.clear();
			}
		}
		LOGGER.debug("Team Name count After ==> {}" , teamNames.size());
		LOGGER.debug("Student count After ==> {}" , students.size());
	}
	
	public static String validateTeamCountAndUniquness(ClassVO classVO, long schoolId,
			Map<String, TeamNameCountVO> teamNameMapForSchool) {
		// Holds only the class level team information, after final validation this will
		// merged with school map
		Map<String, TeamNameCountVO> teamNameMapForClass = new HashMap<>();

		if (!CollectionUtils.isEmpty(classVO.getStudentList())) {
			for (StudentVO studentVO : classVO.getStudentList()) {
				TeamNameCountVO teamNameCountVOSchoolLevel = teamNameMapForSchool.get(studentVO.getTeamName());
				// If the team already choosen by other class return with error message
				if (null != teamNameCountVOSchoolLevel
						&& !teamNameCountVOSchoolLevel.getClassSectionName().equals(classVO.getClassAndSectionName())) {
					return TEAM_NAME.concat(studentVO.getTeamName()).concat(TEAM_AREADY_CHOOSED)
							.concat(teamNameCountVOSchoolLevel.getClassSectionName());
				}
				TeamNameCountVO teamNameCountVOClassLevel = teamNameMapForClass.get(studentVO.getTeamName());

				if (null != teamNameCountVOClassLevel) {
					// If team name already present within the class check if the count maximum
					// count is already 5
					if (teamNameCountVOClassLevel.getStudentCount() == 5) {
						return TEAM_NAME.concat(studentVO.getTeamName()).concat(TEAM_HAS_5_STUDENTS)
								.concat(teamNameCountVOClassLevel.getClassSectionName());
					} else {
						// If not reached 5 then increment the count
						teamNameCountVOClassLevel.setStudentCount(teamNameCountVOClassLevel.getStudentCount() + 1);
					}
				} else {
					// New team name
					// If the team name does not exist then add to the map, so that it will
					// validated for next student
					TeamNameCountVO newTeamNameCountVO = new TeamNameCountVO();
					newTeamNameCountVO.setClassId(classVO.getId());
					newTeamNameCountVO.setTeamName(studentVO.getTeamName());
					newTeamNameCountVO.setClassSectionName(classVO.getClassAndSectionName());
					newTeamNameCountVO.setStudentCount(1);
					teamNameMapForClass.put(studentVO.getTeamName(), newTeamNameCountVO);
				}
			}

			// Update the school map with the class level details
			for (Map.Entry<String, TeamNameCountVO> entry : teamNameMapForClass.entrySet()) {
				teamNameMapForSchool.put(entry.getKey(), entry.getValue());
			}
		}
		return null;
	}

}
