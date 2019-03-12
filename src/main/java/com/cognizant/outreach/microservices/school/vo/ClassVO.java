/**
 * ${ClassVO}
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
package com.cognizant.outreach.microservices.school.vo;

import java.util.List;

/**
 * To hold the value objects for class
 * 
 * @author 371793
 *
 */
public class ClassVO {
	private long id;
	
	private String className;
	
	private String sectionName;
	
	private String classAndSectionName;
	
	private List<StudentVO> studentList;
	
	private List<String> teamList;
	
	private long schoolId;
	
	private String teamName;
	
	private List<TeamNameCountVO> schoolTeamList;
	
	public List<TeamNameCountVO> getSchoolTeamList() {
		return schoolTeamList;
	}

	public void setSchoolTeamList(List<TeamNameCountVO> schoolTeamList) {
		this.schoolTeamList = schoolTeamList;
	}

	public long getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(long schoolId) {
		this.schoolId = schoolId;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getClassAndSectionName() {
		return classAndSectionName;
	}

	public void setClassAndSectionName(String classAndSectionName) {
		this.classAndSectionName = classAndSectionName;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getSectionName() {
		return sectionName;
	}

	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}

	public List<StudentVO> getStudentList() {
		return studentList;
	}

	public void setStudentList(List<StudentVO> studentList) {
		this.studentList = studentList;
	}

	public List<String> getTeamList() {
		return teamList;
	}

	public void setTeamList(List<String> teamList) {
		this.teamList = teamList;
	}

}
