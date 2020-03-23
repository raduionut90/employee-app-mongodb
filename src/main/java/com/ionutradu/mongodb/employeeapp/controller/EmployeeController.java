package com.ionutradu.mongodb.employeeapp.controller;

import com.ionutradu.mongodb.employeeapp.documents.Employee;
import com.ionutradu.mongodb.employeeapp.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    EmployeeRepository employeeRepository;

    @PostMapping()
    public String save(@RequestBody Employee employee){
        employee.checkLenghtName();
        employeeRepository.save(employee);
        return employee.toString();
    }

    @PutMapping()
    public String update(@RequestBody Employee employee){
        if(employeeRepository.existsById(employee.getId()) == false){
            throw new RuntimeException("Employee id not found - " + employee.getId());
        }
        employee.checkLenghtName();
        employeeRepository.save(employee);
        return employee + " has been updated";
    }

    @GetMapping
    public List<Employee> findAll(){
        return employeeRepository.findAll();
    }

    @GetMapping("/{id}")
    public Employee findById(@PathVariable int id){
        Optional<Employee> temp = employeeRepository.findById(id);
        Employee employee = temp.get();
        if(employee == null) {
            throw new RuntimeException("Employee id not found - " + id);
        }
        return employee;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable int id){
        Employee employee = findById(id);
        employeeRepository.delete(employee);
        return employee + " has been deleted";
    }

    @DeleteMapping("/all")
    public String deleteAll(){
        employeeRepository.deleteAll();
        return "DB clean";
    }

    @PostMapping("/report/{department}")
    public String reports(@PathVariable String department){

        Employee employeeMaxSalary = maxSalaryFromDepartment(department);
        Employee theManager = checkManagerEmployees();

        return "The employee who has the biggest salary in a given " + department + " department is " + employeeMaxSalary +
                "\n \n The manager who has the most direct employees coordinated by him is " + theManager;
//        return department;
    }

    public Employee checkManagerEmployees(){
        List<Integer> list = employeeRepository.findAll().stream().map(x -> x.getManager()).collect(Collectors.toList());
        long size = employeeRepository.findAll().stream().count();

        // Insert all elements in hash
        Map<Integer, Integer> hp =
                new HashMap<Integer, Integer>();

        for(int i = 0; i < size; i++)
        {
            int key = list.get(i);
            if(hp.containsKey(key))
            {
                int freq = hp.get(key);
                freq++;
                hp.put(key, freq);
            }
            else
            {
                hp.put(key, 1);
            }
        }

        // find max frequency.
        int max_count = 0, res = -1;

        for(Map.Entry<Integer, Integer> val : hp.entrySet())
        {
            if (max_count < val.getValue())
            {
                res = val.getKey();
                max_count = val.getValue();
            }
        }

        Employee manager = employeeRepository.findById(res).get();
        return manager;

    }

    public Employee maxSalaryFromDepartment(String department){
        Employee employee = employeeRepository.findAll().stream()
                .filter(empl -> empl.getDepartment().equals(department))
                .max(Comparator.comparingDouble(Employee::getSalary)).get();
        return employee;
    }


}
