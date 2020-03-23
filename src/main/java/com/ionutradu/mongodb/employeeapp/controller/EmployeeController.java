package com.ionutradu.mongodb.employeeapp.controller;

import com.ionutradu.mongodb.employeeapp.documents.Employee;
import com.ionutradu.mongodb.employeeapp.repository.EmployeeRepository;
import com.ionutradu.mongodb.employeeapp.services.SequenceGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    SequenceGeneratorService sequenceGeneratorService;

    @PostMapping()
    public Employee createEmployee(@Valid @RequestBody Employee employee) {
        employee.setId(sequenceGeneratorService.generateSequence(Employee.SEQUENCE_NAME));
        return employeeRepository.save(employee);
    }

    @PutMapping()
    public String update(@Valid @RequestBody Employee employee){
        long nextId = sequenceGeneratorService.generateSequence(Employee.SEQUENCE_NAME);
        if(nextId == 0){
            throw new RuntimeException("NEXT ID is - " + nextId);
        }
        employee.setId(nextId);
//        if(employeeRepository.existsById((int)employee.getId()) == false){
//            throw new RuntimeException("Employee id not found - " + employee.getId());
//        }
        checkManagerId(employee);

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

    @PostMapping("/reports/{department}")
    public String reports(@PathVariable String department){

        Employee employeeMaxSalary = maxSalaryFromDepartment(department);
        Employee theManager = checkManagerEmployees();

        return "The employee who has the biggest salary in a given " + department + " department is " + employeeMaxSalary +
                "\n \n The manager who has the most direct employees coordinated by him is " + theManager;
//        return department;
    }

    public Employee checkManagerEmployees(){
        List<Integer> list = employeeRepository.findAll()
                .stream()
                .map(Employee::getManagerId)
                .collect(Collectors.toList());

        long listSize = employeeRepository.findAll()
                .size();

        // Insert all elements in hash
        Map<Integer, Integer> hp =
                new HashMap<Integer, Integer>();

        for(int i = 0; i < listSize; i++)
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
        int max_count = 0, managerId = -1;

        for(Map.Entry<Integer, Integer> val : hp.entrySet())
        {
            if (max_count < val.getValue())
            {
                managerId = val.getKey();
                max_count = val.getValue();
            }
        }

        Employee manager = employeeRepository.findById(managerId).get();
        return manager;

    }

    public Employee maxSalaryFromDepartment(String department){
        Employee employee = employeeRepository.findAll().stream()
                .filter(empl -> empl.getDepartment().equals(department))
                .max(Comparator.comparingDouble(Employee::getSalary)).get();
        return employee;
    }

    public void checkManagerId(Employee employee){
        // managerID = 0 for employee who has no boss. Like a boos :)
        if(!employeeRepository.existsById(employee.getManagerId()) && employee.getManagerId() != 0){
            throw new RuntimeException("Manager ID not found - " + employee.getManagerId());
        }
    }

}
