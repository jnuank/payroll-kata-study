package org.domain

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll
import org.example.org.domain.HourlyEmployee
import org.example.org.domain.Payment
import java.time.LocalDate

class HourlyEmployeeTest : StringSpec({

    lateinit var john : HourlyEmployee

    beforeEach {
        john = HourlyEmployee(1, "John", 1000, 20)
    }

    "給与は時間×働いた時間" {
        john.calcPay() shouldBe Payment(employeeId = 1, 20_000)
    }

    "金曜日だけが支払日" {
        val friday = LocalDate.of(2026, 10, 2)
        checkAll(Arb.int(-1000..1000)) { weeks ->
            val paymentDue = friday.plusWeeks(weeks.toLong())

            john.isPaymentDueOn(paymentDue) shouldBe true

            for (offset in 1L..6L){
                john.isPaymentDueOn(paymentDue.plusDays(offset)) shouldBe false
            }

        }
    }
})
