package com.example.foodordering.repositories;

import com.example.foodordering.common.EmployeeRole;
import com.example.foodordering.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Employee findByUsername(String username);

    @Query("select e.role from Employee e where e.username =:username ")
    EmployeeRole findRoleByUsername(String username);

    List<Employee> findByRole(EmployeeRole role);
}
