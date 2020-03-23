package com.ionutradu.mongodb.employeeapp.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "employee")
public class Employee {

    @Id
    private int id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private int manager;
    private double salary;
    private String department;

    public Employee() {
    }

    public Employee(int id, String firstName, String lastName, LocalDate dateOfBirth, int manager, double salary, String department) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.manager = manager;
        this.salary = salary;
        this.department = department;
    }

    public void checkLenghtName(){
        if (this.getFirstName().length() > 32 ||
                this.getLastName().length() > 32) {
            throw new RuntimeException("The maximum number of characters allowed for first name and last name fields is 32.");
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public int getManager() {
        return manager;
    }

    public void setManager(int manager) {
        this.manager = manager;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthDate=" + dateOfBirth +
                ", manager=" + manager +
                ", salary=" + salary +
                ", department='" + department + '\'' +
                '}';
    }
}
