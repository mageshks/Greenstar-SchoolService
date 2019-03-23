/**
 * ${StudentServiceImpl}
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
package com.cognizant.outreach.microservices.school.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.cognizant.outreach.entity.ClassDetail;
import com.cognizant.outreach.entity.Student;
import com.cognizant.outreach.entity.StudentSchoolAssoc;
import com.cognizant.outreach.microservices.school.dao.ClassRepository;
import com.cognizant.outreach.microservices.school.dao.MeasurableParamDataRepository;
import com.cognizant.outreach.microservices.school.dao.StudentRepository;
import com.cognizant.outreach.microservices.school.dao.StudentSchoolAssocRepository;
import com.cognizant.outreach.microservices.school.helper.ExcelHelper;
import com.cognizant.outreach.microservices.school.helper.SchoolHelper;
import com.cognizant.outreach.microservices.school.helper.StudentHelper;
import com.cognizant.outreach.microservices.school.vo.ClassVO;
import com.cognizant.outreach.microservices.school.vo.SchoolVO;
import com.cognizant.outreach.microservices.school.vo.StudentSearchVO;
import com.cognizant.outreach.microservices.school.vo.StudentVO;
import com.cognizant.outreach.microservices.school.vo.TeamNameCountVO;

/**
 * Service to do crud operation on student details
 * 
 * @author 371793
 */
@Service
public class StudentServiceImpl implements StudentService {

	@Autowired
	StudentSchoolAssocRepository studentSchoolAssocRepository;

	@Autowired
	StudentRepository studentRepository;

	@Autowired
	MeasurableParamDataRepository measurableParamDataRepository;

	@Autowired
	ClassRepository classRepository;

	@Autowired
	SchoolService schoolService;

	private static final Logger LOGGER = LoggerFactory.getLogger(StudentService.class);
	
	@Override
	public List<TeamNameCountVO> getSchoolTeamList(long schoolId) {
		LOGGER.debug("To retrieve school list for schoolId {}",schoolId);
		Optional<List<Object[]>> teamList = studentSchoolAssocRepository.listTeamName(schoolId);
		List<TeamNameCountVO> teamNameCountVOs = new ArrayList<>();
		if (teamList.isPresent()) {
			for (Object[] dbRow : teamList.get()) {
				TeamNameCountVO teamNameCountVO = new TeamNameCountVO();
				teamNameCountVO.setTeamName((String) dbRow[0]);
				teamNameCountVO.setStudentCount(((Long) dbRow[1]).intValue());
				teamNameCountVO.setClassId((Long) dbRow[2]);
				teamNameCountVO.setClassSectionName((String) dbRow[3] + "-" + (String) dbRow[4]);
				teamNameCountVOs.add(teamNameCountVO);
			}
		}
		return teamNameCountVOs;
	}

	@Override
	@Transactional
	public ClassVO saveStudents(ClassVO classVO) {
		return saveOrUpdateStudents(classVO);
	}

	private void deleteStudents(List<StudentVO> studentVOs, long classId) {
		LOGGER.debug("Delete students for classId {}",classId);
		Optional<List<StudentSchoolAssoc>> associations = studentSchoolAssocRepository
				.findClassDetailByClassId(classId);
		if (associations.isPresent()) {
			Map<Long, StudentVO> uiStudentMap = new HashMap<>();
			for (StudentVO studentVO : studentVOs) {
				uiStudentMap.put(studentVO.getAssociationId(), studentVO);
			}

			// If the db association not present in the existing list list the student
			for (StudentSchoolAssoc studentSchoolAssoc : associations.get()) {
				if (null == uiStudentMap.get(studentSchoolAssoc.getId())) {
					measurableParamDataRepository.deleteByStudentSchoolAssocId(studentSchoolAssoc.getId());
					studentSchoolAssocRepository.delete(studentSchoolAssoc);
				}
			}
		}
	}

