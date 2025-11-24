package com.example.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
//@Entity
public class Employee {
//    @Id
    private int id;
    private String name;
    private int phone;
    private int age;
    private String address;
}
