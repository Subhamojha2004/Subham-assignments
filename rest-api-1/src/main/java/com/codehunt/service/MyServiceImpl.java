package com.codehunt.service;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.codehunt.dto.EmployeeDto;
import com.codehunt.entity.EmployeeEntity;
import com.codehunt.repo.EmployeeRepository;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@Service
public class MyServiceImpl implements MyService{
	
	
	@Autowired
	private EmployeeRepository repo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	private Validator validator;
	public MyServiceImpl() {
		ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
		
//		Validator validator = vf.getValidator();
//		this.validator=validator;
		
		//insted
		this.validator=vf.getValidator();

	}
	

	@Override
	public EmployeeEntity insert(EmployeeDto emp) {
		// TODO Auto-generated method stub
		EmployeeEntity employee = modelMapper.map(emp,EmployeeEntity.class);
		employee.setDate(LocalDate.now());
		EmployeeEntity saved = repo.save(employee);
		return saved;
	}

	

	@Override
	public List<EmployeeEntity> readAll() {
		// TODO Auto-generated method stub
		List<EmployeeEntity> list = repo.findAll();
		return list;
	}

	@Override
	public Optional<EmployeeEntity> readById(int id) {
		// TODO Auto-generated method stub
		return repo.findById(id);
	}

	

	@Override
	public EmployeeEntity fullupdate(EmployeeEntity existingEmp, @Valid EmployeeDto employeeDto) {
		// TODO Auto-generated method stub
		modelMapper.map(employeeDto,existingEmp);
		existingEmp.setDate(LocalDate.now());
		return repo.save(existingEmp);
	}

	@Override
	public List<String> validate(Map<String, Object> map) {
		// TODO Auto-generated method stub
		List<String> errorlist=new ArrayList<>();
		
		for(Entry<String,Object> entry:map.entrySet())
		{
			String fieldName=entry.getKey();
			Object fieldValue=entry.getValue();
			
			try 
			{
				Field field = EmployeeDto.class.getDeclaredField(fieldName);
				field.setAccessible(true);
				EmployeeDto employeeDto=new EmployeeDto();
				field.set(employeeDto, fieldValue);
//				Set<ConstraintViolation<EmployeeDto>> violations = validator.validate(employeeDto);
				Set<ConstraintViolation<EmployeeDto>> violations =validator.validateValue(EmployeeDto.class, fieldName, fieldValue);
				for(ConstraintViolation<EmployeeDto> violation:violations)
				{
					errorlist.add(violation.getMessage());
				}
			} 
			catch (NoSuchFieldException | IllegalAccessException e) 
			{
				// TODO if the field doesnot exist
				errorlist.add("invalid feild access : "+fieldName);
				e.printStackTrace();
			} 				
		}
		
		return errorlist;
	}


	@Override
	public EmployeeEntity partialUpdate(EmployeeEntity existingEntity, Map<String, Object> map) {
		// TODO Auto-generated method stub
		for(Entry<String,Object> entry:map.entrySet())
		{
			String fieldName=entry.getKey();
			Object fieldVlaue=entry.getValue();
			
			//traditional way to set the vlaues
			/*if(fieldName.equals("name"))
			{
				existingEntity.setName(fieldName);
			}
			if(fieldName.equals("address"))
			{
				existingEntity.setAddress(fieldName);
			}
			if(fieldName.equals("salary"))
			{
				existingEntity.setSalary(Integer.parseInt(fieldName));
			}*/
			
			//scalable way
			try {
				Field field = EmployeeEntity.class.getDeclaredField(fieldName);
				field.setAccessible(true);
				field.set(existingEntity,fieldVlaue);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			existingEntity.setDate(LocalDate.now());
		}
		return repo.save(existingEntity);
		
	}

	
}
