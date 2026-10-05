package org.usecase

import com.sun.org.apache.xalan.internal.lib.ExsltDatetime.date
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNot
import org.example.org.domain.Employee
import org.example.org.domain.Payment
import org.example.org.usecase.PayrollUsecase
import org.example.org.usecase.port.EmployeePort
import org.example.org.usecase.port.PaymentPort
import org.junit.jupiter.api.fail
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

    fun payrollWith(employees: List<Employee>): Pair<PayrollUsecase, RecordingPaymentGateway> {
        val employeePortMock = StubEmployeeGateway(employees)
        val paymentPort = RecordingPaymentGateway()
        val usecase = PayrollUsecase(employeePortMock, paymentPort)
        return usecase to paymentPort
    }

    "支払い対象である従業員は、給与を支払う" {
        val date = LocalDate.of(2026, 10, 1)
        val john = StubEmployee(1, "John", date, 100_000)
        val mary = StubEmployee(2, "Mary", date.plusDays(1), 200)
        val doe = StubEmployee(3, "Doe", date,12_000)

        val (usecase, paymentPort) = payrollWith(listOf(john, mary, doe))

        usecase.execute(date)

        paymentPort.payments shouldBe listOf(
            Payment(1, 100_000),
            Payment(3, 12_000)
        )
    }

    "支払い対象外の従業員は給与計算を行わない" {
        var calcPayCalls: Int = 0

        val employee = object: Employee {
            override val id = 1
            override val name = "Zod"
            override fun isPaymentDueOn(date: LocalDate) = false

            override fun calcPay(): Payment {
                calcPayCalls++
                return fail("呼ばれてはいけない")
            }
        }

        val (usecase, _) = payrollWith(listOf(employee))

        usecase.execute(LocalDate.of(2026, 10, 1))

        calcPayCalls shouldBe 0
    }
})

data class StubEmployee(
    override val id: Int,
    override val name: String,
    private val paymentDue: LocalDate,
    private val amount: Int
) : Employee {
    override fun isPaymentDueOn(date: LocalDate): Boolean = paymentDue == date

    override fun calcPay(): Payment = Payment(id, amount)
}

class StubEmployeeGateway(
    val employees: List<Employee>
) : EmployeePort {
    override fun allEmployees(): List<Employee> = employees
}

class RecordingPaymentGateway : PaymentPort {
    val payments = mutableListOf<Payment>()

    override fun pay(payment: Payment) {
        payments += payment
    }
}
