package com.ionutradu.mongodb.employeeapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ionutradu.mongodb.employeeapp.documents.Employee;
import com.ionutradu.mongodb.employeeapp.documents.IdGenerator;
import com.ionutradu.mongodb.employeeapp.repository.EmployeeRepository;
import com.ionutradu.mongodb.employeeapp.services.IdGeneratorService;
import com.ionutradu.mongodb.employeeapp.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class EmployeeController {

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    IdGeneratorService idGeneratorService;

    @Autowired
    StorageService storageService;

    @GetMapping("/employees")
    public List<Employee> findAll(){
        return employeeRepository.findAll();
    }

    @GetMapping("/employees/{id}")
    public Employee findById(@PathVariable int id){
        Optional<Employee> temp = employeeRepository.findById(id);
        Employee employee = temp.orElse(null);
        if(employee == null) {
            throw new RuntimeException("Employee id not found - " + id);
        }
        return employee;
    }

    @PostMapping("/employees")
    public Employee create(@Valid @RequestBody Employee employee) {
        checkManagerId(employee);
        employee.setId(idGeneratorService.generateSequence(Employee.SEQUENCE_NAME));
        return employeeRepository.save(employee);
    }

    @PutMapping("/employees")
    public String update(@RequestBody Employee employee){
        int index = (int)employee.getId();
        if(!employeeRepository.existsById(index)){
            throw new RuntimeException("Employee id not found - " + employee.getId());
        }
        checkManagerId(employee);
        return employeeRepository.save(employee) + " has been updated";
    }

    @DeleteMapping("/employees/{id}")
    public String delete(@PathVariable int id){
        Employee employee = findById(id);
        employeeRepository.delete(employee);
        return employee + " has been deleted";
    }

    @DeleteMapping("employees/all")
    public String deleteAll(){
        employeeRepository.deleteAll();
        idGeneratorService.resetSeq();
        return "DB clean";
    }

    @GetMapping("/reports/{department}")
    public String reports(@PathVariable String department){
        if (employeeRepository.findAll().size() == 0){
            throw new RuntimeException("No employees in db");
        }
        if (employeeRepository.findByDepartmentIs(department).size() == 0){
            throw new RuntimeException(department + " department don`t exist in db");
        }
        List<Employee> employeeMaxSalary = maxSalaryFromDepartment(department);

        Employee theManager = null;
        if (checkBestManager() != 0) {
            int managerId = checkBestManager();
            theManager = employeeRepository.findById(managerId).orElse(null);
        }

        return "The employee who has the biggest salary in a given " + department + " department is " + employeeMaxSalary +
                "\n \n The manager who has the most direct employees coordinated by him is " + theManager;
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file) {
        if (!file.getOriginalFilename().endsWith(".json")){
            throw new RuntimeException("invalid file extension! It must to be .json");
        }
        String filePaths = storageService.uploadFile(file);
        List<Employee> employeeList = storageService.readFile(filePaths);
        createEmployeeByList(employeeList);
        return "You successfully uploaded " + file.getOriginalFilename() + " annd insert in db: \n \n" + employeeList;
    }

    public void createEmployeeByList(List<Employee> employees) {
        for (Employee employee:employees) {
            checkManagerId(employee);
            employee.setId(idGeneratorService.generateSequence(Employee.SEQUENCE_NAME));
            employeeRepository.save(employee);
        }
    }

    public int checkBestManager(){
        List<Integer> list = employeeRepository.findAll()
                .stream()
                .map(Employee::getManagerId)
                .filter(id -> id > 0)
                .collect(Collectors.toList());

        int managerId = 0;
        if (list.size() != 0) {
            managerId = list.stream()
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                    .entrySet()
                    .stream()
                    .max(Map.Entry.comparingByValue())
                    .orElse(null).getKey();
        }

        return managerId;
    }

    public List<Employee> maxSalaryFromDepartment(String department){
        double maxSalary = employeeRepository.findAll()
                .stream()
                .filter(empl -> empl.getDepartment().equals(department))
                .map(Employee::getSalary)
                .max(Double::compareTo).orElse(null);

        return employeeRepository.findByDepartmentIsAndSalaryIs(department, maxSalary);
    }

    public void checkManagerId(Employee employee){
        // managerID = 0 for employee who has no boss. Like a boss :)
        if(!employeeRepository.existsById(employee.getManagerId()) && employee.getManagerId() != 0){
            throw new RuntimeException("Manager ID not found - " + employee.getManagerId());
        }
    }

}
