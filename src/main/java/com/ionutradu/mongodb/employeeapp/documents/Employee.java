package com.ionutradu.mongodb.employeeapp.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Document(collection = "employee")
public class Employee {

    @Transient
    public static final String SEQUENCE_NAME = "users_sequence";

    private static long nextId = 0;

    @Id
    private long id;

    @NotBlank
    @Size(max=32)
    private String firstName;

    @NotBlank
    @Size(max=32)
    private String lastName;

    private LocalDate dateOfBirth;
    private int managerId;
    private double salary;
    private String department;

    public Employee() {
    }

    public Employee(String firstName, String lastName, LocalDate dateOfBirth, int managerId, double salary, String department) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.managerId = managerId;
        this.salary = salary;
        this.department = department;
    }

    public static long getNextId() {
        return nextId;
    }

    public static void setNextId(long nextId) {
        Employee.nextId = nextId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public int getManagerId() {
        return managerId;
    }

    public void setManagerId(int managerId) {
        this.managerId = managerId;
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
                ", managerId=" + managerId +
                ", salary=" + salary +
                ", department='" + department + '\'' +
                '}';
    }
}
