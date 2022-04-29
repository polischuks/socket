package server

fun main() {
    val DEFAULT_PORT = 8885
    val server = EchoWebSocketServer(DEFAULT_PORT)
    server.start()

}