package org.usecase

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import org.example.org.domain.Employee
import org.example.org.domain.HourlyEmployee
import org.example.org.domain.Payment
import org.example.org.domain.SalaryEmployee
import org.example.org.usecase.PayrollUsecase
import org.example.org.usecase.port.EmployeePort
import org.example.org.usecase.port.PaymentPort
import java.time.LocalDate

// 時間給
//  時間×時給
//  支払うタイミング：週末の金曜

// 月給
//  月給固定
//  支払うタイミング：月末

// 歩合制
//  基本給 + (売上 * 歩合率）
//  支払うタイミング：隔週金曜

// payroll実行 date
// DBからEmployee取得
// 対象かどうか
// 計算
// 給与支払い
// 実行ログ

// payrollのusecaseを見て、社員の契約によって判断をする
// その社員が、支払い対象だったら、計算をして、支払う
// 社員の属性は期にするけど、支払い処理を行うユースケースにおいては、支払い対象かどうか・支払いの金額計算・送るの3点



class PayrollTest : FreeSpec({

    fun payrollWith(employees: List<Employee>): Pair<PayrollUsecase, MockPaymentGateway> {
        val employeePortMock = StubEmployeeGateway(employees)
        val paymentPort = MockPaymentGateway()
        val usecase = PayrollUsecase(employeePortMock, paymentPort)
        return usecase to paymentPort
    }

    "時間給" - {
        "金曜は時間給の従業員が支払い対象になる" {
            val john = HourlyEmployee(1, "John", 1000, 20)
            val mary = SalaryEmployee(2, "Mary")
            val friday = LocalDate.of(2026, 10, 2)

            val (usecase, payrollPort) = payrollWith(listOf(
                john, mary
            ))

            usecase.execute(friday)

            payrollPort.paidEmployeeIds shouldBe listOf(john.id)

        }

        "金曜じゃなければ支払われない" {
            val (usecase, payrollPort) = payrollWith(listOf(
                HourlyEmployee(1, "John", 1, 1),
                SalaryEmployee(2, "Mary"),
            ))

            usecase.execute(LocalDate.of(2026, 10, 1))

            payrollPort.paidEmployeeIds shouldBe emptyList()
        }

        "時給と時間で計算して、送る" {
            val (usecase, payrollPort) = payrollWith(listOf(
                HourlyEmployee(1, "John", 1000, 20),
                HourlyEmployee(2, "Doe", 2000, 20),
            ))

            usecase.execute(LocalDate.of(2026, 10, 2))

            payrollPort.payments shouldBe listOf(
                Payment(1, 20000),
                Payment(2, 40000),
            )
        }

    }

    "月給" - {
        "月末に支払われる" {

        }
    }
})


class StubEmployeeGateway(
    val employees: List<Employee>
): EmployeePort {
    override fun allEmployees(): List<Employee> = employees
}

class MockPaymentGateway: PaymentPort {
     val payments = mutableListOf<Payment>()

    override fun pay(payment: Payment) {
        payments += payment
    }

    val paidEmployeeIds: List<Int> get() = payments.map { it.employeeId }
}