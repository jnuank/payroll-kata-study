package org.example.org.usecase

import org.example.org.usecase.port.EmployeePort
import org.example.org.usecase.port.PaymentPort
import java.time.LocalDate

class PayrollUsecase(private val port: EmployeePort,  private val paymentPort: PaymentPort) {
    fun execute(date: LocalDate) {
        val employees = port.allEmployees()

        employees.forEach { employee ->
            if(employee.isPaymentDueOn(date)) {
                val pay = employee.calcPay()
                paymentPort.pay(pay)
            }
        }
    }
}
