package client

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import java.util.concurrent.TimeUnit
import kotlin.system.exitProcess

fun main() {
    println("Connecting...")
    val websocket = getWebSocket()
    Thread.sleep(2000)
    while (true) {
        println("Input message or command Exit ")
        when (val msg = readLine()!!) {
            "Exit" -> {
                websocket.close(1000, "Exit")
                exitProcess(0)
            }
            else -> {
                websocket.send(msg)
                Thread.sleep(1500)
                continue
            }
        }
    }
}

fun getWebSocket(): WebSocket {
    val client = OkHttpClient.Builder()
        .readTimeout(1, TimeUnit.SECONDS)
        .build()
    val wsURL = "ws://127.0.0.1:8885"
    val request = Request.Builder()
        .url(wsURL)
        .build()
    val listener = EchoWebSocketListener()
    return client.newWebSocket(request, listener)
}