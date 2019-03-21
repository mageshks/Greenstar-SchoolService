/**
 * ${SchoolControllerMockServiceTest}
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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import com.cognizant.outreach.microservices.school.service.SchoolService;
import com.cognizant.outreach.microservices.school.vo.SchoolVO;


/**
 * Test class for school controller
 * 
 * @author 371793
 */
@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SchoolControllerMockServiceTest {
	
	MockMvc mockMvc;
	
	@InjectMocks
	private SchoolController schoolController;

	@Mock
	SchoolService schoolService;
	

	@Test
	public void TestGetSchools() {
		when(schoolService.getSchools()).thenReturn(getSchools());
		assertEquals(schoolController.getSchools().getStatusCode(),HttpStatus.OK);
	}

	private Optional<List<SchoolVO>> getSchools() {
		SchoolVO schoolVO = new SchoolVO();
		List<SchoolVO> schoolVOs = new ArrayList<>();
		schoolVOs.add(schoolVO);
		Optional<List<SchoolVO>> optional = Optional.of(schoolVOs);
		return optional;
	}
	

}

