package com.codehunt.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class EmployeeDto {
	@NotEmpty(message="name cant be empty")
	@NotBlank(message="name cannot be blank")
	private String name;
	
	@NotEmpty(message="address cant be empty")
	@NotBlank(message="address cannot be blank")
	private String address;
	private int salary;

}
