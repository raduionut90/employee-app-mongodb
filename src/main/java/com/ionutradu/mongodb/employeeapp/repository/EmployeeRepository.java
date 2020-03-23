package com.ionutradu.mongodb.employeeapp.repository;

import com.ionutradu.mongodb.employeeapp.documents.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends MongoRepository<Employee, Integer>{

}
