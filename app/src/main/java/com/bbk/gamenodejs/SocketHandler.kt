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
    val onNewScore: LiveData<Score> get() = _onNewScore

    init {
        try {
            socket = IO.socket(SOCKET_URL)
            socket?.connect()

            registerOnNewScore()
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
    }

    private fun registerOnNewScore() {
        socket?.on(SCORE_KEYS.BROADCAST) { args ->
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

    fun disconnectSocket() {
        socket?.disconnect()
        socket?.off()
    }

    fun emitScore(score: Score) {
        val jsonStr = Gson().toJson(score, Score::class.java)
        socket?.emit(SCORE_KEYS.NEW_MESSAGE, jsonStr)
    }

    private object SCORE_KEYS {
        const val NEW_MESSAGE = "new_message"
        const val BROADCAST = "broadcast"
    }

    companion object {
        private const val SOCKET_URL = "http://192.168.1.7:3000/"
//        private const val SOCKET_URL = "http://localhost:3000/"
    }
}