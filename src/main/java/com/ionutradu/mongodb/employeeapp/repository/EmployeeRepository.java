package com.ionutradu.mongodb.employeeapp.repository;

import com.ionutradu.mongodb.employeeapp.documents.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends MongoRepository<Employee, Integer>{

    List<Employee> findByDepartmentIs(String department);
    List<Employee> findByDepartmentIsAndSalaryIs(String department, double salary);
    Employee findByFirstNameIs(String firstName);
}
