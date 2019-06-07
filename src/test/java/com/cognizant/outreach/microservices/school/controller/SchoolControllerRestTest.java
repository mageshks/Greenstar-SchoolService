/**
 * ${SchoolControllerRestTest}
 *
 *  2019 Cognizant Technology Solutions. All Rights Reserved.
 *
 *  This software is the confidential and proprietary information of Cognizant Technology
 *  Solutions("Confidential Information").  You shall not disclose or use Confidential
 *  Information without the express written agreement of Cognizant Technology Solutions.
 *  Modification Log:
 *  -----------------
 *  Date                   Author           Description
 *  18/Feb/2019            Panneer        Developed base code structure
 *  ---------------------------------------------------------------------------
 */
package com.cognizant.outreach.microservices.school.controller;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.cognizant.outreach.microservices.school.vo.SchoolVO;

/**
 * Test class for school controller
 * 
 * @author Magesh
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SchoolControllerRestTest {

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
	 * To check get schools
	 */
	@Test
	@SuppressWarnings({"rawtypes"})
	public void testGetSchools() throws Exception {
		ResponseEntity<List> response = template.postForEntity("/getSchools", "1,2,3", List.class);
		Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	

	/**
	 * To check get schools
	 */
	
	@Test
	@SuppressWarnings({"rawtypes","unchecked"})
	public void testGetClassDetail() throws Exception {
		ResponseEntity<List> schoolResponse = template.postForEntity("/getSchools", "1,2", List.class);
		List<Map<Object, Object>> schoolsMap = schoolResponse.getBody();		
		Assert.assertEquals(HttpStatus.OK, schoolResponse.getStatusCode());
		
		if(!CollectionUtils.isEmpty(schoolsMap)) {
			Map<Object, Object> schoolMap = (Map<Object, Object>) schoolsMap.get(0);
			SchoolVO schoolVO = new SchoolVO();
			schoolVO.setId((Integer)schoolMap.get("id"));
			ResponseEntity<List> response = template.postForEntity("/getClassList",schoolVO, List.class);
			Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
		}
	}
}
