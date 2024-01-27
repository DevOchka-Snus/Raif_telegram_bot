package com.raif.onlinecashier

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update

var bot_token= ""
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
        println("TOKEN: $token")
        return token
    }
    override fun getBotUsername(): String {
        println("GetName")
        return botName
    }

    override fun onUpdateReceived(update: Update) {
        if (update.hasMessage()) {
            val msg = update.message
            val reply = SendMessage(msg.chatId.toString(), msg.text)
            execute(reply)
        }
    }

    fun testfunc() {
        val send = SendMessage("472209097", "abn")
        execute(send)
    }

}