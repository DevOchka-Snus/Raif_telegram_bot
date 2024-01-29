package com.raif.onlinecashier

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto
import org.telegram.telegrambots.meta.api.objects.InputFile
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.Message
import java.io.FileInputStream


var bot_token = ""


fun Double.format(digits: Int) = "%.${digits}f".format(this)


fun currentCommandCreate(msg: Message): Boolean {
    val price: Double? = msg.text.toDoubleOrNull()
    return price != null
}

fun parseCommand(msg: Message): MutableList<String> {
    if (msg.text.isEmpty()) {
        return mutableListOf("")
    }
    val normalized = msg.text.replace("\n", " ").trim()
    val args = normalized.split(" ").toMutableList()
    args[0] = args[0].lowercase()
    return args
}

fun currentCommandRefund(msg: Message): Boolean {
    val command = parseCommand(msg)
    return (command[0] == "/refund" || command[0] == "/возврат") && (command.count() == 1)
}


@Service
class MyBot : TelegramLongPollingBot() {//TODO move bot token to constructor

    @Value("\${telegram.botName}")
    private val botName = ""

    @Value("\${telegram.botToken}")
    private var token = ""

    override fun getBotToken(): String {
        if (token != "") {
            bot_token = token
        } else {
            token = bot_token
        }
        return token
    }

    override fun getBotUsername(): String {
        return botName
    }

    override fun onUpdateReceived(update: Update) {
        if (update.hasMessage()) {
            val msg = update.message
            val chatId = msg.chatId.toString()
            if (currentCommandRefund(msg)) {
                if (!msg.isReply) {
                    sendMessageExecute(
                        chatId,
                        "Пожалуйста, отпраьте команду /refund *в ответ* на сообщение бота с qr кодом, оплату по которому нужно вернуть",
                        markdown = "MarkdownV2"
                    )
                    return
                }
                val repl = msg.replyToMessage

                if (repl.from.userName != botName) {
                    sendMessageExecute(
                        chatId,
                        "Пожалуйста, отпраьте команду /refund в ответ *на сообщение бота* с qr кодом, оплату по которому нужно вернуть",
                        markdown = "MarkdownV2"
                    )
                    return
                }
                if (!repl.hasPhoto() || repl.caption.slice(IntRange(0, 3)) != "qrId") {
                    sendMessageExecute(
                        chatId,
                        "Пожалуйста, отпраьте команду /refund в ответ на *сообщение* бота *с qr кодом*, оплату по которому нужно вернуть",
                        markdown = "MarkdownV2"
                    )
                    return
                }
                val qrId = repl.caption.split("\n")[0].split(" ")[1]
                val refundRes = refund(qrId, chatId)
                sendMessageExecute(chatId, refundRes, markdown = "MarkdownV2")

            } else if (currentCommandCreate(msg)) {
                val price: Double = msg.text.toDoubleOrNull() ?: 0.0
                if (price < 10) {
                    sendMessageExecute(chatId, "Минимальная сумма чека: 10 рублей")
                    return
                }
                val qr = generateQr(price, chatId)
                if (qr == null) {
                    sendMessageExecute(chatId, "Ошибка при создании qr")
                } else {
                    val replayMsg = "qrId: ${qr.qrId}\nPrice: ${price.format(2)}"
                    val replyTo = sendPhotoExecute(chatId, qr.qrUrl, replayMsg)
                    val threadWithRunnable = Thread(CheckPayment(chatId, qr.qrId, replyTo))
                    threadWithRunnable.start()
                }
            } else {
                sendMessageExecute(
                    chatId,
                    "Не понимаю, что вы имеете в виду. Пожалуйста, воспользуйтесь командой /help"
                )
            }

        }
    }

    fun sendMessageExecute(chatId: String, text: String, markdown: String? = null, replyTo: Int? = null): Int {
        val send = SendMessage(chatId, text)
        if (markdown != null) {
            send.parseMode = markdown
        }
        if (replyTo != null) {
            send.replyToMessageId = replyTo
        }
        val e = execute(send)
        return e.messageId
    }

    fun sendPhotoExecute(chatId: String, url: String, text: String): Int {
        val send = SendPhoto(chatId, InputFile(url))
        send.caption = text
        val e = execute(send)
        return e.messageId
    }

    fun testfunc() {
        val send = SendMessage("472209097", "abn")
        execute(send)
    }

    fun notEnough() {

    }
}