	private ClassVO saveOrUpdateStudents(ClassVO classVO) {
		if (!CollectionUtils.isEmpty(classVO.getStudentList())) {
			for (StudentVO studentVO : classVO.getStudentList()) {
				// Save student
				if (studentVO.getId() == 0L) {
					// Save student
					Student student = new Student();
					student.setStudentName(studentVO.getStudentName());
					SchoolHelper.addAuditInfo(classVO.getUserId(), student);
					student = studentRepository.save(student);
					studentVO.setId(student.getId());
					// save associations
					StudentSchoolAssoc association = new StudentSchoolAssoc();
					association.setRollId(classVO.getClassName() + classVO.getSectionName() + student.getId());
					association.setStatus("ACTIVE");
					association.setStudent(student);
					association.setTeamName(studentVO.getTeamName().toLowerCase());
					ClassDetail classDetail = classRepository.findById(classVO.getId()).get();
					association.setClazz(classDetail);
					SchoolHelper.addAuditInfo(classVO.getUserId(), association);
					association = studentSchoolAssocRepository.save(association);
					studentVO.setAssociationId(association.getId());
				} else {
					// Update student
					Student student = studentRepository.findById(studentVO.getId()).get();
					student.setStudentName(studentVO.getStudentName());
					SchoolHelper.updateAuditInfo(classVO.getUserId(), student);
					studentRepository.save(student);
					StudentSchoolAssoc association = studentSchoolAssocRepository.findById(studentVO.getAssociationId())
							.get();
					association.setTeamName(studentVO.getTeamName().toLowerCase());
					SchoolHelper.updateAuditInfo(classVO.getUserId(), association);
					studentSchoolAssocRepository.save(association);
				}
				deleteStudents(classVO.getStudentList(), classVO.getId());
			}
		}
		return classVO;

	}

	@Override
	public byte[] downloadTemplate(StudentSearchVO searchVO, boolean isExport) throws IOException {
		// Create Work book
		XSSFWorkbook workbook = new XSSFWorkbook();
		// If not export then add the introduction sheet
		if (!isExport) {
			XSSFSheet sheet = workbook.createSheet(ExcelHelper.EXCEL_INTRODUCTION_SHEETNAME);

			ExcelHelper.fillInstructionsSheet(workbook, sheet, getSchoolTeamList(searchVO.getSchoolId()));
		}

		Map<String, List<StudentVO>> studentClassMap = getStudentsClassMap(searchVO.getSchoolId());

		ExcelHelper.createClassWiseSheets(workbook, studentClassMap);

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		workbook.write(bos);

		return bos.toByteArray();
	}

	private Map<String, List<StudentVO>> getStudentsClassMap(long schoolId) {
		// Key as class and section string value as student
		Map<String, List<StudentVO>> studentClassMap = initStudentClassMap(schoolId);

		Optional<List<StudentSchoolAssoc>> schoolAssociations = studentSchoolAssocRepository
				.findByClazzSchoolId(schoolId);
		if (null != studentClassMap && schoolAssociations.isPresent()) {
			for (StudentSchoolAssoc studentSchoolAssoc : schoolAssociations.get()) {
				StudentVO studentVO = new StudentVO();
				studentVO.setAssociationId(studentSchoolAssoc.getId());
				studentVO.setClassId(studentSchoolAssoc.getClazz().getId());
				studentVO.setId(studentSchoolAssoc.getStudent().getId());
				studentVO.setRollId(studentSchoolAssoc.getRollId());
				studentVO.setTeamName(studentSchoolAssoc.getTeamName());
				studentVO.setStudentName(studentSchoolAssoc.getStudent().getStudentName());
				studentClassMap.get(studentSchoolAssoc.getClazz().getClassAndSection()).add(studentVO);
			}
		}
		return studentClassMap;
	}

	/**
	 * Method to init student class map
	 */
	private Map<String, List<StudentVO>> initStudentClassMap(long schoolId) {
		Optional<List<ClassDetail>> classes = classRepository.findClassesBySchoolId(schoolId);
		Map<String, List<StudentVO>> studentClassMap = null;
		if (classes.isPresent()) {
			studentClassMap = new HashMap<>();
			for (ClassDetail classDetail : classes.get()) {
				studentClassMap.put(classDetail.getClassAndSection(), new ArrayList<>());
			}
		}
		return studentClassMap;
	}

