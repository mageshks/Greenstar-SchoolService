package com.cognizant.outreach.microservices.school.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.cognizant.outreach.entity.ClassDetail;
import com.cognizant.outreach.entity.IndiaStateDistrict;
import com.cognizant.outreach.entity.MeasurableParam;
import com.cognizant.outreach.entity.School;
import com.cognizant.outreach.entity.SchoolHoliday;
import com.cognizant.outreach.entity.SchoolWeekendWorkingDay;
import com.cognizant.outreach.entity.Student;
import com.cognizant.outreach.entity.StudentSchoolAssoc;
import com.cognizant.outreach.microservices.school.dao.ClassRepository;
import com.cognizant.outreach.microservices.school.dao.IndiaStateDistrictRepository;
import com.cognizant.outreach.microservices.school.dao.MeasurableParamRepository;
import com.cognizant.outreach.microservices.school.dao.SchoolHolidayRepository;
import com.cognizant.outreach.microservices.school.dao.SchoolRepository;
import com.cognizant.outreach.microservices.school.dao.SchoolWeekendWorkingDayRepository;
import com.cognizant.outreach.microservices.school.dao.StudentSchoolAssocRepository;
import com.cognizant.outreach.microservices.school.vo.ClassVO;
import com.cognizant.outreach.microservices.school.vo.HolidayVO;
import com.cognizant.outreach.microservices.school.vo.PerformanceParamVO;
import com.cognizant.outreach.microservices.school.vo.SchoolSearchVO;
import com.cognizant.outreach.microservices.school.vo.SchoolVO;
import com.cognizant.outreach.microservices.school.vo.StateVO;
import com.cognizant.outreach.microservices.school.vo.WeekendWorkingDayVO;
import com.cognizant.outreach.util.DateUtil;

public class SchoolServiceTest {

	@InjectMocks
	SchoolService schoolService = new SchoolServiceImpl();

	@Mock
	SchoolRepository schoolRespository;

	@Mock
	ClassRepository classRepository;

	@Mock
	MeasurableParamRepository measurableParamRepository;

	@Mock
	IndiaStateDistrictRepository indiaStateDistrictRepository;

	@Mock
	SchoolHolidayRepository schoolHolidayRepository;

	@Mock
	StudentSchoolAssocRepository studentSchoolAssocRepository;

	@Mock
	SchoolWeekendWorkingDayRepository weekendWorkingDayRepository;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void TestGetSchools() {
		when(schoolRespository.getSchools()).thenReturn(getSchools());
		Optional<List<SchoolVO>> optionalList = schoolService.getSchools();
		assertEquals(optionalList.get().size(), 2);
	}

	@Test
	public void TestGetClassBySchoolId() {
		when(classRepository.findClassesBySchoolId(1L)).thenReturn(getClassBySchoolId());
		Optional<List<ClassVO>> optionalList = schoolService.getClassBySchoolId(1L);
		assertEquals(optionalList.get().size(), 1);
	}

	@Test
	public void TestGetStudentAndTeamDetailsByClassId() {
		when(studentSchoolAssocRepository.findClassDetailByClassId(1L)).thenReturn(findClassDetailByClassId());
		Optional<ClassVO> optionalList = schoolService.getStudentAndTeamDetailsByClassId(1L);
		assertTrue(optionalList.isPresent() == true);
	}

	@Test
	public void TestGetStudentAndTeamDetailsByClassId_else() {
		when(studentSchoolAssocRepository.findClassDetailByClassId(1L)).thenReturn(findClassDetailByClassId_negative());
		Optional<ClassVO> optionalList = schoolService.getStudentAndTeamDetailsByClassId(1L);
		assertTrue(optionalList.get().getStudentList().size() == 0);
	}

	@Test
	public void TestGetStates() {
		when(indiaStateDistrictRepository.findAll()).thenReturn(getStates());
		List<StateVO> states = schoolService.getStates();
		assertTrue(states.size() == 1);
	}

