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




val hourlyCsv = """
id,名前,時給,週時間
1,Alice,2000,40
2,Bob,2000,20
""".trimIndent()

val salariedCsv = """
id,名前,月給
3,Cathy,500000
""".trimIndent()

val commissionedCsv = """
id,名前,基本給,歩合率,売上
4,Donald,200000,10,500000
""".trimIndent()

val payTypeCsv = """
id,type
1,bank
2,home
3,office
4,bank
""".trimIndent()

val bankCsv = """
id,bank
1,xxxBank
4,yyyBank
""".trimIndent()

val addressCsv = """
id,address
2,saitama
""".trimIndent()

val calculatorCsv = """
id,calculator-name
3,nakajima
""".trimIndent()