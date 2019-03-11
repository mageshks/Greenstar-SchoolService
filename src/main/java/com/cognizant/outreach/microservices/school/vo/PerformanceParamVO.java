package com.cognizant.outreach.microservices.school.vo;

public class PerformanceParamVO {
	private long id;
	private String paramTitle;
	private String paramDesc;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getParamTitle() {
		return paramTitle;
	}

	public void setParamTitle(String paramTitle) {
		this.paramTitle = paramTitle;
	}

	public String getParamDesc() {
		return paramDesc;
	}

	public void setParamDesc(String paramDesc) {
		this.paramDesc = paramDesc;
	}
}
