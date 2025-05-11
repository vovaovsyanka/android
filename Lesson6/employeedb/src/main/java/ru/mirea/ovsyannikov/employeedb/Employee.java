package ru.mirea.ovsyannikov.employeedb;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Employee {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public String name;
    public int salary;
    public String superpower;

    public Employee() {}

    public Employee(String name, int salary, String superpower) {
        this.name = name;
        this.salary = salary;
        this.superpower = superpower;
    }
}
