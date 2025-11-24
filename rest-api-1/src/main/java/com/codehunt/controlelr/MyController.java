package com.codehunt.controlelr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.codehunt.RestApi1Application;
import com.codehunt.dto.EmployeeDto;
import com.codehunt.entity.EmployeeEntity;
import com.codehunt.service.MyService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/")
public class MyController {

    
	@Autowired 
	private MyService service;

    
	
	@PostMapping("/employees")
	public ResponseEntity<?> insert(@Valid @RequestBody EmployeeDto emp,BindingResult bg)
	{
		if(bg.hasErrors())
		{
			ArrayList<String> list=new ArrayList<>();
			for (ObjectError objectError : bg.getAllErrors()) {
				list.add(objectError.getDefaultMessage());
			}
			return new ResponseEntity<>(list,HttpStatus.BAD_REQUEST);
		}
		else
		{
			//service
			EmployeeEntity saved = service.insert(emp);
			Map<Object, Object> map = new HashMap<>();
			map.put("message", "employee saved sucessfully");
			map.put("data", saved);
			return new ResponseEntity<>(map,HttpStatus.OK);
		}
		
	}
	
	@GetMapping("/employees")
	public ResponseEntity<?> readAll()
	{
		List<EmployeeEntity> list = service.readAll();
			return  ResponseEntity.ok(list);
	}
	
	@GetMapping("employees/{id}")
	public ResponseEntity<?> readById(@PathVariable int id)
	{
		Optional<EmployeeEntity> employee = service.readById(id);
		if(employee.isPresent())
		{
			return ResponseEntity.ok(employee.get());
		}
		else
		{
			return ResponseEntity.notFound().build();
		}
	}
	
	//full object update by put mapping 
	@PutMapping("/employees/{id}")
	public ResponseEntity<?> fullUpdate(@PathVariable int id,@Valid @RequestBody EmployeeDto employeeDto,BindingResult bg)
	{
		if(bg.hasErrors())
		{
			List<String> list=new ArrayList<>();
			for (ObjectError error : bg.getAllErrors()) {
				list.add(error.getDefaultMessage());
			}
			return new ResponseEntity<>(list,HttpStatus.BAD_REQUEST);
		}
		else
		{
			//go to service to update the data
			Optional<EmployeeEntity> oldemp = service.readById(id);
			if(oldemp.isEmpty())
			{
				return ResponseEntity.notFound().build();
			}
			else
			{
				EmployeeEntity updatedemp = service.fullupdate(oldemp.get(),employeeDto);
				Map<Object, Object> map = new HashMap<>();
				map.put("message", "updated successfully");
				map.put("data",updatedemp);
				return ResponseEntity.ok(map);
			}
		}
	}
	
	@PatchMapping("/employees/{id}")
	public ResponseEntity<?> updatePartial(@PathVariable int id,@RequestBody Map<String,Object> map) 
	{
		 List<String> errorlist = service.validate(map);
		 if(errorlist.isEmpty())
		 {
			 //check if that id exist for which we are trying to update
			 Optional<EmployeeEntity> existingEmp = service.readById(id);
			 if(existingEmp.isEmpty())
			 {
				 return ResponseEntity.notFound().build(); 
			 }
			 else
			 {
				 //perform update
				 EmployeeEntity updatedEmp = service.partialUpdate(existingEmp.get(),map);
				 
					 Map<String,Object> resultmap=new HashMap<>();
					 resultmap.put("messge","updated sucessfully");
					 resultmap.put("data",updatedEmp);
					 return new ResponseEntity<>(resultmap,HttpStatus.OK);
				 
			 }
			 
		 }
		 
		 {
			 return new ResponseEntity<>(errorlist,HttpStatus.BAD_REQUEST);
		 }
	}
	

}
