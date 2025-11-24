package com.codehunt.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.codehunt.dto.EmployeeDto;
import com.codehunt.entity.EmployeeEntity;

import jakarta.validation.Valid;


public interface MyService {

	public EmployeeEntity insert(EmployeeDto emp);

	public List<EmployeeEntity> readAll();

	public Optional<EmployeeEntity> readById(int id);

	public EmployeeEntity fullupdate(EmployeeEntity oldemp, @Valid EmployeeDto employeeDto);

	public List<String> validate(Map<String, Object> map);

	public EmployeeEntity partialUpdate(EmployeeEntity employeeEntity, Map<String, Object> map);

}
