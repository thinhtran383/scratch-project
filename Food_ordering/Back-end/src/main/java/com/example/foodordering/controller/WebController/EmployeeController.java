package com.example.foodordering.controller.WebController;

import com.example.foodordering.DTO.PasswordForgotDTO;
import com.example.foodordering.DTO.ResponseObject;
import com.example.foodordering.entity.Employee;
import com.example.foodordering.services.WebServices.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/register")
    public ResponseEntity<ResponseObject> registerEmployee(@RequestBody Employee employee) {
        Optional<Employee> isExist = Optional.ofNullable(employeeService.findByUsername(employee.getUsername()));
        if(isExist.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("false", "Username is exits", "")
            );
        }

        Employee savedEmployee = employeeService.saveEmployee(employee);
        if (savedEmployee != null) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Registration successfully", "")
            );
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("false", "Registration failed", "")
            );
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseObject> loginEmployee(@RequestBody Employee request) {
        Employee foundEmployee = employeeService.findByUsername(request.getUsername());
        if (foundEmployee != null && employeeService.checkPassword(foundEmployee, request.getPassword())) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Login successfully", employeeService.findRoleByUsername(request.getUsername()))
            );
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new ResponseObject("failed", "Login failed", "")
            );
        }
    }

    @GetMapping("")
    public ResponseEntity<ResponseObject> getAllEmployees(){ // lay toan bo danh sach nhan vien
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Query successfully", employeeService.getAllEmployees())
        );
    }

    @PutMapping("/forgot")
    public ResponseEntity<ResponseObject> forgotPassword(@RequestBody PasswordForgotDTO request){
        employeeService.forgotPassword(request.getPassword(), request.getConfirmPassword());
        return null;
    }
}
