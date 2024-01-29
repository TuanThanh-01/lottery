package com.qldt.lottery.service;

import com.qldt.lottery.data.ResultRequest;
import com.qldt.lottery.entity.Employee;
import com.qldt.lottery.util;
import lombok.RequiredArgsConstructor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@RequiredArgsConstructor
public class EmployeeService implements IEmployeeService {
    private final ResourceLoader resourceLoader;

    @Override
    public void getAllEmployee() throws IOException, InvalidFormatException {
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
                    case util.COLUMN_INDEX_EMPLOYEE_ID -> employee.setEmployeeID(String.valueOf(getCellValue(cell)));
                    case util.COLUMN_INDEX_EMAIL -> employee.setEmail((String) getCellValue(cell));
                    case util.COLUMN_INDEX_NAME -> employee.setName((String) getCellValue(cell));
                    case util.COLUMN_UNIT_NAME ->  employee.setUnit((String) getCellValue(cell));
                    default -> {
                    }
                }
            }
            util.AllEmployee.add(employee);

        }
    }

    @Override
    public Employee getWinner() {
        Random random = new Random();
        int randomIndex = random.nextInt(util.AllEmployee.size());
        return util.AllEmployee.get(randomIndex);
    }

    public List<Employee> getListWinner(int num) {
        Set<Employee> employeeSet = new HashSet<>();
        while(employeeSet.size()<num){
            employeeSet.add(getWinner());
        }

        System.out.println("Set: " + employeeSet);
        return employeeSet.stream().toList();
    }

    @Override
    public void saveResult(ResultRequest resultRequest) {
        String id = resultRequest.getEmployeeID();

        String content = resultRequest.getEmployeeID() + "-"
                + resultRequest.getEmail() + "-"
                + resultRequest.getName() + " ====>>>> "
                + resultRequest.getPrize().toUpperCase();
        writeFile(content, resultRequest.getPrize());
        writeFile(content, "tổng kết");
        for (Employee employee : util.AllEmployee) {
            if (employee.getEmployeeID().equals(id)) {

                util.AllEmployee.remove(employee);
                System.out.println("remove: " + id + ". List size: " + util.AllEmployee.size());
                break;
            }
        }
    }

    @Override
    public String saveListResult(List<ResultRequest> lstResultRequest) {
        StringBuilder sb = new StringBuilder();
        lstResultRequest.forEach(resultRequest -> {
            String content = resultRequest.getEmployeeID() + "-"
                    + resultRequest.getEmail() + "-"
                    + resultRequest.getName() + " ====>>>> "
                    + resultRequest.getPrize().toUpperCase() + "\n";
            sb.append(content);
        });
        sb.deleteCharAt(sb.length() - 1);
        writeFile(sb.toString(), lstResultRequest.get(0).getPrize());
        writeFile(sb.toString(), "tổng kết");
        return sb.toString();
    }

//    public List<String> getLstEmployeeIDReceivedPrize() {
//        String content = readFile();
//        String [] lstEmployeeData = content.split("[\\r\\n]+");
//        System.out.println(Arrays.toString(lstEmployeeData));
//        List<String> lstEmployeeID = new ArrayList<>();
//        for(String employeeData : lstEmployeeData) {
//            lstEmployeeID.add(employeeData.split("-")[0].replace("\n", ""));
//        }
//        if(lstEmployeeID.isEmpty()){
//            return lstEmployeeID;
//        }
//
//        if(lstEmployeeID.get(0).equalsIgnoreCase("")){
//            lstEmployeeID.remove(lstEmployeeID.size() - 1);
//        }
//        return lstEmployeeID;
//    }

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

    private String readFile() {
        Resource resource = resourceLoader.getResource("classpath:result/tổng kết.txt");
        try {
            InputStream inputStream = resource.getInputStream();
            byte[] fileData = FileCopyUtils.copyToByteArray(inputStream);
            inputStream.close();
            return new String(fileData, StandardCharsets.UTF_8);
        } catch (IOException e) {
            // Xử lý lỗi khi không thể đọc file
            e.printStackTrace();
            return null;
        }
    }

    private void writeFile(String content, String prize) {
        try {
            File file = new File("src/main/resources/result/" + prize + ".txt");
            // Tạo file mới nếu file chưa tồn tại
            if (!file.exists()) {
                file.createNewFile();
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
            writer.write(content);
            writer.newLine(); // Thêm dòng mới
            writer.close();
        } catch (IOException e) {
            // Xử lý lỗi khi không thể ghi file
            e.printStackTrace();
        }
    }
}
