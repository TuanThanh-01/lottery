package com.qldt.lottery.controller;

import com.qldt.lottery.data.ResultRequest;
import com.qldt.lottery.entity.Employee;
import com.qldt.lottery.service.EmployeeService;
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
    public ResponseEntity<List<Employee>> getAllEmployee() throws IOException, InvalidFormatException {
        return ResponseEntity.ok(employeeService.getAllEmployee());
    }

    @PostMapping("/save-result")
    public ResponseEntity<String> saveResult(@RequestBody ResultRequest resultRequest) {
        employeeService.saveResult(resultRequest);
        return ResponseEntity.ok("Save result success!");
    }

    @PostMapping("/save-list-result")
    public ResponseEntity<String> saveListResult(@RequestBody List<ResultRequest> resultRequest) {
        return ResponseEntity.ok(employeeService.saveListResult(resultRequest));
    }
}
