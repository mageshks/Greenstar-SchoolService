package com.cognizant.outreach.microservices.school.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.poi.util.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.cognizant.outreach.entity.ClassDetail;
import com.cognizant.outreach.entity.Student;
import com.cognizant.outreach.entity.StudentSchoolAssoc;
import com.cognizant.outreach.microservices.school.dao.ClassRepository;
import com.cognizant.outreach.microservices.school.dao.IndiaStateDistrictRepository;
import com.cognizant.outreach.microservices.school.dao.MeasurableParamDataRepository;
import com.cognizant.outreach.microservices.school.dao.MeasurableParamRepository;
import com.cognizant.outreach.microservices.school.dao.SchoolHolidayRepository;
import com.cognizant.outreach.microservices.school.dao.SchoolRepository;
import com.cognizant.outreach.microservices.school.dao.SchoolWeekendWorkingDayRepository;
import com.cognizant.outreach.microservices.school.dao.StudentRepository;
import com.cognizant.outreach.microservices.school.dao.StudentSchoolAssocRepository;
import com.cognizant.outreach.microservices.school.vo.ClassVO;
import com.cognizant.outreach.microservices.school.vo.SchoolVO;
import com.cognizant.outreach.microservices.school.vo.StudentSearchVO;
import com.cognizant.outreach.microservices.school.vo.StudentVO;
import com.cognizant.outreach.microservices.school.vo.TeamNameCountVO;

public class StudentServiceTest {
	@InjectMocks
	StudentService studentService = new StudentServiceImpl();

	@Mock
	SchoolService schoolService;

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

	@Mock
	StudentRepository studentRepository;

	@Mock
	MeasurableParamDataRepository measurableParamDataRepository;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void TestGetSchoolTeamList() {
		when(studentSchoolAssocRepository.listTeamName(Mockito.any(Long.class))).thenReturn(getSchoolTeams());
		List<TeamNameCountVO> nameCountVOs = studentService.getSchoolTeamList(1L);
		assertEquals(nameCountVOs.size(), 2);
	}

	@Test
	public void TestSaveStudents() {
		when(studentSchoolAssocRepository.listTeamName(Mockito.any(Long.class))).thenReturn(getSchoolTeams());
		when(studentRepository.save(Mockito.any(Student.class))).thenReturn(saveStudent());
		when(studentSchoolAssocRepository.save(Mockito.any(StudentSchoolAssoc.class))).thenReturn(saveSchoolAssoc());
		when(classRepository.findById(Mockito.any(Long.class))).thenReturn(getClassDetail());
		when(studentRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(saveStudent()));
		when(studentSchoolAssocRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(saveSchoolAssoc()));
		when(studentSchoolAssocRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(saveSchoolAssoc()));
		when(studentSchoolAssocRepository.findClassDetailByClassId(Mockito.any(Long.class)))
				.thenReturn(getDBassociations());
		List<Long> ids = new ArrayList<>();
		ids.add(1L);
		when(studentSchoolAssocRepository.findClassDetailByClassId(Mockito.any(Long.class)))
				.thenReturn(getDBassociations());
		when(measurableParamDataRepository.deleteByStudentSchoolAssocId(Mockito.any(Long.class))).thenReturn(ids);
		doNothing().when(studentSchoolAssocRepository).delete(Mockito.any(StudentSchoolAssoc.class));
		ClassVO classVO = studentService.saveStudents(createClassAndStudents());
		assertTrue(classVO.getStudentList().get(0).getId() != 0L);
	}

	@Test
	public void TestDownloadTemplate() throws IOException {
		when(studentSchoolAssocRepository.listTeamName(Mockito.any(Long.class))).thenReturn(getSchoolTeams());
		when(studentSchoolAssocRepository.findByClazzSchoolId(Mockito.any(Long.class)))
				.thenReturn(getStudentAssociationsDB());
		when(classRepository.findClassesBySchoolId(Mockito.any(Long.class))).thenReturn(getClassDetailDB());
		StudentSearchVO searchVO = new StudentSearchVO();
		searchVO.setSchoolId(1L);
		byte[] bs = studentService.downloadTemplate(searchVO, false);
		assertTrue(bs.length != 0);
		bs = studentService.downloadTemplate(searchVO, true);
		assertTrue(bs.length != 0);
	}

