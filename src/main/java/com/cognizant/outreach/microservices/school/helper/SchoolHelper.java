/**
 * ${SchoolHelper}
 *
 *  2019 Cognizant Technology Solutions. All Rights Reserved.
 *
 *  This software is the confidential and proprietary information of Cognizant Technology
 *  Solutions("Confidential Information").  You shall not disclose or use Confidential
 *  Information without the express written agreement of Cognizant Technology Solutions.
 *  Modification Log:
 *  -----------------
 *  Date                   Author           Description
 *  11/Mar/2019            371793        Developed base code structure
 *  ---------------------------------------------------------------------------
 */
package com.cognizant.outreach.microservices.school.helper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.cognizant.outreach.entity.BaseEntity;
import com.cognizant.outreach.entity.ClassDetail;
import com.cognizant.outreach.entity.MeasurableParam;
import com.cognizant.outreach.entity.School;
import com.cognizant.outreach.entity.SchoolHoliday;
import com.cognizant.outreach.entity.SchoolWeekendWorkingDay;
import com.cognizant.outreach.microservices.school.vo.ClassVO;
import com.cognizant.outreach.microservices.school.vo.HolidayVO;
import com.cognizant.outreach.microservices.school.vo.PerformanceParamVO;
import com.cognizant.outreach.microservices.school.vo.SchoolVO;
import com.cognizant.outreach.microservices.school.vo.WeekendWorkingDayVO;
import com.cognizant.outreach.util.DateUtil;

public class SchoolHelper {

	public static SchoolVO getSchoolVO(School school) {
		SchoolVO schoolVO = new SchoolVO();
		schoolVO.setSchoolName(school.getSchoolName());
		schoolVO.setAddress(school.getAddress());
		schoolVO.setCityName(school.getCityName());
		schoolVO.setDistrict(school.getDistrict());
		schoolVO.setState(school.getState());
		schoolVO.setId(school.getId());
		return schoolVO;
	}

	public static List<PerformanceParamVO> getPerformanceParamVOList(List<MeasurableParam> measurableParams) {
		PerformanceParamVO performanceParamVO = null;
		List<PerformanceParamVO> performanceParamVOs = new ArrayList<>();
		for (MeasurableParam measurableParam : measurableParams) {
			performanceParamVO = new PerformanceParamVO();
			performanceParamVO.setId(measurableParam.getId());
			performanceParamVO.setParamTitle(measurableParam.getParameterTitle());
			performanceParamVO.setParamDesc(measurableParam.getParameterDesc());
			performanceParamVOs.add(performanceParamVO);
		}
		return performanceParamVOs;
	}
	
	
	public static List<ClassVO> getClassVOList(List<ClassDetail> classes) {
		ClassVO classVO = null;
		List<ClassVO> ClassVOs = new ArrayList<>();
		for (ClassDetail classDetail : classes) {
			classVO = new ClassVO();
			classVO.setId(classDetail.getId());
			classVO.setClassName(classDetail.getClassName());
			classVO.setSectionName(classDetail.getSection());
			ClassVOs.add(classVO);
		}
		return ClassVOs;
	}
	
	public static List<HolidayVO> getHolidayVOList(List<SchoolHoliday> holidays) throws ParseException {
		HolidayVO holidayVO = null;
		List<HolidayVO> holidayVOs = new ArrayList<>();
		for (SchoolHoliday schoolHoliday : holidays) {
			holidayVO = new HolidayVO();
			holidayVO.setId(schoolHoliday.getId());
			holidayVO.setFromDate(DateUtil.getDBDateString(schoolHoliday.getFromDate()));
			holidayVO.setToDate(DateUtil.getDBDateString(schoolHoliday.getToDate()));
			holidayVO.setDescription(schoolHoliday.getDescription());
			holidayVOs.add(holidayVO);
		}
		return holidayVOs;
	}
	
	public static ClassDetail populateClass(String userId, School school, ClassVO classVO, boolean isUpdate) {
		ClassDetail classDetail = new ClassDetail();
		classDetail.setSchool(school);
		classDetail.setClassName(classVO.getClassName());
		classDetail.setSection(classVO.getSectionName());
		if (isUpdate) {
			classDetail.setId(classVO.getId());
			updateAuditInfo(userId, classDetail);
		} else {
			addAuditInfo(userId, classDetail);
			classVO.setId(classDetail.getId());
		}
		return classDetail;
	}
	