	@Test
	public void TestGetSchoolsForSearch() {
		when(schoolRespository.findByState("Tamilnadu")).thenReturn(findByState());
		SchoolSearchVO schoolSearchVO = new SchoolSearchVO();
		schoolSearchVO.setStateName("Tamilnadu");
		schoolSearchVO.setDistrict("--Select District--");
		List<SchoolVO> schoolVOs = schoolService.getSchoolsForSearch(schoolSearchVO);
		assertTrue(schoolVOs.size() == 1);
	}

	@Test
	public void TestGetSchoolsForSearch_District() {
		when(schoolRespository.findByStateAndDistrict("Tamilnadu", "Erode")).thenReturn(findByState());
		SchoolSearchVO schoolSearchVO = new SchoolSearchVO();
		schoolSearchVO.setStateName("Tamilnadu");
		schoolSearchVO.setDistrict("Erode");
		List<SchoolVO> schoolVOs = schoolService.getSchoolsForSearch(schoolSearchVO);
		assertTrue(schoolVOs.size() == 1);
	}

	@Test
	public void TestSaveSchool() throws ParseException {
		when(schoolRespository.save(Mockito.any(School.class))).thenReturn(saveSchool());
		when(classRepository.save(Mockito.any(ClassDetail.class))).thenReturn(saveClassRepository());
		when(measurableParamRepository.save(Mockito.any(MeasurableParam.class))).thenReturn(savePerfParameters());
		when(schoolHolidayRepository.save(Mockito.any(SchoolHoliday.class))).thenReturn(saveHolidays());
		when(weekendWorkingDayRepository.save(Mockito.any(SchoolWeekendWorkingDay.class)))
				.thenReturn(saveWeekendWorkingDays());
		SchoolVO schoolVO = createSchoolVO();
		schoolVO = schoolService.saveSchool(schoolVO);
		assertTrue(schoolVO.getId() == 1L);
	}

	@Test
	public void TestGetSchoolDetail() throws ParseException {
		when(schoolRespository.findById(Mockito.any(Long.class))).thenReturn(getSchool());
		when(classRepository.findClassesBySchoolId(Mockito.any(Long.class))).thenReturn(getClasses());
		when(measurableParamRepository.findBySchoolId(Mockito.any(Long.class))).thenReturn(getParams());
		when(schoolHolidayRepository.findBySchoolId(Mockito.any(Long.class))).thenReturn(getHolidays());
		when(weekendWorkingDayRepository.findBySchoolId(Mockito.any(Long.class))).thenReturn(getWorkingDays());
		SchoolVO schoolVO = schoolService.getSchoolDetail(1L);
		assertTrue(schoolVO.getId() == 1L);
	}
	
	@Test
	public void TestUpdateSchool() throws ParseException {
		when(schoolRespository.save(Mockito.any(School.class))).thenReturn(saveSchool());
		when(schoolRespository.findById(Mockito.any(Long.class))).thenReturn(getSchool());
		when(classRepository.save(Mockito.any(ClassDetail.class))).thenReturn(updateClass());
		when(classRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(saveClassRepository()));
		when(measurableParamRepository.save(Mockito.any(MeasurableParam.class))).thenReturn(savePerfParameters());
		when(measurableParamRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(savePerfParameters()));
		when(schoolHolidayRepository.save(Mockito.any(SchoolHoliday.class))).thenReturn(saveHolidays());
		List<Long> ids = new ArrayList<>();
		ids.add(1L);
		when(schoolHolidayRepository.deleteBySchoolId(Mockito.any(Long.class))).thenReturn(ids);
		when(weekendWorkingDayRepository.save(Mockito.any(SchoolWeekendWorkingDay.class)))
				.thenReturn(saveWeekendWorkingDays());
		when(weekendWorkingDayRepository.deleteBySchoolId(Mockito.any(Long.class))).thenReturn(ids);
		SchoolVO schoolVO = createSchoolVO();
		schoolVO = schoolService.updateSchool(schoolVO);
		assertTrue(schoolVO.getId() == 1L);
	}

