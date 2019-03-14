/**
 * ${ExcelHelper}
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

import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.CollectionUtils;

import com.cognizant.outreach.microservices.school.vo.StudentVO;
import com.cognizant.outreach.microservices.school.vo.TeamNameCountVO;

/**
 * Method holds the util methods to create excel sheets for student bulk upload
 * module
 * 
 * @author 371793
 */
public class ExcelHelper {
	public static final String EXCEL_INTRODUCTION_HEADER = "Bulk Upload Instructions";
	public static final String EXCEL_INTRODUCTION_SHEETNAME = "Upload Instructions";
	public static final String EXCEL_DATA_SHEETNAME = "Students_BulkUpload_Sheet";
	public static final String INSTRUCTION_REQUEST_CONTENT = "Please follow below instructions to fill and upload the student bulk upload sheet to avoid data issues";
	public static final String INSTRUCTION_LINE_1 = "1. Don't change the header data, sheet name,tab name and info in hidden columns  in the sheet";
	public static final String INSTRUCTION_LINE_2 = "2. Student name and Team name are mandatory fields";
	public static final String INSTRUCTION_LINE_3 = "3. You can edit the student name and team name for existing records listed";
	public static final String INSTRUCTION_LINE_4 = "4. If you want to delete a student please delete the entire row instead of renaming the fields";
	public static final String INSTRUCTION_LINE_5 = "5. Deleting a student will delete his/her entire performance data";
	public static final String INSTRUCTION_LINE_6 = "6. Add a student by adding a row at the last avoid inserting rows inbetween";
	public static final String INSTRUCTION_LINE_7 = "7. Refer tab name for corresponding class";
	public static final String INSTRUCTION_LINE_8 = "8. A team can contain 3 to 5 students";
	public static final String INSTRUCTION_LINE_9 = "9. Team name should be unique across the school";

	public static final String INSTRUCTION_LINE_10 = "List of team names already taken";
	public static final String STUDENT_COUNT = "Count";
	public static final String TEAM_NAME_HEADER = "Team";
	public static final String CLASS_NAME_HEADER = "Class";
	public static final String HEADER_ID = "Id";
	public static final String HEADER_STUDENT_NAME = "Student Name";
	public static final String HEADER_TEAM_NAME = "Team Name";

	/**
	 * To create the the introduction text cell style
	 * 
	 * @param workBook
	 * @return
	 */
	private static CellStyle getIntroductionHeaderCellStyle(Workbook workBook) {

		CellStyle cellStyle = workBook.createCellStyle();

		// To Set Font Style
		Font font = workBook.createFont();
		font.setFontHeightInPoints((short) 11);
		font.setColor(IndexedColors.BLACK.getIndex());
		font.setBold(true);
		cellStyle.setFont(font);

		cellStyle.setAlignment((short) HorizontalAlignment.CENTER.ordinal());
		cellStyle.setVerticalAlignment((short) VerticalAlignment.CENTER.ordinal());

		return cellStyle;
	}

	/**
	 * To set the border for the fiven cell details
	 * 
	 * @param region
	 * @param cell
	 * @param sheet
	 * @param workbook
	 */
	private static void setBorder(CellRangeAddress region, Cell cell, XSSFSheet sheet, XSSFWorkbook workbook) {

		RegionUtil.setBorderBottom(1, region, sheet, workbook);
		RegionUtil.setBorderTop(1, region, sheet, workbook);
		RegionUtil.setBorderLeft(1, region, sheet, workbook);
		RegionUtil.setBorderRight(1, region, sheet, workbook);

		CellStyle cellStyle = cell.getCellStyle();
		cellStyle.setFillBackgroundColor(IndexedColors.WHITE.getIndex());
	}

	/**
	 * To create style for content other than header
	 * 
	 * @param workBook
	 * @return
	 */
	private static CellStyle getIntroductionValueItemsCellStyle(Workbook workBook) {

		CellStyle cellStyle = workBook.createCellStyle();

		// To Set Font Style
		Font font = workBook.createFont();
		font.setFontHeightInPoints((short) 11);
		cellStyle.setFont(font);

		cellStyle.setAlignment((short) HorizontalAlignment.LEFT.ordinal());
		cellStyle.setVerticalAlignment((short) VerticalAlignment.CENTER.ordinal());

		return cellStyle;
	}