	@Transactional
	@Override
	public String uploadStudentData(MultipartFile file, String userId) throws ParseException, IOException {
		LOGGER.debug("Bulk Upload requested by {}",userId);
		Workbook workbook = new XSSFWorkbook(file.getInputStream());
		List<ClassVO> excelClasses = new ArrayList<>();
		if (file != null) {
			long schoolId = getSchoolIFromExcelName(file.getOriginalFilename());
			SchoolVO schoolVO = schoolService.getSchoolDetail(schoolId);

			Map<String, ClassVO> classMap = new HashMap<>();
			for (ClassVO classVO : schoolVO.getClassList()) {
				classMap.put(classVO.getClassAndSectionName(), classVO);
			}

			String classCountError = validateClassVsSheetCount(workbook, classMap);
			// If there is mismatch in the class count in the sheet and database return with
			// error message
			if (!StringUtils.isEmpty(classCountError)) {
				return classCountError;
			}

			// Iterate through each sheet, create class and persist
			for (int i = 1; i < workbook.getNumberOfSheets(); i++) {
				Sheet currentSheet = workbook.getSheetAt(i);
				String sheetHeaderErrorMsg = validateSheetHeader(currentSheet);
				if (!StringUtils.isEmpty(validateSheetHeader(currentSheet))) {
					return sheetHeaderErrorMsg;
				}
				ClassVO classVO = classMap.get(currentSheet.getSheetName());
				// Update student and check if the student id as part of meta data is modified
				String studentRecordError = createStudentsFromsheet(currentSheet, classVO);
				if (!StringUtils.isEmpty(studentRecordError)) {
					return studentRecordError;
				}
				
				excelClasses.add(classVO);
			}

			// Group students into default group names if any of the student is provided with team name
			// This case occurs when user opt for auto team name by system
			boolean isGroupRandomGenerated = StudentHelper.groupByRandomTeam(excelClasses);
			List<TeamNameCountVO> teamNameCountVOs = new ArrayList<>();
			// For a map container for easy retrieval based on team name
			Map<String, TeamNameCountVO> teamNameMap = new HashMap<>();
			
			// If not random generated then consider the existing team list for validation
			if (!isGroupRandomGenerated) {
				teamNameCountVOs = getSchoolTeamList(schoolId);
				for (TeamNameCountVO teamNameCountVO : teamNameCountVOs) {
					teamNameMap.put(teamNameCountVO.getTeamName(), teamNameCountVO);
				}
			}
		
			for (ClassVO classVO2 : excelClasses) {
				// Check if any of the team name on student is empty
				String studentRecordError = checkTeamNameEmpty(classVO2);
				if (!StringUtils.isEmpty(studentRecordError)) {
					return studentRecordError;
				}
				// Check if the team has been assigned with size 3,4 or 5 number of students and
				// it is not duplicate team name
				String teamNameError = StudentHelper.validateTeamCountAndUniquness(classVO2, schoolId, teamNameMap);
				if (!StringUtils.isEmpty(teamNameError)) {
					return teamNameError;
				}
			}
			
			// Check if all the class has minimum 3 and maximum 5 members in each team
			for (Map.Entry<String, TeamNameCountVO> entry : teamNameMap.entrySet()) {
				TeamNameCountVO teamNameCountVO = entry.getValue();
				if (teamNameCountVO.getStudentCount() > 5 || teamNameCountVO.getStudentCount() < 3) {
					return StudentHelper.TEAM_COUNT_NOT_VALID.concat(teamNameCountVO.getClassSectionName())
							.concat(StudentHelper.WITH_TEAM_NAME)
							.concat(teamNameCountVO.getTeamName()).concat(StudentHelper.HAS_COUNT)
							.concat(teamNameCountVO.getStudentCount() + StudentHelper.BLANK);
				}
			}
			
			// Finally save or update all the students to the DB
			for (ClassVO classVO2 : excelClasses) {
				classVO2.setUserId(userId);
				this.saveOrUpdateStudents(classVO2);
			}
		}
		return StudentHelper.BULK_UPLOAD_SUCCESS;
	}
	
  	private String validateSheetHeader(Sheet currentSheet) {
		Row row = currentSheet.getRow(0);
		if (!((row.getCell(0).getStringCellValue().equals(ExcelHelper.HEADER_ID))
				&& (row.getCell(1).getStringCellValue().equals(ExcelHelper.HEADER_STUDENT_NAME))
				&& (row.getCell(2).getStringCellValue().equals(ExcelHelper.HEADER_TEAM_NAME)))) {
			return StudentHelper.SHEET_HEADER_METADATA_MODIFIED + currentSheet.getSheetName();
		}
		return null;
	}

