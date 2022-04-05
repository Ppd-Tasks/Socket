package com.example.websocket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.example.websocket.databinding.ActivityMainBinding
import com.example.websocket.manager.SocketListener
import com.example.websocket.manager.WebSocketBtc
import com.example.websocket.model.BitCoin
import okhttp3.*
import okio.ByteString

class MainActivity : AppCompatActivity() {
    lateinit var webSocketBts: WebSocketBtc
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        webSocketBts = WebSocketBtc()

        initViews()
    }

    private fun initViews() {
        binding.btnConnect.setOnClickListener {
            webSocketBts.connectToSocket()
        }

        webSocketBts.socketListener(object : SocketListener{
            override fun onSuccess(bitCoin: BitCoin) {
                runOnUiThread {
                    if (bitCoin.event == "bts:subscription_succeeded"){
                        binding.btnConnect.text = "Successfully Connected,Wait a moment"
                    }else{
                        binding.btnBitcoin.text = "1 BTC"
                        binding. btnUsd.text = "${bitCoin.data.price_str}$"
                    }
                }
            }

            override fun onFailure(message: String) {
                runOnUiThread {
                    binding.btnConnect.text = message
                }
            }

        })
    }




}