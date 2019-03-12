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

import java.util.List;

import com.cognizant.outreach.microservices.school.vo.TeamNameCountVO;

public interface StudentService {

	/**
	 * To list of team names and it's current count
	 * 
	 * @return List<TeamNameCountVO> 
	 */
	public List<TeamNameCountVO> getSchoolTeamList(long schoolId);

}