	/**
	 * Iterate through the sheet and add the student information retrieved from
	 * excel to classVO
	 * 
	 * @param currentSheet
	 * @param classVO
	 * @return error message if any
	 */
	private String createStudentsFromsheet(Sheet currentSheet, ClassVO classVO) {
		List<StudentVO> students = new ArrayList<>();
		Iterator<Row> iterator = currentSheet.iterator();
		// Skip the header row
		iterator.next();
		// Iterate through each student row
		while (iterator.hasNext()) {
			Row currentRow = iterator.next();
			StudentVO studentVO = new StudentVO();
			Cell idCell = currentRow.getCell(0);
			Cell nameCell = currentRow.getCell(1);
			Cell teamNameCell = currentRow.getCell(2);
			
			// when all the cells are empty then break the loop, considering sheet ends
			if ((null == idCell || idCell.getCellType() == Cell.CELL_TYPE_BLANK)
					&& (null == nameCell || nameCell.getCellType() == Cell.CELL_TYPE_BLANK)
					&& (null == teamNameCell || teamNameCell.getCellType() == Cell.CELL_TYPE_BLANK)) {
				break;
			}
			
			// If cell is null it means student id not present then it is a new record
			if (null != idCell && idCell.getCellType() != Cell.CELL_TYPE_BLANK) {
				long studentId = (long) idCell.getNumericCellValue();
				studentVO.setId(studentId);
				Optional<StudentSchoolAssoc> schoolAssoc = studentSchoolAssocRepository
						.findByClazzIdAndStudentId(classVO.getId(), studentId);
				// If the student id is available in sheet but not in DB then throw the error
				// message
				if (!schoolAssoc.isPresent()) {
					return StudentHelper.STUDENT_ID_MODIFIED.concat(currentSheet.getSheetName())
							.concat(StudentHelper.AT_ROW_NUM).concat(currentRow.getRowNum() + StudentHelper.BLANK);
				} else {
					studentVO.setAssociationId(schoolAssoc.get().getId());
				}
			}

			// Validate and set the student name
			
			
			if (null == nameCell || nameCell.getCellType() == Cell.CELL_TYPE_BLANK) {
				return StudentHelper.STUDENT_NAME_EMPTY.concat(currentSheet.getSheetName()).concat(StudentHelper.AT_ROW_NUM)
						.concat(currentRow.getRowNum() + StudentHelper.BLANK);
			} else {
				studentVO.setStudentName(nameCell.getStringCellValue());
			}

			// Validate and set Team Name
			if (null == teamNameCell || teamNameCell.getCellType() == Cell.CELL_TYPE_BLANK) {
				studentVO.setTeamName(null);
			} else {
				studentVO.setTeamName(teamNameCell.getStringCellValue());
			}
			students.add(studentVO);
		}
		classVO.setStudentList(students);
		return null;
	}

	private String checkTeamNameEmpty(ClassVO classVO) {
		Iterator<StudentVO> iterator = classVO.getStudentList().iterator();
		// Iterate through each student row
		while (iterator.hasNext()) {
			StudentVO studentVO = iterator.next();
			if (StringUtils.isEmpty(studentVO.getTeamName())) {
				return StudentHelper.TEAM_NAME_EMPTY.concat(classVO.getClassAndSectionName());
			}
		}
		return null;
	}

	private String validateClassVsSheetCount(Workbook workbook, Map<String, ClassVO> classMap) {
		if (workbook.getNumberOfSheets() - 1 != classMap.size()) {
			return StudentHelper.CLASS_COUNT_MISMATCH;
		}
		// Skip the instruction sheet
		for (int i = 1; i < workbook.getNumberOfSheets(); i++) {
			// Check if the sheet name is an existing class name else return with error
			// message string
			if (null == classMap.get(workbook.getSheetAt(i).getSheetName())) {
				return StudentHelper.CLASS_NAME_NOT_EXIST + workbook.getSheetAt(i).getSheetName();
			}
		}
		return null;
	}

	private long getSchoolIFromExcelName(String excelName) {
		String schoolId = excelName.substring(excelName.indexOf("Bulk_Upload_Student_") + 20,
				excelName.indexOf("_", excelName.indexOf("Bulk_Upload_Student_") + 21));
		return Long.parseLong(schoolId);
	}
}
