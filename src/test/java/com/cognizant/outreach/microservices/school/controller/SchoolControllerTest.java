/**
 * ${SecurityControllerTest}
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

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


/**
 * Test class for security service
 * 
 * @author 371793
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SchoolControllerTest {
	
	MockMvc mockMvc;
	
	@Mock
	private SchoolController schoolController;

	@Autowired
	private TestRestTemplate template;

	/**
	 * Setup mock mvc
	 * 
	 * @throws Exception
	 */
	@Before
	public void setup() throws Exception {
		mockMvc = MockMvcBuilders.standaloneSetup(schoolController).build();
	}


	/**
	 * To check if the schools are retrieved
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetSchools() throws Exception {
		ResponseEntity<List> response1 = template.getForEntity("/getSchools", List.class);
		Assert.assertEquals(HttpStatus.OK, response1.getStatusCode());
	}
	
	
	/**
	 * To check if the schools are retrieved
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetStates() throws Exception {
		ResponseEntity<List> response1 = template.getForEntity("/getStates", List.class);
		Assert.assertEquals(HttpStatus.OK, response1.getStatusCode());
	}
	

}

