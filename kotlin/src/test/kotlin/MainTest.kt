import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

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
        "時給2000円の人が40時間働いたとき" {
            true shouldBe false
        }
    }
})
