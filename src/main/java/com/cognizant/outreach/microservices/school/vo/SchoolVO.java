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

	private long id;
	
	private String userId;
	
	private String action;
	
	private String schoolName;
	
	private String district;
	
	private String state;
	
	private String cityName;
	
	private String address;
	
	private List<ClassVO> classList;
	
	private List<HolidayVO> holidays;
	
	private List<WeekendWorkingDayVO> weekendWorkingDays;
	
	private List<PerformanceParamVO> perfParamList;
	
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public List<PerformanceParamVO> getPerfParamList() {
		return perfParamList;
	}

	public void setPerfParamList(List<PerformanceParamVO> perfParamList) {
		this.perfParamList = perfParamList;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public List<HolidayVO> getHolidays() {
		return holidays;
	}

	public void setHolidays(List<HolidayVO> holidays) {
		this.holidays = holidays;
	}

	public List<WeekendWorkingDayVO> getWeekendWorkingDays() {
		return weekendWorkingDays;
	}

	public void setWeekendWorkingDays(List<WeekendWorkingDayVO> weekendWorkingDays) {
		this.weekendWorkingDays = weekendWorkingDays;
	}

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

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