	public static MeasurableParam populateParam(String userId, School school, PerformanceParamVO performanceParamVO,
			boolean isUpdate) {
		MeasurableParam measurableParam = new MeasurableParam();
		measurableParam.setParameterTitle(performanceParamVO.getParamTitle());
		measurableParam.setParameterDesc(performanceParamVO.getParamDesc());
		measurableParam.setSchool(school);
		if (isUpdate) {
			measurableParam.setId(performanceParamVO.getId());
			SchoolHelper.updateAuditInfo(userId, measurableParam);
		} else {
			SchoolHelper.addAuditInfo(userId, measurableParam);
		}
		return measurableParam;
	}
	
	public static SchoolWeekendWorkingDay populateWeekendWorkingDay(String userId, School school,
			WeekendWorkingDayVO weekendWorkingDayVO, boolean isUpdate) throws ParseException {
		SchoolWeekendWorkingDay schoolWeekendWorkingDay = new SchoolWeekendWorkingDay();
		schoolWeekendWorkingDay.setSchool(school);
		schoolWeekendWorkingDay.setWorkingDate(DateUtil.getDatabaseDate(weekendWorkingDayVO.getWorkingDate()));
		schoolWeekendWorkingDay.setReason(weekendWorkingDayVO.getReason());
		if (isUpdate) {
			schoolWeekendWorkingDay.setId(weekendWorkingDayVO.getId());
			SchoolHelper.updateAuditInfo(userId, schoolWeekendWorkingDay);
		} else {
			SchoolHelper.addAuditInfo(userId, schoolWeekendWorkingDay);
		}
		return schoolWeekendWorkingDay;
	}
	
	public static SchoolHoliday populateHoliday(String userId, School school, HolidayVO holidayVO, boolean isUpdate)
			throws ParseException {
		SchoolHoliday schoolHoliday = new SchoolHoliday();
		schoolHoliday.setFromDate(DateUtil.getDatabaseDate(holidayVO.getFromDate()));
		schoolHoliday.setToDate(DateUtil.getDatabaseDate(holidayVO.getToDate()));
		schoolHoliday.setDescription(holidayVO.getDescription());
		schoolHoliday.setSchool(school);

		if (isUpdate) {
			schoolHoliday.setId(holidayVO.getId());
			SchoolHelper.updateAuditInfo(userId, schoolHoliday);
		} else {
			SchoolHelper.addAuditInfo(userId, schoolHoliday);
		}
		return schoolHoliday;
	}
	
	public static void addAuditInfo(String userId, BaseEntity baseEntity) {
		Date now = new Date();
		baseEntity.setCreatedDtm(now);
		baseEntity.setCreatedUserId(userId);
		baseEntity.setLastUpdatedDtm(now);
		baseEntity.setLastUpdatedUserId(userId);
	}

	public static School populateSchool(SchoolVO schoolVO,School dbSchool, boolean isUpdate) {
		School school = (null == dbSchool ? new School() :  dbSchool);
		school.setSchoolName(schoolVO.getSchoolName());
		school.setAddress(schoolVO.getAddress());
		school.setCityName(schoolVO.getCityName());
		school.setDistrict(schoolVO.getDistrict());
		school.setState(schoolVO.getState());
		if (isUpdate) {
			school.setId(schoolVO.getId());
			SchoolHelper.updateAuditInfo(schoolVO.getUserId(), school);
		} else {
			SchoolHelper.addAuditInfo(schoolVO.getUserId(), school);
		}
		return school;
	}
	
	public static void updateAuditInfo(String userId, BaseEntity baseEntity) {
		Date now = new Date();
		baseEntity.setLastUpdatedDtm(now);
		baseEntity.setLastUpdatedUserId(userId);
	}
	
	public static List<WeekendWorkingDayVO> getWorkingDayVOList(List<SchoolWeekendWorkingDay> workingDays) throws ParseException {
		WeekendWorkingDayVO weekendWorkingDayVO = null;
		List<WeekendWorkingDayVO> workingDayVOs = new ArrayList<>();
		for (SchoolWeekendWorkingDay weekendWorkingDay : workingDays) {
			weekendWorkingDayVO = new WeekendWorkingDayVO();
			weekendWorkingDayVO.setId(weekendWorkingDay.getId());
			weekendWorkingDayVO.setWorkingDate(DateUtil.getDBDateString(weekendWorkingDay.getWorkingDate()));
			weekendWorkingDayVO.setReason(weekendWorkingDay.getReason());
			workingDayVOs.add(weekendWorkingDayVO);
		}
		return workingDayVOs;
	}

}
