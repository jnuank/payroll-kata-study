package org.example.org.usecase.port

import org.example.org.domain.Employee

interface PayRollEventPort {
    fun payed(employee: Employee): Unit
}
