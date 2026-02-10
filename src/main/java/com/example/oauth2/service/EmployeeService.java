package com.example.oauth2.service;

import com.example.oauth2.model.dto.request.EmployeeCreateRequest;
import com.example.oauth2.model.dto.request.EmployeeDeleteResponse;
import com.example.oauth2.model.dto.request.EmployeeUpdateRequest;
import com.example.oauth2.model.dto.response.EmployeeResponse;
import com.example.oauth2.model.entity.Employee;
import com.example.oauth2.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeResponse createEmployee(EmployeeCreateRequest request) {
        Employee employee = new Employee();
        employee.setFirstName(request.firstName());
        employee.setLastName(request.lastName());
        employee.setEmail(request.email());
        return new EmployeeResponse(employeeRepository.save(employee));
    }

    public List<EmployeeResponse> getAllEmployees() {
        return employeeRepository.findAll()
                .stream()
                .map(EmployeeResponse::new)
                .toList();
    }

    public EmployeeDeleteResponse deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow();
        employeeRepository.delete(employee);
        return new EmployeeDeleteResponse(id, true);
    }

    public EmployeeResponse getAllEmployeeById(Long id) {
        return new EmployeeResponse(employeeRepository.findById(id)
                .orElseThrow());
    }

    public EmployeeResponse updateEmployee(EmployeeUpdateRequest request) {
        Employee employee = employeeRepository.findById(request.id()).orElseThrow();
        employee.setFirstName(request.firstName());
        employee.setLastName(request.lastName());
        employee.setEmail(request.email());
        return new EmployeeResponse(employeeRepository.save(employee));
    }
}
