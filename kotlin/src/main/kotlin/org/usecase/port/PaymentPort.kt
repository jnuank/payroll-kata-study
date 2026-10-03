package org.example.org.usecase.port

import org.example.org.domain.Payment

interface PaymentPort {
    fun pay(payment: Payment)
}
