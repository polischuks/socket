package client

import okhttp3.*
import java.util.Scanner
import java.util.concurrent.TimeUnit
import kotlin.system.exitProcess

fun main() {
    val websockets = connect()
    println("Client started")
    val sc = Scanner(System.`in`)

    while (true) {
        println("Input message or exit ")
        val msg = sc.next()
        when (msg) {
            "exit" -> {
                exitProcess(0)
            }
            else -> {
                websockets.send(msg)
                continue
            }
        }
    }
}

fun connect(): WebSocket {

    val client = OkHttpClient.Builder()
        .readTimeout(3, TimeUnit.SECONDS)
        .build()
    val wsURN = "ws://127.0.0.1:8885"
    val request = Request.Builder()
        .url(wsURN)
        .build()
    val listener = EchoWebSocketListener()
    return client.newWebSocket(request, listener)
}