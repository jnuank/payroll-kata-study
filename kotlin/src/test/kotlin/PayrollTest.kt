import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import org.example.org.domain.Employee
import org.example.org.domain.HourlyEmployee
import org.example.org.domain.Payment
import org.example.org.usecase.PayrollUsecase
import org.example.org.domain.SalaryEmployee
import org.example.org.usecase.port.EmployeePort
import org.example.org.usecase.port.PayRollEventPort
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

class PayrollTest : FreeSpec({

    fun payrollWith(employees: List<Employee>): Triple<PayrollUsecase, MockPaymentGateway, MockPayrollEventPort> {
        val employeePortMock = StubEmployeeGateway(employees)
        val payrollEventPortMock = MockPayrollEventPort()
        val paymentPort = MockPaymentGateway()
        val usecase = PayrollUsecase(employeePortMock, payrollEventPortMock, paymentPort)
        return Triple(usecase, paymentPort, payrollEventPortMock)
    }

    "時間給" - {
        "毎週金曜に支払われる" {
            val (usecase, payrollPort, payrollEventPortMock) = payrollWith(listOf(
                HourlyEmployee(1, "John", 1, 1),
                SalaryEmployee(2, "Mary"),
            ))

            usecase.execute(LocalDate.of(2026, 10, 2))

            payrollEventPortMock.events() shouldBe listOf(
                HourlyEmployee(1, "John", 1, 1),
            )

            payrollEventPortMock.calledCount shouldBe 1

        }

        "金曜じゃなければ支払われない" {
            val (usecase, payrollPort, payrollEventPortMock) = payrollWith(listOf(
                HourlyEmployee(1, "John", 1, 1),
                SalaryEmployee(2, "Mary"),
            ))

            usecase.execute(LocalDate.of(2026, 10, 1))

            payrollEventPortMock.events() shouldBe emptyList()

            payrollEventPortMock.calledCount shouldBe 0
        }

        "時給と時間で計算して、送る" {
            val (usecase, payrollPort, payrollEventPortMock) = payrollWith(listOf(
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

class MockPayrollEventPort: PayRollEventPort {
    private val employees = mutableListOf<Employee>()
    var calledCount: Int = 0

    override fun payed(employee: Employee) {
        employees.add(employee)
        calledCount++
    }

    fun events(): List<Employee> {
        return employees
    }

}

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
}