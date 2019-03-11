package com.cognizant.outreach.microservices.school.vo;

public class WeekendWorkingDayVO {

	private long id;
	private String workingDate;
	private String reason;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getWorkingDate() {
		return workingDate;
	}

	public void setWorkingDate(String workingDate) {
		this.workingDate = workingDate;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

}