	private SchoolVO createSchoolVO() {
		SchoolVO schoolVO = new SchoolVO();
		schoolVO.setAddress("Erode");
		schoolVO.setCityName("Erode");
		schoolVO.setId(1L);
		schoolVO.setSchoolName("Coimbatore Sr Sec School");
		schoolVO.setDistrict("Erode");
		schoolVO.setState("Tamilnadu");
		schoolVO.setUserId("Magesh");

		HolidayVO holidayVO = new HolidayVO();
		holidayVO.setDescription("Deepavali");
		holidayVO.setFromDate("2019-10-11");
		holidayVO.setToDate("2019-10-11");
		holidayVO.setId(1L);
		List<HolidayVO> holidayVOs = new ArrayList<>();
		holidayVOs.add(holidayVO);
		schoolVO.setHolidays(holidayVOs);

		WeekendWorkingDayVO weekendWorkingDayVO = new WeekendWorkingDayVO();
		weekendWorkingDayVO.setReason("Sports day");
		weekendWorkingDayVO.setWorkingDate("2019-10-15");
		weekendWorkingDayVO.setId(1L);
		List<WeekendWorkingDayVO> weekendWorkingDayVOs = new ArrayList<>();
		weekendWorkingDayVOs.add(weekendWorkingDayVO);
		schoolVO.setWeekendWorkingDays(weekendWorkingDayVOs);

		PerformanceParamVO performanceParamVO = new PerformanceParamVO();
		performanceParamVO.setParamDesc("Attendance");
		performanceParamVO.setParamTitle("Attendance");
		performanceParamVO.setId(1L);
		List<PerformanceParamVO> performanceParamVOs = new ArrayList<>();
		performanceParamVOs.add(performanceParamVO);
		schoolVO.setPerfParamList(performanceParamVOs);

		ClassVO classVO = new ClassVO();
		classVO.setClassName("I");
		classVO.setId(1L);
		classVO.setSectionName("B");
		List<ClassVO> classVOs = new ArrayList<>();
		classVOs.add(classVO);
		schoolVO.setClassList(classVOs);

		return schoolVO;
	}

	private Optional<School> getSchool() {

		return Optional.of(saveSchool());
	}

	private Optional<List<ClassDetail>> getClasses() {
		List<ClassDetail> classDetails = new ArrayList<>();
		classDetails.add(saveClassRepository());
		return Optional.of(classDetails);
	}


	
	private School saveSchool() {
		School school = new School();
		school.setId(1L);
		school.setAddress("Erode");
		school.setCityName("Erode");
		school.setSchoolName("Coimbatore Sr Sec School");
		school.setDistrict("Erode");
		school.setState("Tamilnadu");
		return school;
	}

	private ClassDetail saveClassRepository() {

		ClassDetail classVO = new ClassDetail();
		classVO.setClassName("I");
		classVO.setSection("B");
		classVO.setId(1L);
		return classVO;
	}
	
	private ClassDetail updateClass() {

		ClassDetail classVO = new ClassDetail();
		classVO.setClassName("I");
		classVO.setSection("B");
		classVO.setId(0L);
		return classVO;
	}

	private Optional<List<MeasurableParam>> getParams() {
		List<MeasurableParam> measurableParas = new ArrayList<>();
		measurableParas.add(savePerfParameters());
		return Optional.of(measurableParas);
	}

	private MeasurableParam savePerfParameters() {
		MeasurableParam performanceParamVO = new MeasurableParam();
		performanceParamVO.setParameterDesc("Attendance");
		performanceParamVO.setParameterTitle("Attendance");
		performanceParamVO.setId(1L);
		return performanceParamVO;
	}

	private Optional<List<SchoolHoliday>> getHolidays() throws ParseException {
		List<SchoolHoliday> holidays = new ArrayList<>();
		holidays.add(saveHolidays());
		return Optional.of(holidays);
	}