	@Test
	public void TestUploadStudentData_TemplateModified() throws IOException, ParseException {
		File file = new File("src/test/resources/Bulk_Upload_Student_312_TeamCount1.xlsx");
		FileInputStream filestream = new FileInputStream(file);
		MultipartFile multiPartFile = new MockMultipartFile("file", file.getName(), "application/vnd.ms-excel",
				IOUtils.toByteArray(filestream));
		when(schoolService.getSchoolDetail(Mockito.any(Long.class))).thenReturn(getSchool_WithoutClass());

		String message = studentService.uploadStudentData(multiPartFile, "magesh");

		assertTrue(message.equalsIgnoreCase(
				"It seems the template meta data is modified. Either class or instruction sheet is removed while loading."));
	}

	@Test
	public void TestUploadStudentData_CorrectData() throws IOException, ParseException {
		File file = new File("src/test/resources/Bulk_Upload_Student_312_CorrectData.xlsx");
		FileInputStream filestream = new FileInputStream(file);
		MultipartFile multiPartFile = new MockMultipartFile("file", file.getName(), "application/vnd.ms-excel",
				IOUtils.toByteArray(filestream));
		when(schoolService.getSchoolDetail(Mockito.any(Long.class))).thenReturn(getSchool());
		when(studentSchoolAssocRepository.listTeamName(Mockito.any(Long.class))).thenReturn(getSchoolTeams1());

		List<Long> ids = new ArrayList<>();
		ids.add(1L);
		when(studentRepository.save(Mockito.any(Student.class))).thenReturn(saveStudent());
		when(studentSchoolAssocRepository.save(Mockito.any(StudentSchoolAssoc.class))).thenReturn(saveSchoolAssoc());
		when(classRepository.findById(Mockito.any(Long.class))).thenReturn(getClassDetail());
		when(studentRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(saveStudent()));
		when(studentSchoolAssocRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(saveSchoolAssoc()));
		when(studentSchoolAssocRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(saveSchoolAssoc()));
		when(studentSchoolAssocRepository.findClassDetailByClassId(Mockito.any(Long.class)))
				.thenReturn(getDBassociations());
		when(studentSchoolAssocRepository.findClassDetailByClassId(Mockito.any(Long.class)))
				.thenReturn(getDBassociations());
		when(measurableParamDataRepository.deleteByStudentSchoolAssocId(Mockito.any(Long.class))).thenReturn(ids);
		doNothing().when(studentSchoolAssocRepository).delete(Mockito.any(StudentSchoolAssoc.class));
		doNothing().when(studentRepository).delete(Mockito.any(Student.class));

		when(studentSchoolAssocRepository.findByClazzIdAndStudentId(Mockito.any(Long.class), Mockito.any(Long.class)))
				.thenAnswer(new Answer<Optional<StudentSchoolAssoc>>() {

					@Override
					public Optional<StudentSchoolAssoc> answer(InvocationOnMock arg0) throws Throwable {
						StudentSchoolAssoc schoolAssoc = new StudentSchoolAssoc();
						Long studentId = arg0.getArgument(1);
						Optional<StudentSchoolAssoc> optional;
						if (studentId >= 1L && studentId <= 6L) {
							schoolAssoc.setId(1L);
							optional = Optional.of(schoolAssoc);
						} else {
							optional = Optional.empty();
						}

						return optional;
					}
				});

		String message = studentService.uploadStudentData(multiPartFile, "magesh");
		assertTrue(message.equalsIgnoreCase("Bulk Uplaod Successful!"));
	}

