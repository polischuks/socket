package server

import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer
import java.net.InetSocketAddress

class EchoWebSocketServer(port: Int) : WebSocketServer(InetSocketAddress(port)) {
    private var nextId = 0

    override fun onOpen(conn: WebSocket?, handshake: ClientHandshake?) {
        val id = synchronized(this) {
            ++nextId
        }
        val message = "#$id connected"
        conn?.send(message)
        println(message)
        conn?.setAttachment(id)
    }

    override fun onClose(conn: WebSocket?, code: Int, reason: String?, remote: Boolean) {

    }

    override fun onMessage(conn: WebSocket?, message: String?) {
        val toClientMessage = "${conn?.getAttachment<Int>()}: $message"
        conn?.send("Server resend you message => $toClientMessage")
    }

    override fun onError(conn: WebSocket?, ex: Exception?) {
        println("error: $ex")
    }

    override fun onStart() {
        println("Server started on port $port")
    }
}
