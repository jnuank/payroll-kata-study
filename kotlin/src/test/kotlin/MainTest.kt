import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import org.example.Employee
import org.example.PayrollUsecase
import org.example.org.example.EmployeePort
import org.example.org.example.PayRollEventPort

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

class MainTest : FreeSpec({
    lateinit var portMock: MockEmployeeGateway
    lateinit var payrollEventPortMock: MockPayrollEventPort
    lateinit var usecase: PayrollUsecase

    beforeEach {
        portMock = MockEmployeeGateway()
        payrollEventPortMock = MockPayrollEventPort()
        usecase = PayrollUsecase(portMock, payrollEventPortMock)
    }

    "生成" {

        usecase.execute()

        payrollEventPortMock.getEmployee() shouldBe listOf(
            Employee(1, "John"),
            Employee(2, "Mary"),
        )
        payrollEventPortMock.calledCount shouldBe 2
    }

    "時間給" - {
        "毎週金曜に支払われる" {

        }
//
//        "Aliceは時給2000円で40時間働いた" {
//            calculatePay().contains("Alice" to 80000) shouldBe true
//        }
//
//        "Bobは時給2000円で20時間働いた" {
//            calculatePay().contains("Bob" to 40000) shouldBe true
//        }



    }
})

class MockPayrollEventPort: PayRollEventPort {
    private val employees = mutableListOf<Employee>()
    var calledCount: Int = 0

    override fun payed(employee: Employee) {
        employees.add(employee)
        calledCount++
    }

    fun getEmployee(): List<Employee> {
        return employees
    }

}

class MockEmployeeGateway: EmployeePort {
    override fun allEmployees(): List<Employee> {
        return listOf(
            Employee(1, "John"),
            Employee(2, "Mary"),
        )
    }
}
