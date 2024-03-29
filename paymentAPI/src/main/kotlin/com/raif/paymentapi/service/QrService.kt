package com.raif.paymentapi.service

import com.raif.paymentapi.domain.dto.QrDynamicDto
import com.raif.paymentapi.domain.dto.QrStaticDto
import com.raif.paymentapi.domain.dto.QrVariableDto
import com.raif.paymentapi.domain.dto.SbpClientDto
import com.raif.paymentapi.domain.model.PaymentInformation
import com.raif.paymentapi.domain.model.QrInformation
import raiffeisen.sbp.sdk.model.`in`.QRUrl

interface QrService {
    fun registerDynamicQr(qrDynamicDto: QrDynamicDto): QRUrl
    fun registerStaticQr(qrStaticDto: QrStaticDto): QRUrl
    fun registerVariableQr(qrVariableDto: QrVariableDto): QRUrl
    fun getQrInfo(qrId: String, sbpClientDto: SbpClientDto): QrInformation
    fun getPaymentInfo(qrId: String, sbpClientDto: SbpClientDto): PaymentInformation
}