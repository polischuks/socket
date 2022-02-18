package server

import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer
import org.java_websocket.WebSocket
import java.lang.Exception
import java.net.InetSocketAddress

class WebSocketServer(port: Int) : WebSocketServer(InetSocketAddress(port)) {

    private var nextId = 0

    override fun onOpen(conn: WebSocket?, handshake: ClientHandshake?) {
        val id = synchronized(this) {
            ++nextId
        }
        val message = "#$id connected"
        sendToAll(message)
        println(message)
        conn?.setAttachment(id)
    }

    override fun onClose(conn: WebSocket?, code: Int, reason: String?, remote: Boolean) {
        val message = "#${conn?.getAttachment<Int>()} disconnected"
        sendToAll(message)
        println(message)
    }

    override fun onMessage(conn: WebSocket?, message: String?) {
        val toClientMessage = "${conn?.getAttachment<Int>()}: $message"
        println("Input msg ${toClientMessage}")
        sendToAll(toClientMessage)
    }

    override fun onStart() {
        println("server starts on port $port")
    }

    override fun onError(conn: WebSocket?, e: Exception?) {
        println("error: $e")
    }

    private fun sendToAll(message: String) {
        connections.filter(WebSocket::isOpen).forEach { openedConnection ->
            try {
                openedConnection.send(message)
            } catch (t: Throwable) {
            }
        }
    }
}

object Main {

    @JvmStatic
    fun main(vararg args: String) {
        val DEFAULT_PORT = 8885
        val server = WebSocketServer(DEFAULT_PORT).apply {
            isReuseAddr = true
            start()
        }

        Runtime.getRuntime().addShutdownHook(object : Thread() {
            override fun run() {
                server.stop()
            }
        })
    }
}