package org.example

import java.time.DayOfWeek
import java.time.LocalDate

interface Employee {
    val id: Int
    val name: String
    fun isPaymentDueOn(date: LocalDate): Boolean
}

data class HourlyEmployee(override val id: Int, override val name: String): Employee {
    override fun isPaymentDueOn(date: LocalDate): Boolean {
        return date.dayOfWeek == DayOfWeek.FRIDAY
    }
}

data class SalaryEmployee(override val id: Int, override val name: String): Employee {
    override fun isPaymentDueOn(date: LocalDate): Boolean {
        return false
    }

}