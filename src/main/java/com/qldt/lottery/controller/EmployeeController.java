package com.qldt.lottery.controller;

import com.qldt.lottery.data.ResultRequest;
import com.qldt.lottery.entity.Employee;
import com.qldt.lottery.service.EmployeeService;
import com.qldt.lottery.util;
import lombok.RequiredArgsConstructor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/lottery")
@RequiredArgsConstructor
@CrossOrigin("*")
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("/all-employee-data")
    public ResponseEntity<Employee> getAllEmployee() throws IOException, InvalidFormatException {
        if(util.AllEmployee.isEmpty()){
            employeeService.getAllEmployee();
        }
        return ResponseEntity.ok(employeeService.getWinner());
    }

    @GetMapping("/winner-employee-data")
    public ResponseEntity<Employee> getWinnerEmployee() throws IOException, InvalidFormatException {
        if(util.AllEmployee.isEmpty()){
            employeeService.getAllEmployee();
            System.out.println(util.AllEmployee);
        }
        Employee employee = employeeService.getWinner();
        System.out.println(employee);
        return ResponseEntity.ok(employee);
    }

    @GetMapping("/list-winner-employee-data")
    public ResponseEntity<List<Employee>> getListWinnerEmployee() throws IOException, InvalidFormatException {
        if(util.AllEmployee.isEmpty()) {
            employeeService.getAllEmployee();
            System.out.println(util.AllEmployee);
        }
        return ResponseEntity.ok(employeeService.getListWinner(util.DEFAULT_NUM_OF_EMPLOYEES));
    }

    @PostMapping("/save-result")
    public ResponseEntity<String> saveResult(@RequestBody ResultRequest resultRequest) {
        System.out.println("Result: " + resultRequest.toString());
        employeeService.saveResult(resultRequest);
        return ResponseEntity.ok("Save result success!");
    }

    @PostMapping("/save-list-result")
    public ResponseEntity<String> saveListResult(@RequestBody List<ResultRequest> resultRequest) {
        return ResponseEntity.ok(employeeService.saveListResult(resultRequest));
    }
}
