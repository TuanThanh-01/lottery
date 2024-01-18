package com.qldt.lottery.service;

import com.qldt.lottery.data.ResultRequest;
import com.qldt.lottery.entity.Employee;
import com.qldt.lottery.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService implements IEmployeeService {
    private final FileUtils fileWriter;
    private static final int COLUMN_INDEX_EMPLOYEE_ID = 0;
    private static final int COLUMN_INDEX_EMAIL = 1;
    private static final int COLUMN_INDEX_NAME = 2;

    @Override
    public List<Employee> getAllEmployee() throws IOException, InvalidFormatException {
        List<String> lstEmployeeIDReceivedPrize =  getLstEmployeeIDReceivedPrize();
        List<Employee> lstEmployee = new ArrayList<>();
        ClassPathResource resource = new ClassPathResource("datasample.xlsx");
        InputStream inputStream = new FileInputStream(resource.getFile());
        Workbook workbook = WorkbookFactory.create(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        for (Row nextRow : sheet) {
            if (nextRow.getRowNum() == 0) {
                continue;
            }
            // Get all cells
            Iterator<Cell> cellIterator = nextRow.cellIterator();
            // Read cells and set value for book object
            Employee employee = new Employee();
            while (cellIterator.hasNext()) {
                //Read cell
                Cell cell = cellIterator.next();
                Object cellValue = getCellValue(cell);
                if (cellValue == null || cellValue.toString().isEmpty()) {
                    continue;
                }
                // Set value for book object
                int columnIndex = cell.getColumnIndex();
                switch (columnIndex) {
                    case COLUMN_INDEX_EMPLOYEE_ID -> employee.setEmployeeID(String.valueOf(getCellValue(cell)));
                    case COLUMN_INDEX_EMAIL -> employee.setEmail((String) getCellValue(cell));
                    case COLUMN_INDEX_NAME -> employee.setName((String) getCellValue(cell));
                    default -> {
                    }
                }
            }

            if(!lstEmployeeIDReceivedPrize.contains(employee.getEmployeeID())){
                lstEmployee.add(employee);
            }
        }
        return lstEmployee;
    }

    @Override
    public void saveResult(ResultRequest resultRequest) {
        String content = resultRequest.getEmployeeID() + "-"
                + resultRequest.getEmail() + "-"
                + resultRequest.getName() + " ====>>>> "
                + resultRequest.getPrize().toUpperCase();
        fileWriter.writeFile(content, resultRequest.getPrize());
        fileWriter.writeFile(content, "tổng kết");
    }

    public List<String> getLstEmployeeIDReceivedPrize() {
        String content = fileWriter.readFile("result/tổng kết.txt");
        String [] lstEmployeeData = content.split("\r");
        List<String> lstEmployeeID = new ArrayList<>();
        for(String employeeData : lstEmployeeData) {
            lstEmployeeID.add(employeeData.split("-")[0].replace("\n", ""));
        }
        lstEmployeeID.remove(lstEmployeeID.size() - 1);
        return lstEmployeeID;
    }

    private static Object getCellValue(Cell cell) {
        CellType cellType = cell.getCellTypeEnum();
        Object cellValue = null;
        switch (cellType) {
            case BOOLEAN -> cellValue = cell.getBooleanCellValue();
            case FORMULA -> {
                Workbook workbook = cell.getSheet().getWorkbook();
                FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                cellValue = evaluator.evaluate(cell).getNumberValue();
            }
            case NUMERIC -> cellValue = (int) cell.getNumericCellValue();
            case STRING -> cellValue = cell.getStringCellValue();
            default -> {
            }
        }
        return cellValue;
    }
}
