/**
 * ${StudentVO}
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

/**
 * Holds the value object for Student
 * 
 * @author 371793
 *
 */
public class StudentVO {
	private long id;
	
	private String studentName;
	
	private long associationId;
	
	private long classId;
	
	private String teamName;
	
	private String rollId;
	
	public long getClassId() {
		return classId;
	}

	public void setClassId(long classId) {
		this.classId = classId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public long getAssociationId() {
		return associationId;
	}

	public void setAssociationId(long associationId) {
		this.associationId = associationId;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getRollId() {
		return rollId;
	}

	public void setRollId(String rollId) {
		this.rollId = rollId;
	}
}
