package com.raif.paymentapi.domain.dto

import jakarta.validation.constraints.NotNull
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.validation.annotation.Validated
import java.math.BigDecimal

@Validated
data class OrderDto(
    @field:NotNull(message = "amount must be not null")
    var amount: BigDecimal,
    val qrId: String?,
    val id: String?,
    val comment: String?,
    @field:DateTimeFormat(pattern = "YYYY-MM-DD ТHH24:MM:SS±HH:MM / +nM / +nm")
    val expirationDate: String?
)
