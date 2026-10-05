package org.domain

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import org.example.org.domain.Payment
import org.example.org.domain.SalaryEmployee
import java.time.LocalDate

class SalaryEmployeeTest : StringSpec({

    "給与は固定" {
        val john = SalaryEmployee(1, "John", 300_000)
        john.calcPay() shouldBe Payment(1, 300_000)

        val mary = SalaryEmployee(2, "Mary", 500_000)
        mary.calcPay() shouldBe Payment(2, 500_000)
    }

    "月末は支払日" {
        val john = SalaryEmployee(1, "John", 300_000)
        john.isPaymentDueOn(LocalDate.of(2026, 10, 31)) shouldBe true
    }

    "月末以外は支払い日じゃない" {
        val john = SalaryEmployee(1, "John", 300_000)
        john.isPaymentDueOn(LocalDate.of(2026, 10, 1)) shouldBe false
    }

})
