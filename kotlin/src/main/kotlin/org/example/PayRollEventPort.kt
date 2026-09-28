package org.example.org.example

import org.example.Employee

interface PayRollEventPort {
    fun payed(employee: Employee): Unit
}
