import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import org.example.payroll

// 時間給
//  時間×時給
//  支払うタイミング：週末の金曜

// 月給
//  月給固定
//  支払うタイミング：月末

// 歩合制
//  基本給 + (売上 * 歩合率）
//  支払うタイミング：隔週金曜

class MainTest : FreeSpec({
    "時間給" - {
        "Aliceは時給2000円で40時間働いた" {
            payroll().contains("Alice" to 80000) shouldBe true
        }

        "Bobは時給2000円で20時間働いた" {
            payroll().contains("Bob" to 40000) shouldBe true
        }
    }
})
