package org.example.org.domain

import java.time.DayOfWeek
import java.time.LocalDate

interface Employee {
    val id: Int
    val name: String
    fun isPaymentDueOn(date: LocalDate): Boolean
    fun calcPay(): Payment
}

data class HourlyEmployee(override val id: Int, override val name: String): Employee {
    override fun isPaymentDueOn(date: LocalDate): Boolean {
        return date.dayOfWeek == DayOfWeek.FRIDAY
    }

    override fun calcPay(): Payment {
        return Payment(1, 20000)
    }
}

data class SalaryEmployee(override val id: Int, override val name: String): Employee {
    override fun isPaymentDueOn(date: LocalDate): Boolean {
        return false
    }

    override fun calcPay(): Payment {
        TODO("Not yet implemented")
    }

}