package com.cognizant.outreach.microservices.school.vo;

import java.util.List;

public class StateVO {

	private String stateName;
	
	private List<String> districts;

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public List<String> getDistricts() {
		return districts;
	}

	public void setDistricts(List<String> districts) {
		this.districts = districts;
	}
	
	
	
}
