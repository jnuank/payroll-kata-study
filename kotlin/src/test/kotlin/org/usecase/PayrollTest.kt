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

    "支払い対象である従業員は、給与を支払う" {
        val john = MockEmployee(1, "John", true, 100_000)
        val mary = MockEmployee(2, "Mary", false, 200)

        val (usecase, paymentPort) = payrollWith(listOf(john, mary))

        usecase.execute(LocalDate.now())

        paymentPort.payments shouldBe listOf(
            Payment(1, 100_000)
        )
    }
})

data class MockEmployee(
    override val id: Int,
    override val name: String,
    private val isPayment: Boolean,
    private val payment: Int
) : Employee {
    override fun isPaymentDueOn(date: LocalDate): Boolean = isPayment

    override fun calcPay(): Payment = Payment(id, payment)
}

class StubEmployeeGateway(
    val employees: List<Employee>
) : EmployeePort {
    override fun allEmployees(): List<Employee> = employees
}

class MockPaymentGateway : PaymentPort {
    val payments = mutableListOf<Payment>()

    override fun pay(payment: Payment) {
        payments += payment
    }
}