	/**
	 * Method to fill the instruction sheet for bulk upload excel with styling
	 * 
	 * @param workbook
	 * @param sheet
	 */
	public static void fillInstructionsSheet(XSSFWorkbook workbook, XSSFSheet sheet,
			List<TeamNameCountVO> teamNameList) {
		Row row = sheet.createRow(1);
		Cell headerCell = row.createCell(2);
		headerCell.setCellValue(EXCEL_INTRODUCTION_HEADER);
		headerCell.setCellStyle(ExcelHelper.getIntroductionHeaderCellStyle(workbook));
		CellRangeAddress cellMerge = new CellRangeAddress(1, 1, 2, 4);
		sheet.addMergedRegion(cellMerge);
		ExcelHelper.setBorder(cellMerge, headerCell, sheet, workbook);

		createIntroductionCells(workbook, sheet, 3, 2, 11, INSTRUCTION_REQUEST_CONTENT);
		createIntroductionCells(workbook, sheet, 5, 2, 11, INSTRUCTION_LINE_1);
		createIntroductionCells(workbook, sheet, 6, 2, 7, INSTRUCTION_LINE_2);
		createIntroductionCells(workbook, sheet, 7, 2, 9, INSTRUCTION_LINE_3);
		createIntroductionCells(workbook, sheet, 8, 2, 10, INSTRUCTION_LINE_4);
		createIntroductionCells(workbook, sheet, 9, 2, 9, INSTRUCTION_LINE_5);
		createIntroductionCells(workbook, sheet, 10, 2, 10, INSTRUCTION_LINE_6);
		createIntroductionCells(workbook, sheet, 11, 2, 7, INSTRUCTION_LINE_7);
		createIntroductionCells(workbook, sheet, 12, 2, 6, INSTRUCTION_LINE_8);
		createIntroductionCells(workbook, sheet, 13, 2, 7, INSTRUCTION_LINE_9);
		// If team list already available then add the list to instruction sheet else
		// leave it empty
		if (!CollectionUtils.isEmpty(teamNameList)) {
			createIntroductionCells(workbook, sheet, 15, 2, 11, INSTRUCTION_LINE_10);
			Row rowTeam = sheet.createRow(16);
			createCell(workbook, sheet, rowTeam, 2, TEAM_NAME_HEADER);
			createCell(workbook, sheet, rowTeam, 3, STUDENT_COUNT);
			createCell(workbook, sheet, rowTeam, 4, CLASS_NAME_HEADER);
			int rowIndex = 17;
			for (TeamNameCountVO teamNameCountVO : teamNameList) {
				Row rowTeamCount = sheet.createRow(rowIndex);
				createCellForString(workbook, sheet, rowTeamCount, 2, teamNameCountVO.getTeamName());
				createCellForNumeric(workbook, sheet, rowTeamCount, 3, teamNameCountVO.getStudentCount());
				createCellForString(workbook, sheet, rowTeamCount, 4, teamNameCountVO.getClassSectionName());
				rowIndex++;
			}
		}

	}

	/**
	 * To create introduction cells
	 * 
	 * @param workbook
	 * @param sheet
	 * @param rowNumber
	 * @param start
	 * @param end
	 * @param content
	 */
	private static void createIntroductionCells(XSSFWorkbook workbook, XSSFSheet sheet, int rowNumber, int start,
			int end, String content) {
		Row row = sheet.createRow(rowNumber);
		Cell headerCell = row.createCell(2);
		headerCell.setCellValue(content);
		headerCell.setCellStyle(ExcelHelper.getIntroductionValueItemsCellStyle(workbook));
		CellRangeAddress cellMerge = new CellRangeAddress(rowNumber, rowNumber, start, end);
		sheet.addMergedRegion(cellMerge);
	}

	/**
	 * To create cell with string type for the given sheet
	 * 
	 * @param workbook
	 * @param sheet
	 * @param rowTeam
	 * @param cellIndex
	 * @param content
	 */
	private static void createCellForString(XSSFWorkbook workbook, XSSFSheet sheet, Row rowTeam, int cellIndex,
			String content) {

		Cell headerCell = rowTeam.createCell(cellIndex);
		headerCell.setCellType(Cell.CELL_TYPE_STRING);
		headerCell.setCellValue(content);
		headerCell.setCellStyle(ExcelHelper.getIntroductionValueItemsCellStyle(workbook));
	}

	/**
	 * To create a numeric cell for the given sheet
	 * 
	 * @param workbook
	 * @param sheet
	 * @param rowTeam
	 * @param cellIndex
	 * @param content
	 */
	private static void createCellForNumeric(XSSFWorkbook workbook, XSSFSheet sheet, Row rowTeam, int cellIndex,
			long content) {

		Cell headerCell = rowTeam.createCell(cellIndex);
		headerCell.setCellType(Cell.CELL_TYPE_STRING);
		headerCell.setCellValue(content);
		headerCell.setCellStyle(ExcelHelper.getIntroductionValueItemsCellStyle(workbook));
	}

	/**
	 * To create cell with the provided index and content
	 * 
	 * @param workbook
	 * @param sheet
	 * @param teamRow
	 * @param cellIndex
	 * @param content
	 */
	private static void createCell(XSSFWorkbook workbook, XSSFSheet sheet, Row teamRow, int cellIndex, String content) {
		Cell headerCell = teamRow.createCell(cellIndex);
		headerCell.setCellValue(content);
		headerCell.setCellStyle(ExcelHelper.getIntroductionValueItemsCellStyle(workbook));
	}

	/**
	 * Method to create class wise tabs
	 * 
	 * @param workbook
	 * @param studentClassMap
	 */
	public static void createClassWiseSheets(XSSFWorkbook workbook, Map<String, List<StudentVO>> studentClassMap) {
		for (Map.Entry<String, List<StudentVO>> entry : studentClassMap.entrySet()) {
			XSSFSheet sheet = workbook.createSheet(entry.getKey());
			Row row = sheet.createRow(0);
			createCell(workbook, sheet, row, 0, HEADER_ID);
			createCell(workbook, sheet, row, 1, HEADER_STUDENT_NAME);
			createCell(workbook, sheet, row, 2, HEADER_TEAM_NAME);
			int rowNumber = 1;
			if (!CollectionUtils.isEmpty(entry.getValue())) {
				for (StudentVO studentVO : entry.getValue()) {
					Row studentDetailRow = sheet.createRow(rowNumber);
					createCellForNumeric(workbook, sheet, studentDetailRow, 0, studentVO.getId());
					createCellForString(workbook, sheet, studentDetailRow, 1, studentVO.getStudentName());
					createCellForString(workbook, sheet, studentDetailRow, 2, studentVO.getTeamName());
					rowNumber++;
				}
			}
			sheet.setColumnHidden(0, true);
			sheet.setColumnWidth(1, 6000);
			sheet.setColumnWidth(2, 6000);
		}
	}
}
