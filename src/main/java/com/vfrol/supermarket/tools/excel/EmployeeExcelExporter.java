package com.vfrol.supermarket.tools.excel;

import com.vfrol.supermarket.dto.employee.EmployeeDetailsDTO;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class EmployeeExcelExporter extends BaseExcelExporter<EmployeeDetailsDTO> {

    @Override
    protected String getSheetName() {
        return "Employees Full Report";
    }

    @Override
    protected String[] getColumns() {
        return new String[]{
                "ID", "Surname", "Name", "Patronymic", "Role",
                "Salary", "Date of Birth", "Date of Start",
                "Phone", "City", "Street", "Zip Code"
        };
    }

    @Override
    protected void fillRow(EmployeeDetailsDTO emp, Row row, ExcelStyles styles) {
        row.createCell(0).setCellValue(emp.id());
        row.createCell(1).setCellValue(emp.surname());
        row.createCell(2).setCellValue(emp.name());
        row.createCell(3).setCellValue(emp.patronymic() != null ? emp.patronymic() : "");
        row.createCell(4).setCellValue(emp.role().name());

        Cell salary = row.createCell(5);
        salary.setCellValue(emp.salary());
        salary.setCellStyle(styles.money);

        Cell dob = row.createCell(6);
        dob.setCellValue(emp.dateOfBirth());
        dob.setCellStyle(styles.date);

        Cell dos = row.createCell(7);
        dos.setCellValue(emp.dateOfStart());
        dos.setCellStyle(styles.date);

        row.createCell(8).setCellValue(emp.phoneNumber());
        row.createCell(9).setCellValue(emp.city() != null ? emp.city() : "");
        row.createCell(10).setCellValue(emp.street() != null ? emp.street() : "");
        row.createCell(11).setCellValue(emp.zipCode() != null ? emp.zipCode() : "");
    }
}