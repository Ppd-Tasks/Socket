package com.example.websocket.manager

import android.util.Log
import com.example.websocket.model.BitCoin
import com.example.websocket.model.Currency
import com.example.websocket.model.DataSend
import com.google.gson.Gson
import okhttp3.*
import okio.ByteString

class WebSocketBtc{
    lateinit var mWebSocket:WebSocket
     var gson = Gson()
     lateinit var socketListener: SocketListener

     fun connectToSocket() {
        val client = OkHttpClient()

        val request: Request = Request.Builder().url("wss://ws.bitstamp.net").build()
        client.newWebSocket(request,object : WebSocketListener(){
            override fun onOpen(webSocket: WebSocket, response: Response) {
                mWebSocket = webSocket

                webSocket.send(gson.toJson(Currency("bts:subscribe",DataSend("live_trades_btcusd"))))
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d("@@@", "onMessage: $text")
                val bitCoin = gson.fromJson(text,BitCoin::class.java)
                socketListener.onSuccess(bitCoin)
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                Log.d("@@@", "onMessage bytes: $bytes")
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                Log.d("@@@", "onClosing: $code / $reason")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.d("@@@", "onFailure: ${t.message}")
                socketListener.onFailure(t.localizedMessage)
            }
        })
        client.dispatcher.executorService.shutdown()

    }

    fun socketListener(socketListener: SocketListener) {
        this.socketListener = socketListener
    }
}

interface SocketListener{
    fun onSuccess(bitCoin: BitCoin)
    fun onFailure(message:String)
}