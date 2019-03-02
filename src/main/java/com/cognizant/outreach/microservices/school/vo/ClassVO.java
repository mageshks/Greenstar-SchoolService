/**
 * ${Clazz}
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
	public long id;
	
	public String className;
	
	public List<SectionVO> sectionList;

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

	public List<SectionVO> getSectionList() {
		return sectionList;
	}

	public void setSectionList(List<SectionVO> sectionList) {
		this.sectionList = sectionList;
	}

}
