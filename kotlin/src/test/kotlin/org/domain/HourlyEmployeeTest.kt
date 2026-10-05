package org.domain

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.localDate
import io.kotest.property.checkAll
import org.example.org.domain.HourlyEmployee
import org.example.org.domain.Payment
import java.time.DayOfWeek
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
        checkAll(Arb.localDate()) { date ->
            println("$date: ${date.dayOfWeek}")
            john.isPaymentDueOn(date) shouldBe (date.dayOfWeek == DayOfWeek.FRIDAY)
        }
    }
})
