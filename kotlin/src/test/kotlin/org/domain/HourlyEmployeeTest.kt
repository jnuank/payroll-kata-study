package org.domain

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import org.example.org.domain.HourlyEmployee
import org.example.org.domain.Payment
import java.time.LocalDate

class HourlyEmployeeTest : StringSpec({

    lateinit var john : HourlyEmployee

    beforeEach {
        john = HourlyEmployee(1, "John", 1000, 20)
    }

    "毎週金曜は支払日" {
        val friday = LocalDate.of(2026, 10, 2)

        john.isPaymentDueOn(friday) shouldBe true
    }

    "金曜日ではなかったら支払い対象ではない" {
        val thursday = LocalDate.of(2026, 10, 1)
        john.isPaymentDueOn(thursday) shouldBe false
    }

    "給与は時間×働いた時間" {
        john.calcPay() shouldBe Payment(employeeId = 1, 20_000)
    }
})
