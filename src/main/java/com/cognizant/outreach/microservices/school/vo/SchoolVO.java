/**
 * ${SchoolVO}
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
 * Holds the value object for School
 * 
 * @author 371793
 *
 */
public class SchoolVO {

	public long id;
	
	public String schoolName;
	
	public List<ClassVO> classList;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public List<ClassVO> getClassList() {
		return classList;
	}

	public void setClassList(List<ClassVO> classList) {
		this.classList = classList;
	}

}