	private SchoolVO getSchool_WithoutClass() {
		SchoolVO schoolVO = new SchoolVO();
		schoolVO.setId(1L);
		List<ClassVO> classVOs = new ArrayList<>();
		schoolVO.setClassList(classVOs);
		return schoolVO;
	}

	private SchoolVO getSchool() {

		SchoolVO schoolVO = new SchoolVO();
		schoolVO.setId(1L);
		ClassVO classVO = new ClassVO();
		classVO.setClassName("I");
		classVO.setSectionName("A");
		classVO.setClassAndSectionName("I-A");

		List<TeamNameCountVO> schoolTeamList = new ArrayList<>();
		TeamNameCountVO nameCountVO = new TeamNameCountVO();
		nameCountVO.setClassId(1L);
		nameCountVO.setStudentCount(5);
		nameCountVO.setClassSectionName("I-A");
		nameCountVO.setTeamName("paalai");
		schoolTeamList.add(nameCountVO);

		nameCountVO = new TeamNameCountVO();
		nameCountVO.setClassId(1L);
		nameCountVO.setStudentCount(1);
		nameCountVO.setClassSectionName("I-A");
		nameCountVO.setTeamName("marutham");
		schoolTeamList.add(nameCountVO);

		classVO.setSchoolTeamList(schoolTeamList);

		List<String> teamList = new ArrayList<>();
		teamList.add("marutham");
		teamList.add("paalai");

		classVO.setTeamList(teamList);

		List<StudentVO> studentList = new ArrayList<>();
		StudentVO studentVO = new StudentVO();
		studentVO.setAssociationId(1L);
		studentVO.setTeamName("paalai");
		studentVO.setStudentName("magesh");
		studentList.add(studentVO);

		studentVO = new StudentVO();
		studentVO.setAssociationId(2L);
		studentVO.setTeamName("paalai");
		studentVO.setStudentName("panneer");
		studentList.add(studentVO);

		studentVO = new StudentVO();
		studentVO.setAssociationId(3L);
		studentVO.setTeamName("paalai");
		studentVO.setStudentName("bharath");
		studentList.add(studentVO);

		studentVO = new StudentVO();
		studentVO.setAssociationId(4L);
		studentVO.setTeamName("paalai");
		studentVO.setStudentName("akil");
		studentList.add(studentVO);

		studentVO = new StudentVO();
		studentVO.setAssociationId(5L);
		studentVO.setTeamName("paalai");
		studentVO.setStudentName("moulish");
		studentList.add(studentVO);

		studentVO = new StudentVO();
		studentVO.setAssociationId(6L);
		studentVO.setTeamName("marutham");
		studentVO.setStudentName("kumar");
		studentList.add(studentVO);

		classVO.setStudentList(studentList);

		List<ClassVO> classVOs = new ArrayList<>();
		classVOs.add(classVO);

		schoolVO.setClassList(classVOs);
		return schoolVO;
	}

	private Optional<List<ClassDetail>> getClassDetailDB() {
		ClassDetail classDetail = new ClassDetail();
		classDetail.setId(1L);
		classDetail.setClassName("I");
		classDetail.setSection("A");
		List<ClassDetail> classDetails = new ArrayList<>();
		classDetails.add(classDetail);
		Optional<List<ClassDetail>> optional = Optional.of(classDetails);
		return optional;
	}

	private Optional<List<StudentSchoolAssoc>> getStudentAssociationsDB() {
		ClassDetail classDetail = new ClassDetail();
		classDetail.setId(1L);
		classDetail.setClassName("I");
		classDetail.setSection("A");

		List<StudentSchoolAssoc> assocs = new ArrayList<>();

		StudentSchoolAssoc schoolAssoc = new StudentSchoolAssoc();
		schoolAssoc.setClazz(classDetail);
		Student student = new Student();
		student.setStudentName("Magesh");
		student.setId(1L);
		schoolAssoc.setStudent(student);
		schoolAssoc.setId(1L);
		schoolAssoc.setTeamName("Paalai");
		schoolAssoc.setRollId("IA001");
		schoolAssoc = new StudentSchoolAssoc();
		assocs.add(schoolAssoc);

		schoolAssoc.setClazz(classDetail);
		student = new Student();
		student.setStudentName("Panneer");
		student.setId(2L);
		schoolAssoc.setStudent(student);
		schoolAssoc.setId(2L);
		schoolAssoc.setTeamName("Paalai");
		schoolAssoc.setRollId("IA002");
		assocs.add(schoolAssoc);
		Optional<List<StudentSchoolAssoc>> optional = Optional.of(assocs);
		return optional;
	}

