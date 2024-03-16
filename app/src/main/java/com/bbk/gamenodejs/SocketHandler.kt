package com.bbk.gamenodejs

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

class SocketHandler {

    private var socket: Socket? = null

    private val _onNewScore = MutableLiveData<Score>()
    private val _onNewScore2 = MutableLiveData<Score>()
    private val _onLedStatus = MutableLiveData<LedStatus>()

    val onNewScore: LiveData<Score> get() = _onNewScore
    val onNewScore2: LiveData<Score> get() = _onNewScore2
    val onLedStatus: LiveData<LedStatus> get() = _onLedStatus

    init {
        try {
            socket = IO.socket(SOCKET_URL)
            socket?.connect()

            registerOnNewScore()
            registerOnNewScore2()
            registerOnLedStatus()
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
    }

    private fun registerOnNewScore() {
        socket?.on(MESSAGE_KEYS.BROADCAST) { args ->
            args?.let { d ->
                if (d.isNotEmpty()) {
                    val data = d[0]
                    Log.d("DATADEBUG", "$data")
                    if (data.toString().isNotEmpty()) {
                        val score = Gson().fromJson(data.toString(), Score::class.java)
                        _onNewScore.postValue(score)
                    }
                }
            }
        }
    }

    private fun registerOnNewScore2() {
        socket?.on(MESSAGE_KEYS.BROADCAST2) { args ->
            args?.let { d ->
                if (d.isNotEmpty()) {
                    val data = d[0]
                    Log.d("DATADEBUG2", "$data")
                    if (data.toString().isNotEmpty()) {
                        val score = Gson().fromJson(data.toString(), Score::class.java)
                        _onNewScore2.postValue(score)
                    }
                }
            }
        }
    }

    private fun registerOnLedStatus() {
        socket?.on(MESSAGE_KEYS.LED_STATUS) { args ->
            args?.let { d ->
                if (d.isNotEmpty()) {
                    val data = d[0]
                    Log.d("DATADEBUG3", "$data")
                    if (data.toString().isNotEmpty()) {
                        val ledStatus = Gson().fromJson(data.toString(), LedStatus::class.java)
                        _onLedStatus.postValue(ledStatus)
                    }
                }
            }
        }
    }

    fun disconnectSocket() {
        socket?.disconnect()
        socket?.off()
    }

    fun emitScore(score: Score) {
        val jsonStr = Gson().toJson(score, Score::class.java)
        socket?.emit(MESSAGE_KEYS.NEW_MESSAGE, jsonStr)
    }

    fun emitScore2(score: Score) {
        val jsonStr = Gson().toJson(score, Score::class.java)
        socket?.emit(MESSAGE_KEYS.NEW_MESSAGE2, jsonStr)
    }

    private object MESSAGE_KEYS {
        const val NEW_MESSAGE = "new_message"
        const val BROADCAST = "broadcast"

        const val NEW_MESSAGE2 = "new_message2"
        const val BROADCAST2 = "broadcast2"

        const val LED_STATUS = "led_status"
    }

    companion object {
        private const val SOCKET_URL = "https://game-node-js-5412cbf208fc.herokuapp.com"
//        private const val SOCKET_URL = "http://192.168.1.7:3000/"
//        private const val SOCKET_URL = "http://localhost:3000/"
    }
}