	private SchoolHoliday saveHolidays() throws ParseException {
		SchoolHoliday holidayVO = new SchoolHoliday();
		holidayVO.setId(1L);
		holidayVO.setDescription("Deepavali");
		holidayVO.setFromDate(DateUtil.convertToDBFormat("2019-10-11"));
		holidayVO.setToDate(DateUtil.convertToDBFormat("2019-10-11"));
		return holidayVO;
	}

	private Optional<List<SchoolWeekendWorkingDay>> getWorkingDays() throws ParseException {
		List<SchoolWeekendWorkingDay> workingDays = new ArrayList<>();
		workingDays.add(saveWeekendWorkingDays());
		return Optional.of(workingDays);
	}

	private SchoolWeekendWorkingDay saveWeekendWorkingDays() throws ParseException {
		SchoolWeekendWorkingDay weekendWorkingDayVO = new SchoolWeekendWorkingDay();
		weekendWorkingDayVO.setReason("Sports day");
		weekendWorkingDayVO.setWorkingDate(DateUtil.convertToDBFormat("2019-10-15"));
		weekendWorkingDayVO.setId(1L);
		return weekendWorkingDayVO;
	}

	private Optional<List<School>> findByState() {
		List<School> schools = new ArrayList<>();
		School school = new School();
		school.setAddress("Erode");
		school.setCityName("Erode");
		school.setId(1L);
		school.setSchoolName("Coimbatore Sr Sec School");
		school.setDistrict("Erode");
		school.setState("Tamilnadu");

		schools.add(school);

		return Optional.of(schools);
	}

	public static Iterable<IndiaStateDistrict> getStates() {
		List<IndiaStateDistrict> states = new ArrayList<>();
		IndiaStateDistrict indiaStateDistrict = new IndiaStateDistrict();
		indiaStateDistrict.setState("TamilNadu");
		indiaStateDistrict.setDistrict("Erode");
		states.add(indiaStateDistrict);
		indiaStateDistrict = new IndiaStateDistrict();
		indiaStateDistrict.setState("TamilNadu");
		indiaStateDistrict.setDistrict("Coimbatore");
		states.add(indiaStateDistrict);
		Iterable<IndiaStateDistrict> iterable = states;
		return iterable;
	}

	private Optional<List<StudentSchoolAssoc>> findClassDetailByClassId() {
		List<StudentSchoolAssoc> asscoationList = new ArrayList<>();
		ClassDetail classVO = new ClassDetail();
		classVO.setId(1L);
		classVO.setClassName("I");
		classVO.setSection("B");

		Student student = new Student();
		student.setId(1L);
		student.setStudentName("Magesh");

		StudentSchoolAssoc schoolAssoc = new StudentSchoolAssoc();
		schoolAssoc.setClazz(classVO);
		schoolAssoc.setId(1L);
		schoolAssoc.setStudent(student);
		schoolAssoc.setTeamName("Mullai");
		asscoationList.add(schoolAssoc);
		return Optional.of(asscoationList);
	}

	private Optional<List<StudentSchoolAssoc>> findClassDetailByClassId_negative() {
		return Optional.empty();
	}

	private Optional<List<ClassDetail>> getClassBySchoolId() {
		List<ClassDetail> classDetails = new ArrayList<>();
		ClassDetail classVO = new ClassDetail();
		classVO.setId(1L);
		classVO.setClassName("I");
		classVO.setSection("B");
		classDetails.add(classVO);
		return Optional.of(classDetails);
	}

	public static Optional<List<Object[]>> getSchools() {
		List<Object[]> schools = new ArrayList<>();
		Object[] schoolRow = { 1L, "Coimbatore Sr Sec School" };
		schools.add(schoolRow);
		Object[] schoolRow1 = { 2L, "Annur Sr Sec School" };
		schools.add(schoolRow1);
		return Optional.of(schools);
	}

}
