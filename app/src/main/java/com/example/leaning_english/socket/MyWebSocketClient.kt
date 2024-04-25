package com.example.leaning_english.socket

import android.util.Log
import com.example.leaning_english.ConstantData
import com.example.leaning_english.database.GsonInstance
import com.example.leaning_english.database.entity.Meanings
import com.example.leaning_english.database.entity.Translation
import com.example.leaning_english.json.Words
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.java_websocket.client.WebSocketClient
import org.java_websocket.enums.ReadyState
import org.java_websocket.exceptions.WebsocketNotConnectedException
import org.java_websocket.handshake.ServerHandshake

class MyWebSocketClient(val url: String) : WebSocketClient(java.net.URI(url)) {

    override fun onOpen(handshakedata: ServerHandshake?) {
        println("WebSocket已连接")
        // 连接建立成功后可在此处发送初始化信息
    }

    override fun onMessage(message: String?) {
        println("接收到服务器消息: $message")
        // 在此处处理来自服务器的消息
    }

    override fun onClose(code: Int, reason: String?, remote: Boolean) {
        println("WebSocket已断开连接，原因：$reason")
    }

    override fun onError(ex: Exception?) {
        println("WebSocket发生错误: ${ex?.message}")
    }

    companion object {
        var client: MyWebSocketClient? = null

        @JvmStatic
        fun main(args: Array<String>) {
            client?.connectBlocking()
            val word = Words(1, "hello", "h", "h", Translation(arrayListOf(
                Meanings("n.",
                arrayListOf("你好"))
            ))
            )
            var t = 10
            while (t-- > 0) {
                client?.sendMessage(GsonInstance().INSTANCE?.gson?.toJson(word)!!)
            }
        }
    }

    fun checkAndReconnect() {
        when (readyState) {
            ReadyState.NOT_YET_CONNECTED -> {
                // 尝试连接
                Log.d("WebSocket", "尝试连接")
                connect()
            }
            ReadyState.CLOSING, ReadyState.CLOSED -> {
                // 尝试重连
                Log.d("WebSocket", "尝试重连")
                reconnectBlocking()
            }
            else -> {
                // WebSocket是打开状态，无需重连
                Log.d("WebSocket", "WebSocket已打开")
            }
        }
    }

    // 发送消息到服务器
    fun sendMessage(message: String) {
        try {
            this.send(message)
        } catch (e: WebsocketNotConnectedException) {
            println("WebSocket未连接，无法发送消息")
        }
    }
}



