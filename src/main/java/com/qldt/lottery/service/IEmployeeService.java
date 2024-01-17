package com.qldt.lottery.service;

import com.qldt.lottery.data.ResultRequest;
import com.qldt.lottery.entity.Employee;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.util.List;

public interface IEmployeeService {

    List<Employee> getAllEmployee() throws IOException, InvalidFormatException;

    void saveResult(ResultRequest resultRequest);
}