	private Optional<ClassDetail> getClassDetail() {
		ClassDetail classDetail = new ClassDetail();
		Optional<ClassDetail> optional = Optional.of(classDetail);
		return optional;
	}

	private Student saveStudent() {
		Student student = new Student();
		student.setId(1L);
		return student;
	}

	private StudentSchoolAssoc saveSchoolAssoc() {
		StudentSchoolAssoc studentSchoolAssoc = new StudentSchoolAssoc();
		studentSchoolAssoc.setId(1L);
		return studentSchoolAssoc;
	}

	private ClassVO createClassAndStudents() {
		ClassVO classVO = new ClassVO();
		classVO.setUserId("Magesh");
		classVO.setClassAndSectionName("I-A");
		classVO.setClassName("I");
		classVO.setSectionName("A");
		List<String> teamList = new ArrayList<>();
		teamList.add("Mullai");
		teamList.add("Paalai");
		classVO.setTeamList(teamList);

		classVO.setStudentList(getStudents());

		return classVO;
	}

	private List<StudentVO> getStudents() {
		List<StudentVO> studentVOs = new ArrayList<>();
		StudentVO studentVO = new StudentVO();
		studentVO.setId(0L);
		studentVO.setStudentName("Magesh");
		studentVO.setTeamName("Paalai");
		studentVOs.add(studentVO);
		studentVO = new StudentVO();
		studentVO.setId(1L);
		studentVO.setStudentName("Panneer");
		studentVO.setTeamName("Paalai");
		studentVO.setAssociationId(1L);
		studentVOs.add(studentVO);
		studentVO.setId(2L);
		studentVO.setStudentName("Bharath");
		studentVO.setTeamName("Paalai");
		studentVO.setAssociationId(2L);
		studentVOs.add(studentVO);
		return studentVOs;
	}

	private Optional<List<StudentSchoolAssoc>> getDBassociations() {
		List<StudentSchoolAssoc> StudentSchoolAssocs = new ArrayList<>();
		StudentSchoolAssoc schoolAssoc = new StudentSchoolAssoc();

		Student student = new Student();
		student.setId(0L);
		student.setStudentName("Magesh");
		schoolAssoc.setStudent(student);
		schoolAssoc.setId(0L);
		StudentSchoolAssocs.add(schoolAssoc);

		student = new Student();
		student.setId(1L);
		student.setStudentName("Panneer");
		schoolAssoc = new StudentSchoolAssoc();
		schoolAssoc.setStudent(student);
		schoolAssoc.setId(1L);
		StudentSchoolAssocs.add(schoolAssoc);
		Optional<List<StudentSchoolAssoc>> optional = Optional.of(StudentSchoolAssocs);
		return optional;
	}

	private Optional<List<Object[]>> getSchoolTeams() {
		Object[] row1 = { "Paalai", 5L, 1L, "I", "A" };
		Object[] row2 = { "Mullai", 1L, 1L, "I", "A" };
		List<Object[]> objects = new ArrayList<>();
		objects.add(row1);
		objects.add(row2);
		return Optional.of(objects);
	}

	private Optional<List<Object[]>> getSchoolTeams1() {
		Object[] row1 = { "paalai", 5L, 1L, "I", "A" };
		Object[] row2 = { "marutham", 1L, 1L, "I", "A" };
		List<Object[]> objects = new ArrayList<>();
		objects.add(row1);
		objects.add(row2);
		return Optional.of(objects);
	}
}
