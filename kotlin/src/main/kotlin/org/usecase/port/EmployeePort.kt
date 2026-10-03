package org.example.org.usecase.port

import org.example.org.domain.Employee

interface EmployeePort {
    fun allEmployees(): List<Employee>

}
