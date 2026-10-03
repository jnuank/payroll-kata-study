package org.example.org.domain

import java.time.DayOfWeek
import java.time.LocalDate
import kotlin.time.times

interface Employee {
    val id: Int
    val name: String
    fun isPaymentDueOn(date: LocalDate): Boolean
    fun calcPay(): Payment
}

data class HourlyEmployee(
    override val id: Int,
    override val name: String,
    private val hourlyRate: Int,
    private val hours: Int
) : Employee {
    override fun isPaymentDueOn(date: LocalDate): Boolean {
        return date.dayOfWeek == DayOfWeek.FRIDAY
    }

    override fun calcPay(): Payment {
        return Payment(id, hourlyRate * hours)
    }
}

data class SalaryEmployee(override val id: Int, override val name: String) : Employee {
    override fun isPaymentDueOn(date: LocalDate): Boolean {
        return false
    }

    override fun calcPay(): Payment {
        TODO("Not yet implemented")
    }

}