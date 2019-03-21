package com.cognizant.outreach.microservices.school.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

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
import com.cognizant.outreach.entity.Student;
import com.cognizant.outreach.entity.StudentSchoolAssoc;
import com.cognizant.outreach.microservices.school.dao.ClassRepository;
import com.cognizant.outreach.microservices.school.dao.IndiaStateDistrictRepository;
import com.cognizant.outreach.microservices.school.dao.MeasurableParamRepository;
import com.cognizant.outreach.microservices.school.dao.SchoolHolidayRepository;
import com.cognizant.outreach.microservices.school.dao.SchoolRepository;
import com.cognizant.outreach.microservices.school.dao.SchoolWeekendWorkingDayRepository;
import com.cognizant.outreach.microservices.school.dao.StudentRepository;
import com.cognizant.outreach.microservices.school.dao.StudentSchoolAssocRepository;
import com.cognizant.outreach.microservices.school.vo.ClassVO;
import com.cognizant.outreach.microservices.school.vo.StudentVO;
import com.cognizant.outreach.microservices.school.vo.TeamNameCountVO;

public class StudentServiceTest {
	@InjectMocks
	StudentService studentService = new StudentServiceImpl();

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

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void TestGetSchoolTeamList() {
		when(studentSchoolAssocRepository.listTeamName(Mockito.any(Long.class))).thenReturn(getSchoolTeams());
		List<TeamNameCountVO>  nameCountVOs = studentService.getSchoolTeamList(1L);
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
		when(studentSchoolAssocRepository.findClassDetailByClassId(Mockito.any(Long.class))).thenReturn(getDBassociations());
		ClassVO classVO = studentService.saveStudents(createClassAndStudents());
		assertTrue(classVO.getStudentList().get(0).getId()!=0L);
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
	
	private List<StudentVO> getStudents(){
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
	
	private Optional<List<StudentSchoolAssoc>> getDBassociations(){
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
		Object[] row1 = {"Paalai",5L,1L,"I","A"};
		Object[] row2 = {"Mullai",5L,1L,"II","A"};
		List<Object[]> objects = new ArrayList<>();
		objects.add(row1);
		objects.add(row2);
		return Optional.of(objects);
	}
}
